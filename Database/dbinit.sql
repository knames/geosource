BEGIN;


-- google stuff
CREATE DOMAIN varchar(100) AS varchar(100);
CREATE DOMAIN varchar(25) AS varchar(25); -- restriction




-- channel stuff
CREATE DOMAIN varchar(50) as varchar(50); -- this is a restriction
CREATE DOMAIN spec as varchar(100); -- the filepath string
CREATE DOMAIN BOOLEAN as BOOLEAN; -- whether it is visible public or not

-- How will we handle posts?
-- Do we care about the date something was created?

CREATE TABLE users (
	u_email varchar(100) PRIMARY KEY,
	u_username varchar(25)
	);

CREATE TABLE channels (
	ch_name varchar(50) NOT NULL,
	ch_owner varchar(100) NOT NULL,
	ch_spec spec NOT NULL,
	ch_public BOOLEAN NOT NULL,
	FOREIGN KEY (ch_owner) REFERENCES users (u_email),
	PRIMARY KEY (ch_name, ch_owner)
	);

CREATE TABLE channelmods (
	cm_m_email varchar(100) NOT NULL,
	cm_m_name varchar(50) NOT NULL,
	FOREIGN KEY (cm_u_email) REFERENCES users (u_email),
	FOREIGN KEY (cm_ch_name) REFERENCES channels (ch_name)
	);

CREATE TABLE channelfavs (
	ch_fav_email varchar(100) NOT NULL,
	ch_fav_chname varchar(50) NOT NULL
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

--COMMIT;
--ROLLBACK; -- replace with COMMIT; to submit.

