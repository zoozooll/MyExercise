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

$pay_id = intval(get_args('pay_id'));

if(!$pay_id) {
	exit($m_langpackage->m_handle_err);
}

$sql = "delete from `$t_shop_payment` where shop_id='$shop_id' and pay_id='$pay_id' ";
if($dbo->exeUpdate($sql)) { 
	action_return(1,$m_langpackage->m_plug_unin_suc);
} else {
	action_return(0,$m_langpackage->m_plug_unin_lose);
}
?>