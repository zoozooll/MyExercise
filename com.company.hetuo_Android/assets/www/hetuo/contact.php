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
          <td><a href="http://<?php echo $_SERVER['HTTP_HOST']?>/contact.php">联系我们</a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody">
      <table border="0" cellpadding="0" cellspacing="0" 
width="690px" id="new_pro">
        <tbody>
          <tr>
            <th >联系我们</th>
          </tr>
          <tr>
            <td  align="left" valign="top" height="10">&nbsp;</td>
          </tr>
          <tr>
            <td align="left" valign="top" ><pre style=" width:600px;font:14px/24px normal;padding:10px;margin:5px 0;	border:1px #eee solid;	color:#333;
	text-align:left;">
	公司名称：合拓科技有限公司
	地    址：北京市海淀区西三环北路50号豪柏大厦B2座1101B室
	邮    编: 100048
	电    话: 010-68485908
	传    真: 010-68485908
	E - mail: info@hetuo-tech.com
   </pre>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>