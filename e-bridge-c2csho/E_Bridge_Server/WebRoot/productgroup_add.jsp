<%@ page language="java" import="java.util.*" pageEncoding=gbk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'productgroup_add.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
<table border="1" width="200px">
<tr>
<td>&nbsp;���в�Ʒ��</td></tr>
<c:forEach var="group" items="${productGroups}">
<tr>
<td>${group.progId }</td></tr>
</c:forEach>
</table>
<div>
<br/>
<form action="addProductGroup.action" method="post">
<table border="1" width="200px">
<tr>
<td>&nbsp;��Ʒ������</td>
<td>&nbsp;<input type="text" name="progGroupname"></td></tr>
<tr>
<td>&nbsp;��Ʒ��ȫ��</td>
<td>&nbsp;<input type="text" name="progFullname"></td></tr>
<tr>
<td>&nbsp;��Ʒ�����</td><td>&nbsp;<input type="text" name="proGroupcode;"></td></tr>
<tr>
<td>&nbsp;����·��</td><td>&nbsp;<input type="text" name="progPath;"></td></tr>
<tr>
<td><input type="Submit" name="button4" value="�ύ"/></td><td><input type="Reset" name="button5" value="ȡ��"/></td>
</tr>
</table>
</form>
</div>
  </body>
</html>
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->