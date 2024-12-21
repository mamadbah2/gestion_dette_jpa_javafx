-- Fichier de migration pour PostgreSQL

-- Création de la table des utilisateurs
CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- Par exemple: "Admin", "Boutiquier", "Client"
    isActive BOOLEAN DEFAULT TRUE
);

-- Création de la table des clients
CREATE TABLE Clients (
    id SERIAL PRIMARY KEY,
    surname VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) NOT NULL UNIQUE,
    addresse TEXT NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    user_id INT UNIQUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- Création de la table des articles
CREATE TABLE Articles (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,
    qte_stock INT NOT NULL DEFAULT 0,
    prix NUMERIC(10, 2) NOT NULL,
    is_archived BOOLEAN DEFAULT FALSE
);

-- Création de la table des dettes
CREATE TABLE Debts (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    amount NUMERIC(10, 2) NOT NULL,
    paidAmount NUMERIC(10, 2) DEFAULT 0,
    remainingAmount NUMERIC(10, 2) NOT NULL,
    is_achievied BOOLEAN DEFAULT FALSE,
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE
);

-- Création de la table des détails de dettes
CREATE TABLE DetailDebts (
    id SERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    prix NUMERIC(10, 2) NOT NULL,
    article_id INT NOT NULL,
    debt_id INT NOT NULL,
    FOREIGN KEY (debt_id) REFERENCES Debts(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES Articles(id) ON DELETE CASCADE
);

-- Création de la table des paiements
CREATE TABLE Payments (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    amount NUMERIC(10, 2) NOT NULL,
    debt_id INT NOT NULL,
    FOREIGN KEY (debt_id) REFERENCES Debts(id) ON DELETE CASCADE
);

-- Création de la table des demandes de dette
CREATE TABLE DebtRequests (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'En Cours', -- "En Cours", "Annuler", "Valider"
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE
);

-- Création de la table des détails des demandes de dette
CREATE TABLE DetailDebtRequests (
    id SERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    debt_request_id INT NOT NULL,
    article_id INT NOT NULL,
    FOREIGN KEY (debt_request_id) REFERENCES DebtRequest(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES Articles(id) ON DELETE CASCADE
);

-- Contraintes supplémentaires pour garantir l'intégrité des données
ALTER TABLE Debts ADD CONSTRAINT chk_remaining_amount CHECK (remainingamount >= 0);
ALTER TABLE Payments ADD CONSTRAINT chk_payment_amount CHECK (amount > 0);
ALTER TABLE DetailDebts ADD CONSTRAINT chk_quantity CHECK (quantity > 0);
ALTER TABLE DetailDebtRequests ADD CONSTRAINT chk_request_quantity CHECK (quantity > 0);
