-- Supprimer les contraintes supplémentaires
ALTER TABLE Debts DROP CONSTRAINT IF EXISTS chk_remaining_amount;
ALTER TABLE Payments DROP CONSTRAINT IF EXISTS chk_payment_amount;
ALTER TABLE DetailDebts DROP CONSTRAINT IF EXISTS chk_quantity;
ALTER TABLE DetailDebtRequests DROP CONSTRAINT IF EXISTS chk_request_quantity;

-- Supprimer les tables dans l'ordre inverse de leur création pour respecter les dépendances
DROP TABLE IF EXISTS DetailDebtRequests CASCADE;
DROP TABLE IF EXISTS DebtRequests CASCADE;
DROP TABLE IF EXISTS Payments CASCADE;
DROP TABLE IF EXISTS DetailDebts CASCADE;
DROP TABLE IF EXISTS Debts CASCADE;
DROP TABLE IF EXISTS Articles CASCADE;
DROP TABLE IF EXISTS Client CASCADE;
DROP TABLE IF EXISTS Users CASCADE;



DELETE FROM clients WHERE user_id IS NOT NULL;
DELETE FROM users;
DELETE FROM client;