<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_honor.php");

/* post 数据处理 */
$id = intval(get_args('id'));

if(!$id) {
	exit();
}

//数据表定义区
$t_shop_honor = $tablePreStr."shop_honor";

//定义写操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$honor_info = get_honor_info($dbo,$t_shop_honor,$shop_id,$id);
if(empty($honor_info)) { exit(); }

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "delete from  `$t_shop_honor` where shop_id='$shop_id' and honor_id='$id'";
if($dbo->exeUpdate($sql)) {
	@unlink($honor_info['honor_img']);
	@unlink($honor_info['honor_thumb']);
	@unlink($honor_info['honor_original']);
	echo '1';
}
?>