#!/bin/bash
rm -rf media
killall -9 java
mysql -pdevsnake371 << EOF
use dev;
DROP TABLE posts_okenso_testing CASCADE;
DROP TABLE private_view_channels CASCADE;
DROP TABLE admin CASCADE;
DROP TABLE users_fav_posts CASCADE;
DROP TABLE channelfavs CASCADE;
DROP TABLE channelmods CASCADE;
DROP TABLE channels CASCADE;
DROP TABLE users CASCADE;
EOF
mysql -pdevsnake371 << EOF
use dev;
SOURCE /var/www/okenso.com/cmpt371group2/Database/dbinit.sql;
EOF
