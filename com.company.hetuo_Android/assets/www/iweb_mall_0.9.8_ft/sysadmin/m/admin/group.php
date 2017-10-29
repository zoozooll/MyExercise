<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("group_update");

//数据表定义区
$t_admin_group = $tablePreStr."admin_group";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
if($_POST){
	//权限管理
	$right=check_rights("group_add");
	if(!$right){
		header('location:m.php?app=error');
	}else{
		$name = short_check(get_args('name'));
		if(empty($name)) {
			echo "<script type='text/javascript'>alert('$a_langpackage->a_adminnamenot_null');window.location.href='m.php?app=admin_group';</script>";
			exit;
		}
		$sql = "insert `$t_admin_group` (group_name) values ('$name')";
		$dbo->exeUpdate($sql);
	}
}
$sql = "select * from `$t_admin_group` where del_flg=0 ";
$result = $dbo->fetch_page($sql,10);

$sql_max = "select max(id)as max from `$t_admin_group` ";
$result_max = $dbo->getRow($sql_max);

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
td span {color:black;}
.green {color:green;}
.red {color:red;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_m_admingroup_set; ?></a></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_m_admingroup_set; ?></span> <span class="right" style="margin-right:15px;"><a href="javascript:new_add('add');"><?php echo $a_langpackage->a_add; ?><?php echo $a_langpackage->a_mana_group; ?></a></span></h3>
    <div class="content2">
		<form action="m.php?app=admin_group" method="post" name="form">
		<input type="hidden" id="edit" value="<?php echo $right;?>">
		<table class="list_table">
			<thead>
			<tr style="text-align:center;">
				<th width="60px">ID</th>
				<th align="left"><?php echo $a_langpackage->a_mana_group; ?><?php echo $a_langpackage->a_name; ?></th>
				<th width="100px"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style="text-align:center;">
				<td width="60px"><?php echo $value['id'];?></td>
				<td align="left" id="group_<?php echo $value['id']; ?>"><span onclick="edit_group_name(this,<?php echo $value['id']; ?>)"><?php echo $value['group_name'];?></span></td>
				<td width="100px">
					<?php if ($value['id'] > 1){?>
						<a href="a.php?act=group_del&id=<?php echo $value['id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_mana_del_group; ?>')"><?php echo $a_langpackage->a_delete; ?></a>
					<?php }?>
					<a href="m.php?app=group_rights&id=<?php echo $value['id'];?>"><?php echo $a_langpackage->a_set_privilege; ?></a>
				</td>
			</tr>
			<?php }} else { ?>
			<tr>
				<td colspan="7"></td>
			</tr>
			<?php } ?>
			<tr id="add" style="display:none; text-align:center;">
				<td><?php echo $result_max['max']+1;?></td>
				<td align="left"><input class="small-text" type="text" name="name"></td>
				<td><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_add; ?><?php echo $a_langpackage->a_mana_group; ?>"></span></td>
			</tr>
			<tr>
				<td colspan="7"><?php include("m/page.php"); ?></td>
			</tr>
			</tbody>
		</table>
		</form>
	   </div>
	 </div>
   </div>
</div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function edit_group_name(span,id) {
	var rights=document.getElementById("edit").value;
	if(rights != '0'){
		obj = document.getElementById("group_"+id);
		group_value = span.innerHTML;
		group_value = group_value.replace(/&nbsp;/ig,"");
		obj.innerHTML = '<input style="width:60px" type="text" id="group_edit_' + id + '" value="' + group_value + '" onblur="group_edit_post(this,' + id + ')" />';
		document.getElementById("group_edit_"+id).focus();
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}
function group_edit_post(input,id){
	var obj = document.getElementById("group_"+id);
	if(group_value==input.value) {
		obj.innerHTML = '<span onclick="edit_group_name(this,' + id + ')">&nbsp;' + group_value + '&nbsp;</span>';
	} else {
		ajax("a.php?act=group_edit","POST","id="+id+"&value="+input.value,function(data){
			if(data==1) {
				obj.innerHTML = '<span onclick="edit_group_name(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_group_name(this,' + id + ')">&nbsp;' + group_value + '&nbsp;</span>';
			}
			if (data== -1){
				alert("<?php echo $a_langpackage->a_privilege_mess;?>");
				location.href="m.php?app=error";
			}
		});
	}
}
function new_add(DIV) {
	document.getElementById(DIV).style.display='';
}

// -->
</script>
</body>
</html>