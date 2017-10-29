<?php /* Smarty version 2.6.18, created on 2011-02-20 05:51:53
         compiled from login.html */ ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=<?php echo $this->_tpl_vars['Charset']; ?>
">
<title><?php echo $this->_tpl_vars['_control_login']; ?>
 - <?php echo $this->_tpl_vars['site_title']; ?>
</title>
<link rel="stylesheet" type="text/css" href="css/login.css" />
<script language="javascript" src="../scripts/jquery.js"></script>
<script language="javascript" src="js/jquery.pngFix.js"></script>
<body>
<div id="header">
   <div class="logo"></div>
</div>
<div id="wrapper">
   <div class="console_left">
	   <div class="title"><?php echo $this->_tpl_vars['_control_login']; ?>
</div>
	   <p><?php echo $this->_tpl_vars['_phpb2b_info']; ?>
</p>
	   <div class="intro_1"><?php echo $this->_tpl_vars['_config_your_info']; ?>
</div>
	   <div class="intro_2"><?php echo $this->_tpl_vars['_manage_your_ads']; ?>
</div>
	   <div class="intro_3"><?php echo $this->_tpl_vars['_set_site_secure']; ?>
</div>
	  
   </div>
   <div class="console_right">
	   <div class="title"><?php echo $this->_tpl_vars['_cp_login_first']; ?>
</div>
	   <div class="login">
		  <form action="login.php" method="post" name="login_frm" id="LoginFrm" class="s_lo_f" autocomplete="off">
			<input type="hidden" name="do" value="login"  />
			<input type="hidden" name="formhash" value="<?php echo $this->_tpl_vars['formhash']; ?>
" />
		   <div class="user"><label><?php echo $this->_tpl_vars['_cp_username_n']; ?>
</label><input tabindex="1" type="text" name="data[username]" id="username" size="16" maxLength="20" value=""><?php echo $this->_tpl_vars['LoginError']; ?>
</div>
		   <div class="pwd"><label><?php echo $this->_tpl_vars['cp_passwd']; ?>
</label><input tabindex="2" type="password" name="data[userpass]" id="userpass" size="16" maxLength="20" value=""></div>
		   <div class="forget"><a href="<?php echo $this->_tpl_vars['SiteUrl']; ?>
getpasswd.php"><?php echo $this->_tpl_vars['_cp_forget_passwd']; ?>
</a></div>
		   <?php if ($this->_tpl_vars['ifcapt']): ?>
		   <div class="code"><label><?php echo $this->_tpl_vars['_cp_capt']; ?>
</label><input type="text" value="" name="data[capt_login_admin]" size="16" tabindex="3" id="capt_login_admin" /><img id="imgcaptcha" src="../captcha.php?sid=<?php echo $this->_tpl_vars['sid']; ?>
" alt="<?php echo $this->_tpl_vars['_refresh_capt']; ?>
" /></div>
		   <?php endif; ?>
		   <div class="btn_login"><input type="submit" name="login" id="Login" value="<?php echo $this->_tpl_vars['_cp_login']; ?>
" /></div>
		   </form>
	   </div>
   </div>
   <div class="clear"></div>
	<div class="reg"><?php echo $this->_tpl_vars['_meet_with_more_phpb2b_users']; ?>
&nbsp;<input name="" id="register_member" type="button" value="<?php echo $this->_tpl_vars['_register_now']; ?>
" onclick="location.href='http://bbs.phpb2b.com/'" /></div>
	<hr class="hr_solid" /></div>
</div>
<div id="footer"><span class="left">&copy;2007-2010 Powered By <?php echo $this->_tpl_vars['_soft_name']; ?>
 <?php echo @PHPB2B_VERSION; ?>
, <a href="http://www.phpb2b.com/">Ualink</a> Inc.
</span><span class="right"><a href="<?php echo $this->_tpl_vars['SiteUrl']; ?>
index.php" title="<?php echo $this->_tpl_vars['_return_homepage']; ?>
"><?php echo $this->_tpl_vars['_return_homepage']; ?>
</a>&nbsp;|&nbsp;<a href="http://bbs.phpb2b.com/"><?php echo $this->_tpl_vars['_feed_back']; ?>
</a></span> 
</div>   
<script language="javascript">
<!-- 
$(document).ready(function(){   
	$('#username').focus();
	$('#imgcaptcha').click(
		function(){
		$('#imgcaptcha').attr('src','../captcha.php?sid=' + Math.random());
		$('#capt_login_admin').focus();
		return false;
		}
	);
	$("#Login").click(
		function(){   
			<?php if ($_SESSION['is_admin'] == ""): ?>
				if($('#username').val() == ""){
					alert("<?php echo $this->_tpl_vars['_pls_input_username']; ?>
");
					$('#username').focus();
					return false;
				}
			<?php endif; ?>
			if($('#userpass').val() == ""){
				alert("<?php echo $this->_tpl_vars['_pls_input_passwd']; ?>
");
				$('#userpass').focus();
				return false;
			}
			<?php if ($this->_tpl_vars['ifcapt']): ?>
			if($('#capt_login_admin').val() == ""){
				alert("<?php echo $this->_tpl_vars['_pls_input_authcode']; ?>
");
				$('#capt_login_admin').focus();
				return false;
			}
			<?php endif; ?>
			$("#LoginFrm").submit();   
			$("#Login").attr("disabled", "disabled");
		}
	);
});
//-->
</script>
<script type="text/javascript">
	$(document).ready(function(){
		$('div.logo').pngFix( );
	});
</script>
</body>
</html>