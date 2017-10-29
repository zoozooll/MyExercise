<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_category.php");

//引入语言包
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("cat_list");
if (!$right){
	header('location:m.php?app=error');
	exit;
}

//数据表定义区
$t_category = $tablePreStr."category";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result_category = $dbo->getRs($sql_category);

$category_dg = get_dg_category($result_category);

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
.green {color:green;}
.red {color:red;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_aboutgoods_management;?> &gt;&gt; <?php echo $a_langpackage->a_category_list; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_category_list; ?></span><span class="right" style="margin-right:15px;"> <a href="m.php?app=goods_category_add" style="float:right;"><?php echo $a_langpackage->a_category_add; ?></a></span></h3>
    <div class="content2">
		<table class="list_table">
			<thead>
			<tr style=" text-align:center;">
				<th width="50px">ID</th>
				<th align="left"><?php echo $a_langpackage->a_category_name; ?></th>
				<th width="65px"><?php echo $a_langpackage->a_goods_num; ?></th>
				<th width="65px"><?php echo $a_langpackage->a_show_sort; ?></th>
				<th width="115px"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($category_dg) {
			foreach($category_dg as $value) { ?>
			<tr style=" text-align:center;">
				<td><?php echo $value['cat_id'];?>.</td>
				<td align="left" <?php if($value['parent_id']=='0') {echo 'style="font-weight:bold;"';} ?>>&nbsp;<?php echo $value['str_pad'];?><?php echo $value['cat_name'];?></td>
				<td><?php echo $value['goods_num'];?></td>
				<td><?php echo $value['sort_order'];?></td>
				<td>
					<a href="m.php?app=goods_attr_manage&cat_id=<?php echo $value['cat_id'];?>"><?php echo $a_langpackage->a_attr_set; ?></a>
					<a href="m.php?app=goods_category_edit&id=<?php echo $value['cat_id'];?>"><?php echo $a_langpackage->a_update; ?></a>
					<a href="a.php?act=goods_category_del&id=<?php echo $value['cat_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_sure_del_category; ?>');"><?php echo $a_langpackage->a_delete; ?></a>
				</td>
			</tr>
			<?php }} else { ?>
			<tr>
				<td colspan="6"><?php echo $a_langpackage->a_no_list; ?></td>
			</tr>
			<?php } ?>
			</tbody>
		</table>
	  </div>
	 </div>
	</div>
</div>
</body>
</html>