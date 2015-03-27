

CREATE TABLE users (
	u_identity varchar(64) NOT NULL UNIQUE,
	u_username varchar(25) PRIMARY KEY
	);

CREATE TABLE channels (
	ch_name varchar(50) NOT NULL,
	ch_owner varchar(25) NOT NULL,
	ch_spec INT UNSIGNED  NOT NULL,
	ch_public BOOL NOT NULL,
	FOREIGN KEY (ch_owner) REFERENCES users (u_username),
	PRIMARY KEY (ch_name, ch_owner)
	);

CREATE TABLE channelmods (
	cm_username varchar(25) NOT NULL,
	cm_chname varchar(50) NOT NULL,
	cm_chowner varchar(25) NOT NULL,
	FOREIGN KEY (cm_username) REFERENCES users (u_username),
	FOREIGN KEY (cm_chname) REFERENCES channels (ch_name),
	FOREIGN KEY (cm_chowner) REFERENCES users (u_username),
	PRIMARY KEY (cm_username, cm_chname, cm_chowner)
	);

CREATE TABLE channelfavs (
	ch_fav_username varchar(25) NOT NULL,
	ch_fav_chname varchar(50) NOT NULL,
	ch_fav_chowner varchar(25) NOT NULL,
	FOREIGN KEY (ch_fav_username) REFERENCES users (u_username),
	FOREIGN KEY (ch_fav_chname) REFERENCES channels (ch_name),
	FOREIGN KEY (ch_fav_chowner) REFERENCES users (u_username),
	PRIMARY KEY (ch_fav_username, ch_fav_chname, ch_fav_chowner)
	);


CREATE TABLE admin (
	a_username varchar(25) PRIMARY KEY,
	FOREIGN KEY (a_username) REFERENCES users (u_username)
	);

CREATE TABLE private_view_channels (
	prv_username varchar(25) NOT NULL,
	prv_chname varchar(50) NOT NULL,
	prv_chowner varchar(25) NOT NULL,
	FOREIGN KEY (prv_username) REFERENCES users (u_username),
	FOREIGN KEY (prv_chname) REFERENCES channels (ch_name),
	FOREIGN KEY (prv_chowner) REFERENCES users (u_username),
	PRIMARY KEY (prv_username, prv_chname, prv_chowner)
	);

CREATE TABLE users_fav_posts (
	ufp_username varchar(25) NOT NULL,
	ufp_chname varchar(50) NOT NULL,
	ufp_chowner varchar(25) NOT NULL,
	ufp_number int UNSIGNED NOT NULL,
	FOREIGN KEY (ufp_username) REFERENCES users (u_username),
	FOREIGN KEY (ufp_chname) REFERENCES channels (ch_name),
	FOREIGN KEY (ufp_chowner) REFERENCES users (u_username),
	PRIMARY KEY (ufp_username, ufp_chname, ufp_chowner, ufp_number)
	);


-- comment this out when done with dummydata
source /var/www/okenso.com/cmpt371group2/Database/dbdummydata.sql

/** Sample Table
CREATE TABLE tblname (
	name domain NOT NULL,
	name2 domain2 NOT NULL,
	FOREIGN KEY (nameinthistable) REFERENCES tblname (nameinothertable),
	PRIMARY KEY (name, name2)
);
*/



