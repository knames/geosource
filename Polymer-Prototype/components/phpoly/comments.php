<?php 
	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";

	$data = json_decode(file_get_contents('php://input'), true);

	$chname = $data["chname"]; //TODO these might need changing based on whats passed in
	$chowner = $data["chowner"];
	$pnum = $data["pnum"];

	// Create connection
	$mysqli = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($mysqli->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	//echo "Connected successfully <br>";

 	/** returns an array of the required info
 	@chname is the name of the channel
 	@chowner is the channel owners username
 	@pnum is the number of the post */
	function querytoarray ($chname,  $chowner, $pnum){
		global $mysqli;
		$result = $mysqli->query("SELECT pc_username, pc_comment, pc_time FROM post_comments 
				WHERE pc_chname = \"$chname\" AND pc_chowner = \"$chowner\" AND pc_number = \"$pnum\"
				ORDER BY pc_time ASC;");
		$rows = array();
		while($r = mysqli_fetch_assoc($result)){
			$rows[] = array('user'=>$r['pc_username'],'comment' => $r['pc_comment'], 'time'=>$r['pc_time']);
		}
		return $rows;
	}	

	if (!$chname == null && !$chowner == null && !$pnum == null){
		$comments = querytoarray($chname, $chowner, $pnum);
		print "[".json_encode($comments)."]";

	} else {
		$comments = "error: some or all of chname chowner and pnum were null";
	}


	$mysqli->close();
?>

