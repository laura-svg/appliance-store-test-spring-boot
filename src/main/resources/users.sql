CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(50) NOT NULL
);
INSERT INTO users (email, password, role) VALUES ('admin@gmail.com', 'admin123', 'ADMIN');
INSERT INTO users (email, password, role) VALUES ('client1@gmail.com', 'client123', 'CLIENT');
INSERT INTO users (email, password, role) VALUES ('employee1@gmail.com', 'employee123', 'EMPLOYEE');