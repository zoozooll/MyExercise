<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$a_langpackage=new adminlp;
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_edit_admin_psd; ?></a></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_edit_admin_psd; ?></h3>
    <div class="content2">
		<form action="a.php?act=change_password" method="post">
		<table class="form-table">
			<tr>
				<td width="60px"><?php echo $a_langpackage->a_old_passwd; ?>：</td>
				<td><input class="small-text" type="password" name="password" /> <span class="red">*<span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_new_passwd; ?>：</td>
				<td><input class="small-text" type="password" name="new_password" /> <span class="red">*<span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_repeat_passwd; ?>：</td>
				<td><input class="small-text" type="password" name="re_password" /> <span class="red">*<span></td>
			</tr>
			<tr>
				<td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_change_passwd; ?>" /></span></td>
			</tr>
		</table>
		</form>
       </div>
     </div>
   </div>
</div>
<script>

</script>
</body>
</html>