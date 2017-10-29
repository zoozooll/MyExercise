<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件


//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_shop_inquiry = $tablePreStr."shop_inquiry";

// 处理post变量
if(empty($_POST)){
	$iid = intval(get_args('id'));
}else{
	$iid=get_args('iid');
	$iid=implode(',',$iid);
}

$sql = "update `$t_shop_inquiry` set shop_del_status=0 where iid in($iid) and shop_id='$shop_id'";
//echo $sql;
if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_del_success);
} else {
	action_return(0,$m_langpackage->m_del_fail,'-1');
}
exit;
?>