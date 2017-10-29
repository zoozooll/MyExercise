<?php
/*
	class.Utilities.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/

class Utilities {

	public static function &arrayTurnStr($data) {
		$str = '';
		if(is_array($data)) {
			$str .= "array(";
			foreach($data as $k => $v) {
				if(is_array($v)) {
					$str .= "'$k' => ";
					$str .= self::arrayTurnStr($v).',';
				} else {
					$str .= "'$k' => '$v',";
				}
			}
			$str = trim($str, ',').")";
		}
		return $str;
	}
	
	public static function &addslashesStr($data) {
		if(is_array($data)) {
			foreach($data as $key => $val) {
				if(is_array($val)) {
					$data[$key] = self::addslashesStr($val);
				} else {
					$data[$key] = addslashes($val);
				}
			}
		} else {
			$data = addslashes($data);
		}
		return $data;
	}
	
	public static function &stripSlashesStr($data) {
		if(is_array($data)) {
			foreach($data as $key => $val) {
				if(is_array($val)) {
					$data[$key] = self::stripSlashesStr($val);
				} else {
					$data[$key] = stripslashes($val);
				}
			}
		} else {
			$data = stripslashes($data);
		}
		return $data;
	}

	public static function &toHtml($data) {
		if(is_array($data)) {
			foreach($data as $key => $val) {
				if(is_array($val)) {
					$data[$key] = self::toHtml($val);
				} else {
					$data[$key] = nl2br(str_replace(" ", "&nbsp;", htmlspecialchars($val)));
				}
			}
		} else {
			$data = nl2br(str_replace(" ", "&nbsp;", htmlspecialchars($data)));
		}
		return $data;
	}
	
	public static function formatXmlSpecialChar($str) {
		return str_replace("'",'&apos;',str_replace('"','&quot;',str_replace('<','&lt;',str_replace('>','&gt;',str_replace('&','&amp;',$str)))));
	}
	
	public static function cutString($str, $len, $start = 0) {
		if(strlen($str) <= $len) {
			return $str;
		}
		for($loop=0; $loop<$len; $loop++) {
			if(ord($str[$loop]) > 224) {
				$loop += 2;
				continue;
			}
			if(ord($str[$loop]) > 192) {
				$loop++;
			}
		}
		/*if($loop == $len + 1) {
			$len--;
		}*/
		return substr($str, 0, $loop) . "...";
	}
	
	//$groundImage ԭͼ��$waterPos ˮӡλ�ã�$baseImage ˮӡ͸��ͼƬ��$waterImage ˮӡͼƬ��$tagetImage Ŀ��ͼ��
	public static function rectifyImages($groundImage, $waterPos=0, $baseImage="", $waterImage="", $max_width, $max_height, $tagetImage, $outtype = 'jpg'){ 
		$isWaterImage = FALSE; 
		
		if(!empty($waterImage) && file_exists($waterImage)) { 
			$isWaterImage = TRUE; 
			$water_info = getimagesize($waterImage); 
			$water_w    = $water_info[0];
			$water_h    = $water_info[1];
			switch($water_info[2]){
				case 1:$water_im = imagecreatefromgif($waterImage);break; 
				case 2:$water_im = imagecreatefromjpeg($waterImage);break; 
				case 3:$water_im = imagecreatefrompng($waterImage);break; 
			} 
		} else { 
			return false; 
		} 
		//��ȡ͸��ͼƬ 
		if(!empty($baseImage) && file_exists($baseImage)) { 
			$base_info = getimagesize($baseImage); 
			$base_w    = $base_info[0];
			$base_h    = $base_info[1];
			switch($base_info[2]){
				case 1:$base_im = imagecreatefromgif($baseImage);break; 
				case 2:$base_im = imagecreatefromjpeg($baseImage);break; 
				case 3:$base_im = imagecreatefrompng($baseImage);break; 
			} 
		}else { 
			return false;; 
		} 
		//��ȡ����ͼƬ 
		if(!empty($groundImage) && file_exists($groundImage)) { 
			$ground_info = getimagesize($groundImage); 
			$ground_w    = $ground_info[0];
			$ground_h    = $ground_info[1];
	
			switch($ground_info[2]){
				case 1:$ground_im = @imagecreatefromgif($groundImage);break; 
				case 2:$ground_im = @imagecreatefromjpeg($groundImage);break; 
				case 3:$ground_im = @imagecreatefrompng($groundImage);break; 
			} 
			if (!$ground_im){
			   return false;  
			}
		}else { 
			return false; 
		} 
	
		if($ground_w <= $max_width && $ground_h <= $max_height){ 
			$dst_width = $ground_w;
			$dst_height = $ground_h;
		}elseif($ground_w/$ground_h >= $max_width/$max_height){ 
			$dst_width = $max_width;
			$dst_height = (int)($max_width*$ground_h/$ground_w);
		}else{ 
			$dst_width = (int)($max_height*$ground_w/$ground_h);
			$dst_height = $max_height;
		}
	
		if($isWaterImage) { 
			$w = $max_width; 
			$h = $max_height; 
			$label = "picture's"; 
		} 
		switch($waterPos) { 
			case 0://��� 
				$posX = rand(0,($w - $dst_width)); 
				$posY = rand(0,($h - $dst_height)); 
				break; 
			case 1://1Ϊ���˾��� 
				$posX = 0; 
				$posY = 0; 
				break; 
			case 2://2Ϊ���˾��� 
				$posX = ($w - $dst_width) / 2; 
				$posY = 0; 
				break; 
			case 3://3Ϊ���˾��� 
				$posX = $w - $dst_width; 
				$posY = 0; 
				break; 
			case 4://4Ϊ�в����� 
				$posX = 0; 
				$posY = ($h - $dst_height) / 2; 
				break; 
			case 5://5Ϊ�в����� 
				$posX = ($w - $dst_width) / 2; 
				$posY = ($h - $dst_height) / 2; 
				break; 
			case 6://6Ϊ�в����� 
				$posX = $w - $dst_width; 
				$posY = ($h - $dst_height) / 2; 
				break; 
			case 7://7Ϊ�׶˾��� 
				$posX = 0; 
				$posY = $h - $dst_height; 
				break; 
			case 8://8Ϊ�׶˾��� 
				$posX = ($w - $dst_width) / 2; 
				$posY = $h - $dst_height; 
				break; 
			case 9://9Ϊ�׶˾��� 
				$posX = $w - $dst_width; 
				$posY = $h - $dst_height; 
				break; 
			default://��� 
				$posX = rand(0,($w - $dst_width)); 
				$posY = rand(0,($h - $dst_height)); 
				break;     
		} 
	
		imagealphablending($base_im, true); 
		if (function_exists("imagecopyresampled")){
			@imagecopyresampled($base_im, $ground_im,$posX,$posY, 0, 0, $dst_width, $dst_height, $ground_w, $ground_h);
		}else{
			@imagecopyresized($base_im, $ground_im,$posX,$posY, 0, 0, $dst_width, $dst_height, $ground_w, $ground_h);
		}
		imagecopy($base_im,$water_im, 0, 0, 0, 0, $water_w,$water_h);
		@unlink($tagetImage); 
		/*switch($ground_info[2]){//ȡ�ñ���ͼƬ�ĸ�ʽ,�涨����jpg��ʽ
			case 1:imagegif($base_im,$tagetImage);break; 
			case 2:imagejpeg($base_im,$tagetImage);break; 
			case 3:imagepng($base_im,$tagetImage);break; 
			default:die($errorMsg); 
		}*/ 
		if($outtype == 'jpg') {
			imagejpeg($base_im,$tagetImage, 100);
		}
		if($outtype == 'png') {
			imagepng($base_im,$tagetImage, 9);
		}
		if($outtype == 'gif') {
			imagegif($base_im,$tagetImage, 100);
		}
		//if(!file_exists($tagetImage)){
		//}else{
		//}
		
		if(isset($water_info)) unset($water_info); 
		if(isset($water_im)) imagedestroy($water_im); 
		unset($ground_info); 
		unset($base_info);
		imagedestroy($ground_im); 
		imagedestroy($base_im); 
		return true;
	} 
	
	//page
	public static function getPageArray($allpage, $pn, $pass = 8, $end = 2) {
		if($allpage < 2) {
			return $arrPage["0"] = 1;
		}
		$pn = $pn -1;
		//if($pn > $end) {$k =  $end + 1;} else {$k = 0;}
		$dotpn = $pn;
		$pass = ($pass + $end) > $allpage ? ($allpage - $end) : $pass;
		$pn = ($pn + $pass + $end) > $allpage ? ($allpage - $pass - $end) : $pn;
		$k = $pn > $end ? $end + 1 : 0;
		if($k > 0) {
			for($i = 0; $i<$k; $i++) {
				$arrPage[$i] =  $pn + $i - $end;
			}
			//$arrPage[$i] = "...";
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
	
	/*
	 *form 2 ά����ת��sql ����������ַ��� ����(),(), 2ά����ʱ �±�Ҫ������ $data[$k][0]������ֵ ����(),(),() ��ʽ) 
	 */
	function formArrayTurnStr($data, $stat = '') {
		if(is_array($data)) {
			$str = '';
			$stat = empty($stat) ? '' : "'$stat', ";
			if(is_array($data[0])) {
				$num1 = count($data);
				$num2 = count($data[0]);
				$num2 = empty($data[0][$num2 - 1]) ? $num2 - 1 : $num2;
				for($i = 0; $i < $num2; $i++) {
					$str .= ",(".$stat;
					for($j = 0; $j < $num1; $j++) {
						if($data[$j][$i] == '') {
							$str .= 'NULL,';
						} else {
							$str .= "'".$data[$j][$i]."',";
						}
					}
					$str = trim($str, ',') . ")";
				}
			} else {
				$str = "(".$stat;
				foreach($data as $k=>$v) {
					if(empty($v)) {
						$str .= 'NULL,';
					} else {
						$str .= "'" . $v . "',";
					}
				}
				$str = trim($str, ',') . ")";
			}
			return trim($str, ',');
		}
		return $data;
	}
	
	/*
	 *form 2 ά����ת��sql �������µ��ַ��� str=str ��ʽ
	 */
	function formArrayTurn2Str($data) {
		$str = ' ';
		if(is_array($data)) {
			foreach($data as $k=>$v) {
				if(is_array($v)) { 
					foreach($v as $kk=>$vv) {
						$str .= $k . "='" . $vv . "',"; 
					}
				} else {
					$str .= $k . "='" . $v . "',"; 
				}
			}
			return trim($str, ',');
		}
		return $data;
	}
}
?>