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
          <td><a href="http://<?php echo $_SERVER['HTTP_HOST']?>/company.php">公司简介</a></td>
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
            <th >公司简介</th>
          </tr>
          <tr>
            <td  align="left" valign="top" height="10">&nbsp;</td>
          </tr>
          <tr>
            <td align="left" valign="top" ><pre style=" width:600px;font:14px/24px normal;padding:10px;margin:5px 0;	border:1px #eee solid;	color:#333;
	text-align:left;">
   合拓科技有限公司专注于光电领域的技术服务与产品经销，致力于引进国外顶级光电器件制造商的技术与产品，以提供国际一流的产品和服务为宗旨，致力于开拓中国市场，与您携手共进，为国内客户提供优质的产品与服务。合拓科技将以务实的作风，科学的态度，创新的精神，专业的团队服务于客户和合作方，为中国激光科学和工业的发展贡献力量。</pre>
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