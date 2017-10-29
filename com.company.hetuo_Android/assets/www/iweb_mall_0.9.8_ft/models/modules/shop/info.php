<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_areas.php");
require("foundation/module_shop.php");
require("foundation/module_users.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_areas = $tablePreStr."areas";
$t_shop_info = $tablePreStr."shop_info";
$t_users = $tablePreStr."users";
$t_privilege = $tablePreStr."privilege";
$t_user_rank = $tablePreStr."user_rank";

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

$shop_info = get_shop_info($dbo,$t_shop_info,$shop_id);
$areas_info = get_areas_info($dbo,$t_areas);
$user_info = get_user_info($dbo,$t_users,$shop_id);

$rank_info = get_userrank_info($dbo,$t_user_rank,$user_info['rank_id']);
$privilege = unserialize($rank_info['privilege']);
$flag ='';
foreach ($privilege as $key =>$vlaue){
	if ($key =='9'){
		$flag ='1';
	}
}
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