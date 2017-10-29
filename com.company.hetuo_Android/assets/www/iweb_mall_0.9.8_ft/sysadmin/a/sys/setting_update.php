<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_news.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;
//数据表定义区
$t_settings = $tablePreStr."settings";
$t_admin_log = $tablePreStr."admin_log";

$ctime = new time_class;

$right=check_rights("site_set_update");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/*数据处理 */
dbtarget('r',$dbServs);
$dbo=new dbex;

$sql = "select * from `$t_settings`";
$result = $dbo->getRs($sql);
if($result) {
	foreach($result as $v) {
		$SYSINFO[$v['variable']] = $v['value'];
	}
}
/* logo图片上传 */
$upload = new upload('jpg|gif|png',1024,'logo_images');
$upload->set_dir("../uploadfiles/logo/","{y}/{m}/{d}");
$file = $upload->execute();
/* logo图片上传end */
$sysinfo = get_args('sysinfo');
if ($file){
	if ($file[0]['flag'] == 1){
		$web_dir = $file[0]['dir'].$file[0]['name'];
		$web_dir = substr($web_dir,3);
		$sysinfo['sys_logo'] = $web_dir;
	}else{
		$sysinfo['sys_logo'] =$SYSINFO['sys_logo'];
	}
}else{
	$sysinfo['sys_logo'] =$SYSINFO['sys_logo'];
}
$sysinfo['seller_page']=intval($sysinfo['seller_page']);
if($sysinfo['seller_page']==0){
	$sysinfo['seller_page']=10;
}
$sysinfo['search_page']=intval($sysinfo['search_page']);
if($sysinfo['search_page']==0){
	$sysinfo['search_page']=10;
}
$sysinfo['product_page']=intval($sysinfo['product_page']);
if($sysinfo['product_page']==0){
	$sysinfo['product_page']=10;
}
$sysinfo['article_page']=intval($sysinfo['article_page']);
if($sysinfo['article_page']==0){
	$sysinfo['article_page']=10;
}
$sysinfo['height1']=intval($sysinfo['height1']);
if($sysinfo['height1']==0){
	$sysinfo['height1']=84;
}
$sysinfo['height2']=intval($sysinfo['height2']);
if($sysinfo['height2']==0){
	$sysinfo['height2']=300;
}
$sysinfo['width1']=intval($sysinfo['width1']);
if($sysinfo['width1']==0){
	$sysinfo['width1']=84;
}
$sysinfo['width2']=intval($sysinfo['width2']);
if($sysinfo['width2']==0){
	$sysinfo['width2']=300;
}

//数据表定义区
$t_settings = $tablePreStr."settings";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$time = $ctime->long_time();

$sql = "REPLACE INTO `$t_settings` (variable,`value`) VALUES('lastupdate','$time')";
foreach($sysinfo as $k=>$v) {
	$sql .= ",('$k','$v')";
}

if($dbo->exeUpdate($sql)) {
	//配置文件静态化
	$configfile=$webRoot.'configuration.php';
	$config_content = file_get_contents($configfile);
	$update_arr = array(
		'langPackagePara' => "'".$sysinfo["lp"]."'",
		'baseUrl' => "'".$sysinfo["web"]."'",
		'url_rewrite' => $sysinfo["url_r"],
		'im_enable' => $sysinfo["im_enable"],
		'session_prefix' => "'".$sysinfo["session"]."'",
	);
	$new_config_content = update_config_file($config_content,$update_arr);
	file_put_contents($configfile,$new_config_content);

	put_file($sysinfo);
	/** 添加log */
	$admin_log ="更新站点设置";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_upd_suc);
} else {
	action_return(0,$a_langpackage->a_upd_lose,'-1');
}

function put_file($sysinfo) {
	$content = '<'.'?php'."\n";
	$content .= "if(!".'$'."IWEB_SHOP_IN) {die('Hacking attempt');} \n";
	foreach($sysinfo as $k=>$v) {
		$v = str_replace('\"','"',$v);
		$content .= '$'."SYSINFO['$k'] = '$v'; \n";
	}
	$content .= '?'.'>';
	file_put_contents("../cache/setting.php",$content);
}
?>