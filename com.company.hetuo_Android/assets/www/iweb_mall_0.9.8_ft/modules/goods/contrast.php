<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/goods/contrast.html
 * 如果您的模型要进行修改，请修改 models/modules/goods/contrast.php
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
if(filemtime("templates/default/modules/goods/contrast.html") > filemtime(__file__) || (file_exists("models/modules/goods/contrast.php") && filemtime("models/modules/goods/contrast.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/goods/contrast.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
	/*
	***********************************************
	*$ID:
	*$NAME:
	*$AUTHOR:E.T.Wei
	*DATE:Tue Mar 30 09:58:29 CST 2010
	***********************************************
	*/
	if(!$IWEB_SHOP_IN) {
		die('Hacking attempt');
	}
	//文件引入
	require("foundation/module_goods.php");
	//定义数据表
	$t_goods = $tablePreStr."goods";
	$t_brand= $tablePreStr."brand";
	//读写分类定义方法
	$dbo=new dbex;
	dbtarget('r',$dbServs);	
	//引入语言包
	$m_langpackage=new moduleslp;
	//操作
	$goods_ids = substr(short_check(get_args("contrast_goods_id")),0,-1);
	$sql=" SELECT goods_name,goods_thumb,goods_id,goods_price,brand_id,favpv FROM $t_goods WHERE goods_id IN ($goods_ids) ";
	$goods_list = $dbo->getRs($sql);
	foreach ($goods_list as $key=>$value){
		$row=$dbo->getRow("SELECT brand_name FROM $t_brand WHERE brand_id='{$value['brand_id']}'");
		$goods_list[$key]['brand_name']=$row['brand_name'];
	}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
th{background:#EFEFEF; text-align:center}
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
.apart {
	width:100%;
}
.attr_class { background:#F8F8F8; }
.attr_class div.div {border:2px solid #fff; padding:3px;}
.attr_class div span.left{display:block; width:150px; float:left; margin-left:10px; text-align:right; _line-height:24px;}
.attr_class div span.right{display:block; width:350px; float:left; margin-left:5px; text-align:left;}
.attr_class div span.right input {margin-left:5px;}

#picspan {width:82px; height:82px; padding:1px; border:1px solid #efefef; line-height:80px; text-align:center; display:inline-block; overflow:hidden; float:right;}
.form_table td {
	text-align:center;
}
.list_item_title td {
	
	background:#fcf2e7;
}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
		<div class="title_uc"><table align="center"><tr><td>商品对比</td></tr></table></div>
		<table align="center" width="100%" class="form_table" cellpadding="0.5" cellspacing="0" border="1" style="width:960px;">
			<tr class="list_item_title">
				<td align="center">商品名称</td>
				<td align="center">缩略图</td>
				<td align="center">商品价格</td>
				<td align="center">商品品牌</td>
				<td align="center">收藏人气</td>
				<td align="center">操作</td>
			</tr>
			<?php if(is_array($goods_list)){?>
			<?php foreach($goods_list as $value) {?>
			<tr class="">
				<td><?php echo $value['goods_name'];?></td>
				<td align="center"><img src="<?php echo $value['goods_thumb'];?>" /></td>
				<td align="center"><?php echo $value['goods_price'];?></td>
				<td align="center"><?php echo $value['brand_name'];?></td>
				<td align="center"><?php echo $value['favpv'];?></td>
				<td align="center"><a href="modules.php?app=user_order&gid=<?php echo $value['goods_id'];?>&v=1" >购买</a></td>
			</tr>
			<?php }?>
			<?php }?>
		</table> 
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</body>
</html><?php } ?>