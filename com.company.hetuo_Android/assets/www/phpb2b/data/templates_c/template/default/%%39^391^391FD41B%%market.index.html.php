<?php /* Smarty version 2.6.18, created on 2010-09-09 14:45:13
         compiled from default%5Cmarket.index.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'ads', 'default\\market.index.html', 6, false),array('block', 'news', 'default\\market.index.html', 16, false),array('block', 'market', 'default\\market.index.html', 53, false),array('block', 'offer', 'default\\market.index.html', 96, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_market'])); ?>
<?php $this->assign('nav_id', '7'); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<script src="scripts/swfobject1.5.js" type="text/javascript"></script>
<div class="adv mauto">
	<?php $this->_tag_stack[] = array('ads', array('typeid' => 1,'row' => '8')); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><a href="[link:url]" rel="promotion">[field:src]</a><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
</div>
<div class="wrapper clearfix">
   <div class="main_left ">
	  <div class="price_info">
          <div class="title_bar_s2">
            <span class="title_top_s2"><span></span></span>
            <h3><span class="title_more"><a href="news/list.php?typeid=1"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_price_quotes']; ?>
</h3>
          </div>
			<ul class="hot_list rate_list">
			<?php $this->_tag_stack[] = array('news', array('magic' => 'y','typeid' => 1,'row' => 7,'titlelen' => 34,'infolen' => 22)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
			<li[field:style]>[field:url]</li>
			<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			</ul>
			<div class="corner_bottom_bar">
                 <span class="corner_bottom_s1"><span></span></span>
			</div>
	   </div>
	  <div class="main_left_img" id="marketFocus"></div>
   </div>
   <div class="main_right">
	<div class="freereg">
            <div class="reg_bar_s1">
               <span class="reg_top_s1"><span></span></span>
              <h2><?php echo $this->_tpl_vars['_after_free_reg']; ?>
</h2>
              <div class="freeregc clearfix">
				<p class="fl freetit"><a><?php echo $this->_tpl_vars['_build_your_site']; ?>
</a></p>
				<p class="fl freetit"><a><?php echo $this->_tpl_vars['_post_offer_trade']; ?>
</a></p>
				<p class="fl freetit"><a><?php echo $this->_tpl_vars['_spread_your_products']; ?>
</a></p>
				<p class="fl freetit"><a><?php echo $this->_tpl_vars['_meet_more_friends']; ?>
</a></p>
				<p class="fl mt10 big-color-weight"><a class="btn_free" href="member.php" title="<?php echo $this->_tpl_vars['_register']; ?>
"><?php echo $this->_tpl_vars['_free_reg_member']; ?>
</a></p>
			   </div>
            </div>
			<div class="reg_bottom_bar">
				<span class="reg_bottom_s1"><span></span></span>
			</div>
		</div>
		
   </div>
   <div class="blank6"></div>
   <div class="main_left">
	<div class="nominate_market">
       <div class="title_bar_s1">
            <span class="title_top_s1"><span></span></span>
            <h3><?php echo $this->_tpl_vars['_nominate_market']; ?>
</h3>
       </div>
	  <ul class="nominate_list clearfix">
		<?php $this->_tag_stack[] = array('market', array('row' => 12,'type' => 'image')); $_block_repeat=true;smarty_block_market($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
		 <li><a href="[link:title]"><img src="[img:src]" width="100" height="80" alt="[field:title]" /></a>
		  <p><a href="[link:title]">[field:title]</a></p>
		</li>
		<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_market($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
	  </ul>
	  </div>
	  <div class="blank6"></div>
	  <div class="special_market">
      <div class="special_market_title">
	   <h2><span class="more"><a href="market/list.php" title="<?php echo $this->_tpl_vars['_more']; ?>
"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_search_more_market']; ?>
</a></span><?php echo $this->_tpl_vars['_regional_sub_station']; ?>
</h2>
       </div>
	   	<table class="special_market_list">
        <?php $_from = $this->_tpl_vars['AreaItems']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['AreaItems'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['AreaItems']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['area_key'] => $this->_tpl_vars['area_item']):
        $this->_foreach['AreaItems']['iteration']++;
?>
          <tr>
            <th><a href="market/list.php?areaid=<?php echo $this->_tpl_vars['area_item']['id']; ?>
"><?php echo $this->_tpl_vars['area_item']['name']; ?>
</a></th>
            <td>
            <?php $_from = $this->_tpl_vars['area_item']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['AreaItems2'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['AreaItems2']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['area_key2'] => $this->_tpl_vars['area_item2']):
        $this->_foreach['AreaItems2']['iteration']++;
?>
            <a href="market/list.php?areaid=<?php echo $this->_tpl_vars['area_item2']['id']; ?>
"><span><?php echo $this->_tpl_vars['area_item2']['name']; ?>
</span></a> <?php endforeach; endif; unset($_from); ?></td>
          </tr>
        <?php endforeach; endif; unset($_from); ?>
        </table>
	  </div>
   </div>
   <div class="main_right">
	 <div class="add_market">
         <div class="title_bar_s1">
             <span class="title_top_s1"><span></span></span>
             <h2><span class="intro"><a href="market/add.php"><?php echo $this->_tpl_vars['_i_would_join']; ?>
&raquo;</a></span><?php echo $this->_tpl_vars['_new_add_market']; ?>
</h2>
         </div>
			<ul class="add_market_list">
				<?php $this->_tag_stack[] = array('market', array('row' => 12)); $_block_repeat=true;smarty_block_market($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
				<li><a href="[link:title]" title="[field:title]">[field:title]</a></li>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_market($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			</ul>
	 </div>
   </div> 
   <div class="blank6"></div>
   <div class="new_supply">
   <div class="new_supply_title">
	 <h2><?php echo $this->_tpl_vars['_new_offer']; ?>
</h2>
     </div>
	 <ul class="new_supply_list clearfix">
		<?php $this->_tag_stack[] = array('offer', array('row' => 10)); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
		<li><a href="offer/list.php?typeid=[field:typeid]">[[field:typename]]</a>&nbsp;<a href="[link:title]">[field:title]</a></li>
		<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
	 </ul>
   </div>
</div>
<script>
var myflash = new SWFObject("images/slide.swf", "27", 477, 245, "7", "#F3F3F3");
myflash.addParam("quality", "high");
myflash.addParam("wmode", "opaque");
myflash.addVariable("image","<?php echo $this->_tpl_vars['flashvar']['image']; ?>
");
myflash.addVariable("url","<?php echo $this->_tpl_vars['flashvar']['url']; ?>
");
myflash.addVariable("info", "<?php echo $this->_tpl_vars['flashvar']['info']; ?>
");
myflash.addVariable("stopTime","5000");
myflash.write("marketFocus");
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>