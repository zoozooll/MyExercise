<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

/* post 数据处理 */
$flg = intval(get_args('value'));
require("foundation/module_areas.php");
//数据表定义区
$t_shop_info=$tablePreStr."shop_info";
$t_goods=$tablePreStr."goods";
//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

//改变商铺状态
$sql_shop = "update $t_shop_info SET open_flg=$flg where shop_id='$shop_id'";
$rs = $dbo->exeUpdate($sql_shop);
$_SESSION['shop_open']=$flg;
if($flg){
	echo '<a onclick="change_open_status(0)" style="color:red;">开启</a>';
	$sql_goods = "update $t_goods SET is_on_sale=0 where shop_id='$shop_id'";
}else{
	echo '<a onclick="change_open_status(1)" style="color:red;">关闭</a>';
	$sql_goods = "update $t_goods SET is_on_sale=1 where shop_id='$shop_id'";
}
$rs = $dbo->exeUpdate($sql_goods);
?>