CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT,
    enabled BIT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE patients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    date_of_birth DATE,
    phone VARCHAR(32),
    address VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE doctors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    specialization VARCHAR(255) NOT NULL,
    hospital VARCHAR(255),
    approved BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE availability (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    doctor_id BIGINT,
    day_of_week VARCHAR(16),
    specific_date DATE,
    start_time TIME,
    end_time TIME,
    max_slots INT,
    FOREIGN KEY (doctor_id) REFERENCES doctors (id)
);

CREATE TABLE appointments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT,
    doctor_id BIGINT,
    appointment_time DATETIME,
    status VARCHAR(32),
    reason VARCHAR(255),
    notes VARCHAR(255),
    rejection_reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients (id),
    FOREIGN KEY (doctor_id) REFERENCES doctors (id)
);

CREATE INDEX idx_appointment_status ON appointments (status);

