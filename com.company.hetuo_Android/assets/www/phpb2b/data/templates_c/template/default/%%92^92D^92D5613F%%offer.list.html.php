<?php /* Smarty version 2.6.18, created on 2010-09-09 14:45:05
         compiled from default%5Coffer.list.html */ ?>
<?php require_once(SMARTY_CORE_DIR . 'core.load_plugins.php');
smarty_core_load_plugins(array('plugins' => array(array('function', 'html_options', 'default\\offer.list.html', 28, false),array('modifier', 'truncate', 'default\\offer.list.html', 67, false),array('block', 'offer', 'default\\offer.list.html', 115, false),array('block', 'company', 'default\\offer.list.html', 122, false),)), $this); ?>
<?php $this->assign('page_title', ($this->_tpl_vars['page_title'])); ?>
<?php $this->assign('nav_id', ($this->_tpl_vars['nav_id'])); ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/header.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<div class="wrapper">
	<div class="tips"><span><?php echo $this->_tpl_vars['position']; ?>
</span></div>
    <div class="blank6"></div>
	<div class="kinds">
		<p>
		<span><?php echo $this->_tpl_vars['_sort_screening']; ?>
</span>
		<?php $_from = $this->_tpl_vars['TradeTypes']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }if (count($_from)):
    foreach ($_from as $this->_tpl_vars['type_key'] => $this->_tpl_vars['type_item']):
?>
		<a href="offer/list.php?<?php echo $this->_tpl_vars['addParams']; ?>
typeid=<?php echo $this->_tpl_vars['type_key']; ?>
" title="<?php echo $this->_tpl_vars['type_item']; ?>
"><?php echo $this->_tpl_vars['type_item']; ?>
</a>
		<?php endforeach; endif; unset($_from); ?>
		</p>
		<p>
		<span><?php echo $this->_tpl_vars['_industry_screening']; ?>
</span>
		<?php $_from = $this->_tpl_vars['OtherIndustry']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['subindustry'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['subindustry']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['key1'] => $this->_tpl_vars['item1']):
        $this->_foreach['subindustry']['iteration']++;
?>
		<a href="offer/list.php?<?php echo $this->_tpl_vars['addParams']; ?>
industryid=<?php echo $this->_tpl_vars['key1']; ?>
" title="<?php echo $this->_tpl_vars['item1']; ?>
"><?php echo $this->_tpl_vars['item1']; ?>
</a>
		<?php endforeach; else: ?>
		<a><?php echo $this->_tpl_vars['_no_subclass']; ?>
</a>
		<?php endif; unset($_from); ?>
		</p>
		<form name="search_frm" id="OfferSearchFrm" action="" method="get">
		<p class="group2">
		<span><?php echo $this->_tpl_vars['_fast_screening']; ?>
</span>
		<label>
		<select name="industryid" onchange="$('#OfferSearchFrm').submit();">
			<option value="0"><?php echo $this->_tpl_vars['_all_sort']; ?>
</option>
			<?php echo smarty_function_html_options(array('options' => $this->_tpl_vars['OtherIndustry'],'selected' => $_GET['industryid']), $this);?>

		</select>
		</label>
		<label>
			<select name="areaid" onchange="$('#OfferSearchFrm').submit();">
				<option value="0"><?php echo $this->_tpl_vars['_all_area']; ?>
</option>
				<?php echo smarty_function_html_options(array('options' => $this->_tpl_vars['OtherArea'],'selected' => $_GET['areaid']), $this);?>

			</select>
		</label>
		<label><input type="submit"  value="<?php echo $this->_tpl_vars['_screening']; ?>
" class="submit"/>
		</label>
		<div class="clear"></div>
		</p>
		</form>
	</div>

<div class="qiugoucontent clearfix">
	<div class="qiugoucontentleft">
    <div class="base_title">
        <span class="more"><a href="help/"><?php echo $this->_tpl_vars['_how_bump_to_top']; ?>
</a></span>
        <h2><span class="corner_t_l"></span><span class="corner_t_m title_mouse"><?php echo $this->_tpl_vars['_search_result']; ?>
</span><span class="corner_t_r"></span></h2>
	</div>
		<div class="qiugouleftcon box_bord">
			<form name="offer_list_frm">
            <table>
				<tr>
					<th><?php echo $this->_tpl_vars['_picture']; ?>
</th>
					<th><?php echo $this->_tpl_vars['_offer']; ?>
/<?php echo $this->_tpl_vars['_publisher']; ?>
</th>
					<th></th>
					<th><?php echo $this->_tpl_vars['_area_n']; ?>
</th>
					<th><?php echo $this->_tpl_vars['_member_group']; ?>
