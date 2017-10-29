<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/**
 * 截取字符串的函数
 *
 * @param	string		$str		被截取的字符串
 * @param	int			$length		截取的长度
 * @param	bool		$append		是否附加省略号
 * $param	string		$charset	编码设置，utf8,gbk
 *
 * @return  string
 */
function sub_str($str, $length = 0, $append = true, $charset='utf8') {
	$str = trim($str);
	$strlength = strlen($str);
	$charset = strtolower($charset);

	if ($charset == 'utf8') {
		$l = 0;
		$i=0;
		while ($i < $strlength) {
			if (ord($str{$i}) < 0x80) { 
				$l++; $i++;
			} else if (ord($str{$i}) < 0xe0) {
				$l++; $i += 2; 
			} else if (ord($str{$i}) < 0xf0) { 
				$l += 2; $i += 3; 
			} else if (ord($str{$i}) < 0xf8) {
				$l += 1; $i += 4; 
			} else if (ord($str{$i}) < 0xfc) { 
				$l += 1; $i += 5; 
			} else if (ord($str{$i}) < 0xfe) { 
				$l += 1; $i += 6; 
			}

			if ($l >= $length) { 
				$newstr = substr($str, 0, $i);
				break;
			}
		}
		if($l < $length) {
			return $str;
		}
	} elseif($charset == 'gbk') {
		if ($length == 0 || $length >= $strlength) {
			return $str;
		}
		while ($i <= $strlength) {
			if (ord($str{$i}) > 0xa0) { 
				$l += 2; $i += 2;
			} else {
				$l++; $i++;
			}

			if ($l >= $length) { 
				$newstr = substr($str, 0, $i);
				break;
			}
		}
	}

	if ($append && $str != $newstr) {
		$newstr .= '..';
	}

	return $newstr;
}

/**
 * 更新配置文件
 *
 * @param	string		$config_content		文件路径
 * @param	array		$update_arr			变量数组
 *
 * @return  string
 */
function update_config_file($config_content,$update_arr) {
	$content = $config_content;
	foreach($update_arr as $key => $value){
		$content=preg_replace("/(\\$$key) *= *([^;]+)/","\$1 = $value",$content);
	}
	return $content;
}
function db_create_in($item_list, $field_name = '')
{
    if (empty($item_list))
    {
        return $field_name . " IN ('') ";
    }
    else
    {
        if (!is_array($item_list))
        {
            $item_list = explode(',', $item_list);
        }
        $item_list = array_unique($item_list);
        $item_list_tmp = '';
        foreach ($item_list AS $item)
        {
            if ($item !== '')
            {
                $item_list_tmp .= $item_list_tmp ? ",'$item'" : "'$item'";
            }
        }
        if (empty($item_list_tmp))
        {
            return $field_name . " IN ('') ";
        }
        else
        {
            return $field_name . ' IN (' . $item_list_tmp . ') ';
        }
    }
}
function search_url(&$attr_picks, $attr_id = 0)
{
    $str = '';
    foreach ($attr_picks AS $pick_id)
    {
        if ($pick_id != $attr_id)
        {
            $str .= '&amp;attr['.$pick_id.']='.urlencode($_GET['attr'][$pick_id]);
        }
    }

    return $str;
}
?>