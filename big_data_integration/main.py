import linktransformer as lt
import pandas as pd
import json

# Carica i dati CSV come DataFrame
df1 = pd.read_csv('./schema/df1.csv')
df2 = pd.read_csv('./schema/df2.csv')
df3 = pd.read_csv('./schema/df3.csv')

# Carica i campi da allineare dal file JSON
with open('./schema/tem_schema.json') as f:
    fields_to_align = json.load(f)

with open('./schema/tem_schema2.json') as g:
    fields_align = json.load(g)

# Funzione per unire i DataFrame sui campi specificati
def merge_on_fields(df1, df2, fields, model="all-MiniLM-L6-v2"):
    merged_df_list = []
    for field_group, columns in fields.items():
        for column in columns:
            if column in df1.columns and column in df2.columns:
                merged_df = lt.merge(df1, df2,
                                     merge_type='1:1',
                                     left_on=column,
                                     right_on=column,
                                     model=model
                                     )
                merged_df_list.append(merged_df)
    # Combina tutti i DataFrame uniti
    final_merged_df = pd.concat(merged_df_list, axis=1)
    final_merged_df = final_merged_df.loc[:, ~final_merged_df.columns.duplicated()]
    return final_merged_df

def merge_on_fields2(df1, df2, fields, model="all-MiniLM-L6-v2"):
    merged_df_list = []
    for field_group, columns in fields.items():
        for column in columns:
            if column in df1.columns and column in df2.columns:
                merged_df = lt.merge(df1, df2,
                                     merge_type='1:1',
                                     left_on=column,
                                     right_on=column,
                                     model=model,
                                     suffixes=('_z', '_j')
                                     )
                merged_df_list.append(merged_df)
    # Combina tutti i DataFrame uniti
    final_merged_df = pd.concat(merged_df_list, axis=1)
    final_merged_df = final_merged_df.loc[:, ~final_merged_df.columns.duplicated()]
    return final_merged_df


# Esegui il merge dei DataFrame utilizzando i campi specificati
merged_df = merge_on_fields(df1, df2, fields_to_align)

# Esegui il merge del risultato con il terzo DataFrame
final_merged_df = merge_on_fields2(merged_df, df3, fields_align)

# Salva i dati uniti in un nuovo file CSV
final_merged_df.to_csv('./schema/align_data.csv', index=False)
