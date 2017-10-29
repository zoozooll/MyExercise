<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_keywords_count = $tablePreStr."keywords_count";

dbtarget('r',$dbServs);
$dbo=new dbex;

$sql = "select * from $t_keywords_count order by count desc";
$result = $dbo->getRs($sql);

$right_array=array(
	"keyword_del"    =>   "0",
    "keyword_update"    =>   "0",
);
foreach($right_array as $key => $value){
	$right_array[$key]=check_rights($key);
}

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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management;?> &gt;&gt; <?php echo $a_langpackage->a_key_managment;?></div>
        <hr />
	<div class="infobox">
	    	<h3><?php echo $a_langpackage->a_key_managment;?></h3>
	        <div class="content2">
			<form action="a.php?act=searchkey_del" method="post">
			<input type="hidden" name="keyword_update" value="<?php echo $right_array['keyword_update']?>">
			<table class="list_table">
			<thead>
				<tr style="text-align:center;">
					<th width="25"><input type="checkbox" onclick="checkall(this);" /></th>
					<th><?php echo $a_langpackage->a_key_word;?></th>
					<th><?php echo $a_langpackage->a_search_count;?></th>
					<th><?php echo $a_langpackage->a_search_num_now;?></th>
					<th><?php echo $a_langpackage->a_search_num_week;?></th>
					<th><?php echo $a_langpackage->a_search_num_month;?></th>
					<th><?php echo $a_langpackage->a_last_use_time;?></th>
					<th><?php echo $a_langpackage->a_operate;?></th>
				</tr>
			</thead>
			<tbody>
			<?php if($result) {
				foreach($result as $value) { ?>
				<tr style="text-align:center;">
					<td><input type="checkbox" name="searchkey[]" value="<?php echo  $value['id'];?>" /></td>
					<td><?php echo $value['keywords']?></td>
					<td><?php echo $value['count']?></td>
					<td><?php echo $value['day']?></td>
					<td><?php echo $value['week']?></td>
					<td><?php echo $value['month']?></td>
					<td><?php echo date('Y-m-d H:m:s',$value['dataline'])?></td>
					<?php if($right_array['keyword_del']) { ?>
					<td><a href="a.php?act=searchkey_del&id=<?php echo $value['id'];?>"><?php echo $a_langpackage->a_dele;?></a></td>
					<?php } else {?>
						<td>&nbsp;</td>
					<?php }?>
				</tr>
				<?php }?>
				<tr>
					<td colspan="6"><?php if($right_array['keyword_del']) { ?><INPUT class="regular-button" onclick="return confirm('<?php echo $a_langpackage->a_operate_message;?>');" type=submit value="<?php echo $a_langpackage->a_dele;?>" name=deletesubmit> <?php } ?></td>
					<td align="right"><?php if($right_array['keyword_update']) { ?><input class="regular-button" type="button" value="<?php echo $a_langpackage->a_start_update;?>" onclick="updateGoodsKeyword();" /> <?php } ?></td>
					<td><span id="updateKeyword_message"></span></td>
				</tr>
			<?php }else{?>
				<tr><td colspan="7"><?php echo $a_langpackage->a_no_data;?></td></tr>
			<?php }?>
			</tbody>
			</table>
			</form>
			</div>
		</div>
	</div>
</div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var inputs = document.getElementsByTagName("input");
function submitform() {
	var status = document.getElementsByName("goods");
	var checknum = 0;
	for(var i=0; i<inputs.length; i++) {
		if(inputs[i].type=='checkbox') {
			if(inputs[i].checked) {
				checknum++;
			}
		}
	}
	if(checknum==0) {
		alert('<?php echo $a_langpackage->a_select_check;?>');
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
function updateGoodsKeyword() {
	var right = document.getElementsByTagName("keyword_update");
	if(right){
		var updateKeyword_message = document.getElementById("updateKeyword_message");
		updateKeyword_message.innerHTML = '<?php echo $a_langpackage->a_updating;?>';
		var d = new Date();
		var t = d.getTime();
		ajax("./a.php?act=goods_update_keyword&t="+t,"GET","v="+t,function(data){
			if(data==1) {
				updateKeyword_message.innerHTML = '<?php echo $a_langpackage->a_update_over;?>';
			} else {
				updateKeyword_message.innerHTML = '<?php echo $a_langpackage->a_update_fail;?>';
			}
		});
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}

//-->
</script>
</body>
</html>