<?php 
	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";

	$data = json_decode(file_get_contents('php://input'), true);
	$userid = $data["gid"];
	$newUname = $data["username"];

	$err = false;
	$errMsg="";

	if (empty($newUname)){
		$errMsg="Please enter a user name";
		$err = true;
	} else if (!preg_match("/^[a-zA-Z0-9]*$/",$newUname)){
		$errMsg = "Only letters and numbers allowed.";
		$err = true;
	} else if (nameExists($newUname)){
		$errMsg = "That username is taken, please choose another.";
		$err = true;
	} else {
		insertHash($newUname);
	}

	$resp = array('error' => $err, 'message' => $errMsg, 'username' => $newUname, 'userid' => $userid);

	echo json_encode($resp);



	/** checks to see if this new username is already in the database */
	function nameExists($newUname){
		global $servername, $username, $dbname, $password, $userid;
		$conn = new mysqli($servername, $username, $password, $dbname);

		// Check connection
		if ($conn->connect_error) {
		    die("Connection failed: " . $conn->connect_error);
		} 
		else {
			$sql = "SELECT * FROM users WHERE u_username = \"".$newUname."\";";
			$result = $conn->query($sql);
			if($result->num_rows > 0){ // if > 0, then obviously this name is taken
				$conn->close();
				return true;
			}
			else {
				$conn->close();
				return false;
			}
		} 
	}

	/** strip dangerous stuff out */
	function cleanName($newUname){
		$newUname = trim($newUname);
		$newUname = stripslashes($newUname);
		$newUname = htmlspecialchars($newUname);
		return $newUname;
	}

	/** insert the newly created hash name in. */
	function insertHash($newUname){
		global $servername, $username, $dbname, $password, $userid, $err, $errMsg;
		// Create connection
		$conn = new mysqli($servername, $username, $password, $dbname);

		// Check connection
		if ($conn->connect_error) {
		    die("Connection failed: " . $conn->connect_error);
		} 

		// Hash insert
		$options = ['cost' => 11, 'salt' => '$VuOmsSDxuiwJiquy3PgaG',]; 
		$hash = password_hash($userid, PASSWORD_DEFAULT, $options);
		//$userid = mysql_real_escape_string($userid);
		//$newUname = mysql_real_escape_string($newUname)''
		$sql = "insert into users (u_identity, u_username) values (\"".$userid."\",\"".$newUname."\");";
		if ($conn->query($sql)){
			//echo "inserted hash!<br>";
		}
		else{
			/** If this gets hit, then you either need to change the userid (fake google id), or somehow
			google assigned two people the same ID
			 */
			$errMsg = "Somehow, that google ID is already in our system. That's not right...";
			$err = true;
		}
		$conn->close();
	}
?>
