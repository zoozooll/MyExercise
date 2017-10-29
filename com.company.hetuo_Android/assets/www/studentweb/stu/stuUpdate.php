<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>修改学生信息</title>
</head>
<body>
<?php
	include("stutop.inc");
	include("stuleft.inc");
?>
<div align="center">
<table border="0">
<tr>
<th colspan="2">修改个人信息</th>
</tr>
<form name="form1" method="post" action="inputNews.php">
  <tr>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td width="71">姓名</td>
    <td width="80">
      <input type="text" name="stuname"/>
    </td>
  </tr>
  <tr>
    <td>学号</td>
    <td><input name="stuno" type="text" disabled="true" readonly="true"></td>
  </tr>
  <tr>
    <td>性别</td>
    <td><input type="radio" name="sex" value="male">男
      <input type="radio" name="sex" value="female">女</td>
  </tr>
  <tr>
    <td>身份证号码</td>
    <td><input type="text" name="indetity"></td>
  </tr>
  <tr>
    <td>EMAIL</td>
    <td><input type="text" name="email"></td>
  </tr>
  <tr>
    <td>备注信息</td>
    <td><textarea name="remark"></textarea></td>
  </tr>
  <tr>
    <td>确认修改</td>
    <td><input type="submit" name="Submit" value="修改"><input name="重置" type="reset" value="重置"></td>
  </tr>
</form>
</table>

</div>
</body>
</html>
