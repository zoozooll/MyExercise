<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;

/* POST */
$user_id = intval(get_args('uid'));
$code = short_check(get_args('ucode'));

if(!$user_id || !$code) {
	echo '<script language="JavaScript">alert("此链接已失效！"); location.href="modules.php"</script>';
	exit;
}

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_users = $tablePreStr."users";

$sql = "select * from `$t_users` where user_id='$user_id'";
$row = $dbo->getRow($sql);
if($row['forgot_check_code'] != $code) {
	echo '<script language="JavaScript">alert("此链接已失效！"); location.href="modules.php"</script>';
	exit;
}
?>