<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/ftpl_compile.php");
$pl_langpackage=new pluginslp;
$plugin_table=$tablePreStr."plugins";
$path=get_args('path');
$type=get_args('type');
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="skin/css/plugin.css">
<title></title>
</head>
<body style="text-align:center">
	<div style="width:100%;text-align:left" id="show" >
<?php
$dbo = new dbex;
if(!is_null(get_args('operator')) && get_args('operator')=='update')
{
	dbtarget('w',$dbServs);
	$valid=get_args('valid');
	$autoorder=get_args('autoorder');
	if(is_null($valid))$valid=0;
	if(is_null($autoorder))$autoorder=0;
	$sql="update  $plugin_table set autoorder=$autoorder,valid=$valid where name='$path'";

	if($dbo->exeUpdate($sql)){
		comp_plugins_position($SYSINFO['templates'],$SYSINFO['template_mode']);
		echo"<script>parent.Dialog.alert_ok('".$pl_langpackage->pl_update_suc."')</script>";
	}
	else echo"<script>parent.Dialog.alert('".$pl_langpackage->pl_update_false."')</script>";
}
if(!is_null($path))
{
	//检测是不是有数据库文件
	dbtarget('r',$dbServs);
	$sql="select *  from $plugin_table where name='$path'";
	$plugin=$dbo->getRs($sql);
	if(is_array($plugin)){
		if(isset($plugin[0]['autoorder']) && $plugin[0]['autoorder']==1) $autoorder="checked";
		else $autoorder="";
		if(isset($plugin[0]['valid']) && $plugin[0]['valid']==1) $valid="checked";
		else $valid="";
		echo <<<EOD
<form method="post" name="uploadform" action="m.php?app=plugin_update&path=$path&operator=update">
	<table width="100%" border="0">
	  <tr>
			<td><input type='checkbox' id='autoorder' $autoorder name='autoorder' value="1" />$pl_langpackage->pl_is_order </td>
			<td>$pl_langpackage->pl_order_info </td>
	  </tr>
	  <tr>
			<td><input type='checkbox' id='valid' $valid name='valid' value="1" /> $pl_langpackage->pl_inspire</td>
			<td>$pl_langpackage->pl_inspire_info</td>
	  </tr>
	</table>
</form>
EOD;
	}
}
?>
</div>
</body>
</html>