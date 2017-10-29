<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/address.html
 * 如果您的模型要进行修改，请修改 models/modules/user/address.php
 *
 * 修改完成之后需要您进入后台重新编译，才会重新生成。
 * 如果您开启了debug模式运行，那么您可以省去上面这一步，但是debug模式每次都会判断程序是否更新，debug模式只适合开发调试。
 * 如果您正式运行此程序时，请切换到service模式运行！
 *
 * 如您有问题请到官方论坛（http://tech.jooyea.com/bbs/）提问，谢谢您的支持。
 */
?><?php
/*
 * 此段代码由debug模式下生成运行，请勿改动！
 * 如果debug模式下出错不能再次自动编译时，请进入后台手动编译！
 */
/* debug模式运行生成代码 开始 */
if(!function_exists("tpl_engine")) {
	require("foundation/ftpl_compile.php");
}
if(filemtime("templates/default/modules/user/address.html") > filemtime(__file__) || (file_exists("models/modules/user/address.php") && filemtime("models/modules/user/address.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/address.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_users.php");
require("foundation/module_areas.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_users = $tablePreStr."users";
$t_user_address = $tablePreStr."user_address";
$t_areas = $tablePreStr."areas";

//变量定义区
$address_id=intval(get_args('address_id'));

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$sql="select * from $t_user_address where user_id=$user_id";
//echo $sql;
$address_rs=$dbo->getRs($sql);

$user_info['to_user_name'] = '';
$user_info['full_address'] = '';
$user_info['email'] = '';
$user_info['mobile'] = '';
$user_info['telphone'] = '';
$user_info['zipcode'] = '';
$user_info['user_province'] = 0;
$user_info['user_city'] = 0;
//$user_info['user_country'] = 0;

if($address_id && $address_rs){
	foreach($address_rs as $value) {
		if($address_id == $value['address_id']) {
			$user_info = $value;
		}
	}
}
// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = isset($user_info['user_country']) ? $user_info['user_country'] : 1;

$areas_info = get_areas_info($dbo,$t_areas);

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
th{background:#EFEFEF}

</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_getgoods_address;?></h3></div>
			<table width="98%" class="form_table">
				<tr><th class="center"><?php echo $m_langpackage->m_getpackage_pop;?></th><th><?php echo $m_langpackage->m_getpackage_area;?></th><th><?php echo $m_langpackage->m_address;?></th><th class="center"><?php echo $m_langpackage->m_zipcode;?></th><th class="center"><?php echo $m_langpackage->m_mobile;?>/<?php echo $m_langpackage->m_telphone;?></th><th class="center"><?php echo $m_langpackage->m_email;?></th><th class="center"><?php echo $m_langpackage->m_manage;?></th></tr>
				<?php if(empty($address_rs)){?>
					<tr><td class="center" colspan="7"><?php echo $m_langpackage->m_dontsave_getgoods_addresslist;?></td></tr>
				<?php }else{?>
				<?php foreach($address_rs as $val){?>
				<tr>
					<td class="center"><?php echo $val['to_user_name'];?></td><td><?php echo $areas_info[0][$val['user_country']]['area_name'];?> <?php echo $areas_info[1][$val['user_province']]['area_name'];?> <?php echo $areas_info[2][$val['user_city']]['area_name'];?> <?php echo $areas_info[3][$val['user_district']]['area_name'];?></td><td><?php echo $val['full_address'];?></td><td class="center"><?php echo $val['zipcode'];?></td><td class="center"><?php echo $val['mobile'];?>/<?php echo $val['telphone'];?></td><td class="center"><?php echo $val['email'];?></td><td class="center"><a href="modules.php?app=user_address&address_id=<?php echo $val['address_id'];?>"><?php echo $m_langpackage->m_edit;?></a> | <a href="do.php?act=user_address_del&address_id=<?php echo $val['address_id'];?>"><?php echo $m_langpackage->m_del;?></a></td>
				</tr>
				<?php }?>
				<?php }?> 
			</table>
			<form action="do.php?act=user_address_add" method="post" name="form_profile" onsubmit="return checkform();">
				<table width="98%" class="form_table">
					<input type="hidden" name="address_id" value="<?php echo  $user_info['address_id'];?>" />
					<tr class="center"><th colspan="2"><?php echo $m_langpackage->m_addnew_getpackage_address;?></th></tr>
					<tr><td class="textright"><?php echo $m_langpackage->m_getpackage_name;?>：</td><td class="textleft"><input type="text" name="to_user_name" value="<?php echo  $user_info['to_user_name'];?>" maxlength="12" /></td></tr>
					<tr>
						<td class="textright"><?php echo  $m_langpackage->m_stayarea;?>：</td>
						<td class="textleft">
							<span id="user_country"><select name="country" onchange="areachanged(this.value,0);">
								<option value='0'><?php echo  $m_langpackage->m_select_country;?></option>
							 <?php   foreach($areas_info[0] as $v){?>
								<option value="<?php echo  $v['area_id'];?>" <?php  if($v['area_id']==$user_info['user_country']){echo 'selected';}?>><?php echo  $v['area_name'];?></option>
							<?php }?>
							</select></span>
							<span id="user_province"><?php  if($user_info['user_country']){?>
							<select name="province" onchange="areachanged(this.value,1);">
								<option value='0'><?php echo  $m_langpackage->m_select_province;?></option>
							<?php foreach($areas_info[1] as $v) {
									if($v['parent_id'] == $user_info['user_country']){?>
								<option value="<?php echo  $v['area_id'];?>" <?php   if($v['area_id']==$user_info['user_province']){echo 'selected';}?>><?php echo  $v['area_name'];?></option>
							<?php 	}
							}?>
							</select>
							<?php }?></span>
							<span id="user_city"><?php 	 if($user_info['user_province']){?>
							<select name="city" onchange="areachanged(this.value,2);">
								<option value='0'><?php echo  $m_langpackage->m_select_city;?></option>
							<?php 	 foreach($areas_info[2] as $v) {
									if($v['parent_id'] == $user_info['user_province']){?>
								<option value="<?php echo  $v['area_id'];?>" <?php  if($v['area_id']==$user_info['user_city']){echo 'selected';}?>><?php echo  $v['area_name'];?></option>
							<?php 	}
							}?></select>
							<?php }?></span>
							<span id="user_district"><?php  if($user_info['user_city']) {?>
							<select name="district">
								<option value='0'><?php echo  $m_langpackage->m_select_district;?></option>
							<?php foreach($areas_info[3] as $v) {
									if($v['parent_id'] == $user_info['user_city']){?>
								<option value="<?php echo  $v['area_id'];?>" <?php   if($v['area_id']==$user_info['user_district']){echo 'selected';}?>><?php echo  $v['area_name'];?></option>
							<?php 	}
							}?></select>
							<?php }?></span>
						</td>
					</tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_address;?>：</td><td class="textleft"><input type="text" name="full_address" value="<?php echo  $user_info['full_address'];?>" style="width:250px;" maxlength="200" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_zipcode;?>：</td><td class="textleft"><input type="text" name="zipcode" value="<?php echo  $user_info['zipcode'];?>" maxlength="6" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_mobile;?>：</td><td class="textleft"><input type="text" name="mobile" value="<?php echo  $user_info['mobile'];?>" maxlength="20" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_telphone;?>：</td><td class="textleft"><input type="text" name="telphone" value="<?php echo  $user_info['telphone'];?>" maxlength="20" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_email;?>：</td><td class="textleft"><input type="text" name="email" value="<?php echo  $user_info['email'];?>" maxlength="20" /></td></tr>
					<tr><td colspan="2" align="center"><input type="hidden" name="user_id" value="<?php echo  $user_id;?>" /><input type="submit" name="submit" value="提交" /></td></tr>
				</table>
			</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</div><?php  require("modules/footer.php");?></div>
<script language="JavaScript" src="servtools/ajax_client/ajax.js"></script>
<script language="JavaScript">
<!--
function areachanged(value,type){
	if(value > 0) {
		ajax("do.php?act=ajax_areas","POST","value="+value+"&type="+type,function(return_text){
			var return_text = return_text.replace(/[\n\r]/g,"");
			if(return_text==""){
				alert("<?php echo  $m_langpackage->m_select_again;?>！");
			} else {
				if(type==0) {
					document.getElementById("user_province").innerHTML = return_text;
					show("user_province");
					hide("user_city");
					hide("user_district");
				} else if(type==1) {
					document.getElementById("user_city").innerHTML = return_text;
					show("user_city");
					hide("user_district");
				} else if(type==2) {
					document.getElementById("user_district").innerHTML = return_text;
					show("user_district");
				}
			}
		});
	} else {
		if(type==2) {
			hide("user_district");
		} else if(type==1) {
			hide("user_district");
			hide("user_city");
		} else if(type==0) {
			hide("user_district");
			hide("user_city");
			hide("user_province");
		}
	}
}

function hide(id) {
	document.getElementById(id).style.display = 'none';
}

function show(id) {
	document.getElementById(id).style.display = '';
}

function checkform(){
	var to_user_name = document.getElementsByName('to_user_name')[0];
	if(to_user_name.value==''){
		alert('<?php echo $m_langpackage->m_pl_getgoods_name;?>');
		return false;
	}

	var province = document.getElementsByName('province')[0];
	if(province.value==0){
		alert('<?php echo $m_langpackage->m_pl_getgoods_province;?>');
		return false;
	}

	var city = document.getElementsByName('city')[0];
	if(city.value==0){
		alert('<?php echo $m_langpackage->m_pl_getgoods_city;?>');
		return false;
	}

	var district = document.getElementsByName('district')[0];
	if(district.value==0){
		alert('<?php echo $m_langpackage->m_pl_getgoods_district;?>');
		return false;
	}

	var full_address = document.getElementsByName('full_address')[0];
	if(full_address.value==''){
		alert('<?php echo $m_langpackage->m_pl_getgoods_address;?>');
		return false;
	}

	var zipcode = document.getElementsByName('zipcode')[0];
	if(zipcode.value==''){
		alert('<?php echo $m_langpackage->m_pl_getgoods_zipcode;?>');
		return false;
	}

	var email = document.getElementsByName('email')[0];
	var user_email_reg = /^[0-9a-zA-Z_\-\.]+@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/;
	if(!email.value=='' && !user_email_reg.test(email.value)){
		alert('<?php echo $m_langpackage->m_email_type_notine;?>');
		return false;
	}

	var user_mobile = document.getElementsByName('mobile')[0];
	var user_mobile_reg = new RegExp("[0-9-]{5,15}");

	var user_telphone = document.getElementsByName('telphone')[0];
	var user_telphone_reg = new RegExp("[0-9-]{5,15}");

	if(user_mobile.value=='' && user_telphone.value=='') {
		alert('<?php echo $m_langpackage->m_sorry_p_mselectone;?>');
		return false;
	} else if(!user_mobile.value=='' && !user_mobile_reg.test(user_mobile.value)) {
		alert('<?php echo $m_langpackage->m_sorry_mobiletype;?>');
		return false;
	} else if(!user_telphone.value=='' && !user_telphone_reg.test(user_telphone.value)) {
		alert('<?php echo $m_langpackage->m_sorry_phonetype;?>');
		return false;
	}else {
		return true;
	}

}
//-->
</script>
</body>
</html><?php } ?>