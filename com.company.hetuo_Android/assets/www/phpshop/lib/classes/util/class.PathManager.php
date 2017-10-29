<?php
/*
 * Created on 2006-6-23
 * class.PathManager.php
 * -------------------------
 * 
 * 所有路径相关(包括URL)的方法
 * 
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class PathManager {

	protected static function toLower($str){
		return strtolower($str);
	}
	
	public static function getMypic($mypic = NULL) {
		if(empty($mypic)) {
			return "images/default/defaultmypic.jpg";
		}
		return $mypic;
	}
	
	public static function getPicWebPath($userid = NULL, $tableid = NULL, $n = NULL, $p = "source") {
		if($userid == NULL) {
			$fileid = "guest";
			return __IMAGE_DOMAIN_NAME.$fileid."/pic/".$p."/".$n;
		} else {
			$memberid = CommonDao::common($tableid, "memberid");
			$fileid = self::creatFileId($userid);
			return __IMAGE_DOMAIN_NAME.$memberid."/".$fileid."/".$userid."/pic/".$p."/".$n;
		}
	}
	
	public static function getPicPhyPath($userid = NULL, $tableid = NULL, $n = NULL, $p = "source") {
		if($userid == NULL) {
			return __GUEST_FILE."/pic/".$p."/".$n;
		} else {
			$memberid = CommonDao::common($tableid, "memberid");
			$fileid = self::creatFileId($userid);
			return __USER_FILE.$memberid."/".$fileid."/".$userid."/pic/".$p."/".$n;
		}
	}
	
	public static function getXMLWebPath($userid = NULL, $tableid = NULL, $n = NULL) {
		if($userid == NULL) {
			$fileid = "guest";
			return _XML_DOMAIN_NAME.$fileid."/xml/".$n;
		} else {
			$memberid = CommonDao::common($tableid, "memberid");
			$fileid = self::creatFileId($userid);
			return _XML_DOMAIN_NAME.$memberid."/".$fileid."/".$userid."/xml/".$n;
		}
	}
	
	public static function getXMLPhyPath($userid = NULL, $tableid = NULL, $n = NULL) {
		if($userid == NULL) {
			return __GUEST_FILE."/xml/".$n;
		} else {
			$memberid = CommonDao::common($tableid, "memberid");
			$fileid = self::creatFileId($userid);
			return __USER_FILE.$memberid."/".$fileid."/".$userid."/xml/".$n;
		}
	}

	public static function createUserDir($userid, $tableid) {
		$memberid = CommonDao::common($tableid, "memberid");
		$fileid = self::creatFileId($userid);
		$dir = __USER_FILE.$memberid;
		createDir($dir);
		createDir($dir."/".$fileid);
		createDir($dir."/".$fileid."/".$userid);
		createDir($dir."/".$fileid."/".$userid."/pic");
		createDir($dir."/".$fileid."/".$userid."/pic/small");
		createDir($dir."/".$fileid."/".$userid."/pic/source");
		createDir($dir."/".$fileid."/".$userid."/xml");
		createDir($dir."/".$fileid."/".$userid."/xml/tmp");
	}

	public static function createGuestDir() {
	}
	
	public static function creatFileId($userid) {
		if(strlen($userid) >= 2) {
			$fileid = substr($userid, -2);
		} else {
			$fileid = "0" . $userid;
		}
		return $fileid;
	}
}
?>