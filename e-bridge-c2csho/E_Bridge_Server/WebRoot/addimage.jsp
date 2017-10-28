<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0066)http://china.alibaba.com/member/join.htm?tracelog=main_toolbar_reg -->
<html>
	<head>
		<title>图片</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="css/style_min.css" />
		<style type="text/css">
td {
	padding-bottom: 20px;
}
</style>
	</head>
	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
				href="index.jsp">首页</a>&gt;&gt;<a href="myshopcartproductlist.jsp">我的商铺</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">产品中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">上传产品图片</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<div id="main">
				<div id="latest" style="height: 240px;" align="center">
					<div
						style="font: bolder; font-size: x-large; margin-left: 150px; margin-bottom: 20px;">
						<ul>
							<LI style="font-size: 18px;">
								上传图片
							</LI>
						</ul>
					</DIV>

					<table>
						<tr>
							<td style="font: bolder">
								图片名称
								<span class="red">: </span>
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="img_name" class="required">
							</td>
						</tr>
						<tr>
							<td style="font: bolder">
								上传的图片
								<span class="red">: </span>
							</td>
							<td>
								<INPUT type="file" maxLength=100 size=32 name="img_path"
									class="required">
							</td>
						</tr>
						<tr>
							<td style="font: bolder">
								图片备注
								<span class="red">: </span>
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="img_memo" class="required">
							</td>

						</tr>
					</table>
					<div style="margin-top: 20px">
						<input type="submit" value="上传" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="reset" value="取消" />
					</div>
				</div>

			</div>
		</div>
	</body>
</html>
<%@ include file="footer.jsp"%>
