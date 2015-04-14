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

        favorite($conn, $user, $pid, $channelname, $channelowner);

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message, 
        	'channelname' => $channelname, 'channelowner' => $channelowner, 'pid' => $pid);
        echo json_encode($response);

        function favorite($conn, $user, $pid, $channelname, $channelowner) {
    		global $error, $error_message;
    		$stmt = $conn->prepare("INSERT INTO users_fav_posts (ufp_username, ufp_chname, ufp_chowner, ufp_number) VALUES (?, ?, ?, ?)");
			$stmt->bind_param("sssi", $user, $channelname, $channelowner, $pid);
			if(!$stmt->execute()) {
				$error = true;
				$error_message = "Either the post has already been favorited, or the post no longer exists";
			}
			$stmt->close();
        }
?>

