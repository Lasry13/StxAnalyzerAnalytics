CREATE TABLE `raw_data_hourly` (
    `symbol` VARCHAR(20) NOT NULL,
    `name` VARCHAR(20) NOT NULL,
    `open` float NOT NULL,
    `high` float NOT NULL,
    `low` float NOT NULL,
	`close` float NOT NULL,
	`volume` BIGINT NOT NULL,
    `date` DATE NOT NULL
)