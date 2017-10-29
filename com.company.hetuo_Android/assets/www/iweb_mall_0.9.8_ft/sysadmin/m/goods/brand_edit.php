<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_brand.php");
require("../foundation/module_category.php");
$t_category = $tablePreStr."category";
//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_brand = $tablePreStr."brand";
$t_brand_category = $tablePreStr."brand_category";
//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$brand_id = intval(get_args('id'));
if(!$brand_id) {exit($a_langpackage->a_error);}

$info = get_one_brand_info($dbo,$t_brand,$brand_id);
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result_category = $dbo->getRs($sql_category);
$category_dg = get_dg_category($result_category);

$sql = "SELECT cat_id FROM $t_brand_category WHERE brand_id='$brand_id'";
$list = $dbo->getRs($sql);
$category_brand_list=array();
if (is_array($list)) {
	foreach ($list as $value){
		$category_brand_list[]=$value['cat_id'];
	}
}
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_aboutgoods_management;?> &gt;&gt; <?php echo $a_langpackage->a_brand_edit; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_brand_edit; ?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=goods_brand_list" style="float:right;"><?php echo $a_langpackage->a_brand_list; ?></a></span></h3>
    <div class="content2">
			<form action="a.php?act=goods_brand_edit" method="post" onsubmit="return checkform();" enctype="multipart/form-data">
			<table class="form-table">
			<tr>
				<td width="63px;"><?php echo $a_langpackage->a_brand_name; ?>：</td>
				<td><input class="small-text" type="text" name="brand_name" value="<?php echo $info['brand_name']; ?>" /> <span>*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_brand_desc; ?>：</td>
				<td><textarea name="brand_desc" style="width:200px; height:50px;"><?php echo $info['brand_desc']; ?></textarea></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_brand_siteurl; ?>：</td>
				<td><input class="small-text" type="text" name="site_url" value="<?php echo $info['site_url']; ?>" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_brand; ?>logo：</td>
				<td><input type="file" name="logo[]" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_linke_category?></td>
				<td>
					<select name="cat_id[]" id="cat_id" multiple style="width:190px; height:300px;">
						<?php foreach($category_dg as $value) {?>
							<option value="<?php echo $value['cat_id'];?>" <?php if(in_array($value['cat_id'],$category_brand_list)){echo "selected";}?>><?php echo $value['str_pad'];?><?php echo $value['cat_name'];?></option>
						<?php }?>
					</select>
				</td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_news_isshow; ?>：</td>
				<td><input type="checkbox" name="is_show" value="1" <?php if ($info['is_show']){ echo "checked";} ?> /><?php echo $a_langpackage->a_show; ?> </td>
			</tr>
			<tr>
				<input type="hidden" name="brand_id" value="<?php echo $brand_id; ?>" />
				<td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_brand_edit; ?>" /></td>
			</tr>
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