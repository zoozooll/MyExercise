<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;
require("foundation/asession.php");
require("configuration.php");
$u = intval($_GET['u']);

if($im_enable && $u) {
	require("includes.php");
	$t_users = $tablePreStr."users";

	$dbo=new dbex();
	dbtarget('r',$dbServs);
	$row = $dbo->getRow("SELECT * FROM chat_users WHERE uid=$u");
	
	if($row) {
		$line_status = $row['line_status'];
	} else {
		$row = $dbo->getRow("select user_id uid, user_name u_name, user_ico u_ico from `$t_users` where user_id=$u");
		if($row) {
			$dbo->exeUpdate("insert into chat_users (uid,u_name,u_ico) values($row[uid],'$row[u_name]','$row[u_ico]')");
		}
		$line_status = 0;
	}
	
	if($line_status) {
		echo 'document.write("<a href=\"iwebim.php#'.$u.'\" target=\"imwindow\"><img src=\"'.$baseUrl.'skin/default/images/imonline.gif\" height=\"19\" /></a>");';
	} else {
		echo 'document.write("<a href=\"iwebim.php#'.$u.'\" target=\"imwindow\"><img src=\"'.$baseUrl.'skin/default/images/imoffline.gif\" height=\"19\" /></a>");';
	}
?>

function iweb_imshow() {
	alert("此功能暂不能使用！");
}

<?php
}
?>