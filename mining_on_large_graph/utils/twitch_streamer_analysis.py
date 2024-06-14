import pandas as pd
import time
from Levenshtein import distance as levenshtein_distance
from concurrent.futures import ProcessPoolExecutor

class StreamerPartnershipAnalyzer:
    
    def __init__(self, features_file='./data/large_twitch_edges.csv', followers_file='./data/large_twitch_followers.csv'):
        print("Loading features and followers data...")
        start_time = time.time()
        self.df_features = pd.read_csv(features_file)
        self.df_followers = pd.read_csv(followers_file)
        self.followers_dict = self.df_followers.set_index('numeric_id').to_dict(orient='index')
        end_time = time.time()
        print(f"Data loaded successfully in {end_time - start_time:.2f} seconds.")
    
    def calculate_follower_similarity(self, follower1, follower2):
        return 1 / (abs(follower1 - follower2) + 1)
    
    def process_batch(self, batch):
        print(f"Processing batch with {len(batch)} rows...")
        results = []
        for index, row in batch.iterrows():
            numeric_id_1 = row['numeric_id_1']
            numeric_id_2 = row['numeric_id_2']

            info_1 = self.followers_dict.get(numeric_id_1)
            info_2 = self.followers_dict.get(numeric_id_2)

            if not info_1 or not info_2:
                results.append({
                    'Language similarity': '-',
                    'Follower similarity': '-',
                    'Language similarity score': '-',
                    'Follower similarity score': '-',
                    'Partnership': 0
                })
                continue
            
            language1 = info_1['language']
            language2 = info_2['language']
            followers1 = info_1['followers']
            followers2 = info_2['followers']
            
            if language1==language2: 
                language_similarity=1
            else :
                language_similarity=0
            follower_similarity = self.calculate_follower_similarity(followers1, followers2)
            
            results.append({
                'Language similarity': f"{language1}, {language2}",
                'Follower similarity': f"{followers1}, {followers2}",
                'Language similarity score': language_similarity,
                'Follower similarity score': follower_similarity,
                'Partnership': 1 if language_similarity == 1.0 and follower_similarity > 0.1 else 0
            })
        print(f"Finished processing batch with {len(batch)} rows.")
        return results
    
    def find_potential_partnerships(self, output_excel='./data/partnerships_data.xlsx', batch_size=10000):
        num_batches = len(self.df_features) // batch_size + 1
        print(f"Starting to process {len(self.df_features)} rows in {num_batches} batches...")

        all_results = []

        with ProcessPoolExecutor() as executor:
            futures = []
            for batch_start in range(0, len(self.df_features), batch_size):
                batch_end = min(batch_start + batch_size, len(self.df_features))
                batch = self.df_features.iloc[batch_start:batch_end]
                print(f"Submitting batch {batch_start // batch_size + 1} to executor...")
                future = executor.submit(self.process_batch, batch)
                futures.append(future)

            for future in futures:
                all_results.extend(future.result())
                print("Batch processed and results collected.")
                
        # Save results to multiple sheets if necessary
        max_rows_per_sheet = 1048576
        num_sheets = len(all_results) // max_rows_per_sheet + 1

        with pd.ExcelWriter(output_excel, engine='xlsxwriter') as writer:
            for i in range(num_sheets):
                start_row = i * max_rows_per_sheet
                end_row = min((i + 1) * max_rows_per_sheet, len(all_results))
                df_results = pd.DataFrame(all_results[start_row:end_row])
                df_results.to_excel(writer, sheet_name=f'Sheet{i+1}', index=False)
                print(f"Sheet {i+1} saved with rows from {start_row} to {end_row}")

        print(f"Results saved to {output_excel}")