<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_users.php");
//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_user_rank = $tablePreStr."user_rank";
$t_privilege = $tablePreStr."privilege";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_user_rank` order by rank_id desc";

$result = $dbo->fetch_page($sql,13);

$privilege_list = get_privilege_list($dbo,$t_privilege);

foreach($result['result'] as $key=>$value) {
	$privilege = '';
	if(!isset($result['result'][$key]['pri'])) {
		$result['result'][$key]['pri'] = '';
	}
	if($value['privilege']) {
		$privilege = unserialize($value['privilege']);
		if($privilege) {
			foreach($privilege as $k=>$v) {
				$result['result'][$key]['pri'] .= $privilege_list[$k]['privilege_name'].':'.$v.' ';
			}
		}
	}
}
$right_array=array(
	"user_rank_edit"    =>   "0",
    "user_rank_del"    =>   "0",
);
foreach($right_array as $key => $value){
	$right_array[$key]=check_rights($key);
}
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <?php echo $a_langpackage->a_user_rank?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_user_rank?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=member_rank_add" style="float: right;"><?php echo $a_langpackage->a_add_rank?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=member_rank_del" method="post">
		<table class="list_table">
		 <thead>
			<tr style="text-align:center;">
				<th width="20px" class="center"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="30px"><?php echo $a_langpackage->a_ID?></th>
				<th width="90px" align="left"><?php echo $a_langpackage->a_user_rank?></th>
				<th align="left"><?php echo $a_langpackage->a_privilege?></th>
				<th width="120px"><?php echo $a_langpackage->a_operate?></th>
			</tr>
		 </thead>
		 <tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style="text-align:center;">
				<td><input type="checkbox" name="rank_id[]" <?php if($value['rank_id'] < 5){echo "disabled value='' ";} else {echo ' value="'.$value['rank_id'].'" ';} ?> /></td>
				<td><?php echo $value['rank_id'];?></td>
				<td align="left"><?php echo $value['rank_name'];?></td>
				<td align="left"><?php echo $value['pri'];?></td>
				<td class="center">
					<a href="m.php?app=member_rank_edit&id=<?php echo $value['rank_id'];?>"><?php echo $a_langpackage->a_update_privilege?></a>
					<?php if ($value['rank_id'] > 4){ ?>
						<a href="a.php?act=member_rank_del&id=<?php echo $value['rank_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_rank_del_all_confirm?>');"><?php echo $a_langpackage->a_delete?></a>
					<?php }?>
				</td>
			</tr>
			<?php }?>
			<tr>
				<td colspan="5"><span class="button-container"><input class="regular-button" type="submit" name=""  onclick="return confirm('<?php echo $a_langpackage->a_rank_del_all_confirm?>');" value="<?php echo $a_langpackage->a_batch_del?>" /></span></td>
			</tr>
			<?php } else { ?>
			<tr>
				<td colspan="7"></td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="7" class="center"><?php include("m/page.php"); ?></td>
			</tr>
		  </tbody>
		</table>
		</form>
	  </div>
	 </div>
   </div>
</div>
<script>
var inputs = document.getElementsByTagName("input");
function checkall(obj) {
	if(obj.checked) {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = true;
			}
		}
	} else {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = false;
			}
		}
	}
}
</script>
</body>
</html>