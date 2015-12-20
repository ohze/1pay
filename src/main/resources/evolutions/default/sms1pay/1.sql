# --- !Ups
CREATE TABLE IF NOT EXISTS 1pay_log (
	id INT(11) NOT NULL AUTO_INCREMENT,
  request_id VARCHAR(100) NOT NULL,
  phone VARCHAR(15) NOT NULL,
  uid INT(11) NOT NULL,
  status VARCHAR(10) NOT NULL,
  `output` VARCHAR(255) NOT NULL,
  amount INT(11) NOT NULL,
  updated INT(11) NOT NULL,
  type VARCHAR(10) NOT NULL,
  PRIMARY KEY (id),
  KEY request_id (request_id),
  KEY updated (updated),
  KEY phone (phone),
  KEY status (status),
  KEY type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
