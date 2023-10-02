INSERT INTO users (username, password, email, role, classroom_id) VALUES
('teacher1', '$2a$10$UQQSRpugZzS6N3aUg/mPq.Nzxlbfb1Pe7heBq1tUmnGx3WIDxSGpG', 'teacher1@example.com', 'TEACHER', NULL),
('teacher2', '$2a$10$JEgcLvkTT4HKE5V3cC7Cr.c/Vd7pTQQqMN8AgVjhQyYdFzINn7j56', 'teacher2@example.com', 'TEACHER', NULL),
('admin', '$2a$10$Y6AgirGgC/zNOGcLbojD/OmO/T8FrqO8opl0xLBYOy2CdL1fZqdxC', 'admin@example.com', 'ADMIN', NULL),
('student1', '$2a$10$s4wv1PoUmfhc6dyruJsDcuHy99lLV2iQlOZ0Jul/TjvZd7atMVdMm', 'student1@example.com', 'STUDENT', 1),
('student2', '$2a$10$T63jcABzCHIQ2A25BsDH6eO/YNE0MZTpuTCehuNUSBawab0v/WHiG', 'student2@example.com', 'STUDENT', 1),
('student3', '$2a$10$.T48fgWXQ2PMa2avkuDNSOfPMfIAiZk.AVFGIbs8.EM6ykcqHnbwO', 'student3@example.com', 'STUDENT', 1),
('student4', '$2a$10$xipCA6mYeDMzvT.4nF8W9.YbLtZqHX6sb9HZ0sYUx9KqVi3kLOMxK', 'student4@example.com', 'STUDENT', 2),
('student5', '$2a$10$R4Y6GFK9rMK7m10aazTdou3rK1R6B7cEXelJFo8sega7FAcrjt8w.', 'student5@example.com', 'STUDENT', 2),
('student6', '$2a$10$/r3ROBg04DURW9bLf8aqteXETFd5ySApKtXyrmCAYy/2mFba5Z0a2', 'student6@example.com', 'STUDENT', 2)
ON CONFLICT(username) DO NOTHING;

INSERT INTO teacher_assignment (teacher_id, subject_id) VALUES
(1, 1),
(2, 2);