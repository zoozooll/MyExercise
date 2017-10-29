<?php
?>
<div>
<form action="" method="post" enctype="multipart/form-data">
<table border="0">
  <tr>
    <td>学生姓名</td>
    <td><input name="stuname" type="text"></td>
  </tr>
  <tr>
    <td>性别</td>
    <td><input name="sex" type="radio" value="male">男<input name="sex" type="radio" value="female">女</td>
  </tr>
  <tr>
    <td>生日</td>
    <td>
	<select name="year"></select>年
	<select name="month"></select>月
	<select name="dateOfMonth"></select>日
	</td>
  </tr>
  <tr>
    <td>身份证号码</td>
    <td><input type="text" name="identity"></td>
  </tr>
  <tr>
    <td>学号</td>
    <td><input name="stuno" type="text"></td>
  </tr>
  <tr>
    <td>邮箱</td>
    <td><input name="email" type="text"></td>
  </tr>
  <tr>
    <td>兴趣爱好</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>备注</td>
    <td><textarea name="remark" cols="" rows=""></textarea></td>
  </tr>  <tr>
    <td><input name="" type="submit" value="提交"></td>
    <td><input name="" type="reset" value="重设"></td>
  </tr>
</table>
 <input type="hidden" name="stuId" value="">
</form>
</div>