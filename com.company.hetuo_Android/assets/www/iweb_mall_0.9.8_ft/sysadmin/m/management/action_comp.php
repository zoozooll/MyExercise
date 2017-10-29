<?php
	require("../foundation/ftpl_compile.php");
	require("../foundation/module_admin_logs.php");
	//语言包引入
	$u_langpackage=new uilp;
	$a_langpackage=new adminlp;
	//权限管理
	$right=check_rights("template_operate");
	if(!$right){
		header('location:m.php?app=error');
		exit;
	}
	//数据表定义
	$t_settings=$tablePreStr."settings";

	//数据库连接
	$dbo=new dbex;
	dbtarget('r',$dbServs);

	$sql="select * from `$t_settings` where variable='template_mode'";
	$row = $dbo->getRow($sql);
	$template_mode = $row['value'];

	//获得方案名
	$loc=short_check(get_args('pro'));

	if(empty($loc)){
		echo "<script type='text/javascript'>alert('$u_langpackage->u_file_no');window.history.go(-1);</script>";
	}

	//数据表定义
	$t_settings=$tablePreStr."settings";
	$t_admin_log = $tablePreStr."admin_log";

	//数据库连接
	$dbo=new dbex;
	dbtarget('w',$dbServs);

	$sql="update $t_settings set value='$loc' where variable='templates'";
	$dbo->exeUpdate($sql);

	if(get_args('batch')){
		$c_tmp=get_args('c_tmp');

		if (!$c_tmp){
			echo "<script language='javascript'> alert('".$a_langpackage->a_need_select_one."'); history.go(-1);</script>";
			exit;
		}

		//批量生成模板
		function batch_comp($loc,$c_tmp){
			global $template_mode;
			foreach($c_tmp as $value){
				if($template_mode=='service') {
					tpl_engine($loc,$value,0,'service');
				} else {
					tpl_engine($loc,$value);
				}
			}
		}
	}else{
		//直接应用模板
		function list_child_file($local){
			global $template_mode;
			$ref=opendir("../templates/".$local);
			while($tp_dir=readdir($ref)){
				if(!preg_match("/^\./",$tp_dir)){
					if(filetype("../templates/".$local."/".$tp_dir)=="dir"){
						list_child_file($local."/".$tp_dir);
					}
					if(filetype("../templates/".$local."/".$tp_dir)=="file"){
						global $loc;
						$show_local=$local.'/'.$tp_dir;
						$show_local=preg_replace("/$loc\//","",$show_local);
						if($template_mode=='service') {
							tpl_engine($loc,$show_local,0,'service');
						} else {
							tpl_engine($loc,$show_local);
						}
					}
				}
			}
		}
		$dbo=new dbex;
		dbtarget('r',$dbServs);
		$sql="SELECT * FROM $t_settings";
		$content = $dbo->getRsassoc($sql);
		foreach ($content as $key=>$value){
			$sysinfo[$value["variable"]]=$value['value'];
		}
		array_shift($sysinfo);
		put_file($sysinfo);
	}
	/** 添加log */
	$admin_log ="编译模板";
	admin_log($dbo,$t_admin_log,$admin_log);

	function put_file($sysinfo) {
		$content = '<'.'?php'."\n";
		$content .= "if(!".'$'."IWEB_SHOP_IN) {die('Hacking attempt');} \n";
		foreach($sysinfo as $k=>$v) {
			$v = str_replace('\"','"',$v);
			$content .= '$'."SYSINFO['$k'] = '$v'; \n";
		}
		$content .= '?'.'>';
		file_put_contents("../cache/setting.php",$content);
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
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $u_langpackage->u_compile_state?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $u_langpackage->u_compile_state?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=manage_template"><?php echo $u_langpackage->u_back_template_list; ?></a></span></h3>
    <div class="content2">
		<table class="list_table">
			<tbody>
				<tr><td><?php if(get_args('batch')){batch_comp($loc,$c_tmp);}else{list_child_file($loc);}?></td></tr>
				<tr><td><span class="button-container"><input class="regular-button" type='button' value='<?php echo $u_langpackage->u_list_back; ?>' onclick='window.history.go(-1);' /></span></td></tr>
			</tbody>
		</table>
	   </div>
	 </div>
	</div>
</div>
</body>
</html>