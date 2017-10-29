<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/index.html
 * 如果您的模型要进行修改，请修改 models/index.php
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
if(filemtime("templates/default/index.html") > filemtime(__file__) || (file_exists("models/index.php") && filemtime("models/index.php") > filemtime(__file__)) ) {
	tpl_engine("default","index.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require_once("foundation/fstring.php");

/* 用户信息处理 */
//require 'foundation/alogin_cookie.php';
if(get_sess_user_id()) {
	$USER['login'] = 1;
	$USER['user_name'] = get_sess_user_name();
	$USER['user_id'] = get_sess_user_id();
	$USER['user_email'] = get_sess_user_email();
	$USER['shop_id'] = get_sess_shop_id();
} else {
	$USER['login'] = 0;
	$USER['user_name'] = '';
	$USER['user_id'] = '';
	$USER['user_email'] = '';
	$USER['shop_id'] = '';
}

//引入语言包
$i_langpackage=new indexlp;

$header['title'] = $i_langpackage->i_index." - ".$SYSINFO['sys_title'];
$header['keywords'] = $SYSINFO['sys_keywords'];
$header['description'] = $SYSINFO['sys_description'];

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_category = $tablePreStr."category";
$t_goods = $tablePreStr."goods";
$t_index_images = $tablePreStr."index_images";
$t_brand = $tablePreStr."brand";
$t_article = $tablePreStr."article";
$t_users = $tablePreStr."users";
$t_flink= $tablePreStr."flink";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc,sort_order asc";
$result_category = $dbo->getRs($sql_category);

$CATEGORY = array();
if($result_category) {
	foreach($result_category as $v) {
		$CATEGORY[$v['parent_id']][$v['cat_id']] = $v;

	}
}

/* 轮显图片 */
$sql_images = "select * from `$t_index_images` where `status`=1 order by id asc limit 6";
$images_info = $dbo->getRs($sql_images);

if($images_info) {
	$images_order = '""';
	$images_array = '';
	$i = 1;
	foreach($images_info as $images) {
		$images_order .= ',"'.$i.'"';
		$images_array .= "imgLink[$i] = '$images[images_link]'; \n";
		$images_array .= "imgUrl[$i] = '$images[images_url]'; \n";
		$images_array .= "imgText[$i] = '$images[name]'; \n";
		$i++;
	}

}

/* 产品处理 */
$sql_promote = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_promote=1 and lock_flg=0 order by pv desc limit 14";
$sql_best = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_best=1 and lock_flg=0 order by pv desc limit 10";
$sql_hot = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_hot=1 and lock_flg=0 order by pv desc limit 10";
$sql_new_hot = "select * from $t_goods where is_on_sale=1 AND is_new=1 and lock_flg=0 order by pv desc limit 3";
$sql_new = "select * from $t_goods where is_on_sale=1 and lock_flg=0 order by add_time desc limit 10";
$sql_brand = "select * from $t_brand where is_show=1 and brand_logo!='' ORDER BY brand_id DESC limit 10";
$sql_notice = "select * from $t_article where cat_id=3 and is_show=1 order by add_time desc limit 8;";
$sql_help = "select * from $t_article where cat_id=5 and is_show=1 order by add_time desc limit 8;";
$sql_maller = "select * from $t_article where cat_id=6 and is_show=1 order by add_time desc limit 3;";
$sql_seller = "select * from $t_article where cat_id=7 and is_show=1 order by add_time desc limit 3;";
$sql_flink = "select * from $t_flink where is_show=1 and brand_logo!='' ORDER BY brand_id DESC limit 10";

$goods_promote = $dbo->getRs($sql_promote);
$goods_best = $dbo->getRs($sql_best);
$goods_hot = $dbo->getRs($sql_hot);
$goods_new_hot = $dbo->getRs($sql_new_hot);
$goods_new = $dbo->getRs($sql_new);
$brand_rs = $dbo->getRs($sql_brand);
$notice = $dbo->getRs($sql_notice);
$help = $dbo->getRs($sql_help);
$maller = $dbo->getRs($sql_maller);
$seller = $dbo->getRs($sql_seller);
/* 友情链接 */
$flink_rs = $dbo->getRs($sql_flink);
/* 商家信息 */
$sql_shop = "SELECT a.*,b.user_name FROM $t_shop_info as a,$t_users as b  where a.user_id = b.user_id and a.lock_flg=0 order by a.shop_id desc limit 7;";
$shop_info = $dbo->getRs($sql_shop);

/*导航位置*/
$nav_selected=1;

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><?php echo  $header['title'];?></title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="keywords" content="<?php echo  $header['keywords'];?>" />
<meta name="description" content="<?php echo  $header['description'];?>" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css" rel="stylesheet" type="text/css" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
</head>
<body>
<div class="container">
<?php  include("shop/index_header.php");?>
<div class="board spart">
<div class="top">
            <ul id="tab1">
                <li id="tab1_title0" onmouseover="nTabs('tab1',this);" class="selected"><a href="article_list.php?id=3"><?php echo  $i_langpackage->i_index_nocite;?></a></li>
                <li id="tab1_title1" onmouseover="nTabs('tab1',this);"><a href="article_list.php?id=5" hidefocus="true"><?php echo  $i_langpackage->i_fastsell;?></a></li>
			</ul>
        </div>
        <div id="tab1_content0" class="content">
            <ul>
			<?php  foreach($notice as $value){?>
             <li><a href="<?php echo  article_url($value['article_id']);?>" title="<?php echo  $value['title'];?>"><?php echo  sub_str($value['title'],22,false);?></a></li>
			<?php }?>
            </ul>
        </div>
        <div id="tab1_content1" class="content" style="display:none">
            <ul>
               <?php  foreach($help as $value){?>
                <li><a href="<?php echo  article_url($value['article_id']);?>" title="<?php echo  $value['title'];?>"><?php echo  sub_str($value['title'],22,false);?></a></li>
			   <?php }?>
            </ul>
        </div>
    </div>
    <div id="slider" class="slide mpart">
		<div class="slider-stage">
        <?php  foreach($images_info as  $key =>$value){?>
            <p id="tab2_content<?php echo  $key;?>">
            <a target="_blank" href="<?php echo  $value['images_link'];?>">
            <img style="opacity: 1; display: block;" src="<?php echo  $value['images_url'];?>" alt="" height="240" width="420">
            </a>
            </p>
        <?php }?>
        </div>
		<div class="slider-stick">
            <ul id="tab2">
              <?php  foreach($images_info as $key =>$value){?>
                <li id="tab2_title<?php echo $key;?>" onmouseover="nTabs_index('tab2',<?php echo $key;?>);" <?php if($key==0){?>class="active" style="border-top:0"<?php }?> <?php if($key!=0){?>class=" " <?php }?> ><a target="_blank" href="<?php echo  $value['images_link'];?>"><?php echo  $value['name'];?></a></li>
               <?php }?>
            </ul>
        </div>
    </div>
    <div class="spart omega">
    	<div class="user_guide"><a class="first left" href="<?php echo  article_url(11);?>"></a><a class="free right" href="modules.php?app=reg"></a></div>
        <div class="user_help">
        	<p class="group"><a href=""><?php echo  $i_langpackage->i_group_buy_small_shops;?></a></p>
        	<p class="credit" style="margin-bottom:0"><a href="" class="orange"><?php echo  $i_langpackage->i_credit_evaluation_system;?></a></p>
        </div>
        <div class="user_service">
        	<div class="top">
           		 <div class="line"></div>
                    <ul id="tab0">
                    <li id="tab0_title0" class="selected" onmouseover="nTabs('tab0',this);"><a href="" hidefocus="true"><?php echo  $i_langpackage->i_ammall;?></a></li>
                    <li id="tab0_title1" onmouseover="nTabs('tab0',this);"><a href="" hidefocus="true"><?php echo  $i_langpackage->i_amsell;?></a></li>
                    </ul>
            </div>
            <div id="tab0_content0" class="content">
            	<ul>
                  	 <?php  foreach($maller as $key =>$value){?>
               			<li><a href="article.php?id=<?php echo $value['article_id'];?>"><?php echo  $value['title'];?></a></li>
               		 <?php }?>
                </ul>
            </div>
            <div id="tab0_content1" class="content" style="display:none">
            	<ul>
                	 <?php  foreach($seller as $key =>$value){?>
               			<li><a href="article.php?id=<?php echo $value['article_id'];?>"><?php echo  $value['title'];?></a></li>
            	   <?php }?>
                </ul>
            </div>
        </div>
	</div>
    <div class="clear"></div>
    <div class="apart asd1"><script language="JavaScript" src="uploadfiles/asd/1.js"></script></div>
    <div class="clear"></div>
    <div class="category lpart">
    	<div class="top">
        	<div class="line"></div><span class="right"><a class="highlight" href="search.php"><?php echo $i_langpackage->i_allgoodsheader_category;?>>></a></span><ul><li class="active"><a href="search.php"><?php echo  $i_langpackage->i_goodsheader_category;?></a></li></ul>
        </div>
        <div class="content zoom">
        <?php  foreach(array_slice ($CATEGORY[0], 0, 3) as $key=>$cat){?>
                 <?php if($key!=2){?><div class="item t2"> <?php }?>
                 <?php if($key==2){?><div class="item" style="background:none">  <?php }?>
                <h3 class="t2"><span><a href="<?php echo  category_url($cat['cat_id']);?>" title="<?php echo  $cat['cat_name'];?>"><?php echo  $cat['cat_name'];?></a></span></h3>
            <?php if(isset($CATEGORY[$cat['cat_id']]) && $CATEGORY[$cat['cat_id']]){?>

                <ul>
                <?php  foreach(array_slice ($CATEGORY[$cat['cat_id']], 0, 3) as $subcat){?>
                        <li>
                            <dl>
                              <dt><a href="<?php echo  category_url($subcat['cat_id']);?>" title="<?php echo  $cat['cat_name'];?>"><?php echo  $subcat['cat_name'];?></a></dt>
                              <dd>
                              <?php if(isset($CATEGORY[$subcat['cat_id']]) && $CATEGORY[$subcat['cat_id']]){?>
                                <?php  foreach($CATEGORY[$subcat['cat_id']] as $thirdcat){?>
                                 <a target="_blank" href="<?php echo  category_url($thirdcat['cat_id']);?>" title="<?php echo  $thirdcat['cat_name'];?>"><?php echo  $thirdcat['cat_name'];?></a>
                                <?php }?>
                              <?php }?>
                             </dd>
                            </dl>
                        </li>
                <?php }?>
             	</ul>
             <?php }?>
              <?php if(isset($CATEGORY[$cat['cat_id']]) && $CATEGORY[$cat['cat_id']]){?>
              <ul>

                    <?php  foreach(array_slice ($CATEGORY[$cat['cat_id']], 3, 3) as $subcat){?>
                        <li>
                            <dl>
                              <dt><a href="<?php echo  category_url($subcat['cat_id']);?>" title="<?php echo  $subcat['cat_name'];?>"><?php echo  $subcat['cat_name'];?></a></dt>
                              <dd>
                                <?php if(isset($CATEGORY[$subcat['cat_id']]) && $CATEGORY[$subcat['cat_id']]){?>
                                <?php  foreach($CATEGORY[$subcat['cat_id']] as $thirdcat){?>
                                <a target="_blank" href="<?php echo  category_url($thirdcat['cat_id']);?>" title="<?php echo  $thirdcat['cat_name'];?>" ><?php echo  $thirdcat['cat_name'];?></a>
                                <?php }?>
                                <?php }?>
                              </dd>
                            </dl>
                        </li>
                    <?php }?>

                 </ul>
                    <?php }?>
            </div>
            <div class="clear"></div>
		<?php }?>
        </div>

    </div>
    <div class="spart omega">
    <div class="rank">
	<div class="top"><div class="bg_right"></div><div class="bg_left"></div><h2><?php echo  $i_langpackage->i_hotgoods_sort;?></h2></div>
		<div class="c2_t">
           <span class="right"></span>
        </div>
        <div class="content">
		<ul id="hotul" class="cls">
		<?php $i=1;?>
		<?php   foreach($goods_hot as $value){?>
			<li onmouseover="promote_change(this)" <?php if($i==1){?> class="selected"<?php }?> >
                <span class="num"><?php echo $i;?></span>
                <a href="<?php echo  goods_url($value['goods_id']);?>">
                <img height="60" width="60" src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $value['goods_name'];?>" />                 <span title="<?php echo  $value['goods_name'];?>"><?php echo  sub_str($value['goods_name'],20,false);?></span>
                </a>
                <div class="price"><span>￥<?php echo $value['goods_price'];?></span></div>
            </li>
			<?php $i++;?>
		<?php }?>
		</ul>
		</div>
        <div class="c_bt">
          <div class="c_bt_l left"></div>
          <div class="c_bt_r right"></div>
        </div>
        <div class="bottom"></div>
	</div>
         <div class="asd3" style="margin-top:12px;"><script language="JavaScript" src="uploadfiles/asd/3.js"></script></div>
    </div>
    <div class="clear"></div>
    <div class="promotion lpart">
    	<div class="top"><span class="right"><a href="search.php?new=1"><?php echo  $i_langpackage->i_moregoods;?>>></a></span><h2><a href="search.php?new=1"><?php echo  $i_langpackage->i_promote_goods;?></a></h2></div>
        <div class="content">
        	<ul>
        	<?php  foreach($goods_promote as $value){?>
				<li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic_small.gif';?>" alt="<?php echo  $value['goods_name'];?>" /></a><a  class="pro_name" href="<?php echo  goods_url($value['goods_id']);?>" title="<?php echo $value['goods_name'];?>"><?php echo  sub_str($value['goods_name'],20,false);?></a><label>￥<?php echo $value['goods_price'];?><?php echo  $i_langpackage->i_yan;?></label></li>
			<?php }?>
        	</ul>
            <div class="clear"></div>
        </div>
    </div>
    <div class="brand spart omega">
    	<div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo  $i_langpackage->i_links_list;?></h2></div>
        <div class="content zoom">
			<?php  foreach($flink_rs as $value){?>
                <a href="<?php echo $value['site_url'];?>" target="_blank"><img src="<?php echo $value['brand_logo'];?>" width="82" height="48" alt="<?php echo $value['brand_name'];?>" /></a>
			<?php }?>

            <div class="clear"></div>
        </div>
    </div>
    <div class="clear"></div>
    <div class="shop_recommend apart">
    	<div class="top"><div class="line"></div> <span class="right"><a class="highlight" href="brand.php"><?php echo  $i_langpackage->i_moreshop;?>>></a></span><ul><li class="active"><a href="brand.php"><?php echo  $i_langpackage->i_best_store;?></a></li></ul></div>
        <div class="content">
        	<ul>
            <?php  foreach($shop_info as $value){?>
            	<li>
                <a href="<?php echo  shop_url($value['shop_id']);?>" class="logo" title=""><img src="<?php echo  $value['shop_logo'] ? $value['shop_logo'] : 'skin/default/images/shop_nologo.gif';?>" width="108" height="53" alt="<?php echo  $value['shop_name'];?>" /></a>
                <a href="<?php echo  shop_url($value['shop_id']);?>" title="<?php echo  $value['shop_name'];?>" class="shop_name"><?php echo  $value['shop_name'];?></a>
             <a href="<?php echo  shop_url($value['shop_id'],'index');?>" class="shoper"><?php echo  $i_langpackage->i_seller;?>：<?php echo  $value['user_name'];?></a></p></li>
                <?php }?>
            	            </ul>
            <div class="clear"></div>
		</div>
    </div>
    <div class="clear"></div>
<?php  require("shop/index_footer.php");?>
</body>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var index_n = 0;
function setvalgo() {
	nTabs_index('tab2',index_n);
}
function nTabs_index(tabObj,n) {
	var tabList = $(tabObj).getElementsByTagName('li');
	if(n>=tabList.length) {
		n = 0;
	}
	var obj = $(tabObj+"_title"+n);
	for(i=0;i<tabList.length;i++) {
		if(tabList[i].id == obj.id) {
			$(tabObj+"_title"+i).className = "active";
			$(tabObj+"_content"+i).style.display = "";
		} else {
			$(tabObj+"_title"+i).className = "";
			$(tabObj+"_content"+i).style.display = "none";
		}
	}
	index_n = n+1;
}
setvalgo();
setInterval("setvalgo()",5000);

var d = new Date();
var t = d.getTime();
ajax("crons.php","POST","t="+t,function(data){});
//-->
</script>
</html>
<?php } ?>