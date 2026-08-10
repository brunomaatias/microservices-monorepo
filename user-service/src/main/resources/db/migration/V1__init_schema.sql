CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    auth_uuid UUID UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    avatar_url VARCHAR(255),
    address_line VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255)
);
