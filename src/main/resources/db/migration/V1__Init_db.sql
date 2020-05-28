CREATE TABLE `user` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`username` varchar(255) NOT NULL,
	`password` varchar(255) NOT NULL,
	`first_name` varchar(255) NOT NULL,
	`last_name` varchar(255) NOT NULL,
	`role` varchar(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `UK_username_on_user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `trip` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	`created_at` datetime(6) NOT NULL,
	`updated_at` datetime(6) NOT NULL,
	`user_id` bigint(20) NOT NULL,
	PRIMARY KEY (`id`),
	KEY `FK_user_on_trip` (`user_id`),
	CONSTRAINT `FK_user_on_trip` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `place` (
	 `id` bigint(20) NOT NULL AUTO_INCREMENT,
	 `google_place_id` varchar(255) NOT NULL,
	 `name` varchar(255) NOT NULL,
	 `trip_id` bigint(20) NOT NULL,
	 `formatted_address` varchar(255) NOT NULL,
	 `location_lat` double NOT NULL,
	 `location_lng` double NOT NULL,
	 `parent_location` varchar(255) DEFAULT NULL,
	 `price_level` int(11) DEFAULT NULL,
	 `rating` double DEFAULT NULL,
	 PRIMARY KEY (`id`),
	 KEY `FK_trip_on_place` (`trip_id`),
	 CONSTRAINT `FK_trip_on_place` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `type` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `UK_name_on_type` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `place_type` (
	`type_id` bigint(20) NOT NULL,
	`place_id` bigint(20) NOT NULL,
	PRIMARY KEY (`type_id`,`place_id`),
	KEY `FK_place_on_place_trip` (`place_id`),
	CONSTRAINT `FK_type_on_place_type` FOREIGN KEY (`type_id`) REFERENCES `place` (`id`),
	CONSTRAINT `FK_place_on_place_type` FOREIGN KEY (`place_id`) REFERENCES `type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

