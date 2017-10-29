<?php
$html = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset='.__CHARSET.'" />
<title>－'.$mailsubject.'－</title>
</head>

<body>
<table width="100%" border="1">
  <tr>
    <td>－'.$mailsubject.' 注册成功！－</td>
  </tr>
  <tr>
    <td></td>
  </tr>
  <tr>
    <td></td>
  </tr>
  <tr>
    <td></td>
  </tr>
  <tr>
    <td></td>
  </tr>
  <tr>
    <td>您的注册信息：</td>
  </tr>
   <tr>
    <td>您的昵称：'.$name.'</td>
  </tr>
  <tr>
    <td>您的email：'.$email.'</td>
  </tr>
  <tr>
    <td>您的密码：'.$pass.'</td>
  </tr>
</table>
</body>
</html>
';?>
