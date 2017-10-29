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
//权限管理
$right=check_rights("shop_lock");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* post 数据处理 */
$value = intval(get_args('v'));
$shopid=get_args('shop_id');
if($shopid){
	$shop_id=implode(",", $shopid);
}else{
	$shop_id = intval(get_args('id'));
}

if(!$shop_id) {exit($a_langpackage->a_error);}
//数据表定义区
$t_shop_info = $tablePreStr."shop_info";
$t_goods = $tablePreStr."goods";
$t_users = $tablePreStr."users";
$t_settings = $tablePreStr."settings";
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select shop_name from $t_shop_info where shop_id IN ($shop_id)";
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
//锁定商铺
$sql_shop = "update $t_shop_info set lock_flg='$value'  where shop_id in($shop_id)";
$_SESSION['shop_lock']=$value;
$shop=$dbo->exeUpdate($sql_shop);
//商品下架
if($value){
	$sql_goods = "update $t_goods set is_on_sale=0, lock_flg=$value where shop_id in($shop_id)";
}else{
	$sql_goods = "update $t_goods set is_on_sale=1, lock_flg=$value where shop_id in($shop_id)";
}
$goods=$dbo->exeUpdate($sql_goods);
if($value){
	if($shop or $goods) {
		//商铺锁定
		if ($shop){
			// 发送提醒
			$t_remind_user = $tablePreStr."remind_user";
			$t_remind = $tablePreStr."remind";
			$t_remind_info = $tablePreStr."remind_info";
			dbtarget('r',$dbServs);
			$remind_id = 5;
			$user_id = $shop_id;
			$shopid = is_array(get_args('id'))?get_args('id'):array(get_args('id'));

			if (get_args('shop_id')){
				$shopid = is_array(get_args('shop_id'))?get_args('shop_id'):array(get_args('shop_id'));
			}elseif(get_args('id')) {
				$shopid = is_array(get_args('id'))?get_args('id'):array(get_args('id'));
			}
			foreach ($shopid as $k=>$v){
				// 获取提醒信息
				$reminds = get_remind($dbo,$t_remind,$remind_id);
				if($reminds['enable']) {
					$remind_tpl = $reminds['remind_tpl'];
					// 获取用户的提醒设置
					$row = get_remind_user($dbo,$t_remind_user,$v,$remind_id);
					if($row) {
						$nowtime = $ctime->long_time();
						$remind_info = remind_info_replace($remind_tpl,array('time'=>$nowtime,'goods'=>$shop_name));
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
								$mailbody = $a_langpackage->a_dear_user.$user_name.$a_langpackage->a_hello."<br />".$a_langpackage->a_your_products.$shop_name.$a_langpackage->a_in.$nowtime.$a_langpackage->a_by_admin_lockgoods.$a_langpackage->a_email_someth.$SYSINFO['sys_name'].$a_langpackage->a_generated_email."<br />";
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
		}
		if ($goods){
			// 发送提醒
			$t_remind_user = $tablePreStr."remind_user";
			$t_remind = $tablePreStr."remind";
			$t_remind_info = $tablePreStr."remind_info";
			dbtarget('r',$dbServs);
			$remind_id = 2;
			$user_id = $shop_id;
			// 获取提醒信息
			$reminds = get_remind($dbo,$t_remind,$remind_id);
			foreach ($shopid as $k=>$v){
				if($reminds['enable']) {
					$remind_tpl = $reminds['remind_tpl'];
					// 获取用户的提醒设置
					$row = get_remind_user($dbo,$t_remind_user,$v,$remind_id);
					if($row) {
						$nowtime = $ctime->long_time();
						$remind_info = remind_info_replace($remind_tpl,array('time'=>$nowtime,'goods'=>$shop_name));
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
								$user_info = get_user_info($dbo,$t_users,$v);
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
		}
		action_return(1,$a_langpackage->a_lock_shop_suc);
	} else {
		action_return(0,$a_langpackage->a_lock_shop_fail,'-1');
	}
}else{
	if($shop or $goods) {
		//商铺解除锁定
		// 发送提醒
		$t_remind_user = $tablePreStr."remind_user";
		$t_remind = $tablePreStr."remind";
		$t_remind_info = $tablePreStr."remind_info";
		dbtarget('r',$dbServs);
		$remind_id = 6;
		$user_id = $shop_id;
		// 获取提醒信息
		$reminds = get_remind($dbo,$t_remind,$remind_id);
		if (get_args('shop_id')){
			$shopid = is_array(get_args('shop_id'))?get_args('shop_id'):array(get_args('shop_id'));
		}elseif(get_args('id')) {
			$shopid = is_array(get_args('id'))?get_args('id'):array(get_args('id'));
		}
		foreach ($shopid as $v){
			if($reminds['enable']) {
				$remind_tpl = $reminds['remind_tpl'];
				// 获取用户的提醒设置
				$row = get_remind_user($dbo,$t_remind_user,$v,$remind_id);
				if($row) {
					$nowtime = $ctime->long_time();
					$remind_info = remind_info_replace($remind_tpl,array('time'=>$nowtime,'goods'=>$shop_name));
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
							$user_info = get_user_info($dbo,$t_users,$v);
							$user_email = $user_info['user_email'];
							$user_name = $user_info['user_name'];
							//邮件发送
							$nowtime = $ctime->long_time();
							$mailbody = $a_langpackage->a_dear_user.$user_name.$a_langpackage->a_hello."<br />".$a_langpackage->a_your_products.$shop_name.$a_langpackage->a_in.$nowtime.$a_langpackage->a_a_by_admin_removegoods.$a_langpackage->a_email_someth.$SYSINFO['sys_name'].$a_langpackage->a_generated_email."<br />";
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
		admin_log($dbo,$t_admin_log,$sn = '店铺锁定解锁操作');
		action_return(1,$a_langpackage->a_free_shop_suc);
	} else {
		action_return(0,$a_langpackage->a_free_shop_fail,'-1');
	}
}
?>