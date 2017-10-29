<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_category.php");
require("foundation/module_goods.php");
require("foundation/module_attr.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_category = $tablePreStr."category";
$t_attribute = $tablePreStr."attribute";
$t_shop_category = $tablePreStr."shop_category";
$t_goods_attr = $tablePreStr."goods_attr";
$t_goods_transport = $tablePreStr."goods_transport";
$t_shop_payment = $tablePreStr."shop_payment";
$t_payment = $tablePreStr."payment";

$goods_id = intval(get_args('id'));

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$goods_info = get_goods_info($dbo,$t_goods,'*',$goods_id,$shop_id);
if(empty($goods_info)) {
	exit("非法操作！");
}
$transport_template_list = get_transport_template_list($dbo,$t_goods_transport);
$category_info = get_category_info($dbo,$t_category);
foreach($category_info as $value) {
	if($goods_info['cat_id']==$value['cat_id']) {
		$select_category_name = $value['cat_name'];
		break;
	}
}

$shop_category = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$html_shop_category = html_format_shop_category($shop_category,$goods_info['ucat_id']);

$attribute_info = array();
if($goods_info['cat_id']) {
	$attribute_info = get_attribute_info($dbo,$t_attribute,$goods_info['cat_id']);
}
$js_attribute_info = json_encode($attribute_info);

$goods_attr = get_goods_attr($dbo,$t_goods_attr,$goods_id);
/* 判断支付方式 */
$sql = "SELECT b.pay_id,b.pay_code FROM $t_shop_payment AS a, $t_payment AS b WHERE a.pay_id=b.pay_id AND a.shop_id=$shop_id AND a.enabled=1";
$isset_payment = $dbo->getRs($sql);
?>