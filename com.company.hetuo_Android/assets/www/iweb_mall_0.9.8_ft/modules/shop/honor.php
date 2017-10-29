<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/honor.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/honor.php
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
if(filemtime("templates/default/modules/shop/honor.html") > filemtime(__file__) || (file_exists("models/modules/shop/honor.php") && filemtime("models/modules/shop/honor.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/honor.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_honor.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_honor = $tablePreStr."shop_honor";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$honor_list = get_honor_list($dbo,$t_shop_honor,$shop_id);

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<style type="text/css">
th{background:#EFEFEF}
.red { color:red; }
td div {float:left; text-align:center; border: 1px solid #DADADA; margin: 5px; padding:2px; width:130px;}
td span {display:block; margin-left:10px;}
.imga {display:block; margin-bottom:4px; height:130px;}
</style>
</head>
<body>
<div class="header"><?php  require("modules/header.php");?></div>
<div class="main">
	<div class="left_menu">
		<?php  require("modules/left_menu.php");?>
	</div>
	<div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_shop_honor;?></h3></div>
		<form action="do.php?act=shop_honor_update" method="post" name="form_shop_honor_update" enctype="multipart/form-data">
		<table width="98%" class="form_table">
			<tr><td class="center">
			<?php  if(!empty($honor_list)){
				foreach($honor_list as $value){?>
			<div id="honor_<?php echo  $value['honor_id'];?>">
				<a href="javascript:;" onclick="if (confirm('<?php echo  $m_langpackage->m_suredel_honor;?>')) dropImg('<?php echo  $value['honor_id'];?>')">[<?php echo  $m_langpackage->m_del;?>]</a>
				<br />
				<a href="<?php echo  $value['honor_original'];?>" target="_blank" class="imga"><img src="<?php echo  $value['honor_thumb'];?>" class="img" /></a>
				<input type="text" value="<?php echo  $value['honor_desc'];?>" size="15" name="old_img_desc[<?php echo  $value['honor_id'];?>]" maxlength="50" />
            </div>
			<?php  }}else {?>
				echo "<?php echo  $m_langpackage->m_not_uploadimg;?>";	
			<?php }?>
			</td></tr>
			<tr><td id="upload_img">
			<span><a href="javascript:addNewUploadSpan();">[+]</a> <?php echo  $m_langpackage->m_img_desc;?>：<input type="text" name="img_desc[]" maxlength="50" /> <?php echo  $m_langpackage->m_upload_file;?>：<input type="file" name="attach[]" /></span>
			</td></tr>
			<tr><td class="center"><input type="hidden" name="shop_id" value="<?php echo  $shop_id;?>" />
			<input type="submit" name="submit" value="<?php echo  $m_langpackage->m_update_image;?>" /></td></tr>
		</table>
		</form>
	</div>
	<div class="clear"></div>
</div>
<div class="footer"><?php  require("modules/footer.php");?></div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function addNewUploadSpan() {
	var upload_img = document.getElementById("upload_img");
	var newspan = document.createElement("span");
	newspan.innerHTML = '<a href="javascript:removeUploadSpan();">[-]</a> <?php echo  $m_langpackage->m_img_desc;?>：<input type="text" name="img_desc[]" maxlength="50" /> <?php echo  $m_langpackage->m_upload_file;?>：<input type="file" name="attach[]" />';
	upload_img.appendChild(newspan);
}

function removeUploadSpan() {
	var upload_img = document.getElementById("upload_img");
	var number = upload_img.children.length;
	var delnode = upload_img.children[number-1];
	upload_img.removeChild(delnode);
}

function dropImg(id) {
	ajax("do.php?act=shop_honor_drop","POST","id="+id+"&sid=<?php echo  $shop_id;?>",function(data){
		if(data==1) {
			document.getElementById("honor_"+id).style.display='none';
		} else {
			alert('<?php echo  $m_langpackage->m_delfail_tryagain;?>');
		}
	});
}
//-->
</script>
</body>
</html>
<?php } ?>