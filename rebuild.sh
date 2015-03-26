#!/bin/bash
killall -9 java
mysql -pdevsnake371 << EOF
use dev;
SOURCE /var/www/okenso.com/cmpt371group2/Database/dbdrop.sql;
SOURCE /var/www/okenso.com/cmpt371group2/Database/dbinit.sql;
EOF
java -jar HoopSnake-GeoSource.jar
