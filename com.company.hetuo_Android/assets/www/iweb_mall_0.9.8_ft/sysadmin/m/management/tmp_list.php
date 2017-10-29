<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$u_langpackage=new uilp;
$a_langpackage=new adminlp;

$loc=short_check(get_args('loc'));

if(empty($loc)){
	echo "<script type='text/javascript'>alert('$u_langpackage->u_file_no $u_langpackage->u_file_no');window.history.go(-1);</script>";
}

	$u_amend=$u_langpackage->u_amend;
function list_child_file($local){
	global $u_amend;
	global $loc;
	$ref=opendir("../templates/".$local);
	while($tp_dir=readdir($ref)){
		if(!preg_match("/^\./",$tp_dir)){
			if(filetype("../templates/".$local."/".$tp_dir)=="dir"){
				list_child_file($local."/".$tp_dir);
			}
			if(filetype("../templates/".$local."/".$tp_dir)=="file"){
				$act_time=date("Y-m-d H:i:s",filemtime("../templates/".$local));
				$show_local=$local.'/'.$tp_dir;
				$show_local=preg_replace("/$loc\//","",$show_local);
				echo '<tr style="text-align:center;"><td class="center"><input type="checkbox" name="c_tmp[]" value="'.$show_local.'" /></td>
				<td style="color:blue" align="left">&nbsp;'.$show_local.'</td>
				<td class="center">'.$act_time.'</td>
				<td class="center"><a href="m.php?app=tmp_change&tmp_path='.$local.'/'.$tp_dir.'">'.$u_amend.'</a></td></tr>';
			}
		}
	}
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo $u_langpackage->u_template_list; ?></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<script type='text/javascript'>

ifcheck =true;
function all_select(form)
{
	for(i = 0; i < form.elements.length; i ++)
	{
		var e = form.elements[i];
		if (e.type == 'checkbox')
		{
			e.checked = ifcheck;
		}
	}
	ifcheck = (ifcheck == true) ? false : true;
}

</script>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_application_management; ?> &gt;&gt; <?php echo $u_langpackage->u_tempfile_list?></div>
        <hr />
	<div class="infobox">
	<h3><span class="left"><?php echo $u_langpackage->u_tempfile_list?></span> <span class="right" style="margin-right:15px;"><a href="m.php?app=manage_template"><?php echo $u_langpackage->u_back_template_list; ?></a></span></h3>
    <div class="content2">
		<form action='m.php?app=action_comp&pro=<?php echo $loc;?>' method='post'>
		<table class="list_table">
			<thead>
			<tr style="text-align:center;">
				<th width="35px"> <?php echo $u_langpackage->u_choice?> </th>
				<th width="300px" align="left"> <?php echo $u_langpackage->u_file_name?> </th>
				<th> <?php echo $u_langpackage->u_amend_time?> </th>
				<th> <?php echo $u_langpackage->u_admin_handle?> </th>
			</tr>
			</thead>
			<?php
				list_child_file($loc);
			?>
			<tbody>
			<tr>
				<td colspan='4'>
					<span class="button-container"><input class="regular-button" type="button" value="<?php echo $u_langpackage->u_check?>" onclick="all_select(form);" /></span>
					<span class="button-container"><input class="regular-button" type="submit" value="<?php echo $u_langpackage->u_compile?>" name='batch' /></span>
					<span class="button-container"><input class="regular-button" type="button" onclick='javascript:history.go(-1);' value="<?php echo $u_langpackage->u_list_back?>" /></spam>
				</td>
			</tr>
			</tbody>
		</table>
		</form>

		<table class="list_table">
		  <tbody>
			<tr><td><?php echo $u_langpackage->u_clew_inf?>：<?php echo str_replace('{temp}',$loc,$u_langpackage->u_temp_save)?></td></tr>
			<tr>
				<td>
				&nbsp;<?php echo $u_langpackage->u_do_flow?> <br />
				&nbsp;<?php echo $u_langpackage->u_flow_1?><br />
				&nbsp;<?php echo $u_langpackage->u_flow_2?><br />
				&nbsp;<?php echo $u_langpackage->u_flow_3?><br />
				&nbsp;<?php echo $u_langpackage->u_flow_4?><br />
				&nbsp;<?php echo $u_langpackage->u_flow_5?><br />
				&nbsp;<?php echo $u_langpackage->u_flow_6?><br />
				</td>
			</tr>
		  </tbody>
		</table>
	   </div>
	 </div>
   </div>
</div>
</body>
</html>