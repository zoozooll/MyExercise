<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

require("../foundation/module_areas.php");

//数据表定义区
$t_areas = $tablePreStr."areas";

//变量定义区
$parent_id=intval(get_args('parent_id'));
$area_type=intval(get_args('area_type'));

if ($parent_id){
	//权限管理
	$right=check_rights("area_edit");
	if (!$right){
		header('location:m.php?app=error');
		exit;
	}
}
$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

if($area_type<=0||!$area_type){
	$area_type=0;
}

$areas_info = get_areas_list($dbo,$t_areas,$parent_id);
$right_array=array(
	"area_add"    =>   "0",
    "area_edit"    =>   "0",
    "area_del"    =>   "0",
);
foreach($right_array as $key => $value){
	$right_array[$key]=check_rights($key);
}
$sql="select parent_id from $t_areas where area_id=$parent_id";
$parent_id_last=$dbo->getRow($sql);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
.green {color:green;}
.red {color:red;}
.edit span{background:#efefef;}
</style>
</head>
<body>
<form id="form1" name="form1" method="post" action="a.php?act=sys_area_add&parent_id=<?php echo $parent_id;?>&area_type=<?php echo $area_type?>">
<input type="hidden" id="area_edit" value="<?php echo $right_array['area_edit'];?>">
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings;?> &gt;&gt; <?php echo $a_langpackage->a_area_list;?></div>
        <hr />
      <div class="seachbox">
        <div class="content2">
            <table class="form-table">
            	<tbody>
            	  <tr>
                   	<td width="78px">
                   		<?php echo $a_langpackage->a_NUM;?><?php echo $area_type+1?><?php echo $a_langpackage->a_rank_add;?>
                   	</td>
					<td width="100px">
						<input class="small-text" type="text" name="name" id="textfield" />
					</td>
                  	<td width="">
                  		<input class="regular-button" type="submit" name="button" id="button" value="<?php echo $a_langpackage->a_add;?>" />
                  	</td>
                  </tr>
                </tbody>
            </table>
        </div>
    </div>

	<div class="infobox">
	<h3><span class="left"><?php echo $a_langpackage->a_sys_area; ?></span><span class="right" style="margin-right:15px;"><a href="m.php?app=sys_area&parent_id=<?php echo $parent_id_last['0'];?>&area_type=<?php echo $area_type-1?>">
						<?php echo $a_langpackage->a_back_last;?></a></span> </h3>
    <div class="content2">
		<table class="list_table">
			<thead>
				<tr style="text-align:center;">
					<th width="20%"><?php echo $a_langpackage->a_ID;?></thd>
					<th><?php echo $a_langpackage->a_area_name;?></th>
					<th width="30%"><?php echo $a_langpackage->a_operate;?></th>
				</tr>
			</thead>
			<tbody>
				<?php if($areas_info) {
				foreach($areas_info as $val) { ?>
				<tr style="text-align:center;">
					<td width="20%"><?php echo $val['area_id'];?>.</td>
					<td id="area_id_<?php echo $val['area_id'];?>">
					<span onclick="edit_name(this,<?php echo $val['area_id'];?>)">&nbsp;<?php echo $val['area_name'];?>&nbsp;</span></td>
					<td width="30%">
					<a href="m.php?app=sys_area&parent_id=<?php echo $val['area_id'];?>&area_type=<?php echo $area_type+1?>"><?php echo $a_langpackage->a_edit;?></a>
					<a href="a.php?act=sys_area_del&id=<?php echo $val['area_id'];?>" onclick="return confirm('<?php echo $a_langpackage->a_suc_del;?>');"><?php echo $a_langpackage->a_dele;?></a></td>
				</tr>
				<?php }?>

				<?php } else { ?>
				<tr>
					<td colspan="4"><?php echo $a_langpackage->a_no_list;?></td>
				</tr>
				<?php } ?>
			</tbody>
		</table>
</div></div></div></div>
</form>
</body>
</html>
<script language="JavaScript" src="../servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
var name_value;
function edit_name(span,id) {
	right = document.getElementById("area_edit").value;

	if(right != '0'){
		obj = document.getElementById("area_id_"+id);
		name_value = span.innerHTML;
		name_value = name_value.replace(/&nbsp;/ig,"");
		obj.innerHTML = '<input style="width:60px" type="text" class="small-text" id="input_area_id_' + id + '" value="' + name_value + '" onblur="edit_name_post(this,' + id + ')" maxlength="100"  />';
		document.getElementById("input_area_id_"+id).focus();
	}else{
		alert("<?php echo $a_langpackage->a_privilege_mess;?>");
		location.href="m.php?app=error";
	}
}

function edit_name_post(input,id) {
	var obj = document.getElementById("area_id_"+id);

	if(name_value==input.value) {
		obj.innerHTML = '<span onclick="edit_name(this,' + id + ')">&nbsp;' + name_value + '&nbsp;</span>';
	} else {
		ajax("a.php?act=sys_area_add","POST","id="+id+"&v="+input.value,function(data){
			if(data==1) {
				obj.innerHTML = '<span onclick="edit_name(this,' + id + ')">&nbsp;' + input.value + '&nbsp;</span>';
			} else {
				obj.innerHTML = '<span onclick="edit_name(this,' + id + ')">&nbsp;' + name_value + '&nbsp;</span>';
			}
		});
	}
}

//-->
</script>