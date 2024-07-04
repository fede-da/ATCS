import os
import json

def process_json_files():
    mediated_schema = {}  # Initialize an empty dictionary to store keys and counts

    # Walk through all directories and files in the current directory
    for root, dirs, files in os.walk('.'):
        for file in files:
            if file.endswith('.json'):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, 'r') as json_file:
                        # Load JSON data from file
                        data = json.load(json_file)
                        # Iterate over keys in JSON object
                        for key in data.keys():
                            # Increment the count of the key in the mediated schema dictionary
                            if key in mediated_schema:
                                mediated_schema[key] += 1
                            else:
                                mediated_schema[key] = 1
                except json.JSONDecodeError:
                    print(f"Error decoding JSON from file: {file_path}")

    # Write the mediated schema dictionary to a JSON file
    with open('mediated_schema.json', 'w') as schema_file:
        json.dump(mediated_schema, schema_file, indent=4)

    print("Mediated schema has been saved to 'mediated_schema.json'.")

# Run the function
if __name__ == '__main__':
    process_json_files()