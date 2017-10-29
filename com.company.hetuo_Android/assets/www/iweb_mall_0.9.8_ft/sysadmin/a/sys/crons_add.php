<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_crons.php");
require("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;


/* post 数据处理 */
$POST['name'] = short_check(get_args('name'));
$POST['available'] = short_check(get_args('available'));
$POST['weekday'] = short_check(get_args('weekdaynew'));
$POST['day'] = short_check(get_args('daynew'));
$POST['hour'] = short_check(get_args('hournew'));
$POST['phpfile'] = short_check(get_args('filenamenew'));
$minutenew = short_check(get_args('minutenew'));
$id = intval(get_args('id'));
$right=check_rights("crons_add");
$right_edit=check_rights("programme_edit");
if(!$right_edit&&$id){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
if($minutenew){
	$search=array(
		" ",
		"，",
	);
	$replace=array(
		"",
		","
	);
	$minutenew=str_replace($search,$replace,$minutenew);
	$minutenew=explode(",", $minutenew);
	$minutenew = array_unique($minutenew);
	sort($minutenew);
	foreach($minutenew as $key=>$val){
		$return=is_numeric($val);
		if(!$return){
			unset($minutenew[$key]);
		}
		if($val<0 || $val>60){
			unset($minutenew[$key]);
		}
	}
	$minutenew=implode(",",$minutenew);
	$POST['minute'] = $minutenew;
}
if(!$POST['minute']){
	$POST['minute']=0;
}

//数据表定义区
$t_crons = $tablePreStr."crons";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
if(!empty($id)){
	$POST['nextrun'] = next_run($POST['weekday'],$POST['day'],$POST['hour'],$POST['minute']);
	$upd_suc=update_crons($dbo,$t_crons,$POST,$id);
	if($upd_suc) {
		/** 添加log */
		$admin_log ="更新计划任务";
		admin_log($dbo,$t_admin_log,$admin_log);

		action_return(1,$a_langpackage->a_upd_suc,'-1');
	} else {
		action_return(0,$a_langpackage->a_upd_lose,'-1');
	}
}else{
	$time=$ctime->time_stamp();
	$POST['lastrun'] = $time;

	$POST['nextrun'] = next_run($POST['weekday'],$POST['day'],$POST['hour'],$POST['minute']);
	$int_suc=insert_crons($dbo,$t_crons,$POST);
	if($int_suc) {
		/** 添加log */
		$admin_log ="添加计划任务";
		admin_log($dbo,$t_admin_log,$admin_log);

		action_return(1,$a_langpackage->a_upd_suc,'-1');
	} else {
		action_return(0,$a_langpackage->a_upd_lose,'-1');
	}
}

?>