#!/bin/bash
killall -9 java
rm nohup.out
nohup java -jar HoopSnake-Geosource.jar 9001 5 &
