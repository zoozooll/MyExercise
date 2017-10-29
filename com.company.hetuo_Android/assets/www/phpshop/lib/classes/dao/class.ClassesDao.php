<?php 
/*
	class.ClassesDao.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class ClassesDao {
	//getClasssSetValue
	public static function getClasssSetValue($id = NULL) {
		$sql = "SELECT id, name, enname, cid, cpid, ccidnum, orderid, "
			  ."depth, des, hidden, espeuser, url, picurl, spliter, "
			  ."affiche, advalue, espegroup, sortid"
			  ." FROM ".DB_TABLE."classes";
		if($id > 0 and is_numeric($id)) {
			$sql .= " WHERE id ='$id'";
			return DBQuery::DB()->getRow($sql);
		}
		$sql .= " ORDER BY sortid, cid ASC, orderid ASC, cpid ASC, depth ASC;";
		return DBQuery::DB()->getList($sql);
	}
	
	//getAttribute
	public static function getAttribute($cid = NULL, $isfilter = false) {
		$sql = "SELECT id,cid,name,isfilter,orderid, mainatr FROM ".DB_TABLE."attribute WHERE 1=1";
		if($cid > 0) {
			$sql .= " AND cid = '{$cid}'";
		}	
		if($isfilter) {
			$sql .= " AND isfilter = '1'";
		}		
		$sql .= " ORDER BY orderid;";
		return DBQuery::DB()->getList($sql);
	}
	
	//getBrand
	public static function getBrand($id = NULL) {
		$sql = "SELECT * FROM ".DB_TABLE."product_brand";
		if(!empty($id)) $sql .= " WHERE id = '$id';";
		return DBQuery::DB()->getList($sql); 
	}
	
	//getSeries
	public static function getSeries($bid = NULL) {
		$sql = "SELECT * FROM ".DB_TABLE."product_series";
		if(!empty($bid)) {
			$sql .= " WHERE bid = '{$bid}';";
		}
		return DBQuery::DB()->getList($sql); 
	}
	
	//getBank
	public static function getBank($type = NULL, $id = NULL) {
		$sql = "SELECT * FROM ".DB_TABLE."shop_bank WHERE 1=1";
		if(!empty($type)) {
			$sql .= " AND type='$type';";
		}
		if(!empty($id)) $sql .= " AND id = '$id';";
		return DBQuery::DB()->getList($sql);
	}
	
	//getFreight
	public static function getFreight($id = NULL) {
		$sql = "SELECT * FROM ".DB_TABLE."shop_freight";
		if(!empty($id)) {
			$sql .= " WHERE id = '$id';";
			return DBQuery::DB()->getRow($sql);
		}
		return DBQuery::DB()->getList($sql);
	}
	//getSupply
	public static function getSupply($id = NULL) {
		$sql = "SELECT * FROM ".DB_TABLE."shop_supply";
		if(!empty($id)) {
			$sql .= " WHERE id = '$id';";
			return DBQuery::DB()->getRow($sql);
		}
		return DBQuery::DB()->getList($sql);
	}

	
}
?>