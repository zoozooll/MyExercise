<?php 
/*
	class.NewsDao.php 
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
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