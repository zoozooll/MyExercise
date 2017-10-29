<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

$a_langpackage=new adminlp;

require("../foundation/module_category.php");
$inputtype_arr = array(
	'0'	=> $a_langpackage->a_text_type.'(text)',
	'1' => $a_langpackage->a_select_type.'(select)',
	'2' => $a_langpackage->a_radio_type.'(radio)',
	'3' => $a_langpackage->a_checkbox_type.'(checkbox)',
);

//数据表定义区
$t_category = $tablePreStr."category";
$t_attribute = $tablePreStr."attribute";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$cat_id = intval(get_args('cat_id'));

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result_category = $dbo->getRs($sql_category);

if(!$result_category) { exit($a_langpackage->a_add_category_need.'!'); }

$category_dg = get_dg_category($result_category);

// 当前选中的分类信息
$parent_id = 0;
$cat_info = array();
$attr_info = array();
if($cat_id) {
	foreach($result_category as $value) {
		if($value['cat_id'] == $cat_id) {
			$cat_info = $value;
			break;
		}
	}
	if($cat_info) {
		$sql = "select attr_id,cat_id,attr_name,input_type,attr_values,sort_order from `$t_attribute` where cat_id=".$cat_id;
		$attr_info = $dbo->getRs($sql);
		$parent_id = $cat_info['parent_id'];
	}
}
$right_array=array(
	"attr_add"    =>   "0",
    "attr_append"    =>   "0",
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
<input type="hidden" name="attr_add" value="<?php echo $right_array['attr_add'];?>" >
<input type="hidden" name="attr_append" value="<?php echo $right_array['attr_append'];?>" >
<style>
td span {color:red;}
.green {color:green;}
.red {color:red;}
td textarea {width:95%; height:42px; font-size:12px;}
td .inputtext{width:120px;}
</style>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var cat_id = "<?php echo $cat_id;?>";

function change_cat_id(v) {
	location.href = "m.php?app=goods_attr_manage&cat_id=" + v;
}

function attr_info_cancel(v) {
	var deltr = document.getElementById("tr_"+v);
	deltr.style.display = 'none';
}

function attr_info_save(v) {

	var attr_id = v;
	var attr_name = document.getElementsByName("attr_name["+v+"]")[0];
	var input_type = document.getElementsByName("input_type["+v+"]");
	var attr_values = document.getElementsByName("attr_values["+v+"]")[0];
	var sort_order = document.getElementsByName("sort_order["+v+"]")[0];
	if(attr_name.value=='') { alert("<?php echo $a_langpackage->a_attrname_notnone; ?>!"); return false; }
	var input_type_v = 0;
	for(var i=0; i<input_type.length; i++) {
		if(input_type[i].checked){
			input_type_v = input_type[i].value;
		}
	}

	ajax("a.php?act=goods_attr_edit","POST","attr_id="+attr_id+"&cat_id="+cat_id+"&attr_name="+attr_name.value+"&input_type="+input_type_v+"&attr_values="+attr_values.value+"&sort_order="+sort_order.value,function(data){
		if(data=='-2') {
			alert("<?php echo $a_langpackage->a_privilege_mess;?>");
			location.href="m.php?app=error";
		}else{
			if(data=='-1') {
				alert("<?php echo $a_langpackage->a_fail; ?>!");
			} else {
				if(attr_id>0) {
					alert("<?php echo $a_langpackage->a_edit_success; ?>!");
				} else {
					var tr_0 = document.getElementById("tr_0");

					add_new_attr_info(data,attr_name.value,input_type_v,attr_values.value,sort_order.value)

					attr_name.value = '';
					attr_values.value = '';
					sort_order.value = 0;
					tr_0.style.display = 'none';
					alert("<?php echo $a_langpackage->a_add_success; ?>!");
				}
			}
		}
	});
}

function add_new_attr_info(data,attr_name,input_type,attr_values,sort_order) {
	var attr_tbody = document.getElementById("attr_tbody");
	// 创建新tr
	var newtr = document.createElement("tr");
	newtr.id = "tr_"+data;
	newtr.style.background = "#B4D7E9";

	var td1 = document.createElement("td");
	td1.className = "";
	td1.width="60px";
	td1.innerHTML = data+".";
	newtr.appendChild(td1);

	var td2 = document.createElement("td");
	td2.className = "";
	td2.width="100px";
	td2.innerHTML = '<input type="text" class="small-text" style="width:50px;" name="attr_name['+data+']" value="'+attr_name+'" class="inputtext">';
	newtr.appendChild(td2);

	var td3 = document.createElement("td");
	td3.className = "";
	td3.width="500px";
	if(input_type==0){
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="0" checked="checked" /><?php echo $a_langpackage->a_text_type; ?>(text)';
	} else {
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="0" /><?php echo $a_langpackage->a_text_type; ?>(text)';
	}
	if(input_type==1){
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="1" checked="checked" /><?php echo $a_langpackage->a_select_type; ?>(select)<br />';
	} else {
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="1" /><?php echo $a_langpackage->a_select_type; ?>(select)<br />';
	}
	if(input_type==2){
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="2" checked="checked" /><?php echo $a_langpackage->a_radio_type; ?>(radio)';
	} else {
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="2" /><?php echo $a_langpackage->a_radio_type; ?>(radio)';
	}
	if(input_type==3){
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="3" checked="checked" /><?php echo $a_langpackage->a_checkbox_type; ?>(checkbox)';
	} else {
		td3.innerHTML += '<input type="radio" name="input_type['+data+']" value="3" /><?php echo $a_langpackage->a_checkbox_type; ?>(checkbox)';
	}
	newtr.appendChild(td3);

	var td4 = document.createElement("td");
	td4.className = "";
	td4.width="300px";
	td4.innerHTML = '<textarea name="attr_values['+data+']">'+attr_values+'</textarea>';
	newtr.appendChild(td4);

	var td5 = document.createElement("td");
	td5.className = "";
	td5.width="60px";
	td5.align="center";
	td5.innerHTML = '<input type="text" class="small-text" name="sort_order['+data+']" value="'+sort_order+'" style="width:25px;" maxlength="3" />';
	newtr.appendChild(td5);

	var td6 = document.createElement("td");
	td6.className = "";
	td6.width="175px";
	td6.align="center";
	td6.innerHTML = '<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_save; ?>" name="btn['+data+']" onclick="attr_info_save('+data+');" />&nbsp;';
	td6.innerHTML += '<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_delete; ?>" name="delbtn['+data+']" onclick="attr_info_del('+data+');" />';
	newtr.appendChild(td6);

	attr_tbody.appendChild(newtr);
}

function attr_info_del(v) {
	if(confirm('<?php echo $a_langpackage->a_attr_suredel; ?>')) {
		if(v) {
			ajax("a.php?act=goods_attr_del","POST","id="+v+"&cat_id="+cat_id,function(data){
				if(data=='-2') {
					alert("<?php echo $a_langpackage->a_privilege_mess;?>");
					location.href="m.php?app=error";
				}else{
					if(data!='-1') {
						var deltr = document.getElementById("tr_"+v);
						deltr.style.display = 'none';
					} else {
						alert("<?php echo $a_langpackage->a_operate_fail; ?>!");
					}
				}
			});
		}
	}
}

function attr_info_add() {
	var rights = document.getElementsByName("attr_add")[0].value;
	if(rights != '0'){
		if(cat_id > 0) {
			var tr_0 = document.getElementById("tr_0");
			tr_0.style.display = '';
		} else {
			alert("<?php echo $a_langpackage->a_plselect_category; ?>!");
		}
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}

function attr_info_extend(v) {
	var rights = document.getElementsByName("attr_append")[0].value;
	if(rights != '0'){
		if(v>0) {
			if(confirm('<?php echo $a_langpackage->a_extended_category; ?>')) {
				ajax("a.php?act=goods_attr_extend","POST","parent_id="+v+"&cat_id="+cat_id,function(data){
					if(data=='-1') {
						alert("<?php echo $a_langpackage->a_operate_fail_repeat; ?>");
					} else if (data=='-2') {
						alert("<?php echo $a_langpackage->a_noattr_extended; ?>!");
					} else {
						for(var i=0; i<data.length; i++) {
							add_new_attr_info(data[i].attr_id,data[i].attr_name,data[i].input_type,data[i].attr_values,data[i].sort_order);
						}
					}
				},'JSON');
			}
		} else {
			alert("<?php echo $a_langpackage->a_nocateogyr_opefail; ?>");
		}
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}

//-->
</script>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_m_aboutgoods_management;?> &gt;&gt; <?php echo $a_langpackage->a_attr_list; ?></div>
        <hr />
		<div class="infobox">
    	<h3><?php echo $a_langpackage->a_attr_list; ?></h3>
        <div class="content2">
		<div class="oprate" style="line-height:24px;">&nbsp;&nbsp;<?php echo $a_langpackage->a_select_extended_category; ?></div>
		<div class="oprate" style="line-height:50px;"><span style="float:right;">

			<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_extended_parentattr; ?>" onclick="attr_info_extend(<?php echo $parent_id; ?>)" />&nbsp;&nbsp;
			<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_attr_add; ?>" onclick="attr_info_add()" />&nbsp;&nbsp;</span>&nbsp;&nbsp;<?php echo $a_langpackage->a_plselect_category; ?>：
			<select name="id" onchange="change_cat_id(this.value);">
				<option value="0"><?php echo $a_langpackage->a_plselect_category; ?></option>
				<?php foreach($category_dg as $value) { ?>
				<option value="<?php echo $value['cat_id']; ?>" <?php if($value['cat_id']==$cat_id) echo "selected"; ?>><?php echo $value['str_pad'].$value['cat_name']; ?></option>
				<?php } ?>
			</select>
		</div>
		<table class="content" id="attr_table">
			<tbody id="attr_tbody">
			<tr>
				<th width="60px">ID</th>
				<th width="100px"><?php echo $a_langpackage->a_attr_name; ?></th>
				<th width="500px"><?php echo $a_langpackage->a_input_type; ?></th>
				<th width="300px"><?php echo $a_langpackage->a_input_value_o; ?></th>
				<th width="60px" align="center"><?php echo $a_langpackage->a_sort; ?></th>
				<th width="175px" align="center"><?php echo $a_langpackage->a_operate; ?></th>
			</tr>
			<tr id="tr_0" style="display:none; background:#F7C331;">
				<td width="60px">0.</td>
				<td width="100px"><input type="text" class="small-text" style="width:50px;" name="attr_name[0]" value="" class="inputtext"></td>
				<td id="attr_input_type_td" width="500px">
				<?php $i=0;
				foreach($inputtype_arr as $k=>$v) { $i++; ?>
					<input type="radio" name="input_type[0]" value="<?php echo $k; ?>" <?PHP if('0'==$k) {echo "checked";} ?> /><?php echo $v; ?>
				<?php if($i==2) { echo "<br />";}	} ?>
				</td>
				<td width="300px"><textarea name="attr_values[0]"></textarea></td>
				<td width="60px" align="center"><input type="text" class="small-text" name="sort_order[0]" value="0" style="width:25px;" maxlength="3" /></td>
				<td width="175px" align="center">
					<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_save; ?>" name="btn[0]" onclick="attr_info_save(0);" />
					<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_cancel; ?>" onclick="attr_info_cancel(0);" />
				</td>
			</tr>
			<?php if($attr_info) {
			foreach($attr_info as $value) { ?>
			<tr id="tr_<?php echo $value['attr_id'];?>">
				<td width="60px"><?php echo $value['attr_id'];?>.</td>
				<td width="100px"><input type="text" class="small-text" style="width:50px;" name="attr_name[<?php echo $value['attr_id'];?>]" value="<?php echo $value['attr_name'];?>" class="inputtext"></td>
				<td width="500px">
				<?php $i=0;
				foreach($inputtype_arr as $k=>$v) { $i++; ?>
					<input type="radio" name="input_type[<?php echo $value['attr_id'];?>]" value="<?php echo $k; ?>" <?PHP if($value['input_type']==$k) {echo "checked";} ?> /><?php echo $v; ?>
				<?php if($i==2) { echo "<br />";}	} ?>
				</td>
				<td width="300px"><textarea name="attr_values[<?php echo $value['attr_id'];?>]" ><?php echo $value['attr_values'];?></textarea></td>
				<td width="60px" align="center"><input type="text" class="small-text" class="small-text" name="sort_order[<?php echo $value['attr_id'];?>]" value="<?php echo $value['sort_order'];?>" style="width:25px;" maxlength="3" /></td>
				<td width="175px" align="center">
					<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_save; ?>" name="btn[<?php echo $value['attr_id'];?>]" onclick="attr_info_save(<?php echo $value['attr_id'];?>);" />
					<input type="button" class="regular-button" value="<?php echo $a_langpackage->a_delete; ?>" name="delbtn[<?php echo $value['attr_id'];?>]" onclick="attr_info_del(<?php echo $value['attr_id'];?>)">
				</td>
			</tr>
			<?php }} else { ?>
			<tr>
				<td colspan="6"><?php echo $a_langpackage->a_no_list; ?></td>
			</tr>
			<?php } ?>
			</tbody>
		</table>
		<table class="content">
			<tr>
				<td>&nbsp;<?php echo $a_langpackage->a_attr_remark; ?></td>
			</tr>
		</table>
		</div>
		</div>
	</div>
</div>
</body>
</html>