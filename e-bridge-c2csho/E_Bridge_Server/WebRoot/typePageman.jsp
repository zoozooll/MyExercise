<%-- ���������ҳ��ѯ --%>

<%@page pageEncoding="GBK" errorPage="/pages/error/jspErrorPage.jsp"%>
<%@page contentType="text/html;charset=GBK" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<table width=100% border="0">
	<tr>
		<td colspan="5"><div align="center"><strong>
              [��${pageBean.allRow}����¼] 
                 ��${pageBean.currentPage}ҳ/��${pageBean.totalPage}ҳ 
               [
               <c:choose>
               	<c:when test="${pageBean.hasPreviousPage}">
               		<a href="findProductByType.action?page=${pageBean.currentPage-1}">��һҳ</a>
               	</c:when>
               	<c:otherwise>
               		��һҳ
               	</c:otherwise>
               </c:choose>
              /
              <c:choose>
              	<c:when test="${pageBean.hasNextPage}">
              	<a href ="findProductByType.action?page=${pageBean.currentPage+1}">��һҳ</a>
              	</c:when>
              	<c:otherwise>
              		��һҳ  <input type="hidden" id="protypeId" value="${protypeId}">
              	</c:otherwise>
              </c:choose>              
               ]
             <span >ת��</span> 
              <select name="select" id="myoption" onchange="findProductByType()">
              	<option>����ѡ��</option>
               <c:forEach var="i" begin="1" end="${pageBean.totalPage}">
                <option value="${i}">${i}</option>
               </c:forEach>
              </select>
               </strong>
             </div>
         </td>
    </tr>
   </table>

<script type="text/javascript">
function findProductByType(){
	var selectval = window.document.getElementById("myoption").value;
	var id=window.document.getElementById("protypeId").value;	
	var url = "findProductByType.action?page="+selectval+"&protypeId="+id;	
	window.navigate(url)
}
</script>