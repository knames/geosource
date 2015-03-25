<?php 
	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";

	$data = json_decode(file_get_contents('php://input'), true);

	$userid = $data["gid"]; // make sure it's the hashed value

	// Create connection
	$mysqli = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($mysqli->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	//echo "Connected successfully <br>";



	/** get username */
 	$result = $mysqli->query("SELECT * FROM users WHERE u_identity = \"$userid\";");
	$uname = $result->fetch_assoc();
 	//echo $row['u_username'];
 	$uname = $uname['u_username'];

 	/** returns an array of the required info
 	@channel is the name of the channel
 	@schname is the select of the row name 
 	@schowner is the select of the row owner
 	@wuser is the username we want to match */
	function querytoarray ($channel,  $schname, $schowner, $wuser){
		global $mysqli, $uname;
		$result = $mysqli->query("SELECT $schname, $schowner FROM $channel 
				WHERE $wuser = \"$uname\";");
		$rows = array();
		while($r = mysqli_fetch_assoc($result)){
			$rows[] = array('name'=>$r[$schname],'owner' => $r[$schowner]);
		}
		return $rows;
	}	


 	if (!$uname == null){
 		/** check if admin */
 		$result = $mysqli->query("SELECT a_username FROM admin WHERE a_username = \"$uname\";");
		$aname = $result->fetch_assoc();
		$aname = $aname['a_username'];
		if (!$aname == null){
			$aname = TRUE;
		} else {
			$aname = FALSE;
		}

		/** find subscribed channels */
		$sub = querytoarray("channelfavs", "ch_fav_chname", "ch_fav_chowner", "ch_fav_username");

		/** check private channel subs */
		$prv = querytoarray("private_view_channels", "prv_chname", "prv_chowner", "prv_username");

		/** check moderator status */
		$mod =  querytoarray("channelmods", "cm_chname", "cm_chowner", "cm_username");

		/** check owner status */
		//select table_name from information_schema.tables where table_name LIKE '%_okenso_%';
 		$result = $mysqli->query("SELECT table_name FROM information_schema.tables WHERE table_name LIKE \"%$uname%\";");
 		$rows = array();
 		while ($r = mysqli_fetch_assoc($result)){
 			$table = $r['table_name'];
 			//print $table;
 			/** The monstrosity below strips posts_ and _username from the table names.*/
 			$rows[] = array('name'=>str_replace($uname."_", '',str_replace("posts_",'',$table)), 'owner'=>$uname);
 		}
 		$own = $rows;
		//SAMPLE str_replace("world","Peter","Hello world!"); 

 		/** check favorite posts */
 		$result = $mysqli->query("SELECT ufp_chname, ufp_chowner, ufp_number FROM users_fav_posts WHERE ufp_username = \"$uname\";");
 		$rows = array();
		while($r = mysqli_fetch_assoc($result)){
			$rows[] = array('channel'=>$r['ufp_chname'],'owner' => $r['ufp_chowner'],'number'=>$r['ufp_number']);
		}
		$favs = $rows;




		//$arr = array('a' => 1, 'b' => 2, 'c' => 3, 'd' => 4, 'e' => 5);
		/** creates the array of this information */
		$arr = array('username' => $uname, 'admin' => $aname, 'subscriber' => $sub, 
			'viewer' => $prv, 'moderator'=>$mod, 'owner'=>$own, 'favorites'=>$favs);

		
		print "[".json_encode($arr)."]";

 	}
 	else {
 		echo "[".json_encode(null)."]";
 	}
	


	//print json_encode("\"username\":".$row['u_username']);




	$mysqli->close();
?>

