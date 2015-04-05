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
        $poster = $data["poster"];
        $time = $data["time"];
        $pid = $data["pid"];
        $channelname = $data["channelname"];
        $channelowner = $data["channelowner"];

        if(isAdmin($conn, $user) || isOwner($conn, $user, $channelname, $channelowner) || isModerator($conn, $user, $channelname, $channelowner)) {
            deleteComment($conn, $poster, $time, $pid, $channelname, $channelowner);
        } else {
            $error = true;
            $error_message = "You do not have permission to delete posts from this channel";
        }

        $conn->close();

        $response = array('error' => $error, 'error_message' => $error_message, 
            'channelname' => $channelname, 'channelowner' => $channelowner, 'pid' => $pid,
            'poster' => $poster, 'time' => $time);
        echo json_encode($response);

        function deleteComment($conn, $user, $time, $pid, $channelname, $channelowner) {
            global $error, $error_message;
            $stmt = $conn->prepare("DELETE FROM post_comments WHERE 
                pc_username=? AND pc_time=? AND pc_chname=? AND pc_chowner=? AND pc_number=?");
            $stmt->bind_param("ssssi", $user, $time, $channelname, $channelowner, $pid);
            $stmt->execute();
            $stmt->close();
        }
?>

