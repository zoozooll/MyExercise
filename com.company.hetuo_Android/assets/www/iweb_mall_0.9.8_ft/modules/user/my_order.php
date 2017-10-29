<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/my_order.html
 * 如果您的模型要进行修改，请修改 models/modules/user/my_order.php
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
if(filemtime("templates/default/modules/user/my_order.html") > filemtime(__file__) || (file_exists("models/modules/user/my_order.php") && filemtime("models/modules/user/my_order.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/my_order.html",1);
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
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
$user_id = get_sess_user_id();
$sql = "select a.order_id,a.pay_time,a.get_back_time,a.payid,a.shop_id,a.goods_id,a.goods_name,a.order_amount,a.order_time,a.order_status,a.pay_status,a.transport_status,a.seller_reply,a.group_id,a.complaint,b.goods_thumb,c.user_id,c.shop_name from `$t_order_info` as a, `$t_goods` as b, `$t_shop_info` as c where a.goods_id=b.goods_id and a.shop_id=c.shop_id and a.user_id='$user_id' ";

$sql .= " order by order_time desc";
$result = $dbo->fetch_page($sql,13);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<style type="text/css">
.red{color:#fc0000;}
.green {color:green;}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
            <div class="title_uc"><h3><?php echo  $m_langpackage->m_my_order;?></h3></div>
            <table class="form_table">
				<thead>
	            	<tr>
	                	<td width="82"><?php echo  $m_langpackage->m_goods_image;?></td>
	                	<td><?php echo  $m_langpackage->m_order_goods_info;?></td>
	                    <td width="60"><?php echo  $m_langpackage->m_count;?></td>
	                    <td width="170"><?php echo  $m_langpackage->m_order_orderinfo;?></td>
	                    <td width="50"><?php echo  $m_langpackage->m_status;?></td>
	                    <td width="60"><?php echo  $m_langpackage->m_manage;?></td>
	                </tr>
	            </thead>
	            <tbody>
				<?php 
				if(!empty($result['result'])) {
					foreach($result['result'] as $v) {?>
				<tr>
					<td class="center"><a href="goods.php?id=<?php echo $v['goods_id'];?>" target="_blank"><img src="<?php echo $v['goods_thumb'];?>" width="80" height="80" /></a></td>
					<td class="textleft"><a href="goods.php?id=<?php echo $v['goods_id'];?>"
					 target="_blank" style="color:#0044DD;"><?php echo  $v['goods_name'];?></a><br/>
					<?php echo  $m_langpackage->m_order_shops;?>：<a href="shop.php?shopid=<?php echo  $v['shop_id'];?>&app=index" target="_blank"><?php echo  $v['shop_name'];?></a>  <script src="imshow.php?u=<?php echo  $v['user_id'];?>"></script>
					</td>
					<td class="center"><?php echo  $v['order_amount'];?></td>
					<td class="center">
						<?php echo  $m_langpackage->m_order_payids;?>：<a href="modules.php?app=user_order_view&order_id=<?php echo  $v['order_id'];?>"
						title="<?php echo  $m_langpackage->m_view_orderinfo;?>" style="color:#E38016;"><?php echo  $v['payid'];?></a> <?php  if(!empty($v['group_id'])) {?> <a href="goods.php?app=groupbuyinfo&id=<?php echo  $v['group_id'];?>" class="green" target="_blank">[<?php echo  $m_langpackage->m_group_buy;?>]</a> <?php }?><br />
					<?php echo  $m_langpackage->m_order_ordertime;?>：<?php echo  substr($v['order_time'],0,16);?></td>
					<td class="center">
						<?php  if($v['order_status']==0){
							echo "<span class='black'>".$m_langpackage->m_order_cancel."</span><br />";
						} elseif($v['order_status']==3) {
							echo "<span class='green'>".$m_langpackage->m_order_combuy."</span><br />";
						} else {
							if($v['order_status']==1) {
								echo "<span class='red'>".$m_langpackage->m_order_nosure."</span><br /> ";
							} else {
								echo "<span class='green'>".$m_langpackage->m_order_sure."</span><br /> ";
							}
							if($v['pay_status']) {
								echo "<span class='green'>".$m_langpackage->m_order_payed."</span><br /> ";
							} else {
								echo "<span class='red'>".$m_langpackage->m_order_nopayed."</span><br /> ";
							}
							if($v['transport_status']) {
								echo "<span class='green'>".$m_langpackage->m_order_transported."</span><br /> ";
							} else {
								echo "<span class='red'>".$m_langpackage->m_order_notransported."</span> <br />";
							}
							if($v['get_back_time']&&($v['order_status']>0&&$v['order_status']<3)) {
								echo "<span class='red'>申请退款</span> ";
							}
					}?>
					</td>
					<td class="center">
						<?php if($v['order_status']==1) {?><a href="do.php?act=user_order_del&id=<?php echo  $v['order_id'];?>"
							onclick="return confirm('<?php echo  $m_langpackage->m_sure_cancelthisorder;?>');"><?php echo  $m_langpackage->m_cancelorder;?></a><br /><?php }?>
						<?php  if($v['pay_status']==0 && $v['order_status']>=1) {?><a href="modules.php?app=user_payment_message&id=<?php echo  $v['order_id'];?>">
							<?php echo  $m_langpackage->m_pay;?></a><br /><?php }?>
						<?php   if($v['transport_status']==1 && $v['order_status']<3 ) {?>
							<a href="do.php?act=user_order_checkget&id=<?php echo  $v['order_id'];?>"  onclick="return confirm('<?php echo  $m_langpackage->m_sure_thisgoodsreceive;?>');"><?php echo  $m_langpackage->m_sure_receive;?></a><br />
						<?php }?>
						<?php   if($v['transport_status']==1 && $v['order_status']==3 && $v['seller_reply']==0) {?>
							<a href="modules.php?app=shop_credit_add&id=<?php echo  $v['order_id'];?>&t=buyer" ><?php echo  $m_langpackage->m_evaluate;?></a><br />
						<?php }else if($v['transport_status']==1 && $v['order_status']==3 && $v['seller_reply']==1){?>
						已评价<br />
						<?php }?>
						<a href="modules.php?app=user_order_view&order_id=<?php echo  $v['order_id'];?>" title="<?php echo  $m_langpackage->m_view_orderinfo;?>"><?php echo  $m_langpackage->m_view_orderinfo2;?></a><br />
						<?php   if($v['complaint']==0) {?>
						<a href="modules.php?app=user_complaint&order_id=<?php echo  $v['order_id'];?>" ><?php echo  $m_langpackage->m_complaints;?></a><br />
						<?php }else{?>
						已投诉<br />
						<?php }?>
						<?php if(empty($v['get_back_time'])&&$v['pay_status']==1&&$v['order_status']>0&&$v['order_status']<3&&time()>(strtotime($v['pay_time'])+24*3600)){?>
							<a href="do.php?act=get_back_money&order_id=<?php echo  $v['order_id'];?>" ><?php echo  $m_langpackage->m_refund;?></a>
						<?php }?>
					</td>
				</tr>
					<?php }?>
				<tr><td colspan="6" class="center"><?php  require("modules/page.php");?></td></tr>
				<?php } else {?>
				<tr><td colspan="6" class="center"><?php echo  $m_langpackage->m_nolist_record;?></td></tr>
				<?php }?>
				</tbody>
			</table>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</div>
</body>
</html>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function toggle_show(obj,id) {
	var re = /yes/i;
	var src = obj.src;
	var isshow = 1;
	var sss = src.search(re);
	if(sss > 0) {
		isshow = 0;
	}
	ajax("do.php?act=shop_isshow_toggle","POST","id="+id+"&s="+isshow,function(data){
		if(data) {
			obj.src = 'skin/default/images/'+data+'.gif';
		}
	});
}
//-->
</script><?php } ?>