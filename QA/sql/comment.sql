-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `content` text NOT NULL,
  `create_date` datetime NOT NULL,
  `entity_id` int(11) NOT NULL,
  `entity_type` int(11) NOT NULL,
  `status` int null default 0,
  PRIMARY KEY (`id`),
  INDEX `entity_index` (`entity_id` ASC,`entity_type` ASC),
  INDEX `user_index` (`user_id` ASC ),
  INDEX `date_index` (`create_date` ASC )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;