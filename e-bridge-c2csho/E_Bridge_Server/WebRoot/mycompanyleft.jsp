<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>�޽�����</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />		
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
	
</head>
	<body>				
			<div id="left">				
				<div id="category">
				<c:choose>
				<c:when test="">
					<span style="padding-left: 5px;"><font style="font-size: 14px;color: white;"><b>��������</b></font></span>
				</c:when>
				<c:otherwise>
					<span style="padding-left: 5px;"><font style="font-size: 14px;color: white;"><b>��������</b></font></span>
				</c:otherwise>
				</c:choose>	<div id="catborder">
							<h4><font color="white" size="3px"><b>��Ʒ����</b></font><br/></h4>				
										<ul>
											<li><a href="productadd.jsp">���Ӳ�Ʒ</a></li>
											<li><a href="addimage.jsp">���Ӳ�ƷͼƬ</a></li>
											<li><a href="addbrand.jsp">���Ӳ�ƷƷ��</a></li>
											<li><a href="myshopproductlist.jsp">�鿴��Ʒ</a></li>											
											<li><a href="productdelete.jsp">ɾ����Ʒ</a></li>											
										</ul>
							<h4><font color="white" size="3px"><b>��������</b></font><br/></h4>				
										<ul>
											<li><a href="findAllOrderAction.action">�鿴����</a></li>											
											<li><a href="deleteorder.jsp">ɾ������</a></li>											
										</ul>							
							<h4><font color="white" size="3px"><b>��������</b></font><br/></h4>				
										<ul>
										<li><a href="findAllDelivery.action">���н�����</a></li>
											<li><a href="">�ȴ������Ķ���</a></li>
											<li><a href="">�����еĶ���</a></li>
											<li><a href="">�ѷ����Ķ���</a></li>	
											<li><a href="shophelp.jsp">�˷Ѳ�ѯ</a></li>										
										</ul>
							<h4><font color="white" size="3px"><b>��������</b></font><br/></h4>				
										<ul>
											<li><a href="userdata.jsp">��������</a></li>
											<li><a href="findAllPurchasersAction.action">�鿴�����û�</a></li>
											<li><a href="usermodifypassword.jsp">�޸�����</a></li>	
											<li><a href="userupdate.jsp">�޸�����</a></li>											
										</ul>
								<h4><font color="white" size="3px"><b>��������</b></font><br/></h4>				
										<ul>
											<li><a href="">������ѯ</a></li>
											<li><a href="">�ҵ�Ͷ��</a></li>	
											<li><a href="">�ҵķ���</a></li>		
											<li><a href="helpprice.jsp">�۸񱣻�</a></li>	
											<li><a href="">�ҵľٱ�</a></li>										
								</ul>
								<h4><font color="white" size="3px"><b>��������</b></font><br/></h4>				
										<ul>
											<li><a href="">�ҵ�����</a></li>
											<li><a href="">��Ҫ����</a></li>																				
								</ul>
							<h4><font color="white" size="3px"><b>��������</b></font><br/></h4>				
										<ul>
											<li><a href="findAllRecivery.action">�鿴�տ�б�</a></li>																														
										</ul>
					</div>
				</div>				
			</div>				
	</body>
</html>
