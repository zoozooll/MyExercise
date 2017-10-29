<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//数据表定义
$t_settings = $tablePreStr."settings";
//语言包引入
$u_langpackage=new uilp;
$a_langpackage=new adminlp;
//数据库连接
$dbo = new dbex;
dbtarget('w',$dbServs);

$sql = "select value from $t_settings where variable='templates'";
$templ = $dbo->getRow($sql);

$sql = "select value from $t_settings where variable='template_mode'";
$template_mode = $dbo->getRow($sql);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
</style>
</head>
<body>

<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt;<?php echo $u_langpackage->u_temp_admin; ?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $u_langpackage->u_temp_admin; ?></h3>
    <div class="content2">
		<table class="list_table">
		  <thead>
			<tr style="text-align:center;">
				<th><?php echo $u_langpackage->u_temp_list; ?></th>
				<th><?php echo $u_langpackage->u_amend_time; ?></th>
				<th><?php echo $u_langpackage->u_currently_apply; ?></th>
				<th><?php echo $u_langpackage->u_admin_handle; ?></th>
			</tr>
		  </thead>
		  <tbody>
		<?php
			$ref=opendir("../templates");
			while($tp_dir=readdir($ref)){
				if(!preg_match("/^\./",$tp_dir)){
					$act_time=date("Y-m-d H:i:s",filemtime("../templates/".$tp_dir));
					$selected="";
					if($templ['value']==$tp_dir){
						$selected="√";
					}
					echo '<tr style="text-align:center;"><td class="center">'.$tp_dir.'</td> <td class="center">'.$act_time.'</td> <td class="center">'.$selected.'</td>
					<td class="center">&nbsp|&nbsp<a href="m.php?app=action_comp&pro='.$tp_dir.'" onclick=\'return confirm("'.$u_langpackage->u_admin_con.'");\'>'. $u_langpackage->u_app_temp.'</a>
					&nbsp|&nbsp<a href="m.php?app=tmp_list&loc='.$tp_dir.'">'.$u_langpackage->u_admin_temp.'</a>&nbsp|&nbsp</td></tr>';
				}
			}
		?>
		</tbody>
		</table>
		<table  class="list_table">
		   <tbody>
			<tr><td><?php echo $u_langpackage->u_prompt_inf; ?>：</td></tr>
			<tr>
				<td>
				&nbsp;<?php echo $u_langpackage->u_prompt_1; ?><br />
				&nbsp;<?php echo str_replace('{default}',$templ['value'],$u_langpackage->u_prompt_2); ?><br />
				&nbsp;<?php echo $u_langpackage->u_prompt_3; ?><br />
				&nbsp;<?php echo $u_langpackage->u_prompt_4; ?><br />
				&nbsp;<?php echo $u_langpackage->u_prompt_5; ?><br />
				<!-- &nbsp;6、如果替换时发生错误可以进入站点UI恢复——>恢复模板，进行恢复；<br /> -->
				</td>
			</tr>
			</tbody>
		</table>
		<table>
		  <tbody>
			<tr><td colspan="3"><?php echo $u_langpackage->u_runing; ?><span style="color:red;" id="now_mode"><?php if($template_mode['value']=='service') { ?><?php echo $u_langpackage->u_service; ?><?php }else{ ?><?php echo $u_langpackage->u_debug; ?><?php } ?></span><?php echo $u_langpackage->u_run; ?>)  &nbsp;&nbsp; <span id="ajax_loading"></span></td></tr>
			<tr>
				<td><?php echo $u_langpackage->u_debug; ?>:</td>
				<td><?php echo $u_langpackage->u_debug_caution; ?></td>
				<td><span class="button-container"><input class="regular-button" type="button" value="<?php echo $u_langpackage->u_app; ?>" onclick="template_mode('debug');" /></span></td>
			</tr>
			<tr>
				<td><?php echo $u_langpackage->u_service; ?>:</td>
				<td><?php echo $u_langpackage->u_service_caution; ?></td>
				<td><span class="button-container"><input class="regular-button" type="button" value="<?php echo $u_langpackage->u_app; ?>" onclick="template_mode('service');" /></span></td>
			</tr>
			</tbody>
		</table>
		</div>
		</div>
	 </div>
</div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var t_mode = "<?php echo $template_mode['value']; ?>";
var tpl = "<?php echo $templ['value'];?>";
function template_mode(v) {
	if(v==t_mode) {
		alert("<?php echo $u_langpackage->u_present; ?>"+v+"<?php echo $u_langpackage->u_mode_run; ?>");
		return ;
	} else {
		if(confirm("<?php echo $u_langpackage->u_mode_run_con; ?>")){
			var ajax_loading = document.getElementById("ajax_loading");
			var now_mode = document.getElementById("now_mode");
			ajax_loading.innerHTML = '<?php echo $u_langpackage->u_mode_cho; ?>';
			var d = new Date();
			var t = d.getTime();
			ajax("a.php?act=tpl_ajax_comp","POST","mode="+v+"&tpl="+tpl+"&t="+t,function(data){
				if(data!='-1') {
					ajax_loading.innerHTML = '<span style="color:green;"><?php echo $u_langpackage->u_mode_cho_suc; ?><span>';
					now_mode.innerHTML = v+'<?php echo $u_langpackage->u_mode; ?>';
					t_mode = v;
				} else {
					ajax_loading.innerHTML = '<span style="color:red;"><?php echo $u_langpackage->u_mode_cho_lose; ?><span>';
				}
			});
		}
	}
}
//-->
</script>
</body>
</html>