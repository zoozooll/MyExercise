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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><title></title>
<style>
body {margin:0;padding:0;background-color:transparent; font-size:12px;}
</style>
</head>
    <body>
    <form name="upload" method="post" action="a.php?act=upload_act" enctype="multipart/form-data" style="margin:0">
        <input type="file" name="attach[]"/> 
        <input type="submit" name="submit" value=" <?php echo  $a_langpackage->a_image_upload;?> " />
         <?php echo  $a_langpackage->a_upload_tolimit;?>
    </form>
    </body>
</html>