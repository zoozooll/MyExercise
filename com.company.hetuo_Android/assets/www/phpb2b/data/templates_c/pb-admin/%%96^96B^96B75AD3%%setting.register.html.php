<?php /* Smarty version 2.6.18, created on 2011-02-20 05:52:10
         compiled from setting.register.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('modifier', 'default', 'setting.register.html', 51, false),array('function', 'editor', 'setting.register.html', 84, false),)), $this); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<div id="currentPosition">
	<p><?php echo $this->_tpl_vars['_your_current_position']; ?>
 <?php echo $this->_tpl_vars['_setting_global']; ?>
 &raquo; <?php echo $this->_tpl_vars['_reg_and_visit']; ?>
</p>
</div>
<div id="rightTop"> 
    <h3><?php echo $this->_tpl_vars['_reg_and_visit']; ?>
</h3>
    <ul class="subnav">
		<li><a href="setting.php?do=register" class="btn1"><span>注册</span></a></li>
        <li><a href="setting.php?do=registerfile">默认设置</a></li>
    </ul>
</div>
<div class="info"> 
    <form method="post" name="edit_frm" action="setting.php"> 
    <input type="hidden" name="data[reg_filename]" value="<?php echo $this->_tpl_vars['item']['REG_FILENAME']; ?>
" />
    <input type="hidden" name="data[post_filename]" value="<?php echo $this->_tpl_vars['item']['POST_FILENAME']; ?>
" />
        <table class="infoTable"> 
         	<tr> 
                <th class="paddingT15">允许新用户注册:</th> 
              	<td class="paddingT15 wordSpacing5">
                <label for="close_register"><input type="radio" name="data[setting][register_type]" id="close_register" value="close_register" onclick="$('#tbRegItem').hide();" />关闭注册</label>
  <br />
  <label for="open_common_reg"><input type="radio" name="data[setting][register_type]" id="open_common_reg" value="open_common_reg" onclick="$('#tbRegItem').show();" />开放普通注册</label>
  <br />
  <label for="open_invite_reg"><input type="radio" name="data[setting][register_type]" id="open_invite_reg" value="open_invite_reg" onclick="$('#tbRegItem').show();" />开放邀请注册</label></td> 
          	</tr> 
            <tbody id="tbRegItem" style="display: none">
			<tr> 
                <th class="paddingT15">注册协议:</th> 
       			<td class="paddingT15 wordSpacing5"><textarea name="data[setting1][agreement]" style="width:600px;" class="mceEditor"><?php echo $this->_tpl_vars['item']['agreement']; ?>
</textarea><label class="field_notice">可以用参数%site_name%表示网站名称,%site_url%表示网站地址</label></td> 
          	</tr> 
         	<tr> 
                <th class="paddingT15">新用户注册必须审核:</th> 
              	<td class="paddingT15 wordSpacing5">
                  <select name="data[setting][new_userauth]" id="NewUserauth">
                  <option value="0" <?php if ($this->_tpl_vars['item']['NEW_USERAUTH'] == 0): ?>selected="selected"<?php endif; ?>>无</option>
                  <option value="1" <?php if ($this->_tpl_vars['item']['NEW_USERAUTH'] == 1): ?>selected="selected"<?php endif; ?>>Email验证</option>
                  <option value="2" <?php if ($this->_tpl_vars['item']['NEW_USERAUTH'] == 2): ?>selected="selected"<?php endif; ?>>人工审核</option>
                  </select>
                </td> 
          	</tr> 
			<tr> 
                <th class="paddingT15">禁止注册IP地址:</th> 
       			<td class="paddingT15 wordSpacing5"><textarea name="data[forbid_ip]"><?php echo $this->_tpl_vars['item']['forbid_ip']; ?>
</textarea></td> 
          	</tr> 
			<tr> 
                <th class="paddingT15">关键字屏蔽:</th> 
       			<td class="paddingT15 wordSpacing5"><textarea name="data[forbid_word]"><?php echo $this->_tpl_vars['item']['forbid_word']; ?>
</textarea></td> 
          	</tr> 
         	<tr> 
                <th class="paddingT15">同一 IP 注册间隔限制:</th> 
              	<td class="paddingT15 wordSpacing5"><input type="text" name="data[setting][ip_reg_sep]" id="IP_REG_SEP" value="<?php echo ((is_array($_tmp=@$this->_tpl_vars['item']['IP_REG_SEP'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)); ?>
" class="infoTableInput" /><label class="field_notice">单位每小时，0为不限制</label></td> 
          	</tr>
         	<tr> 
                <th class="paddingT15">发送欢迎信息：</th> 
              	<td class="paddingT15 wordSpacing5">
				  <label for="welcome_msg0"><input type="radio" name="data[setting1][welcome_msg]" id="welcome_msg0" value="0" onclick="$('#tbWelcomeMsg').hide();" />不发送</label>
				  <br />
				  <label for="welcome_msg1"><input type="radio" name="data[setting1][welcome_msg]" id="welcome_msg1" value="1" onclick="$('#tbWelcomeMsg').show();" />发送欢迎<?php echo $this->_tpl_vars['_pms']; ?>
</label>
				  <br />
				  <label for="welcome_msg2"><input type="radio" name="data[setting1][welcome_msg]" id="welcome_msg2" value="2" onclick="$('#tbWelcomeMsg').hide();" />发送欢迎 Email</label>
				</td> 
          	</tr> 
            <tbody id="tbWelcomeMsg" style="display: none">
         	<tr> 
                <th class="paddingT15">欢迎信息标题：</th> 
              	<td class="paddingT15 wordSpacing5"><input type="text" name="data[welcome_msg_title]" id="welcome_msg_title" value="<?php echo ((is_array($_tmp=@$this->_tpl_vars['item']['WELCOME_MSG_TITLE'])) ? $this->_run_mod_handler('default', true, $_tmp, ($this->_tpl_vars['_welcome_title'])) : smarty_modifier_default($_tmp, ($this->_tpl_vars['_welcome_title']))); ?>
" class="infoTableInput" /></td> 
          	</tr> 
         	<tr> 
                <th class="paddingT15">欢迎信息内容：</th> 
              	<td class="paddingT15 wordSpacing5"><textarea name="data[welcome_msg_content]" style="width:600px;" class="mceEditor"><?php echo ((is_array($_tmp=@$this->_tpl_vars['item']['WELCOME_MSG_CONTENT'])) ? $this->_run_mod_handler('default', true, $_tmp, ($this->_tpl_vars['_welcome_content'])) : smarty_modifier_default($_tmp, ($this->_tpl_vars['_welcome_content']))); ?>
</textarea></td> 
          	</tr> 
            </tbody>
            </tbody>
            <tr> 
            <th></th> 
            <td class="ptb20"> 
                <input class="formbtn" type="submit" name="saveregister" value="<?php echo $this->_tpl_vars['_submit']; ?>
" /> 
                <input class="formbtn" type="reset" name="reset" value="<?php echo $this->_tpl_vars['_reset']; ?>
" /> 
            </td> 
        </tr> 
        </table> 
    </form> 
</div> 
<?php echo smarty_function_editor(array('type' => 'tiny_mce','mode' => 'specific_textareas'), $this);?>

<script>
var register_type = "<?php echo ((is_array($_tmp=@$this->_tpl_vars['item']['REGISTER_TYPE'])) ? $this->_run_mod_handler('default', true, $_tmp, 'open_common_reg') : smarty_modifier_default($_tmp, 'open_common_reg')); ?>
";
var welcome_msg = "<?php echo ((is_array($_tmp=@$this->_tpl_vars['item']['WELCOME_MSG'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)); ?>
";
jQuery(document).ready(function($) {
	if(register_type=="close_register"){
		$("#close_register").attr("checked","checked");
	}else if(register_type=="open_common_reg"){
		$("#open_common_reg").attr("checked", "checked");
		$("#tbRegItem").show();
	}else if(register_type=="open_invite_reg"){
		$("#open_invite_reg").attr("checked", "checked");
		$("#tbRegItem").show();
	}
	if(welcome_msg=="1"){
		$("#welcome_msg1").attr("checked","checked");
		$("#tbWelcomeMsg").show();
	}else if(welcome_msg=="2"){
		$("#welcome_msg2").attr("checked","checked");
		$("#tbWelcomeMsg").show();
	}else{
		$("#welcome_msg0").attr("checked", "checked");
	}
})
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>