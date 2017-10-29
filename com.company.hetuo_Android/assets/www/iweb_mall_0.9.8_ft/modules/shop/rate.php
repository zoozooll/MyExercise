<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/rate.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/rate.php
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
if(filemtime("templates/default/modules/shop/rate.html") > filemtime(__file__) || (file_exists("models/modules/shop/rate.php") && filemtime("models/modules/shop/rate.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/rate.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_rate.php");
require_once("foundation/fstring.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_credit = $tablePreStr."credit";
$t_user = $tablePreStr."users";

//变量定义区
$t=short_check(get_args('t'));

$credit=array(
		"1"=>"好",
		"0"=>"中",
		"-1"=>"差",
	);

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

if($t=="seller"){
//来自卖家评价
	$sql="select a.*,b.goods_name,b.goods_price,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.buyer=$user_id and b.goods_id=a.goods_id and c.user_id=a.seller";
	$result=$dbo->fetch_page($sql,10);
	if(!empty($result['result'])){
		foreach($result['result'] as $key=>$val){
			if(empty($val['buyer_evaltime'])){
				unset($result['result'][$key]);
			}else{
				$result['result'][$key]['people']=$val['buyer'];
				$result['result'][$key]['credit']=$val['buyer_credit'];
				$result['result'][$key]['evaluate']=$val['buyer_evaluate'];
				$result['result'][$key]['evaltime']=$val['buyer_evaltime'];
				$result['result'][$key]['explanation']=$val['buyer_explanation'];
				$result['result'][$key]['exptime']=$val['buyer_exptime'];
			}
		}
	}
}elseif($t=="buyer"){
	$sql="select a.*,b.goods_name,b.goods_price,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where a.seller=$user_id and b.goods_id=a.goods_id and c.user_id=a.buyer";
	$result=$dbo->fetch_page($sql,10);
	if(!empty($result['result'])){
		foreach($result['result'] as $key=>$val){
			if(empty($val['seller_evaltime'])){
				unset($result['result'][$key]);
			}else{
				$result['result'][$key]['people']=$val['seller'];
				$result['result'][$key]['credit']=$val['seller_credit'];
				$result['result'][$key]['evaluate']=$val['seller_evaluate'];
				$result['result'][$key]['evaltime']=$val['seller_evaltime'];
				$result['result'][$key]['explanation']=$val['seller_explanation'];
				$result['result'][$key]['exptime']=$val['seller_exptime'];
			}
		}
	}
}elseif($t=="bymain"){
	$sql="select a.*,b.goods_name,b.goods_price,c.user_name from $t_credit as a,$t_goods as b,$t_user as c where (a.buyer=$user_id and b.goods_id=a.goods_id and c.user_id=a.seller) or (a.seller=$user_id and b.goods_id=a.goods_id and c.user_id=a.buyer)";
	$result=$dbo->fetch_page($sql,10);
	
	if(!empty($result['result'])){
		foreach($result['result'] as $key=>$val){
			if($val['seller']==$user_id){
				if(empty($val['buyer_evaltime'])){
					unset($result['result'][$key]);
				}else{
					$result['result'][$key]['people']=$val['buyer'];
					$result['result'][$key]['credit']=$val['buyer_credit'];
					$result['result'][$key]['evaluate']=$val['buyer_evaluate'];
					$result['result'][$key]['evaltime']=$val['buyer_evaltime'];
					$result['result'][$key]['explanation']=$val['buyer_explanation'];
					$result['result'][$key]['exptime']=$val['buyer_exptime'];
					$result['result'][$key]['exptime']='exptime';
				}
			}elseif($val['buyer']==$user_id){
				if(empty($val['seller_evaltime'])){
					unset($result['result'][$key]);
				}else{
					$result['result'][$key]['people']=$val['seller'];
					$result['result'][$key]['credit']=$val['seller_credit'];
					$result['result'][$key]['evaluate']=$val['seller_evaluate'];
					$result['result'][$key]['evaltime']=$val['seller_evaltime'];
					$result['result'][$key]['explanation']=$val['seller_explanation'];
					$result['result'][$key]['exptime']=$val['seller_exptime'];
					$result['result'][$key]['exptime']='exptime';
				}
			}
		}
	}
}
//echo $sql;
//print_r($result);
//print_r($result_seller);
//print_r($result_bymain);
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
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_shoprate_manage;?></h3></div>
		<form action="do.php?act=user_passwd" method="post" name="form_passwd" >
		<table width="98%" class="form_table">
			<tr class="center"><th><?php echo  $m_langpackage->m_evaluate;?></th>
				<th width="55%"><?php echo  $m_langpackage->m_comment;?></th>
				<th width="10%"><?php echo  $m_langpackage->m_commentators;?></th>
				<th width="25%"><?php echo  $m_langpackage->m_goods_info;?></th>
				<th><?php echo  $m_langpackage->m_manage;?></th>
			</tr>
			
			<?php  if(!empty($result['result'])) {
				foreach($result['result'] as $v) {?>
			<tr>
				<td class="center"><?php echo $credit[$v['credit']];?></td>
				<td><?php echo $v['evaluate'];?><?php if($v['exptime'] and $v['explanation']){?><br />[<?php echo  $m_langpackage->m_commentate;?>]<?php echo $v['explanation'];?><?php }?><br />[<?php echo $v['evaltime'];?>]</td>
				<td class="center"><?php echo $v['user_name'];?></td>
				<td><a href="<?php echo  goods_url($v['goods_id']);?>" target="_blank" style="font-size:12px; font-weight:bold; color:#0044DD" title="<?php echo $v['goods_name'];?>"><?php echo sub_str($v['goods_name'],50);?></a>
				<br /><?php echo  $m_langpackage->m_price;?>：￥<span style="color:#FF6600; font-weight:bold;"><?php echo  $v['goods_price'];?></span><?php echo  $m_langpackage->m_yuan;?></td>
				<td><?php if(!$v['exptime']){?><a href="modules.php?app=shop_rate_r&id=<?php echo  $v['cid'];?>&t=<?php echo $t;?>" ><?php echo  $m_langpackage->m_commentate;?></a><?php }?></td>
			</tr>
			<?php }?>
			<tr><td colspan="5" class="center"><?php require("modules/page.php");?></td></tr>
			<?php }else {?>
			<tr><td colspan="5" class="center"><?php echo  $m_langpackage->m_nolist_record;?></td></tr>
			<?php }?>
		</table>
		</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</body>
</html><?php } ?>