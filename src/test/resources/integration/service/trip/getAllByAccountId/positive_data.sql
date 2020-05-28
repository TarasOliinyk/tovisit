SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE table trip;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO trip (id, account_id, created_at, updated_at, name) VALUES (1, 3, "2020-05-28 02:28:44", "2020-05-28 02:28:44", "Ukraine-1")
INSERT INTO trip (id, account_id, created_at, updated_at, name) VALUES (2, 2, "2020-05-28 02:28:44", "2020-05-28 02:28:44", "Ukraine-2")
INSERT INTO trip (id, account_id, created_at, updated_at, name) VALUES (3, 3, "2020-05-28 02:28:44", "2020-05-28 02:28:44", "Ukraine-3")