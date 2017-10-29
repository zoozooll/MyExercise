<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/category.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/category.php
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
if(filemtime("templates/default/modules/shop/category.html") > filemtime(__file__) || (file_exists("models/modules/shop/category.php") && filemtime("models/modules/shop/category.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/category.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");

require("foundation/module_shop.php");
//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_shop_category = $tablePreStr."shop_category";

//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);
$category_list = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$category_list_new = array();
if(!empty($category_list)) {
	foreach($category_list as $v) {
		$category_list_new[$v['shop_cat_id']]['shop_cat_id'] = $v['shop_cat_id'];
		$category_list_new[$v['shop_cat_id']]['shop_cat_name'] = $v['shop_cat_name'];
		$category_list_new[$v['shop_cat_id']]['parent_id'] = $v['parent_id'];
		$category_list_new[$v['shop_cat_id']]['shop_cat_unit'] = $v['shop_cat_unit'];
		$category_list_new[$v['shop_cat_id']]['sort_order'] = $v['sort_order'];
	}
}
unset($category_list);

function get_sub_category ($category_list,$parent_id) {
	$array = array();
	foreach($category_list as $k=>$v) {
		if($v['parent_id']==$parent_id) {
			$array[$k] = $v;
		}
	}
	return $array;
}
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
.edit span{background:#FFF2E6;}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><span style="float:right; margin-right:15px;"><a href="modules.php?app=shop_category_add" style="color:#F67A06;">
			<?php echo  $m_langpackage->m_add_ucategory;?></a>&nbsp;&nbsp;</span><?php echo  $m_langpackage->m_ucategory;?></h3></div>
		<table width="98%" class="form_table">
			<tr class="center"><th><?php echo  $m_langpackage->m_category_name;?></th><th width="60"><?php echo  $m_langpackage->m_number_unit;?></th>
				<th width="40"><?php echo  $m_langpackage->m_sort;?></th><th width="200"><?php echo  $m_langpackage->m_manage;?></th></tr>
			<?php if(empty($category_list_new)){?>
			<tr class="center"><td colspan="4"><?php echo  $m_langpackage->m_nocat_addnow;?></td></tr>
			<?php 	} else {
				$category_0 = get_sub_category($category_list_new,0);
				foreach($category_0 as $v) {?>
			<tr>
				<td><img src="skin/default/images/menu_minus.gif" style="margin-left:1em" />
				<span><a href="#?<?php echo  $v['shop_cat_id'];?>"><?php echo  $v['shop_cat_name'];?></a></span>
				</td>
				<td class="center edit" id="unit_id_<?php echo  $v['shop_cat_id'];?>"><span onclick="edit_unit(this,<?php echo  $v['shop_cat_id'];?>)">&nbsp;<?php echo  $v['shop_cat_unit'];?>&nbsp;</span></td>
				<td class="center edit" id="cat_id_<?php echo  $v['shop_cat_id'];?>"><span onclick="edit_sort(this,<?php echo  $v['shop_cat_id'];?>)">&nbsp;<?php echo  $v['sort_order'];?>&nbsp;</span></td>
				<td class="center"><a href="modules.php?app=shop_category_add&id=<?php echo  $v['shop_cat_id'];?>"><?php echo  $m_langpackage->m_add_subcategory;?></a> <a href="modules.php?app=shop_category_edit&id=<?php echo  $v['shop_cat_id'];?>"><?php echo  $m_langpackage->m_edit;?></a> <a href="javascript:del_cat('<?php echo  $v['shop_cat_id'];?>')"><?php echo  $m_langpackage->m_del;?></a></td>
			</tr>
			<?php 
					$category_sub = get_sub_category($category_list_new,$v['shop_cat_id']);
					if(!empty($category_sub)){
						foreach($category_sub as $value) {?>
					<tr>
						<td><img src="skin/default/images/menu_minus.gif" style="margin-left:2em" />
						<span><a href="#?<?php echo  $value['shop_cat_id'];?>"><?php echo  $value['shop_cat_name'];?></a></span>
						</td>
						<td class="center edit" id="unit_id_<?php echo  $value['shop_cat_id'];?>"><span onclick="edit_unit(this,<?php echo  $value['shop_cat_id'];?>)">&nbsp;<?php echo  $value['shop_cat_unit'];?>&nbsp;</span></td>
						<td class="center edit" id="cat_id_<?php echo  $value['shop_cat_id'];?>"><span onclick="edit_sort(this,<?php echo  $value['shop_cat_id'];?>)">&nbsp;<?php echo  $value['sort_order'];?>&nbsp;</span></td>
						<td class="center"><a href="modules.php?app=shop_category_edit&id=<?php echo  $value['shop_cat_id'];?>"><?php echo  $m_langpackage->m_edit;?></a> <a href="javascript:del_cat('<?php echo  $value['shop_cat_id'];?>')"><?php echo  $m_langpackage->m_del;?></a></td>
					</tr>
					 <?php }?>
					<?php }?>
				<?php }?>
			<?php }?>
			<tr><td colspan="4">&nbsp;&nbsp;<?php echo  $m_langpackage->m_remark_edit;?></td></tr>
		</table>
        </div>
    </div>
<div class="clear"></div>
<?php  require("modules/footer.php");?>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var sort_value,unit_value;
function edit_sort(span,id) {
	obj = document.getElementById("cat_id_"+id);
	sort_value = span.innerHTML;
	sort_value = sort_value.replace(/&nbsp;/ig,"");
	obj.innerHTML = '<input style="width:35px" type="text" id="input_cat_id_' + id + '" value="' + sort_value + '" onblur="edit_sort_post(this,' + id + ')" maxlength="2"  />';
	document.getElementById("input_cat_id_"+id).focus();
}

function edit_sort_post(input,id) {
	var obj = document.getElementById("cat_id_"+id);
	if(isNaN(input.value)) {
		alert("<?php echo  $m_langpackage->m_input_num;?>");
		obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
		return ;
	}
	if(sort_value==input.value) {
		obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
	} else {
		ajax("do.php?act=shop_catsort_edit","POST","id="+id+"&v="+input.value,function(data){
			if(data==1) {
				obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_sort(this,' + id + ')">&nbsp;' + sort_value + '&nbsp;</span>';
			}
		});
	}
}

function edit_unit(span,id){
	obj = document.getElementById("unit_id_"+id);
	unit_value = span.innerHTML;
	unit_value = unit_value.replace(/&nbsp;/ig,"");
	obj.innerHTML = '<input style="width:35px" type="text" id="input_unit_id_' + id + '" value="' + unit_value + '" onblur="edit_unit_post(this,' + id + ')" maxlength="8" />';
	document.getElementById("input_unit_id_"+id).focus();
}

function edit_unit_post(input,id) {
	var obj = document.getElementById("unit_id_"+id);
	if(unit_value==input.value) {
		obj.innerHTML = '<span onclick="edit_unit(this,' + id + ')">&nbsp;' + unit_value + '&nbsp;</span>';
	} else {
		ajax("do.php?act=shop_catunit_edit","POST","id="+id+"&v="+input.value,function(data){
			if(data==1) {
				obj.innerHTML = '<span onclick="edit_unit(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_unit(this,' + id + ')">&nbsp;' + unit_value + '&nbsp;</span>';
			}
		});
	}
}

function del_cat(id){
	if(confirm("<?php echo  $m_langpackage->m_sure_delcat;?>")) {
		location.href="do.php?act=shop_category_del&id="+id;
	}
}
//-->
</script>
</body>
</html><?php } ?>