<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/favorite.html
 * 如果您的模型要进行修改，请修改 models/modules/user/favorite.php
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
if(filemtime("templates/default/modules/user/favorite.html") > filemtime(__file__) || (file_exists("models/modules/user/favorite.php") && filemtime("models/modules/user/favorite.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/favorite.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_user_favorite = $tablePreStr."user_favorite";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "SELECT c.user_id,a.goods_id,a.favorite_id,a.add_time,b.shop_id,b.goods_name,b.goods_thumb,b.goods_price,b.favpv,c.shop_name,c.shop_id FROM `$t_user_favorite` AS a, `$t_goods` AS b, `$t_shop_info` as c WHERE a.goods_id=b.goods_id AND b.shop_id=c.shop_id AND a.user_id='$user_id'";

$sql .= " order by a.add_time desc";

$result = $dbo->fetch_page($sql,10);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
.edit span{background:#efefef;}
.search {margin:5px;}
.search input {color:#444;}
td.img img{cursor:pointer}
</style>
<script>
var inputs = document.getElementsByTagName("input");
function submitform() {
	var status = document.getElementsByName("favorite");
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
<form action="do.php?act=user_favorite_del" name="form" method="post" onsubmit="return submitform();">
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><?php echo $m_langpackage->m_favorite;?></h3></div>
		<table width="98%" class="form_table">
			<tr class="center">
				<th width="20"><input type="checkbox" onclick="checkall(this);" /></th>
				<th width="90"><?php echo  $m_langpackage->m_goods_image;?></th>
				<th><?php echo  $m_langpackage->m_goods_info;?></th>
				<th width="30"><?php echo  $m_langpackage->m_manage;?></th></tr>
			<?php  
			if(!empty($result['result'])) {
				foreach($result['result'] as $v) {?>
			<tr>
				<td><input type="checkbox" name="favorite[]" value="<?php echo  $v['favorite_id'];?>" /></td>
				<td class="center"><a href="<?php echo  goods_url($v['goods_id']);?>" target="_blank"><img src="<?php echo  $v['goods_thumb'];?>" width="80" height="80"></a></td>
				<td class="textleft"><a href="<?php echo  goods_url($v['goods_id']);?>" target="_blank" style="font-size:14px; font-weight:bold; color:#0044DD"><?php echo  $v['goods_name'];?></a>
				<br /> <?php echo  $m_langpackage->m_order_shops;?>：<a href="<?php echo shop_url($v['shop_id']);?>" target="_blank" style="color:#0044DD;"><?php echo  $v['shop_name'];?></a> &nbsp;&nbsp; <?php echo  $m_langpackage->m_price;?>：￥<span style="color:#FF6600; font-weight:bold;"><?php echo  $v['goods_price'];?></span><?php echo  $m_langpackage->m_yuan;?>
				<br /> <?php echo  $m_langpackage->m_collect_num;?>：<span style="color:#FF6600; font-weight:bold;"><?php echo  $v['favpv'];?></span> &nbsp;&nbsp; <?php echo  $m_langpackage->m_add_time;?>：<?php echo  substr($v['add_time'],0,16);?> &nbsp;&nbsp; <script src="imshow.php?u=<?php echo  $v['user_id'];?>"></script>
				</td>
				<td class="center"><a href="do.php?act=user_favorite_del&id=<?php echo  $v['favorite_id'];?>" onclick="return confirm('<?php echo  $m_langpackage->m_suredel_favorite;?>');"><?php echo  $m_langpackage->m_del;?></a><br />
					<a href="modules.php?app=user_order&gid=<?php echo  $v['goods_id'];?>&v=1" style="color:#FF6600;"><?php echo  $m_langpackage->m_ccbuy;?></a>
				</td>
			</tr>
			<?php }?>
			<tr><td colspan="4" class="textleft"><INPUT onclick="return confirm('<?php echo $m_langpackage->m_manage_sure_del;?>');" type=submit value=<?php echo $m_langpackage->m_pl_del;?> name=deletesubmit> </td></tr>
			<tr><td colspan="4" class="center"><?php  require("modules/page.php");?></td></tr>
			<?php  } else {?>
			<tr><td colspan="4" class="center"><?php echo  $m_langpackage->m_nolist_record;?></td></tr>
			<?php }?>
		</table>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</form>
</body>
</html><?php } ?>