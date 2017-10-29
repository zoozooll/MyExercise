<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

require("../foundation/module_remind.php");

$id = intval(get_args('id'));

//数据表定义区
$t_remind = $tablePreStr."remind";


dbtarget('r',$dbServs);
$dbo=new dbex;

$row = get_remind($dbo,$t_remind,$id);
//print_r($row);
$type=array(
	"1"=>$a_langpackage->a_buyer_remind,
	"2"=>$a_langpackage->a_saler_remind,
);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
</head>

<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings; ?> &gt;&gt; <?php echo $a_langpackage->a_remind_edit; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_remind_edit; ?></span> <span class="right" style="margin-right:15px;" ><a href="m.php?app=remind_set"><?php echo $a_langpackage->a_back_remind_list; ?></a></span></h3>
    <div class="content2">

		<form name="cpform" method="post" action="a.php?act=remind_upd" >
		<input class="small-text" type="hidden" name="id" value="<?php echo $id?>" />
		<table class="form-table">

		<tr><td width="58px"><?php echo $a_langpackage->a_remind_type; ?>:</td>
		<td><select name="remind_type">
		<?php foreach($type as $key=>$val){?>
		<option value="<?php echo $key?>" <?php if($row['remind_type']==$key){echo "selected";}?>><?php echo $val?></option>
		<?php }?>
		</select></td></tr>

		<tr><td><?php echo $a_langpackage->a_remind_name; ?>:</td>
		<td><input class="small-text" name="remind_name" value="<?php echo $row['remind_name']?>" type="text" style="width:400px" /></td></tr>

		<tr><td><?php echo $a_langpackage->a_remind_content; ?>:</td>
		<td><input class="small-text" name="remind_tpl" value="<?php echo $row['remind_tpl']?>" type="text" style="width:400px" /></td></tr>

		<tr><td><?php echo $a_langpackage->a_current_status; ?>:</td>
		<td><select name="enable">
		<option value="0" <?php if($row['enable']==0){echo "selected";}?>><?php echo $a_langpackage->a_close; ?></option>
		<option value="1" <?php if($row['enable']==1){echo "selected";}?>><?php echo $a_langpackage->a_open; ?></option>
		</select></td></tr>

		<tr><td colspan="2"><div class="fixsel"><input type="submit" class="regular-button" value="<?php echo $a_langpackage->a_mail_post; ?>" /></div></td></tr>

		</table>
		</form>
		</div>
	   </div>
     </div>
   </div>
</body>
</html>