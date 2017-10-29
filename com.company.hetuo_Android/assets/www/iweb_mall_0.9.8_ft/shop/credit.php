<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/shop/credit.html
 * 如果您的模型要进行修改，请修改 models/shop/credit.php
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
if(filemtime("templates/default/shop/credit.html") > filemtime(__file__) || (file_exists("models/shop/credit.php") && filemtime("models/shop/credit.php") > filemtime(__file__)) ) {
	tpl_engine("default","shop/credit.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_users.php");
require_once("foundation/module_credit.php");

//引入语言包
$s_langpackage=new shoplp;

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_shop_guestbook = $tablePreStr."shop_guestbook";
$t_credit = $tablePreStr."credit";
$t_integral = $tablePreStr."integral";

/* 商铺信息处理 */
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}
$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks[0];

$header['title'] = $s_langpackage->s_shop_credit." - ".$SHOP['shop_name'];
$header['keywords'] = $SHOP['shop_management'];
$header['description'] = sub_str(strip_tags($SHOP['shop_intro']),100);

/* 本页面信息处理 */
$user_id=$SHOP['user_id'];
$sql="select * from $t_credit where buyer=$user_id or seller=$user_id";
$credit = $dbo->getRs($sql);

$seller_credit=$seller_credit_num = $seller_credit_before=$seller_credit_sex=0;
$seller_credit_num_2 = $seller_credit_before_2=$seller_credit_sex_2=0;
$seller_credit_num_1 = $seller_credit_before_1=$seller_credit_sex_1=0;
$seller_credit_num_0 = $seller_credit_before_0=$seller_credit_sex_0=0;
$seller_credit_month=$seller_credit_month_0=$seller_credit_month_1=$seller_credit_month_2=0;
$seller_credit_weed=$seller_credit_weed_0=$seller_credit_weed_1=$seller_credit_weed_2=0;

$buyer_credit=$buyer_credit_num = $buyer_credit_before=$buyer_credit_sex=0;
$buyer_credit_num_2 = $buyer_credit_before_2=$buyer_credit_sex_2=0;
$buyer_credit_num_1 = $buyer_credit_before_1=$buyer_credit_sex_1=0;
$buyer_credit_num_0 = $buyer_credit_before_0=$buyer_credit_sex_0=0;
$buyer_credit_month=$buyer_credit_month_0=$buyer_credit_month_1=$buyer_credit_month_2=0;
$buyer_credit_weed=$buyer_credit_weed_0=$buyer_credit_weed_1=$buyer_credit_weed_2=0;

