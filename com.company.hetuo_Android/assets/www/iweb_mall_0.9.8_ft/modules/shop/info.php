<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/info.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/info.php
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
if(filemtime("templates/default/modules/shop/info.html") > filemtime(__file__) || (file_exists("models/modules/shop/info.php") && filemtime("models/modules/shop/info.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/info.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_areas.php");
require("foundation/module_shop.php");
require("foundation/module_users.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_areas = $tablePreStr."areas";
$t_shop_info = $tablePreStr."shop_info";
$t_users = $tablePreStr."users";
$t_privilege = $tablePreStr."privilege";
$t_user_rank = $tablePreStr."user_rank";

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

$shop_info = get_shop_info($dbo,$t_shop_info,$shop_id);
$areas_info = get_areas_info($dbo,$t_areas);
$user_info = get_user_info($dbo,$t_users,$shop_id);

$rank_info = get_userrank_info($dbo,$t_user_rank,$user_info['rank_id']);
$privilege = unserialize($rank_info['privilege']);
$flag ='';
foreach ($privilege as $key =>$vlaue){
	if ($key =='9'){
		$flag ='1';
	}
}
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/default_small.gif',
	'bigimgurl' => 'skin/default/images/default.gif',
	'tpltag' => 'default',
	'tplname' => '默认模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/green_small.gif',
	'bigimgurl' => 'skin/default/images/green.gif',
	'tpltag' => 'green',
	'tplname' => '绿色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/blue_small.gif',
	'bigimgurl' => 'skin/default/images/blue.gif',
	'tpltag' => 'blue',
	'tplname' => '蓝色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/red_small.gif',
	'bigimgurl' => 'skin/default/images/red.gif',
	'tpltag' => 'red',
	'tplname' => '红色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/purple_small.gif',
	'bigimgurl' => 'skin/default/images/purple.gif',
	'tpltag' => 'purple',
	'tplname' => '紫色模板'
);
$shoptemplate_arr[] = array(
	'imgurl' => 'skin/default/images/gray_small.gif',
	'bigimgurl' => 'skin/default/images/gray.gif',
	'tpltag' => 'gray',
	'tplname' => '灰色模板'
);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
.red{color:red;}
.templageimg span{float:left; display:block; text-align:left; margin-left:1px;}
.templageimg img{border:2px solid #eee; cursor:pointer;}
</style>
</head>
<script type="text/javascript" src="servtools/nicedit/nicEdit.js"></script>
<script type="text/javascript">
bkLib.onDomLoaded(function() {
	new nicEditor({iconsPath : 'servtools/nicedit/nicEditorIcons.gif'}).panelInstance('shop_intro');
});
</script>
<body <?php if($SYSINFO['map']=='true') {?> onload="initialize()" <?php }?>>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_shop_info;?></h3></div>
		<form action="do.php?act=shop_info" method="post" name="form_shop_info" onsubmit="return checkForm();" enctype="multipart/form-data">
		<table width="98%" class="form_table">
			<tr>
				<?php  if($SYSINFO['sys_domain']) {?>
					<?php  if($flag) {?>
						<td class="textright"><?php echo  $m_langpackage->m_domain;?>：</td>
						<td><input type="text" name="shop_domain" value="shop<?php echo  $shop_info['shop_id'];?>" disabled style="width:250px;" maxlength="50" /></td>
					<?php }?>
				<?php }?>
			</tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shop_name;?>：</td>
				<td><input type="text" name="shop_name" value="<?php echo  $shop_info['shop_name'];?>" style="width:250px;" maxlength="50" />
					<span class="red">*</span></td>
			</tr>
			<tr>
				<td class="textright"><?php echo  $m_langpackage->m_stayarea;?>：</td>
				<td>
					<span id="shop_country"><select name="country" onchange="areachanged(this.value,0);">
						<option value='0'><?php echo  $m_langpackage->m_select_country;?></option>
					<?php  foreach($areas_info[0] as $v) {?>
						<option value="<?php echo  $v['area_id'];?>"
							<?php  if($v['area_id']==$shop_info['shop_country']){echo 'selected';}?>><?php echo  $v['area_name'];?></option>
					<?php }?>
					</select></span>
					<span id="shop_province">
					<?php  if($shop_info['shop_country']) {?>
					<select name="province" onchange="areachanged(this.value,1);">
						<option value='0'><?php echo  $m_langpackage->m_select_province;?></option>
					<?php  foreach($areas_info[1] as $v) {
							if($v['parent_id'] == $shop_info['shop_country']) {?>
						<option value="<?php echo  $v['area_id'];?>" <?php  if($v['area_id']==$shop_info['shop_province']){echo 'selected';}?>><?php echo  $v['area_name'];?></option>
					<?php }?>
					<?php }?>
					<?php }?>
					</select>
					<span id="shop_city"><?php  if($shop_info['shop_province']) {?>
					<select name="city" onchange="areachanged(this.value,2);">
						<option value='0'><?php echo  $m_langpackage->m_select_city;?></option>
					<?php  foreach($areas_info[2] as $v) {
							if($v['parent_id'] == $shop_info['shop_province']) {?>
						<option value="<?php echo  $v['area_id'];?>" <?php   if($v['area_id']==$shop_info['shop_city']){echo 'selected';}?>>
						<?php echo  $v['area_name'];?></option>
					<?php }?>
					<?php }?></select>
					<?php }?></span>
					<span id="shop_district"><?php  if($shop_info['shop_city']) {?>
					<select name="district">
						<option value='0'><?php echo  $m_langpackage->m_select_district;?></option>
					<?php  foreach($areas_info[3] as $v) {
							if($v['parent_id'] == $shop_info['shop_city']) {?>
						<option value="<?php echo  $v['area_id'];?>" <?php if($v['area_id']==$shop_info['shop_district']){echo 'selected';}?>>
						<?php echo  $v['area_name'];?></option>
					<?php }?>
					<?php }?></select>
					<?php }?></span>
				<span class="red">*</span></td>
			</tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_address;?>:</td>
				<td><input type="text" name="shop_address" value="<?php echo  $shop_info['shop_address'];?>" style="width:250px;" maxlength="200" /> <span class="red">*</span>
				<?php if($SYSINFO['map']=='true') {?>
				&nbsp;【<a onclick="discontrol('map_canvas',this)" href="#"><?php echo  $m_langpackage->m_open_map;?></a>】
				<div id="map_canvas" style="width:600px; height:400px;"></div>
				<?php }?>
				</td>
			</tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shop_management;?>:</td>
				<td><input type="text" name="shop_management" value="<?php echo  $shop_info['shop_management'];?>" style="width:250px;" maxlength="200" /> <span class="red">*</span></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shop_intro;?>:</td>
				<td>
			<textarea name="shop_intro" id="shop_intro" cols="75" rows="5"><?php echo  $shop_info['shop_intro'];?></textarea>
			</td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_select_template;?>:</td><td class="templageimg">
			<?php  foreach($shoptemplate_arr as $v) {?>
				<span><img src="<?php echo  $v['imgurl'];?>" width="95" alt="<?php echo  $v['tplname'];?>" onclick="wshowimg('<?php echo  $v['bigimgurl'];?>')" onmouseover="imgmover(this)" onmouseout="imgmout(this)" /><br /><input type="radio" name="shop_template" value="<?php echo  $v['tpltag'];?>" <?php  if($shop_info['shop_template']==$v['tpltag']) {?> checked<?php }?> /> <?php echo  $v['tplname'];?></span>
			<?php }?>
			</td></tr>

			<tr style="display:none;"><td class="textright"><?php echo  $m_langpackage->m_shop_introimg;?>:</td>
			<td><input type="file" name="attach_images[]" /> <?php  if($shop_info['shop_images'])
			{ echo "<img src='".$shop_info['shop_images']."' height='80' />";?><?php }?> <?php echo  $m_langpackage->m_shop_introimg_msg;?></td></tr>

			<tr><td class="textright"><?php echo  $m_langpackage->m_shop_logoimg;?>:</td>
			<td><input type="file" name="attach_logo[]" /> <?php  if($shop_info['shop_logo'])
			{ echo "<img src='".$shop_info['shop_logo']."' height='80' />";?><?php }?> <?php echo  $m_langpackage->m_shop_logoimg_msg;?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shop_bannerimg;?>:</td>
			<td><input type="file" name="attach_template[]" /> <?php  if($shop_info['shop_template_img'])
			{echo "<img src='".$shop_info['shop_template_img']."' height='80' />";?><?php }?> <?php echo  $m_langpackage->m_shop_bannerimg_msg;?></td></tr>
			<tr><td colspan="2" align="center"><input type="hidden" name="shop_id" value="<?php echo  $shop_id;?>" />
			<input type="submit" name="submit" value="<?php echo  $m_langpackage->m_edit_shop;?>" /></td></tr>

			<input type="hidden" name="now_x" id="now_x" value="<?php echo $shop_info['map_x'];?>" />
			<input type="hidden" name="now_y" id="now_y" value="<?php echo $shop_info['map_y'];?>" />
			<input type="hidden" name="now_zoom" id="now_zoom" value="<?php echo $shop_info['map_zoom'];?>" />
		</table>
		</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</div>
<div id="showimg" style="display:none; width:408px; text-align:center; border:5px solid #F6A248; position:absolute; padding:4px; background:#fff; top:200px;"><img id="imgsrc" src="skin/default/images/shop_template_default_big.gif" width="400" /></div>
<div style="width:0px; height:0px; overflow:hidden;"><input type="input" id="hiddeninput" onblur="whideimg()" /></div>
<?php if($SYSINFO['map']=='true') {?>
<script src="http://maps.google.com/maps?file=api&v=2.x&key=<?php echo $SYSINFO['map_key'];?>" type="text/javascript"></script>
<?php }?>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function imgmover(obj) {
	obj.style.border = '2px solid #E38016';
}

function imgmout(obj) {
	obj.style.border = '2px solid #eee';
}

function wshowimg(v) {
	var width = document.body.clientWidth;
	var showimg = document.getElementById("showimg");
	var imgsrc = document.getElementById("imgsrc");

	var left = "100";
	if(width) {
		left = (width-400)/2;
	}
	showimg.style.left = left+"px";
	showimg.style.display = '';
	imgsrc.src = v;
	document.getElementById("hiddeninput").focus();
}

function whideimg() {
	var showimg = document.getElementById("showimg");
	showimg.style.display = 'none';
}

function areachanged(value,type){
	if(value > 0) {
		ajax("do.php?act=ajax_areas","POST","value="+value+"&type="+type,function(return_text){
			return_text = return_text.replace(/[\n\r]/g,"");
			if(return_text==""){
				alert("<?php echo  $m_langpackage->m_select_again;?>");
			} else {
				if(type==0) {
					document.getElementById("shop_province").innerHTML = return_text;
					show("shop_province");
					hide("shop_city");
					hide("shop_district");
				} else if(type==1) {
					document.getElementById("shop_city").innerHTML = return_text;
					show("shop_city");
					hide("shop_district");
				} else if(type==2) {
					document.getElementById("shop_district").innerHTML = return_text;
					show("shop_district");
				}
			}
		});
	} else {
		if(type==2) {
			hide("shop_district");
		} else if(type==1) {
			hide("shop_district");
			hide("shop_city");
		} else if(type==0) {
			hide("shop_district");
			hide("shop_city");
			hide("shop_province");
		}
	}
}

function hide(id) {
	document.getElementById(id).style.display = 'none';
}
function show(id) {
	document.getElementById(id).style.display = '';
}

function checkForm() {
	var shop_name = document.getElementsByName("shop_name")[0];
	var shop_address = document.getElementsByName("shop_address")[0];
	var shop_management = document.getElementsByName("shop_management")[0];

	if(shop_name.value=='') {
		alert("<?php echo  $m_langpackage->m_shopname_notnone;?>");
		shop_name.focus();
		return false;
	} else if(document.getElementsByName("country")[0].value==0) {
		alert("<?php echo  $m_langpackage->m_select_countrypl;?>");
		return false;
	} else if(document.getElementsByName("province")[0].value==0) {
		alert("<?php echo  $m_langpackage->m_select_provincepl;?>");
		return false;
	} else if(document.getElementsByName("city")[0].value==0) {
		alert("<?php echo  $m_langpackage->m_select_citypl;?>");
		return false;
	} else if(document.getElementsByName("district")[0].value==0) {
		alert("<?php echo  $m_langpackage->m_select_districtpl;?>");
		return false;
	} else if(shop_address.value=='') {
		alert("<?php echo  $m_langpackage->m_address_notnone;?>");
		shop_name.focus();
		return false;
	} else if(shop_management.value=='') {
		alert("<?php echo  $m_langpackage->m_shopmanagement_notnone;?>");
		shop_management.focus();
		return false;
	}
	return true;
}

<?php if($SYSINFO['map']=='true') {?>
// 地图处理开始
var now_x = <?php echo $shop_info['map_x'];?>;
var now_y = <?php echo $shop_info['map_y'];?>;
var now_zoom = <?php echo $shop_info['map_zoom'];?>;

if(now_x=='0' && now_y=='0'){
	var now_x = '116.39328002929687';
	var now_y = '39.89709437260048';
	var now_zoom = '5';
}

function initialize() {
	if (GBrowserIsCompatible()) {
		var map = new GMap2(document.getElementById("map_canvas"));
		var center = new GLatLng(now_y, now_x);
		map.setCenter(center, now_zoom);

		var point = new GLatLng(now_y,now_x);
		var marker = new GMarker(point);
		map.addOverlay(marker);

		GEvent.addListener(map,"click", function(overlay,latlng) {
			if(latlng) {
				var point = new GLatLng(latlng.y,latlng.x); // 根据经纬度创建点
				var marker = new GMarker(point);			// 创建标注
				map.clearOverlays();						// 清除现有地图上的所有标注
				map.addOverlay(marker);						// 添加新标注

				now_x = latlng.x;
				now_y = latlng.y;
				now_zoom = map.getZoom();

				document.getElementById('now_x').value=now_x;
				document.getElementById('now_y').value=now_y;
				document.getElementById('now_zoom').value=now_zoom;
			}
		});

		map.addControl(new GSmallMapControl());
		map.addControl(new GMapTypeControl());
	}
	document.getElementById("map_canvas").style.display = "none";
}

function discontrol(itemid,obj)
{
	if(document.getElementById(itemid).style.display=='') {
		obj.innerHTML = '点击打开地图，设置我的位置';
		document.getElementById(itemid).style.display="none";
	} else {
		obj.innerHTML = '点击关闭地图，设置完毕';
 		document.getElementById(itemid).style.display="";
	}
}
// 地图处理结束
<?php }?>

//-->
</script>

</body>
</html><?php } ?>