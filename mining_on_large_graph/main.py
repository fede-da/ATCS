from mining_on_large_graph.utils.reader import Reader

nodes = Reader.read_csv_and_create_nodes()

Reader.update_followers(nodes)
Reader.print_all_nodes(nodes)
