<?php
	//获得客户端传过来的参数；
	$stuno=$_REQUEST["stuno"];
	$psssword=$_REQUEST["password"];
	include '../util/connection.php';
	//连接数据库
	$mysql=new mysqlUtil();
	$con=$mysql->getConnection();
	$mysql->getDB();
     
    //执行查询；
    $sql = "select * from student where stuno='".$stuno."' and password='".$psssword."'";
    //echo $sql;
    $rs = $mysql->find($sql);
    if (mysql_num_rows($rs)>0){
    	session_start();
    	$_SESSION["student"]=$stuno;
    	//echo $_SESSION["student"];
    	 /* 释放资源 */
    	$mysql->setResultFree($rs);

   		 /* 断开连接 */
  	  	$mysql->close($con);
    	header("location:../stu/stuinfo.php");
    }
    else{
    	/* 释放资源 */
    	$mysql->setResultFree($rs);

    	/* 断开连接 */
    	$mysql->close($con);
    	header("location:../fail.php");
    }
     /* 释放资源 */
    $mysql->setResultFree($rs);

    /* 断开连接 */
    $mysql->close($con);
?>