<?php /* Smarty version 2.6.18, created on 2010-09-09 14:45:12
         compiled from default%5Cquote.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('function', 'fetch', 'default\\quote.html', 4, false),array('function', 'category', 'default\\quote.html', 8, false),array('block', 'market', 'default\\quote.html', 63, false),array('block', 'product', 'default\\quote.html', 82, false),array('modifier', 'date_format', 'default\\quote.html', 110, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_quotes'])); ?>
<?php $this->assign('nav_id', '6'); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<?php echo smarty_function_fetch(array('file' => "../scripts/date2.js"), $this);?>

	<div class="wrapper">
        <div class="place_price clearfix">
             <strong><?php echo $this->_tpl_vars['_area_prices']; ?>
</strong>
             <div><?php echo smarty_function_category(array('type' => 'area'), $this);?>
</div>
        </div>
        <div class="blank6"></div>        
        <div class="content">
            <div class="main_left_w180">
                <div class="cornerbox">
                    <div class="title_bar_s6">
                        <span class="title_top_s6"><span></span></span>
                        <h2><?php echo $this->_tpl_vars['_quotes']; ?>
<?php echo $this->_tpl_vars['_categories']; ?>
</h2>
                    </div>
                    <div class="price_menu_today clearfix">
                        <ul>
							<?php $_from = $this->_tpl_vars['Categories']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['item']):
?>
							<li><a href="market/quote.php?catid=<?php echo $this->_tpl_vars['item']['id']; ?>
" title="<?php echo $this->_tpl_vars['item']['title']; ?>
"><?php echo $this->_tpl_vars['item']['title']; ?>
</a></li>
							<?php endforeach; endif; unset($_from); ?>
                      </ul>
                        <div class="blank"></div>
                        <div class="btn_price_all"><a href="market/quote.php"><?php echo $this->_tpl_vars['_the_all']; ?>
<?php echo $this->_tpl_vars['_price_quotes']; ?>
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
                        <h2><?php echo $this->_tpl_vars['_quote_search']; ?>
</h2>
                    </div>
                    <div class="corner_content_s6">
                       <div class="box_date_search">
						<form method="get" action="">
						<input type="hidden" name="do" value="chart" />
						   <div><?php echo $this->_tpl_vars['_searching_product']; ?>
<input type="text" name="pn" value="" class="s_input"></div>
						   <div><?php echo $this->_tpl_vars['_date_start']; ?>
<input name="ds" id="date1" value="<?php echo $this->_tpl_vars['QuoteSearchFrom']; ?>
" class="s_input"/><span class="btn_calendar" id="calendar-date1"></span></div>
						   <div><?php echo $this->_tpl_vars['_date_end']; ?>
<input name="de" id="date2" value="<?php echo $this->_tpl_vars['QuoteSearchTo']; ?>
" class="s_input"/><span class="btn_calendar" id="calendar-date2"></span></div>
						   <div class="text_center"><input type="submit" class="btn_submit" value="<?php echo $this->_tpl_vars['_search']; ?>
"></div>
						</form> 
                        </div>                   
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
                        <h2><?php echo $this->_tpl_vars['_commend']; ?>
<?php echo $this->_tpl_vars['_pro_market']; ?>
</h2>
                    </div>
                    <div class="corner_content_s6">
                       <ul>
						<?php $this->_tag_stack[] = array('market', array('row' => 10,'type' => 'commend')); $_block_repeat=true;smarty_block_market($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                         <li><a href="[link:title]">[field:title]</a></li>
						<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_market($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
                       </ul>
                    </div>
                   <div class="corner_bottom">
                        <div class="corner_bottom_l"></div>
                        <div class="corner_tottom_r"></div>
                    </div>
                </div>
              
            </div>
            <div class="main_right_w770">
                <div class="brand_hot">
                    <div class="title">
                        <h2><span class="corner_t_l"></span><span class="corner_t_m title_mouse"><?php echo $this->_tpl_vars['_commend']; ?>
<?php echo $this->_tpl_vars['_product']; ?>
</span><span class="corner_t_r"></span></h2>
                    </div>
                     <div class="product_hot commend box_bord clearfix">
					<ul>
						<?php $this->_tag_stack[] = array('product', array('col' => 6,'row' => 1,'type' => "image,commend")); $_block_repeat=true;smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
						<li><a href="[link:title]"><img src="[img:src]" alt="[field:title]" /></a>
						<p><a href="[link:title]">[field:title]</a></p>
						</li>
					   <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>  
                        </ul>
                     </div>
                </div>
               
                <div class="blank6"></div>
                <div class="product_brand_intro">
                    <table id="hacker">
                     <thead>
                      <tr>
                        <th><?php echo $this->_tpl_vars['_product_name_n']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_av_price']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_area_n']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_market_name_n']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_post_time_n']; ?>
</th>
                      </tr>
                      </thead>
					  <?php $_from = $this->_tpl_vars['Items']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['item']):
?>
                      <tbody>
                      <tr>
                        <td><?php echo $this->_tpl_vars['item']['title']; ?>
</td>
                        <td><?php echo $this->_tpl_vars['item']['price']; ?>
<?php echo $this->_tpl_vars['Monetaries'][$this->_tpl_vars['item']['currency']]; ?>
/<?php echo $this->_tpl_vars['Measuries'][$this->_tpl_vars['item']['units']]; ?>
</td>
                        <td><?php echo $this->_tpl_vars['Areas'][1][$this->_tpl_vars['item']['area_id1']]; ?>
<?php echo $this->_tpl_vars['Areas'][2][$this->_tpl_vars['item']['area_id2']]; ?>
</td>
                        <td><?php echo $this->_tpl_vars['item']['marketname']; ?>
</td>
                        <td class="pb_datetime"><?php echo ((is_array($_tmp=$this->_tpl_vars['item']['pubdate'])) ? $this->_run_mod_handler('date_format', true, $_tmp, "%Y-%m-%d") : smarty_modifier_date_format($_tmp, "%Y-%m-%d")); ?>
</td>
                      </tr>
                      </tbody>
					  <?php endforeach; endif; unset($_from); ?>
                      <tfoot>
                      <tr>
                        <th><?php echo $this->_tpl_vars['_product_name_n']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_av_price']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_area_n']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_market_name_n']; ?>
</th>
                        <th><?php echo $this->_tpl_vars['_post_time_n']; ?>
</th>
                      </tr>
                      </tfoot>
                      <tr>
                        <td colspan="8" id="noborder"><?php echo $this->_tpl_vars['ByPages']; ?>
</td>
                      </tr>
                    </table>
                </div>
            </div>
            <div class="clear"></div>
        </div>
	</div>
<style> 
#hacker tr:hover{ 
background-color: #F6FAFD;
} 
</style>
<script>
    Calendar.setup({
        trigger    : "calendar-date1",
		animation  : false,
        inputField : "date1",
		onSelect   : function() { this.hide() }
    });
    Calendar.setup({
        trigger    : "calendar-date2",
		animation  : false,
        inputField : "date2",
		onSelect   : function() { this.hide() }
    });
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>