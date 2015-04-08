<?php 
	//db credentials
	$servername = "okenso.com";
	$username = "hdev";
	$dbname = "dev";
	$password = "devsnake371";


	// Create connection
	$mysqli = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($mysqli->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	
	$jreader = file_get_contents('../../../media/spec/Josh.2');
 	$specdecode = json_decode($jreader, true);

	echo '<pre>' . print_r($specdecode, true) . '</pre>';
	echo "test";

	$binarydata =$jreader;
	$array = unpack("cchars/nint", $binarydata);
	echo $array;
 	//
 	//$arr = $rows;
	//echo json_encode($arr);


?>