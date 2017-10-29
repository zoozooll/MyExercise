<?php /* Smarty version 2.6.18, created on 2011-02-20 05:52:10
         compiled from setting.cache.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('function', 'html_radios', 'setting.cache.html', 14, false),array('modifier', 'default', 'setting.cache.html', 14, false),)), $this); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<div id="currentPosition">
	<p><?php echo $this->_tpl_vars['_your_current_position']; ?>
 <?php echo $this->_tpl_vars['_setting_global']; ?>
 &raquo; <?php echo $this->_tpl_vars['_cache_setting']; ?>
</p>
</div>
<div id="rightTop"> 
    <h3><?php echo $this->_tpl_vars['_cache_setting']; ?>
</h3> 
</div>
<div class="info"> 
  <form action="setting.php" method="post" id="EditFrm" name="edit_frm">
  <input type="hidden" name="do" value="functions">
    <table class="infoTable">
      <tr>
        <th class="paddingT15"> 主要页面缓存：</th>
        <td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][main_cache]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['MAIN_CACHE'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)),'separator' => ""), $this);?>
</td>
      </tr>
      <tr>
        <th class="paddingT15"> 商务室数据缓存：</th>
        <td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][member_cache]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['MEMBER_CACHE'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)),'separator' => ""), $this);?>
</td>
      </tr>
      <tr>
        <th class="paddingT15"> 企业空间数据缓存：</th>
        <td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][space_cache]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['SPACE_CACHE'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)),'separator' => ""), $this);?>
</td>
      </tr>
      <tr>
        <th class="paddingT15"> 标签调用数据缓存：</th>
        <td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][label_cache]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['LABEL_CACHE'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)),'separator' => ""), $this);?>
</td>
      </tr>
      <tr>
        <th class="paddingT15"> 缓存时间：</th>
        <td class="paddingT15 wordSpacing5"><input name="data[setting][main_cache_lifetime]" type="text" id="DateFormat" value="<?php echo ((is_array($_tmp=@$this->_tpl_vars['item']['MAIN_CACHE_LIFETIME'])) ? $this->_run_mod_handler('default', true, $_tmp, 3600) : smarty_modifier_default($_tmp, 3600)); ?>
"/>
          <span class="gray">单位为秒，默认是3600秒，即一小时，0表示不缓存</span></td>
      </tr>
      <tr>
        <th class="paddingT15"> 是否检查模板变动：</th>
        <td class="paddingT15 wordSpacing5"><?php echo smarty_function_html_radios(array('name' => "data[setting][main_cache_check]",'options' => $this->_tpl_vars['AskAction'],'checked' => ((is_array($_tmp=@$this->_tpl_vars['item']['MAIN_CACHE_CHECK'])) ? $this->_run_mod_handler('default', true, $_tmp, 0) : smarty_modifier_default($_tmp, 0)),'separator' => ""), $this);?>
<span class="gray">如果开启，则当模板变动时，系统自动<?php echo $this->_tpl_vars['_update_cache']; ?>
，默认不检查</span></td>
      </tr>
      <tr>
        <th></th>
        <td class="ptb20">
			<input class="formbtn" type="submit" name="save_cache" value="<?php echo $this->_tpl_vars['_save_setting']; ?>
" />		</td>
      </tr>
    </table>
  </form>
</div> 
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>