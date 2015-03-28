#!/bin/bash
killall -9 java
rm nohup.out
nohup java -jar HoopSnake-GeoSource.jar 9001 5 &
