<?php
$IWEB_SHOP_IN=true;
$self_path=dirname(__file__);
include_once($self_path."/../foundation/fgetandpost.php");
include_once($self_path."/plugin_layout.php");
if(get_args("submit"))
{
	$check_flag=true;
	if(get_args('title') && get_args('version') && get_args('author') && get_args('urls'))
	{
		$dom=new DOMDocument();
		$dom->encoding="UTF-8";
		$dom->version="1.0";
		$plugin=$dom->createElement("plugin");
		$title=$dom->createElement("title",get_args('title'));
		$plugin->appendChild($title);
		$version=$dom->createElement("version",get_args('version'));
		$plugin->appendChild($version);
		$author=$dom->createElement("author",get_args('author'));
		$plugin->appendChild($author);
		$image=$dom->createElement("image",get_args('image'));
		$plugin->appendChild($image);
		$description=$dom->createElement("description",get_args('description'));
		$plugin->appendChild($description);
		$website=$dom->createElement("website",get_args('website'));
		$plugin->appendChild($website);

		$sqlpath=$dom->createElement("sqlpath",get_args('sqlpath'));
		$plugin->appendChild($sqlpath);

		if(preg_match("/^([^:]+:[^,]+[,]?)*$/i",get_args('backrights')))
		{
			$backrights=$dom->createElement("backrights",get_args('backrights'));
			$plugin->appendChild($backrights);

		}
		else
		{
			 $check_flag=false;
		}
		if(preg_match("/^([^:]+:[^,]+[,]?)*$/i",get_args('frontrights')))
		{
			$frontrights=$dom->createElement("frontrights",get_args('frontrights'));
			$plugin->appendChild($frontrights);

		}
		else
		{
			 $check_flag=false;
		}


		$urls=get_args("urls");
		$urlids=get_args("urlids");
		if(count($urls)>0)
		{
			$urlsxml=$dom->createElement("urls");
			for($i=0;$i<count($urls);$i++)
			{
				if($urls[$i]!="")
				{
					$urlxml=$dom->createElement("url",$urls[$i]);
					$urlxml->setAttribute("id",$urlids[$i]);
					$urlsxml->appendChild($urlxml);
				}
			}
			$plugin->appendChild($urlsxml);
		}

		$dom->appendChild($plugin);
		if($check_flag)
		{
			$dom->formatOutput=true;
			$dom->save("plugin.xml");
			header("Content-type:application/xml");
			header("Content-Disposition:attachment;filename=plugin.xml");
			echo $dom->saveXML();
			exit();
		}
		else
		{
			 header("content-type:text/html;charset=utf-8");
			echo "<script>alert('前台或后台权限格式错误！');history.go(-1);</script>";
			exit();
		}
	}
	else
	{
		header("content-type:text/html;charset=utf-8");
		echo "<script>alert('请选择每一个必选项');history.go(-1);</script>";
		exit();
	}
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
.reg{color:#FF0000}
.blue{color:#0000FF}
ul li{
list-style:none;
}
label{
padding-right:10px;
}
</style>
<script language="JavaScript" src="http://ajax.microsoft.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
<title>IWEB插件信息生成器</title>
</head>

<body>
<h1 style="text-align:center">插件信息生成器</h1>
<div >
<form action="" method="post">
<table width="56%" border="0">
  <tr>
    <td width="23%">插件名称：</td>
    <td width="77%"><input type="text" name="title" id="title" />
      <span class="reg">      *</span></td>
  </tr>
  <tr>
    <td>版本号：</td>
    <td><input type="text" name="version" id="version" />
      <span class="reg">*</span></td>
  </tr>
  <tr>
    <td>开发者：</td>
    <td><input type="text" name="author" id="author" />
      <span class="reg">*</span></td>
  </tr>
  <tr>
    <td>缩略图：</td>
    <td><input type="text" name="image" id="image" /></td>
  </tr>
  <tr>
    <td>插件描述：</td>
    <td><textarea name="description" id="description" cols="45" rows="5"></textarea></td>
  </tr>
  <tr>
    <td>后台权限：</td>
    <td><textarea name="backrights" cols="45" rows="5" id="backrights"></textarea></td>
  </tr>
  <tr>
    <td>前台权限：</td>
    <td><textarea name="frontrights" cols="45" rows="5" id="frontrights"></textarea></td>
  </tr>
  <tr>
    <td>开发者主页：</td>
    <td><input name="website" type="text" id="website" size="80" /></td>
  </tr>
    <tr>
    <td>数据库安装SQL文件：</td>
    <td><input name="sqlpath" type="text" id="sqlpath" size="80" /></td>
  </tr>
  <tr>
    <td>展示位置：</td>
    <td>文件地址：<input type="button"  value="增加" onclick="addurl()"/></td>
  </tr>
<tr class="urls">
    <td><select name="urlids[]">
	<?php
	foreach($plugin_layout as $id=>$value)
	{
		echo "<option value='$id' >$value</option>";
	}
	?>
    </select></td>
    <td><input name="urls[]" type="text" id="url" size="60" />
      <span class="blue">*</span></td>
  </tr>
  <tr id="sub_button">
    <td colspan="2"><input type="submit" name="submit" id="生成" value="提交" /></td>
    </tr>
</table>
</form>
</div>
<script>
var sub_button=$("#sub_button");
function addurl()
{
	var urls_one=$(".urls").eq(0).clone();
	urls_one.find("#url").val("");
	urls_one.find("option").eq(0).attr("selected","selected");
	urls_one.insertBefore(sub_button);
}
</script>
</body>
</html>
