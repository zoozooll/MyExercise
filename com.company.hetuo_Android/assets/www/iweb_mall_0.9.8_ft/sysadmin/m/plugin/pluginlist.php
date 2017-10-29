<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

$pl_langpackage=new pluginslp;
$a_langpackage=new adminlp;
$plugin_dir=dir($webRoot."plugins/");
chdir($webRoot."plugins/");
function getInfo($path)
{
	global $webRoot;
	global $pl_langpackage;
	global $dir;
	$p_status=$pl_langpackage->pl_unset;
	$p_change=$pl_langpackage->pl_install;
	$p_update="Update(\"m.php?app=plugin_update&path=$path\")";
	$p_operator="Install";
	$p_index="m.php?app=plugin_install_1";
	$pluginxml= $webRoot."plugins/$path/plugin.xml";
	if(file_exists($pluginxml))
	{
		$p_update="noinstall()";
	}
	else
	{
		$pluginxml=$webRoot."plugins/$path/_plugin.xml";
		$p_status=$pl_langpackage->pl_isset;
		$p_change=$pl_langpackage->pl_unload;
		$p_index="m.php?app=plugin_unload";
		$p_operator="Unloadfirm";
	}
	if(file_exists($pluginxml))
	{
		$dom = new DOMDocument();
		$dom->load($pluginxml);
		$plugin=$dom->getElementsByTagName("plugin");
		$infos=$plugin->item(0)->childNodes;
		$plugin_id="";
		$auto_checked="";
		$valid_checked="";
		foreach($infos as $info)
		{
			if($info->nodeName!="#text")
			{
				$tem=$info->nodeName;
				if($tem!="urls")$$tem=$info->nodeValue;
			}
		}
		if($autoorder)$auto_checked="checked='checked'";
		if($valid)$valid_checked="checked='checked'";
		echo "<tr class='center'><td><a href='#' onclick='$p_update'>$title</a></td><span id='span_$path'></span></td><td>$p_status</td><td><input class='top_button' type='submit' onclick=\"$p_operator('$p_index&path=$path')\" value='$p_change'/></td></tr>";
	}
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="JavaScript" src="../servtools/Dialog.js"></script>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
/* body */
body { text-align: left; font-family:"宋体", Arial; margin:0; padding:0; background: #ffffff; font-size:12px; color:#333;}
div, form, img, ul, ol, li, dl, dt, dd, p { margin:0; padding:0; border:0; }
li { list-style:none; }
h1, h2, h3, h4, h5, h6, input { margin:0; padding:0; }
table, td, tr, th { font-size:12px; }
form{margin:0px;display: inline}
a:link { color: #333; text-decoration:none; }
a:visited { color: #333; text-decoration:none; }
a:hover { color: #ff0000; text-decoration:underline; }
a:active { color: #37a1fa; text-decoration:none; }

/* global */
.spaceline { clear:both; line-height:5px; height:5px; }
.clear { clear:both; height:1px; overflow:hidden;}
.fl{ float:left;}
.fr{ float:right;}
.left {text-align:left;}
.center {text-align:center;}
.right {text-align:right;}

/* table */
table.content { border-collapse:collapse; border:solid #E5E5E5; border-width:1px 0 0 1px; margin:5px auto;}
table.content th, table.content td { border:solid #E5E5E5; border-width:0 1px 1px 0; padding:2px; line-height:24px;}
table.content th {background:#E5E5E5;}
</style>
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<script type='text/javascript'>
var diag_install = new Dialog("Diag1","<?php echo $pl_langpackage->pl_next_step;?>");
var install_submit="";
var install_end=false;
var unload_end=false;
var url="";
function Install(url){
	diag_install.Width = 600;
	diag_install.Height = 350;
	diag_install.Title = "<?php echo $pl_langpackage->pl_guide;?>";
	diag_install.URL = url;
	diag_install.ShowMessageRow = true;
	diag_install.MessageTitle = "<?php echo $pl_langpackage->pl_install_info;?>";
	diag_install.Message = "<?php echo $pl_langpackage->pl_install_worning;?>";
	diag_install.OKEvent = installNext;//点击确定后调用的方法
	diag_install.show();
}
function installNext()
{
	if(install_submit!="")
	{
		diag_install.Ok="<?php echo $pl_langpackage->pl_sure;?>";
		window.frames["_DialogFrame_Diag1"].document.setplugin.submit();
		install_submit="";
	}else
	{
		diag_install.show();
	}
	if(install_end)
	{
		diag_install.close();
		next();
	}
}
var diag_unload = new Dialog("Diag2","<?php echo $pl_langpackage->pl_sure;?>");
function Unload(url){
	diag_unload.Width = 600;
	diag_unload.Height = 350;
	diag_unload.Title = "<?php echo $pl_langpackage->pl_guide;?>";
	diag_unload.URL = url;
	diag_unload.Ok="<?php echo $pl_langpackage->pl_sure;?>";
	diag_unload.ShowMessageRow = true;
	diag_unload.MessageTitle = "<?php echo $pl_langpackage->pl_unload_info;?>";
	diag_unload.Message = "<?php echo $pl_langpackage->pl_unload_remind;?>";
	diag_unload.OKEvent = unloadNext;//点击确定后调用的方法
	diag_unload.show();
}
function unloadNext()
{
	diag_unload.close();
	next();
}
var diag_update = new Dialog("Diag3","<?php echo $pl_langpackage->pl_update;?>");
function Update(url){
	diag_update.Width = 600;
	diag_update.Height = 300;
	diag_update.Title = "<?php echo $pl_langpackage->pl_manage_str;?>";
	diag_update.URL=url;
	diag_update.ShowMessageRow = true;
	diag_update.MessageTitle = "<?php echo $pl_langpackage->pl_manage;?>";
	diag_update.Message = "<?php echo $pl_langpackage->pl_set_update;?>";
	diag_update.OKEvent = updateform;
	diag_update.show();
}
function updateform() {
   window.frames["_DialogFrame_Diag3"].document.uploadform.submit();
}
function next() {
	location.replace('m.php?app=plugin_list');
}
function Unloadfirm(url){
	Dialog.confirm('<?php echo $pl_langpackage->pl_worning;?>',function(){Unload(url);},"<?php echo $pl_langpackage->pl_sure;?>");
}
function noinstall() {
	Dialog.alert('<?php echo $pl_langpackage->pl_unset_state;?>',"<?php echo $pl_langpackage->pl_sure;?>");
}
</script>
<title></title>
</head>
<body>

<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $pl_langpackage->pl_list;?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $pl_langpackage->pl_list;?></h3>
    <div class="content2">
		<table class="list_table" >
		<thead>
			<tr><th><?php echo $pl_langpackage->pl_name;?></th><th><?php echo $pl_langpackage->pl_state;?></th><th><?php echo $pl_langpackage->pl_ctrl;?></th></tr>
	    </thead>
			<?php
			while (false !== ($dir = $plugin_dir->read())) {
				if(is_dir($dir)&&$dir!='.' &&$dir!='..')getInfo($dir);
			}
			$plugin_dir->close();
			?>
		</table>
		</div>
		</div>
	</div>
</div>
</body>
</html>