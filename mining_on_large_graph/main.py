from utils.reader import Reader
from utils.twitch_streamer_analysis import StreamerPartnershipAnalyzer

if __name__ == '__main__':
    nodes = Reader.read_csv_and_create_nodes()

    Reader.update_followers(nodes)
    sorted_list_nodes = Reader.get_top_10_nodes_by_followers(nodes)
    Reader.print_all_nodes_in_list(sorted_list_nodes)

    Reader.save_nodes_to_csv(nodes)

    analyzer = StreamerPartnershipAnalyzer()
    potential_partnerships = analyzer.find_potential_partnerships()
    
    if potential_partnerships:
        print("Partnerships potenziali trovate:")
        for partner1, partner2 in potential_partnerships:
            print(f"Streamer {partner1} e Streamer {partner2}")
    else:
        print("Nessuna partnership potenziale trovata.")