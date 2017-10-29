<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_users.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("user_rank_edit");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

$post['rank_name'] = short_check(get_args('rank_name'));

$array = array();
$privilege = get_args('privilege');
$privilege_name = get_args('privilege_name');
if($privilege) {
	foreach($privilege as $key=>$value) {
		$array[$key] = $value;
		if(isset($privilege_name[$key])) {
			$array[$key] = $privilege_name[$key];
		}
	}
}

$post['privilege'] = serialize($array);
$rank_id = intval(get_args('rank_id'));
if(!$rank_id) { exit($a_langpackage->a_error);}

//数据表定义区
$t_user_rank = $tablePreStr."user_rank";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
$dbo = new dbex;
dbtarget('w',$dbServs);
if(update_userrank_info($dbo,$t_user_rank,$post,$rank_id)) {
	admin_log($dbo,$t_admin_log,$sn = '管理员级别修改');
	action_return(1,$a_langpackage->a_amend_suc,'m.php?app=member_rank_edit&id='.$rank_id);
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>