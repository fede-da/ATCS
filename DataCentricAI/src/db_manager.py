import psycopg2


# Function to connect to the database
def connect_to_db_main():
    conn = psycopg2.connect(
        dbname="postgres",
        user="postgres", #Giorgia2, postgres
        password="postgres",
        host="localhost",
        port="5432"
    )
    return conn


def connect_to_db_from_csv_to_db():
    return {
        'dbname': 'postgres',
        'user': 'postgres', #Giorgia2, postgres
        'password': 'postgres',
        'host': 'localhost',
        'port': '5432'
    }


# Function to fetch data from the database based on a specified query
def fetch_data_from_db(conn, query):
    cur = conn.cursor()
    cur.execute(query)
    rows = cur.fetchall()
    cur.close()
    return rows