$SexMonth = $ctime->time_stamp() - (180 * 24 * 60 * 60);
$SexMonth = date('Y-m-d', $SexMonth);
$LastMonth = $ctime->time_stamp() - (30 * 24 * 60 * 60);
$LastMonth = date('Y-m-d', $LastMonth);
$LastWeek = $ctime->time_stamp() - (7 * 24 * 60 * 60);
$LastWeek = date('Y-m-d', $LastWeek);
foreach ($credit as $key=>$val){
	//作为卖家
	if($val['seller']==$user_id){

		if($val['seller_evaltime']<$SexMonth){
			if($val['seller_credit']==1){
				$seller_credit_before_2++;
			}
			if($val['seller_credit']==0){
				$seller_credit_before_1++;
			}
			if($val['seller_credit']==-1){
				$seller_credit_before_0++;
			}
			$seller_credit_before=$seller_credit_before_0+$seller_credit_before_1+$seller_credit_before_2;
		}else
		if($val['seller_evaltime']>=$SexMonth){
			if($val['seller_credit']==1){
				$seller_credit_sex_2++;
			}
			if($val['seller_credit']==0){
				$seller_credit_sex_1++;
			}
			if($val['seller_credit']==-1){
				$seller_credit_sex_0++;
			}
			$seller_credit_sex=$seller_credit_sex_0+$seller_credit_sex_1+$seller_credit_sex_2;

			if($val['seller_evaltime']>=$LastMonth){
				if($val['seller_credit']==1){
					$seller_credit_month_2++;
				}
				if($val['seller_credit']==0){
					$seller_credit_month_1++;
				}
				if($val['seller_credit']==-1){
					$seller_credit_month_0++;
				}
				$seller_credit_month=$seller_credit_month_0+$seller_credit_month_1+$seller_credit_month_2;

				if($val['seller_evaltime']>=$LastWeek){
					if($val['seller_credit']==1){
						$seller_credit_weed_2++;
					}
					if($val['seller_credit']==0){
						$seller_credit_weed_1++;
					}
					if($val['seller_credit']==-1){
						$seller_credit_weed_0++;
					}
					$seller_credit_weed=$seller_credit_weed_0+$seller_credit_weed_1+$seller_credit_weed_2;
				}
			}
		}
		$seller_credit=$seller_credit+$val['seller_credit'];
	}
	//作为买家
	if($val['buyer']==$user_id){

		if($val['buyer_evaltime']<$SexMonth){
			if($val['buyer_credit']==1){
				$buyer_credit_before_2++;
			}
			if($val['buyer_credit']==0){
				$buyer_credit_before_1++;
			}
			if($val['buyer_credit']==-1){
				$buyer_credit_before_0++;
			}
			$buyer_credit_before=$buyer_credit_before_0+$buyer_credit_before_1+$buyer_credit_before_2;
		}else
		if($val['buyer_evaltime']>=$SexMonth){
			if($val['buyer_credit']==1){
				$buyer_credit_sex_2++;
			}
			if($val['buyer_credit']==0){
				$buyer_credit_sex_1++;
			}
			if($val['buyer_credit']==-1){
				$buyer_credit_sex_0++;
			}
			$buyer_credit_sex=$buyer_credit_sex_0+$buyer_credit_sex_1+$buyer_credit_sex_2;

			if($val['buyer_evaltime']>=$LastMonth){
				if($val['buyer_credit']==1){
					$buyer_credit_month_2++;
				}
				if($val['buyer_credit']==0){
					$buyer_credit_month_1++;
				}
				if($val['buyer_credit']==-1){
					$buyer_credit_month_0++;
				}
				$buyer_credit_month=$buyer_credit_month_0+$buyer_credit_month_1+$buyer_credit_month_2;

				if($val['buyer_evaltime']>=$LastWeek){
					if($val['buyer_credit']==1){
						$buyer_credit_weed_2++;
					}
					if($val['buyer_credit']==0){
						$buyer_credit_weed_1++;
					}
					if($val['buyer_credit']==-1){
						$buyer_credit_weed_0++;
					}
					$buyer_credit_weed=$buyer_credit_weed_0+$buyer_credit_weed_1+$buyer_credit_weed_2;
				}
			}
		}
		$buyer_credit=$buyer_credit+$val['buyer_credit'];
	}
}

$seller_integral=get_integral($dbo,$t_integral,$seller_credit);
$seller_credit_num = $seller_credit_before+$seller_credit_sex;
$seller_credit_num_2 = $seller_credit_before_2+$seller_credit_sex_2;
$seller_credit_num_1 = $seller_credit_before_1+$seller_credit_sex_1;
$seller_credit_num_0 = $seller_credit_before_0+$seller_credit_sex_0;
$seller_percentage=0;
if($seller_credit_num!=0){
	$seller_percentage = sub_str($seller_credit_num_2/$seller_credit_num,6)*100;
}

