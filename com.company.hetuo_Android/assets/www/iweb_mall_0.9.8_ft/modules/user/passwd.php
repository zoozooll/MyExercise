<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/passwd.html
 * 如果您的模型要进行修改，请修改 models/modules/user/passwd.php
 *
 * 修改完成之后需要您进入后台重新编译，才会重新生成。
 * 如果您开启了debug模式运行，那么您可以省去上面这一步，但是debug模式每次都会判断程序是否更新，debug模式只适合开发调试。
 * 如果您正式运行此程序时，请切换到service模式运行！
 *
 * 如您有问题请到官方论坛（http://tech.jooyea.com/bbs/）提问，谢谢您的支持。
 */
?><?php
/*
 * 此段代码由debug模式下生成运行，请勿改动！
 * 如果debug模式下出错不能再次自动编译时，请进入后台手动编译！
 */
/* debug模式运行生成代码 开始 */
if(!function_exists("tpl_engine")) {
	require("foundation/ftpl_compile.php");
}
if(filemtime("templates/default/modules/user/passwd.html") > filemtime(__file__) || (file_exists("models/modules/user/passwd.php") && filemtime("models/modules/user/passwd.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/passwd.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$m_langpackage=new moduleslp;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
td{text-align:left;}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_edit_password;?></h3></div>
			<form action="do.php?act=user_passwd" method="post" name="form_passwd" onsubmit="return checkForm();">
				<table width="98%" class="form_table">
					<tr><td class="textright"><?php echo  $m_langpackage->m_old_password;?>:</td>
						<td><input type="password" name="user_passwd" value="" maxlength="20" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_new_password;?>:</td>
						<td><input type="password" name="user_new_passwd" value="" maxlength="20" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_re_password;?>:</td>
						<td><input type="password" name="user_verify_passwd" value="" maxlength="20" /></td></tr>
					<tr><td colspan="2" class="center"><input type="hidden" name="user_id" value="<?php echo  $user_id;?>" />
						<input type="submit" name="submit" value="<?php echo  $m_langpackage->m_edit_password;?>" /></td></tr>
				</table>
			</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
<script language="JavaScript">
<!--
function checkForm() {
	var user_passwd = document.getElementsByName("user_passwd")[0];
	var user_new_passwd = document.getElementsByName("user_new_passwd")[0];
	var user_verify_passwd = document.getElementsByName("user_verify_passwd")[0];
	if(user_passwd.value=='') {
		alert("<?php echo  $m_langpackage->m_oldpassword_notnone;?>");
		user_passwd.focus();
		return false;
	} else if(user_new_passwd.value=='') {
		alert("<?php echo  $m_langpackage->m_password_notnone;?>");
		user_new_passwd.focus();
		return false;
	} else if(user_verify_passwd.value!=user_new_passwd.value) {
		alert("<?php echo  $m_langpackage->m_password_notsame;?>");
		user_verify_passwd.value = '';
		user_verify_passwd.focus();
		return false;
	} else if(user_passwd.value==user_new_passwd.value) {
		alert("<?php echo  $m_langpackage->m_password_same;?>");
		user_new_passwd.focus();
		user_new_passwd.value = '';
		user_verify_passwd.value = '';
		return false;
	}

	return true;
}
//-->
</script>
</body>
</html><?php } ?>