<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/login.html
 * 如果您的模型要进行修改，请修改 models/login.php
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
if(filemtime("templates/default/login.html") > filemtime(__file__) || (file_exists("models/login.php") && filemtime("models/login.php") > filemtime(__file__)) ) {
	tpl_engine("default","login.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");

//引入语言包
$i_langpackage=new indexlp;

/* 用户信息处理 */
if(get_sess_user_id()) {
	$USER['login'] = 1;
	$USER['user_name'] = get_sess_user_name();
	$USER['user_id'] = get_sess_user_id();
	$USER['user_email'] = get_sess_user_email();
	$USER['shop_id'] = get_sess_shop_id();
} else {
	$USER['login'] = 0;
	$USER['user_name'] = '';
	$USER['user_id'] = '';
	$USER['user_email'] = '';
	$USER['shop_id'] = '';
}

$iweb_shop=get_cookie('iweb_login');

/*导航位置*/
$nav_selected=1;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo $i_langpackage->i_member_login;?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/area.js"></script>
<script type='text/javascript' src='./servtools/md5.js'></script>
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>

<!--search end -->
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <?php echo $i_langpackage->i_member_login;?></div>
<div class="pro_class login">
<form action="do.php?act=login" method="post" onSubmit="return checkForm();">
<TABLE class="login_table left" cellSpacing=0 cellPadding=0 border="0">
  <TR>
    <TD width=60 rowSpan=6></TD>
    <TD colspan="2" style="height:10px"></TD>
    </TR>
  <TR>
    <TD colspan="2" class="login_bg" align="left"><?php echo  str_replace("{iweb_mall}",$SYSINFO['sys_name'],$i_langpackage->i_welcome_iwebshop);?></TD>
    </TR>
  <TR>
    <TD width="80" align="left"><?php echo  $i_langpackage->i_login_email;?>：</TD>
    <TD align="left" ><input name="user_email" type="text" value="<?php echo $iweb_shop;?>" maxlength="200" style="width:150px" /></TD>
    </TR>
  <TR>
    <TD align="left"><?php echo  $i_langpackage->i_login_password;?>：</TD>
    <TD align="left"><input name="user_passwd" type="password" value="" maxlength="50" style="width:150px" /><SPAN>&nbsp;&nbsp;</SPAN><a href="modules.php?app=forgot"><?php echo  $i_langpackage->i_getback_pw;?>?</a></TD>
    </TR>
  <TR>
    <TD align="left"><?php echo  $i_langpackage->i_verifycode;?>：</TD>
    <TD align="left"><input name="verifyCode" type="text" maxlength="4" style="width:70px" /> <img border="0" src="servtools/veriCodes.php" height="20" id="verCodePic" onclick="getVerCode();"></TD>
    </TR>

  <TR><TD></TD>
    <TD><INPUT class="button" type=submit value="<?php echo $i_langpackage->i_login;?>" name=<?php echo $i_langpackage->i_login;?>></TD></TR>

</TABLE>
</form>
<TABLE cellSpacing=0 cellPadding=0 class="register_table">
  <TR>
    <TD colspan="2"></TD></TR>
  <TR>
    <TD colspan="2" class="login_bg_01"><?php echo $i_langpackage->i_login_info_first;?></TD></TR>
  <TR>
    <TD colspan="2" class="login_bg_02"><?php echo $i_langpackage->i_login_info_sec;?></TD></TR>
  <TR>
    <TD colspan="2" class="login_bg_03"><?php echo $i_langpackage->i_login_info_the;?></TD></TR>
  <TR>
    <TD colspan="2" class="login_bg_04"><?php echo $i_langpackage->i_login_info_foru;?></TD></TR>
  <TR>
    <TD width="79">&nbsp;</TD>
    <TD width="239" valign="bottom"><input class="button" type=button value=<?php echo $i_langpackage->i_register_now;?> onclick="javascript:location.href='modules.php?app=reg'" /></TD>
  </TR>
</TABLE>
</div>
<div class="top5 clear"></div>
<script language="JavaScript">
<!--
function getVerCode() {
	document.getElementById("verCodePic").src="servtools/veriCodes.php?vc="+Math.random();
}
function checkForm() {
	var user_email = document.getElementsByName("user_email")[0];
	if(user_email.value=='') {
		alert('<?php echo  $i_langpackage->i_email_notnone;?>');
		user_email.focus();
		return false;
	}
	var user_passwd = document.getElementsByName("user_passwd")[0];
	if(user_passwd.value=='') {
		alert('<?php echo  $i_langpackage->i_password_notnone;?>');
		user_passwd.focus();
		return false;
	} else {
		document.getElementsByName("user_passwd")[0].value = MD5(user_passwd.value);
	}
	var verifyCode = document.getElementsByName("verifyCode")[0];
	if(verifyCode.value=='') {
		alert('<?php echo  $i_langpackage->i_verifycode_notnone;?>');
		verifyCode.focus();
		return false;
	}
	return true;
}
//-->
</script>
<!-- main end -->
<!--main right end-->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>
</body>
</html>
<?php } ?>