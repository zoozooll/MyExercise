<?php 
/*
	class.UpdateCommonDao.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class UpdateCommonDao {
	
	public static function updateClassesInfo($n = 'classesinfo', $spliter = "&#8212;", $dir = __COMMSITE) {
		$filename = $n.".php";
		$rs = ClassesDao::getClasssSetValue();
		$channel_name = '';
		$num = count($rs);
		for($i = 0; $i < $num; $i ++) {
			$rs[$i]["enname"] = trim($rs[$i]["enname"]);
			if(empty($rs[$i]["enname"])) $rs[$i]["enname"] = $rs[$i]["id"];
			if($rs[$i]["cpid"] == '0') $channel_name = $rs[$i]["enname"];
			if($rs[$i]["url"] == NULL) {
				if($rs[$i]["cpid"] == '0') {
					$rs[$i]["url"] = Patch::getClassesUrl($rs[$i]["id"], $rs[$i]["enname"]); 
				} else {
					$rs[$i]["url"] = Patch::getCategoryUrl($channel_name, $rs[$i]["id"], $rs[$i]["enname"]); 
				}
			}
			$f = NULL;
			$dot = $rs[$i]['spliter'] ? $rs[$i]['spliter'] : $spliter;
			for($j = 1; $j < $rs[$i]['depth']; $j++) {
				$f .= "$dot";//"&#8212;"
			}
			if(isset($rs[$i+1]["cpid"])) {
				if($rs[$i+1]["cpid"] == '0') {
					$rs[$i]["end"] = 1;
					$rs[$i]["more_url"] = Patch::getClassesUrl($rs[$i]["cid"], $channel_name); 
				}
			} else {
				$rs[$i]["end"] = 0;
			}
			if(($i + 1) == $num) {
				$rs[$i]["end"] = 1;
				$rs[$i]["more_url"] = Patch::getClassesUrl($rs[$i]["cid"], $channel_name); 
			}
			$rs[$i]["spliterchar"] = $f; 
			$rInfo[$rs[$i]["id"]] = $rs[$i];
		}
		if(empty($rInfo)) $rInfo = array();
		$contant = "<?php\r\n".'$rs'." = ".Utilities::arrayTurnStr(Utilities::addslashesStr($rInfo)).";\r\n?>";
		File::creatFile($filename, $contant, $dir);
	}
	
	public static function updateSiteInfo($n = "baseset", $v = '*', $dir = __COMMSITE) {
		$filename = $n."cache.php";
		$rInfo = AdminDao::getBasesetValue($n, $v);
		if(empty($rInfo)) $rInfo = array();
		$contant = Utilities::arrayTurnStr(Utilities::addslashesStr($rInfo));
		$contant = "<?php\r\n".'$rs'." = ".$contant.";\r\n?>";
		File::creatFile($filename, $contant, $dir);
	}
	
	public static function updatePayInfo($n, $v, $dir = __COMMSITE) {
		$filename = $n.".php";
		File::creatFile($filename, "<?php\r\n".$v."\r\n?>", $dir);
	}
	
	public static function updateAdvertising($cid, $dir = __COMMSITE) {
		$filename = $cid."cache.php";
		$arrRs = AdminDao::getAdvertising($cid);
		if(empty($arrRs)) {
			$arrRs = array();
		} else {
			$arrRs['value1'] = self::setAdvertising(explode("\n",trim($arrRs['value1'])));
			$arrRs['value2'] = self::setAdvertising(explode("\n",trim($arrRs['value2'])));
			$arrRs['value3'] = self::setAdvertising(explode("\n",trim($arrRs['value3'])));
			$arrRs['value4'] = self::setAdvertising(explode("\n",trim($arrRs['value4'])));
			$arrRs['value5'] = self::setAdvertising(explode("\n",trim($arrRs['value5'])));
		}
		$contant = Utilities::arrayTurnStr(Utilities::addslashesStr($arrRs));
		$contant = "<?php\r\n".'$rs'." = ".$contant.";\r\n?>";
		File::creatFile($filename, $contant, $dir);
	}
	
	public static function setAdvertising($arrRs) {
		$num = count($arrRs);
		$arrRss = array();
		$arrExplode = array();
		for($i = 0; $i<$num; $i++) {
			if(!isset($arrRs[$i]) || empty($arrRs[$i])) continue;
			$arrExplode = explode('|', $arrRs[$i]);
			if(empty($arrExplode[0]) || !isset($arrExplode[1]))  continue;
			if(!isset($arrExplode[2])) $arrExplode[2] = '';
			if(!isset($arrExplode[3])) $arrExplode[3] = '';
			if(!isset($arrExplode[4])) $arrExplode[4] = '';
			$arrRss[$i]['pic'] = trim($arrExplode[0]);
			$arrRss[$i]['url'] = trim($arrExplode[1]);
			$arrRss[$i]['text'] = trim($arrExplode[2]);
			$arrRss[$i]['title'] = trim($arrExplode[3]);
			$arrRss[$i]['contents'] = trim($arrExplode[4]);

		}
		return $arrRss;
	}
}
?>