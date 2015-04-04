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
                addBanned($conn, $banned, $channelname, $channelowner);
        } else {
                $error = true;
                $error_message = "You do not have permission to ban a user in this channel";
        }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message);
        echo json_encode($response);

        function addBanned($conn, $banned, $channelname, $channelowner) {
                global $error, $error_message;

                if(isUser($conn, $banned)) {
                        $sql = "INSERT INTO channel_banned (cb_username, cb_chname, cb_chowner) 
                                VALUES (\"".$banned."\", \"".$channelname."\", \"".$channelowner."\");";
                        if(!$conn->query($sql)) {
                                $error = true;
                                $error_message = "User is already banned";
                        }
                } else {
                        $error = true;
                        $error_message = "User does not exist";
                }
        }
?>

