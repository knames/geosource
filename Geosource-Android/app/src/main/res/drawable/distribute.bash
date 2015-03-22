#!/bin/bash


DIRS=(drawable-hdpi/  drawable-ldpi/  drawable-mdpi/  drawable-xhdpi/)

for dir in "${DIRS[@]}"; do
	cd res/$dir;
	mv ic_launcher.png ../../../$dir/$1;
	cd ../..;
done

cd ../..

exit