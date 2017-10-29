<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_asd.php");

//引入语言包
$a_langpackage=new adminlp;

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;

//数据表定义区
$t_asd_position = $tablePreStr."asd_position";
$t_asd_content = $tablePreStr."asd_content";

$asd_id = intval(get_args('id'));
if(!$asd_id) {exit($a_langpackage->a_error);}

$position_info = get_asd_position_all($dbo,$t_asd_position);

$asd_info = get_asd_info($dbo,$t_asd_content,$asd_id);

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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_asd_management;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_edit_asd; ?></a></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_edit_asd; ?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=asd_list"><?php echo $a_langpackage->a_asd_list; ?></a></span></h3>
    <div class="content2">
		<form action="a.php?act=asd_edit" method="post" onsubmit="return checkform();" enctype="multipart/form-data">
		<table>
			<tr>
				<td width="140px"><?php echo $a_langpackage->a_select_asdposition; ?>：</td>
				<td class="left"><select name="position_id">
					<option value="0" title="0X0"><?php echo $a_langpackage->a_select_asdposition; ?></option>
					<?php foreach($position_info as $value) {?>
					<option value="<?php echo $value['position_id']; ?>" title="<?php echo $value['asd_width']."X".$value['asd_height']; ?>" <?php if($value['position_id']==$asd_info['position_id']){ echo "selected";} ?> ><?php echo $value['position_name']; ?></option>
					<?php }?>
				</select> <span id="position_id_message">*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asdposition_size; ?>：</td>
				<td id="wh"> <?php echo intval($position_info[$asd_info['position_id']]['asd_width'])."X".intval($position_info[$asd_info['position_id']]['asd_height']); ?>
				</td>
				<input type="hidden" name="asd_wh" value="<?php echo intval($position_info[$asd_info['position_id']]['asd_width'])."X".intval($position_info[$asd_info['position_id']]['asd_height']); ?>" />
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asd_name; ?>：</td>
				<td><input class="small-text" type="text" name="asd_name" value="<?php echo $asd_info['asd_name']; ?>" style="width:200px;" /> <span id="asd_name_message">*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asd_link; ?>：</td>
				<td><input class="small-text" type="text" name="asd_link" value="<?php echo $asd_info['asd_link']; ?>" style="width:200px;" /> <span id="asd_link_message">*</span></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asd_type; ?>：</td>
				<td><input type="radio" name="media_type" value="1" <?php if($asd_info['media_type']==1) echo "checked"; ?> onclick="showcontent(this.value);" /><?php echo $a_langpackage->a_image; ?>
				<input type="radio" name="media_type" value="2" <?php if($asd_info['media_type']==2) echo "checked"; ?> onclick="showcontent(this.value);" />flash
				<input type="radio" name="media_type" value="3" <?php if($asd_info['media_type']==3) echo "checked"; ?> onclick="showcontent(this.value);" /><?php echo $a_langpackage->a_text; ?></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asd_content; ?>：</td>
				<td id="asd_content"><?php if($asd_info['media_type']==3) {
						echo '<textarea name="content[]" style="width:350px; height:32px;">'.$asd_info['asd_content'].'</textarea>';
					} else {
						echo '<input type="file" name="content[]" /> '.$a_langpackage->a_pls_setempty;
					}
					?></td>
			</tr>
			<tr>
				<td><?php echo $a_langpackage->a_asd_remark; ?>：</td>
				<td><textarea name="remark" style="width:350px; height:100px;"><?php echo $asd_info['remark']; ?></textarea></td>
			</tr>
			<tr>
				<input type="hidden" name="asd_id" value="<?php echo $asd_id; ?>">
				<td colspan="2"><span class="button-container"><input class="regular-button" type="submit" name="submit" value="<?php echo $a_langpackage->a_edit_asd; ?>" /></span></td>
			</tr>
		</table>
		</form>
	   </div>
	 </div>
   </div>
</div>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script>
var type = "<?php echo $asd_info['media_type']; ?>";

var position_id = document.getElementsByName("position_id")[0];
var wh = document.getElementById("wh");
var inputwh = document.getElementsByName("asd_wh")[0];
position_id.onchange = function(){
	var index = position_id.selectedIndex;
	wh.innerHTML = position_id.options[index].title;
	inputwh.value = position_id.options[index].title;
}

var media_type = document.getElementsByName("media_type");
var asd_content = document.getElementById("asd_content");
var content = '<textarea name="content[]" style="width:350px; height:32px;"></textarea>';
if(type==3) {
	content = asd_content.innerHTML;
}

function showcontent(v) {
	if(v==3) {
		asd_content.innerHTML = content;
	} else {
		asd_content.innerHTML = '<input type="file" name="content[]" /> <?php echo $a_langpackage->a_pls_setempty; ?>';
	}
}

var asd_name = document.getElementsByName("asd_name")[0];
var asd_link = document.getElementsByName("asd_link")[0];

function checkform() {
	if(position_id.value==0) {
		alert("<?php echo $a_langpackage->a_pls_select_asdpos; ?>");
		return false;
	}

	if(asd_name.value=='') {
		alert("<?php echo $a_langpackage->a_pls_type_asdname; ?>");
		asd_name.focus();
		return false;
	}

	if(asd_link.value=='http://' || asd_link.value=='') {
		alert("<?php echo $a_langpackage->a_pls_type_asdlink; ?>");
		asd_link.focus();
		return false;
	}

	return true;
}
</script>
</body>
</html>