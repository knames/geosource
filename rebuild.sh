#!/bin/bash
rm -rf Polymer-Prototype/media/fieldContent
rm -rf media
killall -9 java
mysql -pdevsnake371 << EOF
drop database dev;
create database dev;
EOF
mysql -pdevsnake371 << EOF
use dev;
SOURCE /var/www/okenso.com/cmpt371group2/Database/dbinit.sql;
EOF
