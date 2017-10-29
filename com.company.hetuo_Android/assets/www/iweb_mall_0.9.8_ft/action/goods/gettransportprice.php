<?php
	/*
	***********************************************
	*$ID:gettransportprice
	*$NAME:gettransportprice
	*$AUTHOR:E.T.Wei
	*DATE:Thu Apr 08 15:51:34 CST 2010
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
	$t_goods = $tablePreStr."goods";
	$t_transport_template = $tablePreStr."goods_transport";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("r",$dbServs);
	$goods_id = intval(get_args("goods_id"));
	$area_id = intval(get_args("area_id"));
	$type= short_check(get_args("type"));
	$goods_num = intval(get_args("goods_num"));

	//根据商品ID取得运费
	$sql = "SELECT is_transport_template,transport_template_id,transport_price FROM $t_goods WHERE goods_id='$goods_id'";
	$goods_info=$dbo->getRow($sql);
	if ($goods_info['is_transport_template']&&$goods_info['transport_template_id']) {
		$sql="SELECT * FROM $t_transport_template WHERE id='{$goods_info['transport_template_id']}'";
		$transport_template_info = $dbo->getRow($sql);
		$area_cont = unserialize($transport_template_info['content']);
		if (isset($area_cont[$type][$area_id])) {
			$transport_price = $area_cont[$type][$area_id]['frist']+($goods_num-1)*$area_cont[$type][$area_id]['second'];
		}else{
			$transport_price= $area_cont[$type]['frist']+($goods_num-1)*$area_cont[$type]['second'];
		}
	}else{
		$transport_price=$goods_info['transport_price']*$goods_num;
	}
	echo $transport_price;
?>