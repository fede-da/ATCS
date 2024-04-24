import psycopg2


# Function to connect to the database
def connect_to_db():
    conn = psycopg2.connect(
        dbname="postgres",
        user="Giorgia2",
        password="postgres",
        host="localhost",
        port="5432"
    )
    return conn


# Function to fetch data from the database based on a specified query
def fetch_data_from_db(conn, query):
    cur = conn.cursor()
    cur.execute(query)
    rows = cur.fetchall()
    cur.close()
    return rows
