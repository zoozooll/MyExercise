<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_asd.php");
//权限管理
$right=check_rights("adv_get_code");
if(!$right){
	header('location:m.php?app=error');
}
//引入语言包
$a_langpackage=new adminlp;

$position_id = intval(get_args('id'));
if(!$position_id) {
	exit($a_langpackage->a_error);
}

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

//数据表定义区
$t_asd_position = $tablePreStr."asd_position";

$asd_position_info = get_asd_position_info($dbo,$t_asd_position,$position_id);

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
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_asd_management;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_getasd_code; ?></a></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_getasd_code; ?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=asd_position_list"><?php echo $a_langpackage->a_asdposition_list; ?></a></span></h3>
    <div class="content2">
		<table class="form-table">
			<tbody>
			<tr>
				<td width="140px"><?php echo $a_langpackage->a_asdposition_name; ?>：</td>
				<td><?php echo $asd_position_info['position_name']; ?></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asdposition_size; ?>：</td>
				<td><?php echo $asd_position_info['asd_width']; ?>X<?php echo $asd_position_info['asd_height']; ?></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asdposition_desc; ?>：</td>
				<td><?php echo $asd_position_info['position_desc']; ?></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asdpos_code; ?>：</td>
				<td><textarea style="width:90%; height:40px" id="content"><script language="JavaScript" src="uploadfiles/asd/<?php echo $asd_position_info['position_id']; ?>.js"></script></textarea></td>
			</tr>
			<tr>
				<td colspan="2"><span class="button-container"><input class="regular-button" type="button" value="<?php echo $a_langpackage->a_copy_code; ?>" onclick="copyToClipBoard()" /></span></td>
			</tr>
			</tbody>
		</table>
	   </div>
	  </div>
	</div>
</div>
<script language="JavaScript">
<!--
function copyToClipBoard(){
	var clipBoardContent = document.getElementById("content").innerHTML;
	clipBoardContent = clipBoardContent.replace(/&gt;/g, ">");
	clipBoardContent = clipBoardContent.replace(/&lt;/g, "<");
	clipBoardContent = clipBoardContent.replace(/&amp;/g, "&");
	window.clipboardData.setData("Text",clipBoardContent);
	alert("<?php echo $a_langpackage->a_copyed_code; ?>!");
}
//-->
</script>
</body>
</html>