<%@ page pageEncoding="GBK" isErrorPage="true" %>
<!-- ��Ϣ��ʾҳ��,��Ҫ��������: title �� message -->
<!-- ͷ��ҳ�濪ʼ -->
<%@ include file="head.jsp" %>
<!-- ͷ��ҳ����� -->

<%-- ������ǩ�� --%>
<%@ include file="inc_taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<!-- JS, CSS, BASE ��ǵ� -->
<%@ include file="inc_resources.jsp" %>

<TITLE>${appConfig.appTitle} - ������ ${title}</TITLE>
</HEAD>

<BODY leftMargin=0 topMargin=0 marginwidth="0" marginheight="0">

<!-- ҳ������ -->

<TABLE border="1"  cellpadding="0" style="border-collapse: collapse; " bordercolor="#000000" width=760 align=center >
  <TBODY>
    <TR>
      <TD align="center">
	    <p><span class="STYLE6">${title} </span></p>
	    <p><img src="images/error.png" alt="����ͼƬ" width="38" height="38"><br>
	          <font color=green size=5>${message} </font><br>
	          <%
	          if(exception != null) {
	        	  %>
	        	  ϵͳ����! ����ϵ<a href="mailto:wangyong31893189@163.com">��վ����Ա</a>!  ��ϸ������Ϣ����:<br>
	        	  <%=exception%>
	        	  <%
	          }
	          %>
          <a href="javascript:void(0);" onclick="window.history.back();">����</a> </p></TD>
      
    </TR>
 
  </TBODY>
</TABLE>

</BODY>
</HTML>
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->