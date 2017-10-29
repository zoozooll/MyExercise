<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>成绩查询</title>
</head>

<body>
<div>
<form action="stuCource.php" method="post">
	<select name="term">
		<option selected>请选择学期</option>
		<option value="">第一学期</option>
		<option>第二学期</option>
		<option>第三学期</option>
		<option>第四学期</option>
		<option>第五学期</option>
		<option>第六学期</option>
		<option>第七学期</option>
		<option>第八学期</option>
	</select>
    <select name="select">
      <option value="all" selected>全部</option>
	  <option value="all">必修</option>
	  <option value="all">选修</option>
	  <option value="all">其他考试</option>
    </select>
    <input type="submit" name="Submit" value="查询">
</form>
<table border="0">
  <tr>
    <th>学期</th>
    <th>学科</th>
    <th>成绩</th>
	<th>课程类别</th>
    <th>学分（无学分为不及格）</th>
	<th>评分老师</th>
  </tr>
  <tr>
    <td>1：2008秋</td>
    <td>JAVA设计</td>
    <td>98</td>
    <td>必修</td>
	<td>4</td>
	<td>张老师</td>
  </tr>
</table>

</div>
</body>
</html>
