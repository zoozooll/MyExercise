<%-- ��Ʒ������ҳ��ѯ --%>

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
               		<a href="findProductByBrand.action?page=${pageBean.currentPage-1}">��һҳ</a>
               	</c:when>
               	<c:otherwise>
               		��һҳ
               	</c:otherwise>
               </c:choose>
              /
              <c:choose>
              	<c:when test="${pageBean.hasNextPage}">
              	<a href ="findProductByBrand.action?page=${pageBean.currentPage+1}">��һҳ</a>
              	</c:when>
              	<c:otherwise>
              		��һҳ  <input type="hidden" id="brandId" value="${brandId}">
              	</c:otherwise>
              </c:choose>              
               ]
             <span >ת��</span> 
              <select name="select" id="myoption" onchange="findProductByBrand()">
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
function findProductByBrand(){
	var selectval = window.document.getElementById("myoption").value;
	var id=window.document.getElementById("brandId").value;	
	var url = "findProductByBrand.action?page="+selectval+"&brandId="+id;	
	window.navigate(url)
}
</script>