<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_goods.php");

//语言包引入
$m_langpackage=new moduleslp;

$goods_id = intval(get_args('goods_id'));
if(!$goods_id) {
	exit($m_langpackage->m_handle_err);
}


//定义文件表
$t_goods = $tablePreStr."goods";
$t_goods_attr = $tablePreStr."goods_attr";


//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();

// 判断 goods_id 是否为 本店铺下的产品id;
$goods_info = get_goods_info($dbo,$t_goods,'goods_id',$goods_id,$shop_id);
if(empty($goods_info)) {
	exit($m_langpackage->m_handle_err);
}

$have_attr = array();
$goods_attr = get_goods_attr($dbo,$t_goods_attr,$goods_id);
if($goods_attr) {
	foreach($goods_attr as $v) {
		$have_attr[$v['attr_id']] = $v['attr_values'];
	}
}


//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

// 更新goods 相关数据设置
$post = array(
	'type_id'	=> intval(get_args('type_id')),
);
$post['last_update_time'] = $ctime->long_time();
// 更新goods
update_goods_info($dbo,$t_goods,$post,$goods_id,$shop_id);

// 更新goods_attr 相关数据设置
$post_attr = array();
foreach($_POST as $key=>$value) {
	$attr_id = str_replace('attr_','',$key);
	if(is_numeric($attr_id) && !empty($value)) {
		$post_attr[$attr_id] = $value;
	}
}

$filterAttr = filterAttr($have_attr,$post_attr);
if($filterAttr['insert']) {
	insert_goods_attr($dbo,$t_goods_attr,$filterAttr['insert'],$goods_id);
}

if($filterAttr['update']) {
	update_goods_attr($dbo,$t_goods_attr,$filterAttr['update'],$goods_id);
}

if($filterAttr['delete']) {
	delete_goods_attr($dbo,$t_goods_attr,$filterAttr['delete'],$goods_id);
}

action_return(1,$m_langpackage->m_goodsupdate_success,'modules.php?app=goods_type&id='.$goods_id);

// 通过现有的属性与提交上来的属性进行比较
// 取得 需要更新，删除，添加的属性
function filterAttr($haveArray,$postArray) {
	$array = array();
	foreach($haveArray as $key=>$value) {
		if($postArray[$key]) {
			if($postArray[$key] != $value) {
				$array['update'][$key] = $postArray[$key];
			}
			unset($postArray[$key]);
		} else {
			$array['delete'][$key] = $value;
		}
	}
	$array['insert'] = $postArray;
	return $array;
}
?>