<?php 
	//db credentials

	$socket = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
    	or die("error: could not create socket\n");

	socket_connect($socket, 'localhost', 9001) //Connecting to to server using that socket
		or die("error: could not connect to host\n");

	echo "Connected";
?>