<?php /* Smarty version 2.6.18, created on 2010-09-09 14:40:35
         compiled from default/header.html */

$this->_cache_serials['D:\Apache Group\Apache2\htdocs\phpb2b\data\templates_c\template\default\\%%81^81F^81F6D955%%header.html.inc'] = 'c586896f31925ee1a5f1a94b12945505';

?><?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'cacheless', 'default/header.html', 43, false),array('function', 'category', 'default/header.html', 60, false),array('modifier', 'default', 'default/header.html', 80, false),)), $this); ?>
<?php
function _smarty_tplfunc_c586896f31925ee1a5f1a94b12945505_0(&$_smarty)
{
$_smarty->_tag_stack[] = array('cacheless', array()); $_block_repeat=true;smarty_block_cacheless($_smarty->_tag_stack[count($_smarty->_tag_stack)-1][1], null, $_smarty, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
    <p class="headtopcon">
        <a class="index" href="javascript:;" onclick="this.style.behavior='url(#default#homepage)';this.setHomePage('<?php echo $_smarty->_tpl_vars['SiteUrl']; ?>
');"><?php echo $_smarty->_tpl_vars['_set_home_page']; ?>
</a>
        <a href="javascript: myAddPanel(document.title,location.href,document.title);" rel="sidebar" title="<?php echo $_smarty->_tpl_vars['site_name']; ?>
"><?php echo $_smarty->_tpl_vars['_add_favorite']; ?>
</a>
        <?php if ($_smarty->_tpl_vars['pb_username'] != ""): ?>
        <em><?php echo $_smarty->_tpl_vars['_hello']; ?>
<?php echo $_smarty->_tpl_vars['pb_username']; ?>
</em>
        <a href="redirect.php?url=office-room">[<?php echo $_smarty->_tpl_vars['_my_office_room']; ?>
]</a>
        <?php if ($_smarty->_tpl_vars['is_admin']): ?><a  href="pb-admin/login.php" target="_blank">[<?php echo $_smarty->_tpl_vars['_control_pannel']; ?>
]</a><?php endif; ?>
        <a href="logging.php?action=logout">[<?php echo $_smarty->_tpl_vars['_login_out']; ?>
]</a>
        <?php else: ?>
        <em><?php echo $_smarty->_tpl_vars['_hello_welcome_to']; ?>
<?php echo $_smarty->_tpl_vars['site_name']; ?>
</em>
        <a href="logging.php">&nbsp;[<?php echo $_smarty->_tpl_vars['_pls_login']; ?>
]</a>
        <a href="member.php" title="<?php echo $_smarty->_tpl_vars['_register']; ?>
" ><strong>[<?php echo $_smarty->_tpl_vars['_free']; ?>
<?php echo $_smarty->_tpl_vars['_register']; ?>
]</strong></a>
        <?php endif; ?>
        <a href="service.php"  style="float:right;"><?php echo $_smarty->_tpl_vars['_customer_service']; ?>
</a>
        <a href="offer/post.php" style="float:right;"><?php echo $_smarty->_tpl_vars['_free_post']; ?>
</a>
        <a href="standard.php" style="float:right;"><?php echo $_smarty->_tpl_vars['_standards']; ?>
</a>
        <?php echo smarty_function_category(array('type' => 'language'), $_smarty);?>

    </p>
    <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_cacheless($_smarty->_tag_stack[count($_smarty->_tag_stack)-1][1], $_block_content, $_smarty, $_block_repeat); }  array_pop($_smarty->_tag_stack); 
}



?>
