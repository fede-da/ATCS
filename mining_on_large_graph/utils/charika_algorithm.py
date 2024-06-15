import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt

# Step 1: Load the graph data from CSV
# Make sure to provide the correct path to your 'large_twitch_edges.csv' file
edges_df = pd.read_csv('../data/large_twitch_edges.csv')  # Adjust the path as needed

# Step 2: Create a graph from the dataframe
G = nx.from_pandas_edgelist(edges_df, 'numeric_id_1', 'numeric_id_2')


# Step 3: Implement Charikar's Algorithm
def charikar_densest_subgraph(G):
    H = G.copy()
    max_density = 0
    best_subgraph = None

    while H.number_of_nodes() > 0:
        current_density = H.number_of_edges() / H.number_of_nodes()
        if current_density > max_density:
            max_density = current_density
            best_subgraph = H.copy()

        # Find the node with the minimum degree and remove it
        min_degree_node = min(H.degree, key=lambda x: x[1])[0]
        H.remove_node(min_degree_node)

    return best_subgraph, max_density


densest_subgraph, density = charikar_densest_subgraph(G)

# Step 4: Visualize the Densest Subgraph
plt.figure(figsize=(12, 8))
pos = nx.spring_layout(G)  # Positions for all nodes
nx.draw_networkx_nodes(G, pos, node_size=20, node_color='blue', alpha=0.5)
nx.draw_networkx_edges(G, pos, alpha=0.1)

# Highlight the densest subgraph
subgraph_pos = {node: pos[node] for node in densest_subgraph.nodes()}
nx.draw_networkx_nodes(densest_subgraph, subgraph_pos, node_size=30, node_color='red')
nx.draw_networkx_edges(densest_subgraph, subgraph_pos, edge_color='red', width=2)

plt.title('Densest Subgraph Visualization')
plt.show()

# Step 5: Output some results
print("Densest Subgraph Nodes:", densest_subgraph.nodes())
print("Density of the densest subgraph:", density)
