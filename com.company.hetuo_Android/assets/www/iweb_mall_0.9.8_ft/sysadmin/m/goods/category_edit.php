<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_category.php");

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_category = $tablePreStr."category";
$t_brand = $tablePreStr."brand";
$t_brand_category = $tablePreStr."brand_category";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$cat_id = intval(get_args('id'));
if(!$cat_id) {exit($a_langpackage->a_error);}

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result_category = $dbo->getRs($sql_category);
$category_dg = get_dg_category($result_category);
foreach($result_category as $v) {
	if($v['cat_id']==$cat_id) {
		$info = $v;
	}
}
//取得品牌列表
$sql="SELECT brand_id,brand_name FROM $t_brand ";
$brand_list = $dbo->getRs($sql);
//取得对应关系表的品牌
$sql = "SELECT brand_id FROM $t_brand_category WHERE cat_id='$cat_id'";
$list = $dbo->getRs($sql);
$category_brand_list=array();
if (is_array($list)) {
	foreach ($list as $value){
		$category_brand_list[]=$value['brand_id'];
	}
}
//print_r($category_brand_list);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management;?>&gt;&gt;<?php echo $a_langpackage->a_category_edit; ?></div>
    <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_category_edit; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=goods_category_list"><?php echo $a_langpackage->a_category_list; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=goods_category_edit" method="post" onsubmit="return checkform();" enctype="multipart/form-data">
		<table class="form-table">
		  <tbody>
			<tr>
				<td width="60px"><?php echo $a_langpackage->a_parent_category; ?>：</td>
				<td><select name="parent_id">
				<option value="0"><?php echo $a_langpackage->a_top_category; ?></option>
				<?php foreach($category_dg as $value) {?>
				<option value="<?php echo $value['cat_id'];?>" <?php if($value['cat_id']==$info['parent_id']) echo "selected";?>><?php echo $value['str_pad'];?><?php echo $value['cat_name'];?></option>
				<?php }?>
				</select></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_category_name; ?>：</td>
				<td><input type="text" class="small-text" name="cat_name" value="<?php echo $info['cat_name']; ?>" /> <span>*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_category_sort; ?>：</td>
				<td><input type="text" class="small-text" name="sort_order" value="<?php echo $info['sort_order']; ?>" style="width:60px;" /> <span>*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_category_and_brand;?>:</td>
				<td>
					<select name="brand_id[]" id="brand_id" multiple style="width:190px;">
						<?php
							if (is_array($brand_list)) {
								foreach ($brand_list as $value){
						?>
						<option value="<?php echo $value['brand_id'];?>"<?php if(in_array($value['brand_id'],$category_brand_list)){echo "selected";}?>><?php echo $value['brand_name'];?></option>
						<?php
								}
							}
						?>
					</select>
				</td>
			</tr>
			<tr>
				<input type="hidden" name="cat_id" value="<?php echo $cat_id; ?>">
				<td colspan="2"><input type="submit" class="regular-button" name="submit" value="<?php echo $a_langpackage->a_category_edit; ?>" /></td>
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
	var cat_name = document.getElementsByName('cat_name')[0];
	if(cat_name.value=='') {
		alert("<?php echo $a_langpackage->a_category_name_notnone; ?>");
		return false;
	}
	return true;
}
</script>
</body>
</html>