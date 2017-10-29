<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$m_langpackage=new moduleslp;

if(!isset($user_privilege[1])) {
	set_sess_err_msg($m_langpackage->m_error_createshop);
	echo '<script language="JavaScript">location.href="modules.php?app=message"</script>';
	exit;
}

require("foundation/module_areas.php");

//数据表定义区
$t_areas = $tablePreStr."areas";

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

/* 初始化shopinfo */
$shop_info = array(
	'shop_name'		=> '',
	'shop_country'	=> 1,
	'shop_province'	=> 0,
	'shop_city'		=> 0,
	'shop_district'	=> 0,
	'shop_address'	=> '',
	'shop_images'	=> '',
	'shop_template'	=> 'default',
	'shop_intro'	=> '',
	'shop_management' => ''
);

$areas_info = get_areas_info($dbo,$t_areas);

$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/default_small.gif',
	'bigimgurl' => 'skin/default/images/default.gif',
	'tpltag' => 'default',
	'tplname' => '默认模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/green_small.gif',
	'bigimgurl' => 'skin/default/images/green.gif',
	'tpltag' => 'green',
	'tplname' => '绿色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/blue_small.gif',
	'bigimgurl' => 'skin/default/images/blue.gif',
	'tpltag' => 'blue',
	'tplname' => '蓝色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/red_small.gif',
	'bigimgurl' => 'skin/default/images/red.gif',
	'tpltag' => 'red',
	'tplname' => '红色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/purple_small.gif',
	'bigimgurl' => 'skin/default/images/purple.gif',
	'tpltag' => 'purple',
	'tplname' => '紫色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/gray_small.gif',
	'bigimgurl' => 'skin/default/images/gray.gif',
	'tpltag' => 'gray',
	'tplname' => '灰色模板'
);
?>