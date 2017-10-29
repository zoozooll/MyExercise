<?php 
/*
	class.NewsDao.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class NewsDao {
	public static function getNews($cid = NULL, $id = NULL, $keyword = NULL, $b = 1, $c = 10) {
		$b = ($b-1)*$c;
		$sql = "SELECT * FROM ".DB_TABLE."news WHERE 1=1";
		if($cid > 0) {
			$sql .= " AND cid IN ($cid)";
		}
		if($id) {
			$sql .= " AND id = '{$id}'";
			return DBQuery::DB()->getRow($sql); 
		}
		if(!empty($keyword)) {
			$sql .= " AND title like '%$keyword%'";
		}
		$sql .= " ORDER BY id DESC LIMIT $b, $c;";
		return DBQuery::DB()->getList($sql); 
	}
	
	public static function getNumNews($cid = NULL) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."news";
		if(!empty($cid)) {
			$sql .= " WHERE cid IN ($cid);";
		}
		return DBQuery::DB()->getOne($sql); 
	}
}
?>