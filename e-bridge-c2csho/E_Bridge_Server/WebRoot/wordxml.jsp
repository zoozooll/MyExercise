<%-- 
	ajax的自动补全实例，
--%>
 <!-- 与传统的视图层不同，这个jsp返回的是xml的数据，因此contentType的值是text/xml-->
<%@ page  contentType="text/xml; charset=utf-8" language="java" pageEncoding="gbk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 返回xml数据的视图层暂时不做任何逻辑判断，先将所有的单词都返回，待前台后台应用可以完整的协作之后，再限制返回的内容 -->

<words>
	<c:forEach var="w" items="${searchs}">
		<word>${w}</word>		
	</c:forEach>	
	<% 
		
			System.out.println(request.getAttribute("searchs"));
		%>	
</words>
