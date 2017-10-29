<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/remind_info.html
 * 如果您的模型要进行修改，请修改 models/modules/user/remind_info.php
 *
 * 修改完成之后需要您进入后台重新编译，才会重新生成。
 * 如果您开启了debug模式运行，那么您可以省去上面这一步，但是debug模式每次都会判断程序是否更新，debug模式只适合开发调试。
 * 如果您正式运行此程序时，请切换到service模式运行！
 *
 * 如您有问题请到官方论坛（http://tech.jooyea.com/bbs/）提问，谢谢您的支持。
 */
?><?php
/*
 * 此段代码由debug模式下生成运行，请勿改动！
 * 如果debug模式下出错不能再次自动编译时，请进入后台手动编译！
 */
/* debug模式运行生成代码 开始 */
if(!function_exists("tpl_engine")) {
	require("foundation/ftpl_compile.php");
}
if(filemtime("templates/default/modules/user/remind_info.html") > filemtime(__file__) || (file_exists("models/modules/user/remind_info.php") && filemtime("models/modules/user/remind_info.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/remind_info.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//require("foundation/module_remind.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_remind_info = $tablePreStr."remind_info";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$sql="select * from $t_remind_info where user_id=$user_id order by isread";
$remind_rs=$dbo->getRs($sql);

//print_r($remind_rs);

$type=array(
	"0"=>"未读",
	"1"=>"已读",
);

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">

</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
		<div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_my_remind;?></h3></div>
		<form action="do.php?act=user_remind_info" method="post" name="form_profile" onsubmit="return submitform();">
		<table width="98%" class="form_table">
			<tr class="center">
				<th><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th><?php echo $m_langpackage->m_rem_con;?></th>
				<th><?php echo $m_langpackage->m_time_send;?></th>
				<th><?php echo $m_langpackage->m_rem_fettle;?></th>
				<th><?php echo $m_langpackage->m_manage;?></th>
			</tr>
		<?php foreach($remind_rs as $val){?>
			<tr class="center">
				<td><input type="checkbox" name="searchkey[]" value=<?php echo $val['rinfo_id'];?> /></td>
				<td class="left"><?php echo $val['remind_info'];?></td>
				<td><?php echo $val['remind_time'];?></td>
				<td><?php if($val['isread']==0){?><span style='color:red;'> <?php }?><?php echo $type[$val['isread']];?></td>
				<td><?php if($val['isread']==0){?><a href="do.php?act=user_remind_info&searchkey=<?php echo $val['rinfo_id'];?>&updsubmit=updsubmit"><?php echo $m_langpackage->m_mark_read;?></a> | <?php }?><a href="do.php?act=user_remind_info&searchkey=<?php echo $val['rinfo_id'];?>&deletesubmit=deletesubmit" onclick="return confirm('<?php echo $m_langpackage->m_confirm;?>');"><?php echo $m_langpackage->m_del;?></a></td>
			</tr>
		<?php }?>
		<tr><td colspan="5"><INPUT onclick="return confirm('<?php echo $m_langpackage->m_confirm;?>');" type=submit value="<?php echo $m_langpackage->m_mark_read;?>" name=updsubmit> <INPUT onclick="return confirm('<?php echo $m_langpackage->m_confirm;?>');" type=submit value="<?php echo $m_langpackage->m_del;?>" name=deletesubmit> </td></tr>
		</table>
		</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>

<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var inputs = document.getElementsByTagName("input");
function submitform() {
	var checknum = 0;
	for(var i=0; i<inputs.length; i++) {
		if(inputs[i].type=='checkbox') {
			if(inputs[i].checked && inputs[i].value) {
				checknum++;
			}
		}
	}
	if(checknum==0) {
		alert("<?php echo $m_langpackage->m_choice;?>");
		return false;
	}
	return true;
}
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
//-->
</script>
</body>
</html><?php } ?>