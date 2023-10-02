INSERT INTO classroom (class_name, academic_year) VALUES
('1A', '2023/2024'),
('1B', '2023/2024')
ON CONFLICT(class_name) DO NOTHING;

INSERT INTO subject (subject_name) VALUES
('Math'),
('History')
ON CONFLICT(subject_name) DO NOTHING;
