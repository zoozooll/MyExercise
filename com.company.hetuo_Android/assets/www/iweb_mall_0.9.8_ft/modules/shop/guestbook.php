<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/guestbook.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/guestbook.php
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
if(filemtime("templates/default/modules/shop/guestbook.html") > filemtime(__file__) || (file_exists("models/modules/shop/guestbook.php") && filemtime("models/modules/shop/guestbook.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/guestbook.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_guestbook = $tablePreStr."shop_guestbook";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_shop_guestbook` where shop_id='$shop_id' and shop_del_status=1";

$sql .= " order by add_time desc";

$result = $dbo->fetch_page($sql,13);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
th{background:#EFEFEF}
.edit span{background:#efefef;}
.search {margin:5px;}
.search input {color:#444;}
td.img img{cursor:pointer}
</style>
<script>
var inputs = document.getElementsByTagName("input");
function submitform() {
	var status = document.getElementsByName("guest");
	var checknum = 0;
	for(var i=0; i<inputs.length; i++) {
		if(inputs[i].type=='checkbox') {
			if(inputs[i].checked) {
				checknum++;
			}
		}
	}
	if(checknum==0) {
		alert("<?php echo $m_langpackage->m_selceted_one;?>");
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
</script>
</head>
<body>
<form action="do.php?act=shop_guestbook_del" name="form" method="post" onsubmit="return submitform();">
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
		<div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_rc_guestbook;?></h3></div>
			<table width="98%" class="form_table">
				<tr class="center"><th width="3%"><input type="checkbox" onclick="checkall(this);" /></th>
					<th width="10%"><?php echo  $m_langpackage->m_name;?></th><th width="10%"><?php echo  $m_langpackage->m_email;?></th>
					<th width="10%"><?php echo  $m_langpackage->m_other_contact;?></th><th width="15%"><?php echo  $m_langpackage->m_guestbook_content;?></th>
					<th width="15%"><?php echo  $m_langpackage->m_add_time;?></th><th width="20%"><?php echo  $m_langpackage->m_my_write_back;?></th>
					<th width="5%"><?php echo  $m_langpackage->m_status;?></th>
					<th width="10%"><?php echo  $m_langpackage->m_manage;?></th></tr>
				<?php 
				if(!empty($result['result'])) {
					foreach($result['result'] as $v) {?>
					<?php if($v['group_id']) {?>
						<tr>
							<td><input type="checkbox" name="guest[]" value="<?php echo  $v['gid'];?>" /></td>
							<td class="center">[<?php echo  $m_langpackage->m_group_buy;?>:<a href="goods.php?id=<?php echo $v['goods_id'];?>&app=groupbuyinfo&groupid=<?php echo $v['group_id'];?>"><?php echo  $v['group_name'];?></a>]<br /><?php echo  $v['name'];?></td>
							<td class="center"><?php echo  $v['email'];?></td>
							<td class="center"><?php echo  $v['contact'];?></td>
							<td class="center"><?php echo  $v['content'];?></td>
							<td class="center"><?php echo  substr($v['add_time'],0,16);?></td>
							<td class="center"><?php echo  $v['reply'];?></td>
							<td class="center">
								<?php if($v['read_status']) {?>
									<?php echo  $m_langpackage->m_read;?>
								<?php  } else {?>
									<?php echo  $m_langpackage->m_unread;?>
								<?php }?>
							</td>
							<td class="center"><a href="do.php?act=shop_guestbook_del&id=<?php echo  $v['gid'];?>"
								onclick="return confirm('<?php echo  $m_langpackage->m_suredel_guestbook;?>');"><?php echo  $m_langpackage->m_del;?></a>
								<a href="modules.php?app=shop_seller_r&id=<?php echo  $v['gid'];?>" ><?php echo  $m_langpackage->m_write_back;?></a><br />
								<?php if($v['read_status']) {?>
									<a href="do.php?act=edit_guestbook&id=<?php echo  $v['gid'];?>&sta=0" onclick="javascript:if(confirm('<?php echo  $m_langpackage->m_confirm;?>')){return true;} else {return false;}" ><?php echo  $m_langpackage->m_mark_unread;?></a>
								<?php  } else {?>
									<a href="do.php?act=edit_guestbook&id=<?php echo  $v['gid'];?>&sta=1" onclick="javascript:if(confirm('<?php echo  $m_langpackage->m_confirm;?>')){return true;} else {return false;}" ><?php echo  $m_langpackage->m_mark_read;?></a>
								<?php }?>
							</td>
						</tr>
					<?php  } else {?>
						<tr>
							<td><input type="checkbox" name="guest[]" value="<?php echo  $v['gid'];?>" /></td>
							<td class="center"><?php echo  $v['name'];?></td>
							<td class="center"><?php echo  $v['email'];?></td>
							<td class="center"><?php echo  $v['contact'];?></td>
							<td class="center"><?php echo  $v['content'];?></td>
							<td class="center"><?php echo  substr($v['add_time'],0,16);?></td>
							<td class="center"><?php echo  $v['reply'];?></td>
							<td class="center">
								<?php if($v['read_status']) {?>
									<?php echo  $m_langpackage->m_read;?>
								<?php  } else {?>
									<?php echo  $m_langpackage->m_unread;?>
								<?php }?>
							</td>
							<td class="center"><a href="do.php?act=shop_guestbook_del&id=<?php echo  $v['gid'];?>"
								onclick="return confirm('<?php echo  $m_langpackage->m_suredel_guestbook;?>');"><?php echo  $m_langpackage->m_del;?></a>
								<a href="modules.php?app=shop_seller_r&id=<?php echo  $v['gid'];?>" ><?php echo  $m_langpackage->m_write_back;?></a><br />
								<?php if($v['read_status']) {?>
									<a href="do.php?act=edit_guestbook&id=<?php echo  $v['gid'];?>&sta=0" onclick="javascript:if(confirm('<?php echo  $m_langpackage->m_confirm;?>')){return true;} else {return false;}" ><?php echo  $m_langpackage->m_mark_unread;?></a>
								<?php  } else {?>
									<a href="do.php?act=edit_guestbook&id=<?php echo  $v['gid'];?>&sta=1" onclick="javascript:if(confirm('<?php echo  $m_langpackage->m_confirm;?>')){return true;} else {return false;}" ><?php echo  $m_langpackage->m_mark_read;?></a>
								<?php }?>
							</td>
						</tr>
				    <?php }?>
				<?php }?>
				<tr><td colspan="9"><INPUT onclick="return confirm('<?php echo $m_langpackage->m_manage_sure_del;?>');" type=submit value=<?php echo $m_langpackage->m_pl_del;?> name=deletesubmit> </td></tr>
				<tr><td colspan="9" class="center"><?php  require("modules/page.php");?></td></tr>
				<?php  } else {?>
				<tr><td colspan="9" class="center"><?php echo  $m_langpackage->m_nolist_record;?></td></tr>
				<?php }?>
			</table>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</form>
</body>
</html><?php } ?>