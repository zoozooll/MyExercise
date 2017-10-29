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
$newarray = array();
$arr = array('alipay_account',"alipay_truename","bank_truename","bank_name","bank_account");

foreach($_POST as $key=>$value) {
	if($key=='pay_id') {
		$post[$key] = intval($value);
	} elseif($key=='shop_payment_id'){
		$$key = intval($value);
	} elseif(in_array($key,$arr)) {
		$newarray[$key] = short_check($value);
	}
}

$post['pay_config'] = serialize($newarray);

if($shop_payment_id>0) {
	$item_sql = get_update_item($post);
	$sql = "update `$t_shop_payment` set $item_sql where shop_id='$shop_id' and shop_payment_id='$shop_payment_id' ";
} else {
	$post['shop_id'] = $shop_id;
	$item_sql = get_insert_item($post);
	$sql = "insert `$t_shop_payment` $item_sql";
}

if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_edit_success);
} else {
	action_return(0,$m_langpackage->m_edit_fail);
}
?>