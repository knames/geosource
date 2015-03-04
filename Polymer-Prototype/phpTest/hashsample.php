<?php 
	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";

	$preid = 5; // sample google id
	$newUname = "";
?>

<html>
<head>
	<style>
		.error {color: #FF0000;}
	</style>
</head>
<body>you only yolo once<br><br>




	<?php 
	$uNameErr="";
	if ($_SERVER["REQUEST_METHOD"] == "POST") {
		$newUname = $_POST["new_u_name"];

		if (empty($_POST["new_u_name"])){
			$uNameErr="Please enter a user name";
		} else if (!preg_match("/^[a-zA-Z0-9]*$/",$newUname)){
			$uNameErr = "Only letters and numbers allowed.";
		} else if (nameExists($newUname)){
			$uNameErr = "That username is taken, please choose another.";
		} else {
			$newUname = cleanName($_POST["new_u_name"]);
			insertHash($newUname);
		}
	}

	/** checks to see if this new username is already in the database */
	function nameExists($newUname){
		global $servername, $username, $dbname, $password, $preid;
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
		global $servername, $username, $dbname, $password, $preid, $uNameErr;
		// Create connection
		$conn = new mysqli($servername, $username, $password, $dbname);

		// Check connection
		if ($conn->connect_error) {
		    die("Connection failed: " . $conn->connect_error);
		} 

		// Hash insert
		$options = ['cost' => 11, 'salt' => '$VuOmsSDxuiwJiquy3PgaG',]; 
		$hash = password_hash($preid, PASSWORD_DEFAULT, $options);
		//$preid = mysql_real_escape_string($preid);
		//$newUname = mysql_real_escape_string($newUname)''
		$sql = "insert into users (u_identity, u_username) values (\"".$preid."\",\"".$newUname."\");";
		if ($conn->query($sql)){
			echo "inserted hash!<br>";
		}
		else{
			/** If this gets hit, then you either need to change the preid (fake google id), or somehow
			google assigned two people the same ID
			 */
			$uNameErr = "Somehow, that google ID is already in our system. That's not right...";
		}
		$conn->close();
	}
	?>

	<div class="name-creator">
	<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
	   Enter a Username<br> <input type="text" name="new_u_name" placeholder="User Name" value="<?php echo $newUname?>">  
	   <span class="error"><br> <?php echo $uNameErr;?></span>
	   <br>
	   
	   <input type="submit" name="submit" value="Submit"> 
	</form>
	</div>

</body>
</html>