<?php 
/*
	class.SearchDao.php 
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/
class SearchDao {
	public static function getSearchProduct($keyword) {
		$sql = "SELECT distinct p.cid, c.name, c.enname, count(p.id) prodnum  FROM ".DB_TABLE."product p INNER JOIN ".DB_TABLE
			  ."classes c ON c.id = p.cid WHERE c.name LIKE '%$keyword%' OR p.name LIKE '%$keyword%' GROUP BY p.cid;";
		return DBQuery::DB()->getList($sql);
	}

}
?>