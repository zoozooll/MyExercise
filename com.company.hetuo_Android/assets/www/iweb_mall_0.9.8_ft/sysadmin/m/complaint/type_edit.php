<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_complaint.php");

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_complaint_type = $tablePreStr."complaint_type";

$type_id=intval(get_args('type_id'));

if ($type_id){
	//权限管理
	$right=check_rights("edit_complaint_title");
	if(!$right){
		header('Location: m.php?app=error');
		exit;
	}
}else {
	//权限管理
	$right=check_rights("complaint_title_add");
	if(!$right){
		header('Location: m.php?app=error');
		exit;
	}
}
//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
if($type_id){
	$result = get_complaint_type_byid($dbo,"*",$t_complaint_type,$type_id);
}else{
	$result['type_content'] = "";
}

//print_r($result);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
td span {color:red;}
.green {color:green;}
.red {color:red;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_order_mengament;?> &gt;&gt; <?php echo $a_langpackage->a_title_management_of_complaints; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_title_management_of_complaints; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=complaint_type"><?php echo $a_langpackage->a_heading_the_list_of_complaints; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=complaint_type_edit" name="form1" id="form1" method="post">
		<table class="form-table">
		   <tbody>
			<tr>
				<td width="60px"><?php echo $a_langpackage->a_complaint_title; ?>：</td>
				<td><input type="text" class="small-text" name="type_content" value="<?php echo $result['type_content'];?>" />
				<input type="hidden" name="type_id" value="<?php echo $result['type_id'];?>"/></td>
			</tr>
			<tr>
				<td colspan="2"><input class="regular-button" type="submit" value="<?php echo $a_langpackage->a_submit; ?>" /></td>
			</tr>
			</tbody>
		</table>
		</form>
	   </dov>
	  </div>
	</div>
</div>
</body>

</html>