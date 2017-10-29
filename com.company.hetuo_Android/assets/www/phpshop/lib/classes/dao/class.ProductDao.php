<?php 
/*
	class.ProductDao.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class ProductDao {
	public static function updateProductHit($pid) {
		$sql = "UPDATE ".DB_TABLE."product SET hit_times = hit_times +1 WHERE id = '{$pid}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getProductAttribute($pid) {
		$sql = "SELECT * FROM ".DB_TABLE."product_attribute WHERE pid = '{$pid}';";
		return DBQuery::DB()->getList($sql); 
	}

	public static function getProduct($cid = NULL, $id = NULL, $keyword = NULL, $b = 1, $c = 10, $extsql = NULL) {
		$b = ($b-1)*$c;
		$sql = "SELECT * FROM ".DB_TABLE."product WHERE 1=1";
		if(!empty($cid)) $sql .= " AND cid IN ($cid)";
		if(!empty($id)) {
			if(is_numeric($id)) {
				$sql .= " AND id = '{$id}'";
				return DBQuery::DB()->getRow($sql); 
			} else {
				$sql .= " AND id IN($id);";
				return DBQuery::DB()->getList($sql); 
			}
		}
		if(!empty($keyword)) $sql .= " AND `name` like '%$keyword%'";
		if(!empty($extsql)) {
			$sql .= $extsql;
		} else {
			$sql .= " ORDER BY id DESC, store DESC";
		}
		$sql .= " LIMIT $b, $c;";
		return DBQuery::DB()->getList($sql); 
	}

	public static function getNumProduct($cid = NULL, $keyword = NULL, $extsql = NULL) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."product WHERE 1=1";
		if(!empty($cid)) $sql .= " AND cid IN ($cid)";
		if(!empty($keyword)) $sql .= " AND `name` like '%$keyword%'";
		if(!empty($extsql)) $sql .= $extsql;
		return DBQuery::DB()->getOne($sql); 
	}
	
	public static function getFilterProductValue($cid) {
		$sql = "SELECT DISTINCT PA.value, A.name, A.id FROM ".DB_TABLE
		      ."product_attribute PA INNER JOIN ".DB_TABLE
			  ."attribute A ON A.id=PA.atrid WHERE A.cid='{$cid}' AND "
			  ."A.isfilter = '1' AND PA.value != '' ORDER BY A.orderid;";
		return DBQuery::DB()->getList($sql); 
	}
	
	public static function getProductPriceRange($cid) {
		$sql = "SELECT MAX(price) mp, MIN(price) lp FROM ".DB_TABLE."product WHERE cid = '{$cid}'";
		return DBQuery::DB()->getRow($sql); 
	}
	
	public static function getProductBrand($cid) {
		$sql = "SELECT * FROM "
		      .DB_TABLE."product_brand WHERE id IN(SELECT DISTINCT bid FROM "
			  .DB_TABLE."product WHERE cid IN($cid) AND bid != '');";
		return DBQuery::DB()->getList($sql); 
	}
	//review 
	public static function insertReview($arrValue) {
		$str1 = '';
		$str2 = '';
		foreach($arrValue as $k => $v) {
			$str1 .= '`'.$k . '`,';
			$str2 .= "'$v',";
		}
		$str1 = trim($str1, ',');
		$str2 = trim($str2, ',');
		$sql = "INSERT INTO ".DB_TABLE."product_review ($str1) VALUES($str2);";
		return DBQuery::DB()->execute($sql);
	}
	public static function checkOrderByBillno($billno, $pid) {
		$sql = "SELECT id FROM ".DB_TABLE."product_bill WHERE billno='$billno' AND id = '$pid';";
		return DBQuery::DB()->getOne($sql); 
	}
	
	public static function getProductReview($pid, $b = 1, $c = 10) {
		$b = ($b-1)*$c;
		$sql = "SELECT * FROM ".DB_TABLE."product_review WHERE pid='$pid' AND init = '1' ORDER BY id DESC LIMIT $b, $c;";
		return DBQuery::DB()->getList($sql); 
	}

	public static function getNumProductReview($pid) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."product_review WHERE pid='$pid';";
		return DBQuery::DB()->getOne($sql); 
	}
	//repeat price
	public static function insertLowerPrice($pid, $uid, $url, $price,$ip, $add_date) {
		$sql = "INSERT IGNORE INTO ".DB_TABLE."product_lower_price (pid, uid, url, price, ip, add_date) VALUES('{$pid}', '{$uid}', '{$url}', '{$price}','{$ip}', '{$add_date}');";
		return DBQuery::DB()->execute($sql);
	}
}
?>