</th>
					<th><?php echo $this->_tpl_vars['_contact_us']; ?>
</th>
				</tr>
				<?php if ($this->_tpl_vars['StickyItems']): ?>
					<?php $_from = $this->_tpl_vars['StickyItems']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['sticky_offer'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['sticky_offer']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['sticky']):
        $this->_foreach['sticky_offer']['iteration']++;
?>
					<tr style="background:#FFF8DD;">
					<td class="offer_img"><img src="<?php echo $this->_tpl_vars['sticky']['image']; ?>
" border=0 alt="<?php echo $this->_tpl_vars['sticky']['title']; ?>
"></td>
					<td class="title_link">
					<p><span><a href="<?php echo $this->_tpl_vars['sticky']['url']; ?>
" title="<?php echo $this->_tpl_vars['sticky']['title']; ?>
"><?php echo $this->_tpl_vars['sticky']['title']; ?>
</a></span>[<?php echo $this->_tpl_vars['sticky']['pubdate']; ?>
]</p>
					<p><?php echo $this->_tpl_vars['_abstract']; ?>
<?php echo ((is_array($_tmp=$this->_tpl_vars['sticky']['content'])) ? $this->_run_mod_handler('truncate', true, $_tmp, 100) : smarty_modifier_truncate($_tmp, 100)); ?>
</p>
					<p><?php echo $this->_tpl_vars['_publisher']; ?>
<?php if ($this->_tpl_vars['sticky']['companyname']): ?><a href="space.php?userid=<?php echo $this->_tpl_vars['sticky']['username']; ?>
"><?php echo $this->_tpl_vars['sticky']['companyname']; ?>
</a><?php else: ?><?php echo $this->_tpl_vars['sticky']['username']; ?>
<?php endif; ?></p>     
					</td>
					<td><?php if ($this->_tpl_vars['sticky']['if_commend'] > 0): ?><span class="icon-commend"></span><?php endif; ?><?php if ($this->_tpl_vars['sticky']['if_urgent'] > 0): ?><span class="icon-hurry"></span><?php endif; ?><img src="images/top.gif" alt="<?php echo $this->_tpl_vars['_bump_to_top']; ?>
" /></td>
					<td><p><?php if ($this->_tpl_vars['sticky']['area_id1']): ?><?php echo $this->_tpl_vars['Areas'][1][$this->_tpl_vars['sticky']['area_id1']]; ?>
<br /><?php echo $this->_tpl_vars['Areas'][2][$this->_tpl_vars['sticky']['area_id2']]; ?>
<?php endif; ?></p></td>
					<td><?php if ($this->_tpl_vars['sticky']['gradename']): ?><img src="<?php echo $this->_tpl_vars['sticky']['gradeimg']; ?>
" alt="<?php echo $this->_tpl_vars['sticky']['gradename']; ?>
" ><?php endif; ?></td>
					<td>					
						<?php echo $this->_tpl_vars['sticky']['im']; ?>

						<?php if ($this->_tpl_vars['sticky']['companyname'] != ""): ?>
						<a href="space.php?do=feedback&userid=<?php echo $this->_tpl_vars['sticky']['userid']; ?>
"><span class="im_pms"><?php echo $this->_tpl_vars['_station_message']; ?>
</span></a>
						<?php elseif ($this->_tpl_vars['sticky']['username']): ?>
						<a href="office-room/pms.php?do=send&to=<?php echo $this->_tpl_vars['sticky']['username']; ?>
"><span class="im_pms"><?php echo $this->_tpl_vars['_send_message']; ?>
</span></a>
						<?php endif; ?>
					</td>
					</tr>
					<?php endforeach; endif; unset($_from); ?>
				<?php endif; ?>
				<?php $_from = $this->_tpl_vars['Items']; if (!is_array($_from) && !is_object($_from)) { settype($_from, 'array'); }$this->_foreach['offer'] = array('total' => count($_from), 'iteration' => 0);
if ($this->_foreach['offer']['total'] > 0):
    foreach ($_from as $this->_tpl_vars['item']):
        $this->_foreach['offer']['iteration']++;
?>
				<tr>
				<td class="offer_img"><a href="<?php echo $this->_tpl_vars['item']['url']; ?>
"><img src="<?php echo $this->_tpl_vars['item']['image']; ?>
" border=0 alt="<?php echo $this->_tpl_vars['item']['title']; ?>
"></a></td>
				<td class="title_link list_offer_content_width">
				<p><span><a href="<?php echo $this->_tpl_vars['item']['url']; ?>
