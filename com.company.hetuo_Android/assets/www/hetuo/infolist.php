<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="style/style.css" rel="stylesheet" type="text/css" />
<title>无标题文档</title>
</head>
<body>
<div id="container">
  <?php require_once("header.php");?>
  <!--header end-->
  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>您的位置：</td>
          <td><a href="http://<?php echo $_SERVER['HTTP_HOST']?>/index.php">首页</a></td>
          <td>>></td>
          <td><a href="http://<?php echo $_SERVER['HTTP_HOST']?>/index.php">信息专栏</a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody" class="info_list">
    	<div id="news_info">
      <h1>新闻列表</h1>
      <ul>
      	<li>
        	<a href="new.php?">本公司终点新闻1</a>
            <span>2010-10-1</span>
        </li>
        <li>
        	<a href="new.php?">本公司终点新闻1</a>
            <span>2010-10-1</span>
        </li>
        <li>
        	<a href="new.php?">本公司终点新闻11</a>
            <span>2010-10-7</span>
        </li>
        <li>
        	<a href="new.php?">本公司终点新闻11</a>
            <span>2010-10-7</span>
        </li>
      </ul>
      </div>
          	<div id="sec_info" style="margin-top:40px">
      <h1>技术资料</h1>
      <ul>
      	<li>
        	<a href="infomation.php?">本公司终点新闻1</a>
            <span>2010-10-1</span>
        </li>
        <li>
        	<a href="infomation.php?">本公司终点新闻1</a>
            <span>2010-10-1</span>
        </li>
        <li>
        	<a href="infomation.php?">本公司终点新闻11</a>
            <span>2010-10-7</span>
        </li>
        <li>
        	<a href="infomation.php?">本公司终点新闻11</a>
            <span>2010-10-7</span>
        </li>
      </ul>
      </div>
    </div>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>