<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$t_langpackage=new toollp;
$a_langpackage=new adminlp;

$xmlDom=new DomDocument;
$xmlDom->load('toolsBox/tool.xml');
$tool_item=$xmlDom->getElementsByTagName('tool_item');
$show_str='';
$show_error='content_none';
if(empty($tool_item->item(0)->nodeValue)){
	$tool_item=array();
	$show_error='';
	$show_str=$t_langpackage->t_none_tool;
}
//权限管理
$right=check_rights("tools_exe");

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $t_langpackage->t_list;?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $t_langpackage->t_list;?></h3>
    <div class="content2">
		<input type="hidden" id="exe" value="<?php echo $right;?>">
		<table class="list_table" >
		<thead>
			<tr style="text-align:center;">
				<th align="left"><?php echo $t_langpackage->t_tool_name;?></th>
				<th><?php echo $t_langpackage->t_tool_explain;?></th>
				<th><?php echo $t_langpackage->t_ctrl;?></th>
			</tr>
		</thead>
		<?php
		if($tool_item){
		foreach($tool_item as $rs){
		?>
		<tbody>
			<tr style="text-align:center;">
				<td align="left"><?php echo $rs->getElementsByTagName('toolName')->item(0)->nodeValue;?></td>
				<td><?php echo $rs->getElementsByTagName('explain')->item(0)->nodeValue;?></td>
				<td><span class="button-container"><input class="regular-button" type='button' class='top_button' style='width:70px' value='<?php echo $t_langpackage->t_onclick_act?>' onclick='del()' /></span></td>
			</tr>
		<?php }}else{?>
			<tr>
				<td><?php echo $t_langpackage->t_tool_no;?></td>
			</tr>
		<?php }?>
		</tbody>
		</table>

		<table class="list_table">
		  <tbody>
			<tr><td colspan="2" style="font-weight:bold;"><?php echo $t_langpackage->t_tip_inf;?></td></tr>
			<tr>
				<td><?php echo $t_langpackage->t_tool_pro_1;?></td>
			</tr>
			<tr>
				<td><?php echo $t_langpackage->t_tool_pro_2;?></td>
			</tr>
			<tr>
				<td><?php echo $t_langpackage->t_tool_pro_3;?></td>
			</tr>
			<tr>
				<td><?php echo $t_langpackage->t_tool_pro_4;?></td>
			</tr>
			</tbody>
		</table>
		</div>
	 </div>
   </div>
</div>
</body>
<script language="JavaScript">
<!--
function del() {
	var rights=document.getElementById("exe").value;
	if(rights !='0'){
		if(confirm("<?php echo str_replace("{tool}",$rs->getElementsByTagName('toolName')->item(0)->nodeValue,$t_langpackage->t_ask_action);?>"))
		{window.location.href="front_end.php?app=<?php echo $rs->getElementsByTagName('contentIndex')->item(0)->nodeValue;?>";}
	}else{
		alert("<?php echo $t_langpackage->t_no_rights;?>");
		location.href="m.php?app=error";
	}
}
</script>
</html>