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
				<A href="" target=_blank>关于我们</A>|
				<A href="" target=_blank>常见问题</A>|
				<A href="" target=_blank>联系我们</A>|
				<A href="" target=_blank>人才招聘</A>|
				<A href="" target=_blank>Today论坛</A>|
				<A href="" target=_blank>商品评价</A>|
				<A href="admin/login.jsp" target=_blank>后台管理</A>
			</DIV>
			<!--[if !ie]>Footer_Nav end<![endif]-->
			<DIV class="Copyright" align="center">
				广州市 公安局天河分局备案编号：1101081463&nbsp;&nbsp;
				<A class=icp
					href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202007080200026">粤ICP证070359号</A>
				<BR>
				Copyright&copy;2009-2010&nbsp;&nbsp;
				<span style="color: red; font-weight: bold;">这里为网站名称</span>&nbsp;&nbsp;版权所有
				<BR>
			</DIV>
			<!--[if !ie]>Copyright end<![endif]-->
			<DIV class="Footer_Link">
				<A
					href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202007080200026"
					target=_blank><IMG alt="经营性网站备案中心 "
						src="images/footer/footer1.bmp"> </A><A
					href="https://tns-ssverify.cnnic.cn/verifyseal.dll?dn=www.360buy.com"
					target=_blank><IMG alt="站点卫视 " src="images/footer/footer2.bmp">
				</A>
			</DIV>
		</div>
	</body>
</html>
<%--<%@ include file="qqkefu.jsp"%>--%>
<jsp:include page="qqkefu.jsp"></jsp:include>
