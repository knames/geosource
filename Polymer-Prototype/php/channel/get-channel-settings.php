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

	//$data = json_decode(file_get_contents('php://input'), true);
	$user = "Josh";//$data["username"];
	$channelname = "IsThisThingOn";//$data["channelname"];
	$channelowner = "Josh";//$data["channelowner"];


	if(isAdmin($conn, $user) || isOwner($conn, $user, $channelname, $channelowner) || isModerator($conn, $user, $channelname, $channelowner)) {
		$moderators = getModerators($conn, $channelname, $channelowner);
		$viewers = getViewers($conn, $channelname, $channelowner);
		$banned = getBanned($conn, $channelname, $channelowner);
		$public = isPublic($conn, $channelname, $channelowner);
	} else {
		$error = true;
		$error_message = "You do not have permission access settings for this channel";	
	}

	$conn->close();

	$response = array('error' => $error, 'error_message' => $error_message, 
			'moderators' => $moderators, 'viewers' => $viewers, 'banned' => $banned,
			'public' => $public);
			
	echo json_encode($response);

	function getModerators($conn, $channelname, $channelowner) {
		$stmt = $conn->prepare("SELECT cm_username FROM channelmods WHERE cm_chname=? AND cm_chowner=?");
		$stmt->bind_param("ss", $channelname, $channelowner);
		$stmt->execute();
		$stmt->bind_result($username);
		$users = [];
		while($stmt->fetch()) {
			$users[] = $username;
		}
		$stmt->close();
		return $users;
	}

	function getViewers($conn, $channelname, $channelowner) {
		$stmt = $conn->prepare("SELECT prv_username FROM private_view_channels WHERE prv_chname=? AND prv_chowner=?");
                $stmt->bind_param("ss", $channelname, $channelowner);
                $stmt->execute();
                $stmt->bind_result($username);
                $users = [];
                while($stmt->fetch()) {
                        $users[] = $username;
                }
                $stmt->close();
                return $users;
	}

	function getBanned($conn, $channelname, $channelowner) {
        	$stmt = $conn->prepare("SELECT cb_username FROM channel_banned WHERE cb_chname=? AND cb_chowner=?");        
		$stmt->bind_param("ss", $channelname, $channelowner);
                $stmt->execute();
                $stmt->bind_result($username);
                $users = [];
                while($stmt->fetch()) {
                        $users[] = $username;
                }
                $stmt->close();
                return $users;
	}

	function isPublic($conn, $channelname, $channelowner) {
		$stmt = $conn->prepare("SELECT ch_public FROM channels WHERE ch_name=? AND ch_owner=?");
		$stmt->bind_param("ss", $channelname, $channelowner);
		$stmt->execute();
		$stmt->bind_result($public);
		$stmt->fetch();
		$stmt->close();
		return (bool)$public;
	}
?>

