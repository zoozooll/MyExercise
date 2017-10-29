<?php /* Smarty version 2.6.18, created on 2010-09-09 14:45:14
         compiled from default%5Cfair.index.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'ads', 'default\\fair.index.html', 5, false),array('block', 'fair', 'default\\fair.index.html', 20, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_fair'])); ?>
<?php $this->assign('nav_id', '8'); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<div class="adv mauto">
    <?php $this->_tag_stack[] = array('ads', array('typeid' => 1,'row' => '8')); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><span><a href="[link:url]" rel="promotion">[field:src]</a></span><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
</div>
<div class="wrapper">
   <div class="exh_con">
      <div class="exh_con_left">
         <div class="exh_con_img">
			<?php $this->_tag_stack[] = array('ads', array('type' => 'focus','from' => 'fair')); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>This text is replaced by the Flash movie.<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
		 </div>
         <div class="exh_con_net">
             <div class="title_bar_s2">
             <span class="title_top_s2"><span></span></span>
             <h2><?php echo $this->_tpl_vars['_recommended_fair']; ?>
</h2>
             </div>
             <div class="net_content">
              <div class="main clearfix">
               <?php $this->_tag_stack[] = array('fair', array('type' => 'commend','row' => 1,'infolen' => 35)); $_block_repeat=true;smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
               <div class="net_content_img"><img src="[img:src]" /></div>
               <div class="net_content_intro">
                 <h3><a href="[link:title]">[field:title]</a></h3>
                 <p>[field:content]&nbsp;&nbsp;<span><a href="[link:title]"><?php echo $this->_tpl_vars['_detail']; ?>
</a></span> </p>
               </div>
               <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
              </div>
               <div class="net_meeting">
                 <ul>
                 <?php $this->_tag_stack[] = array('fair', array('row' => 2)); $_block_repeat=true;smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                 <li><span class="net_meeting_title"><a href="fair/detail.php?id=[field:id]" target="_blank">[field:title]</a></span><span class="net_meeting_time pb_datetime">[[field:pubdate]]</span></li>
                 <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
                 </ul>
                 <div class="clear"></div>
               </div>
             </div>
         </div>
        <div class="blank6"></div>  
        <div class="title_bar_s1">
        <span class="title_top_s1"><span></span></span>
        <h2><span class="title_more"><a href="fair/list.php"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_new_fair']; ?>
</h2>
        </div>

        <div class="exh_recent">
            <ul class="exh_recentlist clearfix">
            <?php $this->_tag_stack[] = array('fair', array('row' => 24)); $_block_repeat=true;smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                <li><span class="sort"><a href="fair/list.php?typeid=[field:typeid]">[[field:typename]]</a></span><span class="title"><a href="fair/detail.php?id=[field:id]">[field:title]</a></span><span class="date pb_datetime">[[field:pubdate]]</span></li>
            <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
            </ul>
        </div>
      </div>
      <div class="exh_con_right"> 
         <div class="title_bar_s1">
           <span class="title_top_s1"><span></span></span>
           <h2><span class="title_more"><a href="fair/list.php?type=hot"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_hot_fair']; ?>
</h2>
        </div>
        <div class="exh_consulting">
        <ul>
        <?php $this->_tag_stack[] = array('fair', array('row' => 10,'type' => 'hot')); $_block_repeat=true;smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
        <li><a href="fair/detail.php?id=[field:id]">[field:title]</a></li>
        <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
        </ul>
      </div>
    <div class="blank6"></div>
        
          <div class="title_bar_s1">
           <span class="title_top_s1"><span></span></span>
           <h2><span class="title_more"><a href="fair/list.php"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_find_fair']; ?>
</h2>
        </div>
       
        <div class="exh_find">
        <form action="fair/list.php" name="search_fair_frm" id="SearchFairFrm">
        <input type="hidden" name="do" value="search" />
        <table>
          <tr>
            <th><?php echo $this->_tpl_vars['_keyword']; ?>
</th>
            <td><input name="q" type="text" style="width:100px; color:gray;" value="<?php echo $this->_tpl_vars['_input_keywords']; ?>
"/></td>
          </tr>
          
          <tr>
            <th>&nbsp;</th>
            <td><input type="button" value="<?php echo $this->_tpl_vars['_search']; ?>
" class="btn_search" onclick="$('#SearchFairFrm').submit();" /></td>
          </tr>
        </table>
        </form>
        </div>
   </div> 
 <div class="clear"></div>  
</div>
   <div class="blank6"></div>
   <?php $this->_tag_stack[] = array('ads', array('typeid' => 2)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><span><a href="[link:url]" rel="promotion">[field:src]</a></span><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
</div>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>