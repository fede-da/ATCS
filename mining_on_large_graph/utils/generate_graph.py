import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt

# Carica i dati dal file Excel dei risultati
df_results = pd.read_excel('./data/partnerships_data.xlsx', engine='openpyxl')

print("Nomi delle colonne: ", df_results.columns)

G = nx.Graph()

# Aggiungi nodi agli streamer
for index, row in df_results.iterrows():
    G.add_node(row['numeric_id_1'])
    G.add_node(row['numeric_id_2'])

# Aggiungi archi per le partnership con attributi
for index, row in df_results.iterrows():
    G.add_edge(row['numeric_id_1'], row['numeric_id_2'], similarity=row['Follower similarity score'], partnership=row['Partnership'])

plt.figure(flgsize=(10,8))
nx.draw(G, with_labels=True, node_size=50, font_size=8)
plt.show()

communities = nx.algorithms.community.greedy_modularity_communities(G)
print("Comunita trovate:", communities)

degree_centrality = nx.algorithms.centrality.degree_centrality(G)
print("Centralità di grado:", degree_centrality)

nx.write_gpickle(G, './data/streamer_graph.gpickle')
