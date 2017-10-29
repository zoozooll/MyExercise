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
$t_user_favorite = $tablePreStr."user_favorite";

// 处理post变量
if(empty($_POST)){
	$favorite_id = intval(get_args('id'));
}else{
	$favorite=get_args('favorite');
	$favorite_id=implode(',',$favorite);
}
$sql = "delete from `$t_user_favorite` where user_id='$user_id' and favorite_id in ($favorite_id)";

//echo $sql;
if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_del_success);
} else {
	action_return(0,$m_langpackage->m_del_fail,'-1');
}
exit;
?>