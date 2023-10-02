CREATE TABLE IF NOT EXISTS classroom (
    id SERIAL PRIMARY KEY,
    class_name VARCHAR(255) UNIQUE,
    academic_year VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    role VARCHAR(255) NOT NULL,
    classroom_id INT REFERENCES classroom(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS subject (
    id SERIAL PRIMARY KEY,
    subject_name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS grade (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    value NUMERIC NOT NULL,
    description VARCHAR(255),
    weight NUMERIC NOT NULL,
    subject_id INT REFERENCES subject(id) ON DELETE CASCADE,
    student_id INT REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teacher_assignment (
    id SERIAL PRIMARY KEY,
    teacher_id INT REFERENCES users(id) ON DELETE CASCADE,
    subject_id INT REFERENCES subject(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS password_reset_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);