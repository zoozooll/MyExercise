<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>�û������޸�</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="css/global.css" />
	<script type="text/javascript">	
			  function tech()
			 {			 		
			    if(document.getElementById("ap").value!=document.getElementById("arp").value){
			    		document.getElementById("repassword").innerHTML="�ٴ��������벻һ��!";
			    		return false;
			    	}
			    	return true;
			 }
			 
			 function subm(){
			  	return tech(); 
			 }
			 </script>
		<style type="text/css">
			.usermodifypassword input{
				border: 1px solid black;
				width: 200px;
				margin:10px 5px; 
				text-align: center;
			}
		</style>	 
		
  </head>
  <body>
  <div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a href="index.jsp">��ҳ</a>&gt;&gt;<a href="findAllPurchasersAction.action">��������</a>&gt;&gt;<a style="color:red;font-weight: bold;">�û��б�</a>&gt;&gt;</span>			
				<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>
			<div id="main" style="border:1px solid red;border-top: none;padding-bottom: 50px;">
				<div id="latest" style="height: 240px; top: 67px; left: 152px;">
				<div class="usermodifypassword">
				<form action="userModifyPasswordAction.action" method="post" onsubmit="return subm();">
				<div style="font: bolder; font-size: 25px;" align="center">�޸�����</div><br>
				<div align="center">
							<span>&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; ������</span>
							<span class="red">: </span> 
							<span><input type="password" name="purBeforePassword"> </span><span>${message}</span>
				</div>

				<div align="center">&nbsp; <span>������</span> 
							<span class="red">: </span>
							<span><input type="password" id="ap" name="purPassword" ></span><span id="repassword"></span>
				</div>

				<div align="center">&nbsp;&nbsp; 
							<span>ȷ������</span><span class="red">: </span>
							<span><input type="password" id="arp" name="purRePassword" onblur="tech();"></span>
					
				</div>
				<DIV style="margin-top: 20px;margin-left: -100px">
						<center><img src="images/common/modify.jpg" alt="�޸�����"/></center>
					</DIV>
				</form>
				</div>
			</div>
			</div>
		</div>
	</body>
</html>
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->