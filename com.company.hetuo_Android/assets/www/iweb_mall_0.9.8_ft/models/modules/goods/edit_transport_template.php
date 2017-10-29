<?php
	/*
	***********************************************
	*$ID:eidt_transport_template
	*$NAME:eidt_transport_template
	*$AUTHOR:E.T.Wei
	*DATE:Sat Apr 03 10:40:43 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	require("foundation/module_goods.php");
	require("foundation/module_areas.php");
	//引入语言包
	$m_langpackage = new moduleslp;
	//数据表定义区
	$t_areas = $tablePreStr."areas";
	$t_transport_template = $tablePreStr."goods_transport";
	//读写分类定义方法
	$dbo = new dbex;
	dbtarget("r",$dbServs);
	$id = intval(get_args("id"));
	$sql = "SELECT * FROM $t_transport_template WHERE id='$id'";
	$transport_template_info = $dbo->getRow($sql);
	$transport_type = unserialize($transport_template_info['content']);
	foreach ($transport_type as $value){
		$transport_type[]=$value;
	}
	$arr = unserialize($transport_template_info['content']);
	//ems
	$emsstr='';
	if (isset($arr['ems'])&&count($arr['ems'])>1) {
		$emsarea=array();
		foreach ($arr['ems'] as $key=>$value){
			if(is_numeric($key)){
				$emsarea["{$value['frist']},{$value['second']}"][]=$key;
			}
		}
		$idlist="";
		$area_name_list="";
		$num=0;
		foreach ($emsarea as $key=>$value){
			foreach ($value as $k=>$v){
				$idlist.=$v.",";
				$sql="SELECT area_name FROM $t_areas WHERE area_id='$v'";
				$area_info = $dbo->getRow($sql);
				$area_name_list.=$area_info['area_name'].",";
			}
			$num++;
			$key_str = explode(",",$key);
			$emsstr.="&nbsp;&nbsp;&nbsp;&nbsp;<li>至<input type='text' name='ord_item_wordems[]' value='$area_name_list' onclick=\"eidtarea('ems','$idlist','$num')\" id='item$num'  />
				<input type='hidden' id='itemvalue$num' name='ord_item_destems[]' value='$idlist' />的";
			$emsstr.=$m_langpackage->m_transport_template_frist;
			$emsstr.="<input type='text' name='ord_area_fristems[]' value='{$key_str[0]}' />".$m_langpackage->m_transport_template_second.
				"<input type='text' name='ord_area_secondems[]' value='{$key_str[1]}' /></li>";
			$idlist="";
			$area_name_list="";
		}
	}
	//pst
	$pststr='';
	if(isset($arr['pst'])&&count($arr['pst'])>1){
		$pstarea=array();
		foreach ($arr['pst'] as $key=>$value){
			if(is_numeric($key)){
				$pstarea["{$value['frist']},{$value['second']}"][]=$key;
			}
		}
		$idlist="";
		$area_name_list="";
		$num=0;
		foreach ($pstarea as $key=>$value){
			foreach ($value as $k=>$v){
				$idlist.=$v.",";
				$sql="SELECT area_name FROM $t_areas WHERE area_id='$v'";
				$area_info = $dbo->getRow($sql);
				$area_name_list.=$area_info['area_name'].",";
			}
			$num++;
			$key_str = explode(",",$key);
			$pststr.="&nbsp;&nbsp;&nbsp;&nbsp;<li>至<input type='text' name='ord_item_wordpst[]' value='$area_name_list' onclick=\"eidtarea('pst','$idlist','$num')\" id='item$num'  />
				<input type='hidden' id='itemvalue$num' name='ord_item_destpst[]' value='$idlist' />的";
			$pststr.=$m_langpackage->m_transport_template_frist;
			$pststr.="<input type='text' name='ord_area_fristpst[]' value='{$key_str[0]}' />".$m_langpackage->m_transport_template_second.
				"<input type='text' name='ord_area_secondpst[]' value='{$key_str[1]}' /></li>";
			$idlist="";
			$area_name_list="";
		}
	}
	//ex
	$exstr='';
	if(isset($arr['ex'])&&count($arr['ex'])>1){
		$exarea=array();
		foreach ($arr['ex'] as $key=>$value){
			if(is_numeric($key)){
				$exarea["{$value['frist']},{$value['second']}"][]=$key;
			}
		}
		$idlist="";
		$area_name_list="";
		$num=0;
		foreach ($exarea as $key=>$value){
			foreach ($value as $k=>$v){
				$idlist.=$v.",";
				$sql="SELECT area_name FROM $t_areas WHERE area_id='$v'";
				$area_info = $dbo->getRow($sql);
				$area_name_list.=$area_info['area_name'].",";
			}
			$num++;
			$key_str = explode(",",$key);
			$exstr.="&nbsp;&nbsp;&nbsp;&nbsp;<li>至<input type='text' name='ord_item_wordex[]' value='$area_name_list' onclick=\"eidtarea('ex','$idlist','$num')\" id='item$num'  />
				<input type='hidden' id='itemvalue$num' name='ord_item_destex[]' value='$idlist' />的";
			$exstr.=$m_langpackage->m_transport_template_frist;
			$exstr.="<input type='text' name='ord_area_fristex[]' value='{$key_str[0]}' />".$m_langpackage->m_transport_template_second.
				"<input type='text' name='ord_area_secondex[]' value='{$key_str[1]}' /></li>";
			$idlist="";
			$area_name_list="";
		}
	}
	//生成json
	$emsobj="''";
	if (isset($transport_type['ems'])) {
		$emsobj = json_encode($transport_type['ems']);
	}
	$exobj="''";
	if (isset($transport_type['ex'])) {
		$exobj = json_encode($transport_type['ex']);
	}
	$pstobj="''";
	if (isset($transport_type['pst'])) {
		$pstobj = json_encode($transport_type['pst']);
	}
	$area_list = get_area_list_bytype($dbo,$t_areas,1);
?>