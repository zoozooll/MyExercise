<?php /* Smarty version 2.6.18, created on 2010-09-09 15:27:59
         compiled from default%5Ccompany.index.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'company', 'default\\company.index.html', 25, false),array('block', 'area', 'default\\company.index.html', 41, false),array('function', 'plugin', 'default\\company.index.html', 61, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_yellow_page'])); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<div class="wrapper">
<div class="content">
<div class="company_list_left fl">
<div class="co_store">
    <div class="title_bar_s1">
    <span class="title_top_s1"><span></span></span>
    <h3><?php echo $this->_tpl_vars['_yellow_page']; ?>
</h3>
    </div>
</div>
<div class="co_store_c">
	<?php echo $this->_tpl_vars['_find_marketing_company']; ?>

	<p><a href="register.php?type=company" class="submit_w118"><?php echo $this->_tpl_vars['_add_company']; ?>
</a></p>
	<p class="hotline"><?php echo $this->_tpl_vars['_hotline']; ?>
<?php echo $this->_tpl_vars['service_tel']; ?>
</p>
</div>
<div class="co_store co_good">
    <div class="title_bar_s1">
    <span class="title_top_s1"><span></span></span>
    <h3><?php echo $this->_tpl_vars['_new_recommended_company']; ?>
</h3>
    </div>
</div>
<div class="co_store_c">
	<ul>
    	<?php $this->_tag_stack[] = array('company', array('row' => 11)); $_block_repeat=true;smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
		<li><a href="[link:title]" title="[field:title]">[field:title]</a></li>
        <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
	</ul>
</div>
<div class="blank6"></div>
<div class="fincco_ad"><a href="http://www.phpb2b.com/" target="_blank"><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/phpb2b_dev.jpg" /></a></div>
<div class="blank6"></div>
<div class="co_store">
    <div class="title_bar_s1">
    <span class="title_top_s1"><span></span></span>
    <h3><?php echo $this->_tpl_vars['_sub_area']; ?>
</h3>
    </div>
</div>
<div class="co_store_c area_subsite clearfix">
	<ul>
    <?php $this->_tag_stack[] = array('area', array('level' => 1)); $_block_repeat=true;smarty_block_area($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
		<li><a href="special/area.php?id=[field:id]">[field:title]</a></li>
    <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_area($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
	</ul>
</div>
<div class="co_store co_good">
    <div class="title_bar_s1">
    <span class="title_top_s1"><span></span></span>
    <h3><?php echo $this->_tpl_vars['_join_company']; ?>
</h3>
    </div>
</div>
<div class="co_store_c">
	<ul>
    	<?php $this->_tag_stack[] = array('company', array('row' => 11)); $_block_repeat=true;smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
		<li><a href="[link:title]" title="[field:title]">[field:title]</a></li>
        <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
	</ul>
</div>
</div>
<div class="company_list_right fr">
<div class="company_list_focus fl"><?php echo smarty_function_plugin(array('name' => 'vcastr'), $this);?>
</div>
<div class="co_add fl">
	<div class="co_store">
        <div class="title_bar_s1">
    <span class="title_top_s1"><span></span></span>
    <h3><?php echo $this->_tpl_vars['_add_yellow_page']; ?>
</h3>
    </div>
	</div>
	<div class="co_store_c">
		<table width="275" border="0" cellspacing="0" cellpadding="0">
			<?php if ($this->_tpl_vars['pb_username'] != ""): ?>
			  <tr>
				<td>&nbsp;</td>
				<td colspan="2"><p class="co_tg fl"><a href="redirect.php?url=office-room/company" class="submit_w67"><?php echo $this->_tpl_vars['_promotion_company']; ?>
</a></p><p class="co_fb fl"><a href="redirect.php?url=office-room/product" class="submit_w67"><?php echo $this->_tpl_vars['_release_product']; ?>
</a></p></td>
			  </tr>
			<?php else: ?>
				<form name="loggingfrm" id="LoggingFrm" method="post" action="logging.php">
					<input type="hidden" name="action" value="logging">
					<input type="hidden" name="formhash" value="<?php echo $this->_tpl_vars['formhash']; ?>
">
					<input type="hidden" name="forward" value="<?php echo $_GET['forward']; ?>
" />
				  <tr>
					<td align="left" width="60"><?php echo $this->_tpl_vars['_user_name']; ?>
</td>
					<td align="left" width="150"><input type="text" name="data[login_name]" id="LoginName" tabindex="1" class="co_add_in" /></td>
					<td width="65" rowspan="2" align="center"><input type="submit" value="<?php echo $this->_tpl_vars['_login']; ?>
" class="co_login_but company_login_color" /></td>
				  </tr>
				  <tr>
					<td align="left"><?php echo $this->_tpl_vars['_password']; ?>
</td>
					<td align="left"><input name="data[login_pass]" type="password" id="LoginPass" tabindex="2" class="co_add_in" /></td>
				  </tr>
				</form>
			  <tr>
				<td>&nbsp;</td>
				<td colspan="2" align="left"><p class="co_reg"><a href="member.php" class="submit_w165"><?php echo $this->_tpl_vars['_logon_deal']; ?>
</a></p></td>
			  </tr>
			<?php endif; ?>
		</table>
	</div>
</div>
<div class="co_type fl">
    <div class="title_bar_s1" style="height:28px; overflow:hidden">
         <span class="title_top_s1"><span></span></span>
         <h4><span class="titlebg_l"></span><span class="titlebg_m title_mouse"><?php echo $this->_tpl_vars['_industry_sort']; ?>
</span><span class="titlebg_r"></span></h4>
    </div>
	<div class="co_type_c">
	    <?php $_from = $this->_tpl_vars['IndustryList']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_0'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_0']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key0'] => $this->_tpl_vars['item0']):
        $this->_foreach['level_0']['iteration']++;
?>
		<div class="co_type_c_title clearfix">
		<span class="more"><a href="company/list.php?industryid=<?php echo $this->_tpl_vars['item0']['id']; ?>
"><?php echo $this->_tpl_vars['_more']; ?>
</a></span>
		<h3><em><a href="company/list.php?industryid=<?php echo $this->_tpl_vars['item0']['id']; ?>
"><?php echo $this->_tpl_vars['item0']['name']; ?>
</a></em></h3>
		</div>
		<ul class="clearfix">
        <?php if ($this->_tpl_vars['item0']['sub']): ?>
        <?php $_from = $this->_tpl_vars['item0']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_1_industry'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_1_industry']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key_level1'] => $this->_tpl_vars['level1']):
        $this->_foreach['level_1_industry']['iteration']++;
?>
		
		   <li>
			<a href="company/list.php?industryid=<?php echo $this->_tpl_vars['level1']['id']; ?>
" title="<?php echo $this->_tpl_vars['level1']['name']; ?>
" class="co_type_f"><?php echo $this->_tpl_vars['level1']['name']; ?>
</a>
            <br />
            <?php $_from = $this->_tpl_vars['level1']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_2_industry'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_2_industry']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key_level2'] => $this->_tpl_vars['level2']):
        $this->_foreach['level_2_industry']['iteration']++;
?>
			<a href="company/list.php?industryid=<?php echo $this->_tpl_vars['level2']['id']; ?>
" title="<?php echo $this->_tpl_vars['level2']['name']; ?>
"><?php echo $this->_tpl_vars['level2']['name']; ?>
</a><em>|</em>
            <?php endforeach; endif; unset($_from); ?>
		</li>
		
        <?php endforeach; endif; unset($_from); ?>
        <?php endif; ?>
		</ul>
        <?php endforeach; endif; unset($_from); ?>
		<div class="cb"></div>
	</div>
</div>
</div>
<div class="blank6"></div>
<script>
$("#SearchFrm").attr("action","company/list.php");
$("#topMenuCompany").addClass("lcur");
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>