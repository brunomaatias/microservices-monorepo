CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    uuid_role UUID UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    inactive_reason VARCHAR(255),
    notes TEXT
);

CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    uuid_user UUID UNIQUE NOT NULL,
    name VARCHAR(100),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    mobile_number VARCHAR(20),
    profile_image TEXT,
    signature_image TEXT,
    device_id VARCHAR(255),
    nfc_id VARCHAR(255),
    created_at TIMESTAMP,
    last_login TIMESTAMP,
    last_logout TIMESTAMP,
    online BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    inactive_reason VARCHAR(255),
    role_id BIGINT REFERENCES roles(id) ON DELETE SET NULL
);

CREATE TABLE accesses (
    id BIGSERIAL PRIMARY KEY,
    active BOOLEAN DEFAULT TRUE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    tenant_id VARCHAR(255)
);

CREATE TABLE access_permissions (
    access_id BIGINT NOT NULL REFERENCES accesses(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (access_id, permission_id)
);

-- Insert some default values to test
INSERT INTO roles (uuid_role, name, notes) VALUES (gen_random_uuid(), 'ADMIN', 'System Administrator');
INSERT INTO roles (uuid_role, name, notes) VALUES (gen_random_uuid(), 'TECHNICIAN', 'Field Technician');

INSERT INTO permissions (name, description) VALUES ('USERS_CREATE', 'Can create users');
INSERT INTO permissions (name, description) VALUES ('USERS_READ', 'Can list users');

-- Assume Role 1 is ADMIN
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2);
