<%@ page pageEncoding="UTF-8" isErrorPage="true" %>
<jsp:directive.page import="java.util.Enumeration"/>


<%-- 包含标签库 --%>
<%@ include file="inc_taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<!-- JS, CSS, BASE 标记等 -->
<%@ include file="inc_resources.jsp" %>

<TITLE>${appConfig.appTitle} - 注销</TITLE>
</HEAD>



<BODY leftMargin=0 topMargin=0 marginwidth="0" marginheight="0">
<!-- 头部页面开始 -->
<%@ include file="head.jsp" %>
<!-- 头部页面结束 -->

<!-- 页面主体 -->

<table border="1"  cellpadding="0" style="border-collapse: collapse; " bordercolor="#000000" width=760 align=center >
  <tbody>
    <tr>
      <td align="center">
      <p class="h1">您已经注销成功.</p><br/><font color="red" size="14px">
      <a href="index.jsp" style="font-size:15pt;clear:both;">首页</a><br/> <a href="login.jsp" style="font-size:15pt;clear:both;">重新登录</a>
       </font>
	   </td>      
    </tr> 
  </tbody>
</table>

  </body>
</html>
<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->