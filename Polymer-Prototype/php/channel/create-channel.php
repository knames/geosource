<?php 
	//db credentials
	$servername = "okenso.com";
	$usernamme = "hdev";
	$dbname = "dev";
	$password = "devsnake371";

	$data = json_decode(file_get_contents('php://input'), true);

	$channelname = $data["name"];
	$channelowner = $data["owner"];
	
	$error = false;
	$error_message = "";

	if(legalChannel($channelname) && legalFields($fields)) {
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

	$resp = array('error' => $error, 'error_message' => $error_message, 'channel' => $data);
	echo json_encode($resp);

	function legalString($string) {
		globals $error, $error_message;
		if (empty($channelname)) {
			$error = true;
			$error_message  = "Cannot be empty.";
			return false;
		} else if (!preg_match("/^[a-zA-Z0-9]*$/", $channelname)) {
			$error = true;
			$error_message = "Only letters and numbers allowed.";
			return false;
		}
		return true;
	}

	function legalChannel($channelname) {
		globals $error, $error_message;
		if(!legalString($channelname)) {
			$error_message = "Illegal channel name. " . $error_message;
			return false;
		}
		return true;
	}

	function legalFields($fields) {
		globals $error, $error_message;
		foreach ($fields as $field) {
			if(!legalString($field)) {
				$error_message = "Illegal field name. " . $error_message;
				return false;
			} else if ($field == "poster" || $field == "number" || $field == "title" || $field == "geotag") {
				$error = true;
				$error_message = "Illegal field name."
				return false;
			}		
		}
		if(count($fields) < count(array_unique($fields))) {
			$error = true;
			$error_message = "Illegal field name. All field names must be unique.";
			return false;
		}
		return true;
	}
?>
