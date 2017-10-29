<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/reg/register.html
 * 如果您的模型要进行修改，请修改 models/modules/reg/register.php
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
if(filemtime("templates/default/modules/reg/register.html") > filemtime(__file__) || (file_exists("models/modules/reg/register.php") && filemtime("models/modules/reg/register.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/reg/register.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$i_langpackage=new indexlp;
require_once("foundation/asystem_info.php");

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
$nav_selected = '1';
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="开源商城,shop源码,iweb_shop" />
<meta name="description" content="开源商城,shop源码,iweb_shop" />
<title><?php echo  $i_langpackage->i_user_register;?></title>
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/area.js"></script>
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<!--search end -->
<div class="path"><?php echo  $i_langpackage->i_location;?>：<a href="index.php"><?php echo  $i_langpackage->i_index;?></a> > <?php echo  $i_langpackage->i_user_register;?></div>
<div class="pro_class">

<TABLE class="register top10" height=214 cellSpacing=1 cellPadding=1 align=center border=0>
      <TR>
        <TD width="20%" rowspan="11" valign="top" align="center" style="border-right:#ccc 1px dashed;"><?php echo  str_replace("{iweb_mall}",$SYSINFO['sys_name'],$i_langpackage->i_noshop_account_clickhere);?><br />
          <input class="button" style="height:26px;" type="submit" value="马上登录" name="regcheckbtn2" onclick="javascript:location.href='modules.php?app=login'" />
        	<ul><li></li></ul>
         </TD>
        <TD align=right><label>*</label></TD>
        <TD align=left colSpan=3><?php echo  $i_langpackage->i_pls_info_safe_msg;?></TD>
      </TR>
<form action="do.php?act=register" name="reg_form" method="post" onSubmit="return checkForm();">
  <TR>
    <TD align=right width="20%"><?php echo  $i_langpackage->i_reg_username;?>：</TD>
    <TD align=left colSpan=3><SPAN><input type="text" name="user_name" maxlength="20" /><label>*</label></SPAN><SPAN id="user_name_message"><?php echo  $i_langpackage->i_reg_unameinfo;?></SPAN></TD></TR>
  <TR>
    <TD align=right><?php echo  $i_langpackage->i_reg_email;?>：</TD>
    <TD align=left colSpan=3><SPAN><input type="text" name="user_email" maxlength="200" /><label>*</label></SPAN><SPAN id="user_email_message"><?php echo  $i_langpackage->i_reg_emailinfo;?></SPAN></TD></TR>
  <TR>
    <TD align=right><?php echo  $i_langpackage->i_reg_passwd;?>：</TD>
    <TD align=left colSpan=3><SPAN><input type="password" name="user_password" maxlength="16" /><label>*</label></SPAN><SPAN id="user_password_message"><?php echo  $i_langpackage->i_reg_passwdinfo;?></SPAN> </TD></TR>
  <TR>
    <TD align=right><?php echo  $i_langpackage->i_reg_repasswd;?>：</TD>
    <TD colspan="2" align=left style="HEIGHT: 19px"><SPAN><input type="password" name="user_repassword" /><label>*</label></SPAN> <SPAN id="user_repassword_message"></SPAN></TD></TR>


  <TR>
    <TD align=right><?php echo  $i_langpackage->i_reg_truename;?>：</TD>
    <TD colspan="2" align=left><input type="text" name="true_name" maxlength="20" /><label>*</label><SPAN  id="true_name_message"><?php echo  $i_langpackage->i_reg_truenameinfo;?></SPAN></TD></TR>
  <TR>
    <TD align=right><?php echo  $i_langpackage->i_reg_tel;?>：</TD>
    <TD colspan="2" align=left><input type="text" name="user_telphone" value="" /><label>*</label><SPAN id="user_telphone_message"><?php echo  $i_langpackage->i_reg_telinfo;?></SPAN></TD>
  </TR>
  <TR>
    <TD align=right><?php echo  $i_langpackage->i_reg_mobile;?>：</TD>
    <TD colspan="2" align=left><input type="text" name="user_mobile" /><label>*</label><SPAN id="user_mobile_message"><?php echo  $i_langpackage->i_reg_mobileinfo;?></SPAN></TD></TR>
  <TR>
    <TD align=right><?php echo  $i_langpackage->i_verifycode;?>：</TD>
    <TD align=left><input type="text" style="width:60px" name="veriCode" maxlength="4" /> <img border="0" src="servtools/veriCodes.php" id="verCodePic" onclick="getVerCode();"><label>*</label><SPAN id="veriCode_message"><?php echo  $i_langpackage->i_reg_inputvf;?></SPAN>
	</TD>
  </TR>
  <TR>
    <td align="right"></td>
    <td colspan="2" align="left"><input class="button" style="height:26px;" type="submit" value="<?php echo  $i_langpackage->i_register_now;?>" name="regcheckbtn" /></td>
    </TR>
</FORM>
  <TR>
    <TD colspan="3" align=center><span class="agreement top10">
      <textarea name="textarea" ><?php echo $SYSINFO['sys_registerinfo'];?></textarea>
    </span></TD>
    </TR>
</TABLE>

</div>
<div class="top5 clear"></div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--

function getVerCode() {
	document.getElementById("verCodePic").src="servtools/veriCodes.php?vc="+Math.random();
}

// 检测会员用户名
var user_name = document.getElementsByName('user_name')[0];
var user_name_message = document.getElementById('user_name_message');
var user_name_status = false;
var user_name_reg = /^[0-9a-z_]{4,16}$/i
user_name.onblur = function(){
	if(user_name.value=='') {
		user_name_message.style.color = 'red';
		user_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsg_inputuname;?>';
		user_name_status = false;
	} else if(!user_name.value.match(user_name_reg)) {
		user_name_message.style.color = 'red';
		user_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsg_formatname;?>';
		user_name_status = false;
	}else {
		user_name_message.style.color = 'red';
		user_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgname_checknow;?>';
		ajax("do.php?act=user_check_username","POST","v="+user_name.value,function(data){
			if(data==1) {
				user_name_message.style.color = 'green';
				user_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgname_isok;?>';
				user_name_status = true;
			} else {
				user_name_message.style.color = 'red';
				user_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgname_used;?>';
				user_name_status = false;
			}
		});
	}
};

// 检测邮箱
var user_email = document.getElementsByName('user_email')[0];
var user_email_message = document.getElementById('user_email_message');
var user_email_status = false;
var user_email_reg = /^[0-9a-zA-Z_\-\.]+@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/;
user_email.onblur = function(){
	if(user_email.value=='') {
		user_email_message.style.color = 'red';
		user_email_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmail_input;?>';
		user_email_status = false;
	} else if(!user_email.value.match(user_email_reg)) {
		user_email_message.style.color = 'red';
		user_email_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmail_format;?>';
		user_email_status = false;
	} else {
		user_email_message.style.color = 'red';
		user_email_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmail_checknow;?>';
		ajax("do.php?act=user_check_useremail","POST","v="+user_email.value,function(data){
			if(data==1) {
				user_email_message.style.color = 'green';
				user_email_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmail_isok;?>';
				user_email_status = true;
			} else {
				user_email_message.style.color = 'red';
				user_email_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmail_used;?>';
				user_email_status = false;
			}
		});
	}
};

// 检测密码
var user_password = document.getElementsByName('user_password')[0];
var user_password_message = document.getElementById('user_password_message');
var user_password_status = false;
user_password.onblur = function(){
	if(user_password.value=='') {
		user_password_message.style.color = 'red';
		user_password_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgpwd_input;?>';
		user_password_status = false;
	} else if(user_password.value.length<6 || user_password.value.length>16) {
		user_password_message.style.color = 'red';
		user_password_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgpwd_format;?>';
		user_password_status = false;
	} else {
		user_password_message.style.color = 'green';
		user_password_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgpwd_right;?>';
		user_password_status = true;
	}
};

// 检测确认密码
var user_repassword = document.getElementsByName('user_repassword')[0];
var user_repassword_message = document.getElementById('user_repassword_message');
var user_repassword_status = false;
user_repassword.onblur = function(){
	if(user_repassword.value=='') {
		user_repassword_message.style.color = 'red';
		user_repassword_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgrepwd_input;?>';
		user_repassword_status = false;
	} else if(user_repassword.value!=user_password.value) {
		user_repassword_message.style.color = 'red';
		user_repassword_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgpwd_notfaf;?>';
		user_repassword_status = false;
	} else {
		user_repassword_message.style.color = 'green';
		user_repassword_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgrepwd_format;?>';
		user_repassword_status = true;
	}
};

// 检测真实姓名
var true_name = document.getElementsByName('true_name')[0];
var true_name_message = document.getElementById('true_name_message');
var true_name_status = false;
//var true_name_reg = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]){2,4}$");
var true_name_reg = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z _]){2,15}$");
true_name.onblur = function(){
	if(true_name.value=='') {
		true_name_message.style.color = 'red';
		true_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgtn_input;?>';
		true_name_status = false;
	} else if(!true_name_reg.test(true_name.value)) {
		true_name_message.style.color = 'red';
		true_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgtn_notright;?>';
		true_name_status = false;
	} else {
		true_name_message.style.color = 'green';
		true_name_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgtn_right;?>';
		true_name_status = true;
	}
};

// 检测联系电话
var user_telphone = document.getElementsByName('user_telphone')[0];
var user_telphone_message = document.getElementById('user_telphone_message');
var user_telphone_status = false;
//var user_telphone_reg = new RegExp("^0[0-9]{2,3}-?[0-9]{7,8}$");
var user_telphone_reg = new RegExp("[0-9-]{5,12}");
user_telphone.onblur = function(){
	if(user_telphone.value=='') {
		user_telphone_message.style.color = 'red';
		user_telphone_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgtel_input;?>';
		user_telphone_status = false;
	} else if(!user_telphone_reg.test(user_telphone.value)) {
		user_telphone_message.style.color = 'red';
		user_telphone_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgtel_notright;?>';
		user_telphone_status = false;
	} else {
		user_telphone_message.style.color = 'green';
		user_telphone_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgtel_right;?>';
		user_telphone_status = true;
	}
};

// 检测联系手机
var user_mobile = document.getElementsByName('user_mobile')[0];
var user_mobile_message = document.getElementById('user_mobile_message');
var user_mobile_status = false;
//var user_mobile_reg = new RegExp("^1[0-9]{10}$");
var user_mobile_reg = new RegExp("[0-9-]{5,15}");
user_mobile.onblur = function(){
	if(user_mobile.value=='') {
		user_mobile_message.style.color = 'red';
		user_mobile_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmob_input;?>';
		user_mobile_status = false;
	} else if(!user_mobile_reg.test(user_mobile.value)) {
		user_mobile_message.style.color = 'red';
		user_mobile_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmob_notright;?>';
		user_mobile_status = false;
	} else {
		user_mobile_message.style.color = 'green';
		user_mobile_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgmob_right;?>';
		user_mobile_status = true;
	}
};

function checkForm() {
	var veriCode = document.getElementsByName('veriCode')[0];
	var veriCode_message = document.getElementById('veriCode_message');
	if(veriCode.value=='') {
		veriCode_message.style.color = 'red';
		veriCode_message.innerHTML = '<?php echo  $i_langpackage->i_rmsgvf_input;?>';
		return false;
	}
	if(user_name_status && user_email_status && user_password_status && user_repassword_status && true_name_status && user_telphone_status && user_mobile_status) {
		return true;
	} else {
		user_name.onblur();
		user_email.onblur();
		user_password.onblur();
		user_repassword.onblur();
		true_name.onblur();
		user_telphone.onblur();
		user_mobile.onblur();
		if(user_name_status && user_email_status && user_password_status && user_repassword_status && true_name_status && user_telphone_status && user_mobile_status) {
			return true;
		}
		return false;
	}
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