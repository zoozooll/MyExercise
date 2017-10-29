<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;


//数据表定义区
$t_admin_user = $tablePreStr."admin_user";
$t_admin_group = $tablePreStr."admin_group";
//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_admin_user` order by admin_id asc";
$result = $dbo->fetch_page($sql,13);
//print_r($result['result']);
foreach($result['result'] as $key => $value){
	$group_id=$value['group_id'];
	$sql = "select * from `$t_admin_group` where id='$group_id'";
	$result_group = $dbo->getRow($sql);
	$result['result'][$key]['group_name']=$result_group['group_name'];
}
$right=check_rights("admin_group_update");
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <?php echo $a_langpackage->a_admin_list; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_admin_list; ?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=admin_add"><?php echo $a_langpackage->a_add_admin; ?></a></span></h3>
    <div class="content2">
		<form action="m.php?app=member_list" method="get" name="form">
		<input type="hidden" id="hidden_group" value="">
		<input type="hidden" id="right" value="<?php echo $right;?>">
		<table class="list_table">
			<thead>
			<tr style=" text-align:center;">
				<th>ID</th>
				<th align="left"><?php echo $a_langpackage->a_admin_name; ?></th>
				<th align="left"><?php echo $a_langpackage->a_admin_email; ?></th>
				<th><?php echo $a_langpackage->a_add_time; ?></th>
				<th><?php echo $a_langpackage->a_last_login_time; ?></th>
				<th><?php echo $a_langpackage->a_last_login; ?>IP</th>
				<th><?php echo $a_langpackage->a_mana_group; ?></th>
				<th><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) {?>
			<tr style="text-align:center;">
				<td><?php echo $value['admin_id'];?></td>
				<td align="left"><?php echo $value['admin_name'];?></td>
				<td align="left"><?php echo $value['admin_email'];?></td>
				<td><?php echo $value['add_time'];?></td>
				<td><?php echo $value['last_login'];?></td>
				<td><?php echo $value['last_ip'];?></td>
				<td id="update_<?php echo $value['admin_id']; ?>">
					<?php if($value['admin_id']!=1){?>
						<span onclick="edit(this,<?php echo $value['admin_id']; ?>)">&nbsp;<?php echo $value['group_name'];?>&nbsp;</span>
					<?php }?>
				</td>
				<td>
					<?php if($value['admin_id']!=1){?><a href="a.php?act=admin_del&id=<?php echo $value['admin_id'];?>"><?php echo $a_langpackage->a_delete; ?></a><?php }?>
				</td>
			</tr>
			<?php }} else { ?>
			<tr>
				<td colspan="8"></td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="8"><?php include("m/page.php"); ?></td>
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
function edit(span,id) {
	var rights=document.getElementById("right").value;
	if(rights != '0'){
		obj = document.getElementById("update_"+id);
		document.form.hidden_group.value=id;
		ajax("a.php?act=group_update","POST","id="+id+"&type=1",function(data){
			//alert(data);
			obj.innerHTML = data;
		});
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}
function update_group(value){
	id=document.getElementById("hidden_group").value;
	ajax("a.php?act=group_update","POST","group_id="+value+"&type=2"+"&id="+id,function(data){
		//alert(data);
		obj.innerHTML = data;
	});
}
</script>
</body>
</html>