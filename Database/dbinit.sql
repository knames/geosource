START TRANSACTION;

CREATE TABLE users (
	u_email varchar(100) PRIMARY KEY,
	u_username varchar(25)
	);

CREATE TABLE channels (
	ch_name varchar(50) NOT NULL,
	ch_owner varchar(100) NOT NULL,
	ch_spec varchar(100) NOT NULL,
	ch_public BOOL NOT NULL,
	FOREIGN KEY (ch_owner) REFERENCES users (u_email),
	PRIMARY KEY (ch_name, ch_owner)
	);

CREATE TABLE channelmods (
	cm_m_email varchar(100) NOT NULL,
	cm_m_name varchar(50) NOT NULL,
	FOREIGN KEY (cm_m_email) REFERENCES users (u_email),
	FOREIGN KEY (cm_m_name) REFERENCES channels (ch_name)
	);

CREATE TABLE channelfavs (
	ch_fav_email varchar(100) NOT NULL,
	ch_fav_chname varchar(50) NOT NULL,
	FOREIGN KEY (ch_fav_email) REFERENCES users (u_email),
	FOREIGN KEY (ch_fav_chname) REFERENCES channels (ch_name)
	);


/** Sample Table
CREATE TABLE tblname (
	name domain NOT NULL,
	name2 domain2 NOT NULL,
	FOREIGN KEY (nameinthistable) REFERENCES tblname (nameinothertable),
	PRIMARY KEY (name, name2)
);
*/



