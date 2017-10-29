<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_news.php");

//引入语言包
$a_langpackage=new adminlp;

//$cat_id = intval(get_args('id'));

//数据表定义区
//$t_article = $tablePreStr."article";
$t_article_cat = $tablePreStr."article_cat";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_article_cat` where 1 ";
$result = $dbo->fetch_page($sql,13);

//$cat_info = get_news_cat_list($dbo,$t_article_cat);
//print_r($cat_info);
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
.green {color:green;}
.red {color:red;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_article_management;?> &gt;&gt; <?php echo $a_langpackage->a_category_list; ?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_category_list; ?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=news_catadd"><?php echo $a_langpackage->a_category_add; ?></a></span></h3>
    <div class="content2">

		<form action="a.php?act=news_catdel" name="form1" id="form1" method="post">
		<table class="list_table">
		  <thead>
			<tr style=" text-align:center">
				<th width="30px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="30px">ID</th>
				<th width="" align="left"><?php echo $a_langpackage->a_category_name; ?></th>
				<th width="80px"><?php echo $a_langpackage->a_type; ?></th>
				<th width="60px"><?php echo $a_langpackage->a_sort; ?></th>
				<th width="60px"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style=" text-align:center">
				<td width="30px"><input type="checkbox" name="cat_id[]" value="<?php echo $value['cat_id'];?>" <?php if($value['parent_id']==-1){echo "disabled"; }?>/></td>
				<td width="30px"><?php echo $value['cat_id'];?></td>
				<td width="" align="left"><a href="m.php?app=news_list&id=<?php echo $value['cat_id'];?>"><?php echo $value['cat_name'];?></a></td>
				<td width="80px"><?php if($value['parent_id']==-1){echo $a_langpackage->a_sys_type;}elseif($value['parent_id']==0){echo $a_langpackage->a_cus_type;}?></td>
				<td width="60px"><?php echo $value['sort_order'];?></td>
				<td width="60px">
					<a href="m.php?app=news_catedit&id=<?php echo $value['cat_id'];?>"><?php echo $a_langpackage->a_update; ?></a>
					<?php if($value['parent_id']!=-1){?>
					<a href="a.php?act=news_catdel&id=<?php echo $value['cat_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_del_all_catlist; ?>');"><?php echo $a_langpackage->a_delete; ?></a>
					<?php }?>
				</td>
			</tr>
			<?php }?>
			<tr>
				<td colspan="6">
					<span class="button-container"><input class="regular-button" type="submit" name=""  onclick="return confirm('<?php echo $a_langpackage->a_del_all_catlist; ?>');" value="<?php echo $a_langpackage->a_batch_del; ?>" /></span>
				</td>
			</tr>
			<?php } else { ?>
			<tr>
				<td colspan="6" class="center"><?php echo $a_langpackage->a_nonews_list; ?>!</td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="6" class="center"><?php include("m/page.php"); ?></td>
			</tr>
		</table>
		</form>
	   </div>
	  </div>
	 </div>
</div>
<div style="color:red; display:none; width:270px; margin:5px auto;" id="ajaxmessageid">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<?php echo $a_langpackage->a_loading; ?></div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function toggle_show(obj,id) {
	var re = /yes/i;
	var src = obj.src;
	var isshow = 1;
	var sss = src.search(re);
	if(sss > 0) {
		isshow = 0;
	}
	ajax("a.php?act=news_isshow_toggle","POST","id="+id+"&s="+isshow,function(data){
		if(data) {
			obj.src = '../skin/default/images/'+data+'.gif';
		}
	});
}
var inputs = document.getElementsByTagName("input");
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
//-->
</script>
</body>
</html>