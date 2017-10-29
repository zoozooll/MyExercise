<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/profile.html
 * 如果您的模型要进行修改，请修改 models/modules/user/profile.php
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
if(filemtime("templates/default/modules/user/profile.html") > filemtime(__file__) || (file_exists("models/modules/user/profile.php") && filemtime("models/modules/user/profile.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/profile.html",1);
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
$t_user_info = $tablePreStr."user_info";
$t_areas = $tablePreStr."areas";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$user_info = get_user_info($dbo,$t_user_info,$user_id);
// 用户生日
if($user_info['user_birthday']) {
	$Y = substr($user_info['user_birthday'],0,4);
	$M = substr($user_info['user_birthday'],5,2);
	$D = substr($user_info['user_birthday'],8,2);
} else {
	$Y = $M = $D = 0;
}
// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = $user_info['user_country'] ? $user_info['user_country'] : 1;

$areas_info = get_areas_info($dbo,$t_areas);
//print_r($areas_info[2]);

if($user_info['user_gender']==0) { $user_gender0='checked'; } else { $user_gender0=''; }
if($user_info['user_gender']==1) { $user_gender1='checked'; } else { $user_gender1=''; }
if($user_info['user_gender']==2) { $user_gender2='checked'; } else { $user_gender2=''; }

if($user_info['user_marry']==0) { $user_marry0='checked'; } else { $user_marry0=''; }
if($user_info['user_marry']==1) { $user_marry1='checked'; } else { $user_marry1=''; }
if($user_info['user_marry']==2) { $user_marry2='checked'; } else { $user_marry2=''; }
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<style type="text/css">
td{text-align:left;}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
		<div class="title_uc"><h3><?php echo  $m_langpackage->m_edit_profile;?></h3></div>
			<form action="do.php?act=user_profile" method="post" name="form_profile">
				<table width="98%" class="form_table">
					<tr><td class="textright">Email：</td><td><?php echo  get_sess_user_email();?></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_truename;?>：</td><td><input type="text" name="user_truename" value="<?php echo  $user_info['user_truename'];?>" maxlength="12" /></td></tr>
					<tr>
						<td class="textright"><?php echo  $m_langpackage->m_birthday;?>：</td>
						<td><select name="Y">
								<option value="0"><?php echo  $m_langpackage->m_year;?></option>
							<?php  for($i=1950; $i<=date("Y"); $i++){?>
								<option value="<?php echo  $i;?>" <?php  if($Y==$i){echo 'selected';}?>><?php echo  $i;?></option>
							<?php }?>
							</select>
							<select name="M">
								<option value="0"><?php echo  $m_langpackage->m_month;?></option>
							<?php  for($i=1; $i<=12; $i++){?>
								<option value="<?php echo  $i;?>" <?php  if($M==$i){echo 'selected';}?>><?php echo  $i;?></option>
							<?php }?>
							</select>
							<select name="D">
								<option value="0"><?php echo  $m_langpackage->m_day;?></option>
							<?php  for($i=1; $i<=31; $i++){?>
								<option value="<?php echo  $i;?>" <?php  if($D==$i){echo 'selected';}?>><?php echo  $i;?></option>
							<?php }?>
							</select>
						</td>
					</tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_gender;?>：</td><td><input type="radio" name="user_gender" value="0" <?php echo $user_gender0;?> /><?php echo  $m_langpackage->m_secret;?> <input type="radio" name="user_gender" value="1" <?php echo $user_gender1;?> /><?php echo  $m_langpackage->m_man;?> <input type="radio" name="user_gender" value="2" <?php echo $user_gender2;?> /><?php echo  $m_langpackage->m_woman;?></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_marry;?>：</td><td><input type="radio" name="user_marry" value="0" <?php echo $user_marry0;?> /><?php echo  $m_langpackage->m_secret;?> <input type="radio" name="user_marry" value="1" <?php echo $user_marry1;?> /><?php echo  $m_langpackage->m_unmarried;?> <input type="radio" name="user_marry" value="2" <?php echo $user_marry2;?> /><?php echo  $m_langpackage->m_married;?></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_mobile;?>：</td><td><input type="text" name="user_mobile" value="<?php echo  $user_info['user_mobile'];?>" maxlength="20" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_telphone;?>：</td><td><input type="text" name="user_telphone" value="<?php echo  $user_info['user_telphone'];?>" maxlength="20" /></td></tr>
					<tr><td class="textright">MSN：</td><td><input type="text" name="user_msn" value="<?php echo  $user_info['user_msn'];?>"  maxlength="50" /></td></tr>
					<tr><td class="textright">QQ：</td><td><input type="text" name="user_qq" value="<?php echo  $user_info['user_qq'];?>" maxlength="15" /></td></tr>
					<tr><td class="textright">Skype：</td><td><input type="text" name="user_skype" value="<?php echo  $user_info['user_skype'];?>" maxlength="50" /></td></tr>
					<tr>
						<td class="textright"><?php echo  $m_langpackage->m_stayarea;?>：</td>
						<td>
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
					<tr><td class="textright"><?php echo  $m_langpackage->m_address;?>：</td><td><input type="text" name="user_address" value="<?php echo  $user_info['user_address'];?>" style="width:250px;" maxlength="200" /></td></tr>
					<tr><td class="textright"><?php echo  $m_langpackage->m_zipcode;?>：</td><td><input type="text" name="user_zipcode" value="<?php echo  $user_info['user_zipcode'];?>" maxlength="6" /></td></tr>
					<tr><td colspan="2" class="center"><input type="hidden" name="user_id" value="<?php echo  $user_id;?>" /><input type="submit" name="submit" value="<?php echo  $m_langpackage->m_edit_profile;?>" /></td></tr>
				</table>
			</form>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
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
//-->
</script>
</body>
</html><?php } ?>