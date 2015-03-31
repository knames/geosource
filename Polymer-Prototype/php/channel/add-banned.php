<?php 
	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";

	$data = json_decode(file_get_contents('php://input'), true);
	$user = $data["username"];
	$channelname = $data["channelname"];
	$owner = $data["owner"];

	$error = false;
	$error_message="";

	$conn = new mysqli($servername, $username, $password, $dbname);

	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 

	$sql = "INSERT INTO channel_banned (cb_username, cb_chname, cb_chowner) 
	VALUES (\"".$user."\",\"".$channelname."\",\"".$owner."\");";

	if(!$conn->query($sql)) {
		$error = true;
		$error_message = "The user is either already banned or does not exist";
	}

	$conn->close();

	$channel = array('name' => $channelname, 'owner' => $owner);
	$resp = array('error' => $error, 'error_message' => $error_message, 'username' => $user, 'channel' => $channel);

	echo json_encode($resp);
?>

