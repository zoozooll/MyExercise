<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once($webRoot."/foundation/cxmloperator.class.php");

$ri_langpackage=new rightlp;
$a_langpackage = new adminlp;

$dbo = new dbex;
$t_admin_group=$tablePreStr."admin_group";

$gid=get_args('id');

if(get_args('action')){

	//权限管理
	$right=check_rights("group_privi");
//	if($right){
//		header('location:m.php?app=error');
//	}
	//E.T eidt
	if(!$right){
		header('location:m.php?app=error');
		exit();
	}
	//E.T eidt
	dbtarget('w',$dbServs);
	$gid=get_args('group_id');
	$sql="update $t_admin_group set rights=''";
	if(!is_null(get_args('rights')))$sql="update $t_admin_group set rights='".implode(",",get_args('rights'))."'";
	$sql.="where id='$gid'";
	if($dbo->exeUpdate($sql)) {
		$a_permissions_update_suc = $a_langpackage->a_permissions_update_suc;
		echo "<script type=\"text/javascript\"> alert('$a_permissions_update_suc');</script>";

	}
}

dbtarget('r',$dbServs);
$sql="select * from $t_admin_group where id='$gid'";
$groups=$dbo->getRow($sql);
$group=$groups['rights'];
$rights=explode(",",$group);
$rights=array_flip($rights);

$dom=new DOMDocument();
$dom->load("resources/resources.xml");
$classes=$dom->getElementsByTagName("group");

$dom_plugin=new DOMDocument();
$dom_plugin->load($webRoot."plugins/resources.xml");
$classes_plugin=$dom_plugin->getElementsByTagName("group");

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<script type='text/javascript'>
function select_rights(obj){
	var group=document.getElementsByTagName('input');
	for(i=0;i<group.length;i++){
		if(group[i].type=='checkbox'&&group[i].title==obj.title)
			group[i].checked=obj.checked;
	}
}
</script>
<style>
.rights {float:left; margin-left:5px; width:175px;}
</style>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <a href=""><?php echo $ri_langpackage->ri_allot;?></a></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $ri_langpackage->ri_allot;?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=admin_group"><?php echo $a_langpackage->a_m_admingroup_set; ?></a></span></h3>
    <div class="content2">
		<form action="m.php?app=group_rights" method="post" name="form">
			<input type="hidden" name="group_id" value="<?php echo $gid;?>">
		<table class="list_table">
			<thead>
				<tr><th><?php echo $ri_langpackage->ri_local_user;?>：<?php echo $groups['group_name'];?></th></tr>
			</thead>
			<tbody>
		<?php
		$group_num=0;
		foreach($classes as $class){
			$group_num++;
			echo "<tr><td style='font-weight:bold;'>&nbsp;".$class->getAttributeNode('value')->value."&nbsp;<input type='checkbox' title='group_".$group_num."' onclick='select_rights(this)' />".$ri_langpackage->ri_all_select."</td></tr><tr><td>";
			$items=$class->getElementsByTagName("resource");
			for($i = 0; $i < $items->length;$i++){
			$is_checked="";
			if(isset($rights[$items->item($i)->getAttributeNode('id')->value])){
				$is_checked="checked=checked";
			}
		?>
			<li class='rights'><input type="checkbox" title="<?php echo "group_".$group_num;?>" name="rights[<?php echo $items->item($i)->getAttributeNode('id')->value;?>]"  value="<?php echo $items->item($i)->getAttributeNode('id')->value;?>"  <?php echo $is_checked;?> /><?php echo $items->item($i)->getAttributeNode('value')->value;?></li>
		<?php }?>
			</td></tr>
			<tr><td style="height:5px; background:#E5E5E5;"></td></tr>
		<?php }
		foreach($classes_plugin as $class){
			$group_num++;
			echo "<tr><td style='font-weight:bold;'>&nbsp;".$class->getAttributeNode('value')->value."&nbsp;<input type='checkbox' title='group_".$group_num."' onclick='select_rights(this)' />".$ri_langpackage->ri_all_select."</td></tr><tr><td>";
			$items=$class->getElementsByTagName("resource");
			for($i = 0; $i < $items->length;$i++){
			$is_checked="";
			if(isset($rights[$items->item($i)->getAttributeNode('id')->value])){
				$is_checked="checked=checked";
			}
		?>
			<li class='rights'><input type="checkbox" title="<?php echo "group_".$group_num;?>" name="rights[<?php echo $items->item($i)->getAttributeNode('id')->value;?>]"  value="<?php echo $items->item($i)->getAttributeNode('id')->value;?>"  <?php echo $is_checked;?> /><?php echo $items->item($i)->getAttributeNode('value')->value;?></li>
		<?php }?>
			</td></tr>
			<tr><td style="height:5px; background:#E5E5E5;"></td></tr>
		<?php }?>
			<tr><td><span class="button-container"><input class="regular-button" type="submit" name="action" value="<?php echo $ri_langpackage->ri_allot;?>"/></span></td></tr>
			</tbody>
			</table>
		</form>
	   </div>
	  </div>
	</div>
</div>
</body>
</html>
