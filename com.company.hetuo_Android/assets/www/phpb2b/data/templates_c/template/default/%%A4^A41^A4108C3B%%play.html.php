<?php /* Smarty version 2.6.18, created on 2010-09-09 15:27:59
         compiled from D:%5CApache+Group%5CApache2%5Chtdocs%5Cphpb2b%5Cplugins%5Cvcastr%5Ctemplate%5Cplay.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('modifier', 'default', 'D:\\Apache Group\\Apache2\\htdocs\\phpb2b\\plugins\\vcastr\\template\\play.html', 2, false),)), $this); ?>
<div>
   <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="<?php echo ((is_array($_tmp=@$this->_tpl_vars['width'])) ? $this->_run_mod_handler('default', true, $_tmp, 410) : smarty_modifier_default($_tmp, 410)); ?>
" height="<?php echo ((is_array($_tmp=@$this->_tpl_vars['height'])) ? $this->_run_mod_handler('default', true, $_tmp, 190) : smarty_modifier_default($_tmp, 190)); ?>
">
   <param name="movie" value="<?php echo $this->_tpl_vars['movie']; ?>
?LogoText=<?php echo $this->_tpl_vars['flashtitle']; ?>
&IsAutoPlay=1&IsContinue=1&vcastr_xml=<?php echo $this->_tpl_vars['flashvars']; ?>
">
   <param name="quality" value="high">
   <param name="allowFullScreen" value="true" />
   <embed src="<?php echo $this->_tpl_vars['movie']; ?>
?LogoText=<?php echo $this->_tpl_vars['flashtitle']; ?>
&IsAutoPlay=1&IsContinue=1&vcastr_xml=<?php echo $this->_tpl_vars['flashvars']; ?>
" allowFullScreen="true" quality="high" type="application/x-shockwave-flash" width="<?php echo $this->_tpl_vars['width']; ?>
" height="<?php echo $this->_tpl_vars['height']; ?>
"></embed>
</object>
</div>