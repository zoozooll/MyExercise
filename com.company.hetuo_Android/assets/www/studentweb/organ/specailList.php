<?php 
	$colid=$_REQUEST["colid"];
	include '../util/connection.php';
	$mysql=new mysqlUtil();
	$link=$mysql->getConnection();
	$mysql->getDB();
	$query="select sp.id,sp.name,col.col_name from special sp,college col where sp.college_id=col.id and col.id=".$colid;
  	$result=$mysql->find($query);
  	$row=mysql_fetch_array($result);
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>XX��ѧ--<?php echo $row["col_name"]?>ѧԺ--רҵ�б�</title>
</head>

<body>
<div>
<table><tbody><tr>
<td>XX��ѧ----</td><td><?php echo $row["col_name"];?></td><td></td>
</tr>
</tbody></table>
</div>
<div>
<table border="0" align="center">
  <tr>
    <th>ϵ��רҵ����</th>
    <th>ѧԺ</th>
    <th>&nbsp;</th>
    <th>&nbsp;</th>
  </tr>
  <?php 
  foreach ($line as $row){
	?>
  <tr>
    <td><a href=""><?php echo $line["name"];?></a></td>
    <td><a href=""><?php echo $line["col_name"]?></a></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <?php 
    }
  ?>
  
</table>
</div>
</body>
</html>
