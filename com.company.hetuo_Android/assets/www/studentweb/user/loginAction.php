<?php
	//��ÿͻ��˴������Ĳ�����
	$stuno=$_REQUEST["stuno"];
	$psssword=$_REQUEST["password"];
	include '../util/connection.php';
	//�������ݿ�
	$mysql=new mysqlUtil();
	$con=$mysql->getConnection();
	$mysql->getDB();
     
    //ִ�в�ѯ��
    $sql = "select * from student where stuno='".$stuno."' and password='".$psssword."'";
    //echo $sql;
    $rs = $mysql->find($sql);
    if (mysql_num_rows($rs)>0){
    	session_start();
    	$_SESSION["student"]=$stuno;
    	//echo $_SESSION["student"];
    	 /* �ͷ���Դ */
    	$mysql->setResultFree($rs);

   		 /* �Ͽ����� */
  	  	$mysql->close($con);
    	header("location:../stu/stuinfo.php");
    }
    else{
    	/* �ͷ���Դ */
    	$mysql->setResultFree($rs);

    	/* �Ͽ����� */
    	$mysql->close($con);
    	header("location:../fail.php");
    }
     /* �ͷ���Դ */
    $mysql->setResultFree($rs);

    /* �Ͽ����� */
    $mysql->close($con);
?>