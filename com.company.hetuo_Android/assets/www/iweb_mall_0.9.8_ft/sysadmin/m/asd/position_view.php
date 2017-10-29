<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

$position_id = intval(get_args('id'));
//权限管理
$right=check_rights("adv_show");
if(!$right){
	header('location:m.php?app=error');
}
//引入语言包
$a_langpackage=new adminlp;

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><?php echo $a_langpackage->a_view_asd; ?></title>
<base href="<?php echo $baseUrl; ?>" />
<style>
img {border:0px;}
</style>
</head>
<body>
<script language="JavaScript" src="uploadfiles/asd/<?php echo $position_id;?>.js"></script>
</body>
</html>