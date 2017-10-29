<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_complaint.php");

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_complaint_type = $tablePreStr."complaint_type";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$result = get_complaint_type_all($dbo,"*",$t_complaint_type);

//print_r($result);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_order_mengament;?> &gt;&gt; <?php echo $a_langpackage->a_title_management_of_complaints; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_title_management_of_complaints; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=complaint_type_edit"><?php echo $a_langpackage->a_add_a_complaint_title; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=lock_shop" name="form1" id="form1" method="post">
		<table class="list_table">
		  <thead>
			<tr style="text-align:center;">
				<th width="20px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="60px"><?php echo $a_langpackage->a_ID;?></th>
				<th align="left"><?php echo $a_langpackage->a_of_complaint; ?></th>
				<th width="90px"><?php echo $a_langpackage->a_operate;?></th>
			</tr>
		  </thead>
		  <tbody>
			<?php if($result) {
			foreach($result as $value) { ?>
			<tr style="text-align:center;">
				<td><input type="checkbox" name="type_id[]" value="<?php echo $value['type_id'];?>" /></td>
				<td><?php echo $value['type_id'];?>.</td>
				<td align="left"><?php echo $value['type_content'];?></td>
				<td><a href="m.php?app=complaint_type_edit&type_id=<?php echo $value['type_id'];?>"><?php echo $a_langpackage->a_update;?></a> | <a href="a.php?act=complaint_type_del&type_id=<?php echo $value['type_id'];?>"><?php echo $a_langpackage->a_dele;?></a></td>
			</tr>
			<?php } } else { ?>
			<tr>
				<td colspan="9"><?php echo $a_langpackage->a_no_list;?></td>
			</tr>
			<?php } ?>
		 </tbody>
		</table>
		</form>
	  </div>
	 </div>
   </div>
</div>
</body>

</html>