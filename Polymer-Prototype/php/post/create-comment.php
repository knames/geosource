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
        $comment = $data["comment"];
        $pid = $data["pid"];
        $channelname = $data["channelname"];
        $channelowner = $data["channelowner"];

        if(isBanned($conn, $user, $channelname, $channelowner)) {
        	$error = true;
        	$error_message = "You are banned from posting in this channel";
        } else if(strlen($comment) > 256) {
        	$error = true;
        	$error_message = "The comment is longer than 256 characters";
        } else {
	        createComment($conn, $user, $comment, $pid, $channelname, $channelowner);
	        $time = getCommentTime($conn, $user, $pid, $channelname, $channelowner);
	    }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message, 
        	'channelname' => $channelname, 'channelowner' => $channelowner, 'pid' => $pid,
        	'comment' => $comment, 'time' => $time);
        echo json_encode($response);

        function createComment($conn, $user, $comment, $pid, $channelname, $channelowner) {
    		global $error, $error_message;
    		$stmt = $conn->prepare("INSERT INTO post_comments (pc_username, pc_comment, pc_time, pc_chname, pc_chowner, pc_number) 
    			VALUES (?, ?, NOW(), ?, ?, ?)");
			$stmt->bind_param("ssssi", $user, $comment, $channelname, $channelowner, $pid);
			if(!$stmt->execute()) {
				$error = true;
				$error_message = "Either the comment has already been posted, or the post no longer exists";
			}
			$stmt->close();
        }

        //returns the time of the most recent comment posted by the given user to the given channel
        function getCommentTime($conn, $user, $pid, $channelname, $channelowner) {
            $stmt = $conn->prepare("SELECT pc_time FROM post_comments 
            	WHERE pc_username=? AND pc_chname=? AND pc_chowner=? AND pc_number=?
            	ORDER BY pc_time DESC") ;
            $stmt->bind_param("sssssi", $user, $comment, $timestamp, $channelname, $channelowner, $pid);
            $stmt->execute();
            $stmt->bind_result($time);
            $stmt->fetch();
			$stmt->close();
			return $time;
        }
?>

