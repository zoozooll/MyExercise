<%-- 
	ajax���Զ���ȫʵ����
--%>
 <!-- �봫ͳ����ͼ�㲻ͬ�����jsp���ص���xml�����ݣ����contentType��ֵ��text/xml-->
<%@ page  contentType="text/xml; charset=utf-8" language="java" pageEncoding="gbk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- ����xml���ݵ���ͼ����ʱ�����κ��߼��жϣ��Ƚ����еĵ��ʶ����أ���ǰ̨��̨Ӧ�ÿ���������Э��֮�������Ʒ��ص����� -->

<words>
	<c:forEach var="w" items="${searchs}">
		<word>${w}</word>		
	</c:forEach>	
	<% 
		
			System.out.println(request.getAttribute("searchs"));
		%>	
</words>
