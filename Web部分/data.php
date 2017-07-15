<?php
header('Content-Type: text/xml');
header("Cache-Control: no-cache, must-revalidate");

$con = mysql_connect('localhost', 'smarthome', 'smarthome');
if (!$con)
 {
 die('Could not connect: ' . mysql_error());
 }

mysql_select_db("smarthome", $con);

$sql="SELECT t_temp,t_humi FROM t_temp_humi WHERE t_id = (SELECT MAX(t_id) from t_temp_humi)";

$result = mysql_query($sql);

echo '<?xml version="1.0" encoding="ISO-8859-1"?>
<temp_humi>';
while($row = mysql_fetch_array($result))
 {
 echo "<temp>" . $row['t_temp'] . "</temp>";
 echo "<humi>" . $row['t_humi'] . "</humi>";
 }
echo "</temp_humi>";

mysql_close($con);
?>