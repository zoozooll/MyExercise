<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

/* post 数据处理 */
$credit_update=check_rights("credit_update");
if(!$credit_update){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
//数据表定义区
$t_integral = $tablePreStr."integral";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql="TRUNCATE TABLE `$t_integral`";
$dbo->exeUpdate($sql);
$sql="insert into $t_integral(int_min,int_max,int_grade) value(";
for($i=1;$i<=15;$i++){
	$sql.=$_POST["min_$i"].','.$_POST["max_$i"].','.$i;
	if($i<15){
		$sql.='),(';
	}
}
$sql.=')';
$ins_suc=$dbo->exeUpdate($sql);
if($ins_suc){
	/** 添加log */
	$admin_log ="更新信用等级";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_amend_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}

?>