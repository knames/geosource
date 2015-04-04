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
                addViewer($conn, $viewer, $channelname, $channelowner);
        } else {
                $error = true;
                $error_message = "You do not have permission to add a viewer to this channel";       
        }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message);
        echo json_encode($response);

        function addViewer($conn, $viewer, $channelname, $channelowner) {
                global $error, $error_message;

                if(isUser($conn, $viewer)) {
                        $sql = "INSERT INTO private_view_channels (prv_username, prv_chname, prv_chowner) 
                                VALUES (\"".$viewer."\", \"".$channelname."\", \"".$channelowner."\");";
                        if(!$conn->query($sql)) {
                                $error = true;
                                $error_message = "User is already a viewer";
                        }
                } else {
                        $error = true;
                        $error_message = "User does not exist";
                }
        }
?>
