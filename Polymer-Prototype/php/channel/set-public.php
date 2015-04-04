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
        $public = $data["public"];
        $channelname = $data["channelname"];
        $channelowner = $data["channelowner"];

        if(isAdmin($conn, $user) || isOwner($conn, $user, $channelname, $channelowner)) {
                setPublic($conn, $public, $channelname, $channelowner);
        } else {
                $error = true;
                $error_message = "You do not have permission to change this channel's access level";
        }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message);
        echo json_encode($response);

        function setPublic($conn, $public, $channelname, $channelowner) {
                $sql = "UPDATE channels
			SET ch_public = " .((int)$public).
			" WHERE ch_name = \"".$channelname."\" AND ch_owner = \"".$channelowner."\";";
                $conn->query($sql);
        }
?>

