<?php
	//header("content-type:text/html;charset=utf-8");
	require("../foundation/asession.php");
	require("session_check.php");	
	require("../configuration.php");
	require("includes.php");
	
	//语言包引入
	$u_langpackage=new uilp;
	$lp_u_make_suc=$u_langpackage->u_make_suc;
	$lp_u_make_lose=$u_langpackage->u_make_lose;
	$lp_u_cback_suc=$u_langpackage->u_cback_suc;
	$lp_u_cback_lose=$u_langpackage->u_cback_lose;
	
	$re_type=short_check(get_args('r_type'));
	
	function site_re($from_dir,$to_dir){
		global $lp_u_make_suc;
		global $lp_u_make_lose;
		global $lp_u_cback_suc;
		global $lp_u_cback_lose;
		$ref=opendir($from_dir);
		if(!file_exists($to_dir)){
			mkdir($to_dir);
		}
		while($tp_dir=readdir($ref)){
			if(!preg_match("/^\./",$tp_dir)){
				if(filetype($from_dir.$tp_dir)=="dir"){
					if(!file_exists($to_dir.$tp_dir)){
						$is_m=mkdir($to_dir.$tp_dir."/");
						if($is_m==true){
							echo str_replace('{dir}',$to_dir.$tp_dir,$lp_u_make_suc)."<br />";
						}else{
							echo "<font color='red'>". str_replace('{dir}',$to_dir.$tp_dir,$lp_u_make_lose)."</font><br />";
						}
					}
					site_re($from_dir.$tp_dir."/",$to_dir.$tp_dir."/");
				}
				if(filetype($from_dir.$tp_dir)=="file"){
					$show_local=$from_dir.$tp_dir;
					$is_c=copy($show_local,$to_dir.$tp_dir);
					if($is_c==true){
						echo str_replace('{dir}',$tp_dir,$lp_u_cback_suc)." <br />";
					}else{
						echo "<font color='red'>". str_replace('{dir}',$tp_dir,$lp_u_cback_lose)."</font><br />";
					}
				}
			}
		}
	}
	
	switch ($re_type){
		
		case "tmp":
		$str=$u_langpackage->u_cback_temp;
		$from_dir="../defaultview/tpl/";
		$to_dir="../templates/default/";
		break;
		
		case "mod":
		$str=$u_langpackage->u_cback_model;
		$from_dir="../defaultview/models/";
		$to_dir="../models/";
		break;
		
		case "skin":
		$str=$u_langpackage->u_cback_skin;
		$from_dir="../defaultview/skin/";
		$to_dir="../skin/default/";
		break;
		
		case "c_com":
		$str=$u_langpackage->u_cback_all;
		$from_dir="../defaultview/c_files/";
		$to_dir="../";
		break;
		
	}
?>
<html>
<head>
<title><?php echo $u_langpackage->a_ui_back?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="skin/css/right.css">
</head>
<body>
<div class="container">
	<div class="rs_head"><?php echo $str;?></div>
</div>
<table class='main main_left'>
	<tr>
		<td>
			<?php echo site_re($from_dir,$to_dir);?>
		</td>
	</tr>
	<tr>
		<td><input type='button' class='top_button' value='<?php echo $u_langpackage->u_back?>' onclick='window.history.go(-1);' /></td>
	</tr>
<table>
</body>
</html>		