<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$a_langpackage=new adminlp;

require_once("../foundation/module_asd.php");
require_once("../foundation/module_admin_logs.php");
//权限管理
$right=check_rights("adver_add");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* post */
$post = array(
	'position_id'	=> intval(get_args('position_id')),
	'media_type'	=> intval(get_args('media_type')),
	'asd_name'		=> short_check(get_args('asd_name')),
	'asd_link'		=> short_check(get_args('asd_link')),
	'remark'		=> long_check(get_args('remark'))
);
$wh = explode('X',get_args('asd_wh'));

if($post['media_type']==3) {
	$temp = get_args('content');
	$post['asd_content'] = long_check($temp[0]);
} else {
	$cupload = new upload('jpg|gif|png|swf',2048,'content');
	$cupload->set_dir("../uploadfiles/asd/","{y}/{m}/{d}");
	$file = $cupload->execute();
	if($file) {
		$file[0]['dir'] = str_replace("../","./",$file[0]['dir']);
		$post['asd_content'] = $file[0]['dir'].$file[0]['name'];
	}
}

if(!$post['asd_name']) {
	action_return(0,$a_langpackage->a_asdname_null,'-1');
}

$post['last_update_time'] = $ctime->long_time();

//数据表定义区
$t_asd_content = $tablePreStr."asd_content";
$t_admin_log = $tablePreStr."admin_log";
//定义写操作
dbtarget('w',$dbServs);
$dbo = new dbex;

if($asd_id = insert_asd_info($dbo,$t_asd_content,$post)) {
	put_asd_position_file($post['position_id'],$post['asd_link'],$post['asd_content'],$post['media_type'],$wh[0],$wh[1],$post['asd_name']);
	put_asd_position_file($asd_id,$post['asd_link'],$post['asd_content'],$post['media_type'],$wh[0],$wh[1],$post['asd_name']);
	action_return();
	admin_log($dbo,$t_admin_log,"新增广告:$asd_id");
} else {
	action_return(0,$a_langpackage->a_add_lose_repeat,'-1');
}
?>