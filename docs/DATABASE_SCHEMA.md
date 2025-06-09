# Taxi Booking Application - Database Schema

## Core Tables

### users
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User's email address |
| phone_number | VARCHAR(20) | UNIQUE, NOT NULL | User's phone number |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password |
| full_name | VARCHAR(100) | NOT NULL | User's full name |
| user_type | ENUM('passenger', 'driver', 'admin') | NOT NULL | Type of user |
| status | ENUM('active', 'inactive', 'suspended') | NOT NULL, DEFAULT 'active' | Account status |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record update timestamp |

Indexes:
- PRIMARY KEY (id)
- UNIQUE INDEX idx_users_email (email)
- UNIQUE INDEX idx_users_phone (phone_number)
- INDEX idx_users_status (status)

### passenger_profiles
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| user_id | UUIhD | FOREIGN KEY (users.id), NOT NULL | Reference to users table |
| home_address | TEXT | NULL | Default home address |
| work_address | TEXT | NULL | Default work address |
| preferred_payment_method_id | UUID | FOREIGN KEY (payment_methods.id), NULL | Default payment method |
| average_rating | DECIMAL(3,2) | DEFAULT 5.00 | Average rating from drivers |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record update timestamp |

Indexes:
- PRIMARY KEY (id)
- FOREIGN KEY (user_id) REFERENCES users(id)
- INDEX idx_passenger_rating (average_rating)

### driver_profiles
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| user_id | UUID | FOREIGN KEY (users.id), NOT NULL | Reference to users table |
| license_number | VARCHAR(50) | UNIQUE, NOT NULL | Driver's license number |
| vehicle_plate | VARCHAR(20) | UNIQUE, NOT NULL | Vehicle plate number |
| vehicle_model | VARCHAR(100) | NOT NULL | Vehicle model |
| vehicle_year | INTEGER | NOT NULL | Vehicle manufacturing year |
| document_verification_status | ENUM('pending', 'verified', 'rejected') | NOT NULL, DEFAULT 'pending' | Document verification status |
| current_location_lat | DECIMAL(10,8) | NULL | Current latitude |
| current_location_lng | DECIMAL(11,8) | NULL | Current longitude |
| is_available | BOOLEAN | NOT NULL, DEFAULT false | Driver availability status |
| average_rating | DECIMAL(3,2) | DEFAULT 5.00 | Average rating from passengers |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record update timestamp |

Indexes:
- PRIMARY KEY (id)
- FOREIGN KEY (user_id) REFERENCES users(id)
- UNIQUE INDEX idx_license (license_number)
- UNIQUE INDEX idx_vehicle (vehicle_plate)
- INDEX idx_location (current_location_lat, current_location_lng)
- INDEX idx_availability (is_available)
- INDEX idx_driver_rating (average_rating)

### rides
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| passenger_id | UUID | FOREIGN KEY (passenger_profiles.id), NOT NULL | Reference to passenger |
| driver_id | UUID | FOREIGN KEY (driver_profiles.id), NOT NULL | Reference to driver |
| pickup_location_lat | DECIMAL(10,8) | NOT NULL | Pickup latitude |
| pickup_location_lng | DECIMAL(11,8) | NOT NULL | Pickup longitude |
| pickup_address | TEXT | NOT NULL | Pickup address |
| dropoff_location_lat | DECIMAL(10,8) | NOT NULL | Dropoff latitude |
| dropoff_location_lng | DECIMAL(11,8) | NOT NULL | Dropoff longitude |
| dropoff_address | TEXT | NOT NULL | Dropoff address |
| status | ENUM('requested', 'accepted', 'arrived', 'in_progress', 'completed', 'cancelled') | NOT NULL | Ride status |
| scheduled_time | TIMESTAMP | NULL | For scheduled rides |
| started_at | TIMESTAMP | NULL | Ride start time |
| completed_at | TIMESTAMP | NULL | Ride completion time |
| cancellation_reason | TEXT | NULL | Reason if cancelled |
| base_fare | DECIMAL(10,2) | NOT NULL | Base fare amount |
| final_fare | DECIMAL(10,2) | NULL | Final fare after adjustments |
| distance_km | DECIMAL(8,2) | NULL | Total distance in kilometers |
| duration_minutes | INTEGER | NULL | Total duration in minutes |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record update timestamp |

Indexes:
- PRIMARY KEY (id)
- FOREIGN KEY (passenger_id) REFERENCES passenger_profiles(id)
- FOREIGN KEY (driver_id) REFERENCES driver_profiles(id)
- INDEX idx_ride_status (status)
- INDEX idx_scheduled_time (scheduled_time)
- INDEX idx_completion_time (completed_at)

### payments
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| ride_id | UUID | FOREIGN KEY (rides.id), NOT NULL | Reference to ride |
| amount | DECIMAL(10,2) | NOT NULL | Payment amount |
| payment_method_id | UUID | FOREIGN KEY (payment_methods.id), NOT NULL | Payment method used |
| status | ENUM('pending', 'completed', 'failed', 'refunded') | NOT NULL | Payment status |
| transaction_id | VARCHAR(255) | UNIQUE, NULL | External payment gateway transaction ID |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record update timestamp |

Indexes:
- PRIMARY KEY (id)
- FOREIGN KEY (ride_id) REFERENCES rides(id)
- FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
- UNIQUE INDEX idx_transaction (transaction_id)
- INDEX idx_payment_status (status)