" title="<?php echo $this->_tpl_vars['item']['title']; ?>
"><?php echo $this->_tpl_vars['item']['title']; ?>
</a></span><span class="gray">[<?php echo $this->_tpl_vars['item']['pubdate']; ?>
]</span></p>
				<p><?php echo $this->_tpl_vars['_abstract']; ?>
<?php echo ((is_array($_tmp=$this->_tpl_vars['item']['content'])) ? $this->_run_mod_handler('truncate', true, $_tmp, 100) : smarty_modifier_truncate($_tmp, 100)); ?>
</p>
				<p><?php echo $this->_tpl_vars['_publisher']; ?>
<?php if ($this->_tpl_vars['item']['companyname']): ?><a href="space.php?userid=<?php echo $this->_tpl_vars['item']['username']; ?>
"><?php echo $this->_tpl_vars['item']['companyname']; ?>
</a><?php else: ?><?php echo $this->_tpl_vars['item']['username']; ?>
<?php endif; ?></p>     
				</td>
				<td><?php if ($this->_tpl_vars['item']['if_commend'] > 0): ?><span class="icon-commend"></span><?php endif; ?><?php if ($this->_tpl_vars['item']['if_urgent'] > 0): ?><span class="icon-hurry"></span><?php endif; ?></td>
				<td><p><?php if ($this->_tpl_vars['item']['area_id1']): ?><?php echo $this->_tpl_vars['Areas'][1][$this->_tpl_vars['item']['area_id1']]; ?>
<br /><?php echo $this->_tpl_vars['Areas'][2][$this->_tpl_vars['item']['area_id2']]; ?>
<?php endif; ?></p></td>
				<td><?php if ($this->_tpl_vars['item']['gradename']): ?><img src="<?php echo $this->_tpl_vars['item']['gradeimg']; ?>
" alt="<?php echo $this->_tpl_vars['item']['gradename']; ?>
" ><?php endif; ?></td>
				<td>
					<?php echo $this->_tpl_vars['item']['im']; ?>

					<?php if ($this->_tpl_vars['item']['companyname'] != ""): ?>
					<a href="space.php?do=feedback&userid=<?php echo $this->_tpl_vars['item']['userid']; ?>
"><span class="im_pms"><?php echo $this->_tpl_vars['_station_message']; ?>
</span></a>
					<?php elseif ($this->_tpl_vars['item']['username']): ?>
					<a href="office-room/pms.php?do=send&to=<?php echo $this->_tpl_vars['item']['username']; ?>
"><span class="im_pms"><?php echo $this->_tpl_vars['_send_message']; ?>
</span></a>
					<?php endif; ?>
				 </td>
				</tr>
				<?php endforeach; endif; unset($_from); ?>
            </table>
			<div>
				<span><?php echo $this->_tpl_vars['ByPages']; ?>
</span>
			</div>
			</form>
		</div>
	</div>
	<div class="qiugoucontentright">
		<div class="recommendcompany">
			<div class="recommendcompanytop"><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/lhighs.gif" />&nbsp;<?php echo $this->_tpl_vars['_commend']; ?>
<?php echo $this->_tpl_vars['_offer']; ?>
</div>
			<?php $this->_tag_stack[] = array('offer', array('row' => 10,'type' => 'commend','typeid' => ($this->_tpl_vars['typeid']))); $_block_repeat=true;smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
			<p><a href="[link:title]">[field:title]</a></p>
			<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_offer($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			<div class="clear"></div>
		</div>
		<div class="recommendcompany">
			<div class="recommendcompanytop"><img src="<?php echo $this->_tpl_vars['theme_img_path']; ?>
images/lhighs.gif" />&nbsp;<?php echo $this->_tpl_vars['_recommended_company']; ?>
</div>
			<?php $this->_tag_stack[] = array('company', array('row' => 15)); $_block_repeat=true;smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], null, $this, $_block_repeat);while ($_block_repeat) { ob_start(); ?>
			<p><a href="[link:title]" title="[field:fulltitle]">[field:title]</a></p>
			<?php $_block_content = ob_get_contents(); ob_end_clean(); $_block_repeat=false;echo smarty_block_company($this->_tag_stack[count($this->_tag_stack)-1][1], $_block_content, $this, $_block_repeat); }  array_pop($this->_tag_stack); ?>
			<div class="clear"></div>
		</div>
</div>
</div>
<script language="javascript" src="scripts/highlight.js"></script>
<script>
$("#SearchFrm").attr("action","offer/list.php");
$("#topMenuOffer").addClass("lcur");
$(".qiugouleftcon").highlight(["<?php echo $this->_tpl_vars['highlight_str']; ?>
"]);
</script>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['ThemeName'])."/footer.html", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>