<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_guestbook = $tablePreStr."shop_guestbook";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_shop_guestbook` where shop_id='$shop_id' and shop_del_status=1";

$sql .= " order by add_time desc";

$result = $dbo->fetch_page($sql,13);
?>