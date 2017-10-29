<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

require("../foundation/module_remind.php");

//数据表定义区
$t_remind = $tablePreStr."remind";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$remind_rs=get_all($dbo,$t_remind);

$type=array(
	"1"=>$a_langpackage->a_buyer_remind,
	"2"=>$a_langpackage->a_saler_remind,
);

$right_array=array(
	"remind_oper"    =>   "0",
    "remind_update"    =>   "0",
);
foreach($right_array as $key => $value){
	$right_array[$key]=check_rights($key);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings; ?> &gt;&gt; <?php echo $a_langpackage->a_remind_setting; ?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_remind_setting; ?></h3>
    <div class="content2">
		<input type="hidden" id="remind_oper" value="<?php echo $right_array['remind_oper'];?>">
		<table class="list_table">
			<thead>
				<tr style="text-align:center;">
					<th align="left" width="70px"><?php echo $a_langpackage->a_remind_type; ?></th>
					<th align="left"><?php echo $a_langpackage->a_remind_name; ?></th>
					<th align="left"><?php echo $a_langpackage->a_remind_content; ?></th>
					<th width="80px" colspan="2"><?php echo $a_langpackage->a_current_status; ?></th>
					<th width="40px"><?php echo $a_langpackage->a_operate; ?></th>
				</tr>
			</thead>
			<tbody>
		<?php foreach($remind_rs as $value) { ?>
			<tr style="text-align:center;">
				<td align="left"><?php echo $type[$value['remind_type']];?></td>
				<td align="left"><?php echo $value['remind_name'];?></td>
				<td align="left"><?php echo $value['remind_tpl'];?></td>
				<td width="" align="center" id="now_status_<?php echo $value['remind_id'];?>"><?php if($value['enable']==1) {echo "<span style='color:green;'>".$a_langpackage->a_open."</span>";} else {echo "<span style='color:red;'>".$a_langpackage->a_close."</span>";} ?></td>
				<td width="40px" align="center" onclick="toggle(this,<?php echo $value['remind_id'];?>)" style="cursor:pointer;text-decoration:underline;"><?php if($value['enable']) {echo $a_langpackage->a_close;} else {echo $a_langpackage->a_open;} ?></td>
				<td><a href="m.php?app=remind_edit&id=<?php echo $value['remind_id'];?>"><?php echo $a_langpackage->a_update; ?></a></td>
			</tr>
		<?php } ?>
			</tbody>
			<tr><td colspan="7"><?php echo $a_langpackage->a_remind_remark; ?></td></tr>
		</table>
	  </div>
	  </div>
	</div>
</div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function toggle(obj,id) {
	var right=document.getElementById("remind_oper").value;

	if(right != '0'){
		var v = 1;
		if(obj.innerHTML=='<?php echo $a_langpackage->a_close; ?>') {
			v = 0;
		}
		ajax("./a.php?act=remind_status_upd","POST","v="+v+"&id="+id,function(data){
			if(data=='1') {
				var now_status = document.getElementById("now_status_"+id);
				if(v) {
					obj.innerHTML = '<?php echo $a_langpackage->a_close; ?>';
					now_status.innerHTML = "<span style='color:green;'><?php echo $a_langpackage->a_open; ?></span>";
				} else {
					obj.innerHTML = '<?php echo $a_langpackage->a_open; ?>';
					now_status.innerHTML = "<span style='color:red;'><?php echo $a_langpackage->a_close; ?></span>";
				}
			}
		});
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}
//-->
</script>
</body>
</html>