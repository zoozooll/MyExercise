<?php /* Smarty version 2.6.18, created on 2010-09-09 15:28:01
         compiled from default%5Cnews.index.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'newstype', 'default\\news.index.html', 8, false),array('block', 'ads', 'default\\news.index.html', 18, false),array('block', 'news', 'default\\news.index.html', 28, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_industry_info'])); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<div class="wrapper">
  <div class="content clearfix">
  <div class="second_nav clearfix">
        <div id="second_all_nav"><?php echo $this->_tpl_vars['_category_nav']; ?>
</div>
        <div class="news_nav_txt">
        <?php $this->_tag_stack[] = array('newstype', array('row' => 25,'level' => 1)); $_block_repeat=true;smarty_block_newstype($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
        [link:title]
        <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_newstype($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
        </div>
    </div>
  <div class="blank6"></div>
  <div class="news_mainbody">
  <div class="news_mainleft">
      <div class="news_mainbody_left">
      	<div class="news_mainbody_left_top">
			<?php $this->_tag_stack[] = array('ads', array('type' => 'focus')); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>This text is replaced by the Flash movie.<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
		</div>
        <div class="blank6"></div>
        <div class="news_mainbody_left_con">
            <div class="title_bar_s1">
                <span class="title_top_s1"><span></span></span>
                <h2><?php echo $this->_tpl_vars['_hot_news']; ?>
</h2>
             </div>
            <div class="news_mainbody_left_con_body">
             <ul>
             <?php $this->_tag_stack[] = array('news', array('row' => 10,'titlelen' => '35')); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
				<li><a href="[link:title]">[field:title]</a></li>
             <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>  
          </ul> 
    </div>
        </div>
      </div>
      <div class="news_mainbody_mid">
         <div class="title_bar_s2">
             <span class="title_top_s2"><span></span></span>
             <h2><?php echo $this->_tpl_vars['_latest_news']; ?>
</h2>
         </div>
            <ul class="news_mainbody_mid_con clearfix">
            <?php $this->_tag_stack[] = array('news', array('row' => 44,'titlelen' => '25')); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
            <li><span class="hot_word"><a href="news/list.php?typeid=[field:type_id]">[[field:typename]]</a></span><a href="[link:title]">[field:title]</a></li>  
            <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
            </ul> 
        	
      </div>
  </div>
   <div class="news_mainbody_right">
        <div class="title_bar_s1">
             <span class="title_top_s1"><span></span></span>
             <h2><span class="title_more"><a href="news/list.php?typeid=1"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_industry_focus']; ?>
</h2>
         </div>
        <div class="news_mainbody_right_con">
         <ul>
         <?php $this->_tag_stack[] = array('news', array('typeid' => 1,'row' => 10,'titlelen' => 24)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
         <li><a href="news/detail.php?id=[field:id]">[field:title]</a></li>
         <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
         </ul>
      </div>
      <div class="blank6"></div>
       <div class="title_bar_s1">
           <span class="title_top_s1"><span></span></span>
           <h2><span class="title_more"><a href="news/list.php?typeid=4"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_synthesis_info']; ?>
</h2>
       </div>
      
      <div class="news_mainbody_right_con">
         <ul>
             <?php $this->_tag_stack[] = array('news', array('typeid' => 4,'row' => 10,'titlelen' => 24)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
             <li><a href="news/detail.php?id=[field:id]">[field:title]</a></li>
             <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
         </ul>
       </div>
   
   </div>
  
  </div>   
    
  </div>
  <div class="clear"></div>
</div>
<script>
$("#SearchFrm").attr("action","news/list.php");
$("#topMenuNews").addClass("lcur");
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>