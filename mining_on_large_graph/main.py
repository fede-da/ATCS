from utils.reader import Reader
from utils.twitch_streamer_analysis import StreamerPartnershipAnalyzer

import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt

if __name__ == '__main__':
    nodes = Reader.read_csv_and_create_nodes()

    Reader.update_followers(nodes)
    sorted_list_nodes = Reader.get_top_10_nodes_by_followers(nodes)
    Reader.print_all_nodes_in_list(sorted_list_nodes)

    Reader.save_nodes_to_csv(nodes)

    
    analyzer = StreamerPartnershipAnalyzer()
    potential_partnerships=analyzer.find_potential_partnerships('./data/partnerships_data.xlsx')
    
    if potential_partnerships:
        print("Partnerships potenziali trovate:")
        for partner1, partner2 in potential_partnerships:
            print(f"Streamer {partner1} e Streamer {partner2}")
    else:
        print("Nessuna partnership potenziale trovata.")

    nodes_dict = Reader.read_csv_and_create_nodes('./data/large_twitch_features.csv')

    df_results = pd.read_excel('./data/partnerships_data.xlsx', engine = 'openpyxl')

    print("Generazione del grafo... ")

    G = nx.Graph()

    # Aggiungi nodi agli streamer
    for node_id, node in nodes_dict.items():
        G.add_node(node_id, label=node.language, followers=node.followers)

    print("Aggiunti nodi agli streamer ")

    # Aggiungi archi per le partnership con attributi
    for index, row in df_results.iterrows():
        G.add_edge(row['numeric_id_1'], row['numeric_id_2'], similarity=row['Follower similarity score'], partnership=row['Partnership'])

    print("Stampiamo il grafo... ")

    plt.figure(figsize=(12,12))
    nx.draw(G, with_labels=True, node_size=50, font_size=8)
    plt.show()

    #communities = nx.algorithms.community.greedy_modularity_communities(G)
    print("Comunita trovate:", communities)

    degree_centrality = nx.algorithms.centrality.degree_centrality(G)
    print("Centralità di grado:", degree_centrality)

    nx.write_gpickle(G, './data/streamer_graph.gpickle')
