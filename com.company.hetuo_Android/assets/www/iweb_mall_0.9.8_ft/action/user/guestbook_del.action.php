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
$t_shop_guestbook = $tablePreStr."shop_guestbook";

// 处理post变量

if(empty($_POST)){
	$gid = intval(get_args('id'));
}else{
	$gid=get_args('gid');
	$gid=implode(',',$gid);
}

$sql = "update `$t_shop_guestbook` set user_del_status=0 where gid in($gid) and user_id='$user_id'";
//echo $sql;
//exit;
if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_del_success);
} else {
	action_return(0,$m_langpackage->m_del_fail,'-1');
}
exit;
?>