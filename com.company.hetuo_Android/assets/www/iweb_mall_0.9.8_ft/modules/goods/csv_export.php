<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/goods/csv_export.html
 * 如果您的模型要进行修改，请修改 models/modules/goods/csv_export.php
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
if(filemtime("templates/default/modules/goods/csv_export.html") > filemtime(__file__) || (file_exists("models/modules/goods/csv_export.php") && filemtime("models/modules/goods/csv_export.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/goods/csv_export.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
	/*
	***********************************************
	*$ID:
	*$NAME:
	*$AUTHOR:E.T.Wei
	*DATE:Wed Mar 24 08:57:03 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	
	//文件引入
	include_once("foundation/asystem_info.php");
	//引入语言包
	$m_langpackage=new moduleslp;
	//数据表定义区
	$g_tables = $tablePreStr."goods";
	//读写分类定义方法
	$dbo=new dbex;
	dbtarget('r',$dbServs);	
	//取得当前用户的信息
	$shop_id = get_sess_shop_id();
	$sql = "SELECT goods_id,goods_name FROM $g_tables WHERE shop_id='$shop_id' ORDER BY goods_id DESC";
	$goods_list = $dbo->getRs($sql);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
th{background:#EFEFEF}
.red { color:red; }
.edit span{background:#efefef;}
.search {margin:5px;}
.search input {color:#444;}
.clear {clear:both;}

#bgdiv { background-color:#333; position:absolute; left:0px; top:0px; opacity:0.4; filter:alpha(opacity=40); width:100%; height:1000px; z-index:960}
#category_select { width:800px; z-index:961; position:absolute; filter:alpha(opacity=95); left:100px; top:160px; background-color:#fff; height:270px}
.category_title_1 {background:#F6A248; color:#fff; padding-left:10px; line-height:25px; font-weight:bold; font-size:14px;}
.category_title_1 span {float:right; padding-right:5px; cursor:pointer;}
.ulselect {width:198px; height:210px; overflow-x:hidden; overflow-y:scroll; border:1px solid #efefef; float:left;}
.ulselect li {line-height:21px; padding-left:5px; cursor:pointer; }
.ulselect li:hover {background:#F6A248; color:#fff;}
.ulselect li.select {background:#F6A248; color:#fff;}
.category_com {height:30px; line-height:30px; text-align:center;}
.attr_class { background:#F8F8F8; }
.attr_class div.div {border:2px solid #fff; padding:3px;}
.attr_class div span.left{display:block; width:150px; float:left; margin-left:10px; text-align:right; _line-height:24px;}
.attr_class div span.right{display:block; width:350px; float:left; margin-left:5px; text-align:left;}
.attr_class div span.right input {margin-left:5px;}

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
				<div class="title_uc">
				<h3><?php echo $m_langpackage->m_csv_export;?></h3>
			</div>
			<form action="do.php?act=csv_export" method="POST" id="cvsform" name='cvsform' onsubmit="return checkform(this)">
				<input type="hidden" name="shop_id" value="<?php echo $shop_id;?>" />
					<table class="form_table">
						<tr>
							<td width="135"><?php echo $m_langpackage->m_csv_name;?></td><td><input type="text" name="filename" value="" /></td><td><?php echo $m_langpackage->m_no_csv_name;?></td>
						</tr>
						<tr>
							<td><?php echo $m_langpackage->m_no_chose_goods;?></td>
							<td align="left" colspan="2">
							<div style="height:155px; overflow-y:auto; overflow-x:hidden;">
							<?php foreach($goods_list as $value){?>
								<input type="checkbox" name="goods_id[]"  value="<?php echo $value['goods_id'];?>" checked /><?php echo $value['goods_name'];?><br />
							<?php }?>
							</div>
								<input type="checkbox" name="allgoods" checked value="1" onclick="chose()" /><?php echo $m_langpackage->m_all_goods;?>
							</td>
						</tr>
						<tr>
							<td><?php echo $m_langpackage->m_export_code;?></td>
							<td align="left" colspan="2">
								<select name="chast">
									<option value="utf-8" selected>UTF-8</option>
									<option value="gbk">GBK</option>
									<option value="gb2312">GB2312</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<input type="submit" name="submit" value="<?php echo $m_langpackage->m_csv_export;?>" />
							</td>
						</tr>
					</table>
			</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</body>
</html>
<script language="JavaScript" type="text/javascript">
	function chose(){
		var formobj = document.forms['cvsform'];
		var goodsobj = document.getElementsByName('goods_id[]');
		if(!formobj.allgoods.checked){
			formobj.allgoods.checked=false;
			for(i=0;i<goodsobj.length;i++){
				document.getElementsByName('goods_id[]')[i].checked=false;
			}
		}else{
			formobj.allgoods.checked=true;
			for(i=0;i<goodsobj.length;i++){
				document.getElementsByName('goods_id[]')[i].checked=true;
			}
		}
	}
	function checkform(obj){
		var goodsobj = document.getElementsByName('goods_id[]');
		var checkednum=0;
		for(i=0;i<goodsobj.length;i++){
			if(document.getElementsByName('goods_id[]')[i].checked){
				checkednum+=1;
			}
		}
		if(obj.filename.value.length<1){
			alert("<?php echo $m_langpackage->m_no_csv_name;?>");
			return false;
		}
		if(checkednum<1){
			alert("<?php echo $m_langpackage->m_no_chose_goods;?>");
			return false;
		}
	}
</script><?php } ?>