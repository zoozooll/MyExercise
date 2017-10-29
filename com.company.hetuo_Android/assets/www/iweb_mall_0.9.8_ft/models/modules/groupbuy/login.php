<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_groupbuy.php");

//引入语言包
$m_langpackage=new moduleslp;

$group_id = intval(get_args('id'));

//数据表定义区
$t_groupbuy = $tablePreStr."groupbuy";
$t_groupbuy_log = $tablePreStr."groupbuy_log";


//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$group_row=get_groupbuy_info($dbo,"*",$t_groupbuy,$group_id);

$login_rs=get_groupbuylog_list($dbo,"*",$t_groupbuy_log,$group_id)

?>