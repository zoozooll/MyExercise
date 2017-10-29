<?php 
/*
	class.Common.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class Common {
	private static $classesinfo = array();//foruminfo
	private static $siteinfo = array();
	private static $atrinfo = array();
	
	public function __construct() {
		self::checkSite();
	}
	
	public static function chenkSite() {
		$arrForbidIp = explode('|', Common::getSiteInfo('value7', "regAndCon"));
		if(self::getSiteInfo('value7') == '0' || array_search(onLineIp(), $arrForbidIp) !== false) {
			echo stripslashes(self::getSiteInfo('value8'));
			exit;
		}
	}
 	public static function getLaguage($file = "index", $dir = __COMMLAGUAGE) {
		self::chenkSite();
		include_once($dir.self::getSiteInfo("value6")."/".$file.".php");
		return $rs;
	}

	public static function getClassInfo($id = NULL, $cid = NULL, $n ='classesinfo', $dir = __COMMSITE){
		$rs = array();
		if(!isset(self::$classesinfo[$n])) self::$classesinfo[$n] = array();
		if(self::$classesinfo[$n] == NULL){
			$filename = $n.".php";
			if(!file_exists($dir.$filename)){UpdateCommonDao::updateClassesInfo($n, $dir);}
			include_once($dir.$filename);
			self::$classesinfo[$n] = $rs;
		}
		if($id > 0) {
			if(isset(self::$classesinfo[$n][$id])){
				return self::$classesinfo[$n][$id];
			}
			return NULL;
		}
		if($cid > 0) {
			$arrCid = array();
			foreach(self::$classesinfo[$n] as $k => $v) {
				if($v['cid'] == $cid) {
					$arrCid[] = $v;
				}
			}
			return $arrCid;
		}
		return array_values(self::$classesinfo[$n]);
	}
	
	public static function getClassDown($cid) {
		$rsFD = array();
		$rs = self::getClassInfo();
		$num = count($rs);
		for($i = 0; $i < $num; $i++) {
			if($rs[$i]["cid"] == $cid) {
				$rsFD[] = $rs[$i];
			}
		}
		return $rsFD;
	}
	
	public static function &getClassNav($id) {
		$rs = self::getClassInfo($id);
		if(isset($rs["cpid"])) {
			if($rs["cpid"] != "0") {
				$rsNav = self::getClassNav($rs["cpid"]);
			}
		}
		$rsNav[] = $rs;/*do {$rs = self::getForumInfo($id);$rsNav[] = $rs;$id = $rs["cpid"];} while ($id != "0");*/
		return $rsNav;
	}
	
	public static function getClassSmallInfo($id) {
		$rs = self::getClassInfo($id);
		$rsFD = self::getClassDown($rs["cid"]);
		$rsFS = array();
		$num = count($rsFD);
		for($i = 0; $i < $num; $i++) {
			if($rs['cpid'] == $rsFD[$i]["cpid"] && $rs['id'] != $rsFD[$i]["id"]) {
				if($rsFD[$i]["orderid"] > $rs["orderid"]) break;
			}
			if($rsFD[$i]["depth"] > $rs["depth"] && $rsFD[$i]["orderid"] > $rs["orderid"]) {
				$rsFS[] = $rsFD[$i];
			}
		}
		return $rsFS;
	}
	
	public static function getSiteInfo($file = NULL, $n = "baseset", $dir = __COMMSITE) {
		if(!isset(self::$siteinfo[$n])) self::$siteinfo[$n] = array();
		if(self::$siteinfo[$n] == NULL){
			$filename = $n."cache.php";
			if(!file_exists($dir.$filename)){UpdateCommonDao::updateSiteInfo($n, "*", $dir);}
			include_once($dir.$filename);
			self::$siteinfo[$n] = $rs;
		}
		if($file != NULL) {
			return self::$siteinfo[$n][$file];
		} 
		return self::$siteinfo[$n];
	}
				
	public static function getMeta($title = NULL, $description = NULL, $keywords = NULL, $content = NULL) {
		$title = empty($title) ? '' : $title . ','; 
		$description = empty($description) ? '' : $description . ',';  
		$keywords = empty($keywords) ? '' : $keywords . ','; 
		$content = empty($content) ? '' : $content . ','; 
		$rs['Site'] = self::getSiteInfo("value1");
		$rs['Title'] = $title . self::getSiteInfo("value1").', '.self::getSiteInfo("value2", "searchoptim");
		$rs['Keywords'] = $keywords . self::getSiteInfo("value3", "searchoptim");
		$rs['Description'] = $description . self::getSiteInfo("value4", "searchoptim");
		$rs['Content'] = $content . self::getSiteInfo("value5", "searchoptim");
		$rs['Icp'] = self::getSiteInfo("value5");
		$rs['Url'] = self::getSiteInfo("value3");
		$rs['Copyright'] = self::getSiteInfo("value2");
		$rs['Logo'] = self::getSiteInfo("value4");
		$rs['Laguage'] = self::getSiteInfo("value6");
		$rs['Stat'] = Utilities::stripSlashesStr(self::getSiteInfo("value9"));
		$rs['qq'] = explode(';', trim(self::getSiteInfo("value1", 'service'), ';'));
		$rs['msn'] = explode(';', trim(self::getSiteInfo("value2", 'service'), ';'));
		$rs['taobao'] = explode(';', trim(self::getSiteInfo("value3", 'service'), ';'));
		$rs['web_address'] = self::getSiteInfo("value4", 'service');
		$rs['web_postcode'] = self::getSiteInfo("value5", 'service');
		$rs['web_phone'] = self::getSiteInfo("value6", 'service');
		$rs['web_mobile'] = self::getSiteInfo("value7", 'service');
		$rs['web_fax'] = self::getSiteInfo("value8", 'service');
		$rs['web_email'] = self::getSiteInfo("value9", 'service');
		$rs['web_free_phone'] = self::getSiteInfo("value10", 'service');
		$rs['web_free_mobile'] = self::getSiteInfo("value11", 'service');
		return $rs;
	}
	public static function checkCommonCache() {
		if(self::getSiteInfo('value9') == '1' && self::getSiteInfo('value10') > 0) {
			return true;
		}
		return false;
	}
	
	public static function getCommonCache($cacheid) {
		if(self::checkCommonCache()) {
			return Cache::isValid($cacheid, self::getSiteInfo('value10'));
		}
		return false;
	}
	
	public static function getCommonTime($time = 0) {
		return getDateTime(self::getSiteInfo("value16") + $time);
	}
	
	public static function getLoginUser($objCookie = NULL) {
		if(is_object($objCookie)){
		} else {
			$objCookie = new Cookie;
		}
		$md5id = $objCookie -> muantshopid;
		if(empty($md5id)) {
			$md5id = md5(onLineIp().getDateTimeId().md5(uniqid(rand(), true)));
			$objCookie -> muantshopid = $md5id;
		}
		$userinfo = $objCookie -> muantshop;
		if(!empty($userinfo)) {
			$arrUser = explode('	', $userinfo);
			$arrUserInfo['id'] = $arrUser[0];
			$arrUserInfo['name'] = $arrUser[1];
			$arrUserInfo['email'] = $arrUser[2];
			$arrUserInfo['loginid'] = $arrUser[3];
			return $arrUserInfo;
		}
		return NULL;
	}
	
	public static function getCommonAdvertising($cid, $dir = __COMMSITE) {
		$filename = $cid."cache.php";
		if(!file_exists($dir.$filename)){UpdateCommonDao::updateAdvertising($cid, $dir);}
		include_once($dir.$filename);
		return $rs;
	}
	
	public static function sendMail($tomail, $frommail, $content, $subject, $charcode = 'gb2312') {
		$usersmtp = self::getSiteInfo('value1', "interface");
		$objSmtp = new SmtpMail;
		if($usersmtp == '0') {
			$objSmtp -> smtp(self::getSiteInfo('value2', "interface"), self::getSiteInfo('value3', "interface"), true, self::getSiteInfo('value5', "interface"), self::getSiteInfo('value6', "interface"));
			return $objSmtp->sendmail($tomail, $frommail, $subject, $content, 'HTML', self::getSiteInfo('value4', "interface"), $charcode);
		}
		if($usersmtp == '1') {
			return $objSmtp -> sendSimpleMail($tomail, $frommail, $content, $subject, $charcode);
		}
		return false;
	}
	
}
?>