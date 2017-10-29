<?php /* Smarty version 2.6.18, created on 2010-09-09 14:45:04
         compiled from default%5Cprice.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'price', 'default\\price.html', 53, false),array('block', 'product', 'default\\price.html', 79, false),array('function', 'category', 'default\\price.html', 67, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['page_title'])); ?>
<?php $this->assign('nav_id', '5'); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<style type="text/css">
@import url(<?php echo $this->_tpl_vars['theme_img_path']; ?>
effect.css);
</style>
<script src="scripts/jquery.tools.js"></script>
<script src="scripts/jquery.hover.js"></script>
<div class="wrapper">
	<div class="content">
		<div class="main_left_w180">
			<div class="cornerbox">
				<div class="title_bar_s6">
					<span class="title_top_s6"><span></span></span>
					<h2><?php echo $this->_tpl_vars['_product_prices']; ?>
</h2>
				</div>
				<div class="price_menu">
					<ul id="topnav">
					<?php $_from = $this->_tpl_vars['Items']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['item']):
?>
						<li>
							<a href="product/price.php?do=search&catid=<?php echo $this->_tpl_vars['item']['id']; ?>
"><?php echo $this->_tpl_vars['item']['title']; ?>
</a>
							<?php if ($this->_tpl_vars['item']['child']): ?>
							<div class="sub">
							   <div class="row clearfix">
								 <ul>
								 <?php $_from = $this->_tpl_vars['item']['child']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['item_child']):
?>
									<li><a href="product/price.php?do=search&catid=<?php echo $this->_tpl_vars['item_child']['id']; ?>
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
					<div class="blank"></div>
					<div class="btn_price_all"><a href="product/price.php?do=search"><?php echo $this->_tpl_vars['_all_product_categories']; ?>
</a></div>
					<div class="blank6"></div>
				</div>
				<div class="corner_bottom">
					<div class="corner_bottom_l"></div>
					<div class="corner_tottom_r"></div>
				</div>
			</div>
			<div class="blank6"></div>
			<div class="cornerbox">
				<div class="title_bar_s6">
					<span class="title_top_s6"><span></span></span>
					<h2><?php echo $this->_tpl_vars['_latest_price']; ?>
</h2>
				</div>
				<div class="corner_content_s6">
				   <ul>
					  <?php $this->_tag_stack[] = array('price', array('row' => 9)); $_block_repeat=true;smarty_block_price($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					  <li><a href="[link:title]">[field:title]</a></li>
					 <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_price($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				   </ul>
				</div>
			   <div class="corner_bottom">
					<div class="corner_bottom_l"></div>
					<div class="corner_tottom_r"></div>
				</div>
			</div><!--end-->
		</div>
		<div class="main_right_w770">
			 <div class="area_price clearfix">
				 <strong><?php echo $this->_tpl_vars['_area_prices']; ?>
</strong>
				 <div><?php echo smarty_function_category(array('type' => 'area'), $this);?>
</div>
			 </div>
			 <div class="blank6"></div>
			 <!-- the tabs -->
			<ul class="tabs2 title_mouse">
				  <li><a href="javascript:;"><span><?php echo $this->_tpl_vars['_latest_new']; ?>
<?php echo $this->_tpl_vars['_product']; ?>
</span></a></li>
				  <li><a href="javascript:;"><span><?php echo $this->_tpl_vars['_latest_hot']; ?>
<?php echo $this->_tpl_vars['_product']; ?>
</span></a></li>
			</ul>
			<!-- tab "panes" --> 
			<div class="panes box_bord">
				<div class="product_hot clearfix">
					<ul>
						<?php $this->_tag_stack[] = array('product', array('col' => 6,'row' => 1,'type' => 'image')); $_block_repeat=true;smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
						<li><a href="[link:title]"><img src="[img:src]" alt="[field:title]" /></a>
						<p><a href="[link:title]">[field:title]</a></p>
						</li>
					   <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>                                                    
					</ul>
				 </div>
				<div class="product_hot clearfix" style="display:none;">
					<ul>
						<?php $this->_tag_stack[] = array('product', array('col' => 6,'row' => 1,'type' => 'hot')); $_block_repeat=true;smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
						<li><a href="[link:title]"><img src="[img:src]" alt="[field:title]" /></a>
						<p><a href="[link:title]">[field:title]</a></p>
						</li>
					   <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>                                                      
					</ul>
				 </div>
			</div>
			<div class="blank6"></div>
			<div class="brand_hot">
				<div class="title">
					<h2><span class="corner_t_l"></span><span class="corner_t_m title_mouse"><?php echo $this->_tpl_vars['_product_category']; ?>
</span><span class="corner_t_r"></span></h2>
				</div>
				<div class="letterindex clearfix">
					<h3><?php echo $this->_tpl_vars['_sort_by_alpha']; ?>
</h3>
					 <ul id="letternav">
						<?php $_from = $this->_tpl_vars['AlphabetSorts']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['Alphabet'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['Alphabet']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key_alphabet'] => $this->_tpl_vars['item_alphabet']):
        $this->_foreach['Alphabet']['iteration']++;
?>
						<li class="frist">
						 <a href="javascript:;"><?php echo $this->_tpl_vars['key_alphabet']; ?>
</a>
							<div class="sub">
							   <div class="row clearfix">
								 <ul>
									<?php $_from = $this->_tpl_vars['item_alphabet']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['subkey_alphabet'] => $this->_tpl_vars['subitem_alphabet']):
?>
									<li><a href="product/price.php?do=search&catid=<?php echo $this->_tpl_vars['subkey_alphabet']; ?>
"><?php echo $this->_tpl_vars['subitem_alphabet']; ?>
</a></li>
									<?php endforeach; endif; unset($_from); ?>
								 </ul> 
								</div>
							</div>
						</li>
						<?php endforeach; endif; unset($_from); ?>
					</ul>
				</div>
				<div class="letter_product_content clearfix">
					<table>
					<?php $_from = $this->_tpl_vars['Cats']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['Cats'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['Cats']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key1'] => $this->_tpl_vars['item1']):
        $this->_foreach['Cats']['iteration']++;
?>
					  <tr class="bg_hover">
						<th><a href="product/price.php?do=search&catid=<?php echo $this->_tpl_vars['item1']['id']; ?>
"><?php echo $this->_tpl_vars['item1']['title']; ?>
</a></th>
						<td>
						   <ul class="aa clearfix">
							   <?php $_from = $this->_tpl_vars['item1']['child']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['Cats2'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['Cats2']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key2'] => $this->_tpl_vars['item2']):
        $this->_foreach['Cats2']['iteration']++;
?>
							   <li>
								  <h3><a href="product/price.php?do=search&catid=<?php echo $this->_tpl_vars['item2']['id']; ?>
"><?php echo $this->_tpl_vars['item2']['title']; ?>
</a></h3>
								  <ul>
									 <li>
									 <?php $_from = $this->_tpl_vars['item2']['child']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['Cats3'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['Cats3']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key3'] => $this->_tpl_vars['item3']):
        $this->_foreach['Cats3']['iteration']++;
?>
									 <a href="product/price.php?do=search&catid=<?php echo $this->_tpl_vars['item3']['id']; ?>
"><?php echo $this->_tpl_vars['item3']['title']; ?>
</a>
									 <?php endforeach; endif; unset($_from); ?>
									 </li>
								  </ul>
							   </li>
							   <?php endforeach; endif; unset($_from); ?>
						   </ul>
						</td>
					  </tr>
					  <tr><td colspan="2" class="padding0"><hr class="hr_dashed"/></td></tr>
					  <?php endforeach; endif; unset($_from); ?>
					</table>    
				</div>                     
			</div><!--end-->
		
		</div>
		<div class="clear"></div>
	</div>
</div>
<script>
// perform JavaScript after the document is scriptable.
$(function() {
	// setup ul.tabs to work as tabs for each div directly under div.panes
	$("ul.tabs2").tabs("div.panes > div");
});
</script>
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

	$("ul#letternav li .sub").css({'opacity':'0'});
	$("ul#letternav li").hoverIntent(config);
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