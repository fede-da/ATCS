from linktransformer import LinkTransformer
import json

# Carica i dati JSON
with open('0.json') as f:
    data1 = json.load(f)

with open('1.json') as f:
    data2 = json.load(f)

# Carica i campi da allineare dal file JSON
with open('fields_to_align.json') as f:
    fields_to_align = json.load(f)

# Inizializza LinkTransformer con le configurazioni necessarie
link_transformer = LinkTransformer(fields=fields_to_align)

# Esegui il collegamento dei record
aligned_data = link_transformer.align([data1, data2])

# Salva i dati allineati in un nuovo file JSON
with open('aligned_data.json', 'w') as f:
    json.dump(aligned_data, f, indent=4)