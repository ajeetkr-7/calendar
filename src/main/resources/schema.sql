CREATE TABLE IF NOT EXISTS user_master (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS appointment (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    invitee_name VARCHAR(255) NOT NULL,
    invitee_email VARCHAR(255) NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT fk_appointment_user FOREIGN KEY (user_id) REFERENCES user_master (id)
);

CREATE TABLE IF NOT EXISTS availability_rule (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    weekday VARCHAR(16) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT fk_availability_user FOREIGN KEY (user_id) REFERENCES user_master (id)
);