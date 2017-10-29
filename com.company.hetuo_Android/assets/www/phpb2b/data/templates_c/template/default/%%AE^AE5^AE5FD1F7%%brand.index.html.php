<?php /* Smarty version 2.6.18, created on 2010-09-09 14:45:10
         compiled from default%5Cbrand.index.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'ads', 'default\\brand.index.html', 10, false),array('block', 'news', 'default\\brand.index.html', 45, false),array('block', 'brand', 'default\\brand.index.html', 61, false),array('function', 'cycle', 'default\\brand.index.html', 97, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_brands'])); ?>
<?php $this->assign('nav_id', '10'); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<style type="text/css">
@import url(<?php echo $this->_tpl_vars['theme_img_path']; ?>
effect.css);
</style>
<script src="scripts/jquery.hover.js"></script>
<script src="scripts/jquery.scrollTo.js"></script>
<div class="wrapper">
	<div class="banner"><?php $this->_tag_stack[] = array('ads', array('typeid' => 2)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><span><a href="[link:url]">[field:src]</a></span><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></div>
	<div class="content">
		<div class="main_left_w180">
			<div class="cornerbox">
				<div class="title_bar_s6">
					<span class="title_top_s6"><span></span></span>
					<h2><?php echo $this->_tpl_vars['_brands']; ?>
<?php echo $this->_tpl_vars['_categories']; ?>
</h2>
				</div>
				<div class="brand_category_list">
					<ul>
					<?php $_from = $this->_tpl_vars['Items']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['brandtype'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['brandtype']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['brandtype']):
        $this->_foreach['brandtype']['iteration']++;
?>
						<li>
						   <h3><a href="brand/list.php?catid=<?php echo $this->_tpl_vars['brandtype']['id']; ?>
"><?php echo $this->_tpl_vars['brandtype']['title']; ?>
</a></h3>
						   <ul>
							<?php $_from = $this->_tpl_vars['brandtype']['child']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['item_child']):
?>
							   <li><a href="brand/list.php?catid=<?php echo $this->_tpl_vars['item_child']['id']; ?>
"><?php echo $this->_tpl_vars['item_child']['title']; ?>
</a></li>
							<?php endforeach; endif; unset($_from); ?>
						   </ul>
						</li>
					  <?php endforeach; endif; unset($_from); ?>
					</ul>
				</div>
			   <div class="corner_bottom">
					<div class="corner_bottom_l"></div>
					<div class="corner_tottom_r"></div>
				</div>
			</div><!--end-->
			<div class="blank6"></div>
			<div class="cornerbox">
				<div class="title_bar_s6">
					<span class="title_top_s6"><span></span></span>
					<h2><?php echo $this->_tpl_vars['_brands']; ?>
<?php echo $this->_tpl_vars['_information']; ?>
</h2>
				</div>
				<div class="corner_content_s6">
				   <ul>
					 <?php $this->_tag_stack[] = array('news', array('row' => 10)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					 <li><a href="[link:title]">[field:title]</a></li>
					 <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				   </ul>
				</div>
			   <div class="corner_bottom">
					<div class="corner_bottom_l"></div>
					<div class="corner_tottom_r"></div>
				</div>
			</div><!--end-->
		</div>
		<div class="main_right_w770">
		   
			<div class="brand_recommend">
				<h2><?php echo $this->_tpl_vars['_commend']; ?>
<?php echo $this->_tpl_vars['_brands']; ?>
</h2>
				<div class="brand_list clearfix">
				<?php $this->_tag_stack[] = array('brand', array('row' => 6,'col' => 2,'type' => "image,commend")); $_block_repeat=true;smarty_block_brand($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<ul>
					 <li><div><span><a href="[link:title]"><img src="[img:src]" alt="[field:title]" /></a></span></div>
					<p><a href="[link:title]">[field:title]</a></p>
					 </li>
				  </ul>
				  <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_brand($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				</div>
			</div><!--end-->
			<div class="blank6"></div>
			<div class="brand_hot">
				<div class="title">
					<h2><span class="corner_t_l"></span><span class="corner_t_m title_mouse"><?php echo $this->_tpl_vars['_new_added']; ?>
<?php echo $this->_tpl_vars['_brands']; ?>
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
						 <a href="javascript:;" onclick="$.scrollTo('#BrandId<?php echo $this->_tpl_vars['key_alphabet']; ?>
',800);" ><?php echo $this->_tpl_vars['key_alphabet']; ?>
</a>
							<div class="sub">
							   <div class="row clearfix">
								 <ul>
									<?php $_from = $this->_tpl_vars['item_alphabet']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['subkey_alphabet'] => $this->_tpl_vars['subitem_alphabet']):
?>
									<li><a href="brand/detail.php?id=<?php echo $this->_tpl_vars['subkey_alphabet']; ?>
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
				<div class="letter_content clearfix">
						<?php $_from = $this->_tpl_vars['LatestBrands']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['key_brand'] => $this->_tpl_vars['item_brand']):
?>
					  <div class="brand_letter <?php echo smarty_function_cycle(array('values' => "bg_base,bg_yellow"), $this);?>
">
						   <div class="letter_title" id="BrandId<?php echo $this->_tpl_vars['key_brand']; ?>
"><?php echo $this->_tpl_vars['key_brand']; ?>
<span><a href="brand/list.php?letter=<?php echo $this->_tpl_vars['key_brand']; ?>
&do=search"><?php echo $this->_tpl_vars['_more']; ?>
...</a></span></div>
						   <div class="brand_list clearfix">
								<ul>
									<?php $_from = $this->_tpl_vars['item_brand']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['key_brand_child'] => $this->_tpl_vars['item_brand_child']):
?>
									 <li>
									<div><span><a href="brand/detail.php?id=<?php echo $this->_tpl_vars['item_brand_child']['id']; ?>
"><img src="<?php echo $this->_tpl_vars['item_brand_child']['img']; ?>
" /></a></span></div>
									<p><a href="brand/detail.php?id=<?php echo $this->_tpl_vars['item_brand_child']['id']; ?>
"><?php echo $this->_tpl_vars['item_brand_child']['title']; ?>
</a></p>
									</li>  
									<?php endforeach; endif; unset($_from); ?>
							  </ul>
							</div>
					  </div>
					  <?php endforeach; endif; unset($_from); ?>
				</div>                     
			</div><!--end-->
		 
		</div>
		<div class="clear"></div>
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

	$("ul#letternav li .sub").css({'opacity':'0'});
	$("ul#letternav li").hoverIntent(config);


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