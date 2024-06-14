from mining_on_large_graph.utils.reader import Reader

nodes = Reader.read_csv_and_create_nodes()

Reader.update_followers(nodes)
sorted_list_nodes = Reader.get_top_10_nodes_by_followers(nodes)
Reader.print_all_nodes_in_list(sorted_list_nodes)

Reader.save_nodes_to_csv(nodes)