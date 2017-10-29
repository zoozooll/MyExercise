<?php 
/*
	class.UserDao.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class UserDao {
	public static function updateUser($arrValue, $email, $password) {
			$sql = "UPDATE ".DB_TABLE."user SET name = '".$arrValue["name"]."', email = '".$arrValue["email"]
				  ."', phone = '".$arrValue["phone"]
				  ."', mobile = '".$arrValue["mobile"]
				  ."', address = '".$arrValue["address"] ."', postcode = '".$arrValue["postcode"] 
				  ."', sex = '".$arrValue["sex"]."', birthday = '".$arrValue["birthday"]
				  ."', qq = '".$arrValue["qq"]
				  ."', msn = '".$arrValue["msn"]."', taobao = '".$arrValue["taobao"]."', alibaba = '".$arrValue["alibaba"]
				  ."', skype = '".$arrValue["skype"]."'";
			if($arrValue["password"] != '') $sql .= ", password = '".$arrValue["password"]."'";
			$sql .= " WHERE email = '$email' AND password = '{$password}';";
			return DBQuery::DB()->execute($sql);
	}

	public static function insertUser($arrValue) {
		$sql = "INSERT INTO ".DB_TABLE."user(`name`, `email`, `password`,"
			  ." `phone`, `mobile`, `address`, `postcode`, `sex`, `birthday`, `qq`, `msn`,"
			  ." `taobao`, `alibaba`, `skype`, `loginid`, `regip`, `rember_times`, `isreg`)"
			  ." VALUES('".$arrValue["name"]."','".$arrValue["email"]."','".$arrValue["password"]."','"
			  .$arrValue["phone"]."','".$arrValue["mobile"]."','"
			  .$arrValue["address"]."','".$arrValue["postcode"]."','".$arrValue["sex"]."','"
			  .$arrValue["birthday"]."','".$arrValue["qq"]."','".$arrValue["msn"]."','"
			  .$arrValue["taobao"]."','".$arrValue["alibaba"]."','"
			  .$arrValue["skype"]."','".$arrValue["loginid"]."','".$arrValue["regip"]."','"
			  .$arrValue["rember_times"]."','".$arrValue["isreg"]."');";
		return DBQuery::DB()->execute($sql);
	}

	public static function updateUserLoginId($loginid, $time, $email, $password) {
		$sql = "UPDATE ".DB_TABLE."user SET loginid = '".$loginid."', rember_times = '".$time
		      ."' WHERE email = '$email' AND password = '{$password}';";
	    return DBQuery::DB()->execute($sql);
	}
	
	public static function getInsertUserId() {
		return DBQuery::DB()->getInsertID();
	}
	
	public static function getUser($mtid = NULL, $id = NULL, $keyword = NULL, $b = 1, $c = 10, $isreg = NULL) {
		$b = ($b-1)*$c;
		$sql = "SELECT * FROM ".DB_TABLE."user WHERE 1=1";
		if(!empty($mtid)) $sql .= " AND mtid IN ($mtid)";
		if($isreg != NULL) $sql .= " AND isreg = '$isreg'";
		if($id) {
			$sql .= " AND id = '{$id}'";
			return DBQuery::DB()->getRow($sql); 
		}
		if(!empty($name)) {
			$sql .= " AND name like '%$keyword%'";
		}
		$sql .= " LIMIT $b, $c;";
		return DBQuery::DB()->getList($sql); 
	}
	
	public static function getUserByLoginid($email, $loginid) {
		$sql = "SELECT * FROM ".DB_TABLE."user WHERE email='$email' AND loginid = '$loginid';";
		return DBQuery::DB()->getRow($sql); 
	}

	public static function getNumUser($mtid = NULL, $isreg = NULL) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."user WHERE 1=1;";
		if(!empty($cid)) {
			$sql .= " mtid IN ($mtid);";
		}
		if($isreg != NULL) $sql .= " AND isreg = '$isreg'";
		return DBQuery::DB()->getOne($sql); 
	}
	
	public static function deleteUser($id) {
		$sql = "DELETE FROM ".DB_TABLE."user WHERE id = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function checkUser($email, $password = NULL) {
		$sql = "SELECT id, name, email, loginid, rember_times FROM ".DB_TABLE
		      ."user WHERE isreg = '1' AND email='{$email}'";
		if(!empty($password)) {
			$sql .= " AND password = '{$password}';";
		}
		return DBQuery::DB()->getRow($sql);
	}
	
	public static function getUserBillInfo($uid = NULL, $billno = NULL, $str_sql = NULL) {
		$sql = "SELECT ob.*,pb.* FROM ".DB_TABLE ."order_bill ob INNER JOIN "
		      .DB_TABLE."product_bill pb ON ob.billno = pb.billno WHERE 1=1";
		if(!empty($uid)) $sql .= " AND ob.uid = '{$uid}'";
		if(!empty($billno)) $sql .= " AND ob.billno = '{$billno}'";
		if(!empty($str_sql)) $sql .= " AND $str_sql;";
		return DBQuery::DB()->getList($sql);
	}
}
?>