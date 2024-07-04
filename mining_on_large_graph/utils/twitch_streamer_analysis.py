import pandas as pd
import csv
from concurrent.futures import ProcessPoolExecutor
import xlsxwriter
from datetime import datetime
from models.node import Node  # Importa la classe Node dal modulo node.py

class StreamerPartnershipAnalyzer:
    
    def __init__(self, edges_file='./data/large_twitch_edges.csv', features_file='./data/large_twitch_features.csv'):
        print("Loading edges and features data...")
        self.df_edges = pd.read_csv(edges_file)
        self.df_features = pd.read_csv(features_file)
        
        # Convert numeric_id to string
        self.df_edges['numeric_id_1'] = self.df_edges['numeric_id_1'].astype(str)
        self.df_edges['numeric_id_2'] = self.df_edges['numeric_id_2'].astype(str)
        self.df_features['numeric_id'] = self.df_features['numeric_id'].astype(str)
        
    @staticmethod
    def update_followers(file_path, nodes_dict):
        with open(file_path, 'r') as file:
            reader = csv.reader(file)
            next(reader)  # Skip the header
            for row in reader:
                if row:
                    primary_id = row[0]
                    if primary_id in nodes_dict:
                        nodes_dict[primary_id].followers += 1
    
    def calculate_follower_similarity(self, follower1, follower2):
        return 1 / (abs(follower1 - follower2) + 1)
    
    def process_batch(self, batch):
        results = []
        
        nodes_dict = {}
        for _, row in self.df_features.iterrows():
            node = Node(
                views=row['views'],
                mature=row['mature'],
                life_time=row['life_time'],
                created_at=row['created_at'],
                updated_at=row['updated_at'],
                numeric_id=row['numeric_id'],
                dead_account=row['dead_account'],
                language=row['language'],
                affiliate=row['affiliate']
            )
            nodes_dict[row['numeric_id']] = node
        
        for _, row in batch.iterrows():
            numeric_id_1 = row['numeric_id_1']
            numeric_id_2 = row['numeric_id_2']
            
            info_1 = nodes_dict.get(numeric_id_1)
            info_2 = nodes_dict.get(numeric_id_2)
            
            #if not info_1:
                #print(f"Missing data for numeric_id: {numeric_id_1}")
                #continue
            
            #if not info_2:
                #print(f"Missing data for numeric_id: {numeric_id_2}")
                #continue
            
            language1 = info_1.language
            language2 = info_2.language
            followers1 = info_1.followers
            followers2 = info_2.followers
            
            language_similarity = 1 if language1 == language2 else 0
            follower_similarity = self.calculate_follower_similarity(followers1, followers2)
            
            results.append({
                'numeric_id_1': numeric_id_1,
                'numeric_id_2': numeric_id_2,
                'Language similarity': f"{language1}, {language2}",
                'Follower similarity': f"{followers1}, {followers2}",
                'Language similarity score': language_similarity,
                'Follower similarity score': follower_similarity,
                'Partnership': 1 if language_similarity == 1 and follower_similarity > 0.0005 else 0
            })
        
        return results

    
    def find_potential_partnerships(self, output_excel='./data/partnerships_data.xlsx', batch_size=10000):
        self.update_followers('./data/large_twitch_edges.csv', self.df_features)

        num_batches = len(self.df_edges) // batch_size + 1
        all_results = []

        with ProcessPoolExecutor() as executor:
            futures = []
            for batch_start in range(0, len(self.df_edges), batch_size):
                batch_end = min(batch_start + batch_size, len(self.df_edges))
                batch = self.df_edges.iloc[batch_start:batch_end]
                future = executor.submit(self.process_batch, batch)
                futures.append(future)

            for future in futures:
                all_results.extend(future.result())
                
        max_rows_per_sheet = 1048576
        num_sheets = len(all_results) // max_rows_per_sheet + 1

        with pd.ExcelWriter(output_excel, engine='xlsxwriter') as writer:
            for i in range(num_sheets):
                start_row = i * max_rows_per_sheet
                end_row = min((i + 1) * max_rows_per_sheet, len(all_results))
                df_results = pd.DataFrame(all_results[start_row:end_row])
                df_results.to_excel(writer, sheet_name=f'Sheet{i+1}', index=False)

        print(f"Results saved to {output_excel}")
