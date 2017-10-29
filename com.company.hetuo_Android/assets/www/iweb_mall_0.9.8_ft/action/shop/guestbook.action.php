<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

$user_id=get_sess_user_id();
$shop_id=get_sess_shop_id();
/* post 数据处理 */
$post['name'] = short_check(get_args('name'));
$post['email'] = short_check(get_args('email'));
$post['contact'] = short_check(get_args('contact'));
$post['content'] = big_check(get_args('content'));
$post['shop_id'] = intval(get_args('shop_id'));
$post['shop_name'] = short_check(get_args('shop_name'));
$post['add_time'] = $ctime->long_time();
$post['add_ip'] = $_SERVER['REMOTE_ADDR'];
$post['user_id'] = intval($user_id);
$post['group_id'] = intval(get_args('group_id'));
$post['group_name'] = short_check(get_args('group_name'));
$post['goods_id'] = intval(get_args('goods_id'));

if (!$post['goods_id']) {
	$post['goods_id']=0;
}
if($post['shop_id'] == $shop_id) {
	action_return(0,$m_langpackage->m_oneself_shop,'-1');
}
if(empty($post['name'])) {
	action_return(0,$m_langpackage->m_name_null,'-1');
}
if(empty($post['email'])) {
	action_return(0,$m_langpackage->m_mail_null,'-1');
}

//数据表定义区
$t_shop_guestbook = $tablePreStr."shop_guestbook";
$t_settings = $tablePreStr."settings";
$t_users = $tablePreStr."users";
//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
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

$item_sql = get_insert_item($post);
$sql = "insert into `$t_shop_guestbook` $item_sql";
if($dbo->exeUpdate($sql)) {
	// 发送提醒
	$t_remind_user = $tablePreStr."remind_user";
	$t_remind = $tablePreStr."remind";
	$t_remind_info = $tablePreStr."remind_info";
	dbtarget('r',$dbServs);
	include("./foundation/module_remind.php");
	$remind_id = 3;
	$user_id = $post['shop_id'];
	// 获取提醒信息
	$reminds = get_remind($dbo,$t_remind,$remind_id);
	if($reminds['enable']) {
		$remind_tpl = $reminds['remind_tpl'];
		// 获取用户的提醒设置
		$row = get_remind_user($dbo,$t_remind_user,$user_id,$remind_id);
		if($row) {
			$nowtime = $ctime->long_time();
			$remind_info = remind_info_replace($remind_tpl,array('time'=>$nowtime));
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
					include('./foundation/csmtp.class.php');
					include('./foundation/module_users.php');
					//获得用户信息
					$user_info = get_user_info($dbo,$t_users,$post['shop_id']);
					$user_email = $user_info['user_email'];
					$user_name = $user_info['user_name'];
					//邮件发送
					$nowtime = $ctime->long_time();
					if ($post['group_id']){
						$mailbody = "亲爱的用户".$user_name."您好:<br /> 您的团购商品".$post['group_name']."在".$nowtime."收到了新的留言。<br />赶快去看一下吧 <a href='".$baseUrl."goods.php?app=groupbuyinfo&id=".$post['group_id']."' target='_blank'>点击这里</a><br /> 如有疑问请联系管理员，感谢您对".$SYSINFO['sys_name']."的支持。<br />( 这是系统自动产生的邮件，请勿回复。)<br />";
					}else {
						$mailbody = "亲爱的用户".$user_name."您好:<br /> 您的商铺".$post['shop_name']."在".$nowtime."收到了新的留言。<br />赶快去看一下吧 <a href='".$baseUrl."shop.php?shopid=".$post['shop_id']."&app=index' target='_blank'>点击这里</a><br /> 如有疑问请联系管理员，感谢您对".$SYSINFO['sys_name']."的支持。<br />( 这是系统自动产生的邮件，请勿回复。)<br />";
					}
					$mailbody = iconv('UTF-8','GBK',$mailbody);
					$mailtitle = $SYSINFO['sys_name'].'邮箱提醒';
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

	action_return(1,$m_langpackage->m_mess_suc,'-1');
} else {
	action_return(0,$m_langpackage->m_mess_lose,'-1');
}
?>