<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_news.php");

//引入语言包
$a_langpackage=new adminlp;

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

//数据表定义区
//$t_article = $tablePreStr."article";
$t_article_cat = $tablePreStr."article_cat";

$cat_id = intval(get_args('id'));
if(!$cat_id) { exit($a_langpackage->a_error);}

$sql="select * from $t_article_cat where cat_id=$cat_id";

$row=$dbo->getRow($sql);

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
td span {color:red;}
</style>
<script type="text/javascript" src="../servtools/nicedit/nicEdit.js"></script>
<script type="text/javascript">
bkLib.onDomLoaded(function() {
	new nicEditor({iconsPath : '../servtools/nicedit/nicEditorIcons.gif'}).panelInstance('content');
});
</script>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_article_management;?> &gt;&gt; <?php echo $a_langpackage->a_classification_changes;?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_classification_changes;?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=news_catlist"><?php echo $a_langpackage->a_category_list; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=news_catedit" method="post" onsubmit="return checkForm();">
		<table class="form-table">
		<input type="hidden" name="cat_id" value="<?php echo $cat_id?>" />
			<tr>
				<td width="60px"><?php echo $a_langpackage->a_category_name; ?>：</td>
				<td><input class="small-text" type="text" name="cat_name" value="<?php echo $row['cat_name']?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_category_sort; ?>：</td>
				<td><input type="text" class="small-text" name="sort_order" value="<?php echo $row['sort_order']?>" style="width:200px;" /></td>
			</tr>
			<tr><td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_category_edit; ?>" /></span></td></tr>
		</table>
		</form>
	   </div>
	 </div>
   </div>
</div>
<script language="JavaScript">
<!--
function checkForm() {
	var title = document.getElementsByName("title")[0];

	if(title.value=='') {
		alert("<?php echo $a_langpackage->a_news_title_notnone; ?>");
		title.focus();
		return false;
	} else if(document.getElementsByName("cat_id")[0].value==0) {
		alert("<?php echo $a_langpackage->a_plsselectcategory; ?>");
		return false;
	}
	return true;
}
//-->
</script>
</body>
</html>