$buyer_integral=get_integral($dbo,$t_integral,$buyer_credit);
$buyer_credit_num = $buyer_credit_before+$buyer_credit_sex;
$buyer_credit_num_2 = $buyer_credit_before_2+$buyer_credit_sex_2;
$buyer_credit_num_1 = $buyer_credit_before_1+$buyer_credit_sex_1;
$buyer_credit_num_0 = $buyer_credit_before_0+$buyer_credit_sex_0;
$buyer_percentage=0;
if($buyer_credit_num!=0){
	$buyer_percentage = sub_str($buyer_credit_num_2/$buyer_credit_num,6)*100;
}
//echo $sql;
//echo $buyer_credit_weed_2;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo  $header['title'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="keywords" content="<?php echo  $header['keywords'];?>" />
<meta name="description" content="<?php echo  $header['description'];?>" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/shop_<?php echo  $SHOP['shop_template'];?>.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<style>
.shop_header {background:#4A9DA5;}
<?php if($SHOP['shop_template_img']){?>
.shop_header {background:transparent url(<?php echo  $SHOP['shop_template_img'];?>) no-repeat scroll 0 0;}
<?php }?>

table { border-collapse:collapse; border:solid #D7D7D7; border-width:1px 0 0 1px; margin:5px auto;}
table th, table td { border:solid #D7D7D7; border-width:0 1px 1px 0; padding:2px; line-height:24px;}
table th {background:#F8F8F8;}

th,td {text-align:center;}


</style>
</head>
<body>
<div id="wrapper">
	<?php  require("shop/header.php");?>
	<div class="clear"></div>
	<div class="shop_header">
		<h1><?php echo  $SHOP['shop_name'];?></h1>
		<?php  require("shop/menu.php");?>
	</div>
	<?php  require("shop/left.php");?>
<div class="bigpart">
	<div class="shop_notice top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="javascript:;"><?php echo $s_langpackage->s_com;?></a></h2></div>
		<div class="c_m">
		<table width="98%">
		<tr><td style="background: #F4F9FF; text-align:right; border-right:0px; font-weight:bold;" colspan="2"><?php echo $s_langpackage->s_seller_credit;?>：<?php echo $seller_credit;?> <td style="text-align:left; background: #F4F9FF; border-right:0px;"><span class="icon<?php echo $seller_integral['int_grade'];?>" style="float:left;"></span></td></td><td style="background: #F4F9FF; text-align:right;" colspan="3"><?php echo  $s_langpackage->s_good_pro;?>：<?php echo $seller_percentage;?>%&nbsp;</td></tr>
		<tr>
			<th width="30"></th>
			<th width="100"><?php echo $s_langpackage->s_week;?></th>
			<th width="110"><?php echo $s_langpackage->s_month;?></th>
			<th width="120"><?php echo $s_langpackage->s_sex_month;?></th>
			<th width="130"><?php echo $s_langpackage->s_before_smonth;?></th>
			<th><?php echo $s_langpackage->s_sum;?></th>
		</tr>
		<tr>
			<td><?php echo $s_langpackage->s_good;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,1,'<?php echo $LastWeek;?>',1);"><?php echo $seller_credit_weed_2;?></a></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,1,'<?php echo $LastMonth;?>',1);"><?php echo $seller_credit_month_2;?></a></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,1,'<?php echo $SexMonth;?>',1);"><?php echo $seller_credit_sex_2;?></a></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,1,'-1',1);"><?php echo $seller_credit_before_2;?></a></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,1,'',1);"><?php echo $seller_credit_num_2;?></a></td>
		</tr>
		<tr>
			<td><?php echo $s_langpackage->s_centre;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,0,'<?php echo $LastWeek;?>',1);"><?php echo $seller_credit_weed_1;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,0,'<?php echo $LastMonth;?>',1);"><?php echo $seller_credit_month_1;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,0,'<?php echo $SexMonth;?>',1);"><?php echo $seller_credit_sex_1;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,0,'-1',1);"><?php echo $seller_credit_before_1;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,0,'',1);"><?php echo $seller_credit_num_1;?></td>
		</tr>
		<tr>
			<td><?php echo $s_langpackage->s_diff;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,-1,'<?php echo $LastWeek;?>',1);"><?php echo $seller_credit_weed_0;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,-1,'<?php echo $LastMonth;?>',1);"><?php echo $seller_credit_month_0;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,-1,'<?php echo $SexMonth;?>',1);"><?php echo $seller_credit_sex_0;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,-1,'-1',1);"><?php echo $seller_credit_before_0;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,-1,'',1);"><?php echo $seller_credit_num_0;?></td>
		</tr>
		<tr>
			<td><?php echo $s_langpackage->s_sum;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,'','<?php echo $LastWeek;?>',1);"><?php echo $seller_credit_weed;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,'','<?php echo $LastMonth;?>',1);"><?php echo $seller_credit_month;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,'','<?php echo $SexMonth;?>',1);"><?php echo $seller_credit_sex;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,'','-1',1);"><?php echo $seller_credit_before;?></td>
			<td><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,'','',1);"><?php echo $seller_credit_num;?></td>
		</tr>
		</table>
		</div>
		<div style="clear:both;"></div>
	</div>
	<div class="shop_notice top10">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2><a class="highlight" href="javascript:;"><?php echo $s_langpackage->s_buyer_com;?></a></h2></div>
		<div class="c_m">
		<table width="98%">
		<tr><td style="background: #F4F9FF; text-align:right; border-right:0px; font-weight:bold;" colspan="2"><?php echo $s_langpackage->s_buyer_credit;?>：<?php echo $buyer_credit;?> <td style="text-align:left; background: #F4F9FF; border-right:0px;"><span class="icon<?php echo $buyer_integral['int_grade'];?>" style="float:left;"></span></td></td><td style="background: #F4F9FF; text-align:right;" colspan="3"><?php echo  $s_langpackage->s_good_pro;?>：<?php echo $buyer_percentage;?>%&nbsp;</td></tr>
		<tr>
			<th width="30"></th>
			<th width="100"><?php echo $s_langpackage->s_week;?></th>
			<th width="110"><?php echo $s_langpackage->s_month;?></th>
			<th width="120"><?php echo $s_langpackage->s_sex_month;?></th>
			<th width="130"><?php echo $s_langpackage->s_before_smonth;?></th>
			<th><?php echo $s_langpackage->s_sum;?></th>
		</tr>
		<tr>
				<td><?php echo $s_langpackage->s_good;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,1,'<?php echo $LastWeek;?>',1);"><?php echo $buyer_credit_weed_2;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,1,'<?php echo $LastMonth;?>',1);"><?php echo $buyer_credit_month_2;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,1,'<?php echo $SexMonth;?>',1);"><?php echo $buyer_credit_sex_2;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,1,'-1',1);"><?php echo $buyer_credit_before_2;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,1,'',1);"><?php echo $buyer_credit_num_2;?></td>
		</tr>
		<tr>
				<td><?php echo $s_langpackage->s_centre;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,0,'<?php echo $LastWeek;?>',1);"><?php echo $buyer_credit_weed_1;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,0,'<?php echo $LastMonth;?>',1);"><?php echo $buyer_credit_month_1;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,0,'<?php echo $SexMonth;?>',1);"><?php echo $buyer_credit_sex_1;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,0,'-1',1);"><?php echo $buyer_credit_before_1;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,0,'',1);"><?php echo $buyer_credit_num_1;?></td>
		</tr>
		<tr>
				<td><?php echo $s_langpackage->s_diff;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,-1,'<?php echo $LastWeek;?>',1);"><?php echo $buyer_credit_weed_0;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,-1,'<?php echo $LastMonth;?>',1);"><?php echo $buyer_credit_month_0;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,-1,'<?php echo $SexMonth;?>',1);"><?php echo $buyer_credit_sex_0;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,-1,'-1',1);"><?php echo $buyer_credit_before_0;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,-1,'',1);"><?php echo $buyer_credit_num_0;?></td>
		</tr>
		<tr>
				<td><?php echo $s_langpackage->s_sum;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,'','<?php echo $LastWeek;?>',1);"><?php echo $buyer_credit_weed;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,'','<?php echo $LastMonth;?>',1);"><?php echo $buyer_credit_month;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,'','<?php echo $SexMonth;?>',1);"><?php echo $buyer_credit_sex;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,'','-1',1);"><?php echo $buyer_credit_before;?></td>
			<td><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,'','',1);"><?php echo $buyer_credit_num;?></td>
		</tr>
		</table>
		</div>
	</div>
	<!-- 评介内容列表 begin -->
	<div id="comment_list" class="shop_notice top10">
    	<div class="top">
        	<div class="line"></div><ul><li id="seller_selected" class="selected"><a href="javascript:get_appraise('buyer',<?php echo $user_id;?>,'','',1);" hidefocus="true"><?php echo $s_langpackage->s_from_seller_credit;?></a></li>
			<li id="buyer_selected" class=""><a href="javascript:get_appraise('seller',<?php echo $user_id;?>,'','',1);" hidefocus="true"><?php echo $s_langpackage->s_from_buyer_credit;?></a></li></ul>
        </div>
        <div class="c_m" style="border-bottom:1px solid #CCCCCC;" id="credit">

		</div>
	</div>
	<!-- 评介内容列表 end -->
