<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

/* post 数据处理 */
$gid=intval(get_args('id'));
$reply = short_check(get_args('reply'));

//数据表定义区
$t_shop_guestbook = $tablePreStr."shop_guestbook";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_shop_guestbook` set reply='$reply' where gid=$gid";

if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_add_success,'-1');
} else {
	action_return(0,$m_langpackage->m_add_fail,'-1');
}
?>