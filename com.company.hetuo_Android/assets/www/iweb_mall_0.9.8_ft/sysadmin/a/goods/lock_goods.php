<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
include('../foundation/module_remind.php');
include('../foundation/csmtp.class.php');
include('../foundation/module_users.php');
include('../foundation/module_admin_logs.php');
//语言包引入
$a_langpackage=new adminlp;

/* post 数据处理 */
$value = intval(get_args('v'));
$goodsid=get_args('goods_id');
if($goodsid){
	$goods_id=implode(",", $goodsid);
}else{
	$goods_id = intval(get_args('id'));
}

if(!$goods_id) {exit($a_langpackage->a_error);}

//权限管理
if ($value == 1){
	$right=check_rights("goods_lock");
	if(!$right){
		action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
		exit;
	}
}else {
	$right=check_rights("goods_unlock");
	if(!$right){
		action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
		exit;
	}
}

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_users = $tablePreStr."users";
$t_settings = $tablePreStr."settings";
$t_admin_log = $tablePreStr."admin_log";
//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select shop_id,goods_name from $t_goods where goods_id IN ($goods_id)";
$goods_userinfo = $dbo->getRow($sql);
$shop_id = $goods_userinfo['shop_id'];
$goods_name = $goods_userinfo['goods_name'];
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
//锁定商品
$sql = "update $t_goods set lock_flg=$value  where goods_id in($goods_id)";
$update=$dbo->exeUpdate($sql);
if($value==1){
	if($update) {
		// 发送提醒
		$t_remind_user = $tablePreStr."remind_user";
		$t_remind = $tablePreStr."remind";
		$t_remind_info = $tablePreStr."remind_info";
		dbtarget('r',$dbServs);
		$remind_id = 4;
		$user_id = $shop_id;
		// 获取提醒信息
		$reminds = get_remind($dbo,$t_remind,$remind_id);
		if($reminds['enable']) {
			$remind_tpl = $reminds['remind_tpl'];
			// 获取用户的提醒设置
			$row = get_remind_user($dbo,$t_remind_user,$user_id,$remind_id);
			if($row) {
				$nowtime = $ctime->long_time();
				$remind_info = remind_info_replace($remind_tpl,array('time'=>$nowtime,'goods'=>$goods_name));
				if($row['site']) {
					$array = array(
						'user_id' => $user_id,
						'remind_info' => $remind_info,
						'remind_time' => $nowtime,
					);
					insert_remind_info($dbo,$t_remind_info,$array);
				}
				if($row['mail']) {
					if ($SYSINFO['email_send']=='true'){
						//获得用户信息
						$user_info = get_user_info($dbo,$t_users,$shop_id);
						$user_email = $user_info['user_email'];
						$user_name = $user_info['user_name'];
						//邮件发送
						$nowtime = $ctime->long_time();
						$mailbody = $a_langpackage->a_dear_user.$user_name.$a_langpackage->a_hello."<br />".$a_langpackage->a_your_products.$goods_name.$a_langpackage->a_in.$nowtime.$a_langpackage->a_by_admin_lockgoods.$a_langpackage->a_email_someth.$SYSINFO['sys_name'].$a_langpackage->a_generated_email."<br />";
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
		admin_log($dbo,$t_admin_log,"锁定商品：$goods_id");
		action_return(1,$a_langpackage->a_lock_goods_suc);
	} else {
		action_return(0,$a_langpackage->a_lock_goods_fail,'-1');
	}
}else{
	if($update) {
		// 发送提醒
		$t_remind_user = $tablePreStr."remind_user";
		$t_remind = $tablePreStr."remind";
		$t_remind_info = $tablePreStr."remind_info";
		dbtarget('r',$dbServs);
		$remind_id = 7;
		$user_id = $shop_id;
		// 获取提醒信息
		$reminds = get_remind($dbo,$t_remind,$remind_id);
		if($reminds['enable']) {
			$remind_tpl = $reminds['remind_tpl'];
			// 获取用户的提醒设置
			$row = get_remind_user($dbo,$t_remind_user,$user_id,$remind_id);
			if($row) {
				$nowtime = $ctime->long_time();
				$remind_info = remind_info_replace($remind_tpl,array('time'=>$nowtime,'goods'=>$goods_name));
				if($row['site']) {
					$array = array(
						'user_id' => $user_id,
						'remind_info' => $remind_info,
						'remind_time' => $nowtime,
					);
					insert_remind_info($dbo,$t_remind_info,$array);
				}
				if($row['mail']) {
					if ($SYSINFO['email_send']=='true'){
						//获得用户信息
						$user_info = get_user_info($dbo,$t_users,$shop_id);
						$user_email = $user_info['user_email'];
						$user_name = $user_info['user_name'];
						//邮件发送
						$nowtime = $ctime->long_time();
						$mailbody = $a_langpackage->a_dear_user.$user_name.$a_langpackage->a_hello."<br />".$a_langpackage->a_your_products.$goods_name.$a_langpackage->a_in.$nowtime.$a_langpackage->a_by_admin_not_lockgoods.$a_langpackage->a_email_someth.$SYSINFO['sys_name'].$a_langpackage->a_generated_email."<br />";
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
		admin_log($dbo,$t_admin_log,"解锁商品：$goods_id");
		action_return(1,$a_langpackage->a_free_goods_suc);
	} else {
		action_return(0,$a_langpackage->a_free_goods_fail,'-1');
	}
}

?>