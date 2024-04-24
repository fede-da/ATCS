import pandas as pd
import numpy as np
from sklearn import metrics
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from db_manager import fetch_data_from_db, connect_to_db


# Function to analyze data and perform queries
def analyze_and_query_data():
    conn = connect_to_db()

    # Fetch data from the database
    data = fetch_data_from_db(conn, 'SELECT * FROM "USA_HOUSING"')

    # Convert fetched data to DataFrame
    df = pd.DataFrame(data)

    # Separate features (x) and target variable (y)
    x = df.iloc[:, :-2]  # All columns except the last two as input (X)
    y = df.iloc[:, -2]   # The penultimate column as target (y)

    # Split the data into training and testing sets
    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.4, random_state=101)

    # Perform linear regression
    lm = LinearRegression()
    lm.fit(x_train, y_train)
    predictions = lm.predict(x_test)

    # Calculate evaluation metrics
    mae = metrics.mean_absolute_error(y_test, predictions)
    mse = metrics.mean_squared_error(y_test, predictions)
    rmse = np.sqrt(mse)

    # Print out evaluation metrics
    print('MAE:', mae)
    print('MSE:', mse)
    print('RMSE:', rmse)

    # After prediction, perform the SQL query for each predicted price
    for prediction in predictions:
        price_lower = prediction - 10000
        price_upper = prediction + 10000
        query = f"""
            SELECT * FROM "USA_HOUSING"
            WHERE price BETWEEN {price_lower} AND {price_upper}
        """
        selected_data = fetch_data_from_db(conn, query)
        print(f"Number of houses within $10,000 of predicted price {prediction}: {len(selected_data)}")

    conn.close()


def main():
    analyze_and_query_data()


if __name__ == "__main__":
    main()