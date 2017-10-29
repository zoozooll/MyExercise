<?php
/*
* 程序名称：162100简约论坛
* 作者：162100.com
* 邮箱：162100.com@163.com
* 网址：http://www.162100.com
* 演示：http://www.162100.com/bbs
* ＱＱ：184830962
* ＱＱ群：106319161 
* 声明：仅供代码爱好者学习交流，禁用于商业目的，请保留此版权信息
*/
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>


<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top">
<?php
require('inc/set_sql.php');
require('inc/set_area.php');
require('inc/function/confirm_manager.php');
require('inc/function/user_class.php');
require('inc/function/getarea.php');

if(confirm_manager()){
  $manage='&manage=yes';
  require('inc/require/admin_left_menu.txt');
  $yes='欢迎管理员！';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
<a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'<br />
<br />
注：<br />
1、如将本简约论坛(utf-8)搭配162100网址导航(gb2312)使用，因为编码不同，请设管理员名称为英文<br />
2、更多帮助参见：<a href="http://www.162100.com/bbs/" target="_blank">http://www.162100.com/bbs/</a><br /><br />

';
?>

<div align="right"><a href="http://www.162100.com/bbs/162100start_help.php" target="_blank"><img src="images/tools/about.gif" />  如何安装使用此论坛</a></div>
<div class="hot">

  <p><img src="images/ok.gif" align="absmiddle" /> 欢迎使用162100.com免费程序！</p>
  <p>/*<br />
    * 程序名称：162100简约论坛<br />
    * 作者：162100.com<br />
    * 邮箱：162100.com@163.com<br />
    * 网址：http://www.162100.com<br />
    * ＱＱ：184830962<br />
    * ＱＱ群：106319161 <br />
    * 声明：仅供个人使用，禁止用于商业目的，特别是侵犯版权或程序转发的行为，否则我方将追究法律责任<br />
    */</p>
  <p>【程序简介】</p>
  <ol>
    <li>162100.com原创作品，PHP+Mysql构架</li>
    <li>短小精悍(安装包仅190KB)</li>
    <li>无限制设立分论坛、栏目</li>
    <li>允许过客发言设置</li>
    <li>所见即所得的在线编辑器</li>
    <li>随心更换的站点名称、logo，便于应用</li>
    <li>轻松管理广告位</li>
    <li>轻松管理友情链接功能</li>
    <li>轻松在线直接修改文件功能</li>
    <li>可与162100网址导航系统(特别是UTF-8版)完美整合</li>
  </ol>
  <p>【安装】</p>
  <ol>
    <li>打开压缩包直接上传至支持PHP+Mysql的空间即可使用</li>
    <li>登录后点击管理员小钥匙图标进入后台→设置数据库→建立数据库表，完毕</li>
    <li>管理员地址：admin.php。基本管理员名称admin，基本管理员密码admin。数据库安装完毕后可更改数据库中的密码副本</li>
    <li>使用时请确保你的目录（特别是data/、power/、）权限充足。</li>
    <li>更改空间站点的读写权限的一般方法为：用FTP工具设置目标目录权限大于644（window系统下，设文件夹属性中web共享即可）。 </li>
  </ol>
  <p>【升级】</p>
  <ol>
    <li>升级或咨询地址：<a href="http://download.162100.com">http://download.162100.com</a></li>
    <li>欢迎您在使用过程中及时将发现的问题汇报与我方：<a href="http://www.162100.com/bbs">http://www.162100.com/bbs</a>，以便修正此程序。</li>
  </ol>
  <p>【版权声明】</p>
  <ol>
    <li>162100网址导航当前版本为免费，您下载后可以无偿使用，但不能以本程序进行商业交易或程序转发的形为。否则我方有追究法律责任的权利。</li>
    <li>使用者如使用、撷取162100简约论坛的整体或部分程序，都应在您的网页中标记我方版权信息、或链接信息。</li>
    <li>使用者对下载、安装、使用或转载本程序而造成的纠纷或违规行为自承担全部责任。</li>
  </ol>
  <p>【增值产品或服务】</p>
  <ol>
    <li>同时欢迎<a href="http://www.162100.com/service.html">购买赠值产品或服务</a>。</li>
  </ol>
</div>





<?php

}else{
  echo '<img src="images/i.gif" align="absmiddle" /> 请以基本管理员'.$web['manager'].'<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录</b></a>，以获得管理权限';
}

?>    </td>
  </tr>
</table>
<br />
<br />
<br />
<br />

<div id="foot"><?php require('inc/require/foot.txt'); ?></div>
</body>
</html>
