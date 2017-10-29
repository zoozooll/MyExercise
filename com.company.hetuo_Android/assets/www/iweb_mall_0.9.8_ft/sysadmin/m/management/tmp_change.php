<?php
//	require("../foundation/asession.php");
//	require("../configuration.php");
//	require("includes.php");
	require("../foundation/module_admin_logs.php");
	//语言包引入
	$u_langpackage=new uilp;
	$a_langpackage=new adminlp;

	//数据表定义区
	$t_admin_log = $tablePreStr."admin_log";

	//定义读操作
	dbtarget('w',$dbServs);
	$dbo=new dbex;

	//用save_tmp来判断读写操作
	if(get_args('save_tmp')){

		if(get_magic_quotes_gpc()){
			$tmp_contents=stripslashes(get_args('tmp_code'));
		}else{
			$tmp_contents=get_args('tmp_code');
		}

		$tmp_contents=str_replace(array("&lt;","&gt;"),array("<",">"),$tmp_contents);

		$t_whole_path=get_args('whole_path');

		$tmp_type=get_args('tmp_type');

		$tmp_ref=fopen($t_whole_path,"w");

		fwrite($tmp_ref,$tmp_contents);

		fclose($tmp_ref);

		/** 添加log */
		$admin_log ="更新模板文件";
		admin_log($dbo,$t_admin_log,$admin_log);

		echo "<script type='text/javascript'>alert('$u_langpackage->u_amend_suc');window.location.href='m.php?app=tmp_list&loc=$tmp_type';</script>";

	}else{

		$tmp_path=get_args('tmp_path');//接受路径名

		$ex_path=explode("/",$tmp_path);//切割路径取得所属模板

		$tmp_type=$ex_path[0];//赋值所属模板

		$last_c_time=date("Y-m-d H:i:s",fileatime("../templates/".$tmp_path));//取得文件上次修改时间

		$whole_path="../templates/".$tmp_path;//完整路径名

		$file_contents=file_get_contents($whole_path);//把文件内容读取到变量中

		$file_contents=str_replace(array("<",">"),array("&lt;","&gt;"),$file_contents);
	}

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo $u_langpackage->u_template_list; ?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $u_langpackage->u_amend_temp?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $u_langpackage->u_amend_temp?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=manage_template"><?php echo $u_langpackage->u_back_template_list; ?></a></span></h3>
    <div class="content2">
		<form action="" method="post">
		<input type='hidden' value='<?php echo $tmp_type;?>' name='tmp_type' />
		<input type='hidden' value='<?php echo $whole_path;?>' name='whole_path' />
		<table class="list_table" >
		<tbody>
			<tr>
				<td width="60px"><?php echo $u_langpackage->u_belong_temp?>：</td>
				<td><?php echo $tmp_type;?></td>
			</tr>
			<tr>
				<td><?php echo $u_langpackage->u_temp_path?>：</td><td><font color=blue><?php echo "../templates/".$tmp_path;?></font>（<?php echo $u_langpackage->u_last_amend_time?>：<?php echo $last_c_time;?>）<font color=red>*</font></td>
			</tr>
			<tr>
				<td colspan="2">
					<textarea align="left" wrap='off' style='width:100%;height:360px;overflow:auto;scrolling:yes;font-family:Fixedsys,verdana,宋体;font-size:12px;' id='tmp_code' name='tmp_code'>
		<?php echo $file_contents;?>
					</textarea>
				</td>
			</tr>
			<tr>
				<td colspan='2'>
					<span class="button-container"><input class="regular-button" type='submit' value='<?php echo $u_langpackage->u_save?>' name="save_tmp" /></span>&nbsp
					<span class="button-container"><input class="regular-button" type='reset' value='<?php echo $u_langpackage->u_reset?>' /></span>&nbsp
					<span class="button-container"><input class="regular-button" type='button' value='<?php echo $u_langpackage->u_list_back?>' onclick='window.history.go(-1);' /></span>
				</td>
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