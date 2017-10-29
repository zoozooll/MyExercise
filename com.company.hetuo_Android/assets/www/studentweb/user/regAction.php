<?php
	$stuId=$_REQUEST["stuId"];
	$stuname=$_REQUEST["stuname"];
	$sex=$_REQUEST["sex"];
	$year=$_REQUEST["year"];
	$month=$_REQUEST["month"];
	$dateOfMonth=$_REQUEST["dateOfMonth"];
	$identity=$_REQUEST["identity"];
	$stuno=$_REQUEST["stuno"];
	$email=$_REQUEST["email"];
	$remark=$_REQUEST["remark"];
	$con = new mysqli("localhost", "root", "123456","student")
        or die("Could not connect : " . mysql_error());
	$sql="";
	if($stuId==NULL){
		$sql="insert into student(name,sex,birthday,interest,identity,stuno,email,remark
		) values('".$stuname."','".$sex."','".$birthday."','','".$identity."','".$stuno."'.'".$email."','".$remark."')";	
	}else{
		$sql="update student
		 set name='".$stuname."',sex='".$sex."',identity='".$identity."',stuno='".$stuno."',email='".$email."',remark='".$remark."'
		  where id=".$stuId;
	}
	$con->prepare($sql);
    /* ¶Ï¿ªÁ¬½Ó */
	$con->close();	
?>