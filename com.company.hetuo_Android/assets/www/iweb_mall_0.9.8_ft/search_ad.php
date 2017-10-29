<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/search_ad.html
 * 如果您的模型要进行修改，请修改 models/search_ad.php
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
if(filemtime("templates/default/search_ad.html") > filemtime(__file__) || (file_exists("models/search_ad.php") && filemtime("models/search_ad.php") > filemtime(__file__)) ) {
	tpl_engine("default","search_ad.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require("foundation/module_areas.php");

//引入语言包
$i_langpackage = new indexlp;
$m_langpackage = new moduleslp;

/* 用户信息处理 */
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

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_category = $tablePreStr."category";
$t_goods = $tablePreStr."goods";
$t_index_images = $tablePreStr."index_images";
$t_brand = $tablePreStr."brand";
$t_article = $tablePreStr."article";
$t_areas = $tablePreStr."areas";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result_category = $dbo->getRs($sql_category);
$CATEGORY = array();
foreach($result_category as $v) {
	$CATEGORY[$v['parent_id']][$v['cat_id']] = $v;
}

$sql_brand = "select * from $t_brand where is_show=1";
$brand_rs = $dbo->getRs($sql_brand);

$areas_info = get_areas_info($dbo,$t_areas);

//引入语言包
$i_langpackage = new indexlp;

	$header['title'] = '高级搜索';
	$header['keywords'] = '高级搜索';

$header['description'] = $SYSINFO['sys_description'];

$nav_selected=1;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo  $header['title'];?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="<?php echo  $header['keywords'];?>" />
<meta name="description" content="<?php echo  $header['description'];?>" />
<base href="<?php echo  $baseUrl;?>" />
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<!--search end -->
<div class="path"><?php echo $i_langpackage->i_location;?>：<a href="index.php"><?php echo $i_langpackage->i_index;?></a> > <?php echo $i_langpackage->i_serach_ad;?></div>
<div class="pro_class">
<FORM action="search.php" method=get>
<TABLE cellSpacing=8 cellPadding=0 width="100%" align=center border=0>
  <TR>
    <TD align=right width="22%"><?php echo $i_langpackage->i_keywords;?>：</TD>
    <TD width="78%"><INPUT class=InputBorder id=keywords maxLength=120 size=40 
      name="k"></TD></TR>
  <TR>
    <TD align=right><?php echo $i_langpackage->i_category;?>：</TD>
    <TD><SELECT name="cid">
     <OPTION value="" ></OPTION>
    <?php foreach($CATEGORY[0] as $cat){?>
     <OPTION value="<?php echo $cat['cat_id'];?>"   ><?php echo  $cat['cat_name'];?></OPTION>
    <?php }?>
    </SELECT> </TD></TR>
  <TR>
    <TD align=right><?php echo $i_langpackage->i_brand;?>：</TD>
    <TD><SELECT name="brand" >
     <OPTION value="" ></OPTION>
    <?php  foreach($brand_rs as $value){?>
     <OPTION value=<?php echo $value['brand_id'];?> ><?php echo $value['brand_name'];?></OPTION>
    <?php }?>
	</SELECT> </TD></TR>
  <TR>
    <TD align=right><?php echo $i_langpackage->i_price;?>：</TD>
    <TD><INPUT maxLength=8 size=10 name=min_price> - <INPUT maxLength=8 size=10 name=max_price> </TD></TR>
  <TR>
    <TD align=right><?php echo $i_langpackage->i_in_area;?>：</TD>
    <TD><span id="user_country"><select name="country" onchange="areachanged(this.value,0);">
						<option value='0'><?php echo  $m_langpackage->m_select_country;?></option>
					 <?php   foreach($areas_info[0] as $v){?>
						<option value="<?php echo  $v['area_id'];?>" <?php  if($v['area_id']==1){echo 'selected';}?>><?php echo  $v['area_name'];?></option>
					<?php }?>
					</select></span>
					<span id="user_province">
					<select name="province" onchange="areachanged(this.value,1);">
						<option value='0'><?php echo  $m_langpackage->m_select_province;?></option>
					<?php foreach($areas_info[1] as $v){?>
						<option value="<?php echo  $v['area_id'];?>" ><?php echo  $v['area_name'];?></option>
					<?php 
					}?>
					</select>
					</span>
					<span id="user_city">
					
					</span>
					<span id="user_district">
					
					</span></TD></TR>
  <TR>
    <TD align=right> </TD>
    <TD height="40"><input class="button" type="submit" name="button" id="button" value="<?php echo $i_langpackage->i_post;?>" />
    </TD></TR>
  </TABLE>
</FORM>
</div>
<div class="top5 clear"></div>
<!-- main end -->
<!--main right end-->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>

<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function areachanged(value,type){
	if(value > 0) {
		ajax("do.php?act=ajax_areas","POST","value="+value+"&type="+type,function(return_text){
			var return_text = return_text.replace(/[\n\r]/g,"");
			if(return_text==""){
				alert("<?php echo  $m_langpackage->m_select_again;?>！");
			} else {
				if(type==0) {
					document.getElementById("user_province").innerHTML = return_text;
					show("user_province");
					hide("user_city");
					hide("user_district");
				} else if(type==1) {
					document.getElementById("user_city").innerHTML = return_text;
					show("user_city");
					hide("user_district");
				} else if(type==2) {
					document.getElementById("user_district").innerHTML = return_text;
					show("user_district");
				}
			}		
		});
	} else {
		if(type==2) {
			hide("user_district");
		} else if(type==1) {
			hide("user_district");
			hide("user_city");
		} else if(type==0) {
			hide("user_district");
			hide("user_city");
			hide("user_province");
		}
	}
}

function hide(id) {
	document.getElementById(id).style.display = 'none';
}
function show(id) {
	document.getElementById(id).style.display = '';
}
</script>
</body>
</html>
			<?php } ?>