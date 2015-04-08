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
	//echo "Connected successfully <br>";
	$result = $mysqli->query("SELECT ch_owner, ch_name, ch_spec FROM channels;");
 		$rows = array();
 		while ($r = mysqli_fetch_assoc($result)){
 			//print $table;
 			/** The monstrosity below strips posts_ and _username from the table names.*/
 			$rows[] = array('name'=>$r['ch_name'], 'owner'=>$r['ch_owner'],'pid'=>$r['ch_spec']);
 		}
 	$channels = $rows;
 	
 	$count = 0;
 	$rows = array();
 	foreach ($channels as $z){
		$owner = $z['owner'];
		$name = $z['name'];
		$pid = $z['pid'];
		//echo $owner;
		$result = $mysqli->query("SELECT * FROM posts_$owner._$name WHERE items.xml LIKE '%.jpg';");
 		$rows = array();
 		if ($result == true)
 			$numresults = mysql_num_rows($result);
 		else 
 			$numresults = 0;

 		if ($numresults >=1)
 			$thumb = mysqli_fetch_assoc($result);
 		 else 
 			$thumb = null;
 		
 		$result = $mysqli->query("SELECT p_geotag FROM posts_$owner._$name;");
 		if ($result == true)
 			$geo = mysqli_fetch_assoc($result);
 		else
 			$geo = null;

 		if ($geo == null){
 			$time = null;
 			$location = null;
 		} else {
 			$jreader = file_get_contents($geo);
 			$geodecode = json_decode($jreader, true);
 			echo $geodecode;
 			$time = $geodecode['time'];
 			$location = array($geodecode['lat'],$geodecode['lng']);
 		}
 		// NOTE QUESTION IS FALSE HERE TODO IMPLEMENT IT AND IT DOESNT HAVE TEO BE
		$rows[] = array('pid'=>$pid, 'title'=>$name, 'username'=>$owner, 'thumbnail'=>$thumb, 
			'time'=>$time, 'location'=>$geo, 'channel'=>array('name'=>$name, 'owner'=>$owner),
			'question'=>false);
 	}

 	$arr = $rows;
	echo json_encode($arr);


?>