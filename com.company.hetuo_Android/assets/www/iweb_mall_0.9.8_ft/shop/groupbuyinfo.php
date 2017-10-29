<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/groupbuyinfo.html
 * 如果您的模型要进行修改，请修改 models/shop/groupbuyinfo.php
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
if(filemtime("templates/default/shop/groupbuyinfo.html") > filemtime(__file__) || (file_exists("models/shop/groupbuyinfo.php") && filemtime("models/shop/groupbuyinfo.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/groupbuyinfo.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_goods.php");
require("foundation/module_users.php");
require("foundation/module_areas.php");
require("foundation/module_credit.php");
require("foundation/flefttime.php");
//引入语言包
$s_langpackage=new shoplp;
$i_langpackage=new indexlp;
/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_goods_gallery = $tablePreStr."goods_gallery";
$t_areas = $tablePreStr."areas";
$t_goods_attr = $tablePreStr."goods_attr";
$t_credit = $tablePreStr."credit";
$t_integral = $tablePreStr."integral";
$t_attribute = $tablePreStr."attribute";
$t_shop_payment = $tablePreStr."shop_payment";
$t_payment = $tablePreStr."payment";
$t_shop_groupbuy = $tablePreStr."groupbuy";
$t_shop_groupbuy_log = $tablePreStr."groupbuy_log";
$t_shop_guestbook = $tablePreStr."shop_guestbook";

$group_id = intval($_GET['id']);
/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();
$sql = "select goods_id from `$t_shop_groupbuy` where group_id = $group_id";
$groupbuyinfo = $dbo->getRow($sql);
if ($groupbuyinfo){
	$goods_id = $groupbuyinfo['goods_id'];
}else {
	exit('没有此团购!');
}
/* 数据库操作 */
dbtarget('w',$dbServs);
$dbo=new dbex();
$sql = "update $t_goods set pv=pv+1 where goods_id='$goods_id'";
$dbo->exeUpdate($sql);

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 产品信息获取 */
$sql = "select * from `$t_goods` where goods_id=$goods_id and is_on_sale=1";
$goodsinfo = $dbo->getRow($sql);
if($goodsinfo['lock_flg']) { exit("此商品已被锁定!"); }
if(!$goodsinfo) { exit("没有此商品!"); }

//获取商家信用值
$shop_id = $goodsinfo['shop_id'];
$credit=get_credit($dbo,$t_credit,$shop_id);
$credit['SUM(seller_credit)'] = intval($credit['SUM(seller_credit)']);
$integral=get_integral($dbo,$t_integral,$credit['SUM(seller_credit)']);

$sql = "SELECT * FROM $t_goods_gallery WHERE goods_id='$goods_id' order by is_set desc";
$gallery = $dbo->getRs($sql);

$sql = "SELECT * FROM $t_goods_attr WHERE goods_id='$goods_id'";
$goods_attr = $dbo->getRs($sql);
$attr = array();
$attr_ids = array();
$attr_status = false;
if($goods_attr) {
	foreach($goods_attr as $key=>$value) {
		$attr[$value['attr_id']] = $value['attr_values'];
		$attr_ids[] = $value['attr_id'];
	}
	$sql = "SELECT attr_id,attr_name FROM $t_attribute WHERE attr_id IN (".implode(',',$attr_ids).")";
	$attribute_result = $dbo->getRs($sql);
	$attribute = array();
	foreach($attribute_result as $value) {
		$attribute[$value['attr_id']] = $value['attr_name'];
	}
	$attr_status = true;
}

$areainfo = get_areas_kv($dbo,$t_areas);

/* 显示支付方式 */
$sql = "SELECT b.pay_id,b.pay_code FROM $t_shop_payment AS a, $t_payment AS b WHERE a.pay_id=b.pay_id AND a.shop_id=$shop_id AND a.enabled=1";
$result = $dbo->getRs($sql);
$payment_info = array();
if($result) {
	foreach($result as $value) {
		$temp = trim($value['pay_code'],' 0123456789');
		$payment_info[$temp] = $temp;
	}
}

/* 商铺信息处理 */
$shop_id = $goodsinfo['shop_id'];
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}

$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks[0];

$sql = "select goods_id,goods_name,goods_price,goods_thumb,is_set_image from `$t_goods` where is_best=1 and is_on_sale=1 order by sort_order asc,goods_id desc limit 9";
$best_goods = $dbo->getRs($sql);

set_hisgoods_cookie($goodsinfo['goods_id']);

$header['keywords'] = $goodsinfo['goods_name'].','.$goodsinfo['keyword'];
$header['description'] = sub_str(strip_tags($goodsinfo['goods_intro']),100);

$user_id = get_sess_user_id();
/* 团购信息处理 */
if ($user_id){
	$isset_logo = false;
	if ($user_id == $shop_id){
		$sql = "select g.*,t.* from $t_shop_groupbuy g left join $t_goods t on g.goods_id = t.goods_id where g.shop_id = $user_id and g.group_id = $group_id";
	}else {
		$sql = "select * from $t_shop_groupbuy_log where group_id = $group_id and user_id = $user_id";
		$groupbuy_oneinfo = $dbo->getRs($sql);
		if ($groupbuy_oneinfo){
			$isset_groupbuy = true;
			$sql = "select g.*,l.* from $t_shop_groupbuy g left join $t_shop_groupbuy_log l on g.group_id = l.group_id where l.group_id = $group_id and l.user_id = $user_id";


		}else {
			$isset_groupbuy = false;
			$sql = "select * from $t_shop_groupbuy g left join $t_goods t on g.goods_id = t.goods_id where g.group_id = $group_id";
		}
	}
}else {
	$isset_logo = true;
	$sql = "select * from $t_shop_groupbuy g left join $t_goods t on g.goods_id = t.goods_id where g.group_id = $group_id";
}
$groupbuyinfo = $dbo->getRow($sql);

$goods_p_id = $groupbuyinfo['goods_id'];
if ($groupbuyinfo['goods_id']){
	$goods_price = "select goods_price from `$t_goods` where goods_id ='$goods_p_id'";
	$goods_price = $dbo->getRow($goods_price);
	$groupbuyinfo['goods_price'] =$goods_price['goods_price'];
}
$header['title'] = $groupbuyinfo['group_name']." - ".$SHOP['shop_name'];
/* 时间处理 */
$now_time = new time_class();
$start_time = strtotime($groupbuyinfo['start_time']);
$now_time = $now_time -> time_stamp();
$end_time = strtotime($groupbuyinfo['end_time']);
if ($user_id){
	$sql = "select user_id,quantity,linkman,tel FROM `$t_shop_groupbuy_log` where user_id = $user_id";
	$isset_add_groupbuy = $dbo->getRow($sql);
}
/* 留言管理 */
$sql = "SELECT * FROM $t_shop_guestbook WHERE shop_id='$shop_id' order by add_time desc limit 10";
$guestbook_list = $dbo->getRs($sql);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo  $header['title'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="keywords" content="<?php echo  $header['keywords'];?>" />
<meta name="description" content="<?php echo  $header['description'];?>" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/magnifier.js"></script>
<style>
.detail .pro_detail .pro_text ul li.operate a.inquiry{width:89px;background:url(skin/<?php echo  $SYSINFO['templates'];?>/images/inquiry.gif) 0 0 no-repeat;;margin-left:10px;}
dl.guboo {text-align:left; padding:10px; border-bottom:1px solid #eeeeee;}
.ftcolor {color:#E78210;}
dt span {color:#CECFCE;}
.messagebox{text-align:left; padding:10px;}
</style>
</head>
<body>
<div id="wrapper">
	<?php  require("shop/header.php");?>
<div class="clear"></div>
<div class="path2"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <a href="<?php echo  shop_url($shop_id,'index');?>"><?php echo  $SHOP['shop_name'];?></a> > <?php echo  $groupbuyinfo['group_name'];?></div>
<div class="detail">
	<div class="pro_detail">
		<h1><?php echo  $groupbuyinfo['group_name'];?> (<?php echo $s_langpackage->s_groupbuy;?>)</h1>
		<div class="box">
            <div class="pro_pic" onmouseover="document.getElementById('show_bigpic').style.display='none';" onmouseout="document.getElementById('show_bigpic').style.display='block';"><a class="MagicZoom" id="zoom1" href="<?php echo  $gallery[0]['img_original'] ? $gallery[0]['img_original'] : "skin/default/images/nopic_big.gif";?>"><img src="<?php echo  $gallery[0]['img_url'] ? $gallery[0]['img_url'] : "skin/default/images/nopic_big.gif";?>"/></a><IMG class="MagicZoomLoading" alt="loading..." src="skin/<?php echo  $SYSINFO['templates'];?>/images/ajax-loader.gif"><a id="show_bigpic"><?php echo  $s_langpackage->s_click_viewbigimg;?></a></div>
            <div class="pic_box clear">
                <a class="left_button" href="javascript:void(0);" onclick="img_pre('list1_1');"></a>
                    <div id="thumbbox">
                        <div class="long_box" id="list1_1">
                        <?php foreach($gallery as $val){?>
							<a href="<?php echo $val['img_original'];?>" rev="<?php echo $val['img_url'];?>" rel="zoom1"><span><img src="<?php echo $val['thumb_url'];?>"></span></a>
                		<?php }?>
                        </div>
                    </div>
                <a class="right_button" href="javascript:void(0);" onclick="img_next('list1_1');"></a>
            </div>
        </div>
		<div class="pro_text">
			<ul>
				<?php  if($isset_logo){?>
					<?php  if($groupbuyinfo['recommended']){?>
							<li><span><?php echo $s_langpackage->s_groupbuy_time;?>:</span><label><?php echo  time_left(strtotime($groupbuyinfo['end_time']));?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_regiment_number;?>:</span><label><?php echo  $groupbuyinfo['min_quantity'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_restult_num;?>:</span><label><?php echo  $groupbuyinfo['purchase_num'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_old_price;?>:</span>￥<label><?php echo  $groupbuyinfo['goods_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_price;?>:</span>￥<label><?php echo  $groupbuyinfo['spec_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_completed;?></label></li>
							<li><span><a href="login.php" style=" color:#F00"><?php echo $s_langpackage->s_isset_login;?></a></label></li>
						<?php  } else{?>
							<li><span><?php echo $s_langpackage->s_groupbuy_time;?>:</span><label><?php echo  time_left(strtotime($groupbuyinfo['end_time']));?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_regiment_number;?>:</span><label><?php echo  $groupbuyinfo['min_quantity'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_restult_num;?>:</span><label><?php echo  $groupbuyinfo['purchase_num'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_old_price;?>:</span>￥<label><?php echo  $groupbuyinfo['goods_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_price;?>:</span>￥<label><?php echo  $groupbuyinfo['spec_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<?php  if($start_time < $now_time and $now_time < $end_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_ongoing;?></label></li>
							<?php  } else if($start_time > $now_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_not_published;?></label></li>
							<?php  } else if($end_time > $now_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_completed;?></label></li>
							<?php }?>
								<li><span><a href="login.php" style=" color:#F00"><?php echo $s_langpackage->s_isset_login;?></a></label></li>
						<?php }?>
				<?php  } else{?>
					<?php  if($user_id != $shop_id){?>
						<?php  if($groupbuyinfo['recommended']){?>
							<li><span><?php echo $s_langpackage->s_groupbuy_time;?>:</span><label><?php echo  time_left(strtotime($groupbuyinfo['end_time']));?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_regiment_number;?>:</span><label><?php echo  $groupbuyinfo['min_quantity'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_restult_num;?>:</span><label><?php echo  $groupbuyinfo['purchase_num'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_old_price;?>:</span>￥<label><?php echo  $groupbuyinfo['goods_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_price;?>:</span>￥<label><?php echo  $groupbuyinfo['spec_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_completed;?></label></li>
							<?php  if($isset_groupbuy){?>
								<li><a href="javascript:gotoOrder(<?php echo  $groupbuyinfo['group_id'];?>);"
									title="<?php echo $s_langpackage->s_detriment;?>"><?php echo $s_langpackage->s_detriment;?></a>
									</li>
							<?php }?>
						<?php  } else{?>
							<li><span><?php echo $s_langpackage->s_groupbuy_time;?>:</span><label><?php echo  time_left(strtotime($groupbuyinfo['end_time']));?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_regiment_number;?>:</span><label><?php echo  $groupbuyinfo['min_quantity'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_restult_num;?>:</span><label><?php echo  $groupbuyinfo['purchase_num'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_old_price;?>:</span>￥<label><?php echo  $groupbuyinfo['goods_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_price;?>:</span>￥<label><?php echo  $groupbuyinfo['spec_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<?php  if($start_time < $now_time and $now_time < $end_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_ongoing;?></label></li>
								<?php  if($isset_groupbuy){?>

									<li><span><?php echo $s_langpackage->s_groupbuy_buy_num;?>:</span><label><?php echo  $groupbuyinfo['quantity'];?></label></li>
									<li><span><?php echo $s_langpackage->s_groupbuy_real_name;?>:</span><label><?php echo  $groupbuyinfo['linkman'];?></label></li>
									<li><span><?php echo $s_langpackage->s_groupbuy_tel;?>:</span><label><?php echo  $groupbuyinfo['tel'];?></label></li>
									<?php  if($groupbuyinfo['recommended']){?>
										<li><a href="javascript:gotoOrder(<?php echo  $groupbuyinfo['group_id'];?>);"
										style=" background: url(skin/default/images/pj_bg.gif); width=180px; height=133px;"><?php echo  $s_langpackage->s_detriment;?></a>
										</li>
									<?php  } else{?>
										<li><a href="javascript:exitGroupbuy(<?php echo  $groupbuyinfo['group_id'];?>);"
										title="<?php echo $s_langpackage->s_groupbuy_del;?>" style=" background: url(skin/default/images/pj_bg.gif); width=180px; height=133px;"><?php echo $s_langpackage->s_groupbuy_del;?></a>
										</li>
									<?php }?>
								<?php  } else{?>
									<li><span><?php echo $s_langpackage->s_groupbuy_buy_num;?>:</span><input size="8" value="1" id='num' />(<?php echo $s_langpackage->s_required;?>)</li>
									<li><span><?php echo $s_langpackage->s_groupbuy_real_name;?>:</span><input size="8" value="" id='groupbuyname' />(<?php echo $s_langpackage->s_required;?>)</li>
									<li><span><?php echo $s_langpackage->s_groupbuy_tel;?>:</span><input size="8" value="" id='groupbuytel' />(<?php echo $s_langpackage->s_required;?>)</li>
									<li><a href="javascript:gotoGroupbuy(<?php echo  $groupbuyinfo['group_id'];?>);"
										title="<?php echo $s_langpackage->s_groupbuy_add;?>" style=" background: url(skin/default/images/pj_bg.gif); width=180px; height=133px;"><?php echo $s_langpackage->s_groupbuy_add;?></a>
									</li>
								<?php }?>
							<?php  } else if($start_time > $now_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_not_published;?></label></li>
							<?php  } else if($end_time > $now_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_completed;?></label></li>
									<?php  if($isset_groupbuy){?>
										<li><a href="javascript:gotoOrder(<?php echo  $groupbuyinfo['group_id'];?>);"
											 style=" background: url(skin/default/images/pj_bg.gif); width=180px; height=133px;"><?php echo  $s_langpackage->s_detriment;?></a>
											</li>
									<?php }?>
							<?php  } else if($end_time < $now_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_completed;?></label></li>
							<?php }?>
						<?php }?>
					<?php  } else{?>
						<?php  if($groupbuyinfo['recommended']){?>
							<li><span><?php echo $s_langpackage->s_groupbuy_time;?>:</span><label><?php echo  time_left(strtotime($groupbuyinfo['end_time']));?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_regiment_number;?>:</span><label><?php echo  $groupbuyinfo['min_quantity'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_restult_num;?>:</span><label><?php echo  $groupbuyinfo['purchase_num'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_old_price;?>:</span>￥<label><?php echo  $groupbuyinfo['goods_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_price;?>:</span>￥<label><?php echo  $groupbuyinfo['spec_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_completed;?></label></li>
						<?php  } else{?>
							<li><span><?php echo $s_langpackage->s_groupbuy_time;?>:</span><label><?php echo  time_left(strtotime($groupbuyinfo['end_time']));?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_regiment_number;?>:</span><label><?php echo  $groupbuyinfo['min_quantity'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_restult_num;?>:</span><label><?php echo  $groupbuyinfo['purchase_num'];?></label></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_old_price;?>:</span>￥<label><?php echo  $groupbuyinfo['goods_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<li><span><?php echo $s_langpackage->s_groupbuy_price;?>:</span>￥<label><?php echo  $groupbuyinfo['spec_price'];?></label> <?php echo  $s_langpackage->s_yuan;?></li>
							<?php  if($start_time < $now_time and $now_time < $end_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_ongoing;?></label></li>
							<?php  } else if($start_time > $now_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_not_published;?></label></li>
							<?php  } else if($end_time > $now_time){?>
								<li><span><?php echo $s_langpackage->s_group_buy_state;?>:</span><label><?php echo $s_langpackage->s_completed;?></label></li>
							<?php }?>
						<?php }?>
					<?php }?>
				<?php }?>
			</ul>
		</div>
	</div>
	<div class="shop_detail">
		<div class="shop_logo"><a href="<?php echo  shop_url($shop_id,'index');?>" title=""><img src="<?php echo  $SHOP['shop_logo'] ? $SHOP['shop_logo'] : 'skin/default/images/shop_nologo.gif';?>" width="198" height="98" alt="" /></a></div>
		<div class="shop_name"><a href="<?php echo  shop_url($shop_id,'index');?>"><?php echo  $SHOP['shop_name'];?></a></div>
		<ul>
			<li><?php echo $s_langpackage->s_nickname;?>： <?php echo  $ranks['user_name'];?></li>
			<li><?php echo  $s_langpackage->s_contact_seller;?>：<script src="imshow.php?u=<?php echo  $SHOP['user_id'];?>"></script></li>
			<li><?php echo $s_langpackage->s_goods_num;?>：<?php echo  $SHOP['goods_num'];?></li>
			<li><span style="float:left;"><?php echo  $s_langpackage->s_seller_c;?>：<a href="<?php echo  shop_url($shop_id,'credit');?>" hideFocus=true><?php echo $credit['SUM(seller_credit)'];?> </a>&nbsp;&nbsp;</span><span class="icon<?php echo $integral['int_grade'];?>" style="float:left;"></span><div style="clear:both;"></div></li>
			<li><?php echo $s_langpackage->s_new_login;?>：<?php echo  $ranks['last_login_time'];?></li>
			<li><?php echo $s_langpackage->s_creat_time;?>：<?php echo  $SHOP['shop_creat_time'];?></li>
			<li><span class="left"><?php echo $s_langpackage->s_certification;?>：</span>
			<?php  if($SHOP['rank_id']>2){?>
			<a href="javascript:;" title="<?php echo $i_langpackage->i_approve_company;?>"  class="shop_cert left"><?php echo $i_langpackage->i_approve_company;?></a>
			<?php  } else{?>
			<a href="javascript:;" title="<?php echo $i_langpackage->i_noapprove_company;?>"  class="shop_cert2 left"><?php echo $i_langpackage->i_noapprove_company;?></a>
			<?php }?></li>
		</ul>
		<a class="go2shop" href="<?php echo  shop_url($shop_id,'index');?>" title=""><img src="skin/<?php echo  $SYSINFO['templates'];?>/images/go2shop.gif" width="183" height="36" alt="<?php echo $s_langpackage->s_seller_shop;?>" /></a>
	</div>
</div>
<div class="detail_other">
	<ul class="tabbar"><li class="selected"><a href="javascript:void(0);" hideFocus=true><?php echo $s_langpackage->s_group_buy_introduction;?></a></li></ul>
	<div id="tab1_content1" class="detail_content">
		<?php echo  $groupbuyinfo['group_desc'];?>
	</div>
</div>
<div class="bigpart">
	<div class="guestbook top10">
		<div class="c_t">
		  <div class="c_t_l left"></div>
		  <div class="c_t_r right"></div>
		  <h2><a href="javascript:;"><?php echo $s_langpackage->s_buyer_comm;?></a><span>Buyers Comments</span></h2>
		</div>
		<div class="c_m">
                <?php foreach($guestbook_list as $value){?>
                    <?php if($value['group_id']){?>
                        <dl class="guboo">
                            <dt><label class="ftcolor"><?php echo  $value['name'];?>：&nbsp;&nbsp;</label><?php echo  $value['content'];?> &nbsp;&nbsp;<span><?php echo  $value['add_time'];?></span></dt>
                            <?php if($value['reply']) {?><dd style="width:600px; text-align:left;"><?php echo $s_langpackage->s_seller_reply;?>：<?php echo  $value['reply'];?></dd><?php }?>
                        </dl>
                    <?php }?>
                <?php }?>

			<div class="messagebox" >
            	<div>
                    <div id="sendbox" style="display:none;">
                        <form action="do.php?act=shop_guestbook" name="guestForm" method="post" id="guestForm" onSubmit="return checkForm();">
                        <input maxlength="20" name="name" type="hidden" value="<?php echo $USER['user_name'];?>" />
                        <input maxlength="50" name="email" type="hidden" value="<?php echo $USER['user_email'];?>" />
                        <input maxlength="50" name="group_id" type="hidden" value="<?php echo  $groupbuyinfo['group_id'];?>" />
                        <input maxlength="50" name="group_name" type="hidden" value="<?php echo  $groupbuyinfo['group_name'];?>" />
                        <input maxlength="50" name="goods_id" type="hidden" value="<?php echo  $groupbuyinfo['goods_id'];?>" />
                        <input maxlength="50" name="contact" type="hidden" />
                        <input type="hidden" name="shop_id" value="<?php echo  $shop_id;?>" />
                        <input type="hidden" name="shop_name" value="<?php echo  $SHOP['shop_name'];?>" />
                            <textarea cols="40" rows="4" name="content" id="textareac"></textarea><br/ >
                            <input class="button" type="submit" value="<?php echo $s_langpackage->s_post_comm;?>" />
                        </form>

                    </div>
                <div style="">
            		<input class="button" style="margin-left:20px;" type="button" value="<?php echo $s_langpackage->s_wantto_comm;?>" onclick="this.style.display='none';document.getElementById('sendbox').style.display=''" />
            	</div>
               </div>
            </div>
		</div>
		<div class="c_bt">
		  <div class="c_bt_l left"></div>
		  <div class="c_bt_r right"></div>
		</div>
	</div>
</div>
	<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
	<script language="JavaScript">
	<!--
		function gotoGroupbuy(id) {
			var num = document.getElementById('num').value;
			var groupbuyname = document.getElementById('groupbuyname').value;
			var groupbuytel = document.getElementById('groupbuytel').value;
			ajax("do.php?act=goods_add_groupbuy","POST","id="+id+"&num="+num+"&groupbuyname="+groupbuyname+"&groupbuytel="+groupbuytel,function(data){
				if (data == -1){
					alert('<?php echo $s_langpackage->s_groupbuy_isset_num;?>');
				}else if (data == -2){
					alert('<?php echo $s_langpackage->s_groupbuy_isset_name;?>');
				}else if (data == -3){
					alert('<?php echo $s_langpackage->s_groupbuy_isset_tel;?>');
				}else if (data == -4){
					alert('<?php echo $s_langpackage->s_groupbuy_isset_one;?>');
				}else if (data == 1){
					alert('<?php echo $s_langpackage->s_groupbuy_isset_true;?>');
					location.href = "<?php echo  $baseUrl;?>goods.php?id="+id+"&app=groupbuyinfo";
				}
			});
		}
		function exitGroupbuy(id) {
			ajax("do.php?act=goods_exit_groupbuy","POST","id="+id,function(data){
				if (data == 1){
					alert('<?php echo $s_langpackage->s_groupbuy_isset_false;?>');
					location.href = "<?php echo  $baseUrl;?>goods.php?id="+id+"&app=groupbuyinfo";
				}
			});
		}
		function gotoOrder(id) {
			location.href = "<?php echo  $baseUrl;?>modules.php?app=user_order_groupbuy&gid="+id;
		}
	//-->
	</script>
	<?php   require("shop/footer.php");?>
</div>
</body>
</html>
<?php } ?>