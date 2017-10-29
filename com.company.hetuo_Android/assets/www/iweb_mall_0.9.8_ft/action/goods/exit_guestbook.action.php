<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* post 数据处理 */
$guestbook_id = intval(get_args('id'));
$read_status  = intval(get_args('sta'));
if(!$guestbook_id) {
	if (!$guestbook_id){
		exit();
	}
}
//数据表定义区
$t_shop_guestbook = $tablePreStr."shop_guestbook";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_shop_guestbook` set read_status ='$read_status' where gid = $guestbook_id ";
if($dbo->exeUpdate($sql)) {
	$url ="modules.php?app=shop_guestbook";
	action_return(1,'',$url);
}
exit;
?>