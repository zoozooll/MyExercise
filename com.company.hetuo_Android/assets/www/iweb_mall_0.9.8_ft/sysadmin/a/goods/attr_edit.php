<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_attr.php");
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("attr_edit");
if(!$right){
	exit("-2");
}


/* post 数据处理 */
$post['cat_id'] = intval(get_args('cat_id'));
$post['attr_name'] = short_check(get_args('attr_name'));
$post['input_type'] = intval(get_args('input_type'));
$post['attr_values'] = big_check(get_args('attr_values'));
$post['sort_order'] = intval(get_args('sort_order'));
$attr_id = intval(get_args('attr_id'));

if(!$post['cat_id']) {exit("-1");}

if(empty($post['attr_name'])) { exit("-1"); }

//数据表定义区
$t_attribute = $tablePreStr."attribute";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
if($attr_id>0) {
	if(update_attr_info($dbo,$t_attribute,$post,$attr_id)) {
		echo $attr_id;
	} else {
		echo "-1";
	}
} else {
	if($new_attr_id = insert_attr_info($dbo,$t_attribute,$post)) {
		echo $new_attr_id;
	} else {
		echo "-1";
	}
}
?>