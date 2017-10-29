<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_brand = $tablePreStr."flink";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_brand`";

$result = $dbo->fetch_page($sql,13);




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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management;?>&gt;&gt;<?php echo $a_langpackage->a_flink_list; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_flink_list; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=flink_add"><?php echo $a_langpackage->a_flink_add; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=flink_del" name="form1" method="post">
		<table class="list_table">
            <thead>
			<tr style="text-align:center;">
				<th width="2px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="30px">ID</th>
				<th width="70px" align="left"><?php echo $a_langpackage->a_flink_name; ?></th>
				<th align="left"><?php echo $a_langpackage->a_flink_desc; ?></th>
				<th align="left"><?php echo $a_langpackage->a_brand_siteurl; ?></th>
				<th width="35px"><?php echo $a_langpackage->a_show; ?></th>
				<th width="60px"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style="text-align:center;">
				<td><input type="checkbox" name="brand_id[]" value="<?php echo $value['brand_id'];?>" /></td>
				<td><?php echo $value['brand_id'];?>.</td>
				<td align="left"><?php echo $value['brand_name'];?></td>
				<td align="left"><?php echo $value['brand_desc'];?></td>
				<td align="left"><?php echo $value['site_url'];?></td>
				<td>
					<?php if($value['is_show']) { ?>
					<img src="../skin/default/images/yes.gif" />
					<?php } else { ?>
					<img src="../skin/default/images/no.gif" />
					<?php } ?>
				</td>
				<td>
					<a href="m.php?app=flink_edit&id=<?php echo $value['brand_id'];?>"><?php echo $a_langpackage->a_update; ?></a>
					<a href="a.php?act=flink_del&id=<?php echo $value['brand_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_sure_delbrand; ?>');"><?php echo $a_langpackage->a_delete; ?></a>
				</td>
			</tr>
			<?php }?>
			<tr>
				<td colspan="7">
					<input class="regular-button" type="submit" name=""  onclick="return confirm('<?php echo $a_langpackage->a_operate_message;?>');" value="<?php echo $a_langpackage->a_delete;?>" />
				</td>
			</tr>
			<?php } else { ?>
			<tr>
				<td colspan="7"><?php echo $a_langpackage->a_no_list; ?></td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="7"><?php include("m/page.php"); ?></td>
			</tr>
			</tbody>
		</table>
		</form>
	  </div>
	 </div>
	</div>
</div>
<script language="JavaScript">
<!--
var inputs = document.getElementsByTagName("input");
function checkall(obj) {
	if(obj.checked) {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = true;
			}
		}
	} else {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = false;
			}
		}
	}
}
//-->
</script>
</body>
</html>