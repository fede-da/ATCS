import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt

# Carica i dati dal file Excel dei risultati
df_results = pd.read_excel('./data/partnerships_data.xlsx', engine='openpyxl')

G = nx.Graph()

# Aggiungi nodi agli streamer
for index, row in df_results.iterrows():
    G.add_node(row['StreamerID'])

# Aggiungi archi per le partnership con attributi
for index, row in df_results.iterrows():
    G.add_edge(row['StreamerID1'], row['StreamerID2'], similarity=row['Similarity'], partnership=row['Partnership'])

nx.draw(G, with_labels=True)
plt.show()

communities = nx.algorithms.community.greedy_modularity_communities(G)

degree_centrality = nx.algorithms.centrality.degree_centrality(G)

nx.write_gpickle(G, './data/streamer_graph.gpickle')
