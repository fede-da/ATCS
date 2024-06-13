import csv

from mining_on_large_graph.models.node import Node


class Reader:

    @staticmethod
    def read_csv_and_create_nodes(file_path='./data/large_twitch_features.csv'):
        nodes = {}
        with open(file_path, 'r') as file:
            reader = csv.reader(file)
            next(reader)  # Skip the header
            for row in reader:
                if row:
                    node = Node(*row)
                    nodes[node.numeric_id] = node
        return nodes

    @staticmethod
    def print_all_nodes_in_dict(nodes_dict):
        for node_id, node in nodes_dict.items():
            print(f"Node ID: {node_id}, Node Data: {node}")

    @staticmethod
    def print_all_nodes_in_list(nodes_list):
        for node in nodes_list:
            print(f"Node ID: {node.numeric_id}, Node Data: {node}")

    @staticmethod
    def update_followers(nodes_dict: dict[int, Node], file_path='./data/large_twitch_edges.csv'):
        with open(file_path, 'r') as file:
            reader = csv.reader(file)
            next(reader)  # Skip the header
            for row in reader:
                if row:
                    primary_id = int(row[0])
                    if primary_id in nodes_dict:
                        nodes_dict[primary_id].followers += 1

    @staticmethod
    def get_top_10_nodes_by_followers(nodes_dict: dict[int, Node]):
        # Sort the nodes based on the 'follower' attribute in descending order
        sorted_nodes = sorted(nodes_dict.values(), key=lambda x: x.followers, reverse=True)
        return sorted_nodes[:10]
