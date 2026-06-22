-- Insert ResearchUser data
INSERT INTO research_user (username, password) VALUES ('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGzP1eS6M5L9iZgJ3T.O');
INSERT INTO research_user (username, password) VALUES ('researcher1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGzP1eS6M5L9iZgJ3T.O');

INSERT INTO research_user_roles (user_id, role) VALUES (1, 'ADMIN');
INSERT INTO research_user_roles (user_id, role) VALUES (2, 'RESEARCHER');

-- Insert Experiment data
INSERT INTO experiment (id, name, algorithm, difficulty, public_payload, encrypted_payload, status, created_at) VALUES (RANDOM_UUID(), 'Caesar Cipher 1', 'CAESAR', 'EASY', 'Public Hint: Shift is small', 'Jrypbzr gb PelfcgbYno', 'CREATED', CURRENT_TIMESTAMP());
INSERT INTO experiment (id, name, algorithm, difficulty, public_payload, encrypted_payload, status, created_at) VALUES (RANDOM_UUID(), 'XOR Repeated Key', 'XOR', 'MEDIUM', 'Public Hint: Key length is 4', '0x12345678', 'CREATED', CURRENT_TIMESTAMP());
INSERT INTO experiment (id, name, algorithm, difficulty, public_payload, encrypted_payload, status, created_at) VALUES (RANDOM_UUID(), 'Weak RSA 512', 'RSA_WEAK', 'EASY', 'Public modulus: 00:a1:b2...', '0xabcd...', 'CREATED', CURRENT_TIMESTAMP());
INSERT INTO experiment (id, name, algorithm, difficulty, public_payload, encrypted_payload, status, created_at) VALUES (RANDOM_UUID(), 'Hash No Salt', 'HASH_NO_SALT', 'MEDIUM', 'No public payload', '5d41402abc4b2a76b9719d911017c592', 'CREATED', CURRENT_TIMESTAMP());
INSERT INTO experiment (id, name, algorithm, difficulty, public_payload, encrypted_payload, status, created_at) VALUES (RANDOM_UUID(), 'JWT Forgery', 'JWT_FORGERY', 'HARD', 'Token: eyJhbGciOiJIUzI1NiJ9.ey...', 'eyJhbGciOiJIUzI1NiJ9...', 'CREATED', CURRENT_TIMESTAMP());
INSERT INTO experiment (id, name, algorithm, difficulty, public_payload, encrypted_payload, status, created_at) VALUES (RANDOM_UUID(), 'ML-KEM vs RSA', 'ML_KEM', 'HARD', 'Comparison data', 'Encrypted Kyber Payload', 'CREATED', CURRENT_TIMESTAMP());