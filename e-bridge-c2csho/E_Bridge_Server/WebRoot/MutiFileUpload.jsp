<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="head.jsp"%>
<%-- ��Ӳ�Ʒҳ�� --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>�޽�����</title>
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
				<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
					href="index.jsp">��ҳ</a>&gt;&gt;<a
					href="findAllPurchasersAction.action">��Ʒ����</a>&gt;&gt;<a
					style="color: red; font-weight: bold;">�ϴ�ͼƬ</a></span>
				<jsp:include page="mycompanyleft.jsp"></jsp:include>
				<div id="main"
					style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
					<div id="latest" style="height: 240px; top: 67px; left: 152px;padding: 60px;">
						<center>
							<s:fielderror />
							<s:form action="doMultipleUploadUsingList.action" method="POST"
								enctype="multipart/form-data">
								<s:file label="�ļ�1" name="upload" cssClass="upload" />
								<s:file label="�ļ�2" name="upload" cssClass="upload" />
								<s:file label="�ļ�3" name="upload" cssClass="upload" />
								<s:submit cssClass="upload" value="�ϴ�" />
							</s:form>
						</center>
					</div>
				</div>
			</div>

		</div>

	</body>
</html>
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->