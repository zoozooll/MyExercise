<?php /* Smarty version 2.6.18, created on 2010-09-09 14:40:35
         compiled from default/footer.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'userpage', 'default/footer.html', 3, false),array('function', 'mailto', 'default/footer.html', 6, false),array('function', 'im', 'default/footer.html', 8, false),)), $this); ?>
<div id="footer">
	<div class="ins"> 
		<?php $this->_tag_stack[] = array('userpage', array('row' => 15)); $_block_repeat=true;smarty_block_userpage($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
			<a href="[link:title]" title="[field:title]">[field:title]</a><span>|</span>
		<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_userpage($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
		<p><?php echo $this->_tpl_vars['site_name']; ?>
<?php echo $this->_tpl_vars['_copyright']; ?>
&copy;&nbsp;<?php echo $this->_tpl_vars['_service_hotline']; ?>
<?php echo $this->_tpl_vars['service_tel']; ?>
&nbsp;<?php echo $this->_tpl_vars['_service_email']; ?>
:<?php echo smarty_function_mailto(array('text' => ($this->_tpl_vars['service_email']),'address' => ($this->_tpl_vars['service_email']),'encode' => 'javascript'), $this);?>
&nbsp;<a href="javascript:;" onclick="$('html, body').animate({scrollTop: '0px'}, 300);return false;"><?php echo $this->_tpl_vars['_go_top']; ?>
</a>     </p>
		<p><?php echo $this->_tpl_vars['icp_number']; ?>
</p>
		<p class="footer_im"><?php echo smarty_function_im(array('type' => 'qq','id' => ($this->_tpl_vars['service_qq'])), $this);?>
<?php echo smarty_function_im(array('type' => 'msn','id' => ($this->_tpl_vars['service_msn'])), $this);?>
</p>
		<p>Powered by <?php echo $this->_tpl_vars['_soft_name']; ?>
 &copy;2007-2010 <a href="http://www.phpb2b.com/" target="_blank"><strong>Ualink</strong></a> Inc.</p>
	</div>
</div>
</body>
</html>