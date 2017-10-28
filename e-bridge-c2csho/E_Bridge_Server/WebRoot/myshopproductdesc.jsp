<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
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
<!--
#Layer1 {
	position: absolute;
	left: -3px;
	top: 324px;
	width: 195px;
	height: 138px;
	z-index: 1;
	background-color: #F3D88F;
}

#Layer2 {
	position: absolute;
	left: 2px;
	top: 327px;
	width: 204px;
	height: 300px;
	z-index: 1;
}

.STYLE4 {
	font-size: 36px;
	font-family: "����";
}

.productdesc {
	float: left;
}

.productdesc img {
	border: 1px solid orange;
	
}

.productdesctext {
	margin: 13px;	
	font: serif normal 18px;
	width:auto;
	font-size: 14px;
	text-align: left;
}
-->
</style>
	</head>
	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a
				href="myshopcartproductlist.jsp">�ҵ�����</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">��Ʒ����</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">��Ʒ��ϸ��Ϣ</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<div id="main">
				<div id="latest" style="height: 240px;" align="center">

					<div align="center" class="text STYLE4">
						��Ʒ��ϸ��Ϣ
					</div>
					<div class="productdesc" style="margin-top: 25px;padding-left: 20px;">
						<div style="float: left;font-size: 18px">
							ͼƬԤ��
							<div>
								<div>
									<img src="${product.proImagepath}"
										style="border: 1px solid red;" />
								</div>
								<div>
									<c:forEach var="image" items="${product.images}">
										<img src="${image.imgPath}" />${image.imgName}
										${image.imgMemo}
								</c:forEach>
								</div>
							</div>
						</div>
						<div class="productdesc"
							style="width: 400px;padding-left: 40px;">
							<div class="productdesctext">
								��ƷID: ${product.proId}
							</div>
							<div class="productdesctext">
								��Ʒ���� : ${product.proName}
							</div>
							<div class="productdesctext">
								��ƷƷ�� : ${product.brand.brandName}
							</div>
							<div class="productdesctext">
								��Ʒ��� : ${product.productType.typeName}
							</div>
							<div class="productdesctext">
								��Ʒ�� : ${product.productGroup.progGroupname}
							</div>
							<div class="productdesctext">
								��Ʒ���� : ${product.proPrice}
							</div>
							<div class="productdesctext">
								��Ʒ��λ : ${product.proUnit}
							</div>
							<div class="productdesctext">
								��Ʒ�����ص� : ${product.proFeature}
							</div>
							<div class="productdesctext">
								��Ʒ��� : ${product.specs.specName}
							</div>
							<div class="productdesctext">
								��Ʒ���ܲ��� : ${product.specs.specParam}
							</div>
							<div class="productdesctext">
								��Ʒ���� : ${product.stock.stoAmount}
							</div>
						</div>						
					</div>
					<div align="center" style="margin-top: 60px;padding-top: 60px;top:500px;">
					<p></p>
					<p></p>
							<a
								href="addProductToCartAction.action?productId=${product.proId}">
								<img src="images/common/cartbuy2.gif"
									style="height: 32px; width: 132px;" /> </a>
						</div>
				</div>
			</div>
		</div>
	</body>
</html>
<%@ include file="footer.jsp"%>

