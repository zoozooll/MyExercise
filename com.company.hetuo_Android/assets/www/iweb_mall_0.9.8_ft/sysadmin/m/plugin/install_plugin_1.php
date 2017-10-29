<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//语言包引入
$pl_langpackage=new pluginslp;

$step=1;
$plugin_name="";
$path=get_args('path');
$next="m.php?app=plugin_install_2&path=$path";
$plugin_dir=dir($webRoot."plugins/");
chdir($webRoot."plugins/");

//根据位置获取插件信息
function getInfo($path)
{
	global $next;
	global $webRoot;
	$pluginxml = $webRoot."plugins/$path/plugin.xml";
	if(file_exists($pluginxml))
	{
		$dom = new DOMDocument();
		$dom->load($pluginxml);
		$plugin=$dom->getElementsByTagName("plugin");
		$infos=$plugin->item(0)->childNodes;
		$plugin_id="";
		$auto_checked="";
		$valid_checked="";
		$inurl='';

		foreach($infos as $info)
		{
			if($info->nodeName!="#text")
			{
				$tem=$info->nodeName;
				$$tem=$info->nodeValue;
			}
		}

		if(!isset($show))$show=0;
		$next.="&type=$show";
		if(isset($sqlpath))
		{
			$next.="&sql=$sqlpath";
		}
		else
		{
			$sqlpath="";
		}
		if(isset($show))
		{
			$show=intval($show);
			if($show==0)$show=$pl_langpackage->pl_widget_plugin;
			else if($show==1) $show=$pl_langpackage->pl_app_application;
			else $show="Widget+APP";
		}
		else
		{
			$show="";
		}

		if(!isset($image))$image='';
		if(isset($title) && isset($author) && isset($version))
		{
		echo <<<EOD
			<div id="container">
<div id="title">
<ul>
<li id="tag1"><a href="#" onClick="switchTag('tag1','content1',3);this.blur();" class="selectli1"><span class="selectspan1"><?php echo $pl_langpackage->pl_plugin_description; ?></span></a></li>
<li id="tag2"><a href="#" onClick="switchTag('tag2','content2',3);this.blur();"><span><?php echo $pl_langpackage->pl_plugin_details; ?></span></a></li>
</ul>
</div>
<div id="content" class="content1">
<div id="content1">
<table width="100%" border="0" cellpadding="0" cellspacing="0" >
      <tr>
        <td width="23%"><?php echo $pl_langpackage->pl_plugin_name; ?>：</td>
        <td width="60%">&nbsp;$title</td>
        <td width="80" width="80" rowspan="5"><img  src="../plugins/$path/$image" width="80" height="80"/></td>
      </tr>
      <tr>
        <td><?php echo $pl_langpackage->pl_version_number; ?>：</td>
        <td>&nbsp;$version</td>
      </tr>
      <tr>
        <td><?php echo $pl_langpackage->pl_author; ?>：</td>
        <td>&nbsp;$author</td>
      </tr>
      <tr>
        <td><?php echo $pl_langpackage->pl_developer_home; ?>：</td>
        <td>&nbsp;$website</td>
      </tr>
      <tr>
        <td colspan="3"><?php echo $pl_langpackage->pl_plugin_description; ?>：<br/>$description</td>
      </tr>
    </table>
</div>
<div id="content2" class="hidecontent">
<table  width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td><?php echo $pl_langpackage->pl_address_entry; ?></td>
      <td>&nbsp;$inurl</td>
    </tr>
    <tr>
      <td><?php echo $pl_langpackage->pl_data_database_file; ?></td>
      <td>&nbsp;$sqlpath</td>
    </tr>
  </table>
</div>
</div>
</div>
EOD;
		return true;
	}
	else
	{
		echo $pl_langpackage->pl_plugin_info_notall;
		return false;
	}
}
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script>
function switchTag(tag,content,num){
for(i=1; i <num; i++){
   if("tag"+i==tag){
    document.getElementById(tag).getElementsByTagName("a")[0].className="selectli"+i;
    document.getElementById(tag).getElementsByTagName("a")[0].getElementsByTagName("span")[0].className="selectspan"+i;
   }else{
    document.getElementById("tag"+i).getElementsByTagName("a")[0].className="";
    document.getElementById("tag"+i).getElementsByTagName("a")[0].getElementsByTagName("span")[0].className="";
   }
   if("content"+i==content){
    document.getElementById(content).className="";
   }else{
    document.getElementById("content"+i).className="hidecontent";
   }
   document.getElementById("content").className=content;
}
}
</script>
<link rel="stylesheet" type="text/css" href="skin/css/plugin.css">
<title></title>
</head>

<body>
<h1><?php echo $pl_langpackage->pl_plugin_wizard;?></h1>
<hr />
<?php $flag=getInfo(get_args('path'));
if($flag)
{
?>
	  <script>parent.diag_install.URL='<?php echo $next?>';</script>
<?php }?>
</body>
</html>