-- =============================================================================
-- Migration: V5__create_auth_tables.sql
-- Description: Módulo 5 - Autenticación, usuarios y control de acceso (IAM / Security).
-- Database Engine: PostgreSQL (UUID, TIMESTAMPTZ, snake_case)
-- =============================================================================

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

COMMENT ON TABLE users IS 'Tabla de usuarios y credenciales de autenticación del sistema (IAM)';
COMMENT ON COLUMN users.id IS 'Identificador único del usuario (UUID)';
COMMENT ON COLUMN users.username IS 'Nombre de usuario único para inicio de sesión';
COMMENT ON COLUMN users.email IS 'Correo electrónico único de contacto y login';
COMMENT ON COLUMN users.password_hash IS 'Hash de contraseña cifrado con BCrypt';
COMMENT ON COLUMN users.role IS 'Rol de autorización (ROLE_ADMIN, ROLE_PROFESSIONAL, ROLE_PATIENT, ROLE_USER)';
COMMENT ON COLUMN users.status IS 'Estado de la cuenta de usuario (ACTIVE, INACTIVE, LOCKED)';
