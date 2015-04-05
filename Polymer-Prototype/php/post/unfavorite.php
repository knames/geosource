<?php
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
        $pid = $data["pid"];
        $channelname = $data["channelname"];
        $channelowner = $data["channelowner"];

        unfavorite($conn, $user, $banned, $channelname, $channelowner);

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message, 
        	'channelname' => $channelname, 'channelowner' => $channelowner, 'pid' => $pid);
        echo json_encode($response);

        function unfavorite($conn, $user, $pid, $channelname, $channelowner) {
    		global $error, $error_message;
    		$stmt = $conn->prepare("DELETE FROM users_fav_posts WHERE ufp_username=? AND ufp_chname=? AND ufp_chowner=? AND ufp_number=?");
			$stmt->bind_param("sssi", $user, $channelname, $channelowner, $pid);
			$stmt->execute();
			$stmt->close();
        }
?>

