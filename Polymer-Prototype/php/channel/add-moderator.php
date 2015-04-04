<?php
        include '../account/permissions.php';

	$servername = "okenso.com";
	$username = "hdev";
	$password = "devsnake371";
	$dbname = "dev";

	$conn = new mysqli($servername, $username, $password, $dbname);
	if($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	}

	$error = false;
	$error_message = "";

	$data = json_decode(file_get_contents('php://input'), true);
        $user = $data["username"];
        $moderator = $data["moderator"];
        $channelname = $data["channelname"];
        $channelowner = $data["channelowner"];

	if(isAdmin($conn, $user) || isOwner($conn, $user, $channelname, $channelowner)) {
		addModerator($conn, $moderator, $channelname, $channelowner);
	} else {
		$error = true;
		$error_message = "You do not have permission to add a moderator to this channel";	
	}

	$conn->close();

	$response = array('error' => $error, 'error_message' => $error_message);
	echo json_encode($response);

	function addModerator($conn, $moderator, $channelname, $channelowner) {
		global $error, $error_message;

		if(isUser($conn, $moderator)) {
			$sql = "INSERT INTO channelmods (cm_username, cm_chname, cm_chowner) 
				VALUES (\"".$moderator."\", \"".$channelname."\", \"".$channelowner."\");";
			if(!$conn->query($sql)) {
				$error = true;
				$error_message = "User is already a moderator";
			}
		} else {
			$error = true;
			$error_message = "User does not exist";
		}
	}
?>
