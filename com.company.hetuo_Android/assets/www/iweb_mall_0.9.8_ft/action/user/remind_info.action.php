<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_users.php");

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$updsubmit=short_check(get_args('updsubmit'));
$deletesubmit=short_check(get_args('deletesubmit'));
$searchkey=get_args('searchkey');
if(is_array($searchkey)){
	$rinfo_id=implode(',',$searchkey);
}else{
	$rinfo_id=$searchkey;
}

//定义文件表
$t_remind_info = $tablePreStr."remind_info";

if($updsubmit){
	$sql="update $t_remind_info set isread=1 where user_id=$user_id and rinfo_id in($rinfo_id)";
}else if($deletesubmit){
	$sql="delete from $t_remind_info where user_id=$user_id and rinfo_id in($rinfo_id)";
}
if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_upd_suc,'-1');
} else {
	action_return(0,$m_langpackage->m_upd_lose,'-1');
}
?>