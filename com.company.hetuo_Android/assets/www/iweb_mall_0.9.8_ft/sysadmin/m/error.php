<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$a_langpackage=new adminlp;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>
.error_box{width:600px;height:300px;margin:10px auto; border:#F2FD95 1px solid;background:#FAFEE3 url(../skin/default/images/error.png) no-repeat 45px 125px;}
h2{font:26px/2em "黑体";color:#555555;text-align:center;margin:60px 0 40px;}
p{margin-left:180px;font:14px/2em SimSun;}
a{color:#550;text-decoration:none;}
a:hover{text-decoration:underline;}
</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
	<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_mana_center; ?> &gt;&gt; <?php echo $a_langpackage->a_error_message; ?></div>
        <hr />
	<div class="infobox">
	<h3><?php echo $a_langpackage->a_error_message; ?></h3>
    <div class="content2">
	<div class="error_box">
	  <h2><?php echo $a_langpackage->a_no_privilege_oprate; ?></h2>
	  <p><?php echo $a_langpackage->a_sysjump_index; ?></p>
	  <p><a href="m.php" title="<?php echo $a_langpackage->a_clicktojump_index; ?>"><?php echo $a_langpackage->a_clicktojump_index; ?> &gt;&gt;</a></p>
	</div>
	</div>
  </div>
 </div>
</div>

<script language="JavaScript">
setTimeout("jump()",5000);
function jump() {
	location.href="m.php?app=main";
}
</script>
</body>
</html>