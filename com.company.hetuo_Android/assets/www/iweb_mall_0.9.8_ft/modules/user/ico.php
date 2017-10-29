<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/ico.html
 * 如果您的模型要进行修改，请修改 models/modules/user/ico.php
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
if(filemtime("templates/default/modules/user/ico.html") > filemtime(__file__) || (file_exists("models/modules/user/ico.php") && filemtime("models/modules/user/ico.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/ico.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_users.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_users = $tablePreStr."users";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$user_info = get_user_info($dbo,$t_users,$user_id);

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
#picspan {width:82px; height:82px; padding:1px; border:1px solid #efefef; line-height:80px; text-align:center; display:inline-block; overflow:hidden; float:right;}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_ico_set;?></h3></div>
		<form action="do.php?act=user_ico" method="post" name="form_profile" enctype="multipart/form-data">
		<table width="98%" class="form_table">
			<tr><td class="textright"><span id="picspan"><?php if($user_info['user_ico']){?><img src="<?php echo $user_info['user_ico'];?>" width="80" height="80" /><?php  }else{?><?php echo $m_langpackage->m_user_ico;?><?php }?></span></td><td class="textleft"><input type="file" name="attach[]" onchange="showimg(this)" /><br /><?php echo $m_langpackage->m_img_limit;?></td></tr>
			<tr><td colspan="2" align="center"><input type="hidden" name="user_id" value="<?php echo  $user_id;?>" /><input type="submit" name="submit" value="<?php echo $m_langpackage->m_ico_set;?>" /></td></tr>
		</table>
		</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
<script language="JavaScript">
<!--
function showimg(obj) {
	var picspan = document.getElementById("picspan");
	picspan.innerHTML = '';
	var Img = new Image();
	Img.id = "goods_pic";

	if(navigator.userAgent.indexOf("MSIE")>0) {
		Img.src = obj.value;
	} else {
		Img.src = obj['files'][0].getAsDataURL();
	}
	picspan.appendChild(Img);
	//imgwh();
	setTimeout("imgwh()",100);
}

function imgwh() {
	var Img = document.getElementById("goods_pic");
	var w = Img.width;
	var h = Img.height
	if(w>h) {
		Img.height = h*80/w;
		Img.width = '80';
		Img.style.marginTop = (80-Img.height)/2+'px';
	} else {
		Img.width = w*80/h;
		Img.height = '80';
	}
}
//-->
</script>
</body>
</html><?php } ?>