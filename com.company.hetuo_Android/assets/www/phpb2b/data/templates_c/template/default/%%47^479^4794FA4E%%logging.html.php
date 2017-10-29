<?php /* Smarty version 2.6.18, created on 2011-02-20 05:51:45
         compiled from default%5Clogging.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('modifier', 'default', 'default\\logging.html', 26, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_login'])); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<style type="text/css">  
label.error {
  font-weight: bold;
  color: #b80000;
}
</style>
<div class="wrapper">
    <div class="content">
        <div class="loading"><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/lhighs.gif" /><?php echo $this->_tpl_vars['_your_ongoing_operations']; ?>
<span><?php echo $this->_tpl_vars['_pls_login']; ?>
</span><?php echo $this->_tpl_vars['_continue']; ?>
</div>
        <div class="loadingcon">
        <form name="loggingfrm" id="LoggingFrm" method="post" action="logging.php">
        	<input type="hidden" name="action" value="logging">
			<input type="hidden" name="formhash" value="<?php echo $this->_tpl_vars['formhash']; ?>
">
			<input type="hidden" name="forward" value="<?php echo $_GET['forward']; ?>
" />
            <div class="loadingconleft">
                <p><?php echo $this->_tpl_vars['_member_login']; ?>
</p>
				<?php if ($this->_tpl_vars['LoginError']): ?><?php echo $this->_tpl_vars['LoginError']; ?>
<?php endif; ?>
				<br />
                    <label class="loadingname">
                        <?php echo $this->_tpl_vars['_user_name']; ?>

                    </label>
                    <label>
                        <?php if ($this->_tpl_vars['pb_username'] != ""): ?>
                        <input type="text" name="data[login_name]" id="LoginName" value="<?php echo ((is_array($_tmp=@$this->_tpl_vars['pb_username'])) ? $this->_run_mod_handler('default', true, $_tmp, '`$_account_n_email_n_mobile`') : smarty_modifier_default($_tmp, '`$_account_n_email_n_mobile`')); ?>
">
                        <?php else: ?>
                        <input type="text" style="color: #CCCCCC;" name="data[login_name]" id="LoginName" value="<?php echo $this->_tpl_vars['_account_n_email_n_mobile']; ?>
" tabindex="1">
                        <?php endif; ?>
                    </label>
                    <br clear="all" />
                    <label class="loadingname">
                    <?php echo $this->_tpl_vars['_password']; ?>

                    </label>
                    <label>
                        <input name="data[login_pass]" type="password" id="LoginPass" value="" tabindex="2">&nbsp;<a href="getpasswd.php"><?php echo $this->_tpl_vars['_forget_password']; ?>
</a>
                    </label>
                    <br clear="all" />
                    <?php if ($this->_tpl_vars['ifcapt']): ?>
                    <label class="loadingname">
                        <?php echo $this->_tpl_vars['_code']; ?>

                    </label>
                    <label class="loadingcheck">
                        <input name="data[capt_logging]" id="LoginAuth" type="text" value="" size="5" maxlength="5" tabindex="3"><a href="javascript:;" onclick="$('#imgcaptcha').attr('src','captcha.php?sid=' + Math.random());return false;"><img src="captcha.php?sid=<?php echo $this->_tpl_vars['sid']; ?>
" id="imgcaptcha" alt="<?php echo $this->_tpl_vars['_unclear_see_numbers']; ?>
" title="<?php echo $this->_tpl_vars['_unclear_see_numbers']; ?>
"/></a>
                    </label>
                    <div class="clear"></div>
                    <p class="loadingp1"><?php echo $this->_tpl_vars['_refresh_code']; ?>
</p>
                    <?php endif; ?>
                    <div class="clear"></div>
                    <div class="login" id="btnLoginDiv">
                       <input type="submit" name="log_in" id="LogIn" value="<?php echo $this->_tpl_vars['_login']; ?>
" class="loadingbutton" />
                       <input type="reset" name="reset" value="<?php echo $this->_tpl_vars['_reset']; ?>
" class="loadingbutton"/>
                       </div>
                    <div class="clear"></div>
                    <p class="loadingp3"><?php echo $this->_tpl_vars['_questions_service_hotline']; ?>
<?php echo $this->_tpl_vars['service_tel']; ?>
</p>
            </div>
            <div class="loadingconright">
                <p><?php echo $this->_tpl_vars['_free_register']; ?>
</p>
                <ul>
                    <li><a><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/logging_01.gif" alt="" /><p><b><?php echo $this->_tpl_vars['_products_show']; ?>
</b><br /><?php echo $this->_tpl_vars['_illustrated_display_product']; ?>
</p></a></li>
                    <li><a><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/logging_02.gif" alt="" /><p><b><?php echo $this->_tpl_vars['_online_business']; ?>
</b><br /><?php echo $this->_tpl_vars['_internet_power_promotion']; ?>
</p></a></li>
                    <li><a><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/logging_03.gif" alt="" /><p><b><?php echo $this->_tpl_vars['_hr']; ?>
</b><br /><?php echo $this->_tpl_vars['_talents_join']; ?>
</p></a></li>
                    <li><a><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/logging_04.gif" alt="" /><p><b><?php echo $this->_tpl_vars['_agent_join']; ?>
</b><br /><?php echo $this->_tpl_vars['_development_agent_join']; ?>
</p></a></li>
                    <li><a><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/logging_05.gif" alt="" /><p><b><?php echo $this->_tpl_vars['_company_news']; ?>
</b><br /><?php echo $this->_tpl_vars['_display_company_news']; ?>
</p></a></li>
                    <li><a><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/logging_06.gif" alt="" /><p><b><?php echo $this->_tpl_vars['_classifieds']; ?>
</b><br /><?php echo $this->_tpl_vars['_free_classifieds']; ?>
</p></a></li>
                </ul>
                <div class="member_reg">
                       <input type="button" value="<?php echo $this->_tpl_vars['_register']; ?>
" class="loadfreeregister" onclick="redirect('member.php');return false;"/>
               </div>
            </div>
        </form>
        </div>
    </div>
</div>
<script language="javascript" src="scripts/jquery/validate.js"></script>
<script language="javascript" src="scripts/validate.js" charset="<?php echo $this->_tpl_vars['Charset']; ?>
"></script>
<script>
$(document).ready(function(){
	$("#LoginName").focus(function(){
		if($("#LoginName").val()=='<?php echo $this->_tpl_vars['_account_n_email_n_mobile']; ?>
'){
			$("#LoginName").val('').css('color', '#000');
		};
	}).blur(function(){
		if($("#LoginName").val()==''){
		$("#LoginName").val("<?php echo $this->_tpl_vars['_account_n_email_n_mobile']; ?>
").css("color","#ccc")};
	});
});
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>