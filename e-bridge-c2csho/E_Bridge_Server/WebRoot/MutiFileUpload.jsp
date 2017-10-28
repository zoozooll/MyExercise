<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="head.jsp"%>
<%-- 添加产品页面 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>巨匠电子</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<style type="text/css">
.upload {
	border: 1px solid blue;
}
</style>
	</head>
	<body>
		<div id="container" style="height: auto">

			<div>
				<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
					href="index.jsp">首页</a>&gt;&gt;<a
					href="findAllPurchasersAction.action">产品中心</a>&gt;&gt;<a
					style="color: red; font-weight: bold;">上传图片</a></span>
				<jsp:include page="mycompanyleft.jsp"></jsp:include>
				<div id="main"
					style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
					<div id="latest" style="height: 240px; top: 67px; left: 152px;padding: 60px;">
						<center>
							<s:fielderror />
							<s:form action="doMultipleUploadUsingList.action" method="POST"
								enctype="multipart/form-data">
								<s:file label="文件1" name="upload" cssClass="upload" />
								<s:file label="文件2" name="upload" cssClass="upload" />
								<s:file label="文件3" name="upload" cssClass="upload" />
								<s:submit cssClass="upload" value="上传" />
							</s:form>
						</center>
					</div>
				</div>
			</div>

		</div>

	</body>
</html>
<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->