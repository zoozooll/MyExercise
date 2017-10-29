<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/groupbuy/add.html
 * 如果您的模型要进行修改，请修改 models/modules/groupbuy/add.php
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
if(filemtime("templates/default/modules/groupbuy/add.html") > filemtime(__file__) || (file_exists("models/modules/groupbuy/add.php") && filemtime("models/modules/groupbuy/add.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/groupbuy/add.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_category.php");
require("foundation/module_goods.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_category = $tablePreStr."shop_category";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$goods_info = array(
	'goods_name'	=> '',
	'cat_id'		=> 0,
	'ucat_id'		=> 0,
	'goods_intro'	=> '',
	'goods_number'	=> 99,
	'keyword'		=> '',
	'goods_price'	=> '0.00',
	'is_on_sale'	=> 1,
	'is_best'		=> 0,
	'is_new'		=> 0,
	'is_hot'		=> 0,
	'is_promote'	=> 0,
	'goods_wholesale'=> '',
	'transport_price'=> '0.00'
);

$shop_category = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$html_shop_category = html_format_shop_category($shop_category,$goods_info['ucat_id']);

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<script type='text/javascript' src='servtools/calendar.js'></script>
<style type="text/css">
.red { color:red; }
</style>

</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart" style="width:765px;">
			<div class="title_uc"><h3><span style="float:right; margin-right:15px;"><a href="modules.php?app=groupbuy_list" style="color:#F67A06;">
			<?php echo $m_langpackage->m_group_list;?></a>&nbsp;&nbsp;</span><?php echo $m_langpackage->m_add_groupbuy;?></h3>
			</div>
		<form action="do.php?act=groupbuy_add" method="post" name="from1" onsubmit="return checkform();" enctype="multipart/form-data">
		<table width="98%" class="form_table">
			<tr>
				<td class="textright"><?php echo $m_langpackage->m_group_name;?>：</td>
				<td align="left"><input type="text" name="groupbuy_name" value="" style="width:350px;" maxlength="200" /> <span class="red">*</span></td>
			</tr>
			<tr>
				<td class="textright"><?php echo $m_langpackage->m_sta_time;?>：</td>
				<td align="left"><INPUT type='text' AUTOCOMPLETE=off size=10 name='start_time' onclick='getDateString(this,oCalendarChs)' id='start_time' value=''>
				<?php echo $m_langpackage->m_end_time;?>：<INPUT type='text' AUTOCOMPLETE=off size=10 name='end_time' onclick='getDateString(this,oCalendarChs)' id='end_time' value=''> <span class="red">*</span></td>
			</tr>
			<tr>
				<td class="textright"><?php echo $m_langpackage->m_group_shows;?>：</td>
				<td align="left"><textarea name="groupbuy_explain" style="width:350px; height:80px;"></textarea> </td>
			</tr>
			<tr>
				<td class="textright"><?php echo $m_langpackage->m_select_products;?>：</td>
				<td align="left">
				<input type="text" onclick="discontrol('goods_select',this)" id="goods_name" name="goods_name" value="" style="width:350px;" maxlength="200" /> <span class="red">*</span><br />
				<input type="hidden"id="goods_id" name="goods_id" value="" />
				<div id="goods_select" style="width:600px; height:200px; display:none;">
					<table>
						<tr>
							<td><?php echo $m_langpackage->m_products_name;?>：</td>
							<td><input type="text" id="goodsselect_name" name="goodsselect_name" value="" style="width:300px;" maxlength="200" /></td>
						</tr>
						<tr>
							<td><?php echo $m_langpackage->m_products_sort;?>：</td>
							<td><select id="shop_category" name="shop_category"><option value=""><?php echo $m_langpackage->m_select_pl;?></option>
							<?php echo $html_shop_category;?>
							</select></td>
						</tr>
						<tr>
							<td></td>
							<td><input type="button" value="<?php echo $m_langpackage->m_select;?>" onclick="get_goods_list()"></td>
						</tr>
						<tr>
							<td><?php echo $m_langpackage->m_select_products;?>：</td>
							<td>
		                    	<span id="goods_list">
			                    	<select  name="goods_list" class="text" style="width:310px;" size="7">
			                    		<option value="">请先从上面搜索</option>
			                    	</select>
		                    	</span>
							</td>
						</tr>
					</table>
				</div>
				</td>
			</tr>
			<tr>
				<td class="textright"><?php echo $m_langpackage->m_group_number;?>：</td>
				<td align="left"><input type="text" name="min_quantity" value="" style="width:150px;" maxlength="200" /> <span class="red">*</span></td>
			</tr>
			<tr>
				<td class="textright"><?php echo $m_langpackage->m_group_price;?>：</td>
				<td align="left"><input type="text" name="spec_price" value="" style="width:150px;" maxlength="200" /> <span class="red">*</span></td>
			</tr>
			<tr>
				<td class="textright"></td>
				<td><input type="submit" name="" value="<?php echo $m_langpackage->m_group_submit;?>"  /> </td>
			</tr>
		</table>
		</form>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
<div id="bgdiv" style="display:none;"></div>
<div id="category_select" style="display:none;">
	<div class="category_title_1"><span onclick="hidebgdiv();"><?php echo $m_langpackage->m_close;?></span><?php echo $m_langpackage->m_plss_select_cateogry;?></div>
	<ul id="select_first" class="ulselect">
		<?php foreach($category_info as $k=>$v){?>
			<?php if($v['parent_id']==0){?>
			<li title="<?php echo  $v['cat_id'];?>"><?php echo  $v['cat_name'];?></li>
			<?php }?>
		<?php }?>
	</ul>
	<ul id="select_second" class="ulselect"></ul>
	<ul id="select_third" class="ulselect"></ul>
	<ul id="select_fourth" class="ulselect"></ul>
	<div class="category_com"><input type="button" value="<?php echo $m_langpackage->m_post;?>" onclick="postcatid();" /></div>
</div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function discontrol(itemid,obj)
{
	if(document.getElementById(itemid).style.display=='') {
		document.getElementById(itemid).style.display="none";
	} else {
 		document.getElementById(itemid).style.display="";
	}
}

function get_goods_list(){
	var goodsselect_name = document.getElementById("goodsselect_name").value;
	var shop_category = document.getElementById("shop_category").value;
	ajax("do.php?act=groupbuy_selectgoods","POST","shop_category="+shop_category+"&goodsselect_name="+goodsselect_name,function(data){
		var str_data = "<select  name='goods_list' class='text' style='width:310px;' size='7'>";
		str_data+=data;
		str_data+="</select>";
		document.getElementById("goods_list").innerHTML = str_data;
	});
}

function select_goods_id(goods_name,goods_id){
	document.getElementById("goods_name").value = goods_name;
	document.getElementById("goods_id").value = goods_id;
	document.getElementById("goods_select").style.display="none";
}

function checkform(){
	var groupbuy_name = document.getElementsByName('groupbuy_name')[0];
	if(groupbuy_name.value==''){
		alert('<?php echo $m_langpackage->m_group_no_name;?>');
		return false;
	}
	var start_time = document.getElementsByName('start_time')[0];
	if(start_time.value==''){
		alert('<?php echo $m_langpackage->m_sta_no_time;?>');
		return false;
	}
	var end_time = document.getElementsByName('end_time')[0];
	if(end_time.value==''){
		alert('<?php echo $m_langpackage->m_end_no_time;?>');
		return false;
	}
	var goods_id = document.getElementsByName('goods_id')[0];
	if(goods_id.value==''){
		alert('<?php echo $m_langpackage->m_products_no_name;?>');
		return false;
	}
	var min_quantity = document.getElementsByName('min_quantity')[0];
	if(min_quantity.value==''){
		alert('<?php echo $m_langpackage->m_group_no_number;?>');
		return false;
	}
	var spec_price = document.getElementsByName('spec_price')[0];
	if(spec_price.value==''){
		alert('<?php echo $m_langpackage->m_group_no_price;?>');
		return false;
	}
}

//-->
</script>
</body>
</html><?php } ?>