<?php include 'header.share.php';?>
   <?php if(!empty($db_error)){ ?>
   
    <div id="installmessage" style="height:150px; overflow:auto;" class="content"><?php echo $_db_connect_error;?><br />
    </div>
	<a href="javascript:history.go(-2);" class="btn"><?php echo $_go_back;?> : <?php echo $_db_setting;?></a>
	<?php }else{ ?>
	 <div id="installmessage" style="height:50px; overflow:auto; line-height:50px; font-size:28px; font-weight:bold;  padding-left:215px;" class="content"><?php echo $_install_success;?></div>
     <div class="installmessage_img"></div>
     <div class="suc">
	<p><?php echo $_browse_site;?> : <a href="<?php echo $siteUrl;?>" target="_blank"><?php echo $siteUrl;?></a></p>
	<p><?php echo $_login_into_office;?> : <a href="<?php echo $siteUrl;?>logging.php" target="_blank"><?php echo $siteUrl;?>logging.php</a></p>
	<p><?php echo $_login_into_admin;?> : <a href="<?php echo $siteUrl;?>pb-admin/login.php" target="_blank"><?php echo $siteUrl;?>pb-admin/login.php</a></p>

	</div>
	
	<?php } ?>
<form id="install" action="install.php?step=7" method="post">
<input type="hidden" name="step" value="7">
</form>
</div>
</body>
</html>