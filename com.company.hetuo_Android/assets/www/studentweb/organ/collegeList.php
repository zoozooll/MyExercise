<?php 
	include '../util/connection.php';
	$mysql=new mysqlUtil();
	$link=$mysql->getConnection();
	$mysql->getDB();
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>XX大学所有学院</title>
</head>

<body>
<div>
<table><tbody><tr>
<td>XX大学>></td><td></td><td/>
</tr>
</tbody></table>
</div>
<div>
<table border="0" align="center">
  <tr>
    <th>学院名</th>
    <th>编号</th>
    <th>&nbsp;</th>
    <th>&nbsp;</th>
  </tr>
  <?php 
  $query="select * from college";
  $result=$mysql->find($query);
  while($row=mysql_fetch_array($result)){
	?>
  <tr>
    <td><a href=""><?php echo $row["col_name"]?></a></td>
    <td><a href=""><?php echo $row["col_no"]?></a></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <?php 
    }
  ?>
  
</table>

</body>
</div>
</html>
