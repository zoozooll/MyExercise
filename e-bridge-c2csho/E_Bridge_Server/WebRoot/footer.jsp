<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
		<style type="text/css">

.Footer_Nav {
	font: normal 13px black;
	margin: 5px 0px;
}

.Copyright {
	font: normal 15px black;
	margin: 5px 0px;
}

.Footer_Link {
	text-align: center;
	margin: 5px 0px;
	padding: 10px;
}

.Footer_Link img {
	border: 0px;
}
</style>
	</head>

	<body>
		<div>
			<jsp:include page="imagescroll.htm"></jsp:include>
			<div class="Footer_Nav" align="center">
				<A href="" target=_blank>��������</A>|
				<A href="" target=_blank>��������</A>|
				<A href="" target=_blank>��ϵ����</A>|
				<A href="" target=_blank>�˲���Ƹ</A>|
				<A href="" target=_blank>Today��̳</A>|
				<A href="" target=_blank>��Ʒ����</A>|
				<A href="admin/login.jsp" target=_blank>��̨����</A>
			</DIV>
			<!--[if !ie]>Footer_Nav end<![endif]-->
			<DIV class="Copyright" align="center">
				������ ��������ӷ־ֱ�����ţ�1101081463&nbsp;&nbsp;
				<A class=icp
					href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202007080200026">��ICP֤070359��</A>
				<BR>
				Copyright&copy;2009-2010&nbsp;&nbsp;
				<span style="color: red; font-weight: bold;">����Ϊ��վ����</span>&nbsp;&nbsp;��Ȩ����
				<BR>
			</DIV>
			<!--[if !ie]>Copyright end<![endif]-->
			<DIV class="Footer_Link">
				<A
					href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202007080200026"
					target=_blank><IMG alt="��Ӫ����վ�������� "
						src="images/footer/footer1.bmp"> </A><A
					href="https://tns-ssverify.cnnic.cn/verifyseal.dll?dn=www.360buy.com"
					target=_blank><IMG alt="վ������ " src="images/footer/footer2.bmp">
				</A>
			</DIV>
		</div>
	</body>
</html>
<%--<%@ include file="qqkefu.jsp"%>--%>
<jsp:include page="qqkefu.jsp"></jsp:include>
