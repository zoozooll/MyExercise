<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_news.php");

//引入语言包
$a_langpackage=new adminlp;
//
////定义读操作
//dbtarget('r',$dbServs);
//$dbo=new dbex;

//数据表定义区
//$t_article = $tablePreStr."article";
//$t_article_cat = $tablePreStr."article_cat";
//
//$cat_info = get_news_cat_list($dbo,$t_article_cat);

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
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_article_management;?> &gt;&gt; <?php echo $a_langpackage->a_add_category; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left" ><?php echo $a_langpackage->a_add_category; ?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=news_catlist"><?php echo $a_langpackage->a_category_list; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=news_catadd" method="post" onsubmit="return checkForm();">
		<table class="form-table">
			<tr>
				<td width="70px" class="left"><?php echo $a_langpackage->a_category_name; ?>：</td>
				<td class="left"><input class="small-text" type="text" name="cat_name" value="" style="width:200px;" /></td>
			</tr>
			<tr>
				<td width="70px" class="left"><?php echo $a_langpackage->a_category_sort; ?>：</td>
				<td class="left"><input class="small-text" type="text" name="sort_order" value="" style="width:200px;" /></td>
			</tr>
			<tr><td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_category_add; ?>" /></td></span></tr>
		</table>
		</form>
	  </div>
	 </div>
	</div>
</div>
<script language="JavaScript">
<!--
function checkForm() {
	var cat_name = document.getElementsByName("cat_name")[0];

	if(cat_name.value=='') {
		alert("<?php echo $a_langpackage->a_news_title_notnone; ?>");
		return false;
	}
	return true;
}
//-->
</script>
</body>
</html>