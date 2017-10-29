<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_brand.php");

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_brand = $tablePreStr."flink";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$brand_id = intval(get_args('id'));
if(!$brand_id) {exit($a_langpackage->a_error);}

$info = get_one_brand_info($dbo,$t_brand,$brand_id);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management;?>&gt;&gt;<?php echo $a_langpackage->a_flink_edit; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_flink_edit; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=flink_list"><?php echo $a_langpackage->a_flink_list; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=flink_edit" method="post" onsubmit="return checkform();" enctype="multipart/form-data">
		<table class="form-table">
		   <tbody>
			<tr>
				<td width="70px"><?php echo $a_langpackage->a_flink_name; ?>：</td>
				<td><input type="text" class="small-text" name="brand_name" value="<?php echo $info['brand_name']; ?>" /> <span>*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_flink_desc; ?>：</td>
				<td><textarea name="brand_desc" style="width:200px; height:50px;"><?php echo $info['brand_desc']; ?></textarea></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_brand_siteurl; ?>：</td>
				<td><input type="text" class="small-text" name="site_url" value="<?php echo $info['site_url']; ?>" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_flink; ?>logo：</td>
				<td><input type="file" name="logo[]" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_news_isshow; ?>：</td>
				<td><input type="checkbox" name="is_show" value="1" <?php if ($info['is_show']){ echo "checked";} ?> /><?php echo $a_langpackage->a_show; ?> </td>
			</tr>
			<tr>
				<input type="hidden" name="brand_id" value="<?php echo $brand_id; ?>" />
				<td colspan="2"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_flink_edit; ?>" /></td>
			</tr>
		  </tbody>
		</table>
		</form>
	   </div>
	  </div>
	</div>
</div>
<script>
function checkform() {
	var type_name = document.getElementsByName('brand_name')[0];
	if(type_name.value=='') {
		alert("<?php echo $a_langpackage->a_brand_name_notnone; ?>");
		return false;
	}
	return true;
}
</script>
</body>
</html>