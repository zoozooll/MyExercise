<?php 
/*
	class.Patch.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class Patch {
	private static $vUrl = NULL;
	public function __construct() {
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
	}
	
	public static function toLower($enname) {
		return strtolower($enname);
	}
	
	public static function getUrl($name, $act) {
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
		if(self::$vUrl == "2") {
			return $name.'-'.$act.'.html';
		} elseif (self::$vUrl == "1") {
			return $name.".php?switch=".$act."&.htm";
		}
		return $name.".php?switch=".$act;
	}

	public static function getClassesUrl($id, $enname) {
		$enname = self::toLower($enname);
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
		if(self::$vUrl == "2") {
			return $enname."-".$id.".html";
		} elseif (self::$vUrl == "1") {
			return "muant.php?cid=".$id."&.htm";
		}
		return "muant.php?cid=".$id;
	}
	
	public static function getCategoryUrl($channel_name, $id, $enname, $arrParam = NULL) {
		$enname = self::toLower($enname);
		$channel_name = self::toLower($channel_name);
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
		$strparam = self::setParam($arrParam, self::$vUrl);
		if(self::$vUrl == "2") {
			return $channel_name.'-'.$id."-".$enname.$strparam.".html";
		} elseif (self::$vUrl == "1") {
			return "category.php?cid=".$id.$strparam."&.htm";
		}
		return "category.php?cid=".$id.$strparam;
	}
	
	public static function getNewsUrl($id) {
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
		if(self::$vUrl == "2") {
			return 'news-'.$id.".html";
		} elseif (self::$vUrl == "1") {
			return "news.php?id=".$id."&.htm";
		}
		return "news.php?id=".$id;
	}
	
	public static function getProductNewsUrl($cid = '', $pn = '') {
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
		if(self::$vUrl == "2") {
			return 'productnews-'.$cid."--".$pn.".html";
		} elseif (self::$vUrl == "1") {
			return "productnews.php?cid=".$cid."&pn=".$pn.".htm";
		}
		return "productnews.php?cid=".$cid."&pn=".$pn;
	}
	
	public static function getSpecialUrl($cid = '', $pn = '', $switch) {
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
		if(self::$vUrl == "2") {
			return $switch . '-'.$cid."--".$pn.".html";
		} elseif (self::$vUrl == "1") {
			return $switch . ".php?cid=".$cid."&pn=".$pn.".htm";
		}
		return $switch . ".php?cid=".$cid."&pn=".$pn;
	}
	
	public static function getProductUrl($id) {
		if(self::$vUrl == NULL) { 
			self::$vUrl = Common::getSiteInfo("value6", "searchoptim");
		}
		if(self::$vUrl == "2") {
			return 'product-'.$id.'.html';
		} elseif (self::$vUrl == "1") {
			return "product.php?pid=".$id."&.htm";
		}
		return "product.php?pid=".$id;
	}
	
	public static function getProductImg($imgid) {
		if(is_numeric($imgid)) return 'product/src/'.$imgid.'.jpg';
		return $imgid;
	}	
	
	public static function getProductSImg($imgid) {
		if(is_numeric($imgid)) return 'product/s/'.$imgid.'.jpg';
		return $imgid;
	}
	
	public static function getProductBImg($imgid) {
		if(is_numeric($imgid)) return 'product/b/'.$imgid.'.jpg';
		return $imgid;
	}
	
	public static function getProductMImg($imgid) {
		if(is_numeric($imgid)) return 'product/m/'.$imgid.'.jpg';
		return $imgid;
	}
	
	public static function getUserPic($picurl, $info = NULL) {
		if($picurl == NULL) {
			return "<img src='images/default/defaultuserpic.jpg' border = '0' alt='$info'>";
		} 
		return "<img src='".$picurl."' border = '0' alt='$info'>";
	}
	
	public static function getPageArray($allpage, $pn, $pass = 8, $end = 2) {
		if($allpage < 2) {
			$arrPage["0"] = 1;
			return $arrPage;
		}
		$pn = $pn -1;//if($pn > $end) {$k =  $end + 1;} else {$k = 0;}
		$dotpn = $pn;
		$pass = ($pass + $end) > $allpage ? ($allpage - $end) : $pass;
		$pn = ($pn + $pass + $end) > $allpage ? ($allpage - $pass - $end) : $pn;
		$k = $pn > $end ? $end + 1 : 0;
		if($k > 0) {
			for($i = 0; $i<$k; $i++) {
				$arrPage[$i] =  $pn + $i - $end;
			}//$arrPage[$i] = "...";
		}
		for($i = 0; $i<$pass; $i++) {
			$arrPage[$i + $k] = $pn + $i + 1;
		}
		if(($allpage > ($pass + $end)) && ($dotpn < ($allpage - $end - $pass))) {
			$arrPage[$i + $k] = "";
			$i++;
		}
		$arrPage[$i + $k] = $allpage - 1;
		$arrPage[$i + 1 + $k] = $allpage;
		return $arrPage;
	}
	
	public static function setParam($arrParam, $type = NULL) {
		if(!array($arrParam)) return $arrParam;
		$str = '';
		if($type == 2) {
			if(!empty($arrParam)) {
				foreach($arrParam as $k=>$v) {
					if($v == '') continue;
					$str .= '-'.$k.'_'.$v;
				}
			}
		} else {
			if(!empty($arrParam)) {
				foreach($arrParam as $k=>$v) {
					if($v == '') continue;
					$str .= '&'.$k.'='.$v;
				}
			}
		}
		return $str;
	}
}
?>