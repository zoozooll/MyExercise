<%@ page pageEncoding="gbk" isErrorPage="true" language="java" %>
<jsp:directive.page import="java.util.Enumeration"/>
<!-- ͷ��ҳ�濪ʼ -->

<!-- ͷ��ҳ����� -->
<%
//session.invalidate();// ���º���İ���ҳ�湤��������

// ���session�е���������
Enumeration keys = session.getAttributeNames();
while(keys.hasMoreElements()) {
	session.removeAttribute((String)keys.nextElement());
}
%>

<%-- ������ǩ�� --%>
<%@ include file="../inc_taglib.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<!-- JS, CSS, BASE ��ǵ� -->
<%@ include file="../inc_resources.jsp" %>

<TITLE>${appConfig.appTitle} - ע��</TITLE>

</HEAD>



<BODY leftMargin=0 topMargin=0 marginwidth="0" marginheight="0">


<!-- ҳ������ -->

<TABLE border="1"  cellpadding="0" style="border-collapse: collapse; " bordercolor="#000000" width=760 align=center >
  <TBODY>
    <TR>
      <TD align="center">
      <p class="h1">���Ѿ�ע���ɹ�.</p><br/><font color="blue" size="14px">
      <a href="/E_Bridge/welcome.jsp" style="font-size:15pt;clear:both;">��ҳ</a><br/> <a onclick="window.location='login.jsp'" style="font-size:15pt;clear:both;">���µ�¼</a>
       </font>
	   </TD>      
    </TR> 
  </TBODY>
</TABLE>
   

  </body>
</html>
 

<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->
