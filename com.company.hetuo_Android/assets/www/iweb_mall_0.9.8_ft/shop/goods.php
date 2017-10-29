<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/goods.html
 * 如果您的模型要进行修改，请修改 models/shop/goods.php
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
if(filemtime("templates/default/shop/goods.html") > filemtime(__file__) || (file_exists("models/shop/goods.php") && filemtime("models/shop/goods.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/goods.html",1);
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

//引入语言包
$s_langpackage=new shoplp;
$i_langpackage=new indexlp;

/* 定义数据表 */
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
$t_areas = $tablePreStr."areas";
$t_transport_template = $tablePreStr."goods_transport";

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
//获得地区列表
$area_list = get_area_list_bytype($dbo,$t_areas,1);

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
$nav_selected =4;
$header['title'] = $goodsinfo['goods_name']." - ".$SHOP['shop_name'];
$header['keywords'] = $goodsinfo['goods_name'].','.$goodsinfo['keyword'];
$header['description'] = sub_str(strip_tags($goodsinfo['goods_intro']),100);
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
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/magnifier.js"></script>
<style>
.detail .pro_detail .pro_text ul li.operate a.inquiry{width:89px;background:url(skin/<?php echo  $SYSINFO['templates'];?>/images/inquiry.gif) 0 0 no-repeat;;margin-left:10px;}
</style>
</head>
<body onload="gettransport_price(0,'全国');" >
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<div class="clear"></div>
<div class="path2"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <a href="<?php echo  shop_url($shop_id,'index');?>"><?php echo  $SHOP['shop_name'];?></a> > <?php echo  $goodsinfo['goods_name'];?></div>
<div class="detail">
	<div class="pro_detail">
		<h1><?php echo  $goodsinfo['goods_name'];?></h1>
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
				<li><span><?php echo $s_langpackage->s_goods_price;?>：</span>
				<?php  if($goodsinfo['goods_price']=='0.00') {?>
				<label class="fc f18 bold"><?php echo  $s_langpackage->s_no_price;?></label>
				<?php  } else{?>
				￥<label class="fc f18 bold"><?php echo  $goodsinfo['goods_price'];?></label> <?php echo  $s_langpackage->s_yuan;?>
				<?php }?>
				</li>
				<li><span><?php echo $s_langpackage->s_goods_transport;?>：<span  onmouseover="showarealist(1)"  ><span id="area_name"></span>
					 <div style="position:absolute;style:top:0px;left:0px; border:3px #eee solid; padding:10px; padding-left:25px; background:#f8f8f8; display:none; width:300px;" id="areabox">
					 <?php foreach($area_list as $value){?>
					 <div style="float:left; width:60px;" ><a href="javascript:;" onclick="gettransport_price(<?php echo $value['area_id'];?>,'<?php echo $value['area_name'];?>')"><?php echo $value['area_name'];?></a></div>
					 <?php }?>
					  <input class="new_button" type="button" name="btu" value="关闭" onclick="showarealist(0)" />
					 </div>
				</span></span><label id="transport_price"></label> </li>
				<li><span><?php echo $s_langpackage->s_goods_wtbuy;?>：</span><input size="4" value="1" maxvalue="1" minvalue="1" id='num' /><label></label><?php echo  $s_langpackage->s_buy_n;?></li>
				<?php  if($goodsinfo['goods_price']=='0.00') {?>
				<li class="operate"><a class="inquiry" href="inquiry.php?gid=<?php echo  $goodsinfo['goods_id'];?>" 
					title="<?php echo  $s_langpackage->s_g_askprice;?>"></a>
				<?php  } else {?>
				<li class="operate"><a href="javascript:gotoOrder(<?php echo  $goodsinfo['goods_id'];?>);" 
					title="<?php echo  $s_langpackage->s_g_buy;?>"></a>
				<a class="cart" href="javascript:addCart(<?php echo  $goodsinfo['goods_id'];?>);" 
					title="<?php echo  $s_langpackage->s_g_tocart;?>"></a>
				<?php }?>
				<a class="fav" href="javascript:addFavorite(<?php echo  $goodsinfo['goods_id'];?>);" 
					title="<?php echo  $s_langpackage->s_g_tofavorite;?>"></a></li>
				<li><span><?php echo  $s_langpackage->s_company_Support;?>：</span><?php  if($payment_info) {?><?php foreach($payment_info as $val){?><img src="plugins/<?php echo $val;?>/min_logo.gif" height="25" /> <?php }?><?php  } else{?><label class='fc'><?php echo  $s_langpackage->s_no_payment;?></label><?php }?></li>
				<li><span><?php echo $s_langpackage->s_goods_number;?>：</span><?php echo str_replace("{num}","<label class='fc'>".$goodsinfo['goods_number']."</label>",$s_langpackage->s_goods_mum);?></li>
				<li><span><?php echo $s_langpackage->s_goods_pv;?>：</span><?php echo str_replace("{pv}","<label class='fc'>".$goodsinfo['pv']."</label>",$s_langpackage->s_goods_pvnum);?></li>
				<li><span><?php echo $s_langpackage->s_collect_num;?>：</span><label class='fc'><?php echo  $goodsinfo['favpv'];?></label><?php echo  $s_langpackage->s_collect;?></li>
				<li><span><?php echo $s_langpackage->s_send_address;?>：</span><?php echo  $areainfo[$SHOP['shop_province']];?>.<?php echo  $areainfo[$SHOP['shop_city']];?></li>
			</ul>
		</div>
	</div>
	<div class="shop_detail">
		<div class="shop_logo"><a href="<?php echo  shop_url($shop_id,'index');?>" title=""><img src="<?php echo  $SHOP['shop_logo'] ? $SHOP['shop_logo'] : 'skin/default/images/shop_nologo.gif';?>" width="198" height="98" alt="" /></a></div>
		<div class="shop_name"><a href="<?php echo  shop_url($shop_id,'index');?>"><?php echo  $SHOP['shop_name'];?></a></div>
		<ul>
			<li><?php echo $s_langpackage->s_nickname;?>： <?php echo  $ranks['user_name'];?></li>
			<?php if($im_enable==true){?><li><?php echo  $s_langpackage->s_contact_seller;?>：<script src="imshow.php?u=<?php echo  $SHOP['user_id'];?>"></script></li><?php }?>
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
	<div class="clear"></div>
    <div class="top"><div class="line"></div> <span class="right top10 right10"><a class="highlight" href="brand.php"><?php echo  $i_langpackage->i_moreshop;?>>></a></span><ul id="tab1"><li id="tab1_title0" class="selected" onclick="nTabs('tab1',this);"><a href="javascript:void(0);" hideFocus=true><?php echo $s_langpackage->s_details;?></a></li><li id="tab1_title1" onclick="nTabs('tab1',this);"><a href="javascript:void(0);" hideFocus=true><?php echo $s_langpackage->s_wholesale;?></a></li></ul></div>

	<div id="tab1_content0" class="detail_content">
		<table cellspacing="0">
			  <tr>
			<?php 
			if($attr_status) {
				$i = 0;
				foreach($attr as $key=>$value){?>
				<td class="text_right"><?php echo  $attribute[$key];?>:</td>
				<td class="text_left"><?php echo  $value;?></td>
			<?php 
				if($i%2) {
					echo "</tr><tr>";
				}
				$i++;
				
			 } }?>
			  </tr>
		</table>
		<p><?php echo  $goodsinfo['goods_intro'];?></p>
	</div>
	<div id="tab1_content1" class="detail_content" style="display:none">
		<?php echo  $goodsinfo['goods_wholesale'];?>
	</div>
</div>
<div class="recommend3">
    	<div class="top">
        	<div class="line"></div><span class="right top10 right10"><a class="highlight" href="search.php?best=1"><?php echo $i_langpackage->i_more;?>>></a></span><ul><li class="active"><a href="search.php?best=1"><?php echo $i_langpackage->i_boutique;?></a></li></ul>
        </div>
        <div class="c_m" style="overflow:inherit">
        	<ul>
        	<?php 
			if($best_goods) {
			foreach($best_goods as $value){?>
              <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>"/></a><a class="pro_name" href="<?php echo  goods_url($value['goods_id']);?>" title="<?php echo $value['goods_name'];?>"><?php echo  sub_str($value['goods_name'],20);?></a><label>￥<?php echo  $value['goods_price'];?><?php echo $i_langpackage->i_yan;?></label></li>
        	<?php }?>
			<?php }?>
            	<div class="clear"></div>
        	</ul>
		</div>
	 </div>

	<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
	<script language="JavaScript">
	<!--
		function addFavorite(id) {
			ajax("do.php?act=goods_add_favorite","POST","id="+id,function(data){
				if(data == 1) {
					alert("<?php echo  $s_langpackage->s_g_addedfavorite;?>");
				} else if(data == -1) {
					alert("<?php echo  $s_langpackage->s_g_stayfavorite;?>");
				} else {
					alert("<?php echo  $s_langpackage->s_g_addfailed;?>");
				}
			});
		}

		function addCart(id) {
			var num = document.getElementById('num');
			ajax("do.php?act=goods_add_cart","POST","id="+id+"&num="+num.value,function(data){
				if(data == 1) {
					alert("<?php echo  $s_langpackage->s_g_addedcart;?>");
				} else if(data == -1) {
					alert("<?php echo  $s_langpackage->s_staycart;?>");
				} else if(data == -2) {
					alert("<?php echo  $s_langpackage->s_nomachgoods;?>");
				} else {
					alert("<?php echo  $s_langpackage->s_g_addfailed;?>");
				}
			});
		}
		function gotoOrder(id) {
			value = document.getElementById('num').value;
			location.href = "<?php echo  $baseUrl;?>modules.php?app=user_order&gid="+id+"&v="+value;
		}
	//-->
	</script>
	<?php   require("shop/footer.php");?>
</div>
</body>
</html>
<script language="JavaScript" type="text/javascript">
	function showarealist(n){
		var obj = document.getElementById("areabox");
		var width = document.body.clientWidth;
		var left = "100";
		if(width) {
			left = (width-200)/2;
		}
		obj.style.left = left+"px";
		if(n){
			obj.style.display="";
		}else{
			obj.style.display="none";
		}
	}
	function gettransport_price(n,areaname){
		var goods_id = <?php echo $goodsinfo['goods_id'];?>;
		ajax("do.php?act=get_transport_price","POST","goods_id="+goods_id+"&area_id="+n,function(data){
			document.getElementById("transport_price").innerHTML=data;
			document.getElementById("area_name").innerHTML=areaname;
			showarealist(0);
		});
	}
</script>
<?php } ?>