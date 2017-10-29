<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

require("../foundation/module_crons.php");

$id = intval(get_args('id'));

//数据表定义区
$t_crons = $tablePreStr."crons";

dbtarget('r',$dbServs);
$dbo=new dbex;

$row = get_crons_row($dbo,$t_crons,$id);

$week=array(
	"0"=>"日",
	"1"=>"一",
	"2"=>"二",
	"3"=>"三",
	"4"=>"四",
	"5"=>"五",
	"6"=>"六",
);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<script type="text/JavaScript">
var admincpfilename = 'admincp.php', IMGDIR = 'images/default', STYLEID = '1', VERHASH = '1fP', IN_ADMINCP = true, ISFRAME = 1;
</script>
<script src="include/js/common.js" type="text/javascript"></script>
<script src="images/admincp/admincp.js" type="text/javascript"></script>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?>&gt;&gt; <?php echo $a_langpackage->a_application_management;?>&gt;&gt;<?php echo $a_langpackage->a_edit_plan;?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_edit_plan; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=sys_crons" style="float: right;"><?php echo $a_langpackage->a_back_list;?></a></span></h3>
    <div class="content2">
		<table class="list_table" >
			<thead>
				<tr><th><?php echo $a_langpackage->a_skill_notice;?></th></tr>
			</thead>
			<tbody>
				<tr><td><ul id="tipslis"><li><?php echo $a_langpackage->a_crons_edit_message1;?></li><li><?php echo $a_langpackage->a_crons_edit_message2;?></li></ul></td></tr>
			</tbody>
		</table>
		<form name="cpform" method="post" action="a.php?act=sys_crons_add" >
		<input type="hidden" name="id" value="<?php echo $id?>" />
		<table class="list_table">
		  <tbody>
			<tr><td colspan="2"><?php echo $a_langpackage->a_plan_name;?>:</td>
			<td colspan="2" ><input class="small-text" name="name" value="<?php echo $row['name']?>" type="text" class="txt"   /></td></tr>
			<tr><td colspan="2"><?php echo $a_langpackage->a_is_exe;?>:</td>
			<td colspan="2" ><select name="available"><option value="1" <?php if($row['available']==1){echo "selected";}?>><?php echo $a_langpackage->a_useable;?></option><option value="0" <?php if($row['available']==0){echo "selected";}?>><?php echo $a_langpackage->a_no_useable;?></option></select></td></tr>

			<tr><td colspan="2"><?php echo $a_langpackage->a_week_ever;?>:</td>
			<td>
			<select name="weekdaynew"><option value="-1">*</option>
			<?php for($i=0;$i<=6;$i++){?>
			<option value="<?php echo $i?>" <?php if($row['weekday']==$i){echo "selected";}?>><?php echo $week[$i]?></option>
			<?php }?>
			</select></td><td><?php echo $a_langpackage->a_crons_edit_message3;?></td></tr>
			<tr><td colspan="2"><?php echo $a_langpackage->a_month;?>:</td>
			<td>
			<select name="daynew"><option value="-1">*</option>
			<?php for($i=1;$i<=31;$i++){?>
			<option value="<?php echo $i?>" <?php if($row['day']==$i){echo "selected";}?>><?php echo $i?><?php echo $a_langpackage->a_date;?></option>
			<?php }?>
			</select></td><td><?php echo $a_langpackage->a_crons_edit_message4;?></td></tr>
			<tr><td colspan="2"><?php echo $a_langpackage->a_hour_ever;?>:</td>
			<td>
			<select name="hournew"><option value="-1">*</option>
			<?php for($i=0;$i<=24;$i++){?>
			<option value="<?php echo $i?>" <?php if($row['hour']==$i){echo "selected";}?>><?php echo $i?><?php echo $a_langpackage->a_hour;?></option>
			<?php }?>
			</select></td><td><?php echo $a_langpackage->a_crons_edit_message5;?></td></tr>
			<tr><td colspan="2"><?php echo $a_langpackage->a_minu_ever;?>:</td>
			<td>
			<input class="small-text" name="minutenew" value="<?php echo $row['minute']?>" type="text" class="txt"   /></td><td class="vtop tips2"><?php echo $a_langpackage->a_crons_edit_message6;?></td></tr>
			<tr><td colspan="2"><?php echo $a_langpackage->a_task_script;?>:</td>
			<td>
			<input class="small-text" name="filenamenew" value="<?php echo $row['phpfile']?>" type="text" class="txt"   /></td><td class="vtop tips2"><?php echo $a_langpackage->a_crons_edit_message7;?></td></tr>
			<tr><td colspan="15"><span class="button-container"><input type="submit" class="regular-button" id="submit_editsubmit" name="editsubmit" title="<?php echo $a_langpackage->a_submit;?>" value="<?php echo $a_langpackage->a_submit;?>" /></span></td></tr>
			<script type="text/JavaScript">_attachEvent(document.documentElement, 'keydown', function (e) { entersubmit(e, 'editsubmit'); });</script>
		  </tbody>
		</table>
		</form>
		</div>
		</div>
	</div>
</div>
</body>
</html>