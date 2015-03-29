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

	$err = false;
	$errMsg="";

	$conn = new mysqli($servername, $username, $password, $dbname);

	$sql = "insert into channelfavs (ch_fav_username, ch_fav_chname, ch_fav_chowner) values (\"".$user."\",\"".$channelname."\",\"".$owner."\");";
	
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	else { 
		if ($conn->query($sql)){
			//echo "inserted hash!<br>";
		}
		else{
			/** If this gets hit, then you either need to change the userid (fake google id), or somehow
			google assigned two people the same ID
			 */
			$errMsg = "Already subscribed";
			$err = true;
		}
	}
	
	$conn->close();

	$channel = array('name' => $channelname, 'owner' => $owner);
	$resp = array('error' => $err, 'message' => $errMsg, 'channel' => $channel);

	echo json_encode($resp);
?>

