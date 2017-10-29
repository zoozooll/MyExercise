<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

//定义文件表
$t_shop_category = $tablePreStr."shop_category";

$id = intval(get_args('id'));
$ids = $id;
//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();
$sql = "select * from `$t_shop_category` where parent_id='$id' and shop_id='$shop_id'";
$rows = $dbo->getRs($sql);
if($rows) {
	foreach($rows as $value) {
		$ids .= ','.$value['shop_cat_id'];
	}
}

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$sql = "delete from `$t_shop_category` where shop_cat_id in ($ids) and shop_id='$shop_id'";

if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_del_success);
} else {
	action_return(0,$m_langpackage->m_del_fail,'-1');
}
exit;
?>