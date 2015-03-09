insert into users values ("0001", "okenso");
insert into users values ("0002", "frank");
insert into users values ("0003", "cindy");
insert into users values ("0004", "xxLegolasxxYoloxx");

insert into channels values ("okenso's channel", "okenso", 1, true);	
insert into channels values ("okenso's other channel", "okenso", 2, true);
insert into channels values ("pothole", "okenso", 3, true);
insert into channels values ("franks red hot", "frank", 1, true);
insert into channels values ("frank and beans", "frank", 2, true);
insert into channels values ("you only yolo once", "xxLegolasxxYoloxx", 1, true);
insert into channels values ("pothole", "cindy", 1, true);

CREATE TABLE posts_okenso_pothole (
	p_poster varchar(25) NOT NULL,
	p_number INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	p_time DATETIME NOT NULL,
	p_field1 varchar(50) NOT NULL, # will be named after the title
	p_field2 varchar(50) NOT NULL, # how big should these be?
	p_field3 varchar(50) NOT NULL,
	FOREIGN KEY (p_poster) REFERENCES users (u_username)
	);

insert into posts_okenso_pothole 
		(p_poster, p_time, p_field1, p_field2, p_field3)
	values (
		"okenso", '2015-03-08 12:00:00', "", "", ""
		);
