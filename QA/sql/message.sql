-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL auto_increment,
  `from_id` int(11) NOT NULL,
  `to_id` int(11) NOT NULL,
  `content` varchar(225) NOT NULL,
  `create_date` datetime NOT NULL,
  `has_read` int null default 0,
  `conversation_id` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `conversation_id_index` (`conversation_id` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;