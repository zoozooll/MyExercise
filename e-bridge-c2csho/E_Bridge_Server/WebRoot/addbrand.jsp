<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0066)http://china.alibaba.com/member/join.htm?tracelog=main_toolbar_reg -->
<html>
	<head>
		<title>品牌</title>
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
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
				href="index.jsp">首页</a>&gt;&gt;<a href="myshopcartproductlist.jsp">我的商铺</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">产品中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">增加品牌</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<form action="" name="addbrand" method="post">
				<div id="main">
					<div id="latest" style="height: 240px;" align="center">
						<div class="addbrand">
							<div style="font: bolder; font-size: 18px; margin-left: 120px;">
								<ul>
									<LI>
										增加品牌
									</LI>
								</ul>
							</div>
							<div>
								<span style="font: bolder">品牌名称</span>
								<span class="red">: </span>
								<INPUT maxLength=100 size=32 name="brand_name" class="required">
							</div>
							<div>
								<span style="font: bolder">品牌描述</span>
								<span class="red">: </span>
								<input maxLength=100 size=32 name="brand_desc" class="required">
							</div>
							<div>
								<input type="submit" value="增加" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="reset" value="取消" />
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</body>
</html>
<%@ include file="footer.jsp"%>