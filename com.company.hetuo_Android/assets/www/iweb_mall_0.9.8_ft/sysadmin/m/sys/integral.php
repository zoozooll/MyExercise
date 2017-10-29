<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("../foundation/module_credit.php");

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_integral = $tablePreStr."integral";

//变量定义区

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$integral = get_integral_list($dbo,$t_integral);

$credit_update=check_rights("credit_update");

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
.green {color:green;}
.red {color:red;}
.edit span{background:#efefef;}
</style>
</head>
<body>
<form id="form1" name="form1" method="post" action="a.php?act=sys_upd_integral" onsubmit="return checkvalue();">

<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?>&gt;&gt; <?php echo $a_langpackage->a_global_settings;?> &gt;&gt; <?php echo $a_langpackage->a_rank_grade;?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_m_sys_integral; ?></h3>
    <div class="content2">
		<table class="list_table">
			<thead>
				<tr style="text-align:center;">
					<th><?php echo $a_langpackage->a_grade;?></th>
					<th><?php echo $a_langpackage->a_credit_min;?></th>
					<th><?php echo $a_langpackage->a_credit_max;?></th>
					<th><?php echo $a_langpackage->a_credit_img;?></th>
				</tr>
			</thead>
			<tbody>
			<?php if($integral) {
				foreach($integral as $val) { ?>
			<tr style="text-align:center;">
				<td><?php echo $val['int_id'];?></td>
				<td><input class="small-text" type="text" name="min_<?php echo $val['int_id'];?>" value="<?php echo $val['int_min']?>" /></td>
				<td><input class="small-text" type="text" name="max_<?php echo $val['int_id'];?>" value="<?php echo $val['int_max']?>" /></td>
				<td><span class="icon<?php echo $val['int_grade'];?>"></span></td>
			</tr>
				<?php }?>
			<tr><td colspan="4"><span class="button-container"><input class="regular-button" type="submit" name="" value="<?php echo $a_langpackage->a_credit_edit;?>" /></span></td></tr>
			<tr><td colspan="4">&nbsp;&nbsp;<?php echo $a_langpackage->a_crons_notice;?></td></tr>
			<?php }?>
			</tbody>
		</table>
		</div>
	</div>
</div>
</div>
</form>
<script language="JavaScript">
<!--
function checkvalue() {
	var input = document.getElementsByTagName("input");
	var temp=0;
	for(var i=0; i<(input.length-1); i++) {
		if(parseInt(input[i].value)<temp) {
			alert('<?php echo $a_langpackage->a_crons_message;?>'+input[i].value+"<"+temp);
			return false
		}
		temp = parseInt(input[i].value);
	}
	return true;
}
//-->
</script>
</body>
</html>