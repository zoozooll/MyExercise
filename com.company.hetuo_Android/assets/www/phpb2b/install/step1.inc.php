<?php include 'header.share.php';?>
	<div class="content">
		<div id="installdiv">
		  <h3><?php echo $_welcome_to_install;?> <?php echo $_software_name;?> <?php echo PHPB2B_VERSION."(".PHPB2B_RELEASE.strtoupper($charset).")";?></h3>
		  <ul>
			<li>
			<p><br />
			<form name="language" id="language" action="install.php" method="get">
			<?php echo $_select_language;?> : <select name="app_lang"><?php echo showLanguages();?></select><input type="button" name="switch_language" id="SwitchLanguage" onClick="$('#language').submit();" value="<?php echo $_language_switch;?>" />&nbsp;
			<img src="images/help.gif" style="cursor:pointer;" title="<?php echo $_if_want_to_change_language;?>" align="absmiddle" />
			</form></p>
			<p><br /><?php echo $_the_guide_to_install;?></p>
			<p><br /><?php echo $_to_install_please_attention;?></p>

	<ul>
		<li>MySQL 3.23 <?php echo $_or_higher_version;?></li>
		<li>PHP 4.3.0 <?php echo $_or_higher_version;?></li>
	</ul>

	<p><strong><?php echo $_attention;?></strong> <?php echo $_software_name;?><?php echo $_mysql_only_suppport;?></p>
	</li>
		  </ul>
		</div>
		<br />
		<input type="button" class="btn" onClick="$('#install').submit();" value="<?php echo $_start_to_install.$_software_name;?>" title="<?php echo $_click_and_next;?>" />
	</div>
	<form id="install" action="install.php" method="get">
	<input type="hidden" name="step" value="2">
	</form>
  </div>
</div>
</body>
</html>