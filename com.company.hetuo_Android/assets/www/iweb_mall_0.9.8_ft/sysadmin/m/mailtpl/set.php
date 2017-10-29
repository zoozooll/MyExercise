<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

require("../foundation/module_mailtpl.php");

//数据表定义区
$t_mailtpl = $tablePreStr."mailtpl";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$mailtpl=get_all($dbo,$t_mailtpl);
//print_r($mailtpl);

$email_update=check_rights("email_update");
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings; ?> &gt;&gt; <?php echo $a_langpackage->a_mailtpl_setting; ?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_mailtpl_setting; ?></h3>
    <div class="content2">
		<table class="list_table">
			<thead>
				<tr>
					<th><?php echo $a_langpackage->a_mailtpl_name; ?></th>
					<th><?php echo $a_langpackage->a_operate; ?></th>
				</tr>
			</thead>
			<tbody>
		<?php foreach($mailtpl as $value) { ?>
			<tr>
				<td><?php echo $value['tpl_name'];?></td>
				<td class="center" width="80">
				<?php if($email_update){?>
				<a href="m.php?app=mailtpl_edit&id=<?php echo $value['tpl_id'];?>"><?php echo $a_langpackage->a_update; ?></a>
				<?php }?>
				</td>
			</tr>
		<?php } ?>
			<tbody>
		</table>
	  </div>
	 </div>
   </div>
</div>
</body>
</html>