<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$i_langpackage=new indexlp;

require_once("foundation/asystem_info.php");

/* GET */
$user_id = intval(get_args('uid'));
$email_check_code = get_args('ucode');

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_users = $tablePreStr."users";

if($user_id && $email_check_code) {
	$sql = "select email_check_code,email_check from `$t_users` where user_id='$user_id'";
	$row = $dbo->getRow($sql);
	if($row['email_check']) {
		echo '<script language="JavaScript">alert("您已通过验证，请勿重复验证。"); location.href="modules.php"</script>';
		exit;
	}else{
		if($row['email_check_code'] == $email_check_code){
			/* 数据库操作 */
			dbtarget('w',$dbServs);
			$dbo=new dbex();

			$sql = "update `$t_users` set email_check=1 where user_id='$user_id'";
			$dbo->exeUpdate($sql);

			echo '<script language="JavaScript">alert("恭喜您已通过验证。"); location.href="modules.php"</script>';
			exit;
		}else{
			echo '<script language="JavaScript">alert("邮箱验证码错误，请重新发送。"); location.href="modules.php?app=send_code"</script>';
			exit;
		}
	}
} else {
	echo '<script language="JavaScript">alert("url不完整，请确认。"); location.href="modules.php?app=reg2"</script>';
	exit;
}
?>