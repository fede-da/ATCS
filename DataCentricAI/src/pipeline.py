import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression

from DataCentricAI.src.db_manager import fetch_data_from_db, connect_to_db_main


class Pipeline:
    # get data from datasource
    def __init__(self):
        self.lm = LinearRegression()
        self.conn = connect_to_db_main()
        self.output = []
        self.y_test = None
        self.y_train = None
        self.x_test = None
        self.x_train = None
        self.df = None

    def _fetch_data_from_postgresql(self):
        # Fetch data from the database
        data = fetch_data_from_db(self.conn, 'SELECT * FROM "USA_HOUSING"')
        self.df = pd.DataFrame(data)

    def _prepare_training_and_test_data(self):
        # Separate features (x) and target variable (y)
        x = self.df.iloc[:, :-2]  # All columns except the last two as input (X)
        y = self.df.iloc[:, -2]  # The penultimate column as target (y)

        # Split the data into training and testing sets
        self.x_train, self.x_test, self.y_train, self.y_test = train_test_split(x, y, test_size=0.4, random_state=101)

    def _get_model_and_train_it(self):
        # Perform linear regression
        self.lm.fit(self.x_train, self.y_train)

    def _predict_and_exec_query(self):
        predictions = self.lm.predict(self.x_test)
        # After prediction, perform the SQL query for each predicted price
        for prediction in predictions:
            price_lower = prediction - 10000
            price_upper = prediction + 10000
            query = f"""
                SELECT * FROM "USA_HOUSING"
                WHERE price BETWEEN {price_lower} AND {price_upper}
            """
            self.output.append(fetch_data_from_db(self.conn, query))
            print(f"Number of houses within $10,000 of predicted price {prediction}: {len(self.output)}")

    # validate status and run further operations if required
    def _validate_and_print_output(self):
        if (self.output is None) or len(self.output) == 0:
            raise Exception("Pipeline terminated abnormally")

    def exec(self):
        ok = True
        methods = [
            self._fetch_data_from_postgresql,
            self._prepare_training_and_test_data,
            self._get_model_and_train_it,
            self._predict_and_exec_query,
            self._validate_and_print_output
        ]
        for method in methods:
            try:
                method()
                print(f"Ok: {method.__name__}")
            except Exception as e:
                print(f"Exception raised in method: {method.__name__}: {str(e)}")
                # Special handling to deal with this situation
                ok = False
                break
        if ok:
            print("Pipeline terminated successfully")
        else:
            print("Pipeline terminated unsuccessfully")
        self.conn.close()

