<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_payment.php");
//引入语言包
$a_langpackage=new adminlp;
//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

//数据表定义区
$t_payment = $tablePreStr."payment";

$payment = get_payment_info($dbo,$t_payment);


$pay_oper=check_rights("pay_oper");



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
<input type="hidden" id="pay_oper" value="<?php echo $pay_oper;?>">
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings;?> &gt;&gt; <?php echo $a_langpackage->a_pay_port;?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_m_order_payment; ?></h3>
    <div class="content2">
		<table class="list_table">
		 <thead>
			<tr style="text-align:center;">
				<th width="90px" align="left"><?php echo $a_langpackage->a_port_name;?></th>
				<th width="90px"><?php echo $a_langpackage->a_folder_name;?></th>
				<th><?php echo $a_langpackage->a_describe;?></th>
				<th width="85px"><?php echo $a_langpackage->a_writer;?></th>
				<th style="width:40px;"><?php echo $a_langpackage->a_version;?></th>
				<th style="width:70px;"><?php echo $a_langpackage->a_now_status;?></th>
				<th style="width:40px;"><?php echo $a_langpackage->a_operate;?></th>
			</tr>
		  </thead>
		  <tbody>
		<?php foreach($payment as $value) { ?>
			<tr style="text-align:center;">
				<td align="left"><?php echo $value['pay_name'];?></td>
				<td><?php echo $value['pay_code'];?></td>
				<td><?php echo $value['pay_desc'];?></td>
				<td><a href="javascript:;"><?php echo $value['author'];?></a></td>
				<td><?php echo $value['version'];?></td>
				<td id="now_status_<?php echo $value['pay_id'];?>"><?php if($value['enabled']) {echo "<span style='color:green;'>".$a_langpackage->a_open."</span>";} else {echo "<span style='color:red;'>".$a_langpackage->a_close."</span>";} ?></td>
				<td onclick="toggle(this,<?php echo $value['pay_id'];?>)" style="cursor:pointer;text-decoration:underline;"><?php if($value['enabled']) {echo $a_langpackage->a_close;} else {echo $a_langpackage->a_open;} ?></td>

			</tr>
		<?php } ?>
		</tbody>
		<tbody>
			<tr><td colspan="7"><?php echo $a_langpackage->a_notice1;?>
			<br />　　　<?php echo $a_langpackage->a_notice2;?>
			<br />　　　<?php echo $a_langpackage->a_notice3;?></td></tr></tbody>
		</table>
		</div>
	</div>
</div></div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function toggle(obj,id) {
	var right=document.getElementById("pay_oper").value;
	if(right !=0){
		var v = 1;
		var d = new Date();
		var t = d.getTime();
		if(obj.innerHTML=='<?php echo $a_langpackage->a_close;?>') {
			v = 0;
		}
		ajax("./a.php?act=order_payment_toggle&t="+t,"POST","v="+v+"&id="+id,function(data){
			if(data=='1') {
				var now_status = document.getElementById("now_status_"+id);
				if(v) {
					obj.innerHTML = '<?php echo $a_langpackage->a_close;?>';
					now_status.innerHTML = "<span style='color:green;'><?php echo $a_langpackage->a_open;?></span>";
				} else {
					obj.innerHTML = '<?php echo $a_langpackage->a_open;?>';
					now_status.innerHTML = "<span style='color:red;'><?php echo $a_langpackage->a_close;?></span>";
				}
			}
		});
	}else{
		alert("对不起，您没有权限修改!");
		location.href="m.php?app=error";
	}
}
//-->
</script>
</body>
</html>