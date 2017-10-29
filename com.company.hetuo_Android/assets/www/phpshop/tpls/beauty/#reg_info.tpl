<div id="reginfo">
<script language="javascript">
function checkdata() {
	var error = false;
	var form_reg = document.form_reg;
	setMsgDiv('note-username', '');
	if(form_reg.username.value == '') {
		setMsgDiv('note-username', '您忘了填称呼！');
		error = true;
	}
	setMsgDiv('note-email', '');
	if(form_reg.email.value == '') {
		setMsgDiv('note-email', '您忘了填email！');
		error = true;
	} 
	if(form_reg.email.value != form_reg.reemail.value) {
		setMsgDiv('note-email', '两次填入的email不相等！');
		error = true;
	} 
	if(form_reg.email.value != '') {
		var patrn=/^(.+?)@([a-z0-9\.]+)\.([a-z]){2,5}$/i; 
		if (!patrn.exec(form_reg.email.value)) {
			setMsgDiv('note-email', '填入的email不正确！');
			error = true;
		}
	} 
	setMsgDiv('note-password', '');
	if(form_reg.password.value == '') {
		setMsgDiv('note-password', '您忘了填密码！');
		error = true;
	}
	if(form_reg.password.value != form_reg.repassword.value) {
		setMsgDiv('note-password', '两次填入的密码不相等！');
		error = true;
	} 
	setMsgDiv('note-phone', '');
	if(form_reg.phone.value == '') {
		setMsgDiv('note-phone', '您忘了填电话！');
		error = true;
	} 
	if(form_reg.phone.value != '') {
		var patrn=/^([0-9\-\.\s\(\)]+){5,22}$/i; 
		if (!patrn.exec(form_reg.phone.value)) {
			setMsgDiv('note-phone','填入的联系电话不正确！');
			error = true;
		}
	} 

	setMsgDiv('note-mobile', '');
	if(form_reg.mobile.value == '') {
		setMsgDiv('note-mobile', '您忘了填移动电话！');
		error = true;
	}
	if(form_reg.mobile.value != '') {
		var patrn=/^([0-9\-\.\s\(\)]+){5,22}$/i; 
		if (!patrn.exec(form_reg.mobile.value)) {
			setMsgDiv('note-mobile','填入的移动电话不正确！');
			error = true;
		}
	} 
	setMsgDiv('note-address', '');
	if(form_reg.address.value == '') {
		setMsgDiv('note-address', '您忘了填邮寄地址！');
		error = true;
	}
	setMsgDiv('note-postcode', '');
	if(form_reg.postcode.value == '') {
		setMsgDiv('note-postcode', '您忘了填邮寄地址邮编！');
		error = true;
	}
	setMsgDiv('note-validate', '');
	if(form_reg.validate.value == '') {
		setMsgDiv('note-validate', '您忘了填验证码！');
		error = true;
	}
	if(error) {
		return false;
	}
}

</script>
<form name="form_reg" action="register.php?switch=register" method="post" onsubmit="return checkdata()">
<div id="user_info">注册必填 * </div>
	<div>
	  <div id="retinput">* 填写您的称呼：</div>
	  	<div><input name="username" class="f" id="username" size="25" maxlength="50" />（填写您的姓名也可以）</div>
	  <div id="note-username"></div>
	  <div class="clear"></div>
	</div>
	<div>
	  <div id="retinput">* 填写您的接收信件的Email：</div>
	  <div><input name="email" class="f" id="email" size="25" maxlength="50" />（同时作为会员登录使用）</div>
	  <div class="clear"></div>
	  <div id="retinput">* 再次输入的您Email：</div>
	  <div><input name="reemail" id="reemail" size="25" maxlength="50" /></div>
	  <div id="note-email">{if checkemail=='no'}<font color="#FF0000">此Email已被人注册, 您忘记密码请点击这里找回密码！</font>{/if}</div>
	  <div class="clear"></div>
	</div>
	<div>
	  <div id="retinput">* 输入您的登录密码：</div>
	  <div> <input name="password" type="password" id="password" size="25" maxlength="50" />
	  </div>
	  <div class="clear"></div>
	  <div id="retinput">* 再次输入您的登录密码：</div>
	  <div><input name="repassword" type="password" id="repassword" size="25" maxlength="50" />
	  </div>
	  <div id="note-password"></div>
	  <div class="clear"></div>
	</div>
	<div id="user_info">联系资料</div>
	<div>
	  <div id="retinput">* 输入您的联系电话：</div>
	  <div><input name="phone" class="f" id="phone" size="25" maxlength="50" /></div>
	  <div id="note-phone"></div>
	  <div class="clear"></div>
	</div>
	<div>
	  <div id="retinput">* 输入您的移动电话：</div>
	  <div><input id="mobile" size="25" name="mobile" class="f" /></div>
	  <div id="note-mobile"></div>
	  <div class="clear"></div>
	</div>
	<div>
	  <div id="retinput">* 输入您的邮寄地址：</div>
	  <div><input name="address" class="f" id="address" size="25" maxlength="50" /></div>
	 <div id="note-address"></div>
	 <div class="clear"></div>
	</div>
	<div>
	  <div id="retinput">* 输入您的邮寄地址邮编：</div>
	  <div><input name="postcode" class="f" id="postcode" size="25" maxlength="50" /></div>
	  <div id="note-postcode"></div>
	  <div class="clear"></div>
	</div>
	<div>
	  <div id="retinput">* 输入验证码：</div>
	  <div><input name="validate" id="validate" size="4" maxlength="4" />
	  <a href="#y"><img class="f" src="validate.php" name="validate" border="0" align="absmiddle" id="imgvalidate" onclick="this.src='validate.php?u=' + Math.random()"/></a> <a href="#y" onclick="document.getElementById('imgvalidate').src='validate.php?u=' + Math.random()">看不清，点击这里更换验证码，不区分大小写</a>
	  <div id="note-validate"></div></div>
	  <div class="clear"></div>
	</div>
	<div id="user_info">注册选填</div>
	<div>
	  <div id="retinput"> 输入您的性别：</div>
	  <div><input name="sex" id="sex" size="25" maxlength="25" />
	  </div>
	  <div id="retinput"> 输入您的生日：</div>
	  <div><input name="birthday" id="birthday" size="25" maxlength="25" />
	  </div>
	  <div></div>
	</div>
	<div>
	  <div id="retinput"> 输入您的QQ：</div>
	  <div><input name="qq" id="qq" size="25" maxlength="25" />
	  </div>
	  <div id="retinput"> 输入您的MSN：</div>
	  <div><input name="msn" id="msn" size="25" maxlength="25" />
	  </div>
	  <div></div>
	</div>
	<div>
	  <div id="retinput"> 输入您的taobao旺旺：</div>
	  <div><input name="taobao" id="taobao" size="25" maxlength="25" />
	  </div>
	  <div id="retinput"> 输入您的Skype：</div>
	  <div><input name="skype" id="skype" size="25" maxlength="25" /></div>
	  <div></div>
	</div>
	<div class="clear"></div>
	<div id="user_info"></div>
	<div>
	  <div>　　　　　　　　　　<input name="remember" type="checkbox" value="yes" />记住我 <input value="清空重填" type="reset" /> <input name="" type="submit" value="确定提交" /></div>
	</div>
	<div class="clear"></div>
<div id="reg_condition"></div>
</form>
</div>