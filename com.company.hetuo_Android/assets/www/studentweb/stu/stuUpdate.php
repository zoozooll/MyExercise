<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>�޸�ѧ����Ϣ</title>
</head>
<body>
<?php
	include("stutop.inc");
	include("stuleft.inc");
?>
<div align="center">
<table border="0">
<tr>
<th colspan="2">�޸ĸ�����Ϣ</th>
</tr>
<form name="form1" method="post" action="inputNews.php">
  <tr>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td width="71">����</td>
    <td width="80">
      <input type="text" name="stuname"/>
    </td>
  </tr>
  <tr>
    <td>ѧ��</td>
    <td><input name="stuno" type="text" disabled="true" readonly="true"></td>
  </tr>
  <tr>
    <td>�Ա�</td>
    <td><input type="radio" name="sex" value="male">��
      <input type="radio" name="sex" value="female">Ů</td>
  </tr>
  <tr>
    <td>���֤����</td>
    <td><input type="text" name="indetity"></td>
  </tr>
  <tr>
    <td>EMAIL</td>
    <td><input type="text" name="email"></td>
  </tr>
  <tr>
    <td>��ע��Ϣ</td>
    <td><textarea name="remark"></textarea></td>
  </tr>
  <tr>
    <td>ȷ���޸�</td>
    <td><input type="submit" name="Submit" value="�޸�"><input name="����" type="reset" value="����"></td>
  </tr>
</form>
</table>

</div>
</body>
</html>
