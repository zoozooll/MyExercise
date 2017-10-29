<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

/* post 数据处理 */
$parent_id = intval(get_args('value'));
$type = intval(get_args('type'));

if(!$parent_id) {
	exit();
}
if($type<0 || $type>2) {
	exit();
}

/*
//缓存区
$mc=new memcached($options);
$key='areas_parent_id_'.$album_id;
$key_mt='areas_parent_id_mt_'.$album_id;

//根据延时设置判断所缓存内容的更新状态
if(updateCacheStatus($mc,$key_mt,$cache_update_delay_time)){
	$mc->delete($key);
}

//得到缓存数据
$area_info = $mc->get($key);
if(!$area_info){
	// do...
	$mc->set($key,$area_info);
}
*/

require("foundation/module_areas.php");
//数据表定义区
$t_areas=$tablePreStr."areas";
//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$area_info = get_areas_list($dbo,$t_areas,$parent_id);
if(!$area_info) {
	exit();
}

$select = "";
if($type==0) {
	$select .= '<select name="province" onchange="areachanged(this.value,1);">';
	$select .= '<option value="0">'.$m_langpackage->m_select_province.'</option>';
} elseif($type==1) {
	$select .= '<select name="city" onchange="areachanged(this.value,2);">';
	$select .= '<option value="0">'.$m_langpackage->m_select_city.'</option>';
} elseif($type==2) {
	$select .= '<select name="district">';
	$select .= '<option value="0">'.$m_langpackage->m_select_district.'</option>';
}

foreach($area_info as $v) {
	$select .= '<option value="' . $v['area_id'] . '">' . $v['area_name'] . '</option>';
}

$select .= '</select>';

echo $select;
?>