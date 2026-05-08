USE `artiststrackerdb`;

-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event`;

CREATE TABLE IF NOT EXISTS `event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `venue` VARCHAR(255) NULL,
  `city` VARCHAR(100) NULL,
  `state` VARCHAR(50) NULL,
  `country` VARCHAR(50) NULL DEFAULT 'US',
  `event_date` DATETIME NULL,
  `ticket_url` VARCHAR(500) NULL,
  `min_price` DOUBLE NULL,
  `max_price` DOUBLE NULL,
  `currency` VARCHAR(10) NULL DEFAULT 'USD',
  `source` VARCHAR(50) NULL DEFAULT 'stubhub',
  `external_id` VARCHAR(100) NULL,
  `image_url` VARCHAR(500) NULL,
  `artist_id` INT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_event_artist_idx` (`artist_id` ASC),
  CONSTRAINT `fk_event_artist`
    FOREIGN KEY (`artist_id`)
    REFERENCES `artist` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user_tracked_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_tracked_event`;

CREATE TABLE IF NOT EXISTS `user_tracked_event` (
  `user_id` INT UNSIGNED NOT NULL,
  `event_id` INT NOT NULL,
  `tracked_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `notify` TINYINT(1) DEFAULT 1,
  PRIMARY KEY (`user_id`, `event_id`),
  INDEX `fk_ute_event_idx` (`event_id` ASC),
  CONSTRAINT `fk_ute_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_ute_event`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`id`)
    ON DELETE CASCADE
) ENGINE = InnoDB;

-- Sample events data for existing artists
INSERT INTO `event` (`name`, `venue`, `city`, `state`, `country`, `event_date`, `ticket_url`, `min_price`, `max_price`, `artist_id`, `source`, `external_id`) VALUES
('2Pac Tribute Concert', 'Madison Square Garden', 'New York', 'NY', 'US', '2026-07-15 20:00:00', 'https://www.stubhub.com/event/123456', 75.00, 350.00, 1, 'stubhub', 'sh-123456'),
('Adele Live 2026', 'The O2 Arena', 'London', NULL, 'UK', '2026-08-20 19:30:00', 'https://www.stubhub.com/event/234567', 120.00, 800.00, 3, 'stubhub', 'sh-234567'),
('Adele - Las Vegas Residency', 'Caesars Palace', 'Las Vegas', 'NV', 'US', '2026-09-10 21:00:00', 'https://www.stubhub.com/event/234568', 200.00, 1500.00, 3, 'stubhub', 'sh-234568'),
('Aerosmith Farewell Tour', 'Fenway Park', 'Boston', 'MA', 'US', '2026-06-28 19:00:00', 'https://www.stubhub.com/event/345678', 90.00, 500.00, 4, 'stubhub', 'sh-345678'),
('Alabama Shakes Reunion Show', 'Ryman Auditorium', 'Nashville', 'TN', 'US', '2026-10-05 20:00:00', 'https://www.stubhub.com/event/456789', 65.00, 250.00, 5, 'stubhub', 'sh-456789');
