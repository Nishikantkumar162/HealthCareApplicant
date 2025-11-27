INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_DOCTOR'), ('ROLE_PATIENT');

INSERT INTO users (full_name, email, password, role_id, enabled)
VALUES ('System Admin', 'admin@healthcare.com', '{bcrypt}$2a$10$examplehash', 1, 1);

