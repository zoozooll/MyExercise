<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/asystem_info.php");

//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_settings = $tablePreStr."settings";

dbtarget('r',$dbServs);
$dbo=new dbex;

$sql = "select * from `$t_settings`";
$result = $dbo->getRs($sql);
if($result) {
	foreach($result as $v) {
		$SYSINFO[$v['variable']] = $v['value'];
	}
}
function show_back_lp($def_lp){
	echo "<select name='sysinfo[lp]'>";
	$res=opendir("../langpackage");
	while($lp_dir=readdir($res)){
		if(!preg_match("/^\./",$lp_dir)){
			$l_selected='';
			if($lp_dir==$def_lp){$l_selected="selected";}
			$lp_tip=trim(file_get_contents("../langpackage"."/".$lp_dir."/"."tip.php"));
			echo "<option value='".$lp_dir."' ".$l_selected.">".$lp_tip."</option>";
		}
	}
echo "</select>";
}

$all_timezone = array(
	'-12' => '(GMT -12:00) Eniwetok, Kwajalein',
	'-11' => '(GMT -11:00) Midway Island, Samoa',
	'-10' => '(GMT -10:00) Hawaii',
	'-9' => '(GMT -09:00) Alaska',
	'-8' => '(GMT -08:00) Pacific Time (US &amp; Canada), Tijuana',
	'-7' => '(GMT -07:00) Mountain Time (US &amp; Canada), Arizona',
	'-6' => '(GMT -06:00) Central Time (US &amp; Canada), Mexico City',
	'-5' => '(GMT -05:00) Eastern Time (US &amp; Canada), Bogota, Lima, Quito',
	'-4' => '(GMT -04:00) Atlantic Time (Canada), Caracas, La Paz',
	'-3.5' => '(GMT -03:30) Newfoundland',
	'-3' => '(GMT -03:00) Brassila, Buenos Aires, Georgetown, Falkland Is',
	'-2' => '(GMT -02:00) Mid-Atlantic, Ascension Is., St. Helena',
	'-1' => '(GMT -01:00) Azores, Cape Verde Islands',
	'0' => '(GMT) Casablanca, Dublin, Edinburgh, London, Lisbon, Monrovia',
	'1' => '(GMT +01:00) Amsterdam, Berlin, Brussels, Madrid, Paris, Rome',
	'2' => '(GMT +02:00) Cairo, Helsinki, Kaliningrad, South Africa',
	'3' => '(GMT +03:00) Baghdad, Riyadh, Moscow, Nairobi',
	'3.5' => '(GMT +03:30) Tehran',
	'4' => '(GMT +04:00) Abu Dhabi, Baku, Muscat, Tbilisi',
	'4.5' => '(GMT +04:30) Kabul',
	'5' => '(GMT +05:00) Ekaterinburg, Islamabad, Karachi, Tashkent',
	'5.5' => '(GMT +05:30) Bombay, Calcutta, Madras, New Delhi',
	'5.75' => '(GMT +05:45) Katmandu',
	'6' => '(GMT +06:00) Almaty, Colombo, Dhaka, Novosibirsk',
	'6.5' => '(GMT +06:30) Rangoon',
	'7' => '(GMT +07:00) Bangkok, Hanoi, Jakarta',
	'8' => '(GMT +08:00) Beijing, Hong Kong, Perth, Singapore, Taipei',
	'9' => '(GMT +09:00) Osaka, Sapporo, Seoul, Tokyo, Yakutsk',
	'9.5' => '(GMT +09:30) Adelaide, Darwin',
	'10' => '(GMT +10:00) Canberra, Guam, Melbourne, Sydney, Vladivostok',
	'11' => '(GMT +11:00) Magadan, New Caledonia, Solomon Islands',
	'12' => '(GMT +12:00) Auckland, Wellington, Fiji, Marshall Island'
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
<script type='text/javascript'>

function GET_TOP_URL(){
	var location_site="http://"+location.host+location.pathname+location.search;
	var no_file_url=location_site.replace(/.[^\/]*$/g,"/");
	var no_dir_url=no_file_url.replace(/\/sysadmin\//,"/");
	return no_dir_url;
}

function show_domain(){
	var domain_url=GET_TOP_URL();
	document.getElementById('domain_url').innerHTML=domain_url;
	var costom_url=document.getElementById("web_site_domain").value;
	if(costom_url!=domain_url){
		document.getElementById("web_site_domain").style.color='red';
		document.getElementById('domain_url').style.color='red';
	}

}

function check_domain(){
	var domain_url=GET_TOP_URL();
	var costom_url=document.getElementById("web_site_domain").value;
	if(domain_url!=costom_url){
		show_domain();
		return confirm('<?php echo $a_langpackage->a_setting_message1;?>');
	}
}

function set_domain(){
	var domain_url=GET_TOP_URL();
	document.getElementById("web_site_domain").value=domain_url;
	document.getElementById("web_site_domain").style.color='';
	document.getElementById('domain_url').style.color='';
}
function goto_smtptest() {
	var smtp_test = document.getElementById('sys_smtptest').value;
	location.href = "m.php?app=sys_ckmail&smtp_test="+smtp_test;
}
</script>
</head>
<body onload='show_domain()'>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings;?> &gt;&gt; <a href=""><?php echo $a_langpackage->a_set_site;?></a></div>
        <hr />
	<div class="infobox">
		<form action="a.php?act=sys_setting_update" method="post" onsubmit="return check_domain()" enctype="multipart/form-data">
		<h3><?php echo $a_langpackage->a_set_site; ?></h3>
        <div class="content2">
        	<table class="form-table">
				<tbody>
				<tr>
					<th><?php echo $a_langpackage->a_mall_name;?>：</th>
					<td><input class="small-text" type="text" name="sysinfo[sys_name]" value="<?php echo $SYSINFO['sys_name']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message22;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_mall_logo;?>：</th>
					<td><input type="file" name="logo_images[]" style="width:215px;" /><?php echo $a_langpackage->a_setting_message23;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_mall_title;?>：</th>
					<td><input class="small-text" type="text" name="sysinfo[sys_title]" value="<?php echo $SYSINFO['sys_title']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message2;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_mall_key;?>：</th>
					<td><input class="small-text" type="text" name="sysinfo[sys_keywords]" value="<?php echo $SYSINFO['sys_keywords']; ?>" style="width:300px;" /><?php echo $a_langpackage->a_setting_message3;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_mall_descibe;?>：</th>
					<td><textarea name="sysinfo[sys_description]" style="width:350px; height:80px;"><?php echo $SYSINFO['sys_description']; ?></textarea><?php echo $a_langpackage->a_setting_message4;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_company;?>：</th>
					<td><input class="small-text" type="text" name="sysinfo[sys_company]" value="<?php echo $SYSINFO['sys_company']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message24;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_copyright_info;?>：</th>
					<td><input class="small-text" type="text" name="sysinfo[sys_copyright]" value="<?php echo $SYSINFO['sys_copyright']; ?>" style="width:300px;" /><?php echo $a_langpackage->a_setting_message5;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_ICP;?>：</th>
					<td><input class="small-text" type="text" name="sysinfo[sys_icp]" value="<?php echo $SYSINFO['sys_icp']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message6;?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_countjs_code;?>：</th>
					<td><textarea name="sysinfo[sys_countjs]" style="width:350px; height:80px;"><?php if(isset($SYSINFO['sys_countjs'])) echo $SYSINFO['sys_countjs']; ?></textarea><?php echo $a_langpackage->a_setting_message21; ?></td>
				</tr>
				<tr>
					<th><?php echo $a_langpackage->a_register_rule;?>：</th>
					<td><textarea name="sysinfo[sys_registerinfo]" style="width:350px; height:80px;"><?php echo $SYSINFO['sys_registerinfo']; ?></textarea><?php echo $a_langpackage->a_setting_message7;?></td>
				</tr>
				</tbody>
		</table>
		<table class="form-table">
			<tbody>
			<tr>
				<th><?php echo $a_langpackage->a_service_tel;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[sys_kftelphone]" value="<?php echo $SYSINFO['sys_kftelphone']; ?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_service_qq;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[sys_kfqq]" value="<?php echo $SYSINFO['sys_kfqq']; ?>" style="width:200px;" /></td>
			</tr>
			</tbody>
		</table>
		<table class="form-table">
			<tbody>
			<tr>
				<th><?php echo $a_langpackage->a_send_emal;?>：</th>
				<td><select name='sysinfo[email_send]'><option value='true' <?php if ($SYSINFO['email_send']=='true') echo 'selected';?>><?php echo $a_langpackage->a_open;?></option><option value='false' <?php if ($SYSINFO['email_send']=='false') echo 'selected';?>><?php echo $a_langpackage->a_close;?></option></select><?php echo $a_langpackage->a_send_emal_mess;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_smpt_service;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[sys_smtpserver]" value="<?php echo $SYSINFO['sys_smtpserver']; ?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_smpt_port;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[sys_smtpserverport]" value="<?php echo $SYSINFO['sys_smtpserverport']; ?>" style="width:50px;" /></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_smpt_email;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[sys_smtpusermail]" value="<?php echo $SYSINFO['sys_smtpusermail']; ?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_smpt_user_port;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[sys_smtpuser]" value="<?php echo $SYSINFO['sys_smtpuser']; ?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_smpt_user_password;?>：</th>
				<td><input type="password" class="small-text" name="sysinfo[sys_smtppass]" value="<?php echo $SYSINFO['sys_smtppass']; ?>" style="width:200px;" /></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_mail_test;?>：</th>
				<td><input class="small-text" type="text" id="sys_smtptest" name="sys_smtptest" style="width:200px;" />&nbsp;&nbsp;<a href="javascript:goto_smtptest();"><?php echo $a_langpackage->a_mail_test;?></a>（请填写好邮箱测试帐号后点击发送）</td>
			</tr>
			</tbody>
		</table>
		<table class="form-table">
			<tbody>
			<tr>
				<th><?php echo $a_langpackage->a_web_datalog_site;?>：</th>
				<td> <input type='text' size='30' value='<?php echo $SYSINFO['web'];?>' name='sysinfo[web]' id='web_site_domain' /><?php echo $a_langpackage->a_setting_message10;?><a href='javascript:set_domain();' title='<?php echo $a_langpackage->a_setting_message11;?>'><span id='domain_url'></span></a>） </td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_language_default;?>：</th>
				<td><?php echo show_back_lp($SYSINFO['lp']);?><?php echo $a_langpackage->a_setting_message9;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_setting_timezone;?>：</th>
				<td><select name='sysinfo[timezone]'>
				<?php foreach($all_timezone as $key=>$value) { ?>
					<option value='<?php echo $key; ?>' <?php if ($SYSINFO['timezone']==$key) echo 'selected';?> ><?php echo $value; ?></option>
				<?php } ?>
				</select></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_set_session;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[session]" value="<?php echo $SYSINFO['session']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message8;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_url_rewrite_is_open;?>：</th>
				<td>
					<input name='sysinfo[url_r]' type="radio" value="false" <?php if ($SYSINFO['url_r']=='false') echo 'checked';?> /><?php echo $a_langpackage->a_close;?>
					<input name='sysinfo[url_r]' type="radio" value="2" <?php if ($SYSINFO['url_r']=='2') echo 'checked';?> /><?php echo $a_langpackage->a_open_simplerewrite;?>
					<input name='sysinfo[url_r]' type="radio" value="1" <?php if ($SYSINFO['url_r']=='1') echo 'checked';?> /><?php echo $a_langpackage->a_open_complexrewrite;?>
					<br /><?php echo $a_langpackage->a_setting_message17;?>
				</td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_domain;?>：</th>
				<td>
					<input name='sysinfo[sys_domain]' type="radio" value="0" <?php if ($SYSINFO['sys_domain']=='0') echo 'checked';?> /><?php echo $a_langpackage->a_no;?>
					<input name='sysinfo[sys_domain]' type="radio" value="1" <?php if ($SYSINFO['sys_domain']=='1') echo 'checked';?> /><?php echo $a_langpackage->a_yes;?>
					&nbsp;&nbsp;<?php echo $a_langpackage->a_open_domain_word;?>
				</td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_IM_is_open;?>：</th>
				<td><select name='sysinfo[im_enable]'><option value='true' <?php if ($SYSINFO['im_enable']=='true') echo 'selected';?>><?php echo $a_langpackage->a_open;?></option><option value='false' <?php if ($SYSINFO['im_enable']=='false') echo 'selected';?>><?php echo $a_langpackage->a_close;?></option></select><?php echo $a_langpackage->a_setting_message18;?><A href="http://tech.jooyea.com/download.php" target=_blank><?php echo $a_langpackage->a_setting_message19;?></A>）</td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_site_close;?>：</th>
				<td><select name='sysinfo[offline]'><option value='true' <?php if ($SYSINFO['offline']=='true') echo 'selected';?>><?php echo $a_langpackage->a_open;?></option><option value='false' <?php if ($SYSINFO['offline']=='false') echo 'selected';?>><?php echo $a_langpackage->a_close;?></option></select><?php echo $a_langpackage->a_setting_message20;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_site_close_intro;?>：</th>
				<td>
					<textarea style="width:350px; height:80px;" name='sysinfo[off_info]'><?php echo $SYSINFO['off_info'];?></textarea><?php echo $a_langpackage->a_setting_message12;?></td>
			</tr>
			</tbody>
		</table>
		<table class="form-table">
			<tbody>
			<tr>
				<th><?php echo $a_langpackage->a_set_shop_page;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[seller_page]" value="<?php echo $SYSINFO['seller_page']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message13;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_search_shop_page;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[search_page]" value="<?php echo $SYSINFO['search_page']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message14;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_set_goods_page;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[product_page]" value="<?php echo $SYSINFO['product_page']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message15;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_set_article_page;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[article_page]" value="<?php echo $SYSINFO['article_page']; ?>" style="width:200px;" /><?php echo $a_langpackage->a_setting_message16;?></td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_list_img_high;?>：</th>
				<td><?php echo $a_langpackage->a_high;?>：<input type="text" class="small-text" name="sysinfo[height1]" value="<?php echo $SYSINFO['height1']; ?>" style="width:35px;" />px<?php echo $a_langpackage->a_width;?> ：<input type="text" class="small-text" name="sysinfo[width1]" value="<?php echo $SYSINFO['width1']; ?>" style="width:35px;" />px（<?php echo $a_langpackage->a_list_img_high;?>）</td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_show_img_high;?>：</th>
				<td><?php echo $a_langpackage->a_high;?>：<input type="text" class="small-text" name="sysinfo[height2]" value="<?php echo $SYSINFO['height2']; ?>" style="width:35px;" />px<?php echo $a_langpackage->a_width;?> ：<input type="text" class="small-text" name="sysinfo[width2]" value="<?php echo $SYSINFO['width2']; ?>" style="width:35px;" />px（<?php echo $a_langpackage->a_show_img_high;?>）</td>
			</tr>
			</tbody>
		</table>
		<table class="form-table">
			<tbody>
			<tr>
				<th><?php echo $a_langpackage->a_map_open;?>：</th>
				<td><select name='sysinfo[map]'>
					<option value='true' <?php if ($SYSINFO['map']=='true') echo 'selected';?>><?php echo $a_langpackage->a_open;?></option>
					<option value='false' <?php if ($SYSINFO['map']=='false') echo 'selected';?>><?php echo $a_langpackage->a_close;?></option>
				</select>（<?php echo $a_langpackage->a_map_explain;?>）
				</td>
			</tr>
			<tr>
				<th><?php echo $a_langpackage->a_map_api;?>：</th>
				<td><input class="small-text" type="text" name="sysinfo[map_key]" value="<?php echo $SYSINFO['map_key']; ?>" style="width:300px;" /><?php echo $a_langpackage->a_mapapi_url;?></td>
			</tr>
			</tbody>
		</table>
		<table class="form-table">
			<tbody>
				<tr>
					<th></th>
	                <td><span class="button-container"><input type="submit" class="regular-button" name="submit" value="<?php echo $a_langpackage->a_update_site;?>" /></span></td>
				</tr>
			</tbody>
		</table>
	</form>
	</div>
	</div>
</div>
</div>
</body>
</html>