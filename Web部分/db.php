<?php

// require 'db.php';
// header('Content-Type:text/html;charset=utf-8');

$temperature=$_GET["temp"];
$humidity=$_GET["humi"];

$hostname="localhost";
$username="smarthome";
$password="smarthome";

$conn=mysql_connect($hostname,$username,$password);
if(!$conn){
	die('Could not connect: ' . mysql_error());
}else{
	mysql_select_db("smarthome", $conn);
	
	$sql="INSERT INTO t_temp_humi (t_temp, t_humi)
		VALUES ($temperature,$humidity)";
	
	mysql_query($sql);
	
	mysql_close($conn);
}

?>


