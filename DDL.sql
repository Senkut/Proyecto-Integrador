SET search_path TO hospital_inventory, public;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Eliminar tipos si existen
DROP TYPE IF EXISTS equipment_status CASCADE;
DROP TYPE IF EXISTS equipment_type CASCADE;
DROP TYPE IF EXISTS role_enum CASCADE;

-- Crear ENUMS
CREATE TYPE equipment_status AS ENUM (
    'NEW',
    'IN_USE',
    'UNDER_MAINTENANCE',
    'DAMAGED',
    'LOST',
    'DECOMISSIONED',
    'IN_STORAGE',
    'RESERVED',
    'PENDING_INSPECTION',
    'REPLACEMENT_NEEDED',
    'RECOVERED',
    'DONATED',
    'DISPOSED'
);

CREATE TYPE equipment_type AS ENUM (
    'COMPUTING',
    'MEDICAL',
    'LABORATORY',
    'OFFICE',
    'INFRAESTRUCTURE',
    'OTHER'
);

CREATE TYPE role_enum AS ENUM (
    'WATCHMAN',
    'ADMIN',
    'DOCTOR',
    'NURSE',
    'SECRETARY',
    'BOSS',
    'MAINTENANCE_MAN'
);

-- ELIMINAR TABLAS
DROP TABLE IF EXISTS biomedical_equipment CASCADE;
DROP TABLE IF EXISTS tech_equipment CASCADE;
DROP TABLE IF EXISTS equipment CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS provider CASCADE;

-- ==============================
--  TABLE provider (CORRECTA)
-- ==============================
CREATE TABLE IF NOT EXISTS provider (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(120) NOT NULL,
    tax_id VARCHAR(80) NOT NULL UNIQUE,
    contact_email VARCHAR(160) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CHECK (position('@' IN contact_email) > 1)
);

-- ==============================
--  TABLE person
-- ==============================
CREATE TABLE IF NOT EXISTS person (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(160) NOT NULL,
    document VARCHAR(60) NOT NULL UNIQUE,
    role role_enum NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ==============================
--  TABLE equipment (CORREGIDA)
-- ==============================
CREATE TABLE IF NOT EXISTS equipment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    serial VARCHAR(120) NOT NULL UNIQUE,
    brand VARCHAR(120) NOT NULL,
    model VARCHAR(120) NOT NULL,
    type equipment_type NOT NULL,
    state equipment_status NOT NULL,
    provider_id UUID NULL REFERENCES provider(id)
                    ON UPDATE CASCADE
                    ON DELETE SET NULL,
    image_path TEXT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_equipment_provider ON equipment(provider_id);
CREATE INDEX IF NOT EXISTS idx_equipment_type ON equipment(type);
CREATE INDEX IF NOT EXISTS idx_equipment_state ON equipment(state);

-- ==============================
--  TABLE biomedical_equipment
-- ==============================
CREATE TABLE IF NOT EXISTS biomedical_equipment (
    id UUID PRIMARY KEY REFERENCES equipment(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
    risk_class VARCHAR(60) NOT NULL,
    calibration_cert VARCHAR(160) NOT NULL
);

-- ==============================
--  TABLE tech_equipment
-- ==============================
CREATE TABLE IF NOT EXISTS tech_equipment (
    id UUID PRIMARY KEY REFERENCES equipment(id)
                ON UPDATE CASCADE
                ON DELETE CASCADE,
    os VARCHAR(60) NOT NULL,
    ram_gb INT NOT NULL
);
