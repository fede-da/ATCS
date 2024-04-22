import psycopg2


def connect_to_db():
    conn = psycopg2.connect(
        dbname="postgres",
        user="Giorgia2",
        password="postgres",
        host="localhost",
        port="5432"
    )
    return conn


def fetch_data_from_db(conn):
    cur = conn.cursor()
    cur.execute('SELECT * FROM "USA_HOUSING"')
    rows = cur.fetchall()
    cur.close()
    return rows
