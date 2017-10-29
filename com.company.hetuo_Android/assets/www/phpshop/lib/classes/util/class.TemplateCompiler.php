<?php 
/*
	class.TemplateCompiler.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class TemplateCompiler {

	public static $vartag = '$this->__muant';
	public function __construct() {
	}
	
	public static function compiler($contents, $style) {
		$contents = self::getVarLoop(self::getVarTag($contents));
		$contents = self::getVarIf(self::getEnd($contents));
		$contents = self::getVarINC($contents, $style);
		return $contents;
	}
	
	private static function getVarTag($contents) {
		preg_match_all('/{\$([\$A-Za-z0-9_\.\-]+)}/', $contents, $rs);
		foreach($rs[1] as $k => $v) {
			$vartag = self::$vartag;
			$str = self::getDotVar($v);
			$vartag = '<?php echo '.$vartag.$str.' ?>';
			$contents = str_replace($rs[0][$k], $vartag, $contents);
		}
		return $contents;
	}
	
	private static function getVarLoop($contents) {
		preg_match_all('/{\$loop\s([\[\]A-Za-z0-9_\.]+)}/', $contents, $rs);
		foreach($rs[1] as $k => $v) {
			$vartag = self::$vartag;
			$str = '';
			$tmp = explode('.', $v);
			$num = count($tmp);
			for($i = 0; $i<$num; $i++) {
				if($tmp[$i] == NULL) {
					continue;
				}
				$tmp[$i] = ($tmp[$i] == 'loop') ? '$'.$tmp[$i-1].'i' : $tmp[$i];
				$str .= '["'.$tmp[$i].'"]';
			}
			$strvar = $tmp[$num-1];
			$vartag = '<?php $'.$strvar.'inum = count('.$vartag.$str .'); for($'.$strvar.'i = 0; $'.$strvar.'i<$'.$strvar.'inum; $'.$strvar.'i++) { ?>';
			$contents = str_replace($rs[0][$k], $vartag, $contents);
		}
		return $contents;
	}
	
	private static function getEnd($contents) {
		preg_match_all('/{\/(if|loop)}/', $contents, $rs);
		foreach($rs[1] as $k => $v) {
			$vartag = self::$vartag;
			$vartag = '<?php } ?>';
			$contents = str_replace($rs[0][$k], $vartag, $contents);
		}
		return $contents;
	}
	
	private static function getDotVar($v) {
		$str = '';
		$tmp = explode('.', $v);
		$num = count($tmp);
		for($i = 0; $i < $num; $i++) {
			if(is_numeric($tmp[$i]) or $tmp[$i] == 'NULL' or $tmp[$i] == "''" or $tmp[$i][0] == "'") {
				$str .= $tmp[$i];
				continue;
			}
			$next = NULL;
			$dot = '"';
			switch($tmp[$i]) {
				case 'loop':
					$tmp[$i] = '$'.$tmp[$i-1].'i';
				break;
				case 'first':
					$tmp[$i] = '0';
				break;
				case 'next':
					$next = '+1';
					$tmp[$i] = '$'.$tmp[$i-1].'i';
				break;
				case 'preve':
					$next = '-1';
					$tmp[$i] = '$'.$tmp[$i-1].'i';
				break;
				case 'last':
					$next = '-1';
					$tmp[$i] = 'count('.self::$vartag.'["'.$tmp[$i-1].'"])';
					$dot = NULL;
				break;
			}
			$str .= '['.$dot.$tmp[$i].$dot.$next.']';
		}
		return $str;
	}
	
	private static function getVarIf($contents) {
		$contents = self::getElse(self::getIf($contents));
		preg_match_all('/{else}/', $contents, $rs);
		foreach($rs[0] as $k => $v) {
			$vartag = self::$vartag;
			$vartag = '<?php } else { ?>';
			$contents = str_replace($rs[0][$k], $vartag, $contents);
		}
		return $contents;
	}
	private static function getIf($contents) {
		preg_match_all('/{if\s(.+?)}/', $contents, $rs);
		$strb = '';
		$stre = '';
		$tag = '';
		foreach($rs[1] as $k => $v) {
			$vartag = self::$vartag;
			preg_match_all('/(.+)(\==|\!=|\<=|\>=|\<|\>)(.+)/', $v, $vrs);
			if(isset($vrs[1][0])) $strb = $vartag.self::getDotVar($vrs[1][0]);
			if(isset($vrs[3][0])) $stre = self::getDotVar($vrs[3][0]);
			if($stre == '[""]') {
				$stre = '';
			}
			if(is_numeric($stre) or $stre == 'NULL' or $stre == "''" or $stre[0] == "'") {
			} elseif(trim($stre) != '') {
				$stre = $vartag.$stre;//1
			} else {
				$stre = '';
			}
			if(isset($vrs[2][0]))$tag =  $vrs[2][0];
			$vartag = '<?php if('.$strb.$tag.$stre.') { ?>';
			$contents = str_replace($rs[0][$k], $vartag, $contents);
		}
		return $contents;
	}
	
	private static function getElse($contents) {
		preg_match_all('/{elseif\s(.+?)}/', $contents, $rs);
		foreach($rs[1] as $k => $v) {
			$vartag = self::$vartag;
			preg_match_all('/(.+)(\==|\!=|\<=|\>=|\<|\>)(.+)/', $v, $vrs);
			$strb = $vartag.self::getDotVar($vrs[1][0]);
			$stre = self::getDotVar($vrs[3][0]);
			if($stre == '[""]') {
				$stre = '';
			}
			if(is_numeric($stre) or $stre == 'NULL' or $stre == "''" or $stre[0] == "'") {
			} elseif(trim($stre) != '') {
				$stre = $vartag.$stre;//1
			} else {
				$stre = '';
			}
			$tag =  $vrs[2][0];
			$vartag = '<?php } elseif('.$strb.$tag.$stre.') { ?>';
			$contents = str_replace($rs[0][$k], $vartag, $contents);
		}
		return $contents;
	}

	private static function getVarINC($contents, $style) {
		preg_match_all('/{inc\sfile=([\/A-Za-z0-9_\.\#]+)}/', $contents, $rs);
		foreach($rs[1] as $k => $v) {
			//$compiledfile = Template::getDir($v).Template::getDirfile($v).'.php';
			$compiledfile = $v.'.php';
			Template::compilerfile($compiledfile, $v, $style);
			$vartag = '<?php include("'.$compiledfile.'") ?>';
			$contents = str_replace($rs[0][$k], $vartag, $contents);
		}
		return $contents;
	}
	
	private static function getVarIncFile($contents) {
		preg_match_all('/{inc\sfile=([\/A-Za-z0-9_\.\#]+)}/', $contents, $rs);
		foreach($rs[1] as $k => $v) {
			$vartag = self::$vartag;
			$templatefile = $v;
		}
		return $templatefile;
	}
}
?>