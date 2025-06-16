CREATE TABLE driver_profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    license_number VARCHAR(50) NOT NULL,
    vehicle_number VARCHAR(20) NOT NULL,
    vehicle_model VARCHAR(100) NOT NULL,
    vehicle_color VARCHAR(50) NOT NULL,
    current_latitude DOUBLE,
    current_longitude DOUBLE,
    is_available BOOLEAN DEFAULT TRUE,
    average_rating DOUBLE DEFAULT 0.0,
    total_trips INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
); 