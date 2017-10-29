<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_users.php");
//引入语言包
$a_langpackage=new adminlp;

$rank_id = intval(get_args('id'));
if(!$rank_id) {
	exit($a_langpackage->a_error);
}

//数据表定义区
$t_user_rank = $tablePreStr."user_rank";
$t_privilege = $tablePreStr."privilege";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$info = get_userrank_info($dbo,$t_user_rank,$rank_id);
$privilege_list = get_privilege_list($dbo,$t_privilege);
$privilege = unserialize($info['privilege']);

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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <?php echo $a_langpackage->a_member_rank_update?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_member_rank_update?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=member_rank" style="float: right;"><?php echo $a_langpackage->a_user_rank?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=member_rank_edit" method="post">
		<table class="form-table">
		 <tbody>
			<tr>
				<td width="85px"><?php echo $a_langpackage->a_member_rank_name?>：</td>
				<td><input class="small-text" type="text" name="rank_name" value="<?php echo $info['rank_name'];?>"></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_set_privilege?>：</td>
				<td>
				<?php foreach($privilege_list as $key=>$value) { ?>
					<?php if($SYSINFO['sys_domain']){?>
						<input type="checkbox" name="privilege[<?php echo $key; ?>]" value="1" <?php if(isset($privilege[$key]) && $privilege[$key]){echo "checked";} ?> ><?php echo $value['privilege_name']; ?>
							<?php if($value['privilege_type']){ ?>
							<input class="small-text" type="text" name="privilege_name[<?php echo $key; ?>]" value="<?php if(isset($privilege[$key])) {echo $privilege[$key];} ?>" />
							<?php } ?>
						<br />
					<?php }else {?>
						<?php if($value['privilege_id'] != '9') {?>
							<input type="checkbox" name="privilege[<?php echo $key; ?>]" value="1" <?php if(isset($privilege[$key]) && $privilege[$key]){echo "checked";} ?> ><?php echo $value['privilege_name']; ?>
							<?php if($value['privilege_type']){ ?>
							<input class="small-text" type="text" name="privilege_name[<?php echo $key; ?>]" value="<?php if(isset($privilege[$key])) {echo $privilege[$key];} ?>" />
							<?php } ?>
						<br />
						<?php }?>
					<?php }?>
				<?php } ?>
				</td>
			</tr>
			<tr>
				<input type="hidden" name="rank_id" value="<?php echo $info['rank_id'];?>">
				<td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_update_member_rank?>" /></span></td>
			</tr>
		  </tbody>
		</table>
		</form>
	   </div>
	 </div>
	</div>
</div>
</body>
</html>