### payment_methods
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| user_id | UUID | FOREIGN KEY (users.id), NOT NULL | Reference to user |
| type | ENUM('credit_card', 'debit_card', 'upi', 'wallet') | NOT NULL | Payment method type |
| provider | VARCHAR(50) | NOT NULL | Payment provider name |
| last_four | VARCHAR(4) | NOT NULL | Last 4 digits of card/account |
| expiry_date | DATE | NULL | Expiration date for cards |
| is_default | BOOLEAN | NOT NULL, DEFAULT false | Default payment method flag |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record update timestamp |

Indexes:
- PRIMARY KEY (id)
- FOREIGN KEY (user_id) REFERENCES users(id)
- INDEX idx_payment_type (type)
- INDEX idx_default_method (user_id, is_default)

### ratings
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| ride_id | UUID | FOREIGN KEY (rides.id), NOT NULL | Reference to ride |
| rater_id | UUID | FOREIGN KEY (users.id), NOT NULL | User giving rating |
| ratee_id | UUID | FOREIGN KEY (users.id), NOT NULL | User receiving rating |
| rating | INTEGER | NOT NULL, CHECK (rating BETWEEN 1 AND 5) | Rating value (1-5) |
| comment | TEXT | NULL | Rating comment |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |

Indexes:
- PRIMARY KEY (id)
- FOREIGN KEY (ride_id) REFERENCES rides(id)
- FOREIGN KEY (rater_id) REFERENCES users(id)
- FOREIGN KEY (ratee_id) REFERENCES users(id)
- INDEX idx_ratee_ratings (ratee_id, rating)

### driver_earnings
| Column Name | Data Type | Constraints | Description |
|------------|-----------|-------------|-------------|
| id | UUID | PRIMARY KEY | Unique identifier |
| driver_id | UUID | FOREIGN KEY (driver_profiles.id), NOT NULL | Reference to driver |
| ride_id | UUID | FOREIGN KEY (rides.id), NOT NULL | Reference to ride |
| amount | DECIMAL(10,2) | NOT NULL | Earning amount |
| commission_amount | DECIMAL(10,2) | NOT NULL | Platform commission |
| status | ENUM('pending', 'paid', 'cancelled') | NOT NULL | Payment status |
| payout_date | TIMESTAMP | NULL | When payment was made |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record update timestamp |

Indexes:
- PRIMARY KEY (id)
- FOREIGN KEY (driver_id) REFERENCES driver_profiles(id)
- FOREIGN KEY (ride_id) REFERENCES rides(id)
- INDEX idx_earnings_status (status)
- INDEX idx_payout_date (payout_date)

## Relationships Overview

1. users (1) → (0..1) passenger_profiles
2. users (1) → (0..1) driver_profiles
3. users (1) → (0..n) payment_methods
4. passenger_profiles (1) → (0..n) rides
5. driver_profiles (1) → (0..n) rides
6. rides (1) → (1) payments
7. rides (1) → (0..2) ratings
8. rides (1) → (1) driver_earnings

## Common Queries and Optimizations

1. Finding nearby drivers:
```sql
CREATE EXTENSION IF NOT EXISTS cube;
CREATE EXTENSION IF NOT EXISTS earthdistance;

-- Index for geospatial queries
CREATE INDEX idx_driver_location ON driver_profiles 
USING gist (ll_to_earth(current_location_lat, current_location_lng));
```

2. Calculating driver ratings:
```sql
CREATE MATERIALIZED VIEW driver_rating_summary AS
SELECT 
    d.id,
    d.user_id,
    COALESCE(AVG(r.rating), 5.00) as average_rating,
    COUNT(r.id) as total_ratings
FROM driver_profiles d
LEFT JOIN ratings r ON r.ratee_id = d.user_id
GROUP BY d.id, d.user_id;

CREATE UNIQUE INDEX idx_driver_rating_summary ON driver_rating_summary (id);
```

## Notes on Indexing Strategy

1. Primary Keys: All tables use UUID as primary keys for:
   - Better distribution in distributed systems
   - Security (non-sequential IDs)
   - Easier database sharding

2. Foreign Keys: All foreign key relationships are indexed

3. Performance Indexes:
   - Location-based queries (driver_profiles)
   - Status-based queries (rides, payments)
   - Rating queries (passenger_profiles, driver_profiles)
   - Temporal queries (rides.scheduled_time, rides.completed_at)

4. Composite Indexes:
   - driver_profiles(is_available, current_location_lat, current_location_lng)
   - rides(status, scheduled_time)

## Data Integrity Rules

1. Cascading Updates: Disabled for all foreign keys
2. Cascading Deletes: Disabled for all foreign keys
3. Soft Deletes: Implemented via status columns
4. Check Constraints:
   - ratings.rating BETWEEN 1 AND 5
   - payments.amount >= 0
   - rides.final_fare >= 0

## Partitioning Strategy

For large-scale deployments, consider:

1. Rides table partitioning by date range
2. Payments table partitioning by date range
3. Ratings table partitioning by date range

Example:
```sql
CREATE TABLE rides (
    -- columns as defined above
) PARTITION BY RANGE (created_at);

CREATE TABLE rides_2024_q1 PARTITION OF rides
    FOR VALUES FROM ('2024-01-01') TO ('2024-04-01');
``` 