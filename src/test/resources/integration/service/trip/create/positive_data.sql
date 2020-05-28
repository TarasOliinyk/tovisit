SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE table trip;
TRUNCATE table user;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO user (id, first_name, last_name, password, role, username) VALUES (3, 'Piter', 'Pen', 'pass', 'ROLE_USER', 'user');