</div>
	<?php   require("shop/footer.php");?>
</div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script>
function checkForm() {
	var name = document.getElementsByName("name")[0];
	var textareac = document.getElementById("textareac")[0];
	if(!name.value) {
		alert("<?php echo $s_langpackage->s_login_pls;?>");
		return false;
	}
	if(textareac.value) {
		alert("<?php echo $s_langpackage->s_type_comm_pls;?>");
		return false;
	}
	return true;
}

function get_appraise(t,userid,credit,time,page){
	var new_credit=credit;
	var new_time=time;
	ajax("do.php?act=shop_appraise&t="+t+"&userid="+userid+"&credit="+credit+"&time="+time+"&page="+page,"GET",'',function(data){
		var obj_credit = document.getElementById("credit");
		var seller_selected=document.getElementById("seller_selected");
		var buyer_selected=document.getElementById("buyer_selected");
		if(data!='-1'){
			var obj = document.getElementById("page");
			var prepage=data.prepage;
			var nextpage=data.nextpage;
			var firstpage=data.firstpage;
			var lastpage=data.lastpage;
			var page=data.page;
			var countpage=data.countpage;
			var seller_credit='';
			var pagehtml="<tr><td id='page' colspan='4'><A href=\"javascript:get_appraise('"+t+"\',"+userid+",'"+new_credit+"','"+time+"',"+firstpage+");\">首页</A> <A href=\"javascript:get_appraise('"+t+"',"+userid+",'"+new_credit+"','"+time+"',"+prepage+");\">上一页</A> <A href=\"javascript:get_appraise('"+t+"',"+userid+",'"+new_credit+"','"+time+"',"+nextpage+");\">下一页</A> <A href=\"javascript:get_appraise('"+t+"',"+userid+",'"+new_credit+"','"+time+"',"+lastpage+");\">尾页</A> 当前第"+page+"页/总共"+countpage+"页</td></tr>";

			var result = data.result;
			var credit='';

			if(t=='buyer'){

				seller_selected.className="selected";
				buyer_selected.className="";
				for($i=0;$i<result.length;$i++){
					credit=result[$i].seller_credit;
					if(credit=='1'){
						seller_credit="好";
					}else
					if(credit=='0'){
						seller_credit="中";
					}else
					if(credit=='-1'){
						seller_credit="差";
					}
					credit+='<tr><td class="center" width="30">'+seller_credit+'</td><td style="text-align:left" >'+result[$i].seller_evaluate+'<br />[解释]'+result[$i].seller_explanation+'<br />['+result[$i].seller_evaltime+']</td><td>'+result[$i].user_name+'</td><td style="text-align:left" width="200"><a href="goods.php?id='+result[$i].goods_id+'" target="_blank" style="font-size:12px; font-weight:bold; color:#0044DD" title="'+result[$i].goods_name+'">'+result[$i].goods_name+'</a><br />价格：￥<span style="color:#FF6600; font-weight:bold;">'+result[$i].goods_price+'</span>元</td></tr>';
				}
			}else
			if(t=='seller'){
				seller_selected.className="";
				buyer_selected.className="selected";
				for($i=0;$i<result.length;$i++){
					credit=result[$i].buyer_credit;
					if(credit=='1'){
						buyer_credit="好";
					}else
					if(credit=='0'){
						buyer_credit="中";
					}else
					if(credit=='-1'){
						buyer_credit="差";
					}
					credit+='<tr><td class="center" width="30">'+buyer_credit+'</td><td style="text-align:left" >'+result[$i].buyer_evaluate+'<br />[解释]'+result[$i].buyer_explanation+'<br />['+result[$i].buyer_evaltime+']</td><td>'+result[$i].user_name+'</td><td style="text-align:left" width="200"><a href="goods.php?id='+result[$i].goods_id+'" target="_blank" style="font-size:12px; font-weight:bold; color:#0044DD" title="'+result[$i].goods_name+'">'+result[$i].goods_name+'</a><br />价格：￥<span style="color:#FF6600; font-weight:bold;">'+result[$i].goods_price+'</span>元</td></tr>';
				}
			}
			obj_credit.innerHTML = "<table width='98%'>"+credit+pagehtml+"</table>";
		}else{
			if(t=='buyer'){
				seller_selected.className="selected";
				buyer_selected.className="";
				obj_credit.innerHTML = "<table width='98%'><tr><td><?php echo $s_langpackage->s_no_seller_credit;?></td></tr></table>";
			}else
			if(t=='seller'){
				seller_selected.className="";
				buyer_selected.className="selected";
				obj_credit.innerHTML = "<table width='98%'><tr><td><?php echo $s_langpackage->s_no_buyer_credit;?></td></tr></table>";
			}
		}

	},'JSON');
}

get_appraise('buyer',<?php echo $user_id;?>,'','',1);
</script>
</body>
</html>
<?php } ?>