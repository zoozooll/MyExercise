<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/shop/category_add.html
 * 如果您的模型要进行修改，请修改 models/modules/shop/category_add.php
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
if(filemtime("templates/default/modules/shop/category_add.html") > filemtime(__file__) || (file_exists("models/modules/shop/category_add.php") && filemtime("models/modules/shop/category_add.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/shop/category_add.html",1);
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

/* 初始化数据 */
$shop_category_info = array(
	'shop_cat_name'	=> '',
	'parent_id'		=> intval(get_args('id')),
	'shop_cat_unit'	=> '',
	'sort_order'	=> 0
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
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
		<div class="bigpart">
		<div class="title_uc"><h3><span style="float:right; margin-right:15px;"><a href="modules.php?app=shop_category" style="color:#F67A06;">
			<?php echo  $m_langpackage->m_ucategory;?></a>&nbsp;&nbsp;</span><?php echo  $m_langpackage->m_add_ucategory;?></h3></div>
		<form action="do.php?act=shop_category_add" method="post" name="shop_category_add" onsubmit="return checkForm();">
		<table width="98%" class="form_table">
			<tr><td class="textright"><?php echo  $m_langpackage->m_category_name;?>:</td>
				<td><input type="text" name="shop_cat_name" value="<?php echo  $shop_category_info['shop_cat_name'];?>" style="width:250px;" maxlength="50" /> <span class="red">*</span></td></tr>
			<tr>
				<td class="textright"><?php echo  $m_langpackage->m_parent_cat;?>:</td>
				<td>
					<select name="parent_id">
						<option value='0'><?php echo  $m_langpackage->m_top_cat;?></option>
					<?php  if(!empty($category_list_new)){
						$category_0 = get_sub_category($category_list_new,0);
						foreach($category_0 as $v)
						{?>
						<option value='<?php echo  $v['shop_cat_id'];?>' <?php if($v['shop_cat_id']==$shop_category_info['parent_id']){?> selected<?php }?>><?php echo  $v['shop_cat_name'];?></option>	
					<?php  }
					}?>
					</select>
				</td>
			</tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_number_unit;?>:</td><td><input type="text" name="shop_cat_unit" value="<?php echo  $shop_category_info['shop_cat_unit'];?>" style="width:70px;" maxlength="8" /></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_sort;?>:</td><td><input type="text" name="sort_order" value="<?php echo  $shop_category_info['sort_order'];?>" style="width:70px;" maxlength="2" /></td></tr>
			<tr><td colspan="2" class="center"><input type="hidden" name="shop_id" value="<?php echo  $shop_id;?>" /><input type="submit" name="submit" value="<?php echo  $m_langpackage->m_add_ucategory;?>" /></td></tr>
		</table>
		</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</div><?php  require("modules/footer.php");?></div>
<script language="JavaScript">
<!--
function checkForm() {
	var shop_cat_name = document.getElementsByName("shop_cat_name")[0];
	if(shop_cat_name.value=='') {
		alert("<?php echo  $m_langpackage->m_ucategory_notnone;?>");
		shop_cat_name.focus();
		return false;
	}
	return true;
}
//-->
</script>
</body>
</html><?php } ?>