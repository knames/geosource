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
        $viewer = $data["viewer"];
        $channelname = $data["channelname"];
        $channelowner = $data["channelowner"];

        if(isAdmin($conn, $user) || isOwner($conn, $user, $channelname, $channelowner) || isModerator($conn, $user, $channelname, $channelowner)) {
                removeViewer($conn, $viewer, $channelname, $channelowner);
        } else {
                $error = true;
                $error_message = "You do not have permission to remove a viewer from this channel";
        }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message);
        echo json_encode($response);

        function removeViewer($conn, $viewer, $channelname, $channelowner) {
                $sql = "DELETE FROM private_view_channels WHERE
                        prv_username = \"".$viewer."\" AND prv_chname = \"".$channelname."\" AND prv_chowner = \"".$channelowner."\";";
                $conn->query($sql);
        }
?>

