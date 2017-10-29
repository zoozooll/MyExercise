<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;
require("../foundation/asession.php");
require("../configuration.php");
require("includes.php");
//引入语言包
$a_langpackage=new adminlp;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<style>
*{font-size:12px;}
h3{margin:6px 0 0 0;padding:0}
</style>
</head>
<body>
<?php
$IWEB_SHOP_IN = true;
require_once(dirname(__file__)."/../foundation/fgetandpost.php");
require_once(dirname(__file__)."/../foundation/cxmloperator.class.php");
$xmlpath="resources/resources.xml";
$xml=new XMLOperator($xmlpath);
if(get_args('addgroup'))
{
	$id=get_args('id');
	$value=get_args('value');
	if($id && $value && !$xml->query("//group[@id='$id']"))
	{
		$xml->addNode("/resources","group","","id=$id;value=$value");
	}
	$xml->save($xmlpath);
}
else if(get_args('updgroup'))
{
	$id=get_args('id');
	$value=get_args('value');
	$xml->updAttr("//group[@id='$id']","value",$value);
	$xml->save($xmlpath);
}
$groups=$xml->query("//group");
if(get_args('submit'))
{
	$group_id=get_args('group');
	$id=get_args('id');
	$value=get_args('value');
	if(get_args('op')=='upd')
	{
		if(!$xml->query("//group[@id='$group_id']/resource[@id='$id']"))
		{
			$xml->delNode("//resource[@id='$id']");
			$xml->addNode("//group[@id='$group_id']","resource","","id=$id;value=$value");
		}else
		{
			$xml->updAttr("//resource[@id='$id']","value",$value);
		}
		$xml->save($xmlpath);
	}
	else
	{
		if($id && $value)
		{
			if(!$xml->query("//resource[@id='$id']"))
			{
				if($xml->addNode("//group[@id='$group_id']","resource","","id=$id;value=$value"))$xml->save($xmlpath);
			}
			else
			{
				echo $a_langpackage->a_resourceid_resource_null;
			}
		}
		else
		{
			echo $a_langpackage->a_resourceid_exist;
		}
	}
}

$select="<select id='group' name='group'>";
$show="";
if($groups)
{
	foreach($groups as $group)
	{
		$id=$group->getAttributeNode("id")->value;
		$value=$group->getAttributeNode("value")->value;
		$show.="<h3>{$value}[<a href='#' onclick='add(\"$id\")'>".$a_langpackage->a_add."</a> <a href='#' onclick='updgroup(\"$id\",\"$value\")'>".$a_langpackage->a_update."</a>]</h3><hr/>";
		$select.="<option value='$id'>$value</option>";
		$resources=$xml->query("/resources/group[@id='$id']/resource");
		if($resources && $resources->length>0)
		{
			foreach($resources as $resource)
			{
				$rid=$resource->getAttributeNode("id")->value;
				$rvalue=$resource->getAttributeNode("value")->value;
				$show.= "$rvalue <a href='#' onclick='update(\"$rid\",\"$rvalue\",\"$id\")'>".$a_langpackage->a_update."</a> ";
			}
		}
	}
}
$select.="</select>";
echo "<form id='form' method='post'>".$a_langpackage->a_privilege_groups.":{$select}<a href='#'onclick='addgroup()'>".$a_langpackage->a_groups_add."</a><span id='resource'>".$a_langpackage->a_privilege_id."：<input name='id' value=''>".$a_langpackage->a_privilege_name."：<input name='value'/><input type='submit' name='submit' value=".$a_langpackage->a_add."></span></form> $show";
?>
<script>
function update(id,value,gid)
{
	document.getElementById("resource").innerHTML="<?php echo $a_langpackage->a_privilege_id;?>：<input name='id' value='"+id+"' readonly='readonly' /><?php echo $a_langpackage->a_privilege_name;?>：<input name='value' value='"+value+"'/><input type='hidden' name='op' value='upd'/><input type='submit' name='submit' value='<?php echo $a_langpackage->a_update;?>'>";
	SelectItem(document.getElementById('group'),gid);
}
function add(gid)
{
	document.getElementById("resource").innerHTML="<?php echo $a_langpackage->a_privilege_id;?>：<input name='id' value='' /><?php echo $a_langpackage->a_privilege_name;?>：<input name='value' value=''/><input type='submit' name='submit' value='<?php echo $a_langpackage->a_add;?>'>";
	SelectItem(document.getElementById('group'),gid);
}
function addgroup()
{
	document.getElementById("resource").innerHTML="<?php echo $a_langpackage->a_groups_id;?>：<input name='id' value='' /><?php echo $a_langpackage->a_groups_name;?>：<input name='value' value=''/><input type='submit' name='addgroup' value='<?php echo $a_langpackage->a_add;?>'>";
}
function updgroup(id,value)
{
	document.getElementById("resource").innerHTML="<?php echo $a_langpackage->a_groups_id;?>：<input name='id' value='"+id+"'  readonly='readonly'/><?php echo $a_langpackage->a_groups_name;?>：<input name='value' value='"+value+"'/><input type='submit' name='updgroup' value='<?php echo $a_langpackage->a_update;?>'>";
}
function SelectItem(objSelect, objItemText)
{
	var isExit = false;
	for (var i = 0; i < objSelect.options.length; i++)
	{
		if (objSelect.options[i].value == objItemText || objSelect.options[i].text == objItemText)
		{
			objSelect.options[i].selected = true;
			break;
		}
	}
}
</script>
</body>
</html>
