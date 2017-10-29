<?php 
/*
	class.AdminDao.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class AdminDao {
	//base set
	public static function getAdminInfo($id) {
		$sql = "SELECT f.id, f.fsid, f.orderid, f.name, f.enname, f.fun FROM ".DB_TABLE
		      ."function f, ".DB_TABLE."power"
		  	  ." p WHERE p.fid = f.id AND p.aid = '$id' AND f.display = '1' ORDER BY f.fsid ASC, f.orderid ASC";
		//$sql = "SELECT fsid, orderid, name, url FROM function WHERE id IN($id)";
		return DBQuery::DB()->getList($sql);
	}
	
	public static function getAdminSetInfo($aid = NULL) {
		if(empty($aid)) {
			$sql = "SELECT * FROM ".DB_TABLE
				  ."function WHERE display = '1' ORDER BY fsid ASC, orderid ASC";
		} else {
			$sql = "SELECT f.id, f.fsid, f.orderid, f.name, f.enname, f.fun,p.fid FROM ".DB_TABLE
		      ."function f LEFT JOIN ".DB_TABLE."power p"
		  	  ." ON p.fid = f.id AND p.aid = '{$aid}' WHERE f.display = '1' ORDER BY f.fsid ASC, f.orderid ASC";
		}
		return DBQuery::DB()->getList($sql);
	}
	
	public static function getAdmin($name, $pass) {
		$sql = "SELECT * FROM ".DB_TABLE."admin WHERE name = '$name' AND pass = '"
		      .md5($pass)."';";
		return DBQuery::DB()->getRow($sql);
	}
	
	public static function getAdminAll($id = NULL, $keyword = NULL, $b = 1, $c = 10) {
		$b = ($b-1)*$c;
		$sql = "SELECT * FROM ".DB_TABLE."admin WHERE 1=1";
		if(!empty($id)) {
			if(is_numeric($id)) {
				$sql .= " AND id = '{$id}'";
				return DBQuery::DB()->getRow($sql); 
			} else {
				$sql .= " AND id IN($id);";
				return DBQuery::DB()->getList($sql); 
			}
		}
		if(!empty($keyword)) $sql .= " AND name like '%$keyword%'";
		$sql .= " ORDER BY id DESC LIMIT $b, $c;";
		return DBQuery::DB()->getList($sql);
	}
	
	public static function getNumAdminAll($id = NULL, $keyword = NULL) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."admin WHERE 1=1";
		if(!empty($id)) $sql .= " AND id IN ($id)";
		if(!empty($keyword)) $sql .= " AND name like '%$keyword%'";
		return DBQuery::DB()->getOne($sql); 
	}

	public static function updateAdminlogin($id, $rValue) {
		$sql = "UPDATE ".DB_TABLE."admin SET loginip = lastloginip, logintime = lastlogintime WHERE id = '$id';";
		DBQuery::DB()->execute($sql);
		$sql = "UPDATE ".DB_TABLE."admin SET lastloginip = '".$rValue["ip"]."', lastlogintime = '".$rValue["time"]
			  ."', logintimes = logintimes + 1"
			  ." WHERE id = '$id';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateAdmin($id, $name, $password = NULL, $email = NULL) {
		$sql = "UPDATE ".DB_TABLE."admin SET";
		if(!empty($password)) $sql .= " pass = '".md5($password)."'";
		if(!empty($email)) $sql .= " email = '{$email}'";
		$sql .=  " WHERE id = '$id' AND name = '{$name}'";
		return DBQuery::DB()->execute($sql);
	
	}
	public static function addAdmin($name, $email, $password) {
		$sql = "INSERT INTO ".DB_TABLE."admin(name, email, pass) VALUES ('".$name."','".$email."','"
			  .md5($password)."');";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function addAdminPower($strsql) {
		$sql = "INSERT INTO ".DB_TABLE."power(aid, fid) VALUES ".$strsql;
		return DBQuery::DB()->execute($sql);
	}
	
	public static function deleteAdminPower($aid) {
		$sql = "DELETE FROM ".DB_TABLE."power WHERE aid = '{$aid}'";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function deleteAdmin($aid) {
		$sql = "DELETE FROM ".DB_TABLE."admin WHERE id = '{$aid}'";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getFunc($id, $aid = NULL) {
		$sql = "SELECT f.id, f.fun, f.fsid, f.name FROM ".DB_TABLE."function f, "
		      .DB_TABLE."power"
		  	  ." p WHERE p.fid = f.id AND f.id = '$id' AND p.aid = '$aid';";
		return DBQuery::DB()->getRow($sql);
	}
	
	public static function getBasesetValue($fun, $n = '*') {
		$sql = "SELECT $n FROM ".DB_TABLE."baseset WHERE fun ='$fun'";
		return DBQuery::DB()->getRow($sql);
	}
	
	public static function updateBasesetValue($fun, $rValue) {
		$sql = "UPDATE ".DB_TABLE."baseset SET value1 = '".$rValue['value1']
			."', value2 = '".$rValue['value2']."', value3 = '".$rValue['value3']
			."', value4 = '".$rValue['value4']."', value5 = '".$rValue['value5']
			."', value6 = '".$rValue['value6']."', value7 = '".$rValue['value7']
			."', value8 = '".$rValue['value8']."', value9 = '".$rValue['value9']
			."', value10 = '".$rValue['value10']."', value11 = '".$rValue['value11']
			."', value12 = '".$rValue['value12']."', value13 = '".$rValue['value13']
			."', value14 = '".$rValue['value14']."', value15 = '".$rValue['value15']
			."', value16 = '".$rValue['value16']."', value17 = '".$rValue['value17']
			."', value18 = '".$rValue['value18']."', value19 = '".$rValue['value19']
			."', value20 = '".$rValue['value20']."', value21 = '".$rValue['value21']
			."', value22 = '".$rValue['value22']."', value23 = '".$rValue['value23']
			."', value24 = '".$rValue['value24']."', value25 = '".$rValue['value25']
			."', value26 = '".$rValue['value26']."', value27 = '".$rValue['value27']
			."', value28 = '".$rValue['value28']."', value29 = '".$rValue['value29']
			."', value30 = '".$rValue['value30']."' WHERE fun ='$fun';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getEnName($id) {
		$sql = "SELECT enname FROM ".DB_TABLE."function WHERE id = '$id';";
		return DBQuery::DB()->getOne($sql);
	}
	//end base set
	//begin Classes
	public static function updateClassesValue($rValue, $id = NULL) {
		if($id > 0 and is_numeric($id)) {
			$sql = "UPDATE ".DB_TABLE."classes SET name = '".$rValue["name"]."', enname = '".$rValue["enname"]
			      ."', des = '".$rValue["des"]
				  ."', url = '".$rValue["url"]."', picurl = '".$rValue["picurl"]."', spliter = '".$rValue["spliter"]
				  ."', affiche = '".$rValue["affiche"]
				  ."', espegroup = '".$rValue["espegroup"]
				  ."' WHERE id = '$id';";
			return DBQuery::DB()->execute($sql);
		} 
		$sql = "INSERT INTO ".DB_TABLE."classes(name, enname, cid,"
		      ." cpid, orderid, depth, des, url, picurl, spliter, affiche, espegroup, sortid)"
			  ." VALUES('".$rValue["name"]."','".$rValue["enname"]."','"
			  .$rValue["cid"]."','".$rValue["cpid"]."','".$rValue["orderid"]."','"
			  .$rValue["depth"]."','".$rValue["des"]."','"
			  .$rValue["url"]."','".$rValue["picurl"]."','".$rValue["spliter"]."','"
			  .$rValue["affiche"]."','".$rValue["espegroup"]."', '".$rValue["sortid"]."');";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateClassesCid() {
		$sql = "UPDATE ".DB_TABLE."classes SET cid = ".DBQuery::getInsertID()." WHERE id = ".DBQuery::getInsertID();
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateClassesSort($id, $sortid) {
		$sql = "UPDATE ".DB_TABLE."classes SET sortid = '{$sortid}' WHERE cid = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getMAXOrderId($id) {
		$sql = "SELECT id, MAX(orderid) orderid, cid, ccidnum, sortid FROM ".DB_TABLE
			."classes WHERE cpid = '{$id}' GROUP BY orderid ORDER BY orderid DESC LIMIT 1;";
		$row = DBQuery::DB()->getRow($sql);
		if(empty($row)) {
			$sql = "SELECT id, orderid, cid, ccidnum, sortid FROM ".DB_TABLE."classes WHERE id = '{$id}';";
			$row = DBQuery::DB()->getRow($sql);
		}
		if($row['ccidnum'] > '0') {
			$row = self::getMAXOrderId($row['id']);
		}
		return $row;
	}
	
	public static function getMaxSortId() {
		$sql = "SELECT MAX(sortid) FROM ".DB_TABLE."classes;";
		return DBQuery::DB()->getOne($sql);
	}
	
	public static function getMaxId() {
		$sql = "SELECT MAX(id) FROM ".DB_TABLE."classes;";
		return DBQuery::DB()->getOne($sql);
	}
	
	public static function getDepth($id) {
		$sql = "SELECT depth FROM ".DB_TABLE."classes WHERE id = '{$id}';";
		return DBQuery::DB()->getOne($sql);
	}

	public static function updateClassesOrderid($id, $cid) {
		$sql = "UPDATE ".DB_TABLE."classes SET orderid = orderid + 1 WHERE cid = '$cid' AND orderid > '$id'";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateCcidnum($id) {
		$sql = "UPDATE ".DB_TABLE."classes SET ccidnum = ccidnum + 1 WHERE id = '$id';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function delClasses($id) {
		self::updateDelClasses($id);
		$sql = "DELETE FROM ".DB_TABLE."classes WHERE id = '$id';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateDelClasses($id) {
		$sql = "SELECT cpid,orderid FROM ".DB_TABLE."classes WHERE id = '$id';";
		$arrRs = DBQuery::DB()->getRow($sql);
		if($arrRs['cpid'] > '0') {
			$sql = "UPDATE ".DB_TABLE."classes SET ccidnum = ccidnum - 1 WHERE id = ".$arrRs['cpid'];
			DBQuery::DB()->execute($sql);
			$sql = "UPDATE ".DB_TABLE."classes SET orderid = orderid - 1 WHERE cpid = "
					.$arrRs['cpid']." AND orderid > ".$arrRs['orderid'];
			return DBQuery::DB()->execute($sql);
		}
	}
	
	public static function updateClassesOrderUp($id) {
		$sql = "UPDATE ".DB_TABLE."classes SET orderid = orderid + 1 WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);
	}
	public static function updateClassesOrderDown($id) {
		$sql = "UPDATE ".DB_TABLE."classes SET orderid = orderid - 1 WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);
	}
	public static function updateClassesOrder($id, $orderid) {
		$sql = "UPDATE ".DB_TABLE."classes SET orderid = '{$orderid}' WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getClassesProduceCount($cid) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."product WHERE cid = '{$cid}';";
		return DBQuery::DB()->getOne($sql);
	}
	
	public static function updateMoveClasses($arrValue, $id) {
		$updateSql = Utilities::formArrayTurn2Str($arrValue);
		$sql = "UPDATE ".DB_TABLE."classes SET ".$updateSql." WHERE id = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	public static function updateMoveSortClasses($sort, $cid) {
		$sql = "UPDATE ".DB_TABLE."classes SET sortid = '{$sort}' WHERE cid = '{$cid}';";
		return DBQuery::DB()->execute($sql);
	}
	public static function updateMoveClassesSelfCcid($id) {
		$sql = "UPDATE ".DB_TABLE."classes SET ccidnum = ccidnum - 1 WHERE id = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	
	//end Classes
	
	//begin product 
	public static function updateProduct($arrProduct, $cid, $id) {//$arrValue, 
			$sql = "UPDATE ".DB_TABLE."product SET cid = '".$cid."', psid = ".$arrProduct["psid"]
			      .", name = '".$arrProduct["name"]."', enname = '".$arrProduct["enname"]
				  ."', store = '".$arrProduct["store"]."',describes = '".$arrProduct["describe"]
				  ."', b_pic = '".$arrProduct["b_pic"]
				  ."', m_pic = '".$arrProduct["m_pic"] ."', s_pic = '".$arrProduct["s_pic"] 
				  ."', factory_number = '".$arrProduct["factory_number"]."', number = '".$arrProduct["number"]
				  ."', sid = ".$arrProduct["series"]
				  .", bid = ".$arrProduct["brand"].", model = '".$arrProduct["model"]."', price = '".$arrProduct["price"]
				  ."', price_mill = '".$arrProduct["price_mill"]."', price_member = '".$arrProduct["price_member"]
				  ."', price_market = '".$arrProduct["price_market"]
				  ."', price_special = '".$arrProduct["price_special"]."', online = '".$arrProduct["online"]
				  ."', special_offer = '".$arrProduct["special_offer"]."', market_offer = '".$arrProduct["market_offer"]
				  ."', member_offer = '".$arrProduct["member_offer"]."', product_related = '".$arrProduct["product_related"]
				 /* ."', f1 = '".$arrValue["f1"]
				  ."', f2 = '".$arrValue["f2"]."', f3 = '".$arrValue["f3"]."', f4 = '".$arrValue["f4"]
				  ."', f5 = '".$arrValue["f5"]."', f6 = '".$arrValue["f6"]."', f7 = '".$arrValue["f7"]
				  ."', f8 = '".$arrValue["f8"]."', f9 = '".$arrValue["f9"]."', f10 = '".$arrValue["f10"]
				  ."', f11 = '".$arrValue["f11"]."', f12 = '".$arrValue["f12"]."', f13 = '".$arrValue["f13"]
				  ."', f14 = '".$arrValue["f14"]."', f15 = '".$arrValue["f15"]."', f16 = '".$arrValue["f16"]
				  ."', f17 = '".$arrValue["f17"]."', f18 = '".$arrValue["f18"]."', f19 = '".$arrValue["f19"]
				  ."', f20 = '".$arrValue["f20"]*/
				  ."' WHERE id = '$id';";
			return DBQuery::DB()->execute($sql);
	}

	public static function insertProduct($cid, $arrProduct) {//, $arrValue
		$sql = "INSERT INTO ".DB_TABLE."product(cid, psid, name, enname, store,"
			  ." describes, b_pic, m_pic, s_pic, factory_number, number, sid, bid,"
			  ." model, price, price_mill, price_member, price_market, price_special,"
			  ." online, special_offer, market_offer, member_offer, product_related)"
			  //, f1, f2, f3, f4, f5, f6, f7,"
			  //." f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20
			  ." VALUES('{$cid}',".$arrProduct["psid"].",'".$arrProduct["name"]
			  ."','".$arrProduct["enname"]."','".$arrProduct["store"]."','"
			  .$arrProduct["describe"]."','".$arrProduct["b_pic"]."','"
			  .$arrProduct["m_pic"]."','".$arrProduct["s_pic"]."','".$arrProduct["factory_number"]."','"
			  .$arrProduct["number"]."',".$arrProduct["series"].",".$arrProduct["brand"].",'"
			  .$arrProduct["model"]."','".$arrProduct["price"]."','"
			  .$arrProduct["price_mill"]."','".$arrProduct["price_member"]."','"
			  .$arrProduct["price_market"]."','"
			  .$arrProduct["price_special"]."','".$arrProduct["online"]."','"
			  .$arrProduct["special_offer"]."','".$arrProduct["member_offer"]."','"
			  .$arrProduct["market_offer"]."','".$arrProduct["product_related"]."');";
			  /*,'".$arrValue["f1"]."','".$arrValue["f2"]."','"
			  .$arrValue["f3"]."','".$arrValue["f4"]."','".$arrValue["f5"]."','"
			  .$arrValue["f6"]."','".$arrValue["f7"]."','".$arrValue["f8"]."','"
			  .$arrValue["f9"]."','".$arrValue["f10"]."','".$arrValue["f11"]."','"
			  .$arrValue["f12"]."','".$arrValue["f13"]."','".$arrValue["f14"]."','"
			  .$arrValue["f15"]."','".$arrValue["f16"]."','".$arrValue["f17"]."','"
			  .$arrValue["f18"]."','".$arrValue["f19"]."','".$arrValue["f20"]."'*/
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getInsertProductId() {
		return DBQuery::DB()->getInsertID();
	}
	
	public static function insertProductAttribute($cid, $pid, $atr) {
		$sql = "DELETE FROM ".DB_TABLE."product_attribute WHERE pid = '{$pid}';";
		DBQuery::DB()->execute($sql);
		$sql = "INSERT INTO ".DB_TABLE."product_attribute(cid, pid, atrid, value) VALUES " . $atr;
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateProductFilter($cid, $pid, $sqlfilter) {
		$sql = "UPDATE ".DB_TABLE."product SET $sqlfilter WHERE cid = '{$cid}' AND id = '{$pid}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function checkInsertProduct($num) {
		$sql = "SELECT id FROM ".DB_TABLE."product WHERE number = '{$num}';";
		return DBQuery::DB()->getOne($sql);
	}
	
	public static function deleteProduct($cid = NULL, $id = NULL) {
		$sql = "DELETE FROM ".DB_TABLE."product WHERE 1=1";
		if(!empty($cid)) {
			$sql .= " AND cid = '{$cid}'";
		}
		if(!empty($id)) {
			$sql .= " AND id = '{$id}'";
		}
		return DBQuery::DB()->execute($sql);
	}

	public static function divertProduct($cid, $id) {
		$sql = "UPDATE ".DB_TABLE."product SET cid = '{$cid}' WHERE id = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	//end product
	
	//begin news
	public static function updateNews($cid, $id, $arrNews) {//$arrValue, 
		$sql = "UPDATE ".DB_TABLE."news SET cid = '".$cid."', title = '".$arrNews["title"]
			  ."', author = '".$arrNews["author"]
			  ."', source_from = '".$arrNews["source_from"]."',content = '".$arrNews["content"]
			  ."' WHERE id = '$id';";
		return DBQuery::DB()->execute($sql);
	}

	public static function insertNews($cid, $arrNews) {//, $arrValue
		$sql = "INSERT INTO ".DB_TABLE."news(cid, title, author, source_from,"
			  ." content)"
			  ." VALUES('{$cid}','".$arrNews["title"]."','".$arrNews["author"]."','".$arrNews["source_from"]."','"
			  .$arrNews["content"]."');";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function getInsertNewsId() {
		return DBQuery::DB()->getInsertID();
	}
	
	public static function deleteNews($id) {
		$sql = "DELETE FROM ".DB_TABLE."news WHERE id = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	//end news
	
	//begin Attribute
	public static function updateAttribute($cid, $arrValue, $arrFliter, $arrAttid, $arrMainatr) {
		$str = $strn = '';
		$isdotn = $isdot = false;
		foreach($arrValue as $k => $v) {
			if(trim($v) == NULL) continue;
			$dotn = $isdotn == false ? '' : ',';
			$dot = $isdot == false ? '' : ',';
			$arrFliter[$k] = $arrFliter[$k] == '1' ? 1 : 0;
			if(trim($arrAttid[$k]) == NULL) {	
				$isdotn = true;
				$strn .= $dotn."('".$cid."','".$v."','".$arrFliter[$k]."',".($k+1).",'".$arrMainatr[$k]."')";
				continue;
			} else {
				$isdot = true;
				$str .= $dot."('".$arrAttid[$k]."','".$cid."','".$v."','".$arrFliter[$k]."',".($k+1).",'".$arrMainatr[$k]."')";
			}
		}
		if($strn != '') {
			$sqln = "INSERT INTO ".DB_TABLE."attribute (cid, name, isfilter, orderid, mainatr) VALUES ".$strn;
			return DBQuery::DB()->execute($sqln);
		}
		if($str != '') {
			$sql = "REPLACE INTO ".DB_TABLE."attribute (id, cid, name, isfilter, orderid, mainatr) VALUES ".$str;
			return DBQuery::DB()->execute($sql);
		}
	}
	
	public static function delAttribute($cid, $orderid = NULL) {
		$sql = "DELETE FROM ".DB_TABLE."attribute WHERE cid = '{$cid}'";
		if(!empty($orderid)) {
			$sql .= " AND orderid = '{$orderid}'";
		}
		return DBQuery::DB()->execute($sql);
	}
	//end Attribute
	
	//begin Freight
	public static function insertFreight($name, $freight, $shop_price, $describes) {
		$sql = "INSERT INTO ".DB_TABLE."shop_freight (name,freight,shop_price,describes) VALUES ('"
		      .$name."','".$freight."','".$shop_price."','".$describes."');";
		return DBQuery::DB()->execute($sql);	
	}
	
	public static function updateFreight($id, $name, $freight, $shop_price, $describes) {
		$sql = "UPDATE ".DB_TABLE."shop_freight SET name = '".$name
		      ."', freight = '".$freight."', shop_price = '".$shop_price
			  ."', describes = '".$describes."' WHERE id = '".$id."';";
		return DBQuery::DB()->execute($sql);	
	}

	public static function deleteFreight($id) {
		$sql = "DELETE FROM ".DB_TABLE."shop_freight WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);
	}
	//end Freight
	
	//begin Bank
	public static function insertBank($name, $payee, $accounts, $describes, $type) {
		$sql = "INSERT INTO ".DB_TABLE."shop_bank (name,payee,accounts,describes, type)"
		      ." VALUES ('{$name}','{$payee}','{$accounts}','{$describes}','{$type}');";
		return DBQuery::DB()->execute($sql);	
	}
	
	public static function updateBank($id, $name, $payee, $accounts, $describes) {
		$sql = "UPDATE ".DB_TABLE."shop_bank SET name = '".$name
		      ."', payee = '".$payee."', accounts = '".$accounts."', describes = '".$describes."' WHERE id = '".$id."';";
		return DBQuery::DB()->execute($sql);	
	}

	public static function deleteBank($id) {
		$sql = "DELETE FROM ".DB_TABLE."shop_bank WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);
	}
	//end Bank
	
	//begin Series
	public static function insertSeries($bid, $name, $enname) {
		$sql = "INSERT INTO ".DB_TABLE."product_series (bid, name,enname) VALUES ('{$bid}', '{$name}','{$enname}');";
		return DBQuery::DB()->execute($sql);	
	}
	
	public static function updateSeries($id, $name, $enname) {
		$sql = "UPDATE ".DB_TABLE."product_series SET name = '".$name."', enname = '".$enname."' WHERE id = '".$id."';";
		return DBQuery::DB()->execute($sql);	
	}
	
	public static function deleteSeries($id) {
		$sql = "DELETE FROM ".DB_TABLE."product_series WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);	
	}
	//end Series
	
	//begin Brand
	public static function insertBrand($name, $enname) {
		$sql = "INSERT INTO ".DB_TABLE."product_brand (name,enname) VALUES ('{$name}','{$enname}');";
		return DBQuery::DB()->execute($sql);	
	}
	
	public static function updateBrand($id, $name, $enname) {
		$sql = "UPDATE ".DB_TABLE."product_brand SET name = '".$name."', enname = '".$enname."' WHERE id = '".$id."';";
		return DBQuery::DB()->execute($sql);	
	}
	
	public static function deleteBrand($id) {
		$sql = "DELETE FROM ".DB_TABLE."product_brand WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);	
	}
	//end Brand
	
	//begin Supply 
	public static function insertSupply ($name, $phone, $mobile, $email, $address, $describes) {
		$sql = "INSERT INTO ".DB_TABLE."shop_supply (name,phone,mobile,email, address,describes)"
		      ." VALUES ('{$name}','{$phone}','{$mobile}','{$email}','{$address}','{$describes}');";
		return DBQuery::DB()->execute($sql);	
	}
	
	public static function updateSupply ($id, $name, $phone, $mobile, $email, $address, $describes) {
		$sql = "UPDATE ".DB_TABLE."shop_supply SET name = '".$name
		      ."', phone = '".$phone."', mobile = '".$mobile
			  ."', email = '".$email."', address = '".$address."', describes = '".$describes."' WHERE id = '".$id."';";
		return DBQuery::DB()->execute($sql);	
	}

	public static function deleteSupply($id) {
		$sql = "DELETE FROM ".DB_TABLE."shop_supply  WHERE id = '{$id}'";
		return DBQuery::DB()->execute($sql);
	}
	//end Supply	
	
	//Check
	public static function getCheck($state = NULL, $bdata = NULL, $edata = NULL, $billno = NULL, $keyword = NULL, $b = 1, $c = 10) {
		$b = ($b-1)*$c;
		$sql = "SELECT billno FROM ".DB_TABLE."order_bill WHERE 1=1";
		if($state != NULL) $sql .= " AND state = '$state'";
		if(!empty($bdata)) $sql .= " AND add_date >= '$bdata'";
		if(!empty($edata)) $sql .= " AND add_date <= '$edata'";
		if(!empty($billno)) $sql .= " AND billno LIKE'%$billno%'";
		if(!empty($keyword)) $sql .= " AND (`name` LIKE'%$keyword%'"
		                            ." OR email LIKE'%$keyword%'"
									." OR ss LIKE'%$keyword%'"
									." OR phone LIKE'%$keyword%'"
									." OR mobile LIKE'%$keyword%'"
									." OR address LIKE'%$keyword%')";
		$sql .= " ORDER BY id DESC LIMIT $b, $c;";
		$arrRs = DBQuery::DB()->getList($sql);
		if(!empty($arrRs)) {
			$sqlstr = '';
			foreach($arrRs as $k => $v) {
				$sqlstr .= "'".$v['billno']."',";
			}
			$sqlstr = trim($sqlstr, ',');
			if(!empty($sqlstr)) {
				$sql = "SELECT ob.*,pb.id pid, pb.*,u.name uname FROM (".DB_TABLE."order_bill ob, "
					  .DB_TABLE."product_bill pb) LEFT JOIN "
					  .DB_TABLE."user u ON u.id=ob.uid WHERE pb.billno=ob.billno"
					  ." AND ob.billno IN($sqlstr) ORDER BY ob.id DESC";
				return DBQuery::DB()->getList($sql);
			}
		}
		return false;	
	}
	
	public static function getNumCheck($state = NULL, $bdata = NULL, $edata = NULL, $billno = NULL, $keyword = NULL) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."order_bill ob WHERE 1=1";
		if($state != NULL) $sql .= " AND ob.state = '$state'";
		if(!empty($bdata)) $sql .= " AND ob.add_date >= '$bdata'";
		if(!empty($edata)) $sql .= " AND ob.add_date <= '$edata'";
		if(!empty($billno)) $sql .= " AND billno LIKE'%$billno%'";
		if(!empty($keyword)) $sql .= " AND (`name` LIKE'%$keyword%'"
		                            ." OR email LIKE'%$keyword%'"
									." OR ss LIKE'%$keyword%'"
									." OR phone LIKE'%$keyword%'"
									." OR mobile LIKE'%$keyword%'"
									." OR address LIKE'%$keyword%')";
		return DBQuery::DB()->getOne($sql);	
	}
	
	public static function getCheckUser($billno) {
		$sql = "SELECT * FROM ".DB_TABLE."order_bill WHERE billno = '{$billno}';";	
		return DBQuery::DB()->getRow($sql);	
	}
	
	public static function updateCheck($billno, $strValues = NULL, $state = NULL, $time = 'NULL') {
		$sql = "UPDATE ".DB_TABLE ."order_bill SET ";
		if(!empty($strValues)) $sql .= $strValues;
		if($state != NULL) $sql .= "state = '{$state}', shipment_date = $time";
		$sql .= " WHERE billno = '{$billno}'";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function deleteCheck($billno) {
		$sql = "DELETE FROM ".DB_TABLE ."product_bill WHERE billno = '{$billno}';";
		DBQuery::DB()->execute($sql);
		$sql = "DELETE FROM ".DB_TABLE ."order_bill WHERE billno = '{$billno}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateSellPrice($billno, $pid, $sellprice, $num) {
		$sql = "UPDATE ".DB_TABLE ."product_bill SET sellprice = '".$sellprice
			."', num = '{$num}' WHERE billno = '{$billno}' AND id = '".$pid."';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updatePayPrice($billno, $sellprice = NULL, $freightprice = NULL) {
		if($sellprice == NULL && $freightprice == NULL) return false;
		$sql = "UPDATE ".DB_TABLE ."order_bill SET";
		if($sellprice != NULL) $sql .= " sellprice = '".$sellprice."', pay_price = $sellprice + freight_price";
		if($freightprice != NULL) $sql .= " pay_price = $freightprice + sellprice";
		$sql .= " WHERE billno = '{$billno}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function addBuyProduct($billno, $id = NULL, $number = NULL, $arrValue, $md5id) {
		$sql = "INSERT INTO ".DB_TABLE ."product_bill "
		      ."SELECT *, '".$arrValue['number']."', '$billno', '$md5id', '".$arrValue['price']."' FROM "
			  .DB_TABLE."product WHERE ";
		if(!empty($id)) $sql .= " id = '{$id}';";
		if(!empty($number)) $sql .= " number = '{$number}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateBuyProductSellPrice($billno, $sellprice) {
		$sql = "UPDATE ".DB_TABLE ."order_bill SET sellprice = sellprice + "
			  .$sellprice.", pay_price = sellprice + freight_price";
		$sql .= " WHERE billno = '{$billno}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function checkBillProduct($id = NULL, $number = NULL, $billno) {
		$sql = "SELECT id FROM "
			  .DB_TABLE."product_bill WHERE billno = '{$billno}'";
		if(!empty($id)) $sql .= " AND id = '{$id}';";
		if(!empty($number)) $sql .= " AND number = '{$number}';";
		return DBQuery::DB()->getOne($sql);
	}
	
	public static function getProductPrice($id = NULL, $number = NULL) {
		$sql = "SELECT price, price_member, price_special, special_offer, member_offer FROM "
			  .DB_TABLE."product WHERE ";
		if(!empty($id)) $sql .= " id = '{$id}';";
		if(!empty($number)) $sql .= " number = '{$number}';";
		return DBQuery::DB()->getRow($sql);
	}
	
	public static function deleteCheckProduct($uid, $billno) {
		$sql = "DELETE FROM ".DB_TABLE ."product_bill WHERE id = '{$uid}' AND billno = '{$billno}';";
		return DBQuery::DB()->execute($sql);
	}
	//end check
	
	//menber
	public static function getUser($mid = NULL, $name = NULL, $b = 1, $c = 10) {
		$b = ($b-1)*$c;
		$sql = "SELECT * FROM ".DB_TABLE."user WHERE 1=1";
		if(!empty($mid)) $sql .= " AND id = '$mid'";
		if(!empty($name)) $sql .= " AND name LIKE'%$name%'";
		$sql .= " ORDER BY reg_date DESC LIMIT $b, $c;";
		return DBQuery::DB()->getList($sql);
	}
	public static function getNumUser($mid = NULL, $name = NULL) {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."user WHERE 1=1";
		if(!empty($mid)) $sql .= " AND id = '$mid'";
		if(!empty($name)) $sql .= " AND name LIKE'%$name%';";
		return DBQuery::DB()->getOne($sql);
	}
	
	public static function deleteUser($id) {
		$sql = "DELETE FROM ".DB_TABLE."user WHERE id = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	
	public static function updateUserInfo($arrValue, $uid) {
		$updateSql = Utilities::formArrayTurn2Str($arrValue);
		$sql = "UPDATE ".DB_TABLE."user SET ".$updateSql." WHERE id = '{$uid}';";
		return DBQuery::DB()->execute($sql);
	}
	//end menber
	
	//ad
	public static function updateadvertisingValue($rValue, $cid = 'NULL') {
		$sql = "REPLACE INTO ".DB_TABLE."advertising (cid, value1, value2, value3, value4, value5) VALUES('$cid','"
			  .$rValue['value1']."','".$rValue['value2']."','".$rValue['value3']."','".$rValue['value4']."','".$rValue['value5']."')";
		return DBQuery::DB()->execute($sql);
	}
	public static function getAdvertising($cid) {
		$sql = "SELECT * FROM ".DB_TABLE."advertising WHERE cid = '{$cid}';";
		return DBQuery::DB()->getRow($sql);
	}
	//end ad
	//review 
	public static function getProductReview($pid = NULL, $b = 1, $c = 10) {
		$b = ($b-1)*$c;
		$where = empty($pid) ? NULL : "WHERE pid='$pid'";
		$sql = "SELECT * FROM ".DB_TABLE."product_review $where ORDER BY id DESC LIMIT $b, $c;";
		return DBQuery::DB()->getList($sql); 
	}
	public static function getNumProductReview() {
		$sql = "SELECT COUNT(*) FROM ".DB_TABLE."product_review;";
		return DBQuery::DB()->getOne($sql); 
	}
	public static function updateReviewInit($id, $init) {
		$sql = "UPDATE ".DB_TABLE."product_review SET init = '{$init}' WHERE id = '{$id}';";
		return DBQuery::DB()->execute($sql);
	}
	public static function deleteProductReview($delrid) {
		$sql = "DELETE FROM ".DB_TABLE."product_review WHERE id = '{$delrid}';";
		return DBQuery::DB()->execute($sql);
	}
}
?>