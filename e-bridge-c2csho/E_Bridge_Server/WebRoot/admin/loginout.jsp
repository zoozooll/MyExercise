<%@ page pageEncoding="gbk" isErrorPage="true" language="java" %>
<jsp:directive.page import="java.util.Enumeration"/>
<!-- 头部页面开始 -->

<!-- 头部页面结束 -->
<%
//session.invalidate();// 导致后面的包含页面工作不正常

// 清空session中的所有属性
Enumeration keys = session.getAttributeNames();
while(keys.hasMoreElements()) {
	session.removeAttribute((String)keys.nextElement());
}
%>

<%-- 包含标签库 --%>
<%@ include file="../inc_taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<!-- JS, CSS, BASE 标记等 -->
<%@ include file="../inc_resources.jsp" %>

<TITLE>${appConfig.appTitle} - 注销</TITLE>

</HEAD>



<BODY leftMargin=0 topMargin=0 marginwidth="0" marginheight="0">


<!-- 页面主体 -->

<TABLE border="1"  cellpadding="0" style="border-collapse: collapse; " bordercolor="#000000" width=760 align=center >
  <TBODY>
    <TR>
      <TD align="center">
      <p class="h1">您已经注销成功.</p><br/><font color="blue" size="14px">
      <a href="/E_Bridge/welcome.jsp" style="font-size:15pt;clear:both;">首页</a><br/> <a onclick="window.location='login.jsp'" style="font-size:15pt;clear:both;">重新登录</a>
       </font>
	   </TD>      
    </TR> 
  </TBODY>
</TABLE>
   

  </body>
</html>
 

<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->
