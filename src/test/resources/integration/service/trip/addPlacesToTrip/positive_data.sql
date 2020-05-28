SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE table trip;
TRUNCATE table place;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO trip (id, account_id, created_at, updated_at, name) VALUES (1, 3, "2020-05-28 02:28:44", "2020-05-28 02:28:44", "Ukraine-1")
INSERT INTO trip (id, account_id, created_at, updated_at, name) VALUES (2, 2, "2020-05-28 02:28:44", "2020-05-28 02:28:44", "Ukraine-2")
INSERT INTO trip (id, account_id, created_at, updated_at, name) VALUES (3, 3, "2020-05-28 02:28:44", "2020-05-28 02:28:44", "Ukraine-3")
INSERT INTO place (id,formatted_address, google_place_id, location_lat, location_lng, name, parent_location, price_level, rating, trip_id) VALUES (1, 'Lviv', 'Lviv-1', 0.2, 0.1, 'Opera Hotel', 'Lviv-1', 4, 5, 1);
INSERT INTO place (id,formatted_address, google_place_id, location_lat, location_lng, name, parent_location, price_level, rating, trip_id) VALUES (2, 'Lviv', 'Lviv-2', 0.3, 0.2, 'Astoria Hotel', 'Lviv-2', 4, 5, 1);
INSERT INTO place (id,formatted_address, google_place_id, location_lat, location_lng, name, parent_location, price_level, rating, trip_id) VALUES (3, 'Lviv', 'Lviv-3', 0.4, 0.3, 'Grand Hotel', 'Lviv-3', 4, 5, 1);