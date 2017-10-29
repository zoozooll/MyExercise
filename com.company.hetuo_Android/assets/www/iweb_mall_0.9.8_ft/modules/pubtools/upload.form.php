<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/pubtools/upload.form.html
 * 如果您的模型要进行修改，请修改 models/modules/pubtools/upload.form.php
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
if(filemtime("templates/default/modules/pubtools/upload.form.html") > filemtime(__file__) || (file_exists("models/modules/pubtools/upload.form.php") && filemtime("models/modules/pubtools/upload.form.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/pubtools/upload.form.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><title></title>
<style>
body {margin:0;padding:0;background-color:transparent; font-size:12px;}
</style>
</head>
    <body>
    <form name="upload" method="post" action="do.php?act=upload_act" enctype="multipart/form-data" style="margin:0">
        <input type="file" name="attach[]"/> 
        <input type="submit" name="submit" value=" <?php echo  $m_langpackage->m_image_upload;?> " />
         <?php echo  $m_langpackage->m_upload_tolimit;?>
    </form>
    </body>
</html><?php } ?>