-- ----------------------------
-- Table structure for feed
-- ----------------------------
DROP TABLE IF EXISTS `feed`;
CREATE TABLE `feed` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `data` TINYTEXT NOT NULL,
  `create_date` datetime NOT NULL,
  `type` int NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_index` (`user_id` ASC )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;