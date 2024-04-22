import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from sklearn import metrics
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from db_manager import fetch_data_from_db, connect_to_db


def analyze_data():
    conn = connect_to_db()
    data = fetch_data_from_db(conn)
    conn.close()

    # Convert data into a pandas DataFrame
    df = pd.DataFrame(data)

    # Convert numeric data to numeric type
    for column in df.columns:
        try:
            df[column] = pd.to_numeric(df[column])
        except ValueError:
            pass

    # Transform data into a suitable format for analysis
    X = df.iloc[:, :-2]  # All columns except the last two as input (X)
    y = df.iloc[:, -2]   # Penultimate column as output (y)

    # Split the data into training and testing sets
    x_train, x_test, y_train, y_test = train_test_split(X, y, test_size=0.4, random_state=101)

    # Perform linear regression
    lm = LinearRegression()
    lm.fit(x_train, y_train)
    predictions = lm.predict(x_test)

    # Calculate evaluation metrics
    mae = metrics.mean_absolute_error(y_test, predictions)
    mse = metrics.mean_squared_error(y_test, predictions)
    rmse = np.sqrt(mse)

    print('MAE:', mae)
    print('MSE:', mse)
    print('RMSE:', rmse)


analyze_data()
