<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_attr.php");

/* post 数据处理 */
$cat_id = intval(get_args('cat_id'));
$parent_id = intval(get_args('parent_id'));

if(!$cat_id || !$parent_id) {exit("-1");}

//数据表定义区
$t_attribute = $tablePreStr."attribute";

$dbo=new dbex;

// 增加cat_id与parent_id是否为父子关系产品类判断
// ....

//定义写操作
dbtarget('r',$dbServs);
$sql = "SELECT cat_id,attr_name,input_type,attr_values,sort_order FROM $t_attribute WHERE cat_id in ($parent_id,$cat_id)";

$result = $dbo->getRs($sql);
$add_attr = array();
if($result) {
	$cat_attr = array();
	$parent_attr = array();
	$cat_name_array = array();
	foreach($result as $value) {
		if($value['cat_id']==$cat_id) {
			$cat_attr[] = $value;
			$cat_name_array[] = $value['attr_name'];
		} elseif($value['cat_id']==$parent_id) {
			$parent_attr[] = $value;
		}
	}
	unset($result);
	if($cat_attr && $parent_attr) {
		foreach($parent_attr as $value) {
			if(!in_array($value['attr_name'],$cat_name_array)) {
				$add_attr[] = $value;
			}
		}
	} else {
		$add_attr = $parent_attr;
	}
	unset($cat_attr,$parent_attr,$cat_name_array);

	if($add_attr) {
		dbtarget('w',$dbServs);
		$return_array = array();
		$i = 0;
		foreach($add_attr as $key=>$value) {
			$post['cat_id'] = $cat_id;
			$post['attr_name'] = $value['attr_name'];
			$post['input_type'] = $value['input_type'];
			$post['attr_values'] = $value['attr_values'];
			$post['sort_order'] = $value['sort_order'];
			if($new_attr_id = insert_attr_info($dbo,$t_attribute,$post)) {
				$return_array[$key]['attr_id'] = $new_attr_id;
				$return_array[$key]['cat_id'] = $cat_id;
				$return_array[$key]['attr_name'] = $value['attr_name'];
				$return_array[$key]['input_type'] = $value['input_type'];
				$return_array[$key]['attr_values'] = $value['attr_values'];
				$return_array[$key]['sort_order'] = $value['sort_order'];
				$i++;
			}
		}
		echo json_encode($return_array);
	} else {
		exit("-2");
	}
} else {
	exit("-2");
}
?>