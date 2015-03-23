#!/bin/bash
rm -rf media
mkdir media media/spec media/fieldContent
mysql -pdevsnake371 << EOF
use dev;
SOURCE /var/www/okenso.com/cmpt371group2/Database/dbdrop.sql;
SOURCE /var/www/okenso.com/cmpt371group2/Database/dbinit.sql;
EOF
java -jar WriteAndStore.jar
