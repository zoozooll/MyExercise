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
$show_data='';
if(empty($tool_item->item(0)->nodeValue)){
	$tool_item=array();
	$show_error='';
	$show_data='content_none';
	$show_str=$t_langpackage->t_none_tool;
}

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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $t_langpackage->t_tool_manage;?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $t_langpackage->t_tool_manage;?></h3>
    <div class="content2">
		<table class="list_table" >
		<thead>
			<tr style="text-align:center;">
				<th align="left"><?php echo $t_langpackage->t_name;?></th>
				<th><?php echo $t_langpackage->t_code_num;?></th>
				<th><?php echo $t_langpackage->t_time;?></th>
				<th><?php echo $t_langpackage->t_author;?></th>
				<th><?php echo $t_langpackage->t_ctrl;?></th>
			</tr>
		</thead>
		<tbody>
		<?php
		foreach($tool_item as $rs){
		?>
			<tr style="text-align:center;">
				<td align="left"><?php echo $rs->getElementsByTagName('toolName')->item(0)->nodeValue;?></td>
				<td><?php echo $rs->getAttribute("id");?></td>
				<td><?php echo $rs->getElementsByTagName('date')->item(0)->nodeValue;?></td>
				<td><?php echo $rs->getElementsByTagName('author')->item(0)->nodeValue;?></td>
				<td><a href='a.php?act=unload_tool&id=<?php echo $rs->getAttribute("id");?>' onclick='return confirm("<?php echo $t_langpackage->t_ask_unset;?>")'><?php echo $t_langpackage->t_unset;?></a></td>
			</tr>
		<?php }?>
		  </tbody>
		</table>

		<table class="main main_left <?php echo $show_error;?>">
			<tr>
				<td><?php echo $show_str;?></td>
			</tr>
		</table>

		<table class="list_table">
		  <tbody>
			<tr><td colspan="2" style="font-weight:bold;"><?php echo $t_langpackage->t_tip_inf;?></td></tr>
			<tr>
				<td><?php echo $t_langpackage->t_manage_pro_1;?></td>
			</tr>
			<tr>
				<td><?php echo $t_langpackage->t_manage_pro_2;?></td>
			</tr>
			<tr>
				<td><?php echo $t_langpackage->t_manage_pro_3;?></td>
			</tr>
		  </tbody>
		</table>
		</div>
	  </div>
	</div>
</div>
</body>
</html>