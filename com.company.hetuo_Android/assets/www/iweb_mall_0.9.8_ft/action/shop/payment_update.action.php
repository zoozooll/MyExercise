<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件


//语言包引入
$m_langpackage=new moduleslp;

$t_shop_payment = $tablePreStr."shop_payment";

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$post = array();
$post['pay_id'] = intval(get_args('pay_id'));
$post['pay_desc'] = big_check(get_args('pay_desc'));
$post['enabled'] = intval(get_args('enabled'));
$pay_config = array();
if(get_args('pay_config')){
	foreach(get_args('pay_config') as $key=>$value) {
		$pay_config[$key] = ($value);
	}
}
$post['pay_config'] = serialize($pay_config);

$shop_payment_id = intval(get_args('shop_payment_id'));
if($shop_payment_id>0) {
	$item_sql = get_update_item($post);
	$sql = "update `$t_shop_payment` set $item_sql where shop_id='$shop_id' and shop_payment_id='$shop_payment_id' ";
} else {
	$post['shop_id'] = $shop_id;
	$item_sql = get_insert_item($post);
	$sql = "insert `$t_shop_payment` $item_sql";
}

if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_adm_suc);
} else {
	action_return(0,$m_langpackage->m_adm_lose);
}
?>