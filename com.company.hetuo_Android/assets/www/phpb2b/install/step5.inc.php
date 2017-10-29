<?php include 'header.share.php';?>
<div class="content">
<form id="install" name="myform" action="install.php?step=6" method="post">
<input type="hidden" name="step" value="6">
<table width="100%" cellspacing="1" cellpadding="0" >
<caption><?php echo $_input_db_info;?></caption>
<tr>
<th width="30%" align="right" ><?php echo $_db_host;?> : </th>
<td><label>
  <input name="dbhost" type="text" id="dbhost" value="<?php echo $dbhost;?>" style="width:120px" />
</label></td>
</tr>
<tr>
<th align="right"><?php echo $_db_username;?> : </th>
<td><input name="dbuser" type="text" id="dbuser" value="<?php echo $dbuser;?>" style="width:120px" /></td>
</tr>
<tr>
<th align="right"><?php echo $_db_passwd;?> : </th>
<td><input name="dbpw" type="password" id="dbpw" value="<?php echo $dbpasswd;?>" style="width:120px" /></td>
</tr>
<tr>
<th align="right"><?php echo $_db_name;?> : </th>
<td><input name="dbname" type="text" id="dbname" value="<?php echo $dbname;?>" style="width:120px" />
        <?php echo $_if_not_exist_then;?> : <label for="db_create1"><input name="db[create]" type="radio" id="db_create1" checked="checked" value="1" /><?php echo $_create_new_db;?></label><label for="db_create2"><input name="db[create]" type="radio" id="db_create2" value="2" /><?php echo $_not_install;?></label></td>
</tr>
<tr>
<th align="right"><?php echo $_table_prefix;?> : </th>
<td><input name="tablepre" type="text" id="tablepre" value="<?php echo $tb_prefix;?>" style="width:120px" />  <img src="images/help.gif" style="cursor:pointer;" title="<?php echo $_if_multi_db;?>" align="absmiddle" />
<span id='helptablepre'></span></td>
</tr>
</table>

<table width="100%" cellspacing="1" cellpadding="0">
<caption><?php echo $_input_admin_info;?></caption>
  <tr>
	<th width="30%" align="right"><?php echo $_admin_name;?> : </th>
	<td><input name="username" type="text" id="username" value="admin" style="width:120px" /> <?php echo $_between_two_and_twenty;?><font color="FFFF00">(<?php echo $_set_default;?> admin)</font></td>
  </tr>
  <tr>
	<th align="right"><?php echo $_password;?> : </th>
	<td><input name="password" type="text" id="password" value="" style="width:120px" /> <?php echo $_between_three_and_twenty;?><font color="FFFF00">(<?php echo $_set_default;?> admin&nbsp;<a href="javascript:;" onclick="$('#password').val(suggestPassword());"><img src="images/auth.gif" border="0" /></a>)</font></td>
  </tr>
  <tr>
	<th align="right"><?php echo $_check_password;?> : </th>
	<td><input name="pwdconfirm" type="text" id="pwdconfirm" value="" style="width:120px"/></td>
  </tr>
  <tr>
	<th align="right">E-mail : </th>
	<td><input name="email" type="text" id="email" value="<?php echo $admin_email;?>" style="width:120px"/>
		</td>
  </tr>
   <tr>
	<th align="right"><?php echo $_auth_key;?> : </th>
	<td><input name="password_key" type="text" id="password_key" value="" style="width:120px"/><font color="FFFF00">(<?php echo $_auth_key_and_auto_create;?>&nbsp;<a href="javascript:;" onclick="$('#password_key').val(suggestPassword());"><img src="images/auth.gif" border="0" /></a>)</font>
	</td>
  </tr>
</table>
</form>
<a href="javascript:history.go(-1);" class="btn"><?php echo $_go_back;?> : <?php echo $steps[--$step];?></a>
<input type="button" name="completeInstall" onclick="checkform()" class="btn" value="<?php echo $_next_step;?> : <?php echo $_site_info_setting;?>" />
</div>
</div>
</div>
</body>
</html>