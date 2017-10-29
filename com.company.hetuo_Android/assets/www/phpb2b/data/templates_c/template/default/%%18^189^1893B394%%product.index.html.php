<?php /* Smarty version 2.6.18, created on 2010-09-09 15:27:58
         compiled from default%5Cproduct.index.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'ads', 'default\\product.index.html', 10, false),array('block', 'product', 'default\\product.index.html', 52, false),array('block', 'standard', 'default\\product.index.html', 64, false),array('block', 'industry', 'default\\product.index.html', 79, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_nventory'])); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<script src="scripts/swfobject1.5.js" type="text/javascript"></script>
<style type="text/css">
@import url(<?php echo $this->_tpl_vars['theme_img_path']; ?>
effect.css);
</style>
<script src="scripts/jquery.tools.js"></script>
<script src="scripts/jquery.hover.js"></script>
<div class="blank6"></div>
<div class="fullsrceen_ad"><?php $this->_tag_stack[] = array('ads', array('typeid' => 2)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><a href="[link:url]" rel="promotion">[field:src]</a><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></div>
<div class="blank6"></div>
<div class="container clearfix">
   <div class="main_left_w220">
		<div class="cornerbox">
			<div class="title_bar_s1">
				<span class="title_top_s6"><span></span></span>
				<h3><?php echo $this->_tpl_vars['_product_category']; ?>
</h3>
			</div>
			<div class="price_menu product_menu_list">
				<ul id="topnav">
				<?php $_from = $this->_tpl_vars['Industry']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['industry_key'] => $this->_tpl_vars['industry_item']):
?>
					<li>
						<a href="product/list.php?industryid=<?php echo $this->_tpl_vars['industry_key']; ?>
"><?php echo $this->_tpl_vars['industry_item']['title']; ?>
</a>
						<?php if ($this->_tpl_vars['industry_item']['child']): ?>
						<div class="sub">
						   <div class="row clearfix">
							 <ul>
							 <?php $_from = $this->_tpl_vars['industry_item']['child']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['item_child']):
?>
								<li><a href="product/list.php?industryid=<?php echo $this->_tpl_vars['item_child']['id']; ?>
"><?php echo $this->_tpl_vars['item_child']['title']; ?>
</a></li>
							  <?php endforeach; endif; unset($_from); ?>
							 </ul> 
							</div>
						</div>
						<?php endif; ?>
					</li>
				  <?php endforeach; endif; unset($_from); ?>
				</ul>
                <div class="blank6"></div>
			</div>
			<!--<div class="corner_bottom">
				<div class="corner_bottom_l"></div>
				<div class="corner_tottom_r"></div>
			</div>-->
		</div>
      <div class="blank6"></div>
      <div class="title_bar_s1">
        <span class="title_top_s1"><span></span></span>
        <h3><?php echo $this->_tpl_vars['_hot_product']; ?>
</h3>
      </div>
	  <div>
        <ul class="product_sort">
        <?php $this->_tag_stack[] = array('product', array('type' => 'hot','row' => 10,'titlelen' => '10')); $_block_repeat=true;smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
        <li><em>[[field:hits]]</em>[link:url]</li>
        <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
       </ul>
	  </div>
      <div class="blank6"></div>
      <div class="title_bar_s1">
        <span class="title_top_s1"><span></span></span>
        <h3><?php echo $this->_tpl_vars['_standards']; ?>
</h3>
      </div>
	  <div>
        <ul class="product_sort">
        <?php $this->_tag_stack[] = array('standard', array('row' => 10)); $_block_repeat=true;smarty_block_standard($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
        <li>[link:url]</li>
        <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_standard($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
       </ul>
	  </div>
   </div>
   <div class="main_right_w730">  
      <div class="product_reports">
          <div class="product_reports_img"><?php $this->_tag_stack[] = array('ads', array('typeid' => 4)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>This text is replaced by the Flash movie.<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></div>
          <div class="blank6"></div>
          <div class="base_title">
          <h2><span class="corner_t_l"></span><span class="corner_t_m title_mouse"><?php echo $this->_tpl_vars['_category_topics']; ?>
</span><span class="corner_t_r"></span></h2>
	     </div>

        <div class="product_reports_content box_bord clearfix">
         <h3><span class="find_product_blue"><?php echo $this->_tpl_vars['_maybe_browse_categories']; ?>
</span>&nbsp;&nbsp;<?php $this->_tag_stack[] = array('industry', array('row' => '25','topid' => 1)); $_block_repeat=true;smarty_block_industry($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><a href="product/list.php?industryid=[field:id]" title="[field:title]">[field:title]</a> | <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_industry($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></h3>
			<?php $_from = $this->_tpl_vars['IndustryProducts']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['industry_product_key'] => $this->_tpl_vars['industry_product']):
?>         
            <?php if ($this->_tpl_vars['industry_product']['sub']): ?>
            <h2 class="find_product_blue"><span><a href="product/list.php?industryid=<?php echo $this->_tpl_vars['industry_product']['id']; ?>
"><?php echo $this->_tpl_vars['_more']; ?>
...</a></span><?php echo $this->_tpl_vars['industry_product']['name']; ?>
</h2>
            <ul>
              <li>
               	<ul class="product_reports_list clearfix">
                <?php $_from = $this->_tpl_vars['industry_product']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['industry_subproduct_key'] => $this->_tpl_vars['industry_subproduct']):
?> 
                <li><a href="product/content.php?id=<?php echo $this->_tpl_vars['industry_subproduct']['id']; ?>
"><img src="<?php echo $this->_tpl_vars['industry_subproduct']['image']; ?>
" alt="<?php echo $this->_tpl_vars['industry_subproduct']['name']; ?>
" /></a><span><a href="product/content.php?id=<?php echo $this->_tpl_vars['industry_subproduct']['id']; ?>
"><?php echo $this->_tpl_vars['industry_subproduct']['name']; ?>
</span></a></li> 
                <?php endforeach; endif; unset($_from); ?>
                </ul>
              </li>
            </ul>
            <?php endif; ?>
            <?php endforeach; endif; unset($_from); ?>
        </div>
      </div>
      <div class="find_product_right">
      <div class="title_bar_s1">
        <span class="title_top_s1"><span></span></span>
        <h3><?php echo $this->_tpl_vars['_recommended']; ?>
</h3>
       </div>
       <ul class="nominate_content clearfix">
		<?php $this->_tag_stack[] = array('product', array('row' => 8,'type' => "image,commend")); $_block_repeat=true;smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
		<li>
		 <span class="nominate_img"><a href="product/content.php?id=[field:id]"><img src="[img:src]" width="80" height="80" alt="[field:title]" /></a></span>
		 <span class="nominate_text"><p><a href="product/content.php?id=[field:id]" title="[field:title]">[field:title]</a></p><p title="[field:company]">[field:company]</p></span>
		</li>
      <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
    </ul>
      </div>
   
   </div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	function megaHoverOver(){
		$(this).find(".sub").stop().fadeTo('fast', 10000).show();
			
		//Calculate width of all ul's
		(function($) { 
			jQuery.fn.calcSubWidth = function() {
				rowWidth = 0;
				//Calculate row
				$(this).find("ul").each(function() {					
					rowWidth += $(this).width(); 
				});	
			};
		})(jQuery); 
		
		if ( $(this).find(".row").length > 0 ) { //If row exists...
			var biggestRow = 0;	
			//Calculate each row
			$(this).find(".row").each(function() {							   
				$(this).calcSubWidth();
				//Find biggest row
				if(rowWidth > biggestRow) {
					biggestRow = rowWidth;
				}
			});
			//Set width
			$(this).find(".sub").css({'width' :biggestRow});
			$(this).find(".row:last").css({'margin':'0'});
			
		} else { //If row does not exist...
			
			$(this).calcSubWidth();
			//Set Width
			$(this).find(".sub").css({'width' : rowWidth});
			
		}
	}
	
	function megaHoverOut(){ 
	  $(this).find(".sub").stop().fadeTo('fast', 1, function() {
		  $(this).hide(); 
	  });
	}


	var config = {    
		 sensitivity: 2, // number = sensitivity threshold (must be 1 or higher)    
		 interval: 100, // number = milliseconds for onMouseOver polling interval    
		 over: megaHoverOver, // function = onMouseOver callback (REQUIRED)    
		 timeout: 0, // number = milliseconds delay before onMouseOut    
		 out: megaHoverOut // function = onMouseOut callback (REQUIRED)    
	};

	$("ul#topnav li .sub").css({'opacity':'0'});
	$("ul#topnav li").hoverIntent(config);


});
</script>
<script>
$("#SearchFrm").attr("action","product/list.php");
$("#topMenuProduct").addClass("lcur");
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>


