import psycopg2
import pandas as pd
from psycopg2 import sql
from DataCentricAI.src.constants.constants import USAHousingCsvPath, USAHousingTableName
from db_manager import connect_to_db

# Database configuration
conn_params = connect_to_db()

# Connection to db
conn = psycopg2.connect(**conn_params)

cur = conn.cursor()

# Read CSV file into pandas DataFrame
USAHousingCsv = pd.read_csv(USAHousingCsvPath)

# Clean column names
USAHousingCsv.columns = [c.replace('.', '') for c in USAHousingCsv.columns]
USAHousingCsv.columns = [c.replace(' ', '_') for c in USAHousingCsv.columns]

# Prepare column definitions for table creation
columns = ", ".join(
    [f"{column} VARCHAR" for column in USAHousingCsv.columns])  # Assuming all data types as VARCHAR for simplicity

# SQL query to create table
create_table_query = sql.SQL("CREATE TABLE IF NOT EXISTS {} ({})").format(
    sql.Identifier(USAHousingTableName),
    sql.SQL(columns)
)
# Execute the SQL statement
cur.execute(create_table_query)

columns = ", ".join(
    [f"{column}" for column in USAHousingCsv.columns])  # Assuming all data types as VARCHAR for simplicity

# SQL query to insert data into the table
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
