<?php include 'header.share.php';?>
	<div class="content" style="height:350px; overflow:auto;"><?php echo nl2br(str_replace(' ', '&nbsp;', htmlspecialchars($license)));?></div>
	<form id="install" action="install.php" method="get">
<input type="hidden" name="step" value="3">
 </form>
<a onclick="javascript:history.go(-1);" class="btn" title="<?php echo $_go_back;?>"><?php echo $_go_back;?> : <?php echo $steps[--$step];?></a>
<a onClick="$('#install').submit();" class="btn" title="<?php echo $_env_check;?>"><span><?php echo $_agree_go_next;?></span></a>
  </div>
</div>
</body>
</html>

