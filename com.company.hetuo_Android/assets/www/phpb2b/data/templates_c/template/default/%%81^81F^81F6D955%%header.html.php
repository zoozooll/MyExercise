<?php /* Smarty version 2.6.18, created on 2010-09-09 14:40:35
         compiled from default/header.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'cacheless', 'default/header.html', 43, false),array('function', 'category', 'default/header.html', 60, false),array('modifier', 'default', 'default/header.html', 80, false),)), $this); ?>
<?php $this->_cache_serials['D:\Apache Group\Apache2\htdocs\phpb2b\data\templates_c\template\default\\%%81^81F^81F6D955%%header.html.inc'] = 'c586896f31925ee1a5f1a94b12945505'; ?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=<?php echo $this->_tpl_vars['Charset']; ?>
" />
<title><?php echo $this->_tpl_vars['page_title']; ?>
 - <?php echo $this->_tpl_vars['site_title']; ?>
</title>
<base href="<?php echo $this->_tpl_vars['SiteUrl']; ?>
">
<meta name ="keywords" content="<?php echo $this->_tpl_vars['metakeywords']; ?>
">
<meta name ="description" content="<?php echo $this->_tpl_vars['metadescription']; ?>
"> 
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<link rel="stylesheet" type="text/css" href="<?php echo $this->_tpl_vars['theme_img_path']; ?>
base.css" />
<link rel="stylesheet" type="text/css" href="<?php echo $this->_tpl_vars['theme_img_path']; ?>
general.css" />
<link rel="stylesheet" type="text/css" href="<?php echo $this->_tpl_vars['theme_style_path']; ?>
style.css" />
<link rel="stylesheet" type="text/css" href="<?php echo $this->_tpl_vars['theme_img_path']; ?>
layout.css" />
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="shorcut icon" type="image/x-ico" href="favicon.ico" />
<link rel="bookmark" type="image/x-ico" href="favicon.ico" />
<script src="scripts/jquery.js"></script>
<script src="scripts/general.js" charset="<?php echo $this->_tpl_vars['Charset']; ?>
"></script>
<script language="javascript">
<!--
$(document).ready(function() {
	if($("#mn_<?php echo $this->_tpl_vars['nav_id']; ?>
").length>0){
		$("#mn_<?php echo $this->_tpl_vars['nav_id']; ?>
").addClass("current_nav");
	}
	$("#SearchKeyword").focus(function(){
		if($("#SearchKeyword").val()=='<?php echo $this->_tpl_vars['_keywords_sample']; ?>
'){
			$("#SearchKeyword").val("").css('color', '#000000')
			};
		}).blur(function(){
		if($("#SearchKeyword").val()==''){
			$("#SearchKeyword").val("<?php echo $this->_tpl_vars['_keywords_sample']; ?>
").css('color', '#ccc')
			};
	});
	$("#BtnSearch").click(function(){
		if($('#SearchKeyword').val()=='<?php echo $this->_tpl_vars['_keywords_sample']; ?>
') {alert('<?php echo $this->_tpl_vars['_input_keywords']; ?>
');$('#SearchKeyword').focus();return false;}
	})
})
//-->
</script>
<body id="bd_<?php echo @CURSCRIPT; ?>
">
<div id="header">
    <div class="headtop">
    <?php if ($this->caching && !$this->_cache_including): echo '{nocache:c586896f31925ee1a5f1a94b12945505#0}'; endif;$this->_tag_stack[] = array('cacheless', array()); $_block_repeat=true;smarty_block_cacheless($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
    <p class="headtopcon">
        <a class="index" href="javascript:;" onclick="this.style.behavior='url(#default#homepage)';this.setHomePage('<?php echo $this->_tpl_vars['SiteUrl']; ?>
');"><?php echo $this->_tpl_vars['_set_home_page']; ?>
</a>
        <a href="javascript: myAddPanel(document.title,location.href,document.title);" rel="sidebar" title="<?php echo $this->_tpl_vars['site_name']; ?>
"><?php echo $this->_tpl_vars['_add_favorite']; ?>
</a>
        <?php if ($this->_tpl_vars['pb_username'] != ""): ?>
        <em><?php echo $this->_tpl_vars['_hello']; ?>
<?php echo $this->_tpl_vars['pb_username']; ?>
</em>
        <a href="redirect.php?url=office-room">[<?php echo $this->_tpl_vars['_my_office_room']; ?>
]</a>
        <?php if ($this->_tpl_vars['is_admin']): ?><a  href="pb-admin/login.php" target="_blank">[<?php echo $this->_tpl_vars['_control_pannel']; ?>
]</a><?php endif; ?>
        <a href="logging.php?action=logout">[<?php echo $this->_tpl_vars['_login_out']; ?>
]</a>
        <?php else: ?>
        <em><?php echo $this->_tpl_vars['_hello_welcome_to']; ?>
<?php echo $this->_tpl_vars['site_name']; ?>
</em>
        <a href="logging.php">&nbsp;[<?php echo $this->_tpl_vars['_pls_login']; ?>
]</a>
        <a href="member.php" title="<?php echo $this->_tpl_vars['_register']; ?>
" ><strong>[<?php echo $this->_tpl_vars['_free']; ?>
<?php echo $this->_tpl_vars['_register']; ?>
]</strong></a>
        <?php endif; ?>
        <a href="service.php"  style="float:right;"><?php echo $this->_tpl_vars['_customer_service']; ?>
</a>
        <a href="offer/post.php" style="float:right;"><?php echo $this->_tpl_vars['_free_post']; ?>
</a>
        <a href="standard.php" style="float:right;"><?php echo $this->_tpl_vars['_standards']; ?>
</a>
        <?php echo smarty_function_category(array('type' => 'language'), $this);?>

    </p>
    <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_cacheless($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); if ($this->caching && !$this->_cache_including): echo '{/nocache:c586896f31925ee1a5f1a94b12945505#0}'; endif;?>
    </div>
    <div class="logo_search clearfix">
        <h1 id="logo"><a href="<?php echo $this->_tpl_vars['SiteUrl']; ?>
"><img src="<?php echo $this->_tpl_vars['site_logo']; ?>
" alt="<?php echo $this->_tpl_vars['site_banner_word']; ?>
" /></a></h1>
        <div class="search_bar">
            <div class="search_nav">
                <ul>
                    <li id="topMenuProduct"><a href="product/"><span><?php echo $this->_tpl_vars['_search_product']; ?>
</span></a></li>
                    <li id="topMenuCompany"><a href="company/"><span><?php echo $this->_tpl_vars['_search_company']; ?>
</span></a></li>
                    <li id="topMenuOffer"><a href="offer/"><span><?php echo $this->_tpl_vars['_search_offer']; ?>
</span></a></li>
                    <li id="topMenuNews"><a href="news/"><span><?php echo $this->_tpl_vars['_search_news']; ?>
</span></a></li>
                </ul>
                <a href="advsearch.php" class="fl lhighs"><?php echo $this->_tpl_vars['_adv_search']; ?>
</a>
            </div>
            <form name="search_frm" id="SearchFrm" action="offer/list.php" method="get">
            <input type="hidden" name="do" value="search" />
             <div class="search_panel">
                 <span class="search_input_box">
                    <input type="text" name="q" id="SearchKeyword" value="<?php echo ((is_array($_tmp=@$_GET['q'])) ? $this->_run_mod_handler('default', true, $_tmp, @$this->_tpl_vars['_keywords_sample']) : smarty_modifier_default($_tmp, @$this->_tpl_vars['_keywords_sample'])); ?>
" />
                  </span>
                  <span class="search_btn_box">
                  <button type="submit"id="BtnSearch" value="<?php echo $this->_tpl_vars['_search']; ?>
"><?php echo $this->_tpl_vars['_search']; ?>
</button>
                  </span>
                  <span class="history break underline"><strong><?php echo $this->_tpl_vars['_latest_search']; ?>
</strong><?php echo $this->_tpl_vars['SearchHistory']; ?>
</span> 
             </div>

            </form>
        </div>
    </div>
</div>
<div class="header_nav">    
    <div id="nav_inner">
        <ul>
            <?php $_from = $this->_tpl_vars['navs']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['navs'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['navs']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key'] => $this->_tpl_vars['item']):
        $this->_foreach['navs']['iteration']++;
?>
                <li><?php echo $this->_tpl_vars['item']['nav']; ?>
</li>
            <?php endforeach; endif; unset($_from); ?>
        </ul>
	</div>
</div>