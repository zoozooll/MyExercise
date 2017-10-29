<?php
	/*
	***********************************************
	*$ID:get_transport_price
	*$NAME:get_transport_price
	*$AUTHOR:E.T.Wei
	*DATE:Tue Apr 06 14:56:31 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	/* post 数据处理 */
	$goods_id = intval(get_args('goods_id'));
	$area_id = intval(get_args("area_id"));
	//数据表定义区
	$t_goods=$tablePreStr."goods";
	$t_transport_template = $tablePreStr."goods_transport";
	$t_areas = $tablePreStr."areas";
	//定义写操作
	dbtarget('r',$dbServs);
	$dbo=new dbex;
	$goodsinfo=$dbo->getRow("SELECT is_transport_template,transport_price,transport_template_id,shop_id FROM $t_goods WHERE goods_id='$goods_id'");
	$sql = "SELECT content FROM $t_transport_template WHERE id=(SELECT transport_template_id FROM $t_goods WHERE goods_id='$goods_id')";
	//echo $sql;
	$arr = $dbo->getRow($sql);
	$str="";
	$default_transport_price="";
	if (isset($arr['content'])) {
		$transport_arr = unserialize($arr['content']);
		if (isset($transport_arr['ems'][$area_id])) {
			$str.="EMS:￥".$transport_arr['ems'][$area_id]['frist']."元";
		}elseif (isset($transport_arr['ex'][$area_id])) {
			$str.="快递:￥".$transport_arr['ex'][$area_id]['frist']."元";
		}elseif (isset($transport_arr['pst'][$area_id])) {
			$str.="平邮:￥".$transport_arr['pst'][$area_id]['frist']."元";
		}else{
			if ($goodsinfo['is_transport_template']) {
				$sql = "SELECT * FROM $t_transport_template WHERE id='{$goodsinfo['transport_template_id']}' AND shop_id='{$goodsinfo['shop_id']}'";
				$transport_template_info = $dbo->getRow($sql);
				$transport_cont = unserialize($transport_template_info['content']);
				if (isset($transport_cont['ex'])) {
					$default_transport_price="￥".$transport_cont['ex']['frist']."元";
				}elseif (isset($transport_cont['pst'])){
					$default_transport_price="￥".$transport_cont['pst']['frist']."元";
				}elseif (isset($transport_cont['ems'])){
					$default_transport_price="￥".$transport_cont['ems']['frist']."元";
				}else{
					$default_transport_price="￥".$goodsinfo['transport_price']."元";
				}
			}else{
				$transport_template_info=array();
				$default_transport_price="￥".$goodsinfo['transport_price']."元";
			}
		}
		$str.=$default_transport_price;
	}else{
		$transport_template_info=array();
		$default_transport_price=$goodsinfo['transport_price']."元";
		$str.=$default_transport_price;
	}
	echo $str;
	
?>