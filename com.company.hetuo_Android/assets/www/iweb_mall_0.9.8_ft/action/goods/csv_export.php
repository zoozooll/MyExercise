<?php
	/*
	***********************************************
	*$ID:csv_export
	*$NAME:csv_export
	*$AUTHOR:E.T.Wei
	*DATE:Wed Mar 24 10:31:49 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	require("foundation/module_goods.php");
	//引入语言包
	$m_langpackage=new moduleslp;
	//数据表定义区
	$goods_table = $tablePreStr."goods";
	$img_table = $tablePreStr."goods_gallery";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("r",$dbServs);
	$shop_id = intval(get_args('shop_id'));
	//取得商品列表
	$goods_ids = get_args("goods_id");	
	$goods_id_str = "";
	foreach ($goods_ids as $value){
		$goods_id_str.="$value,";
	}
	$chast = get_args("chast");
	$file_name = get_args("filename");
	if (empty($file_name)) {
		$file_name=date("YmdHis");
	}
	$file_name .=".csv";
	$file_name =iconv("utf-8",$chast,$file_name);
	$goods_id_str = substr($goods_id_str,0,-1);
	$sql = "SELECT g.*,i.goods_id as gid,i.img_original FROM $goods_table AS g,$img_table AS i WHERE g.goods_id IN ($goods_id_str) AND i.goods_id=g.goods_id GROUP BY g.goods_id DESC";
	$goods_list = $dbo->getRs($sql);
	header( "Cache-Control: public" );
    header( "Pragma: public" );
	header("Content-type:application/vnd.ms-excel");
    header("Content-Disposition:attachment;filename=".$file_name);
    header('Content-Type:APPLICATION/OCTET-STREAM');
	ob_start(); 
	$header_str = iconv("utf-8",$chast,"商品ID,店铺ID,商品名称,分类ID,自定义分类ID,品牌ID,属性类型ID,商品描述,批发说明,商品库存,商品价格,商品运费,关键字,是否删除,是否精品,是否新品,是否热销,是否特价,是否上架,是否设置图片,商品缩略图,关注度,被收藏次数,排序,添加时间,最后更新时间,是否锁定,原始图片 \n");
    $file_str="";
	foreach ($goods_list as $value){
		$goods_name_pos = strpos($value['goods_name'],",");//商品名称
		$goods_price_pos = strpos($value['goods_price'],",");//商品价格
		$goods_intro_pos = strpos($value['goods_intro'],",");//商品说明
		$goods_goods_wholesale_pos = strpos($value['goods_wholesale'],",");//批发说明
		$goods_transport_price_pos = strpos($value['transport_price'],",");//运费
		$goods_keyword_pos = strpos($value['keyword'],",");//关键词
		$goods_thumb_pos = strpos($value['goods_thumb'],",");//缩略图
		$goods_img_original_pos = strpos("{$SYSINFO['web']}".$value['img_original'],",");//原始图
		$value['goods_name']=str_replace("/r","","{$value['goods_name']}");
		$value['goods_name']=str_replace('"','""',"{$value['goods_name']}");
		$value['goods_name']=str_replace("/n","","{$value['goods_name']}");
		$value['goods_price']=str_replace("/r","","{$value['goods_price']}");
		$value['goods_price']=str_replace("/n","","{$value['goods_price']}");
//		$value['goods_price']=str_replace('"','""',"{$value['goods_price']}");
		$value['goods_intro']=str_replace("/r","","{$value['goods_intro']}");
		$value['goods_intro']=str_replace("<br>","&lt;br&gt;","{$value['goods_intro']}");
		$value['goods_intro']=str_replace("<br/>","&lt;br/&gt;","{$value['goods_intro']}");
		$value['goods_intro']=str_replace("/n","","{$value['goods_intro']}");
		$value['goods_intro']=str_replace('"','""',"{$value['goods_intro']}");
		$value['goods_wholesale']=str_replace("/r","","{$value['goods_wholesale']}");
		$value['goods_wholesale']=str_replace("/n","","{$value['goods_wholesale']}");
		$value['goods_wholesale']=str_replace('"','""',"{$value['goods_wholesale']}");
		$value['googoods_wholesales_name']=str_replace("/n","","{$value['goods_wholesale']}");
		$value['googoods_wholesales_name']=str_replace("/r","","{$value['goods_wholesale']}");
		$value['googoods_wholesales_name']=str_replace('"','""',"{$value['goods_wholesale']}");
		$value['transport_price']=str_replace("/r","","{$value['transport_price']}");
		$value['transport_price']=str_replace("/n","","{$value['transport_price']}");
		$value['transport_price']=str_replace('"','""',"{$value['transport_price']}");
		$value['keyword']=str_replace("/r","","{$value['keyword']}");
		$value['keyword']=str_replace('"','""',"{$value['keyword']}");
		$value['keyword']=str_replace("/n","","{$value['keyword']}");
	    if ($goods_name_pos === false) {
			$goods_name = iconv("utf-8",$chast,$value['goods_name']);
	    }else{
	    	$goods_name ="\"".iconv("utf-8",$chast,$value['goods_name'])."\"";
	    }//商品名称
	    if ($goods_price_pos === false) {
			
	    }else{
	    	$value['goods_price'] ="\"".iconv("utf-8",$chast,$value['goods_price'])."\"";
	    }//商品价格
	    if ($goods_intro_pos === false) {
			$value['goods_intro']=iconv("utf-8",$chast,$value['goods_intro']);
	    }else{
	    	$value['goods_intro'] ='"'.iconv("utf-8",$chast,$value['goods_intro']).'"';
	    }//商品说明
	    if ($goods_goods_wholesale_pos === false) {
			
	    }else{
	    	$value['goods_wholesale'] ="\"".iconv("utf-8",$chast,$value['goods_wholesale'])."\"";
	    }//批发说明
	    if ($goods_keyword_pos === false) {
			
	    }else{
	    	$value['goods_price'] ="\"".iconv("utf-8",$chast,$value['goods_price'])."\"";
	    }//关键词
	    if ($goods_thumb_pos === false) {
			
	    }else{
	    	$value['goods_thumb'] ="\"".iconv("utf-8",$chast,$value['goods_thumb'])."\"";
	    }//缩略图
	    if ($goods_img_original_pos === false) {
			
	    }else{
	    	$value['img_original'] ="\"".iconv("utf-8",$chast,$value['img_original'])."\"";
	    }
		$file_str .=$value['goods_id'].",".$value['shop_id'].",".$goods_name.",".$value['cat_id'].",".$value['ucat_id'].",".$value['brand_id'].",".$value['type_id'].",".$value['goods_intro'].",".iconv("utf-8",$chast,$value['goods_wholesale']).",".$value['goods_number'].",".$value['goods_price'].",".$value['transport_price'].",".iconv("utf-8",$chast,$value['keyword']).",".$value['is_delete'].",".$value['is_best'].",".$value['is_new'].",".$value['is_hot'].",".$value['is_promote'].",".$value['is_on_sale'].",".$value['is_set_image'].",".$value['goods_thumb'].",".$value['pv'].",".$value['favpv'].",".$value['sort_order'].",".$value['add_time'].",".$value['last_update_time'].",".$value['lock_flg'].","."{$SYSINFO['web']}".$value['img_original']."\n";
	}
	ob_end_clean();
	echo $header_str;
	echo $file_str;
	
?>