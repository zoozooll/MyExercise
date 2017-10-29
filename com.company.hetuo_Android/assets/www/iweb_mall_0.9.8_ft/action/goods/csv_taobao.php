<?php
	/*
	***********************************************
	*$ID:
	*$NAME:
	*$AUTHOR:E.T.Wei
	*DATE:Wed Mar 24 14:33:23 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
define('THUMB_WIDTH', 300);
define('THUMB_HEIGHT', 300);
define('THUMB_QUALITY', 85);

/* 淘宝助理CSV字段编号 */
define('FIELD_NUM',            41); // 字段总数
define('FIELD_GOODS_NAME',      0); // 商品名称
define('FIELD_PRICE',              7); // 商品价格
define('FIELD_STOCK',              9); // 库存
define('FIELD_IF_SHOW',        20); // 是否上架
define('FIELD_RECOMMENDED', 21); // 推荐
define('FIELD_ADD_TIME',       22); // 发布时间
define('FIELD_DESCRIPTION', 24); // 商品描述
define('FIELD_LAST_UPDATE', 31); // 更新时间
define('FIELD_GOODS_IMAGE', 35); // 商品图片
define('FIELD_GOODS_ATTR',  26); // 商品属性
define('FIELD_SALE_ATTR',      36); // 销售属性（规格）
define('FIELD_CID',                   1); // 商品类目cid
/* 品牌申请状态 */
define('BRAND_PASSED', 1);
define('BRAND_REFUSE', 0);

	//文件引入
	require("foundation/module_goods.php");
	//引入语言包
	$m_langpackage=new moduleslp;
	//数据表定义区
	$t_goods = $tablePreStr."goods";
	$img_table = $tablePreStr."goods_gallery";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("w",$dbServs);
	//取得上传的csv文件
	$csv_string = unicodeToUtf8(file_get_contents($_FILES['filename']['tmp_name']));
    //$csv_string = addslashes($csv_string); // 必须在转码后进行引号转义

	$arr=explode('"',$csv_string);

    $arr = _parse_taobao_csv($csv_string);


	foreach ($arr as $k=> $value){
		$str_arr = $value;
		//下载远程图片到本地
		//生成图片
		$errstr="";
		$info['shop_id']=$shop_id;
		$info['goods_name']=$str_arr[0];
//		$info['cat_id']=$str_arr[3];
//		$info['ucat_id']=$str_arr[4];
//		$info['brand_id']=$str_arr[5];
		$info['type_id']='1';
		$info['goods_intro']=str_replace(array("\\\"\\\"", "\"\""), array("\\\"", "\""), $str_arr[24]);
//		$info['goods_wholesale']=$str_arr[8];
		$info['goods_number']=$str_arr[9];
		$info['goods_price']=$str_arr[7];
//		$info['transport_price']=$str_arr[11];
//		$info['keyword']=$str_arr[12];
		$info['is_delete']='1';
		$info['is_best']='0';
		$info['is_new']='0';
		$info['is_hot']='0';
		$info['is_promote']='0';
		$info['is_on_sale']='1';
		$info['is_set_image']=$str_arr[19];
		$info['goods_thumb']=$goods_thumb;
//		$info['pv']=$str_arr[21];
//		$info['favpv']=$str_arr[22];
//		$info['sort_order']=$str_arr[23];
		$info['add_time']=date("Y-m-d H:i:s");
		$info['last_update_time']=date("Y-m-d H:i:s");
		$info['lock_flg']='0';
		$goods_id = $dbo->createbyarr($info,$t_goods);
//		$imginfo['goods_id']=$goods_id;
//		$imginfo['thumb_url']=$goods_thumb;
//		$imginfo['img_url']=$goods_img;
//		$img_id = $dbo->createbyarr($imginfo,$img_table);
		if (!$goods_id) {
			$errstr.= ($k+1).",";
		}
	}
	$errstr= substr($errstr,0,-1);
	if (empty($errstr)) {
		action_return(1,"导入成功！","modules.php?app=goods_list");
	}else{
		action_return(1,"第".$errstr."行导入失败！","modules.php?app=goods_list");
	}


	function unicodeToUtf8($str,$order="little")
    {
        $utf8string ="";
        $n=strlen($str);
        for ($i=0;$i<$n ;$i++ )
        {
            if ($order=="little")
            {
                $val = str_pad(dechex(ord($str[$i+1])), 2, 0, STR_PAD_LEFT) .
                       str_pad(dechex(ord($str[$i])),      2, 0, STR_PAD_LEFT);
            }
            else
            {
                $val = str_pad(dechex(ord($str[$i])),      2, 0, STR_PAD_LEFT) .
                       str_pad(dechex(ord($str[$i+1])), 2, 0, STR_PAD_LEFT);
            }
            $val = intval($val,16); // 由于上次的.连接，导致$val变为字符串，这里得转回来。
            $i++; // 两个字节表示一个unicode字符。
            $c = "";
            if($val < 0x7F)
            { // 0000-007F
                $c .= chr($val);
            }
            elseif($val < 0x800)
            { // 0080-07F0
                $c .= chr(0xC0 | ($val / 64));
                $c .= chr(0x80 | ($val % 64));
            }
            else
            { // 0800-FFFF
                $c .= chr(0xE0 | (($val / 64) / 64));
                $c .= chr(0x80 | (($val / 64) % 64));
                $c .= chr(0x80 | ($val % 64));
            }
            $utf8string .= $c;
        }
        /* 去除bom标记 才能使内置的iconv函数正确转换 */
        if (ord(substr($utf8string,0,1)) == 0xEF && ord(substr($utf8string,1,2)) == 0xBB && ord(substr($utf8string,2,1)) == 0xBF)
        {
            $utf8string = substr($utf8string,3);
        }
        return $utf8string;
    }
    /* 解析淘宝助理CSV数据 */
    function _parse_taobao_csv($csv_string)
    {
        // 32空格 34双引号  9制表符  10/n  13/r
        $pos = 0; // 当前的字符偏移量
        $status = 0; // 0标题未开始 1标题已开始
        $records = array(); // 记录集
        $field = 0; // 字段号
        $start_pos = 0; // 字段开始位置
        $field_status = 0; // 0未开始 1双引号字段开始 2无双引号字段开始
        $line =0; // 数据行号
        while($pos < strlen($csv_string))
        {
            $t = ord($csv_string[$pos]); // 每个UTF-8字符第一个字节单元的ascii码
            $next = ord($csv_string[$pos + 1]);
            $next2 = ord($csv_string[$pos + 2]);
            $next3 = ord($csv_string[$pos + 3]);

            $status == 0 && !in_array($t, array(32, 9, 10, 13)) && $status = 1;
            if ($status == 1)
            {
                if ($field_status == 0 && $t== 10 && $next == 34) // \n+引号
                {
                    $field_status = 1; // 字段未开始时 引号字段开始
                    $start_pos = $pos = $pos + 2;
                    continue;
                }
                if ($field_status == 0 && $t == 10 && $next != 34) // \n+无引号
                {
                    $field_status = 2; // 字段未开始时 无引号字段开始
                    $start_pos = $pos = $pos + 1;
                    continue;
                }

                if($field_status == 1 && $t == 34 && in_array($next, array(10, 13, 9))) // 引号+换行 或 引号+\t
                {
                    $records[$line][$field] = addslashes(substr($csv_string, $start_pos, $pos - $start_pos));
                    $field++;
                    if ($field == FIELD_NUM)
                    {
                        $line++;
                        $field = 0;
                        $field_status = 0;
                        continue;
                    }
                    if (($next == 10 && $next2 == 34) || ($next == 9 && $next2 == 34) || ($next == 13 && $next2 == 34))
                    {
                        $field_status = 1;
                        $start_pos = $pos = $pos + 3;
                        continue;
                    }
                    if (($next == 10 && $next2 != 34) || ($next == 9 && $next2 != 34) || ($next == 13 && $next2 != 34))
                    {
                        $field_status = 2;
                        $start_pos = $pos = $pos + 2;
                        continue;
                    }
                    if ($next == 13 && $next2 == 10 && $next3 == 34)
                    {
                        $field_status = 1;
                        $start_pos = $pos = $pos + 4;
                        continue;
                    }
                    if ($next == 13 && $next2 == 10 && $next3 != 34)
                    {
                        $field_status = 2;
                        $start_pos = $pos = $pos + 3;
                        continue;
                    }
                }
                if($field_status == 2 && in_array($t, array(10, 13, 9))) // 换行 或 \t
                {
                    $records[$line][$field] = addslashes(substr($csv_string, $start_pos, $pos - $start_pos));
                    $field++;
                    if ($field == FIELD_NUM)
                    {
                        $line++;
                        $field = 0;
                        $field_status = 0;
                        continue;
                    }
                    if (($t == 10 && $next == 34) || ($t == 9 && $next == 34) || ($t == 13 && $next == 34))
                    {
                        $field_status = 1;
                        $start_pos = $pos = $pos + 2;
                        continue;
                    }
                    if (($t == 10 && $next != 34) || ($t == 9 && $next != 34) || ($t == 13 && $next != 34))
                    {
                        $field_status = 2;
                        $start_pos = $pos = $pos + 1;
                        continue;
                    }
                    if ($t == 13 && $next == 10 && $next2 == 34)
                    {
                        $field_status = 1;
                        $start_pos = $pos = $pos + 3;
                        continue;
                    }
                    if ($t == 13 && $next == 10 && $next2 != 34)
                    {
                        $field_status = 2;
                        $start_pos = $pos = $pos + 2;
                        continue;
                    }
                }
            }

            if($t > 0 && $t <= 127) {
                $pos++;
            } elseif(192 <= $t && $t <= 223) {
                $pos += 2;
            } elseif(224 <= $t && $t <= 239) {
                $pos += 3;
            } elseif(240 <= $t && $t <= 247) {
                $pos += 4;
            } elseif(248 <= $t && $t <= 251) {
                $pos += 5;
            } elseif($t == 252 || $t == 253) {
                $pos += 6;
            } else {
                $pos++;
            }
        }

        return $records;
    }

?>