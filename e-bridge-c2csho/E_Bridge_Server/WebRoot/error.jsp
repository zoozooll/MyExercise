<%@ page pageEncoding="GBK" isErrorPage="true" %>
<!-- 消息显示页面,需要两个属性: title 和 message -->
<!-- 头部页面开始 -->
<%@ include file="head.jsp" %>
<!-- 头部页面结束 -->

<%-- 包含标签库 --%>
<%@ include file="inc_taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<!-- JS, CSS, BASE 标记等 -->
<%@ include file="inc_resources.jsp" %>

<TITLE>${appConfig.appTitle} - 出错了 ${title}</TITLE>
</HEAD>

<BODY leftMargin=0 topMargin=0 marginwidth="0" marginheight="0">

<!-- 页面主体 -->

<TABLE border="1"  cellpadding="0" style="border-collapse: collapse; " bordercolor="#000000" width=760 align=center >
  <TBODY>
    <TR>
      <TD align="center">
	    <p><span class="STYLE6">${title} </span></p>
	    <p><img src="images/error.png" alt="出错图片" width="38" height="38"><br>
	          <font color=green size=5>${message} </font><br>
	          <%
	          if(exception != null) {
	        	  %>
	        	  系统错误! 请联系<a href="mailto:wangyong31893189@163.com">网站管理员</a>!  详细技术信息如下:<br>
	        	  <%=exception%>
	        	  <%
	          }
	          %>
          <a href="javascript:void(0);" onclick="window.history.back();">返回</a> </p></TD>
      
    </TR>
 
  </TBODY>
</TABLE>

</BODY>
</HTML>
<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->