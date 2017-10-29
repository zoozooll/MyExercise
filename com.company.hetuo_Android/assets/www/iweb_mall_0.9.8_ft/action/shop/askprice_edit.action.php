<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

//定义文件表
$t_shop_inquiry = $tablePreStr."shop_inquiry";

// 处理post变量
$askprice_iid = intval(get_args('id'));
$read_status  = intval(get_args('sta'));
if(!$askprice_iid) {
	if (!$askprice_iid){
		exit();
	}
}
//数据库写操作
dbtarget('r',$dbServs);
$dbo=new dbex();

$sql = "update `$t_shop_inquiry` set read_status='$read_status' where iid ='$askprice_iid'";
if($dbo->exeUpdate($sql)) {
	$url ="modules.php?app=shop_askprice";
	action_return(1,'',$url);
}
exit;
?>