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

insert into channelfavs values ("okenso", "pothole", "cindy");
insert into channelfavs values ("okenso", "pothole", "okenso");
insert into channelfavs values ("okenso", "okenso's channel", "okenso");

CREATE TABLE posts_okenso_pothole (
	p_poster varchar(25) NOT NULL,
	p_number INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	p_title varchar(100),
	p_time DATETIME,
	p_field1 varchar(50), # will be named after the title
	p_field2 varchar(50), # how big should these be?
	FOREIGN KEY (p_poster) REFERENCES users (u_username)
	);

insert into posts_okenso_pothole 
		(p_poster)
	values (
		"okenso"		
	);

insert into admin (a_username) values ("okenso");

insert into private_view_channels values 
	("okenso", "franks red hot", "frank"),
	("okenso", "frank and beans", "frank"),
	("okenso", "pothole", "cindy")
	;


	CREATE TABLE posts_okenso_march13 (
	 p_poster varchar(25) NOT NULL,
	 p_number INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	 p_TitleText varchar(100),
	 p_time DATETIME,
	 p_lat DOUBLE,
	 p_long DOUBLE,
	 p_PictureField varchar(100),
	 FOREIGN KEY (p_poster) REFERENCES users (u_username)
	 );


insert into channelmods values
	("okenso", "pothole", "cindy"),
	("okenso", "frank and beans", "frank")
	;



insert into users_fav_posts values 
	("okenso", "pothole", "okenso", 1)
	;


