<%@ page pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="head.jsp" %>
<!-- 消息显示页面,需要两个属性: title 和 message -->

<%-- 包含标签库 --%>
<%@ include file="inc_taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<!-- JS, CSS, BASE 标记等 -->
<%@ include file="inc_resources.jsp" %>

<TITLE>${appConfig.appTitle} - 出错了, 您正在访问的页面不存在!</TITLE>
</HEAD>

<BODY leftMargin=0 topMargin=0 marginwidth="0" marginheight="0">
<!-- 头部页面开始 -->

<!-- 头部页面结束 -->

<!-- 页面主体 -->

<TABLE border="1"  cellpadding="0" style="border-collapse: collapse; " bordercolor="#000000" width=760 align=center >
  <TBODY>
    <TR>
      <TD align="center">
	    <p><span class="STYLE6">${title}</span></p>
	    <p><img src="images/error.gif" alt="出错图片"><br>
	          <font color=red size=4>${message}对不起, 您正在访问的页面不存在, 如有疑问, 请与网站管理员联系!</font><br>

          <a onclick="window.history.back();">返回</a>          </p></TD>
      
    </TR>
 
  </TBODY>
</TABLE>


<!-- 底部页面开始 -->

<!-- 底部页面结束 -->
</BODY>
</HTML>
<%@ include file="footer.jsp" %>