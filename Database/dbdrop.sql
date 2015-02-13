BEGIN;

DROP TABLE channelfavs CASCADE;
DROP TABLE channelmods CASCADE;
DROP TABLE channels CASCADE;
DROP TABLE users CASCADE;

DROP DOMAIN email;
DROP DOMAIN username;
DROP DOMAIN channelname;
DROP DOMAIN spec;
DROP DOMAIN ispublicl;

--COMMIT;
ROLLBACK; -- replace with COMMIT; to submit.

