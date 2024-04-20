import csv
import psycopg2
import pandas as pd
from psycopg2 import sql
from datetime import datetime
from DataCentricAI.src.constants.constants import USAHousingCsvPath, USAHousingTableName

# Database configuration
conn_params = {
    'dbname': 'postgres',
    'user': 'Giorgia2',
    'password': 'postgres',
    'host': 'localhost',
    'port': '5432'
}

# Connection to db
conn = psycopg2.connect(**conn_params)

cur = conn.cursor()

USAHousingCsv = pd.read_csv(USAHousingCsvPath)

USAHousingCsv.columns = [c.replace('.', '') for c in USAHousingCsv.columns]
USAHousingCsv.columns = [c.replace(' ', '_') for c in USAHousingCsv.columns]
columns = ", ".join(
    [f"{column} VARCHAR" for column in USAHousingCsv.columns])  # Assuming all data types as VARCHAR for simplicity

create_table_query = sql.SQL("CREATE TABLE IF NOT EXISTS {} ({})").format(
    sql.Identifier(USAHousingTableName),
    sql.SQL(columns)
)
# Execute the SQL statement
cur.execute(create_table_query)

columns = ", ".join(
    [f"{column}" for column in USAHousingCsv.columns])  # Assuming all data types as VARCHAR for simplicity

# Prepare the INSERT INTO statement
insert_query = sql.SQL("INSERT INTO {} ({}) VALUES ({})").format(
    sql.Identifier(USAHousingTableName),
    sql.SQL(columns),
    sql.SQL(', ').join([sql.Placeholder()] * len(USAHousingCsv.columns))
)

# Insert data
for index, row in USAHousingCsv.iterrows():
    cur.execute(insert_query, tuple(row))

conn.commit()

cur.close()
conn.close()
