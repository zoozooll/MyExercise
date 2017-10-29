	<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
		<div>
			<div class="fLeft<?php if($this->__muant["value"]["$valuei"]["cpid"]==0) { ?> fb<?php } ?>" id="classes">
			<a><?php echo $this->__muant["value"]["$valuei"]["_name"] ?></a>
			</div>
			<div id="sortid" class="fLeft">
			<a title="修改此类别" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>" target="adminMain">修改</a>
            <a title="移动此类别" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>&mv=1" target="adminMain">移动</a> 
			<?php if($this->__muant["value"]["$valuei"]["ccidnum"]==0) { ?>
			<a onClick="return delYesOrNo();" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>&act=del" target="adminMain">删除</a><?php } else { ?><font color="#CCCCCC">删除</font>
			<?php } ?>
			</div>
			<div>
				<?php if($this->__muant["value"]["$valuei"]["cpid"]>'0') { ?>
				<?php if($this->__muant["value"]["$valuei"]["up"]=='1') { ?><a href="#" onClick="sort_sub_class('<?php echo $this->__muant["value"]["$valuei"]["id"] ?>','up')" title="同级上移">↑</a><?php } ?>
				<?php if($this->__muant["value"]["$valuei"]["down"]=='1') { ?><a href="#" onClick="sort_sub_class('<?php echo $this->__muant["value"]["$valuei"]["id"] ?>','down')" title="同级下移">↓</a><?php } ?>
				<?php } else { ?>
				<input name="sort_id[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>">
				<input name="sort_class[]" type="text" size="1" maxlength="3" class="txInput" value="<?php echo $this->__muant["value"]["$valuei"]["sort"] ?>">
				<?php } ?>
			</div>
		<?php if($this->__muant["value"]["$valuei"+1]["cpid"]==0) { ?>
		<?php } ?>
		</div>
		<div class="cls"></div>
	<?php } ?>
<div id="classes" class="fLeft">&nbsp;</div><div id="sortid" class="fLeft">&nbsp;</div>
<div class="fLeft"><input onClick="sort_class()" name="sortclass" type="button" class="txInput" value="排序"></div>
<div class="cls"></div>