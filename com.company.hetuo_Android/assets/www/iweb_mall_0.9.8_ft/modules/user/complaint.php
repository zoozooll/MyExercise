<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/complaint.html
 * 如果您的模型要进行修改，请修改 models/modules/user/complaint.php
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
if(filemtime("templates/default/modules/user/complaint.html") > filemtime(__file__) || (file_exists("models/modules/user/complaint.php") && filemtime("models/modules/user/complaint.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/complaint.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_complaint.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_shop_info = $tablePreStr."shop_info";
$t_complaint_type = $tablePreStr."complaint_type";

//变量定义区
$order_id=intval(get_args('order_id'));

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$sql = "select a.order_id,a.payid,a.shop_id,a.goods_id,a.goods_name,a.order_amount,a.order_time,a.order_status,a.pay_status,a.transport_status,a.seller_reply,a.group_id,c.user_id,c.shop_name from `$t_order_info` as a, `$t_shop_info` as c where a.shop_id=c.shop_id and a.order_id='$order_id' ";

$order_rs=$dbo->getRow($sql);

$complaints_title=get_complaint_type_all($dbo,"*",$t_complaint_type);

//$complaints_title=array(
//	'1'=>'成交不卖',
//	'2'=>'收款不发货',
//	'3'=>'商品与描述不符',
//	'4'=>'评价纠纷',
//	'5'=>'卖家拒绝履行交易',
//	'6'=>'退款纠纷',
//	'7'=>'卖家要求买家先确认收货，卖家再发货',
//	'8'=>'诚保代充-未按时发货索赔',
//	'9'=>'卖家缺货无法交易',
//);

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
			<div class="title_uc"><h3><?php echo  $m_langpackage->m_complaints;?></h3></div>
			<form action="do.php?act=user_complaint_add" method="post" name="form_profile" onsubmit="return checkform();">
				<table width="98%">
					<tr><td class="textright"><?php echo  $m_langpackage->m_by_complainant;?>：</td><td class="textleft"><a href="shop.php?shopid=<?php echo  $order_rs['shop_id'];?>&app=index" target="_blank"><?php echo $order_rs['shop_name'];?></a><input type="hidden" name="shopid" value="<?php echo $order_rs['shop_id'];?>"><input type="hidden" name="shop_name" value="<?php echo $order_rs['shop_name'];?>"></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_related_products;?>：</td><td class="textleft"><a href="goods.php?id=<?php echo $order_rs['goods_id'];?>" target="_blank"><?php echo $order_rs['goods_name'];?></a><input type="hidden" name="goods_id" value="<?php echo $order_rs['goods_id'];?>"><input type="hidden" name="goods_name" value="<?php echo $order_rs['goods_name'];?>"></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_of_complaint;?>：</td>
					<td class="textleft"><select name="complaints_title" id="complaints_title" require="true" datatype="Require" msg="<?php echo  $m_langpackage->m_select_complaints_reason;?>"  onchange="ShowXieShang();PromptType(this.value);" >
		            	<option selected><?php echo  $m_langpackage->m_select_complaints_reason;?></option>
						<?php foreach($complaints_title as $val){?>
							<option value="<?php echo $val['type_content'];?>"><?php echo $val['type_content'];?></option>
						<?php }?>
		              	</select>
					  	<span id="ShowTypeMsg" style="padding-left:5px;"><?php echo  $m_langpackage->m_choose_trade_complaints;?></span></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_complaints_content;?>：</td><td class="textleft"><textarea name="complaints_content" cols="60" rows="10" require="true" datatype="Require" msg="<?php echo  $m_langpackage->m_please_enter_complaints;?>" class="inputmain" onblur="cutSize(this,500);countShow2(this.value,'count');" onkeyup="cutSize(this,500);countShow2(this.value,'count');"></textarea>
					<br />(<?php echo  $m_langpackage->m_current;?><span id="count">0</span><?php echo  $m_langpackage->m_upto_bytes;?>)<br /><span style="padding:10px; "><?php echo  $m_langpackage->m_real_evidence_dispute;?></span></td></tr>
					<tr><td colspan="2" align="center"><input type="submit" name="submit" value="<?php echo  $m_langpackage->m_send;?>" /></td></tr>
				</table>
				<input type="hidden" name="order_id" value="<?php echo $order_id;?>">
			</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function cutSize(obj,len)
{
	//obj.value = obj.value.replace(/(^[\s]*)|([\s]*$)/g, "");
	var str = obj.value;
	if(str.replace(/[^\x00-\xff]/g,'**').length <= len)
	{
		return;
	}
	str = str.substr(0,len);
	while(str.replace(/[^\x00-\xff]/g,'**').length > len)
	{
		str = str.substr(0,str.length -1);
	}
	obj.blur();
	obj.value = str;
	obj.focus();
}

function countShow2(str,idName)
{
	document.getElementById(idName).innerHTML=str.replace(/[^\x00-\xff]/g,'**').length;
}
//-->
</script>
</body>
</html><?php } ?>