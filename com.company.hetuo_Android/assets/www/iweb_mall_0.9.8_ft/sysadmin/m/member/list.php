<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_users.php");

//引入语言包
$a_langpackage=new adminlp;

$name = short_check(get_args('name'));
$email = short_check(get_args('email'));

//数据表定义区
$t_users = $tablePreStr."users";
$t_user_rank = $tablePreStr."user_rank";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_users` where 1";
if($name) {
	//权限管理
	$right=check_rights("user_search");
	if(!$right){
		header('Location: m.php?app=error');
	}else{
		$sql .= " and user_name like '%$name%' ";
	}
}
if($email) {
	//权限管理
	$right=check_rights("user_search");
	if(!$right){
		header('Location: m.php?app=error');
	}else{
		$sql .= " and user_email like '%$email%' ";
	}
}

$sql .= " order by user_id desc";

$result = $dbo->fetch_page($sql,13);
//print_r($result['result']);
$userrank ="";
$userrank = get_userrank_list($dbo,$t_user_rank);

$right_array=array(
	"user_search"    =>   "0",
    "user_update"    =>   "0",
    "user_lock"    =>   "0",
    "user_unlock"    =>   "0",
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_member_oprate;?> &gt;&gt; <?php echo $a_langpackage->a_memeber_list; ?></div>
    <hr />
	<div class="seachbox">
        <div class="content2">
          <form action="m.php?app=member_list" name="searchForm" method="get">
            <table class="form-table">
            	<tbody>
            		<tr>
	                   	<td width="10px">
                        	<span style="margin:1px 0px 0px 0px; float:left; color: #000" >
                        		<img src="skin/images/icon_search.gif" border="0" alt="SEARCH" />
                        	</span>
                        </td>
	                    <td width="60px">
	                    		<?php echo $a_langpackage->a_memeber_name; ?>：
	                    </td>
            			<td width="60px">
            				<input type="text" class="small-text" name="name" value="<?php echo $name; ?>" style="width:100px" /></td>
            			<td width="60px"><?php echo $a_langpackage->a_memeber_email; ?>：</td>
            			<td width="60px"><input type="text" class="small-text" name="email" value="<?php echo $email; ?>" style="width:100px" /></td>
            			<td>
            				<input type="hidden" name="app" value="member_list" />
							<input type="submit" class="regular-button" value="<?php echo $a_langpackage->a_serach; ?>" />
            			</td>
                  	</tr>
                	</tbody>
            </table>
          </form>
        </div>
    </div>
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_memeber_list; ?></h3>
    <div class="content2">
		<form action="m.php?app=member_list" id="form1" method="get">
		<table class="list_table">
		   <thead>
			<tr style="text-align:center;">
				<th width="2x"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="160px" align="left"><?php echo $a_langpackage->a_memeber_name; ?>/<?php echo $a_langpackage->a_memeber_email; ?></th>
				<th width="275px"><?php echo $a_langpackage->a_register_time; ?> / <?php echo $a_langpackage->a_last_login_time; ?></th>
				<th width="110px"><?php echo $a_langpackage->a_last_login_IP; ?></th>
				<th width="276px"><?php echo $a_langpackage->a_email_check; ?>/<?php echo $a_langpackage->a_user_rank; ?></th>
				<th width="45px"><?php echo $a_langpackage->a_status; ?></th>
				<th width="80px"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style=" text-align:center;">
				<td><input type="checkbox" name="user_id[]" value="<?php echo $value['user_id'];?>" /></td>
				<td align="left"><?php echo $value['user_name'];?><br /><?php echo $value['user_email'];?></td>
				<td><?php echo $value['reg_time'];?><br /><?php echo $value['last_login_time'];?></td>
				<td><?php echo $value['last_ip'];?></td>
				<?php if($value['email_check']){?>
					<td class="green center"><?php echo $a_langpackage->a_verify; ?><br /><?php echo $userrank[$value['rank_id']]['rank_name'];?></td>
				<?php }else{?>
					<td class="red center"><?php echo $a_langpackage->a_unverify; ?><br /><?php echo $userrank[$value['rank_id']]['rank_name'];?></td>
				<?php }?>
				<td class="center"><?php if(@$value['locked']==1) { echo "<span style='color:red;'>".$a_langpackage->a_lock."</span>"; }else { echo "<span style='color:green;'>".$a_langpackage->a_normal."</span>"; } ?></td>
				<td class="center">
				<a href="m.php?app=member_reinfo&id=<?php echo $value['user_id'];?>"><?php echo $a_langpackage->a_update; ?></a><br />
				<?php if(@$value['locked']==1) { ?>
				<a href="a.php?act=member_unlock&id=<?php echo $value['user_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_sure_member_unlock; ?>');"><?php echo $a_langpackage->a_unlock; ?></a>
				<?php }else { ?>
				<a href="a.php?act=member_locked&id=<?php echo $value['user_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_sure_member_lock; ?>');"><?php echo $a_langpackage->a_lock; ?></a>
				<?php } ?></td>
			</tr>
			<?php }?>
			<tr>
				<td colspan="10">
					<span class="button-container"><input class="regular-button" type='button' onclick='jump_diff("a.php?act=member_locked");' name="" value="<?php echo $a_langpackage->a_batch_lock; ?>"  /></span>
					<span class="button-container"><input class="regular-button" type='button' onclick='jump_diff("a.php?act=member_unlock");' name="" value="<?php echo $a_langpackage->a_batch_unlock; ?>" /></span>
				</td>
			</tr>
			<?php } else { ?>
			<tr>
				<td colspan="10"></td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="10" class="center"><?php include("m/page.php"); ?></td>
			</tr>
			</tbody>
		</table>
		</form>
		</div>
	  </div>
   </div>
</div>
<script language="JavaScript">
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

function jump_diff(link_href){
	if(confirm('<?php echo $a_langpackage->a_shure_mess;?>')){
		document.getElementById('form1').action=link_href;
		document.getElementById('form1').method='post';
		document.getElementById('form1').submit();
	}
}
</script>
</body>
</html>