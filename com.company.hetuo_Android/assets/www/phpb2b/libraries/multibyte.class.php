<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision$
 */
if (function_exists('mb_internal_encoding')) {
	$encoding = $charset;
	if (!empty($encoding)) {
		mb_internal_encoding($encoding);
	}
}

// Patch in multibyte support
if(!function_exists('mb_substr')){
	function mb_substr($string, $start = 0, $length, $encoding  = null) {
		global $charset;
		if (empty($encoding)) {
			$encoding = $charset;
		}
		$string = trim($string);
		if($length && strlen($string) > $length) {
			$wordscut = '';
			if(strtolower($encoding) == 'utf-8') {
				$n = 0;
				$tn = 0;
				$noc = 0;
				while ($n < strlen($string)) {
					$t = ord($string[$n]);
					if($t == 9 || $t == 10 || (32 <= $t && $t <= 126)) {
						$tn = 1;
						$n++;
						$noc++;
					} elseif(194 <= $t && $t <= 223) {
						$tn = 2;
						$n += 2;
						$noc += 2;
					} elseif(224 <= $t && $t < 239) {
						$tn = 3;
						$n += 3;
						$noc += 2;
					} elseif(240 <= $t && $t <= 247) {
						$tn = 4;
						$n += 4;
						$noc += 2;
					} elseif(248 <= $t && $t <= 251) {
						$tn = 5;
						$n += 5;
						$noc += 2;
					} elseif($t == 252 || $t == 253) {
						$tn = 6;
						$n += 6;
						$noc += 2;
					} else {
						$n++;
					}
					if ($noc >= $length) {
						break;
					}
				}
				if ($noc > $length) {
					$n -= $tn;
				}
				$wordscut = substr($string, 0, $n);
			} else {
				for($i = 0; $i < $length - 1; $i++) {
					if(ord($string[$i]) > 127) {
						$wordscut .= $string[$i].$string[$i + 1];
						$i++;
					} else {
						$wordscut .= $string[$i];
					}
				}
			}
			$string = $wordscut;
		}
		return trim($string);
	}
}

class Multibytes extends PbObject
{
	var $table = null;
	
	function &getInstance() {
		static $instance = array();

		if (!$instance) {
			$instance[0] =& new Multibytes();
		}
		return $instance[0];
	}
	
	function utf8($string) {
		$map = array();

		$values = array();
		$find = 1;
		$length = strlen($string);

		for ($i = 0; $i < $length; $i++) {
			$value = ord($string[$i]);

			if ($value < 128) {
				$map[] = $value;
			} else {
				if (count($values) == 0) {
					$find = ($value < 224) ? 2 : 3;
				}
				$values[] = $value;

				if (count($values) === $find) {
					if ($find == 3) {
						$map[] = (($values[0] % 16) * 4096) + (($values[1] % 64) * 64) + ($values[2] % 64);
					} else {
						$map[] = (($values[0] % 32) * 64) + ($values[1] % 64);
					}
					$values = array();
					$find = 1;
				}
			}
		}
		return $map;
	}
	
	function ascii($array) {
		$ascii = '';

		foreach ($array as $utf8) {
			if ($utf8 < 128) {
				$ascii .= chr($utf8);
			} elseif ($utf8 < 2048) {
				$ascii .= chr(192 + (($utf8 - ($utf8 % 64)) / 64));
				$ascii .= chr(128 + ($utf8 % 64));
			} else {
				$ascii .= chr(224 + (($utf8 - ($utf8 % 4096)) / 4096));
				$ascii .= chr(128 + ((($utf8 % 4096) - ($utf8 % 64)) / 64));
				$ascii .= chr(128 + ($utf8 % 64));
			}
		}
		return $ascii;
	}
	
	function substr($string, $start = 0, $length = 2147483647) {
		if ($start === 0 && $length === null) {
			return $string;
		}

		$string = Multibytes::utf8($string);
		$stringCount = count($string);

		for ($i = 1; $i <= $start; $i++) {
			unset($string[$i - 1]);
		}

		if ($length === null || count($string) < $length) {
			return Multibytes::ascii($string);
		}
		$string = array_values($string);

		$value = array();
		for ($i = 0; $i < $length; $i++) {
			$value[] = $string[$i];
		}
		return Multibytes::ascii($value);
	}
}
?>