<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0066)http://china.alibaba.com/member/join.htm?tracelog=main_toolbar_reg -->
<html>
	<head>
		<title>Ʒ��</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="css/style_min.css" />
		<style type="text/css">
.addbrand div {
	padding-bottom: 20px;
}
</style>
	</head>
	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a href="myshopcartproductlist.jsp">�ҵ�����</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">��Ʒ����</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">����Ʒ��</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<form action="" name="addbrand" method="post">
				<div id="main">
					<div id="latest" style="height: 240px;" align="center">
						<div class="addbrand">
							<div style="font: bolder; font-size: 18px; margin-left: 120px;">
								<ul>
									<LI>
										����Ʒ��
									</LI>
								</ul>
							</div>
							<div>
								<span style="font: bolder">Ʒ������</span>
								<span class="red">: </span>
								<INPUT maxLength=100 size=32 name="brand_name" class="required">
							</div>
							<div>
								<span style="font: bolder">Ʒ������</span>
								<span class="red">: </span>
								<input maxLength=100 size=32 name="brand_desc" class="required">
							</div>
							<div>
								<input type="submit" value="����" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="reset" value="ȡ��" />
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</body>
</html>
<%@ include file="footer.jsp"%>