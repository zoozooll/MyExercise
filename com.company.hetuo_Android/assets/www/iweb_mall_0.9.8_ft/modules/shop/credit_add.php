<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/credit_add.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/credit_add.php
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
if(filemtime("templates/default/modules/shop/credit_add.html") > filemtime(__file__) || (file_exists("models/modules/shop/credit_add.php") && filemtime("models/modules/shop/credit_add.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/credit_add.html",1);
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
$t_order_info = $tablePreStr."order_info";
$t_users = $tablePreStr."users";

$oid=intval(get_args('id'));
$t=short_check(get_args('t'));

$credit=array(
		"1"=>"好",
		"0"=>"中",
		"-1"=>"差",
	);

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

//if($t=="seller"){
	$sql="select a.goods_name,a.goods_id,b.user_name,b.user_id from $t_order_info as a,$t_users as b where a.order_id=$oid and b.user_id=a.user_id";
//}elseif($t=="buyer"){
//	$sql="select a.goods_name,b.user_name from $t_order_info as a,$t_users as b where a.order_id=$oid and a.user_id=b.user_id";
//}

//echo $sql;
$result = $dbo->getRow($sql);

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

</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
		<div class="bigapart">
			<div class="title_uc"><h3><?php echo  $m_langpackage->m_evaluate;?></h3></div>
				<form action="do.php?act=shop_credit_add&id=<?php echo $oid;?>&t=<?php echo $t;?>" name="form1" method="post" onsubmit="return check();">
				<table width="98%" class="form_table">
					<tr class="center">
						<th class="textright"><?php echo  $m_langpackage->m_appraiser;?></th>
						<td class="textleft"><?php echo  $result['user_name'];?></td>
					</tr>
					<tr class="center">
						<th class="textright"><?php echo  $m_langpackage->m_goods_info;?></th>
						<td class="textleft"><a href="goods.php?id=<?php echo $result['goods_id'];?>" target="_blank" style="color:#0044DD;"><?php echo  $result['goods_name'];?></a></td>
					</tr>
					<tr>
						<th class="textleft" colspan="2">
							<?php echo  $m_langpackage->m_my_appraise;?>
						</th>
					</tr>
					<tr>
						<td class="textright"><?php echo  $m_langpackage->m_appraise_grade;?></td>
						<td class="textleft">
							<?php foreach($credit as $key=>$val){?>
							&nbsp;&nbsp;&nbsp;<input type="radio" name="grade" value="<?php echo $key;?>"><?php echo $val;?>
							<?php }?>
						</td>
					</tr>
					<tr>
						<td class="textright"><?php echo  $m_langpackage->m_evaluate_con;?></td>
						<td class="textleft">
							<textarea rows="4" cols="50" name="content"></textarea>
						</td>
					</tr>
					<tr>
						<td class="center" colspan="2">
							<input type="submit" value="<?php echo  $m_langpackage->m_send;?>"/>
						</td>
					</tr>
				</table>
			</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
<script language="JavaScript">
function Trim(center) {
	return center.replace(/\s+$|^\s+/g,"");
}
function check(){
	var center = Trim(document.form1.content.value);
	if(center == ""){
		alert("<?php echo  $m_langpackage->m_commentate_null;?>");
		document.form1.content.value = center;
		return false;
	}
}
</script>
</body>
</html><?php } ?>