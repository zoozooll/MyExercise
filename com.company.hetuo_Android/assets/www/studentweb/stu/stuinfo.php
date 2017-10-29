<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>学生信息</title>
</head>
<body>
<?php

	include '../util/connection.php';
	session_start();
	$stuno=$_SESSION["student"];
	//echo "session".$stuno;
	if ($stuno!=null & stuno!=""){
		$mysql=new mysqlUtil();
		$link=$mysql->getConnection();
		$mysql->getDB();
		$query="SELECT student.name,sex,birthday,interest,stuno,
			email,
			student.id,
			student.name as stuname,
			student.sex,
			student.birthday,
			student.interest,
			student.identity,
			student.stuno,
			student.`password`,
			student.email,
			student.remark,
			class.id,
			class.class_name,
			special.id,
			special.name as special_name,
			special.series,
			college.id,
			college.col_name,
			college.col_no
			FROM
			student
			Inner Join class ON student.classid = class.id
			Inner Join special ON class.special = special.id
			Inner Join college ON special.college_id = college.id
		 where stuno='".$stuno."'";
		//echo $query;
		$result=$mysql->find($query);
		if(mysql_num_rows($result)>0)
		$row=mysql_fetch_assoc($result);
		$mysql->setResultFree($result);
	}
	
	include("stutop.inc");
	include("stuleft.inc");
?>
<div align="center">
<table border="0">
  <tr>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>姓名</td>
    <td><strong><?php echo $row["stuname"]?></strong></td>
  </tr>
  <tr>
    <td>学号</td>
    <td><?php echo $row["stuno"]?></td>
  </tr>
  <tr>
    <td>性别</td>
    <td><?php echo $row["sex"]?></td>
  </tr>
  <tr>
    <td>身份证号码</td>
    <td><?php echo $row["identity"]?></td>
  </tr>

  <tr>
    <td>院系&nbsp;班级</td>
    <td><a href="#"><?php echo $row["col_name"];?></a>
   <a href="#"><?php echo $row["special_name"];?></a>
   <a href="#"><?php echo $row["class_name"];?></a></td>
  </tr>
  <tr>
    <td>EMAIL</td>
    <td><?php echo $row["email"]?></td>
  </tr>
  <tr>
    <td>备注信息</td>
    <td><?php echo $row["remark"]?></td>
  </tr>
  <tr>
    <td>入学年份</td>
    <td><?php ?></td>
  </tr>

</table>

</div>
</body>
</html>
<?php 
	$mysql->close($link);
?>
