<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$a_langpackage=new adminlp;

require_once("../foundation/module_asd.php");
require_once("../foundation/module_admin_logs.php");
//权限管理
$right=check_rights("adv_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');	
}
/* get */

$asd_id=get_args('asd_id');
if($asd_id){
	$asd_id=implode(",", $asd_id);
}else{
	$asd_id = intval(get_args('id'));
}
if(!$asd_id) {
	action_return(0,$a_langpackage->a_error,'-1');
}

//数据表定义区
$t_asd_content = $tablePreStr."asd_content";
$t_asd_position = $tablePreStr."asd_position";
$t_admin_log = $tablePreStr."admin_log";
$dbo=new dbex;
dbtarget('r',$dbServs);
$row = get_asd_info($dbo,$t_asd_content,$asd_id);
if($row) {
	$position_id = $row['position_id'];
} else {
	action_return();
}

//定义写操作
dbtarget('w',$dbServs);

$sql = "delete from `$t_asd_content` where asd_id in($asd_id)";

if($dbo->exeUpdate($sql)) {
	dbtarget('r',$dbServs);
	$sql = "SELECT a.*,b.asd_height,b.asd_width FROM $t_asd_content AS a, $t_asd_position AS b WHERE a.position_id=b.position_id and b.position_id='$position_id' order by a.last_update_time desc limit 1";
	$row = $dbo->getRow($sql);
	if($row) {
		put_asd_position_file($row['position_id'],$row['asd_link'],$row['asd_content'],$row['media_type'],$row['asd_width'],$row['asd_height'],$row['asd_name']);
	} else {
		put_asd_position_file($position_id,'','',0,0,0,'');
	}
	admin_log($dbo,$t_admin_log,"删除广告：$asd_id");
	action_return();
} else {
	action_return(0,$a_langpackage->a_del_lose_repeat,'-1');
}
?>