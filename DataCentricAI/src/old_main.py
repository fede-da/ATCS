import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from pulp import LpVariable, LpProblem, lpSum, LpMinimize

# Genera un dataset di esempio
data = {
    'feature1': [1, 2, 3, 4, 5],
    'feature2': [2, 3, 4, 5, 6],
    'label': [0, 0, 1, 1, 1]
}
df = pd.DataFrame(data)

# Dividi il dataset in training e test set
X_train, X_test, y_train, y_test = train_test_split(df[['feature1', 'feature2']], df['label'], test_size=0.2,
                                                    random_state=42)

# Addestramento del modello di machine learning
model = LogisticRegression()
model.fit(X_train, y_train)


# Funzione per calcolare l'influenza di un'istanza
def influence(instance_index, X_train, model):
    # Assicurati che l'indice esista nel DataFrame
    if instance_index in X_train.index:
        # Calcola la probabilità della previsione corrente
        current_prediction = model.predict_proba(X_train.iloc[[instance_index]])[0][1]

        # Rimuovi l'istanza e calcola la nuova previsione
        X_train_removed = X_train.drop(index=instance_index)
        new_prediction = model.predict_proba(X_train_removed)[:, 1]

        # Calcola l'influenza come differenza nella previsione
        influence_score = current_prediction - new_prediction.mean()
        return influence_score
    else:
        print(f"Indice {instance_index} non trovato nel DataFrame.")
        return 0


# Calcola l'influenza di ogni istanza nel training set
influences = [influence(i, X_train, model) for i in range(len(X_train))]

# Formulazione del problema ILP per la selezione delle istanze da eliminare
prob = LpProblem("InfluenceMinimization", LpMinimize)

# Crea variabili binarie per ogni istanza nel training set
remove = LpVariable.dicts("remove", range(len(X_train)), 0, 1)

# Funzione obiettivo: minimizza la somma delle influenze delle istanze rimosse
prob += lpSum([influences[i] * remove[i] for i in range(len(X_train))])

max_instances_to_remove = 2  # Imposta il massimo numero di istanze da rimuovere
# Vincoli: massimo numero di istanze da rimuovere
prob += lpSum([remove[i] for i in range(len(X_train))]) <= max_instances_to_remove

# Risolvi il problema ILP
prob.solve()

# Ottieni le istanze da rimuovere
instances_to_remove = [i for i in range(len(X_train)) if remove[i].value() == 1]

# Rimuovi le istanze dal training set
X_train_clean = X_train.drop(index=instances_to_remove)
y_train_clean = y_train.drop(index=instances_to_remove)

# Ri-addestramento del modello di machine learning con il training set pulito
model_clean = LogisticRegression()
model_clean.fit(X_train_clean, y_train_clean)

# Valutazione delle prestazioni del modello pulito
accuracy_clean = model_clean.score(X_test, y_test)
