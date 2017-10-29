<?php /* Smarty version 2.6.18, created on 2010-09-09 14:40:35
         compiled from default%5Cindex.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('block', 'ads', 'default\\index.html', 9, false),array('block', 'announce', 'default\\index.html', 21, false),array('block', 'fair', 'default\\index.html', 70, false),array('block', 'offer', 'default\\index.html', 86, false),array('block', 'company', 'default\\index.html', 130, false),array('block', 'tag', 'default\\index.html', 148, false),array('block', 'brand', 'default\\index.html', 185, false),array('block', 'news', 'default\\index.html', 226, false),array('block', 'product', 'default\\index.html', 259, false),array('block', 'newstype', 'default\\index.html', 278, false),array('block', 'topic', 'default\\index.html', 330, false),array('block', 'friendlink', 'default\\index.html', 354, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['_home_page'])); ?>
<?php $this->assign('nav_id', '1'); ?>
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
<div class="adv">
	<?php $this->_tag_stack[] = array('ads', array('typeid' => 1,'row' => '8')); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><span><a href="[link:url]" rel="promotion">[field:src]</a></span><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
</div>
<div class="wrapper">
	<div class="content clearfix">
	   <div class="fl notebox mr7">
        <div class="title_bar_s2">
            <span class="title_top_s2"><span></span></span>
            <h2><span class="title_more"><a href="announce.php" title="<?php echo $this->_tpl_vars['_more']; ?>
" target="_blank"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_board']; ?>
</h2>
            </div>
			<div class="notecont clearfix">
               <div class="note_bar">
				<img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/notice.gif" alt="<?php echo $this->_tpl_vars['_board']; ?>
" />
				<?php $this->_tag_stack[] = array('announce', array('titlelen' => 9,'infolen' => 40,'row' => 1)); $_block_repeat=true;smarty_block_announce($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
				<dl>
					<dt><strong><a href="[link:title]">[field:title]</a></strong></dt>
					<dd><a href="[link:title]" title="[field:content]">[field:content]</a></dd>
				</dl>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_announce($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
               </div>
				<ul class="note_title clearfix">
					<?php $this->_tag_stack[] = array('announce', array('titlelen' => 15,'row' => 4)); $_block_repeat=true;smarty_block_announce($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<li><a href="[link:title]" title="[field:title]">[field:title]</a></li>
					<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_announce($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				</ul>
			</div>
			<div class="corner_bottom_bar">
                 <span class="corner_bottom_s1"><span></span></span>
			</div>
		</div>
	   <div class="fl advertise">
			<?php $this->_tag_stack[] = array('ads', array('typeid' => 5)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>This text is replaced by the Flash movie.<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
		</div>
       <div class="fr freereg">
                <div class="reg_bar_s1">
                   <span class="reg_top_s1"><span></span></span>
                  <h2><?php echo $this->_tpl_vars['_after_free_reg']; ?>
</h2>
                  <div class="freeregc clearfix cursor-hand">
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
	<div class="content">
		<?php $this->_tag_stack[] = array('ads', array('typeid' => 2)); $_block_repeat=true;smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><span><a href="[link:url]" rel="promotion">[field:src]</a></span><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_ads($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
	</div>
	<div class="blank6"></div>
	<div class="content clearfix">
		<div class="fl notebox mr7">
				<div class="title_bar_s1">
                <span class="title_top_s1"><span></span></span>
                <h2><span class="title_more"><a href="fair/list.php" title="<?php echo $this->_tpl_vars['_more']; ?>
"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_new_fair']; ?>
</h2>
                </div>
			    <ul class="hot_list">
					<?php $this->_tag_stack[] = array('fair', array('row' => 10,'magic' => 'y','infolen' => '18','titlelen' => 40)); $_block_repeat=true;smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<li[field:style]>[field:url]</li>
					<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_fair($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				</ul>
		</div>
		<div class="fl latest">
         <!-- the tabs -->
            <ul class="tabs2 title_mouse">
                  <li><a href="offer/list.php?typeid=2"><span><?php echo $this->_tpl_vars['_newest_sell']; ?>
</span></a></li>
                  <li><a href="offer/list.php?typeid=1"><span><?php echo $this->_tpl_vars['_newest_buy']; ?>
</span></a></li>
            </ul>
            <!-- tab "panes" -->
            <div class="panes box_bord">
                <div class="lasestcontbox underline" id="latest-offer-1">
				<div class="lasestcontboxh">
					<ul>
						<?php $this->_tag_stack[] = array('offer', array('typeid' => '2','type' => 'image','row' => 5,'titlelen' => '10')); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
						<li><a href="[link:title]"><img src="[img:thumb]" alt="[field:title]" /></a><p><a href="[link:title]" title="[field:title]">[field:title]</a></p></li>
						<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
					</ul>
				</div>
				<div class="lasestcontboxb">
					<ul>
						<?php $this->_tag_stack[] = array('offer', array('row' => 14,'typeid' => '2')); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
						<li><a href="[link:title]" title="[field:title]">[field:title]</a></li>
						<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
					</ul>
				</div>
			    </div>
                <div class="lasestcontbox underline" id="latest-offer-2" style="display:none;">
				<div class="lasestcontboxh">
					<ul>
						<?php $this->_tag_stack[] = array('offer', array('type' => 'image','row' => 5,'titlelen' => '10','typeid' => '1')); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
						<li><a href="[link:title]"><img src="[img:thumb]" alt="[field:title]" /></a><p><a href="[link:title]" title="[field:title]">[field:title]</a></p></li>
						<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
					</ul>
				</div>
				<div class="lasestcontboxb">
					<ul>
						<?php $this->_tag_stack[] = array('offer', array('typeid' => '1','row' => 14)); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
						<li><a href="[link:title]" title="[field:title]">[field:title]</a></li>
						<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
					</ul>
				</div>
			</div>
            </div>
            <script>
            $(function() {
                $("ul.tabs2").tabs("div.panes > div", {event:'mouseover'});
			
            });
            </script>
		</div>
		<div class="fr notebox">
            <div class="title_bar_s1">
            <span class="title_top_s1"><span></span></span>
            <h2><span class="title_more"><a href="member/purchase.php"><?php echo $this->_tpl_vars['_member_upgrade']; ?>
</a></span><?php echo $this->_tpl_vars['_newest_company']; ?>
</h2>
            </div>
			<div class="vipcont clearfix">
				<ul>
					<?php $this->_tag_stack[] = array('company', array('row' => 13)); $_block_repeat=true;smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<li><a href="[link:title]" title="[field:title]" target="_blank">[field:title]</a></li>
					<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				</ul>
			</div>
		</div>

	</div>
	<div class="blank6"></div>
<div class="content clearfix">

		<div class="type">
        
			<div class="title_bar_s1">
				<span class="title_top_s1"><span></span></span>
				<h4><span class="titlebg_l"></span><span class="titlebg_m title_mouse"><?php echo $this->_tpl_vars['_industry_sort']; ?>
</span><span class="titlebg_r"></span><span class="title_more"><?php echo $this->_tpl_vars['_select_industry_and_post']; ?>
</span></h4>
            </div>
			<div class="hot_search underline"><h3><?php echo $this->_tpl_vars['_hots']; ?>
</h3>
            <div><?php $this->_tag_stack[] = array('tag', array('row' => 15)); $_block_repeat=true;smarty_block_tag($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><a href="[link:url]"  rel="nofollow" target="_blank" title='[field:title]'>[field:title]</a><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_tag($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></div>
            </div>
			<div class="typecont">
            <?php $_from = $this->_tpl_vars['IndustryList']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_0'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_0']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key0'] => $this->_tpl_vars['item0']):
        $this->_foreach['level_0']['iteration']++;
?>
			  <div class="typebox clearfix bg_hover">
                <div class="category_title category_title_width"><a href="<?php echo $this->_tpl_vars['item0']['url']; ?>
"><?php echo $this->_tpl_vars['item0']['name']; ?>
</a></div>
                 <div class="category_content clearfix">
                     <?php if ($this->_tpl_vars['item0']['sub']): ?>
					<ul>
                    	<?php $_from = $this->_tpl_vars['item0']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_1_industry'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_1_industry']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key_level1'] => $this->_tpl_vars['level1']):
        $this->_foreach['level_1_industry']['iteration']++;
?>
						<li>
							<a href="<?php echo $this->_tpl_vars['level1']['url']; ?>
" title="<?php echo $this->_tpl_vars['level1']['name']; ?>
" class="typeboxlet01"><?php echo $this->_tpl_vars['level1']['name']; ?>
</a><br />
                        <?php $_from = $this->_tpl_vars['level1']['sub']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['level_2_industry'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['level_2_industry']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key_level2'] => $this->_tpl_vars['level2']):
        $this->_foreach['level_2_industry']['iteration']++;
?>
							<a href="<?php echo $this->_tpl_vars['level2']['url']; ?>
" title="<?php echo $this->_tpl_vars['level2']['name']; ?>
"><?php echo $this->_tpl_vars['level2']['name']; ?>
</a>
                            <?php if (! ($this->_foreach['level_2_industry']['iteration'] == $this->_foreach['level_2_industry']['total'])): ?>
                            <span>|</span>
                            <?php endif; ?>
                        <?php endforeach; endif; unset($_from); ?>
						</li>
                        <?php endforeach; endif; unset($_from); ?>
					</ul>
                     <?php endif; ?>
                   </div>
			  </div>
                <?php if (! ($this->_foreach['level_0']['iteration'] == $this->_foreach['level_0']['total'])): ?>
				<div class="line"></div>                
                <?php endif; ?>
            <?php endforeach; endif; unset($_from); ?>
			</div>
		</div>
		<div class="fr notebox">
			<div class="fl notebox">
				<div class="title_bar_s1">
               <span class="title_top_s1"><span></span></span>
               <h2><span class="title_more"><a href="office-room/brand.php" title="<?php echo $this->_tpl_vars['_apply_industry_brand']; ?>
" target="_blank"><?php echo $this->_tpl_vars['_apply_industry_brand']; ?>
</a></span><?php echo $this->_tpl_vars['_industry_brand']; ?>
</h2>
              </div>
				<div class="brandcont clearfix">
				<?php $this->_tag_stack[] = array('brand', array('type' => 'image','row' => 10)); $_block_repeat=true;smarty_block_brand($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<li><a href="[link:title]" target="_blank"><img src="[img:src]" alt="[field:title]" /></a><p>[field:title]</p></li>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_brand($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				</div>
				<div class="corner_bottom_bar">
                 <span class="corner_bottom_s1"><span></span></span>
			    </div>
			</div>
			<div class="blank6"></div>
			<a href="http://bbs.phpb2b.com/" title="<?php echo $this->_tpl_vars['_visit_phpb2b_official_site']; ?>
" class="fl" target="_blank"><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/phpb2b_dev.jpg" width="230" height="70" style="border:1px solid #CFCFCF;" /></a>
			<div class="blank6"></div>
			<div class="fl notebox">
				<div class="title_bar_s1">
                <span class="title_top_s1"><span></span></span>
                <h2><span class="title_more"><a href="offer/list.php?typeid=3" title="<?php echo $this->_tpl_vars['_more']; ?>
"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_agent']; ?>
</h2>
                </div>
			   <ul class="hot_list">
				<?php $this->_tag_stack[] = array('offer', array('row' => 9,'typeid' => '1','magic' => 'y','infolen' => '35')); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
				 <li[field:style]>[field:url]</li>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			   </ul>
			</div>
			<div class="blank6"></div>
			<div class="fl notebox">
				 <div class="title_bar_s1">
                <span class="title_top_s1"><span></span></span>
                <h2><span class="title_more"><a href="offer/list.php?typeid=5" title="<?php echo $this->_tpl_vars['_more']; ?>
"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_investment']; ?>
</h2>
                </div>
			   <ul class="hot_list">
				<?php $this->_tag_stack[] = array('offer', array('row' => 9,'magic' => 'y','typeid' => '2','infolen' => '35')); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
				 <li[field:style]>[field:url]</li>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			   </ul>
			</div>
			<div class="blank6"></div>
			<div class="fl notebox">
				<div class="title_bar_s1">
                <span class="title_top_s1"><span></span></span>
                <h2><span class="title_more"><a href="news/list.php?typeid=1" title="<?php echo $this->_tpl_vars['_more']; ?>
"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_site_news']; ?>
</h2>
                </div>
			   <ul class="hot_list">
				<?php $this->_tag_stack[] = array('news', array('typeid' => 4,'row' => 9,'magic' => 'y','titlelen' => 30,'infolen' => 25)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
				 <li[field:style]>[field:url]</li>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			   </ul>
			</div>
			<div class="blank6"></div>
			<div class="fl notebox">
				<div class="title_bar_s1">
                <span class="title_top_s1"><span></span></span>
                <h2><?php echo $this->_tpl_vars['_idea_or_advise']; ?>
</h2>
                </div>
				<div class="hotcont_service">
					<?php echo $this->_tpl_vars['_welcome_your_idea_or_advise']; ?>

					<form action="service/post.php" name="service_frm" id="ServiceFrm" method="post">
					<input type="hidden" name="formhash" value="<?php echo $this->_tpl_vars['formhash']; ?>
">
					<input type="hidden" name="save_service" value="save_service">
					<textarea name="service[content]" cols="" rows=""></textarea>
					<?php echo $this->_tpl_vars['_your_email']; ?>
<input type="text" name="service[email]" value="">
					<div class="blank6"></div>
					<input type="button" name="btn_submit" value="<?php echo $this->_tpl_vars['_submit']; ?>
" class="btn_submit_w45" onclick="$('#ServiceFrm').submit();" />
					<input type="reset" value="<?php echo $this->_tpl_vars['_reset']; ?>
" name="" class="btn_submit_w45"/>
					</form>
				   
				</div>
			</div>
		</div>
	   
	</div>
	<div class="blank6"></div>
	<div class="content">
		<div class="news clearfix">
			<h3 class="newstit title_color break"><strong><?php echo $this->_tpl_vars['_new_products']; ?>
</strong></h3>
			<ul class="clearfix">
			<?php $this->_tag_stack[] = array('product', array('row' => 9,'type' => 'image','titlelen' => '10')); $_block_repeat=true;smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
				<li><a href="[link:title]"><img src="[img:src]" alt="[field:title]" /></a>
                <p><a href="[link:title]">[field:title]</a></p>
                </li>
			<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_product($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			</ul>
		</div>
	</div>
	<div class="blank6"></div>
	<div class="content">
		<div class="corner_2px_t">
           <div class="corner_2px_l"></div>
           <div class="corner_2px_r"></div>
		</div>
		<div class="bottomc">
			<div class="fl newscar mr7">
            
                 <!-- the tabs -->
                <ul class="tabs2 fxg_width title_mouse">
                      <li> <?php $this->_tag_stack[] = array('newstype', array('id' => 4)); $_block_repeat=true;smarty_block_newstype($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><a href="[link:title]"><span>[field:title]</span></a><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_newstype($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></li>
                      <li><?php $this->_tag_stack[] = array('newstype', array('id' => 1)); $_block_repeat=true;smarty_block_newstype($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?><a href="[link:title]"><span>[field:title]</span></a><?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_newstype($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?></li>
                </ul>
                <!-- tab "panes" -->
                <div class="panes box_bord">
                   	<div class="law" id="Newstype1List">
					<ul class="law_list">
					<?php $this->_tag_stack[] = array('news', array('magic' => 'y','typeid' => 2,'row' => 9,'titlelen' => 34,'infolen' => 35)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<li[field:style]>[field:url]</li>
					<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
					</ul>
				</div>
				    <div class="law" id="Newstype2List" style="display:none;">
					<ul class="law_list">
					<?php $this->_tag_stack[] = array('news', array('magic' => 'y','typeid' => 1,'row' => 9,'titlelen' => 34,'infolen' => 35)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<li[field:style]>[field:url]</li>
					<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				 </ul>
				</div>
                </div>
				
			</div>
			<div class="fl latest02">
				<div class="title_bar_s1">
                <span class="title_top_s1"><span></span></span>
                <h2><span class="title_more"><a href="news/list.php?type=hot" title="<?php echo $this->_tpl_vars['_more']; ?>
"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><?php echo $this->_tpl_vars['_hot_news']; ?>
</h2>
                </div>
				<div class="newscarcontbox">
					<div class="lasestcontboxh02">
						<ul>
							<?php $this->_tag_stack[] = array('news', array('row' => 4,'type' => 'image','titlelen' => '10')); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
							<li><a href="[link:title]"><img src="[img:src]" alt="[field:title]" /></a>
                            <p><a href="[link:title]" title="[field:title]">[field:title]</a></p>
                            </li>
							<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
						</ul>
					</div>
					<div class="newscarcontboxb">
						<ul>
							<?php $this->_tag_stack[] = array('news', array('row' => 14)); $_block_repeat=true;smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
							<li><a href="[link:title]" title="[field:title]">[field:title]</a></li>
							<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_news($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
						</ul>
					</div>
				</div>
			</div>
			<div class="fr hotzt">
				<div class="hotztbox">
                   <div class="title">
					<span class="more"><a href="news/list.php?type=topic"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_more']; ?>
</a></span><h2><?php echo $this->_tpl_vars['_topic_news']; ?>
</h2>
                    </div>
                     <ul>
					<?php $this->_tag_stack[] = array('topic', array('type' => 'news','row' => 3)); $_block_repeat=true;smarty_block_topic($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
                         <li>
                           <a href="[link:title]" title=""><img src="[img:src]"/></a>
                           <div><a href="[link:title]" title="">[field:title]</a></div>
                         </li>
					<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_topic($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
                     </ul>
				</div>
			</div>
		</div>
		<div class="corner_2px_b">
		    <div class="corner_2px_bl"></div>
            <div class="corner_2px_br"></div>
		</div>
	</div>
	<div class="blank6"></div>
	<div class="content">
		<div class="pardent">
			<p><a href="friendlink.php" title="<?php echo $this->_tpl_vars['_apply_friendlink']; ?>
" class="fr"><span class="raquo">&raquo;</span><?php echo $this->_tpl_vars['_apply_friendlink']; ?>
</a></p>
			<div class="pardentbox clearfix">
			<table width="100%" border="0">
			  <tr>
				<th class="friend_link"><?php echo $this->_tpl_vars['_friend_links']; ?>
:</th>
				<td class="friend_link_content">
				<?php $this->_tag_stack[] = array('friendlink', array('typeid' => '1')); $_block_repeat=true;smarty_block_friendlink($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<a href="[link:title]" title="[field:tip]" target="_blank">[field:title]</a><span>|</span>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_friendlink($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				</td>
			  </tr>
			  <tr>
				<th class="friend_link"><?php echo $this->_tpl_vars['_membership_links']; ?>
:</th>
				<td class="friend_link_content">
				<?php $this->_tag_stack[] = array('friendlink', array('typeid' => '2')); $_block_repeat=true;smarty_block_friendlink($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
					<a href="[link:title]" title="[field:tip]" target="_blank">[field:title]</a><span>|</span>
				<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_friendlink($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
				</td>
			  </tr>
			</table>
			</div>
		</div>
	</div>
</div>
<script>
$("#topMenuOffer").addClass("lcur");
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>