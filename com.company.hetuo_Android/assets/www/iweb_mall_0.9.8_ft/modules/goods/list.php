<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/goods/list.html
 * 如果您的模型要进行修改，请修改 models/modules/goods/list.php
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
if(filemtime("templates/default/modules/goods/list.html") > filemtime(__file__) || (file_exists("models/modules/goods/list.php") && filemtime("models/modules/goods/list.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/goods/list.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/ashop_news_category.php");
require("foundation/module_category.php");
require("foundation/module_type.php");

//引入语言包
$m_langpackage=new moduleslp;

$k = short_check(get_args('k'));
$ucat_id = intval(get_args('ucat_id'));

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_brand = $tablePreStr."brand";
$t_goods_types = $tablePreStr."goods_types";
$t_shop_category = $tablePreStr."shop_category";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_goods` where shop_id='$shop_id'";
if($k && $k!=$m_langpackage->m_goods_keyword) {
	$sql .= " and goods_name like '%$k%' ";
}
if($ucat_id) {
	$sql .= " and ucat_id='$ucat_id' ";
}

$sql .= " order by sort_order asc,goods_id desc";

$result = $dbo->fetch_page($sql,10);
$rowset = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$shop_category_info = array();
if($rowset) {
	foreach($rowset as $value) {
		$shop_category_info[$value['shop_cat_id']] = $value['shop_cat_name'];
	}
}

$typeinfo = get_goods_type($dbo,$t_goods_types);

$shop_category = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$html_shop_category = html_format_shop_category($shop_category,$ucat_id);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
.edit span{background:#FFF2E6;}
.search {margin:5px; height:20px; background:#fff; width:90%; padding-left:0px; text-align:left;}
.search input {color:#444;}
td{text-align:left;}
td.img img{cursor:pointer;}
td div.goodsname{line-height:18px; height:36px; font-weight:bold;}
td span.category{color:#FF6600;}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><span style="float:right; margin-right:15px;"><a href="modules.php?app=goods_csv_taobao" style="color:#F67A06;">导入淘宝csv</a>|<a href="modules.php?app=goods_add" style="color:#F67A06;"><?php echo  $m_langpackage->m_add_goods;?></a>&nbsp;&nbsp;</span><?php echo  $m_langpackage->m_goods_list;?></h3></div>
		<div class="search" style="flloat:left;">
		<form action="modules.php" method="get" name="search_form" style="float:left;">
			<select name="ucat_id">
				<option value="0"><?php echo  $m_langpackage->m_ucategory;?></option>
				<?php echo  $html_shop_category;?>
			</select>
			<input type="text" name="k" value="<?php  if(empty($k)) { echo $m_langpackage->m_goods_keyword; } else { echo $k; }?>"
			onfocus="javascript:if(this.value=='<?php echo  $m_langpackage->m_goods_keyword;?>'){this.value=''}"
			onblur="javascript:if(this.value==''){this.value='<?php echo  $m_langpackage->m_goods_keyword;?>'}" />
			<input type="hidden" value="goods_list" name="app" />
			<input type="submit" name="submit" value="<?php echo  $m_langpackage->m_search_goods;?>" />
		</form></div>
		<form action="do.php?act=goods_list" method="post" onsubmit="return submitform();">
		<table width="98%" class="form_table">
			<tr class="center">
				<th width="20"><input type="checkbox" name="c" value="" onclick="checkall(this)" /></th>
				<th width="82"></th>
				<th><?php echo  $m_langpackage->m_goods_name;?></th>
				<th width="70"><?php echo  $m_langpackage->m_price;?></th>
				<th width="28"><?php echo  $m_langpackage->m_on_sale;?></th>
				<th width="40"><?php echo  $m_langpackage->m_sort;?></th>
				<th width="40"><?php echo  $m_langpackage->m_goods_number;?></th>
				<th width="50"><?php echo  $m_langpackage->m_manage;?></th>
		 </tr>
			<?php 
			if(!empty($result['result'])) {
				foreach($result['result'] as $v) {?>
			<tr>
				<td><input type="checkbox" name="checkbox[]" value="<?php echo  $v['goods_id'];?>" /></td>
				<td class="center"><a href="goods.php?id=<?php echo  $v['goods_id'];?>" target="_blank"><img src="<?php echo  $v['goods_thumb'];?>" width="80" height="80" /></a></td>
				<td class="img" align="left" valign="top">
					<div class="goodsname"><a href="goods.php?id=<?php echo  $v['goods_id'];?>" target="_blank" style="color:blue;"><?php echo  $v['goods_name'];?></a></div>
					<?php echo  $m_langpackage->m_custom_categories;?>：<span class="category"><?php if(isset($shop_category_info[$v['ucat_id']])){?><?php echo $shop_category_info[$v['ucat_id']];?><?php  } else {?><?php echo  $m_langpackage->m_undefinition;?><?php }?></span> &nbsp; <?php echo  $m_langpackage->m_goods_type;?>：<span class="category"><?php echo  $typeinfo[$v['type_id']];?></span> <br />
					<?php if($v['lock_flg']==0) {?>
					<?php echo  $m_langpackage->m_best;?>:<?php if($v['is_best']){?><img src="skin/default/images/yes.gif" onclick="toggle_show(this,'best','<?php echo  $v['goods_id'];?>')" /><?php  } else {?><img src="skin/default/images/no.gif" onclick="toggle_show(this,'best','<?php echo  $v['goods_id'];?>')" /><?php }?> &nbsp;
					<?php echo  $m_langpackage->m_promote;?>:<?php if($v['is_promote']){?><img src="skin/default/images/yes.gif" onclick="toggle_show(this,'promote','<?php echo  $v['goods_id'];?>')" /><?php  } else {?><img src="skin/default/images/no.gif" onclick="toggle_show(this,'promote','<?php echo  $v['goods_id'];?>')" /><?php }?> &nbsp;
					<?php echo  $m_langpackage->m_new;?>:<?php if($v['is_new']) {?><img src="skin/default/images/yes.gif" onclick="toggle_show(this,'new','<?php echo  $v['goods_id'];?>')" /><?php  } else {?><img src="skin/default/images/no.gif" onclick="toggle_show(this,'new','<?php echo  $v['goods_id'];?>')" /><?php }?> &nbsp;
					<?php echo  $m_langpackage->m_hot;?>:<?php if($v['is_hot']) {?><img src="skin/default/images/yes.gif" onclick="toggle_show(this,'hot','<?php echo  $v['goods_id'];?>')" /><?php  } else {?><img src="skin/default/images/no.gif" onclick="toggle_show(this,'hot','<?php echo  $v['goods_id'];?>')" /><?php }?>
					<?php }?>
				</td>
				<td class="edit" style="text-align:right;" title="<?php echo  $m_langpackage->m_click_editcontent;?>"
					id="goodspriceid_<?php echo  $v['goods_id'];?>"><span onclick="edit_price(this,<?php echo  $v['goods_id'];?>)">&nbsp;
						<?php echo  $v['goods_price'];?>&nbsp;</span></td>
				<td class="center img">
				<?php if($v['lock_flg']==0) {?>
				<?php if($v['is_on_sale']) {?>
				<img src="skin/default/images/yes.gif" onclick="toggle_show(this,'on_sale','<?php echo  $v['goods_id'];?>')" />
				<?php  } else {?>
				<img src="skin/default/images/no.gif" onclick="toggle_show(this,'on_sale','<?php echo  $v['goods_id'];?>')" />
				<?php }?>
				<?php  } else {?>
				<?php echo  $m_langpackage->m_lock;?>
				<?php }?>
				</td>
				<td class="center edit" title="<?php echo  $m_langpackage->m_click_editcontent;?>" id="goodssortid_<?php echo  $v['goods_id'];?>">
					<span onclick="edit_sort(this,<?php echo  $v['goods_id'];?>)">&nbsp;<?php echo  $v['sort_order'];?>&nbsp;</span></td>
				<td class="center edit" title="<?php echo  $m_langpackage->m_click_editcontent;?>" id="goodsnumberid_<?php echo  $v['goods_id'];?>">
					<span onclick="edit_number(this,<?php echo  $v['goods_id'];?>)">&nbsp;<?php echo  $v['goods_number'];?>&nbsp;</span></td>
				<td class="center">
					<?php if($v['lock_flg']==0) {?>
					<a href="modules.php?app=goods_edit&id=<?php echo  $v['goods_id'];?>"><?php echo  $m_langpackage->m_edit;?></a><br />
					<a href="modules.php?app=goods_gallery&id=<?php echo  $v['goods_id'];?>" <?php if($v['is_set_image']==0) { echo 'style="color:red;"';}?> title="<?php echo  $m_langpackage->m_image_upload;?>"><?php echo  $m_langpackage->m_goods_photo;?></a><br />
					<?php }?>
					<a href="do.php?act=goods_del&id=<?php echo  $v['goods_id'];?>" onclick="return confirm('<?php echo  $m_langpackage->m_sure_delgoods;?>');"><?php echo  $m_langpackage->m_del;?></a>
				</td>
			</tr>
			<?php }?>
			<tr><td colspan="8">
				<input type="submit" name="down" value="<?php echo  $m_langpackage->m_goods_ushelves;?>" />
				<input type="submit" name="up" value="<?php echo  $m_langpackage->m_goods_dshelves;?>" />
			</td></tr>
			<tr><td colspan="8" class="center"><?php  require("modules/page.php");?></td></tr>
			<?php  } else {?>
			<tr><td colspan="8" class="center"><?php echo  $m_langpackage->m_nogoods_list;?></td></tr>
			<?php }?>
		</table>
		</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var inputs = document.getElementsByTagName("input");
function submitform() {
	var status = document.getElementsByName("checkbox");
	var checknum = 0;
	for(var i=0; i<inputs.length; i++) {
		if(inputs[i].type=='checkbox') {
			if(inputs[i].checked) {
				checknum++;
			}
		}
	}
	if(checknum==0) {
		alert("<?php echo  $m_langpackage->m_selceted_one;?>");
		return false;
	}
	return true;
}

function checkall(obj) {
	if(obj.checked) {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = true;
			}
		}
	} else {
		for(var i=0; i<inputs.length; i++) {
			if(inputs[i].type=='checkbox') {
				inputs[i].checked = false;
			}
		}
	}
}

function toggle_show(obj,name,id) {
	var re = /yes/i;
	var src = obj.src;
	var s = 1;
	var searchv = src.search(re);
	if(searchv > 0) {
		s = 0;
	}
	var d = new Date();
	var t = d.getTime();
	ajax("do.php?act=goods_toggle&t="+t,"POST","id="+id+"&s="+s+"&name="+name,function(data){
		if(data=='-1'){
			alert("<?php echo  $m_langpackage->m_over_setnum;?>");
		}else if(data) {
			obj.src = 'skin/default/images/'+data+'.gif';
		}
	});
}


var sort_value,number_value,price_value;
function edit_sort(span,id) {
	obj = document.getElementById("goodssortid_"+id);
	sort_value = span.innerHTML;
	sort_value = sort_value.replace(/&nbsp;/ig,"");
	obj.innerHTML = '<input style="width:35px" type="text" id="input_goodssortid_' + id + '" value="' + sort_value + '" onblur="edit_sort_post(this,' + id + ')"  maxlength="2" />';
	document.getElementById("input_goodssortid_"+id).focus();
}

function edit_sort_post(input,id) {
	var obj = document.getElementById("goodssortid_"+id);
	if(isNaN(input.value)) {
		alert("<?php echo $m_langpackage->m_input_numpl;?>");
		obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
		return ;
	}
	if(sort_value==input.value) {
		obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
	} else {
		ajax("do.php?act=goods_sort_edit","POST","id="+id+"&v="+input.value,function(data){
			if(data==1) {
				obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
			}
		});
	}
}

function edit_number(span,id){
	obj = document.getElementById("goodsnumberid_"+id);
	number_value = span.innerHTML;
	number_value = number_value.replace(/&nbsp;/ig,"");
	obj.innerHTML = '<input style="width:35px" type="text" id="input_goodsnumberid_' + id + '" value="' + number_value + '" onblur="edit_number_post(this,' + id + ')" maxlength="5" />';
	document.getElementById("input_goodsnumberid_"+id).focus();
}

function edit_number_post(input,id) {
	var obj = document.getElementById("goodsnumberid_"+id);
	if(isNaN(input.value)) {
		alert("<?php echo $m_langpackage->m_input_numpl;?>");
		obj.innerHTML = '<span onclick="edit_number(this,' + id + ')">&nbsp;' + number_value + '&nbsp;</span>';
		return ;
	}
	if(number_value==input.value) {
		obj.innerHTML = '<span onclick="edit_number(this,' + id + ')">&nbsp;' + number_value + '&nbsp;</span>';
	} else {
		ajax("do.php?act=goods_number_edit","POST","id="+id+"&v="+input.value,function(data){
			if(data==1) {
				obj.innerHTML = '<span onclick="edit_number(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_number(this,' + id + ')">&nbsp;' + number_value + '&nbsp;</span>';
			}
		});
	}
}

function edit_price(span,id){
	obj = document.getElementById("goodspriceid_"+id);
	price_value = span.innerHTML;
	price_value = price_value.replace(/&nbsp;/ig,"");
	obj.innerHTML = '<input style="width:55px" type="text" id="input_goodspriceid_' + id + '" value="' + price_value + '" onblur="edit_price_post(this,' + id + ')" maxlength="8" />';
	document.getElementById("input_goodspriceid_"+id).focus();
}

function edit_price_post(input,id) {
	var obj = document.getElementById("goodspriceid_"+id);
	if(isNaN(input.value)) {
		alert("<?php echo $m_langpackage->m_input_numpl;?>");
		obj.innerHTML = '<span onclick="edit_price(this,' + id + ')">&nbsp;' + price_value + '&nbsp;</span>';
		return ;
	}
	if(price_value==input.value) {
		obj.innerHTML = '<span onclick="edit_price(this,' + id + ')">&nbsp;' + price_value + '&nbsp;</span>';
	} else {
		ajax("do.php?act=goods_price_edit","POST","id="+id+"&v="+input.value,function(data){
			if(data==1) {
				obj.innerHTML = '<span onclick="edit_price(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_price(this,' + id + ')">&nbsp;' + price_value + '&nbsp;</span>';
			}
		});
	}
}
//-->
</script>
</body>
</html><?php } ?>