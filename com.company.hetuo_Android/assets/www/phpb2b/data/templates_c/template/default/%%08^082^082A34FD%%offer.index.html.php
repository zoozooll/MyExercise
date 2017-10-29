<?php /* Smarty version 2.6.18, created on 2010-09-09 15:28:00
         compiled from default%5Coffer.index.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'ads', 'default\\offer.index.html', 5, false),array('block', 'offer', 'default\\offer.index.html', 20, false),array('block', 'company', 'default\\offer.index.html', 82, false),array('block', 'product', 'default\\offer.index.html', 96, false),array('modifier', 'date_format', 'default\\offer.index.html', 17, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_search_offer'])); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
  <div class="blank6"></div>
  <div class="container clearfix">
    <div class="fullsrceen_ad"><?php $this->_tag_stack[] = array('ads', array('typeid' => 2)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><a href="[link:url]" rel="promotion">[field:src]</a><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></div>
    <div class="blank6"></div>
    <div class="offer_list">
      <div class="offer_list_top">
        <div class="offer_list_top1 fl">
          <div class="offer_list_top1_news">
            <div class="title_bar_s1">
            <span class="title_top_s1"><span></span></span>
            <h2><?php echo $this->_tpl_vars['_new_offer']; ?>
</h2>
            </div>
            <div class="offer_list_top1_newscon">
                <div class="title_time">
                  <h4><span id="currentDate"><?php echo $this->_tpl_vars['_today_is']; ?>
<?php echo ((is_array($_tmp=time())) ? $this->_run_mod_handler('date_format', true, $_tmp, "%Y-%m-%d") : smarty_modifier_date_format($_tmp, "%Y-%m-%d")); ?>
</span></h4>
                </div>
              <ul>
			  <?php $this->_tag_stack[] = array('offer', array('row' => 10)); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                <li class="even">
                <span class="even_width" ><a href="[link:title]" title="[field:title]">[field:title]</a></span>
                <span class="even_padding" >[[field:typename]]</span>
                <span class="even_padding">[field:pubdate]</span>
                
                </li>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
              </ul>
            </div>
          </div>
        </div>
        <div class="offer_list_right clearfix">
        <div class="offer_list_top2">
			<div><?php $this->_tag_stack[] = array('ads', array('typeid' => 3)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>This text is replaced by the Flash movie.<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></div>
			<div class="blank6"></div>            
        </div>
        <div class="offer_list_top3">
           <div class="title_bar_s1">
               <span class="title_top_s1"><span></span></span>
               <h5><?php echo $this->_tpl_vars['_welcome_join']; ?>
<?php echo $this->_tpl_vars['site_name']; ?>
</h5>
            </div>

          <div class="offer_list_top3_c">
            <div class="offer_list_top3_c1 cursor-hand">
              <ul><?php echo $this->_tpl_vars['_offer_introduction']; ?>
</ul>
              <div class="clear"></div>
            </div>
            <div class="offer_list_top3_c2">
              <h2><span><a href="member.php" title="<?php echo $this->_tpl_vars['_free_reg']; ?>
"><?php echo $this->_tpl_vars['_free_reg']; ?>
</a></span></h2>
              <?php echo $this->_tpl_vars['_reg_and_upgrade_vip']; ?>

              <?php echo $this->_tpl_vars['_service_hotline']; ?>
<span class="font14" ><?php echo $this->_tpl_vars['service_tel']; ?>
</span>
              </div>
          </div>
        </div>
        </div>
        <!--top3 end -->
      </div>
     <div class="clear"></div>
    </div>
           <div class="offer_list_con">
                <div class="offer_list_con_left">
                    
                     <div class="title_bar_s1">
                        <span class="title_top_s1"><span></span></span>
                        <h2><?php echo $this->_tpl_vars['_urgent_buy']; ?>
</h2>
                        </div>
                     <div class="offer_list_con_left2_c offer_list_con_left_con">
                     <ul>
                        <?php $this->_tag_stack[] = array('offer', array('row' => 8)); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                        <li><a href="[link:title]">[field:title]</a></li>
                        <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
                        </ul>
                      </div>  
                    <div class="blank6"></div>

                    <div class="title_bar_s1">
                        <span class="title_top_s1"><span></span></span>
                        <h2><?php echo $this->_tpl_vars['_commend']; ?>
<?php echo $this->_tpl_vars['_seller']; ?>
</h2>
                    </div>
                    <div  class="offer_list_con_left3_c offer_list_con_left_con">
                    <ul>
                         <?php $this->_tag_stack[] = array('company', array('row' => 8)); $_block_repeat=true;smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                         <li><a href="[link:title]">[field:title]</a></li>
                         <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
                     </ul>
                     </div>
                     <div class="blank6"></div>
                   
                       <div class="title_bar_s1">
                        <span class="title_top_s1"><span></span></span>
                        <h2><?php echo $this->_tpl_vars['_stored_product']; ?>
</h2>
                        </div>
                        
                       <div  class="offer_list_con_left4_c offer_list_con_left_con">
                         <ul>
                         <?php $this->_tag_stack[] = array('product', array('row' => 8)); $_block_repeat=true;smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                         <li><a href="[link:title]">[field:title]</a></li>
                         <?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
                         </ul>
                      </div>
                  
                </div>
                <div class="co_assort fr" >
                  <div class="title_bar_s1" style="height:28px; overflow:hidden">
                    <span class="title_top_s1"><span></span></span>
                    <h4><span class="titlebg_l"></span><span class="titlebg_m title_mouse"><?php echo $this->_tpl_vars['_industry_sort']; ?>
</span><span class="titlebg_r"></span></h4>
                </div>
            <div class="co_assort_c clearfix" >
                <?php $_from = $this->_tpl_vars['IndustryList']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_0'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_0']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key0'] => $this->_tpl_vars['item0']):
        $this->_foreach['level_0']['iteration']++;
?>
                <h3 class="cb"><em class="fl"><a href="offer/list.php?industryid=<?php echo $this->_tpl_vars['item0']['id']; ?>
"><?php echo $this->_tpl_vars['item0']['name']; ?>
</a></em><p class="fr"><a href="offer/list.php?industryid=<?php echo $this->_tpl_vars['item0']['id']; ?>
"><?php echo $this->_tpl_vars['_more']; ?>
</a></p></h3>
                <?php if ($this->_tpl_vars['item0']['sub']): ?>
                <?php $_from = $this->_tpl_vars['item0']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_1_industry'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_1_industry']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key_level1'] => $this->_tpl_vars['level1']):
        $this->_foreach['level_1_industry']['iteration']++;
?>
                <div class="fl">
                    <a href="offer/list.php?industryid=<?php echo $this->_tpl_vars['level1']['id']; ?>
" title="<?php echo $this->_tpl_vars['level1']['name']; ?>
" class="co_type_f"><?php echo $this->_tpl_vars['level1']['name']; ?>
</a>
                    <br />
                    <?php $_from = $this->_tpl_vars['level1']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_2_industry'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_2_industry']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key_level2'] => $this->_tpl_vars['level2']):
        $this->_foreach['level_2_industry']['iteration']++;
?>
                    <a href="offer/list.php?industryid=<?php echo $this->_tpl_vars['level2']['id']; ?>
" title="<?php echo $this->_tpl_vars['level2']['name']; ?>
"><?php echo $this->_tpl_vars['level2']['name']; ?>
</a><em>|</em>
                    <?php endforeach; endif; unset($_from); ?>
                </div>
                <?php endforeach; endif; unset($_from); ?>
                <?php endif; ?>
                <?php endforeach; endif; unset($_from); ?>
                <div class="cb"></div>
            </div>
        </div>
      </div> 
</div>
<script>
$("#SearchFrm").attr("action","offer/list.php");
$("#topMenuOffer").addClass("lcur");
</script>   
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>