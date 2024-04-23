import pandas as pd
from sqlalchemy import create_engine, text
from DataCentricAI.src.constants.constants import USAHousingCsvPath, USAHousingTableName

# Function to connect to the PostgreSQL database
def connect_to_db():
    connection_string = "postgresql://Giorgia2:postgres@localhost:5432/postgres"
    engine = create_engine(connection_string)
    return engine

def infer_sql_dtypes(df):
    dtype_mapping = {
        "object": "VARCHAR",
        "float64": "FLOAT",
        "int64": "INT",
        "datetime64[ns]": "TIMESTAMP",
    }
    sql_types = {column: dtype_mapping[str(df[column].dtype)] for column in df.columns}
    return sql_types

# Database configuration
engine = connect_to_db()

df = pd.read_csv(USAHousingCsvPath)

# Infer SQL data types
sql_types = infer_sql_dtypes(df)

df.columns = [c.replace('.', '') for c in df.columns]
df.columns = [c.replace(' ', '_') for c in df.columns]

create_table_command = f"CREATE TABLE {USAHousingTableName} ("
for column, dtype in sql_types.items():
    create_table_command += (f"{column.replace(' ', '_').lower()} {dtype}, ").replace('.', '')
create_table_command = create_table_command.strip(", ") + ");"

# Create the table in the database
with engine.connect() as connection:
    connection.execute(text(create_table_command))  # Use text to execute raw SQL

# Load data into the PostgreSQL table
df.to_sql(USAHousingTableName, engine, if_exists='replace', index=False, dtype=sql_types)

print(f"Table {USAHousingTableName} created and data inserted successfully.")