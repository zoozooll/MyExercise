<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/category.html
 * 如果您的模型要进行修改，请修改 models/category.php
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
if(filemtime("templates/default/category.html") > filemtime(__file__) || (file_exists("models/category.php") && filemtime("models/category.php") > filemtime(__file__)) ) {
	tpl_engine("default","category.html",1);
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

require_once("foundation/module_areas.php");

/* URL信息处理 */
$cat_id = intval(get_args('id'));
if(!$cat_id) {
	exit("非法请求！");
}

/* 用户信息处理 */
require 'foundation/alogin_cookie.php';
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
$i_langpackage = new indexlp;

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_category = $tablePreStr."category";
$t_goods = $tablePreStr."goods";
$t_users = $tablePreStr."users";
$t_areas = $tablePreStr."areas";
$t_attribute = $tablePreStr."attribute";
$t_goods_attr = $tablePreStr."goods_attr";
$t_brand = $tablePreStr."brand";
$t_brand_category = $tablePreStr."brand_category";
/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result = $dbo->getRs($sql_category);
$CATEGORY = array();
$cat_info = array();
foreach($result as $v) {
	$CATEGORY[$v['parent_id']][$v['cat_id']] = $v;
}
foreach($result as $v) {
	$cat_info[$v['cat_id']] = $v;
}

/* 列表处理 */
if(!function_exists('getsubcats')) {
	function getsubcats($catinfo, $id) {
		$str = '';
		if(isset($catinfo[$id]) && $catinfo[$id]) {
			foreach($catinfo[$id] as $v) {
				$str .= ",".$v['cat_id'];
				$str .= getsubcats($catinfo, $v['cat_id']);
			}
		}
		return $str;
	}
}
$catids = $cat_id;
$catids .= getsubcats($CATEGORY, $cat_id);

$this_catinfo = $cat_info[$cat_id];
$this_parent_info = '';
if($cat_info[$cat_id]['parent_id']>0){
	$this_parent_info = $cat_info[$cat_info[$cat_id]['parent_id']];
}

//echo $catids;
$areainfo = get_areas_kv($dbo,$t_areas);

$sql = "SELECT g.pv,g.is_set_image,g.goods_thumb,g.goods_id,g.shop_id,g.goods_name,g.goods_price,g.goods_intro, s.shop_province,s.shop_city,
		s.shop_district,s.shop_name,s.user_id ,u.rank_id
		FROM `$t_goods` AS g, `$t_shop_info` AS s,`$t_users` AS u WHERE s.user_id=u.user_id and g.shop_id=s.shop_id AND
		g.cat_id IN ($catids) and g.is_on_sale=1 ";
if($_POST){
	//print_r($_POST);
	$order_name=$_POST['name'];
	$order=$_POST['order'];
	if (!$order_name) {
		$order_name="goods_id";
	}
	if (!$order) {
		$order="DESC";
	}
	$sql.= "ORDER BY g.$order_name $order,u.rank_id DESC,g.pv DESC ";

}else{
	$sql.= "ORDER BY u.rank_id DESC,g.goods_price asc,g.pv DESC ";
}

$result = $dbo->fetch_page($sql,$SYSINFO['product_page']);

/* 产品处理 */
$sql_best = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_best=1 order by pv desc limit 4";
$sql_hot = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_hot=1 order by pv desc limit 10";
$goods_best = $dbo->getRs($sql_best);
$goods_hot = $dbo->getRs($sql_hot);

/* 浏览记录 */
$getcookie = get_hisgoods_cookie();
$goodshistory = array();
if($getcookie) {
	arsort($getcookie);
	$getcookie = array_keys($getcookie);
	$gethisgoodsid = implode(",",array_slice($getcookie, 0, 4));
	$sql = "select is_set_image,goods_id,goods_name,goods_thumb,goods_price from $t_goods where goods_id in ($gethisgoodsid)";
	$goodshistory = $dbo->getRs($sql);
}


$header['title'] = $this_catinfo['cat_name']." - ".$SYSINFO['sys_title'];
$header['keywords'] = $this_catinfo['cat_name'].','.$SYSINFO['sys_keywords'];
$header['description'] = $SYSINFO['sys_description'];
/* 属性 */
$sql = "select * from $t_attribute where cat_id='$cat_id'";
$attr_info = $dbo->getRs($sql);

foreach($attr_info as $key => $value){

	$values=json_encode($value['attr_values']);
	$values_after=str_replace('\r\n',',',$values);
	$values_after=str_replace('\n',',',$values);
	$values_after=json_decode($values_after);
	$attr_info[$key]['attr_values']=explode(',',$values_after);

	foreach($attr_info[$key]['attr_values'] as $k => $va){
		$va=trim($va);
		$sql = "select count(*) AS attr_count from $t_goods_attr AS ga, $t_goods AS g  where ga.attr_values='$va' and g.is_on_sale=1 and
		g.goods_id=ga.goods_id ";
		$goods_attr_info = $dbo->getRow($sql);
		$attr_info[$key]['values_count'][$k]=$goods_attr_info["attr_count"];
	}
}
//品牌列表
$sql = "SELECT brand_id FROM $t_brand_category WHERE cat_id='$cat_id'";
$list = $dbo->getRs($sql);
$brand_list=array();
if (is_array($list)) {
	foreach ($list as $value){
		$sql="SELECT brand_id,brand_name FROM $t_brand WHERE brand_id='{$value['brand_id']}'";
		$brand_list[]=$dbo->getRow($sql);
	}
}
$nav_selected =4;
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
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<form name="category_form" id="category_form" method="POST" action="<?php echo  category_url($cat_id);?>">
<input type="hidden" name="name" value="">
<input type="hidden" name="order" value="">
<!--header end -->
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a><?php if(isset($this_parent_info['cat_name']) && $this_parent_info['cat_name']){?> > <a href="<?php echo  category_url($this_parent_info['cat_id']);?>"><?php echo  $this_parent_info['cat_name'];?></a><?php }?> > <?php echo  $this_catinfo['cat_name'];?></div>
<?php  if(isset($CATEGORY[$cat_id]) && $CATEGORY[$cat_id]) {?>
<div class="pro_class" id="cate_id">
	<div onclick="hidden('cate_id','cate')" style="cursor:pointer;"><h3><?php echo $i_langpackage->i_choice_sort;?></h3></div>
	<dl>
	<?php  foreach($CATEGORY[$cat_id] as $value){?>
		<dd><a title="<?php echo  $value['cat_name'];?>" href="<?php echo  category_url($value['cat_id']);?>"><?php echo  $value['cat_name'];?>(<?php echo  $value['goods_num'];?>)</a></dd>
	<?php }?>    
	</dl><div class="clear"></div>
</div>
<?php }?>
<?php  if(isset($CATEGORY[$cat_id]) && $CATEGORY[$cat_id]) {?>
<div class="pro_class" onclick="show('cate_id','cate')" id="cate" style="display:none;cursor:pointer;">
	<h4><?php echo $i_langpackage->i_choice_sort;?></h4>
</div>
<?php }?>
<br>
<?php  if($attr_info) {?>
<?php  foreach($attr_info as $key => $value){?>
<div class="pro_class" id="<?php echo  $value['attr_id'];?>">
	<div style="cursor:pointer;" onclick="hidden('<?php echo  $value['attr_id'];?>','<?php echo  $value['attr_name'];?>')"><h3><?php echo $i_langpackage->i_select;?><?php echo  $value['attr_name'];?><?php echo $i_langpackage->i_select_goods;?></h3></div>
	<dl><?php  foreach($value['attr_values'] as $k => $v){?>
		<dd><a title="<?php echo  $v;?>" href="search.php?attr[<?php echo $value['attr_id'];?>]=<?php echo  $v;?>&cid=<?php echo  $cat_id;?>"><?php echo  $v;?>(<?php echo  $value['values_count'][$k];?>)</a></dd>
		<?php }?>
	</dl><div class="clear"></div>
</div>
<div class="pro_class" onclick="show('<?php echo  $value['attr_id'];?>','<?php echo  $value['attr_name'];?>')" id="<?php echo  $value['attr_name'];?>" style="display:none;cursor:pointer;">
	<h4><?php echo $i_langpackage->i_select;?><?php echo  $value['attr_name'];?><?php echo $i_langpackage->i_select_goods;?></h4>
</div>
<br>
<?php }?>
<?php }?>

<?php  if(count($brand_list)>0) {?>
<div class="pro_class" id="brand_box">
	<div style="cursor:pointer;" onclick="hidden('brand_box','<?php echo $i_langpackage->i_select_products_by_brand;?>')"><h3><?php echo $i_langpackage->i_select_products_by_brand;?></h3></div>
	<dl><?php  foreach($brand_list as $key => $value){?>
		<dd><a title="<?php echo  $value['brand_name'];?>" href="search.php?brand_id=<?php echo  $value['brand_id'];?>&cid=<?php echo  $cat_id;?>"><?php echo  $value['brand_name'];?></a></dd>
		<?php }?>
	</dl><div class="clear"></div>
</div>
<div class="pro_class" id="<?php echo $i_langpackage->i_select_products_by_brand;?>" onclick="show('brand_box','<?php echo $i_langpackage->i_select_products_by_brand;?>')" style="display:none;cursor:pointer;">
	<h4><?php echo $i_langpackage->i_select;?><?php echo $i_langpackage->i_brand;?><?php echo $i_langpackage->i_select_goods;?></h4>
</div>
<br>
<?php }?>

<div class="top5 clear"></div>
<div class="main2 left top5">
	<div class="top">
	  <span class="right"><a id="list" onclick="changeStyle2('list',this)" class="selected" href="javascript:void(0);" hidefocus="true"><?php echo $i_langpackage->i_list;?></a><a id="window" onclick="changeStyle2('window',this)" href="javascript:void(0);" hidefocus="true"><?php echo $i_langpackage->i_show_window;?></a></span>
	  <h2><a href="#"><?php echo $i_langpackage->i_choice_good;?></a></h2>
	</div>
	<div id="listcontent" class="c_m">
		<ul>
			<li class="list_item_title">
				<ul>
				<li class="operate"><?php echo $i_langpackage->i_handle;?></li>
				<li class="place"><?php echo $i_langpackage->i_in_area;?></li>
				<li class="price"><?php echo $i_langpackage->i_price;?>
				<a href="javascript:void(0);" onclick="order_byorder_name('goods_price','asc')"> ↑ </a>
				<a  href="javascript:void(0);" onclick="order_byorder_name('goods_price','desc')"> ↓ </a></li>
				<li class="summary"><?php echo $i_langpackage->i_g_shop_info;?></li>
				</ul>
			</li>
			<?php  if($result['result']) {
				foreach($result['result'] as $v) {?>
			<li class="list_item" id="showli_<?php echo $v['goods_id'];?>">
              <div class="photo"><a href="<?php echo  goods_url($v['goods_id']);?>"><img onmouseover="showbox(<?php echo $v['goods_id'];?>)" onmouseout="hidebox(<?php echo $v['goods_id'];?>)" src="<?php echo  $v['is_set_image'] ? $v['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $v['goods_name'];?>"  width="<?php echo $SYSINFO['width1'];?>" height="<?php echo $SYSINFO['height1'];?>" /></a></div>
              <div class="summary">
                <h3><a href="<?php echo  goods_url($v['goods_id']);?>"><?php echo  sub_str($v['goods_name'],40);?></a></h3>
                <p>[<?php echo  $i_langpackage->i_info;?>]<?php echo  sub_str(strip_tags($v['goods_intro']),80);?></p>
                <p><span class="left"><?php echo  $i_langpackage->i_shop;?>：<a href="<?php echo  shop_url($v['shop_id']);?>"><?php echo  $v['shop_name'];?></a></span>
				<?php  if($v['rank_id']>2){?>
				<a href="javascript:;" title="<?php echo $i_langpackage->i_approve_company;?>"  class="shop_cert left"><?php echo $i_langpackage->i_approve_company;?></a>
				<?php  } else{?>
				<a href="javascript:;" title="<?php echo $i_langpackage->i_noapprove_company;?>"  class="shop_cert2 left"><?php echo $i_langpackage->i_noapprove_company;?></a>
				<?php }?></p>
              </div>
			  <ul class="attribute">
			    <li class="operate"><a class="more" href="<?php echo  goods_url($v['goods_id']);?>"><?php echo $i_langpackage->i_particular;?></a><a class="buy" href="modules.php?app=user_order&gid=<?php echo $v['goods_id'];?>&v=1"><?php echo $i_langpackage->i_buy;?></a><a class="more" href="javascript:;" onclick="initFloatTips('<?php echo $v['goods_id'];?>','<?php echo sub_str($v['goods_name'],10);?>',1)">对比</a></li>
			    <li class="place"><?php echo  $areainfo[$v['shop_province']];?>.<?php echo  $areainfo[$v['shop_city']];?><br /><script src="imshow.php?u=<?php echo  $v['user_id'];?>"></script></li>
			    <li class="price">￥<?php echo  $v['goods_price']=='0.00' ? $i_langpackage->i_price_discuss : $v['goods_price'];?></li>
		      </ul>
              <div class="showbox" id="showbox_<?php echo $v['goods_id'];?>" style="display:none">
				<div class="subbox"><img id="showimg_<?php echo $v['goods_id'];?>" src="<?php echo  $v['is_set_image'] ? $v['goods_thumb'] : 'skin/default/images/nopic.gif';?>" width="234" height="234" alt="<?php echo $i_langpackage->i_loading_img;?>" /> </div>
			  </div>
		  </li>
			<?php }?>
		<?php  }else {?>
			<li><?php echo $i_langpackage->i_no_goods;?>！</li>
		<?php }?>
		</ul>
	<div class="page"><span><?php echo  str_replace("{num}",$result['countnum'],$i_langpackage->i_page_num);?></span><a href="<?php echo  category_url($cat_id,$result['firstpage']);?>" ><?php echo  $i_langpackage->i_page_first;?></a><span><a href="<?php echo  category_url($cat_id,$result['prepage']);?>"><?php echo  $i_langpackage->i_page_pre;?></a></span><a href="<?php echo  category_url($cat_id,$result['nextpage']);?>" ><?php echo  $i_langpackage->i_page_next;?></a><a href="<?php echo  category_url($cat_id,$result['lastpage']);?>" ><?php echo  $i_langpackage->i_page_last;?></a><span><?php echo  $i_langpackage->i_page_now;?><?php echo  $result['page'];?>/<?php echo  $result['countpage'];?></span><span><?php echo  str_replace("{num}",$result['countpage'],$i_langpackage->i_page_count);?></span></div>
	</div>
</div>

<!-- main end -->
<div class="main_right2 left10 top5">
    <div class="recommend2">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $i_langpackage->i_boutique;?></h2><span class="right"><a href="search.php?best=1"><?php echo $i_langpackage->i_more;?>>></a></span></div>
        <div class="c_m">
        	<ul>
              <?php  foreach($goods_best as $value) {?>
			    <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $value['goods_name'];?>" /> <span><?php echo  sub_str($value['goods_name'],20,false);?></span></a><label>￥<?php echo  $value['goods_price'];?><?php echo $i_langpackage->i_yan;?></label></li>
			  <?php }?>
			</ul>
        </div>
	</div>
    <div class="rank">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $i_langpackage->i_hot;?></h2><span class="right"><a href="search.php?hot=1"><?php echo $i_langpackage->i_more;?>>></a>&nbsp;</span></div>
        <div class="c_m">
		<ul id="promote_goods" class="cls clear">
		<?php $i=1;?>
		<?php  foreach($goods_hot as $value){?>
			<li onmouseover="promote_change(this)"><span class="num"><?php echo $i;?></span><a href="<?php echo  goods_url($value['goods_id']);?>"><img height="60" width="60" src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" alt="<?php echo  $value['goods_name'];?>" /> <span title="<?php echo  $value['goods_name'];?>"><?php echo  sub_str($value['goods_name'],14,false);?></span></a><div class="price"><span>￥<?php echo $value['goods_price'];?></span></div></li>
			<?php $i++;?>
		<?php }?>
		</ul>
		</div>
	</div>
    <div class="recommend2">
        <div class="head_line"><div class="bg_left"></div><div class="bg_right"></div></div>
        <div class="top"><h2 class="highlight"><?php echo $i_langpackage->i_new;?></h2></div>
        <div class="c_m">
        	<ul>
			<?php  foreach($goodshistory as $value){?>
              <li><a href="<?php echo  goods_url($value['goods_id']);?>"><img src="<?php echo  $value['is_set_image'] ? $value['goods_thumb'] : 'skin/default/images/nopic.gif';?>" /></a><a class="pro_name" href="<?php echo  goods_url($value['goods_id']);?>"><?php echo  sub_str($value['goods_name'],20,false);?></a><label>￥<?php echo $value['goods_price'];?></label></li>
			<?php }?>
        	</ul>
        </div>
	</div>
</div>
<!--main right end-->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>
</form>
<div id="contrastbox" style="z-index:999;position:absolute;right:0; border:1px solid #f1f1f1; background:#fff; width:188px;margin-right:10px;padding:10px 0 12px 13px; ">
	<form action="modules.php?app=contrast" method="POST" target="_blank" id="contrastform">
		<input type="hidden" name="contrast_goods_num" id="contrast_goods_num" value="0" />
		<input type="hidden" name="contrast_goods_id" id="contrast_goods_id" value="" />
		<input type="hidden" name="contrast_goods_name" id="contrast_goods_name" value="" />
		<div id="contrast_goods_name_show"></div>
		<!--<input type="submit" name="sub" value="对比" /> -->
        <div class="control"><a onclick="document.getElementById('contrastform').submit();return false;" href="javascript:;" class="button_4">开始比较</a>
        </div>
	</form>
</div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function showbox(id) {
	document.getElementById("showli_"+id).className = "list_item now";
	document.getElementById("showbox_"+id).style.display = '';
	var showimg = document.getElementById("showimg_"+id);
	if(showimg.alt=='<?php echo $i_langpackage->i_loading_img;?>') {
		ajax("do.php?act=goods_get_imgurl","POST","id="+id,function(data){
			if(data) {
				showimg.src = data;
				showimg.alt = '';
			}
		});
	}
}
function hidebox(id) {
	document.getElementById("showli_"+id).className = "list_item";
	document.getElementById("showbox_"+id).style.display = 'none';
}
function order_byorder_name(name,order) {
//	alert('123');
	document.getElementsByName("name")[0].value = name;
	document.getElementsByName("order")[0].value = order;
	var category_form = document.forms['category_form'];
	setTimeout(doSubmit,0)
}
function doSubmit(){
	var category_form = document.getElementById('category_form');
	category_form.submit();
}
function hidden(DIV,div){
	document.getElementById(DIV).style.display = 'none';
	document.getElementById(div).style.display = 'block';
}
function show(DIV,div){
	document.getElementById(DIV).style.display = 'block';
	document.getElementById(div).style.display = 'none';
}
</script>
<script language="JavaScript" type="text/javascript">
var tips; var theTop = 145/*这是默认高度,越大越往下*/;
var old = theTop;
function initFloatTips(goodsid,goodsname,action) {
	tips = document.getElementById('contrastbox');
	document.getElementById('contrastbox').style.display="";
	var goods_num = parseInt(document.getElementById('contrast_goods_num').value);
	var goods_id = document.getElementById('contrast_goods_id').value;
	var goods_name= document.getElementById('contrast_goods_name').value;
	var goods_name_show= document.getElementById('contrast_goods_name_show');
	var goods_id_arr = goods_id.split(",");
	var goods_name_arr = goods_name.split(",");
	if(action==1){
		if(goods_num<5){
			var same_num=0;
			for(i=0;i<goods_id_arr.length;i++){
				if(goods_id_arr[i]==goodsid){
					same_num++;
				}
			}
			if(same_num>0){
				alert("已经选择了此商品");
			}else{
				document.getElementById('contrast_goods_id').value+=goodsid+",";
				document.getElementById('contrast_goods_name').value+=goodsname+",";
				document.getElementById('contrast_goods_num').value=parseInt(goods_num+1);
			}
		}else{
			alert("只能同时对比5个商品");
		}
	}
	if(action==0){
		var str="";
		var namestr="";
		for(i=0;i<goods_id_arr.length;i++){
			if(goods_id_arr[i]!=goodsid&&goods_id_arr[i]!=''){
				str+=goods_id_arr[i]+",";
				namestr+=goods_name_arr[i]+",";
			}
		}
		document.getElementById('contrast_goods_id').value=str;
		document.getElementById('contrast_goods_name').value=namestr;
	}

	var goods_id_arr = document.getElementById('contrast_goods_id').value.split(",");
	var goods_name_arr = document.getElementById('contrast_goods_name').value.split(",");
	goods_name_show.innerHTML="";
	for(i=0;i<goods_id_arr.length-1;i++){
		goods_name_show.innerHTML+="<li>" + "<span><a href='javascript:;' onclick=\"initFloatTips('"+goods_id_arr[i]+"','"+goods_name_arr[i]+"',0)\"></a></span>" + goods_name_arr[i]+"</li>";
	}
	document.getElementById('contrast_goods_num').value=goods_id_arr.length-1;
	if(document.getElementById('contrast_goods_num').value==0){
		document.getElementById('contrastbox').style.display="none";
	}
	moveTips();
}
function moveTips() {
	var tt=55;
	if (window.innerHeight) {
		pos = window.pageYOffset
	}else if (document.documentElement && document.documentElement.scrollTop) {
		pos = document.documentElement.scrollTop
	}else if (document.body) {
		pos = document.body.scrollTop;
	}
	pos=pos-tips.offsetTop+theTop;
	pos=tips.offsetTop+pos/10;
	if (pos < theTop) {
		pos = theTop
	}
	if (pos != old) {
		tips.style.top = pos+"px";
		tt=10;
	}
	old = pos;
	setTimeout(moveTips,tt);
}
</script>
</body>
</html><?php } ?>