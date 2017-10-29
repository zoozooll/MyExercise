<?php

/**
 * {0}
 *  
 * @author Administrator
 * @version 
 */
require_once '../inc/DB.php';
class ActicleDao
{
	const HOSTNAME='localhost';
	const USERNAME='root';
	const PASSWORD='123456';
	const DATABASE='mybbs';
	const CHARTSET='GB2312';
	//分页查询文章列表;
	function selectArt()
	{
		$sql="select * from bbs_article where art_class=1 ";
		$mysql=new Mysql();
		$mysql->connect(HOSTNAME,USERNAME,PASSWORD,DATABASE,CHARTSET);
		$result=$mysql->query($sql);
	}
}

?>