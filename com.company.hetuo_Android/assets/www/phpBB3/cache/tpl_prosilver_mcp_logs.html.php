<?php if (!defined('IN_PHPBB')) exit; $this->_tpl_include('mcp_header.html'); ?>


<h2><?php echo ((isset($this->_rootref['L_TITLE'])) ? $this->_rootref['L_TITLE'] : ((isset($user->lang['TITLE'])) ? $user->lang['TITLE'] : '{ TITLE }')); ?></h2>

<form method="post" id="mcp" action="<?php echo (isset($this->_rootref['U_POST_ACTION'])) ? $this->_rootref['U_POST_ACTION'] : ''; ?>">

<div class="panel">
	<div class="inner"><span class="corners-top"><span></span></span>

	<ul class="linklist">
		<li class="leftside">
			<?php echo ((isset($this->_rootref['L_SEARCH_KEYWORDS'])) ? $this->_rootref['L_SEARCH_KEYWORDS'] : ((isset($user->lang['SEARCH_KEYWORDS'])) ? $user->lang['SEARCH_KEYWORDS'] : '{ SEARCH_KEYWORDS }')); ?>: <input type="text" class="inputbox autowidth" name="keywords" value="<?php echo (isset($this->_rootref['S_KEYWORDS'])) ? $this->_rootref['S_KEYWORDS'] : ''; ?>" />&nbsp;<input type="submit" class="button2" name="filter" value="<?php echo ((isset($this->_rootref['L_SEARCH'])) ? $this->_rootref['L_SEARCH'] : ((isset($user->lang['SEARCH'])) ? $user->lang['SEARCH'] : '{ SEARCH }')); ?>" />
		</li>
		<li class="rightside pagination">
			<?php if ($this->_rootref['TOTAL']) {  echo (isset($this->_rootref['TOTAL'])) ? $this->_rootref['TOTAL'] : ''; ?> <?php } if ($this->_rootref['PAGE_NUMBER']) {  if ($this->_rootref['PAGINATION']) {  ?> &bull; <a href="#" onclick="jumpto(); return false;" title="<?php echo ((isset($this->_rootref['L_JUMP_TO_PAGE'])) ? $this->_rootref['L_JUMP_TO_PAGE'] : ((isset($user->lang['JUMP_TO_PAGE'])) ? $user->lang['JUMP_TO_PAGE'] : '{ JUMP_TO_PAGE }')); ?>"><?php echo (isset($this->_rootref['PAGE_NUMBER'])) ? $this->_rootref['PAGE_NUMBER'] : ''; ?></a> &bull; <span><?php echo (isset($this->_rootref['PAGINATION'])) ? $this->_rootref['PAGINATION'] : ''; ?></span><?php } else { ?> &bull; <?php echo (isset($this->_rootref['PAGE_NUMBER'])) ? $this->_rootref['PAGE_NUMBER'] : ''; } } ?>

		</li>
	</ul>

	<table cellspacing="1" class="table1">
	<thead>
	<tr>
		<th><?php echo ((isset($this->_rootref['L_USERNAME'])) ? $this->_rootref['L_USERNAME'] : ((isset($user->lang['USERNAME'])) ? $user->lang['USERNAME'] : '{ USERNAME }')); ?></th>
		<th style="text-align: center"><?php echo ((isset($this->_rootref['L_IP'])) ? $this->_rootref['L_IP'] : ((isset($user->lang['IP'])) ? $user->lang['IP'] : '{ IP }')); ?></th>
		<th style="text-align: center"><?php echo ((isset($this->_rootref['L_TIME'])) ? $this->_rootref['L_TIME'] : ((isset($user->lang['TIME'])) ? $user->lang['TIME'] : '{ TIME }')); ?></th>
		<th><?php echo ((isset($this->_rootref['L_ACTION'])) ? $this->_rootref['L_ACTION'] : ((isset($user->lang['ACTION'])) ? $user->lang['ACTION'] : '{ ACTION }')); ?></th>
		<?php if ($this->_rootref['S_CLEAR_ALLOWED']) {  ?><th><?php echo ((isset($this->_rootref['L_MARK'])) ? $this->_rootref['L_MARK'] : ((isset($user->lang['MARK'])) ? $user->lang['MARK'] : '{ MARK }')); ?></th><?php } ?>

	</tr>
	</thead>
		<tbody>
	<?php if ($this->_rootref['S_LOGS']) {  $_log_count = (isset($this->_tpldata['log'])) ? sizeof($this->_tpldata['log']) : 0;if ($_log_count) {for ($_log_i = 0; $_log_i < $_log_count; ++$_log_i){$_log_val = &$this->_tpldata['log'][$_log_i]; if (!($_log_val['S_ROW_COUNT'] & 1)  ) {  ?><tr class="bg1"><?php } else { ?><tr class="bg2"><?php } ?>

			<td><?php echo $_log_val['USERNAME']; ?></td>
			<td style="text-align: center"><?php echo $_log_val['IP']; ?></td>
			<td style="text-align: center"><?php echo $_log_val['DATE']; ?></td>
			<td><?php echo $_log_val['ACTION']; ?><br />
			<?php echo $_log_val['DATA']; ?>

		</td>
			<?php if ($this->_rootref['S_CLEAR_ALLOWED']) {  ?><td style="width: 5%" align="center"><input type="checkbox" name="mark[]" value="<?php echo $_log_val['ID']; ?>" /></td><?php } ?>

		</tr>
		<?php }} } else { ?>

		<tr>
			<td class="bg1" colspan="<?php if ($this->_rootref['S_CLEAR_ALLOWED']) {  ?>5<?php } else { ?>4<?php } ?>" align="center"><span class="gen"><?php echo ((isset($this->_rootref['L_NO_ENTRIES'])) ? $this->_rootref['L_NO_ENTRIES'] : ((isset($user->lang['NO_ENTRIES'])) ? $user->lang['NO_ENTRIES'] : '{ NO_ENTRIES }')); ?></span></td>
		</tr>
	<?php } ?>

	</tbody>
	</table>

	<?php if (sizeof($this->_tpldata['log'])) {  ?>

		<fieldset class="display-options">
			<?php if ($this->_rootref['NEXT_PAGE']) {  ?><a href="<?php echo (isset($this->_rootref['NEXT_PAGE'])) ? $this->_rootref['NEXT_PAGE'] : ''; ?>" class="right-box <?php echo (isset($this->_rootref['S_CONTENT_FLOW_END'])) ? $this->_rootref['S_CONTENT_FLOW_END'] : ''; ?>"><?php echo ((isset($this->_rootref['L_NEXT'])) ? $this->_rootref['L_NEXT'] : ((isset($user->lang['NEXT'])) ? $user->lang['NEXT'] : '{ NEXT }')); ?></a><?php } if ($this->_rootref['PREVIOUS_PAGE']) {  ?><a href="<?php echo (isset($this->_rootref['PREVIOUS_PAGE'])) ? $this->_rootref['PREVIOUS_PAGE'] : ''; ?>" class="left-box <?php echo (isset($this->_rootref['S_CONTENT_FLOW_BEGIN'])) ? $this->_rootref['S_CONTENT_FLOW_BEGIN'] : ''; ?>"><?php echo ((isset($this->_rootref['L_PREVIOUS'])) ? $this->_rootref['L_PREVIOUS'] : ((isset($user->lang['PREVIOUS'])) ? $user->lang['PREVIOUS'] : '{ PREVIOUS }')); ?></a><?php } ?>

			<label><?php echo ((isset($this->_rootref['L_DISPLAY_POSTS'])) ? $this->_rootref['L_DISPLAY_POSTS'] : ((isset($user->lang['DISPLAY_POSTS'])) ? $user->lang['DISPLAY_POSTS'] : '{ DISPLAY_POSTS }')); ?>: <?php echo (isset($this->_rootref['S_SELECT_SORT_DAYS'])) ? $this->_rootref['S_SELECT_SORT_DAYS'] : ''; ?></label>
			<label><?php echo ((isset($this->_rootref['L_SORT_BY'])) ? $this->_rootref['L_SORT_BY'] : ((isset($user->lang['SORT_BY'])) ? $user->lang['SORT_BY'] : '{ SORT_BY }')); ?> <?php echo (isset($this->_rootref['S_SELECT_SORT_KEY'])) ? $this->_rootref['S_SELECT_SORT_KEY'] : ''; ?></label>
			<label><?php echo (isset($this->_rootref['S_SELECT_SORT_DIR'])) ? $this->_rootref['S_SELECT_SORT_DIR'] : ''; ?></label>
			<input type="submit" name="sort" value="<?php echo ((isset($this->_rootref['L_GO'])) ? $this->_rootref['L_GO'] : ((isset($user->lang['GO'])) ? $user->lang['GO'] : '{ GO }')); ?>" class="button2" />
		</fieldset>

		<hr />

		<ul class="linklist">
			<li class="rightside pagination">
				<?php if ($this->_rootref['TOTAL']) {  echo (isset($this->_rootref['TOTAL'])) ? $this->_rootref['TOTAL'] : ''; ?> <?php } if ($this->_rootref['PAGE_NUMBER']) {  if ($this->_rootref['PAGINATION']) {  ?> &bull; <a href="#" onclick="jumpto(); return false;" title="<?php echo ((isset($this->_rootref['L_JUMP_TO_PAGE'])) ? $this->_rootref['L_JUMP_TO_PAGE'] : ((isset($user->lang['JUMP_TO_PAGE'])) ? $user->lang['JUMP_TO_PAGE'] : '{ JUMP_TO_PAGE }')); ?>"><?php echo (isset($this->_rootref['PAGE_NUMBER'])) ? $this->_rootref['PAGE_NUMBER'] : ''; ?></a> &bull; <span><?php echo (isset($this->_rootref['PAGINATION'])) ? $this->_rootref['PAGINATION'] : ''; ?></span><?php } else { ?> &bull; <?php echo (isset($this->_rootref['PAGE_NUMBER'])) ? $this->_rootref['PAGE_NUMBER'] : ''; } } ?>

			</li>
		</ul>
		<?php echo (isset($this->_rootref['S_FORM_TOKEN'])) ? $this->_rootref['S_FORM_TOKEN'] : ''; ?>

		<span class="corners-bottom"><span></span></span></div>
	</div>

		<?php if ($this->_rootref['S_CLEAR_ALLOWED']) {  ?>

			<fieldset class="display-actions">
				<input class="button2" type="submit" name="action[del_all]" value="<?php echo ((isset($this->_rootref['L_DELETE_ALL'])) ? $this->_rootref['L_DELETE_ALL'] : ((isset($user->lang['DELETE_ALL'])) ? $user->lang['DELETE_ALL'] : '{ DELETE_ALL }')); ?>" />
				&nbsp;<input class="button1" type="submit" value="<?php echo ((isset($this->_rootref['L_DELETE_MARKED'])) ? $this->_rootref['L_DELETE_MARKED'] : ((isset($user->lang['DELETE_MARKED'])) ? $user->lang['DELETE_MARKED'] : '{ DELETE_MARKED }')); ?>" name="action[del_marked]" />

				<div><a href="#" onclick="marklist('mcp', 'mark', true); return false;"><?php echo ((isset($this->_rootref['L_MARK_ALL'])) ? $this->_rootref['L_MARK_ALL'] : ((isset($user->lang['MARK_ALL'])) ? $user->lang['MARK_ALL'] : '{ MARK_ALL }')); ?></a> :: <a href="#" onclick="marklist('mcp', 'mark', false); return false;"><?php echo ((isset($this->_rootref['L_UNMARK_ALL'])) ? $this->_rootref['L_UNMARK_ALL'] : ((isset($user->lang['UNMARK_ALL'])) ? $user->lang['UNMARK_ALL'] : '{ UNMARK_ALL }')); ?></a></div>
			</fieldset>
		<?php } } else { ?>

			<?php echo (isset($this->_rootref['S_FORM_TOKEN'])) ? $this->_rootref['S_FORM_TOKEN'] : ''; ?>

			<span class="corners-bottom"><span></span></span></div>
		</div>
	<?php } ?>

</form>

<br />

<?php $this->_tpl_include('mcp_footer.html'); ?>