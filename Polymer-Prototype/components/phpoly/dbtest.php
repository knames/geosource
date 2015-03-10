<?php

    require_once 'DB_Connect.php';

    $db = new DB_Connect();
    $db->connect();

    $result = mysql_query("SELECT * FROM ad_category") or die(mysql_error());

    while($row=mysql_fetch_assoc($result))
    $output[]=$row;
    print(json_encode($output));
    mysql_close();

?>