-- ----------------------------
-- Table structure for login_ticket
-- ----------------------------
DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(60) NOT NULL,
  `expired` datetime NOT NULL,
  `status` int null default 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ticket_UNIQUE` (`ticket` ASC )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;