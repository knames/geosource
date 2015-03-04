<?php 


	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";



	$preid = 111908491436059750060;


	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	echo "Connected successfully <br>";

    //$sql = "SELECT * FROM users WHERE u_identity = 12345";
    $sql = "SELECT * FROM users";
	$result = $conn->query($sql);

	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
	        echo "id: " . $row["u_identity"]. " - username: " . $row["u_username"]. "<br>";
	    }
	} else {
	    echo "0 results";
	}
	$conn->close();
?>

<html>
<head>
	
</head>
<body>yolo<br><br>




<?php 



	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	echo "Connected successfully <br>";


	/** THIS INSERTS A HASH
	IMPORTANT:  Using a static salt because id's are unique, and i'm unsure of how to remove the salt
	*/
	$options = ['cost' => 11, 'salt' => '$VuOmsSDxuiwJiquy3PgaG',]; 
	$hash = password_hash($preid, PASSWORD_DEFAULT, $options);
	$sql = "insert into users (u_identity, u_username) values (\"".$hash."\", \"hashman\")";
	if ($conn->query($sql)){
		echo "inserted hash!<br>";
	}
	else{
		echo "failed to insert hash: " . mysql_error($conn);
	}


    //$sql = "SELECT * FROM users WHERE u_identity = 12345";
    $sql = "insert into users (u_identity, u_username) values (5,\"rick\")";
	if ($conn->query($sql)){
		echo "inserted!<br>";
	}
	else{
		echo "failed to insert: " . mysql_error($conn);
	}




	$sql = "SELECT * FROM users";
	$result = $conn->query($sql);
	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
	        echo "id: " . $row["u_identity"]. " - username: " . $row["u_username"]. "<br>";
	    }
	} else {
	    echo "0 results";
	}

	// Great sample code for insert/delete queries
	$sql = "DELETE FROM users WHERE u_identity = 5;";
	if ($conn->query($sql)){
		echo "deleted!";
	}
	else {
		echo "error deleting: " . mysqli_error($conn);
	}

	echo "<br><br>";
	$sql = "SELECT * FROM users";
	$result = $conn->query($sql);
	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
	        echo "id: " . $row["u_identity"]. " - username: " . $row["u_username"]. "<br>";
	    }
	} else {
	    echo "0 results";
	}
	$conn->close();
?>













</body>
</html>