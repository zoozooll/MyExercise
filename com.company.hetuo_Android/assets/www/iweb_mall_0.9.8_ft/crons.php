<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require("foundation/module_crons.php");
require("foundation/module_admin_logs.php");

$id = intval(get_args('id'));

$t_crons = $tablePreStr."crons";
$t_admin_log = $tablePreStr."admin_log";

dbtarget('r',$dbServs);
$dbo=new dbex();

// 获取需要更新的计划任务
if(empty($id)){
	$row = get_needful_crons($dbo,$t_crons);
}elseif(!empty($id)&&!empty($_SESSION['admin_name'])){
	$row = get_crons_row($dbo,$t_crons,$id);
}else{
	exit;
}

if($row['phpfile']) {
	// 执行计划任务程
	require("crons/".$row['phpfile']);

	// 执行完毕之后更新当前计划任务
	/* post 数据处理 */
	$id=$row['id'];
	$POST['lastrun'] = $ctime->time_stamp();
	$POST['nextrun'] = next_run($row['weekday'],$row['day'],$row['hour'],$row['minute']);
	$upd_suc=update_crons($dbo,$t_crons,$POST,$id);
	if($upd_suc) {
		/** 添加log */
		$admin_log ="执行计划任务";
		admin_log($dbo,$t_admin_log,$admin_log);

		exit("1");
	} else {
		exit("-1");
	}
}
exit("-1");
?>