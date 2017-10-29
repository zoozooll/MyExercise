<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

$asd_position_info = array(
	'position_name'	=> '',
	'asd_width'		=> '100',
	'asd_height'	=> '100',
	'position_desc'	=> ''
);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
td span {color:red;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_asd_management;?> &gt;&gt; <?php echo $a_langpackage->a_add_asdposition; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_add_asdposition; ?></span> <span class="right" style="margin-right:15px;"><a style="float:right;" href="m.php?app=asd_position_list"><?php echo $a_langpackage->a_asdposition_list; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=asd_position_add" method="post">
		<table class="form-table">
		  <tbody>
			<tr>
				<td width="72px;"><?php echo $a_langpackage->a_asdposition_name; ?>：</td>
				<td><input type="text" class="small-text" name="position_name" value="<?php echo $asd_position_info['position_name']; ?>" style="width:200px" /> <span id="position_name_message">*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asdposition_width; ?>：</td>
				<td><input type="text" class="small-text" name="asd_width" value="<?php echo $asd_position_info['asd_width']; ?>" style="width:50px;" /> px</td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asdposition_height; ?>：</td>
				<td><input type="text" class="small-text" name="asd_height" value="<?php echo $asd_position_info['asd_height']; ?>" style="width:50px;" /> px</td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asdposition_desc; ?>：</td>
				<td><textarea name="position_desc" style="width:350px; height:100px;"><?php echo $asd_position_info['position_desc']; ?></textarea></td>
			</tr>
			<tr>
				<td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_add_asdposition; ?>" /></span></td>
			</tr>
		  </tbody>
		</table>
		</form>
	   </div>
	 </div>
  </div>
</div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script>
var position_name = document.getElementsByName("position_name")[0];
var position_name_message = document.getElementById("position_name_message");
var submit = document.getElementsByName("submit")[0];
submit.disabled = true;
position_name.onblur = function(){
	if(position_name.value=='') {
		position_name_message.innerHTML = '* <?php echo $a_langpackage->a_asdpos_notnone; ?>';
		submit.disabled = true;
	} else {
		position_name_message.innerHTML = '*';
		submit.disabled = false;
	}
}
</script>
</body>
</html>