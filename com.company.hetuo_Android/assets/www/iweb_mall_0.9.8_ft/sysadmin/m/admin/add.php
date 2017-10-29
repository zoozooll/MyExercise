<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_news.php");

//引入语言包
$a_langpackage=new adminlp;
$t_admin_group=$tablePreStr."admin_group";
$info = array(
	'admin_name'	=> '',
	'admin_email'	=> '',
	'admin_password'=> '',
);
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "select * from `$t_admin_group` where del_flg=0 ";
$result_group = $dbo->getRs($sql);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_add_admin; ?></a></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_add_admin; ?></span> <span class="right" style=" margin-right:15px;"><a href="m.php?app=admin_list"><?php echo $a_langpackage->a_admin_list; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=admin_add" method="post" onsubmit="return checkForm();">
		<table class="form-table">
			<tr>
				<td width="72px"><?php echo $a_langpackage->a_admin_name; ?>：</td>
				<td><input class="small-text" type="text" name="admin_name" value="<?php echo $info['admin_name']; ?>" style="width:200px;" /> <span>*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_admin_email; ?>：</td>
				<td><input class="small-text" type="text" name="admin_email" value="<?php echo $info['admin_email']; ?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_admin_passwd; ?>：</td>
				<td><input class="small-text" type="password" name="admin_password" value="<?php echo $info['admin_password']; ?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_mana_group; ?>：</td>
				<td>
					<select name="group">
						<?php foreach($result_group as $key => $value){?>
						<option value="<?php echo $value['id'];?>"><?php echo $value['group_name'];?></option>
						<?php }?>
					</select>
				</td>
			</tr>
			<tr><td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_add_admin; ?>" /></span></td></tr>
		</table>
		</form>
	   </div>
	 </div>
   </div>
</div>
<script language="JavaScript">
<!--
function checkForm() {
	if(document.getElementsByName("admin_name")[0].value=='') {
		alert("<?php echo $a_langpackage->a_admin_name_none; ?>");
		return false;
	}
	return true;
}
//-->
</script>
</body>
</html>