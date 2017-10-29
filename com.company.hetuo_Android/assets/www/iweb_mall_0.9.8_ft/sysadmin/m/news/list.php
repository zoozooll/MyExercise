<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("../foundation/module_news.php");

//引入语言包
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("news_show");
$cat_id = intval(get_args('id'));
if ($cat_id){
	if(!$right){
		header('location:m.php?app=error');
	}
}
//数据表定义区
$t_article = $tablePreStr."article";
$t_article_cat = $tablePreStr."article_cat";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_article` ";
if($cat_id) {
	$sql .= " where cat_id='$cat_id' ";
}
$sql .= " order by add_time desc";
$result = $dbo->fetch_page($sql,13);
$cat_info = get_news_cat_list($dbo,$t_article_cat);
//新闻分类
$sql ="select cat_id,cat_name from `$t_article_cat`";
$cat_list = $dbo->getRs($sql);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_article_management;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_news_list; ?></a></div>
        <hr />
    <div class="seachbox">
        <div class="content2">
        	<form action="m.php?app=news_list" name="searchForm" method="get">
            	<table class="form-table">
	            	<tbody>
	            	<tr>
	                   	<td width="240px">
	                   		<img src="skin/images/icon_search.gif" border="0" alt="SEARCH" />
	                   		<?php echo $a_langpackage->a_news_category; ?>：
	                   		<select name="id">
								<option value="0"><?php echo $a_langpackage->a_select_news_category; ?></option>
								<?php foreach($cat_list as $value) { ?>
								<option value="<?php echo $value['cat_id']; ?>" <?php if($cat_id==$value['cat_id']){echo "selected";} ?> ><?php echo $value['cat_name'];?></option>
								<?php } ?>
							</select>
	                   	</td>
	                   	<td><input type="hidden" name="app" value="news_list"><input class="regular-button" type="submit" value="<?php echo $a_langpackage->a_serach;?>" /></td>
	                </tr>
                </tbody>
            </table>
           </form>
        </div>
    </div>
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_news_list; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=news_add"><?php echo $a_langpackage->a_news_add; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=news_del" name="form1" id="form1" method="post">
		<input type="hidden" id="show_right" value="<?php echo $right;?>">
		<table class="list_table">
        	<thead>
			<tr style="text-align:center">
				<th width="2px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
				<th width="40px">ID</th>
				<th align="left" width="150px"><?php echo $a_langpackage->a_news_title; ?></th>
				<th align="left" width="110px"><?php echo $a_langpackage->a_news_category; ?></th>
				<th width="40px"><?php echo $a_langpackage->a_news_alinks; ?></th>
				<th width="300px" align="left"><?php echo $a_langpackage->a_news_links_url; ?></th>
				<th width="36px"><?php echo $a_langpackage->a_show; ?></th>
				<th width="189px"><?php echo $a_langpackage->a_add_time; ?></th>
				<th width="40px"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			</thead>
			<tbody>
			<?php if($result['result']) {
			foreach($result['result'] as $value) { ?>
			<tr style="text-align:center">
				<td><input type="checkbox" name="article_id[]" value="<?php echo $value['article_id'];?>" /></td>
				<td><?php echo $value['article_id'];?></td>
				<td align="left"><a href="m.php?app=news_edit&id=<?php echo $value['article_id'];?>"><?php echo $value['title'];?></a></td>
				<td align="left"><a href="m.php?app=news_list&id=<?php echo $value['cat_id'];?>"><?php echo $cat_info[$value['cat_id']]['cat_name'];?></a></td>
				<?php echo $value['is_link'] ? '<td class="center green">'.$a_langpackage->a_yes.'</td>' : '<td class="center red">'.$a_langpackage->a_no.'</td>';?>
				<td align="left"><?php echo $value['link_url'];?></td>
				<td>
					<?php if($value['is_show']) { ?>
					<img src="../skin/default/images/yes.gif" onclick="toggle_show(this,'<?php echo $value['article_id']; ?>')" />
					<?php } else { ?>
					<img src="../skin/default/images/no.gif" onclick="toggle_show(this,'<?php echo $value['article_id']; ?>')" />
					<?php } ?>
				</td>
				<td><?php echo $value['add_time'];?></td>
				<td>
					<a href="m.php?app=news_edit&id=<?php echo $value['article_id'];?>"><?php echo $a_langpackage->a_update; ?></a><br />
					<a href="a.php?act=news_del&id=<?php echo $value['article_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_sure_delnews; ?>');"><?php echo $a_langpackage->a_delete; ?></a>
				</td>
			</tr>
			<?php }?>
			<tr>
				<td colspan="9">
					<span class="button-container"><input class="regular-button" type="submit" name=""  onclick="return confirm('<?php echo $a_langpackage->a_exe_message; ?>');" value="<?php echo $a_langpackage->a_batch_del; ?>" /></span>
				</td>
			</tr>
			<?php } else { ?>
			<tr>
				<td colspan="9" class="center"><?php echo $a_langpackage->a_nonews_list; ?>!</td>
			</tr>
			<?php } ?>
			<tr>
				<td colspan="9" class="center"><?php include("m/page.php"); ?></td>
			</tr>
           </tbody>
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
	var rights=document.getElementById("show_right").value;
	if(rights != '0'){
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
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
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