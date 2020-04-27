CREATE TABLE `t_locks` (
  `lkey` varchar(128) NOT NULL,
  `lvalue` varchar(128) NOT NULL,
  `expire_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`lkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
