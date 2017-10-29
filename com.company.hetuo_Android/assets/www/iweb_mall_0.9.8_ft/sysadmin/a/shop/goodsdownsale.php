<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
include('../foundation/module_remind.php');
include('../foundation/csmtp.class.php');
include('../foundation/module_users.php');
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;

/* post 数据处理 */
//权限管理
$right=check_rights("shop_unsale");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
$shop_id=get_args('shop_id');

if($shop_id){
	$shop_id=implode(",", $shop_id);
}else{
	$shop_id = intval(get_args('id'));
}
if(!$shop_id) {exit($a_langpackage->a_error);}

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";
$t_users = $tablePreStr."users";
$t_settings = $tablePreStr."settings";
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select shop_name from $t_shop_info where shop_id IN ( $shop_id )";
$shop_info = $dbo->getRow($sql);
$shop_name = $shop_info['shop_name'];
//系统信息
$sql = "select * from `$t_settings`";
$result = $dbo->getRs($sql);
if($result) {
	foreach($result as $v) {
		$SYSINFO[$v['variable']] = $v['value'];
	}
}
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
$sql = "update $t_goods set is_on_sale=0,is_best=0,is_new=0,is_hot=0,is_promote=0 where shop_id in($shop_id)";
if($dbo->exeUpdate($sql)) {
	// 发送提醒
	$t_remind_user = $tablePreStr."remind_user";
	$t_remind = $tablePreStr."remind";
	$t_remind_info = $tablePreStr."remind_info";
	dbtarget('r',$dbServs);
	$remind_id = 2;

	if (get_args('shop_id')){
		$shop_id_arr = is_array(get_args('shop_id'))?get_args('shop_id'):array(get_args('shop_id'));
	}elseif(get_args('id')) {
		$shop_id_arr = is_array(get_args('id'))?get_args('id'):array(get_args('id'));
	}
	foreach ($shop_id_arr as $value){
		// 获取提醒信息
		$reminds = get_remind($dbo,$t_remind,$remind_id);
		if($reminds['enable']) {
			$remind_tpl = $reminds['remind_tpl'];
			// 获取用户的提醒设置
			$row = get_remind_user($dbo,$t_remind_user,$value,$remind_id);
			if($row) {
				$nowtime = $ctime->long_time();
				$remind_info = remind_info_replace($remind_tpl,array('time'=>$nowtime,'goods'=>$shop_name));
				if($row['site']) {
					$array = array(
						'user_id' => $value,
						'remind_info' => $remind_info,
						'remind_time' => $nowtime,
					);
					insert_remind_info($dbo,$t_remind_info,$array);
				}
				if($row['mail']) {
					if ($SYSINFO['email_send']=='true'){
						//获得用户信息
						$user_info = get_user_info($dbo,$t_users,$value);
						$user_email = $user_info['user_email'];
						$user_name = $user_info['user_name'];
						//邮件发送
						$nowtime = $ctime->long_time();
						$mailbody = $a_langpackage->a_dear_user.$user_name.$a_langpackage->a_hello."<br />".$a_langpackage->a_your_products.$shop_name.$a_langpackage->a_shop_products_all.$nowtime.$a_langpackage->a_by_admin_downsale.$a_langpackage->a_email_someth.$SYSINFO['sys_name'].$a_langpackage->a_generated_email."<br />";
						$mailbody = iconv('UTF-8','GBK',$mailbody);
						$mailtitle = $SYSINFO['sys_name'].$a_langpackage->a_email_reminder;
						$mailtitle = iconv('UTF-8','GBK',$mailtitle);
						$smtp = new smtp($SYSINFO['sys_smtpserver'],$SYSINFO['sys_smtpserverport'],true,$SYSINFO['sys_smtpuser'],$SYSINFO['sys_smtppass']);
						$smtp->sendmail($user_email, $SYSINFO['sys_smtpusermail'], $mailtitle, $mailbody, 'HTML');
					}
				}
				if($row['im']) {

				}
				if($row['mobile']) {

				}
			}
		}
	}
	admin_log($dbo,$t_admin_log,$sn = '下架商品');
	action_return(1,$a_langpackage->a_cancel_suc);
} else {
	action_return(0,$a_langpackage->a_cancel_lose,'-1');
}
?>