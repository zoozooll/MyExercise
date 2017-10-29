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
$t_article = $tablePreStr."article";
$t_article_cat = $tablePreStr."article_cat";

$cat_info = get_news_cat_list($dbo,$t_article_cat);

$news_info = array(
	'cat_id'		=> 0,
	'title'			=> '',
	'content'		=> '',
	'is_link'		=> '',
	'link_url'		=> 'http://',
	'is_show'		=> 1
);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_article_management;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_news_add; ?></a></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_news_add; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=news_list"><?php echo $a_langpackage->a_news_list; ?></a></span></h3>
    <div class="content2">

		<form action="a.php?act=news_add" method="post" onsubmit="return checkForm();">
		<table>
			<tr>
				<td width="80px"><?php echo $a_langpackage->a_select_n_category; ?>：</td>
				<td><select name="cat_id">
					<option value="0"><?php echo $a_langpackage->a_select_n_category; ?></option>
					<?php foreach($cat_info as $value) {?>
					<option value="<?php echo $value['cat_id']; ?>" <?php if($value['cat_id']==$news_info['cat_id']){ echo "selected";} ?> ><?php echo $value['cat_name']; ?></option>
					<?php }?>
				</select> <span id="position_id_message">*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_news_title; ?>：</td>
				<td><input class="small-text" type="text" name="title" value="<?php echo $news_info['title']; ?>" style="width:200px;" /> <span id="asd_name_message">*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_news_content; ?>：</td>
				<td><textarea name="content" id="content" cols="100" rows="5"><?php echo $news_info['content'];?></textarea>
				<iframe name="KindImageIframe" id="KindImageIframe" width="100%" height='30' align="top" allowTransparency="true" scrolling="no" src='m.php?app=upload_form' frameborder=0></iframe></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_news_links; ?>：</td>
				<td><?php echo $a_langpackage->a_use_news_links; ?><input type="checkbox" name="is_link" value="1" <?php if($news_info['is_link']){ echo "checked"; }?> /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_news_links_url; ?>：</td>
				<td><input type="text" name="link_url" value="<?php echo $news_info['link_url'];?>" style="width:250px;" maxlength="200" /></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_news_isshow; ?>：</td>
				<td><input type="checkbox" name="is_show" value="1" <?php if($news_info['is_show']){ echo "checked"; }?> /></td>
			</tr>
			<tr><td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_news_add; ?>" /></span></td></tr>
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
function AddContentImg(ImgName,classId){
	var obj = document.getElementById("content").previousSibling.children[0];
	obj.innerHTML = obj.innerHTML + "<br><IMG src='../"+ImgName+"' /><br>";
}
//-->
</script>
</body>
</html>