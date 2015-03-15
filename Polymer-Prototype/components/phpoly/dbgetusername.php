<?php 
	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";



	$userid = '0002'; // make sure it's the hashed value


	// Create connection
	$mysqli = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($mysqli->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	//echo "Connected successfully <br>";

 	$result = $mysqli->query("SELECT * FROM users WHERE u_identity = \"$userid\";");
	$uname = $result->fetch_assoc();
 	//echo $row['u_username'];
 	$uname = $uname['u_username'];


 	if (!$uname == null){
		//$arr = array('a' => 1, 'b' => 2, 'c' => 3, 'd' => 4, 'e' => 5);
		$arr = array('username' => $uname, 'b' => 2);

		
		print "[".json_encode($arr)."]";

 	}
 	else {
 		echo "[".json_encode(null)."]";
 	}
	


	//print json_encode("\"username\":".$row['u_username']);


	$mysqli->close();
?>

