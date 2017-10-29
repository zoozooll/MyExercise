<?php 
/*
	class.ShopDao.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class ShopDao {
	public static function updateTmpShop($pid, $md5id, $uid, $num) {
			$sql = "UPDATE ".DB_TABLE."tem_shop SET num = '".$num."' WHERE pid = '$pid'";
			if(!empty($uid)) {
				$sql .= " AND uid = '{$uid}';";
				return DBQuery::DB()->execute($sql);
			}		
			$sql .= " AND md5id = '{$md5id}';";	
			return DBQuery::DB()->execute($sql);
	}

	public static function updateTmpShopUid($md5id, $uid) {
			$sql = "UPDATE ".DB_TABLE."tem_shop SET uid = '".$uid."' WHERE md5id = '$md5id';";
			return DBQuery::DB()->execute($sql);
	}
	
	public static function insertTmpShop($pid, $uid, $md5id, $ip) {
		$sql = "INSERT IGNORE INTO ".DB_TABLE."tem_shop(pid, uid, order_ip,"
			  ." md5id, add_date)"
			  ." VALUES('{$pid}','{$uid}','{$ip}','{$md5id}','".Common::getCommonTime()."');";
		return DBQuery::DB()->execute($sql);
	}

	public static function deleteTmpShop($pid = NULL, $md5id, $uid = NULL) {
		$sql = "DELETE FROM ".DB_TABLE."tem_shop WHERE 1=1";
		if(!empty($pid)) {
			$sql .= " AND pid = '{$pid}'";
		}
		if(!empty($uid)) {
			$sql .= " AND uid = '{$uid}';";
			return DBQuery::DB()->execute($sql);
		}
		$sql .= " AND md5id = '{$md5id}';";	
		return DBQuery::DB()->execute($sql);
	}

	public static function getTmpShop($md5id, $uid = NULL) {
		$sql = "SELECT ts.pid,ts.num,ts.add_date,p.* FROM "
			  .DB_TABLE
			  ."tem_shop ts INNER JOIN ".DB_TABLE
			  ."product p ON ts.pid=p.id WHERE 1=1";
		if(!empty($uid)) {
			$sql .= " AND ts.uid = '{$uid}';";
			return DBQuery::DB()->getList($sql); 
		}	
		if(!empty($md5id)) {
			$sql .= " AND ts.md5id = '{$md5id}';";
			return DBQuery::DB()->getList($sql); 
		}
		return NULL;
	}
	
	public static function insertOrderBill($arrValues) {
		$sql = "INSERT INTO ".DB_TABLE ."order_bill(billno, uid,"
		      ." sellprice, pay_price, state, md5id, freight, paymethod,"
			  ." freight_price, web_pay_code, `name`, email, phone,"
			  ." mobile, address, postcode, userip, ss, cps, add_date,host"
			  .") VALUES"
			  ."('".$arrValues['billno']."','".$arrValues['uid']
			  ."','".$arrValues['sellprice']."','".$arrValues['pay_price']."','".$arrValues['state']
			  ."','".$arrValues['md5id']."','".$arrValues['freight']
			  ."','".$arrValues['paymethod']."','".$arrValues['freight_price']
			  ."','".$arrValues['web_pay_code']."','".$arrValues['name']
			  ."','".$arrValues['email']."','".$arrValues['phone']
			  ."','".$arrValues['mobile']."','".$arrValues['address']
			  ."','".$arrValues['postcode']."','".$arrValues['userip']."','".$arrValues['ss']."','".$arrValues['cps']
			  ."','".Common::getCommonTime()."','".$arrValues['host']."')";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getInsertId() {
		return DBQuery::DB()->getInsertID();
	}
	
	public static function updateOrderBillId($billno, $id) {
		$sql = "UPDATE ".DB_TABLE ."order_bill SET billno = '$billno' WHERE id = '$id';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getOrderBillMaxId() {
		$sql = "SELECT MAX(id) FROM ".DB_TABLE ."order_bill;";
		return DBQuery::DB()->getOne($sql); 
	}
	
	public static function insertProductBill($billno, $md5id) {
		$sql = "INSERT INTO ".DB_TABLE ."product_bill "
		      ."SELECT p.*, ts.num, '$billno', '$md5id', NULL FROM ".DB_TABLE."tem_shop ts INNER JOIN "
			  .DB_TABLE."product p ON ts.pid=p.id AND ts.md5id = '{$md5id}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getBillInfo($billno, $md5id = NULL) {
		$sql = "SELECT * FROM ".DB_TABLE ."order_bill WHERE billno = '{$billno}'";
		$sql .= !empty($md5id) ? " AND md5id = '{$md5id}'" : '';
		return DBQuery::DB()->getRow($sql);
	}
	
	public static function getProductBill($billno, $md5id = NULL) {
		$sql = "SELECT * FROM ".DB_TABLE ."product_bill WHERE billno = '{$billno}'";
		$sql .= !empty($md5id) ? " AND md5id = '{$md5id}'" : '';
		return DBQuery::DB()->getList($sql); 
	}
	
	public static function updateBillInfo($billno, $state = NULL, $pay_success = NULL) {
		$sql = "UPDATE ".DB_TABLE ."order_bill SET";
		if($state != NULL) $sql .= " state = '$state'";
		if($pay_success != NULL) $sql .= " pay_success = '$pay_success'";
		if($state == NULL && $pay_success == NULL) return false;
		$sql .= " WHERE billno = '$billno';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updatePrdecuStore($pid, $num) {
		$sql = "UPDATE ".DB_TABLE ."product SET store = store - $num WHERE id = '{$pid}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getBillnoPaySuccess($billno) {
		$sql = "SELECT pay_success FROM ".DB_TABLE ."order_bill WHERE billno = '{$billno}';";
		return DBQuery::DB()->getOne($sql); 
	}
		
}
?>