<%-- 按类别来分页查询 --%>

<%@page pageEncoding="GBK" errorPage="/pages/error/jspErrorPage.jsp"%>
<%@page contentType="text/html;charset=GBK" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<table width=100% border="0">
	<tr>
		<td colspan="5"><div align="center"><strong>
              [共${pageBean.allRow}条记录] 
                 第${pageBean.currentPage}页/共${pageBean.totalPage}页 
               [
               <c:choose>
               	<c:when test="${pageBean.hasPreviousPage}">
               		<a href="findProductByType.action?page=${pageBean.currentPage-1}">上一页</a>
               	</c:when>
               	<c:otherwise>
               		上一页
               	</c:otherwise>
               </c:choose>
              /
              <c:choose>
              	<c:when test="${pageBean.hasNextPage}">
              	<a href ="findProductByType.action?page=${pageBean.currentPage+1}">下一页</a>
              	</c:when>
              	<c:otherwise>
              		下一页  <input type="hidden" id="protypeId" value="${protypeId}">
              	</c:otherwise>
              </c:choose>              
               ]
             <span >转到</span> 
              <select name="select" id="myoption" onchange="findProductByType()">
              	<option>－请选择－</option>
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