<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/cxmloperator.class.php");
require_once("../foundation/ftpl_compile.php");

//语言包引入
$pl_langpackage=new pluginslp;

$plugin_table=$tablePreStr."plugins";
$t_plugin_url=$tablePreStr."plugin_url";

$name=get_args('path');
$opsition_value=get_args('opsition');
$valid_value=get_args('valid');
$autoorder_value=get_args('autoorder');
$submited=get_args("submited");
$type=get_args('type');
if(is_null($valid_value))$valid_value=0;
if(is_null($autoorder_value))$autoorder_value=0;
?>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link rel="stylesheet" type="text/css" href="skin/css/plugin.css">
</head>

<body style="margin:auto">
	<?php
	if(!is_null($name) && !is_null($submited))
{
	$dom = new DOMDocument();
	$plugin_dir=dir($webRoot."plugins/");
	chdir($webRoot."plugins/");
	if(rename($name."/plugin.xml",$name."/_plugin.xml"))
	{
		$dom->load($name."/_plugin.xml");
		$plugin=$dom->getElementsByTagName("plugin")->item(0);
		$autoorder=$plugin->getElementsByTagName('autoorder')->item(0);
		$valid=$plugin->getElementsByTagName('valid')->item(0);
		if($autoorder)
		{
			$autoorder->nodeValue=$autoorder_value;
		}else
		{
			$autoorder=$dom->createElement("autoorder");
			$autoorder->nodeValue=$autoorder_value;
		}
		if($valid)
		{
			$valid->nodeValue=$valid_value;
		}
		else
		{
			$valid=$dom->createElement("valid");
			$valid->nodeValue=$valid_value;
		}
		$plugin->appendChild($autoorder);
		$plugin->appendChild($valid);

		$dom->formatOutput=true;
		$infos=$plugin->childNodes;
		$plugin_urls="";
		foreach($infos as $info)
		{
			if($info->nodeName!="#text")
			{
				$tem=$info->nodeName;
				if($tem=='urls')
				{
					$urls=$info->childNodes;
					foreach($urls as $url)
					{
						if($url->nodeName!="#text")$plugin_urls.="('$name','".$url->getAttributeNode("id")->value."','".$url->nodeValue."'),";
					}
					$plugin_urls=substr($plugin_urls,0,-1);
				}
				else
				{
					$$tem=$info->nodeValue;
				}
			}
		}
		$dom->save($name."/_plugin.xml");
		//$valid=get_args('valid');
		//$autoorder=get_args('autoorder');
		if(isset($show))$type=$show;
		else $type=0;
		if(!isset($image))$image="";
		$description=short_check($description);
		$title=short_check($title);
		if(!isset($backrights))$backrights="";
		else $backrihgts=short_check($backrights);
		if(!isset($frontrights))$frontrights="";
		else $frontrights=short_check($frontrights);
		if(!isset($backurl))$backurl="";
		else $backurl=short_check($backurl);
		//后台权限处理
		if($backrights!="")
		{
			$xmlpath = dirname(__file__)."/../../../plugins/resources.xml";
			$xml=new XMLOperator($xmlpath);
			if(!$xml->query("//group[@id='plugin_$name']"))
			{
				$xml->addNode("/resources","group","","id=plugin_$name;value={$title}.$pl_langpackage->pl_plugin_permission");
			}
			$rights=explode(",",$backrights);
			foreach($rights as $right)
			{
				$resoult=explode(":",$right);
				$xml->addNode("//group[@id='plugin_$name']","resource","","id={$name}_$resoult[0];value=$resoult[1]");
			}
			$xml->save($xmlpath);
		}

		//注册插件
		$dbo = new dbex;
		dbtarget('w',$dbServs);
		$sql="insert into $plugin_table(title,name,valid,autoorder,image,reg_date,info) value('$title','$name','$valid_value','$autoorder_value','$image',NOW(),'$description')";
		//注册插件入口
		$dbo->exeUpdate($sql);
		//注册插件URL
		$sql="insert into $t_plugin_url (name,layout_id,url) values $plugin_urls"	;
		$dbo->exeUpdate($sql);
		comp_plugins_position($SYSINFO['templates'],$SYSINFO['template_mode']);
		echo "<div style='color:green;text-align:center'><script>parent.install_end=true;</script>".$pl_langpackage->pl_plugin_install."</div>";
	}
}
else
{
?>
<form name="setplugin" method="post" action="m.php?app=plugin_install_3&path=<?php echo $name?>">
<table width="100%" border="0">
  <tr>
	<td><input type='checkbox' id='autoorder' name='autoorder' value="1" />
	<?php echo $pl_langpackage->pl_is_order;?></td>
	<td><?php echo $pl_langpackage->pl_order_info;?></td>
  </tr>
  <tr>
	<td><input type='checkbox' id='valid' name='valid' value="1" /><input type='hidden'  name='submited' value="1" />
	<?php echo $pl_langpackage->pl_inspire;?></td>
	<td><?php echo $pl_langpackage->pl_inspire_info;?></td>
  </tr>
</table>
	<p style="text-align:center"><script>parent.install_submit="true";</script></p>
</form>
<?php
	}?>
</body>
</html>