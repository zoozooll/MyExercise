<?php
	//header("content-type:text/html;charset=utf-8");
	require("../foundation/asession.php");
	require("../configuration.php");
	require("includes.php");
	
	//语言包引入
	$u_langpackage=new uilp;
	
	//数据表定义
	$t_settings=$tablePreStr."settings";
	
	//数据库连接
	$dbo=new dbex;
	dbtarget('w',$dbServs);
	
	$sql="select value from $t_settings where variable='templates'";
	$templ=$dbo->getRow($sql);

?>
<html>
<head>
<title><?php echo $u_langpackage->u_UI_cback?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="skin/css/right.css">
</head>

<body>
	
<div class="container">
	<div class="rs_head"><?php echo $u_langpackage->u_cback_temp?></div>
</div>

<table class="main main_left">
	<tr>
		<td><?php echo $u_langpackage->u_cback_temp_say?>
		</td>
	</tr>
	<tr>
		<td><input type='button' class='top_button' value='<?php echo $u_langpackage->u_cback_temp?>' onclick='javascript:window.location.href="m.php?app=UI_restore.action&r_type=tmp";' /></td>
	</tr>	
</table>

<div class="container">
	<div class="rs_head"><?php echo $u_langpackage->u_cback_model?></div>
</div>

<table class="main main_left">
	<tr>
		<td><?php echo $u_langpackage->u_cback_model_say?>
		</td>
	</tr>
	<tr>
		<td><input type='button' class='top_button' value='<?php echo $u_langpackage->u_cback_model?>' onclick='javascript:window.location.href="m.php?app=UI_restore.action&r_type=mod";' /></td>
	</tr>
</table>

<div class="container">
	<div class="rs_head"><?php echo $u_langpackage->u_cback_skin?></div>
</div>

<table class="main main_left">
	<tr>
		<td><?php echo $u_langpackage->u_cback_skin_say?>
		</td>
	</tr>
	<tr>
		<td><input type='button' class='top_button' value='<?php echo $u_langpackage->u_cback_skin?>' onclick='javascript:window.location.href="m.php?app=UI_restore.action&r_type=skin";' /></td>	
	</tr>			
</table>

<div class="container">
	<div class="rs_head"><?php echo $u_langpackage->u_cback_all?></div>
</div>

<table class="main main_left">
	<tr>
		<td><?php echo $u_langpackage->u_cback_all_say?>
		</td>
	</tr>
	<tr>
		<td><input type='button' class='top_button' value='<?php echo $u_langpackage->u_cback_all?>' onclick='javascript:window.location.href="m.php?app=UI_restore.action&r_type=c_com";' /></td>
	</tr>	
</table>
	
</body>
</html>		