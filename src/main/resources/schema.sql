CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role ENUM('CUSTOMER', 'STAFF', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER'
);

CREATE TABLE IF NOT EXISTS bikes (
    bike_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    serial_number VARCHAR(100),

    CONSTRAINT fk_bikes_users
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parts (
    part_id INT AUTO_INCREMENT PRIMARY KEY,
    part_name VARCHAR(100) NOT NULL,
    part_number VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    quantity INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS service_orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    bike_id INT NOT NULL,
    part_id INT,
    service_type VARCHAR(75) NOT NULL,
    problem_description TEXT,
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'READY') NOT NULL DEFAULT 'NOT_STARTED',
    dropoff_date DATE,
    total_amount DECIMAL(10, 2) DEFAULT 0.00,

    CONSTRAINT fk_service_orders_bikes
        FOREIGN KEY (bike_id)
        REFERENCES bikes(bike_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_service_orders_parts
        FOREIGN KEY (part_id)
        REFERENCES parts(part_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);
