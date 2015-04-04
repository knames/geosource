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
                removeModerator($conn, $moderator, $channelname, $channelowner);
        } else {
                $error = true;
                $error_message = "You do not have permission to ban a user in this channel";
        }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message);
        echo json_encode($response);

        function removeModerator($conn, $moderator, $channelname, $channelowner) {
                $sql = "DELETE FROM channelmods WHERE
                        cm_username = \"".$moderator."\" AND cm_chname = \"".$channelname."\" AND cm_chowner = \"".$channelowner."\";";
                $conn->query($sql);
        }
?>

