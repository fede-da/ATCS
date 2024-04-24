## Requirements
Before reading this document, please ensure that the "USA_HOUSING" table already exists in your database. 
If not, run the `from_csv_to_db` script to create the "USA_HOUSING" table.

## Description

This SQL file `convert_data_type.sql` is used to modify the data types of columns in the "USA_HOUSING" table in a PostgreSQL database.

The file contains a series of `ALTER TABLE` statements to change the data types of specific columns in the "USA_HOUSING" table from their current types `VARCHAR` to `DOUBLE PRECISION`. The `USING` clause is used in each statement to ensure that the conversion is done properly.

## Usage

To use this script:

1. Ensure that you have access to the PostgreSQL database where the "USA_HOUSING" table is located.

2. Run the script in a PostgreSQL client or command-line tool. 

After executing the script, the specified columns in the "USA_HOUSING" table will be converted to `DOUBLE PRECISION` data type.