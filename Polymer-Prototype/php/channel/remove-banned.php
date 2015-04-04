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
        $banned = $data["banned"];
        $channelname = $data["channelname"];
        $channelowner = $data["channelowner"];

        if(isAdmin($conn, $user) || isOwner($conn, $user, $channelname, $channelowner) || isModerator($conn, $user, $channelname, $channelowner)) {
                removeBanned($conn, $banned, $channelname, $channelowner);
        } else {
                $error = true;
                $error_message = "You do not have permission to unban a user in this channel";
        }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message, 'banned' => $banned);
        echo json_encode($response);

        function removeBanned($conn, $banned, $channelname, $channelowner) {
                $sql = "DELETE FROM channel_banned WHERE 
			cb_username = \"".$banned."\" AND cb_chname = \"".$channelname."\" AND cb_chowner = \"".$channelowner."\";";
                $conn->query($sql);
        }
?>

