## Description

This project consists of three Python scripts:

1. **db_manager.py**: This script contains functions to connect to a PostgreSQL database and fetch data from it.

2. **from_csv_to_db.py**: This script reads data from a CSV file and inserts it into a PostgreSQL database table. It cleans column names, creates the table if it doesn't exist, and inserts data row by row.

3. **main.py**: This is the main script of the project. It connects to the database, fetches data, performs linear regression analysis on the data, evaluates the model, and then queries the database based on the model's predictions.

## Usage

To use these scripts, follow these steps:

1. Ensure that you have PostgreSQL installed and running on your system.

2. Install the required Python packages by running `pip install -r requirements.txt`.

3. Modify the database connection parameters in `db_manager.py` if necessary.

4. Run `from_csv_to_db.py` to populate the database with data from the CSV file.

5. Run `main.py` to perform data analysis and queries based on the model's predictions.
