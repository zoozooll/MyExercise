<?php /* Smarty version 2.6.18, created on 2011-02-20 05:52:12
         compiled from setting.auth.secure.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('function', 'html_radios', 'setting.auth.secure.html', 21, false),array('modifier', 'default', 'setting.auth.secure.html', 21, false),)), $this); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<div id="currentPosition">
	<p><?php echo $this->_tpl_vars['_your_current_position']; ?>
 <?php echo $this->_tpl_vars['_setting_global']; ?>
 &raquo; 安全验证</p>
</div>
<div id="rightTop"> 
    <h3>安全验证</h3> 
    <ul class="subnav">
        <li><a href="setting.php?do=secure" class="btn1"><span>安全</span></a></li>
		<li><a href="setting.php?do=auth">验证码</a></li>
    </ul>
</div>
<div class="info"> 
    <form method="post" action="setting.php" name="edit_frm"> 
        <table class="infoTable"> 
         	<tr> 
                <th class="paddingT15">网站安全密钥：</th> 
              	<td class="paddingT15 wordSpacing5"><input name="data[setting][auth_key]" value="<?php echo $this->_tpl_vars['item']['AUTH_KEY']; ?>
" /><label class="field_notice">用于识别网站身份的唯一字符串</label></td> 
          	</tr> 
			<tr> 
                <th class="paddingT15">n允许游客发布供求:</th> 
       			<td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][vis_post]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['VIS_POST'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)),'separator' => ""), $this);?>
</td> 
          	</tr> 
			<tr> 
                <th class="paddingT15">游客发布供求审核：</th> 
       			<td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][vis_post_check]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['VIS_POST_CHECK'])) ? $this->_run_mod_handler('default', true, $_tmp, 1) : smarty_modifier_default($_tmp, 1)),'separator' => ""), $this);?>
</td> 
          	</tr> 
			<tr> 
                <th class="paddingT15">开放供应信息：</th> 
       			<td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][sell_logincheck]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['SELL_LOGINCHECK'])) ? $this->_run_mod_handler('default', true, $_tmp, 1) : smarty_modifier_default($_tmp, 1)),'separator' => ""), $this);?>
</td> 
          	</tr> 
			<tr> 
                <th class="paddingT15">开放求购信息：</th> 
       			<td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][buy_logincheck]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['BUY_LOGINCHECK'])) ? $this->_run_mod_handler('default', true, $_tmp, 1) : smarty_modifier_default($_tmp, 1)),'separator' => ""), $this);?>
</td> 
          	</tr> 
         	<tr> 
                <th class="paddingT15">启用本地session.save_path：</th> 
              	<td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][session_savepath]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['SESSION_SAVEPATH'])) ? $this->_run_mod_handler('default', true, $_tmp, 1) : smarty_modifier_default($_tmp, 1)),'separator' => ""), $this);?>
<label class="field_notice">默认为系统定义</label></td> 
          	</tr> 
            <tr> 
            <th></th> 
            <td class="ptb20"> 
                <input class="formbtn" type="submit" name="saveauth" value="<?php echo $this->_tpl_vars['_submit']; ?>
" /> 
            </td> 
        </tr> 
        </table> 
    </form> 
</div> 
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>