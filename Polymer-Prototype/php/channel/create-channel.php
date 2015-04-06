<?php 
	//db credentials
	$servername = "okenso.com";
	$usernamme = "hdev";
	$dbname = "dev";
	$password = "devsnake371";

	$data = json_decode(file_get_contents('php://input'), true);

	$channelname = $data["name"];
	$channelowner = $data["owner"];
	
	$err = false;
	$errMsg = "";

	if (empty($channelname)) {
		$err = true;
		$errMsg  = "Please enter a channel name";
	} else if (!preg_match("/^[a-zA-Z0-9]*$/", $channelname)) {
		$err = true;
		$errMsg = "Only letters and numbers allowed.";
//	} else if (channelExists($channelname, $channelowner)) {
//		$err = true;
//		$errMsg = "You've already created a channel with that name.";
	} else {
		$socket = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
    			or die("error: could not create socket\n");

		socket_connect($socket, 'localhost', 9001) //Connecting to to server using that socket
			or die("error: could not connect to host\n");
	
		socket_write($socket, chr(2), 1);
	
		$command = '[{"command": "CREATE_CHANNEL"},';
		socket_write($socket, $command, strlen($command));

		$channel = json_encode($data)."]";
		socket_write($socket, $channel, strlen($channel));
	}

	$resp = array('error' => $err, 'message' => $errMsg, 'channel' => $data);
	echo json_encode($resp);
?>
