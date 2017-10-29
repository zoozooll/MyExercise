<?php
!function_exists('adminmsg') && exit('Forbidden');

if($adskin != 2){
	require_once(R_P.'admin/windowindex.php');
	exit;
}
require GetLang('left');
$headdb = array();

$guides = $titles = $dfcate = $gd_diy = '';
if (If_manager) {
	//$nav_left['config']['settings']['option']['settings']['all'] = array(getLangInfo('other','admin_settingsall'),$admin_file.'?adminjob=settings&admintype=all');
	$nav_left = array_merge(array('initiator' => array('initiator' => array('name' => $nav_manager['name'],'option' => $nav_manager['option']))),$nav_left);
}
unset($nav_manager);
$diyoptions = $db_diy ? explode(',',$db_diy) : array('setforum','setuser','level','postcache','article');

foreach ($nav_left as $cate => $left) {
	$output1 = '';
	foreach ($left as $title => $value) {
		$output2 = '';
		menuDeal($value['option']);
		if (!$output2) continue;
		$output2 = substr($output2,1);
		$output1.= ",'$title' : {{$output2}}";
		$titles .= ",'$title' : '$value[name]'";
	}
	if (!$output1) continue;
	$output1 = substr($output1,1);
	$guides .= ",\r\n\t'$cate' : {{$output1}}";
	$headdb[$cate] = $nav_head[$cate];
	!$dfcate && $dfcate = $cate;
}
if ($gd_diy) {
	$dfcate = 'common';
	$gd_diy = substr($gd_diy,1);
	$guides .= ",\r\n\t'common' : {'diy' : {{$gd_diy}}}";
	$titles .= ",'diy' : '".getLangInfo('other','admin_diy')."'";
}
$guides = substr($guides,1)."\n";
$titles = substr($titles,1);
include PrintEot('index');exit;

function addMenu($type,$key,$option){
	if (!is_array($option)) return false;
	$type .= ",'$key' : ['$option[0]','$option[1]']";
	return $type;
}
function addSecondMenu($key,$option){
	global $output2;
	$output2	= addMenu($output2,$key,$option);
}
function diyOptionCheck($key,$k=false){
	global $diyoptions;
	if ($k != false) $key .= '_'.$k;
	if (in_array($key,$diyoptions)) return true;
	return false;
}
function addDiyOpion($key,$option,$k=false){
	global $gd_diy;
	if (diyOptionCheck($key,$k)) {
		$gd_diy = addMenu($gd_diy,getRealKey($key,$k),$option);
	}
}
/*
 * 二级菜单及自定义菜单的处理
 */
function secondAndDiy($key,$option,$k=false){
	if ($k) {
		$admincheck = adminRightCheck($k);
	} else {
		$admincheck = adminRightCheck($key);
	}
	if ($admincheck) {
		addSecondMenu(getRealKey($key,$k),$option);
		addDiyOpion($key,$option,$k);
	}
}
/*
 * 处理菜单数据
 */
function menuDeal($options){
	foreach ($options as $key => $option) {
		if (!isset($option[0]) || is_array($option[0])) {
			foreach ($option as $k => $v) {
				secondAndDiy($key,$v,$k);
			}
			continue;
		}
		secondAndDiy($key,$option);
	}
}
function getRealKey($key,$k){
	return $k == false ? $key : $k;
}
?>