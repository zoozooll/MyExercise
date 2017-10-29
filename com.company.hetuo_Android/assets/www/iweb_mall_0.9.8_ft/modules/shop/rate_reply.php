<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/rate_reply.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/rate_reply.php
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
if(filemtime("templates/default/modules/shop/rate_reply.html") > filemtime(__file__) || (file_exists("models/modules/shop/rate_reply.php") && filemtime("models/modules/shop/rate_reply.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/rate_reply.html",1);
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
$t_goods = $tablePreStr."goods";
$t_credit = $tablePreStr."credit";
$t_user = $tablePreStr."users";

$cid=intval(get_args('id'));
$t=short_check(get_args('t'));

$credit=array(
		"1"=>"好",
		"0"=>"中",
		"-1"=>"差",
	);

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

if($t=="seller"){
//来自卖家评价
	$sql="select a.*,b.goods_name,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.cid=$cid and a.buyer=$user_id and b.goods_id=a.goods_id and c.user_id=a.seller";
	$result = $dbo->getRow($sql);
	$result['credit']=$result['buyer_credit'];
	$result['evaluate']=$result['buyer_evaluate'];
	$result['evaltime']=$result['buyer_evaltime'];
}elseif($t=="buyer"){
	$sql="select a.*,b.goods_name,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.cid=$cid and a.seller=$user_id and b.goods_id=a.goods_id and c.user_id=a.buyer";
	$result = $dbo->getRow($sql);
	$result['credit']=$result['seller_credit'];
	$result['evaluate']=$result['seller_evaluate'];
	$result['evaltime']=$result['seller_evaltime'];
}

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
		<div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_commentate;?></h3></div>
			<form action="do.php?act=shop_rate_reply_add&id=<?php echo $cid;?>&t=<?php echo $t;?>" name="form1" method="post" onsubmit="return check();">
				<table width="98%" class="form_table">
					<tr class="center">
						<th class="textright"><?php echo  $m_langpackage->m_evaluate;?></th>
						<td class="textleft"><?php echo $credit[$result['credit']];?></td>
					</tr>
					<tr class="center">
						<th class="textright"><?php echo  $m_langpackage->m_evaluate_con;?></th>
						<td class="textleft"><?php echo  $result['evaluate'];?></td>
					</tr>
					<tr class="center">
						<th class="textright"><?php echo  $m_langpackage->m_commentators;?></th>
						<td class="textleft"><?php echo  $result['user_name'];?></td>
					</tr>
					<tr class="center">
						<th class="textright"><?php echo  $m_langpackage->m_goods_info;?></th>
						<td class="textleft"><?php echo  $result['goods_name'];?></td>
					</tr>
					<tr class="center">
						<th class="textright"><?php echo  $m_langpackage->m_evaluate_time;?></th>
						<td class="textleft"><?php echo $result['evaltime'];?></td>
					</tr>
					<tr>
						<th class="textleft" colspan="2">
							<?php echo  $m_langpackage->m_my_commentate;?>
						</th>
					</tr>
					<tr>
						<td class="textright"><?php echo  $m_langpackage->m_commentate_con;?></td>
						<td class="textleft">
							<textarea rows="4" cols="50" name="reply"></textarea>
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
	var center = Trim(document.form1.reply.value);
	if(center == ""){
		alert("<?php echo  $m_langpackage->m_commentate_null;?>");
		document.form1.reply.value = center;
		return false;
	}
}
</script>
</body>
</html><?php } ?>