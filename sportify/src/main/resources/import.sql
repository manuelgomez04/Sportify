-- Insertar un ADMIN
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at) VALUES (gen_random_uuid(), 'admin_user', '{bcrypt}$2a$12$q4eBcJphwLiFIb1pEM47wuokLamviOKc2BFv0HHfUqJ8NyPBlc9xa', 'Alice Johnson', '1985-03-10', 'admin@example.com', true, null, NOW()); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), 0);

-- Insertar un USUARIO
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at) VALUES (gen_random_uuid(), 'regular_user', '{bcrypt}$2a$12$zKYqTeiCPvPHMdKUo6VAp.G66i.y9SGGqzpv73vqp6c1QjEx7c83O', 'Jane Smith', '1995-08-20', 'user@example.com', true, null, NOW()); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), 1);

-- Insertar un WRITER
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at) VALUES (gen_random_uuid(), 'writer_user', '{bcrypt}$2a$12$wyIc2c9yMIA/EwfAcdJ2yeD0I0odnyJT0dKJM/udmw4VQmbuzWsnK', 'John Doe', '1990-05-15', 'writer@example.com', true, null, NOW()); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), 2);