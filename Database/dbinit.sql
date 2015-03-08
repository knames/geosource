

CREATE TABLE users (
	u_identity varchar(256),
	u_username varchar(25) PRIMARY KEY
	);

CREATE TABLE channels (
	ch_name varchar(50) NOT NULL,
	ch_owner varchar(256) NOT NULL,
	ch_spec INT UNSIGNED  NOT NULL,
	ch_public BOOL NOT NULL,
	FOREIGN KEY (ch_owner) REFERENCES users (u_username),
	PRIMARY KEY (ch_name, ch_owner)
	);

CREATE TABLE channelmods (
	cm_m_identity varchar(256) NOT NULL,
	cm_m_name varchar(50) NOT NULL,
	FOREIGN KEY (cm_m_identity) REFERENCES users (u_username),
	FOREIGN KEY (cm_m_name) REFERENCES channels (ch_name)
	);

CREATE TABLE channelfavs (
	ch_fav_identity varchar(256) NOT NULL,
	ch_fav_chname varchar(50) NOT NULL,
	FOREIGN KEY (ch_fav_identity) REFERENCES users (u_username),
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



