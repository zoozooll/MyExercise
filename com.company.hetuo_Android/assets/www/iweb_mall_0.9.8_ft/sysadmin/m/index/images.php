<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

//$cat_id = intval(get_args('id'));

//数据表定义区
$t_index_images = $tablePreStr."index_images";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_index_images` order by id desc";

$result = $dbo->fetch_page($sql,13);
$right_array=array(
	"image_show"    =>   "0",
    "image_del"    =>   "0",
    "image_upload"    =>   "0",
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
.green {color:green;}
.red {color:red;}
#upload_img span {display:block; margin-left:10px;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings; ?> &gt;&gt; <?php echo $a_langpackage->a_index_images; ?></div>
        <hr />
	<div class="infobox">
	<form action="a.php?act=index_images_del" method="post" enctype="multipart/form-data">
	<h3><?php echo $a_langpackage->a_index_images; ?></h3>
        <div class="content2">
        	<table class="list_table">
				<thead>
					<tr style="text-align:center;">
						<th width="2px"><input type="checkbox" onclick="checkall(this);" value='' /></th>
						<th width="20px">ID</th>
						<th width="" align="left"><?php echo $a_langpackage->a_name; ?></th>
						<th width="" align="left"><?php echo $a_langpackage->a_image_url; ?>/<?php echo $a_langpackage->a_image_link; ?></th>
						<th width="35px"><?php echo $a_langpackage->a_show; ?></th>
						<th width="90px"><?php echo $a_langpackage->a_add_time; ?></th>
						<th width="55px"><?php echo $a_langpackage->a_operate; ?></th>
					</tr>
				</thead>
				<tbody>
		<?php if($result['result']) {
		foreach($result['result'] as $value) { ?>
		<tr style="text-align:center;">
			<td><input type="checkbox" name="id[]" value="<?php echo $value['id'];?>" /></td>
			<td><?php echo $value['id'];?></td>
			<td align="left"><a href="../<?php echo $value['images_url'];?>"><?php echo $value['name'];?></a></td>
			<td align="left"><a href="../<?php echo $value['images_url'];?>" target="_blank"><?php echo $value['images_url'];?></a><br /><?php echo $value['images_link'];?></td>
			<td>
				<?php if($value['status']) { ?>
				<img src="../skin/default/images/yes.gif" id="stasus_img_<?php echo $value['id'];?>"  onclick="toggle_show('stasus_img','<?php echo $value['id']; ?>')" />
				<?php } else { ?>
				<img src="../skin/default/images/no.gif" id="stasus_img_<?php echo $value['id'];?>" onclick="toggle_show('stasus_img','<?php echo $value['id']; ?>')" />
				<?php } ?>
			</td>
			<td><?php echo sub_str($value['add_time'],10,false);?></td>
			<td>
				<a href="a.php?act=index_images_del&id=<?php echo $value['id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_sure_delimage; ?>');"><?php echo $a_langpackage->a_delete; ?><br /></a>
				<?php if($value['status']) { ?>
				<a href="javascript:;" onclick="toggle_show('stasus_img','<?php echo $value['id']; ?>',this)"><?php echo $a_langpackage->a_cancel_show; ?></a>
				<?php } else { ?>
				<a href="javascript:;" onclick="toggle_show('stasus_img','<?php echo $value['id']; ?>',this)"><?php echo $a_langpackage->a_set_display; ?></a>
				<?php } ?>
			</td>
		</tr>
		<?php }?>
		<tr>
			<td colspan="8">
				<span class="button-container"><input class="regular-button" type='submit' onclick="return confirm('<?php echo $a_langpackage->a_operate_message;?>');" name="" value="<?php echo $a_langpackage->a_dele;?>"  /></span>
			</td>
		</tr>
		<?php } else { ?>
		<tr>
			<td colspan="8"><?php echo $a_langpackage->a_no_imagelist; ?>!</td>
		</tr>
		<?php } ?>
		<tr>
			<td colspan="8">
				<?php include("m/page.php"); ?>
			</td>
		</tr>
		</tbody>
	</table>
</form>
<form action="a.php?act=index_images_upload" method="post" enctype="multipart/form-data">
<input type="hidden" id="show_right" value="<?php echo $right_array['image_show'];?>">
<table class="list_table">
	<tbody>
	<tr><td id="upload_img">
		<span class="button-container"><a href="javascript:addNewUploadSpan();">[+]</a>
		<?php echo $a_langpackage->a_descrption; ?>：<input class="small-text" type="text" name="img_name[]" maxlength="200" />
		<?php echo $a_langpackage->a_link; ?>*：<input type="text" class="small-text" name="img_link[]" value="http://" maxlength="200" style="width:200px" />
		<?php echo $a_langpackage->a_uploadFile; ?>*：<input type="file" name="attach[]" /></span>
	</td></tr>
	</tbody>
</table>
<table>
	<tr>
        <td width="115px">
        	<input type="submit" class="regular-button" name="submit" value="<?php echo $a_langpackage->a_uploadimage; ?>" />
        </td>
        <td>
        	<span>
                <?php echo $a_langpackage->a_advice_uploadsize; ?>：<?php echo $a_langpackage->a_img_type; ?>
            </span>
        </td>
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
function toggle_show(idname,id,obj2) {
	var rights=document.getElementById("show_right").value;
	var id_name = idname+"_"+id;
	var obj = document.getElementById(id_name);
	if(rights !='0'){
		var re = /yes/i;
		var src = obj.src;
		var isshow = 1;
		var sss = src.search(re);
		if(sss > 0) {
			isshow = 0;
		}
		ajax("a.php?act=images_status_toggle","POST","id="+id+"&s="+isshow,function(data){
			if(data) {
				obj.src = '../skin/default/images/'+data+'.gif';
				if(data=='yes' &&obj2){
					obj2.innerHTML="<?php echo $a_langpackage->a_cancel_show; ?>";
				}else if(data=='no' &&obj2){
					obj2.innerHTML="<?php echo $a_langpackage->a_set_display; ?>";
				}else {
					location.href="m.php?app=index_images";
				}
			}
		});
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}

function addNewUploadSpan() {
	var upload_img = document.getElementById("upload_img");
	var newspan = document.createElement("span");
	newspan.innerHTML = '<a href="javascript:removeUploadSpan();">[-]&nbsp;</a> <?php echo $a_langpackage->a_descrption; ?>：<input class="small-text" type="text" name="img_name[]" maxlength="200" /> <?php echo $a_langpackage->a_link; ?>*：<input class="small-text" type="text" name="img_link[]" value="http://" maxlength="200" style="width:200px" /> <?php echo $a_langpackage->a_uploadFile; ?>*：<input type="file" name="attach[]" />';
	upload_img.appendChild(newspan);
}

function removeUploadSpan() {
	var upload_img = document.getElementById("upload_img");
	var number = upload_img.children.length;
	var delnode = upload_img.children[number-1];
	upload_img.removeChild(delnode);
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