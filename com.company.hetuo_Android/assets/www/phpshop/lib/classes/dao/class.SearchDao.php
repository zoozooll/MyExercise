<?php 
/*
	class.SearchDao.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class SearchDao {
	public static function getSearchProduct($keyword) {
		$sql = "SELECT distinct p.cid, c.name, c.enname, count(p.id) prodnum  FROM ".DB_TABLE."product p INNER JOIN ".DB_TABLE
			  ."classes c ON c.id = p.cid WHERE c.name LIKE '%$keyword%' OR p.name LIKE '%$keyword%' GROUP BY p.cid;";
		return DBQuery::DB()->getList($sql);
	}

}
?>