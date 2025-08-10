INSERT INTO user_master (id, name, email) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice Johnson', 'alice@example.com'),
    ('22222222-2222-2222-2222-222222222222', 'Bob Smith', 'bob@example.com');

-- Alice is available Mon & Wed from 09:00–12:00 and 14:00–18:00
INSERT INTO availability_rule (id, user_id, weekday, start_time, end_time) VALUES
    ('a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1', '11111111-1111-1111-1111-111111111111', 'MONDAY', '09:00', '12:00'),
    ('a2a2a2a2-a2a2-a2a2-a2a2-a2a2a2a2a2a2', '11111111-1111-1111-1111-111111111111', 'MONDAY', '14:00', '18:00'),
    ('a3a3a3a3-a3a3-a3a3-a3a3-a3a3a3a3a3a3', '11111111-1111-1111-1111-111111111111', 'WEDNESDAY', '10:00', '13:00'),
    ('a4a4a4a4-a4a4-a4a4-a4a4-a4a4a4a4a4a4', '11111111-1111-1111-1111-111111111111', 'WEDNESDAY', '15:00', '17:00'),

    -- Bob is available Tue & Thu from 08:00–12:00
    ('b1b1b1b1-b1b1-b1b1-b1b1-b1b1b1b1b1b1', '22222222-2222-2222-2222-222222222222', 'TUESDAY', '08:00', '12:00'),
    ('b2b2b2b2-b2b2-b2b2-b2b2-b2b2b2b2b2b2', '22222222-2222-2222-2222-222222222222', 'THURSDAY', '08:00', '12:00');

-- Alice has two appointments on upcoming Monday
INSERT INTO appointment (id, user_id, invitee_name, invitee_email, appointment_date, start_time, end_time) VALUES
    ('c1c1c1c1-c1c1-c1c1-c1c1-c1c1c1c1c1c1', '11111111-1111-1111-1111-111111111111', 'Charlie', 'charlie@example.com', '2025-08-11', '09:00', '10:00'),
    ('c2c2c2c2-c2c2-c2c2-c2c2-c2c2c2c2c2c2', '11111111-1111-1111-1111-111111111111', 'Dana', 'dana@example.com', '2025-08-11', '14:00', '15:00'),

    -- Bob has one appointment on Tuesday
    ('d1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1', '22222222-2222-2222-2222-222222222222', 'Eve', 'eve@example.com', '2025-08-12', '08:00', '09:00');