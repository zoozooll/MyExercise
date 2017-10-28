<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>

<%-- 价格保护页面 --%>
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
.addproductpage table input {
	border: 1px solid black;
	width: 200px;
	margin: 10px 5px;
}
</style>
	</head>

	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
				href="index.jsp">首页</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">产品中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">删除产品</a>&gt;&gt;</span>
		<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>		
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
<div id="latest" style="height: 240px; top: 67px; left: 152px;">
				<div class="right">
					<!--[if !ie]>内容 开始<![endif]-->
					<h3 style="background-color: #AACDED;">
						价格保护：
					</h3>
					<div class="help_box">
						<p>
							京东商城的商品价格随市场价格的波动每日都会有涨价、降价或者优惠等变化。如果下完订单后价格发生了变化，实行如下价保原则：
						</p>
						<ul class="List_Number">
							<li>
								先款后货订单，订单中商品出现降价行为：
								<br />
								则商品在未出库时按照新价格结算，多出的钱款以积分的形式（1元=10个积分）在订单完成后返还到您的京东账户中。
								<ul class="List_Letter">
									<li>
										如货款支付完毕但货物未出库，您可进入我的京东-
										<a href="http://jd2008.360buy.com/User_jiabao.aspx"
											target="_blank">价格保护</a>页面自动申请，申请成功后多付货款将以积分形式返还到您的京东账户中；
									</li>
									<li>
										一旦商品出库，商品将不再享受价保，如取消订单则需收取相应运费及手续费。
									</li>
								</ul>
							</li>
							<li>
								货到付款订单，订单中商品出现降价行为：
								<ul class="List_Letter">
									<li>
										在货物出库之前您可进入我的京东-
										<a href="http://jd2008.360buy.com/User_jiabao.aspx"
											target="_blank">价格保护</a>页面自动申请；
									</li>
									<li>
										如商品已出库需您在收到货物的当场致电客服确认价保事宜，以客户订单价格和收到商品时商城价格，两者中最低价格结算（配送人员离开后将不享受价保）。
									</li>
								</ul>
							</li>
							<li>
								上门自提订单，订单中商品出现降价行为：
								<ul class="List_Letter">
									<li>
										在货物出库之前您可进入我的京东-
										<a href="http://jd2008.360buy.com/User_jiabao.aspx"
											target="_blank">价格保护</a>页面自动申请；
									</li>
									<li>
										如商品已出库请您在提货时向自提工作人员提出价保申请，当场为您核实处理，否则货物提走后将不再享受价保。
									</li>
								</ul>
							</li>
							<li>
								如您提交订单后商品出现涨价行为（包括优惠期结束），商品价格还是按照您下单时的价格结算，无论您是否汇款 ，不需要与客服确认。
							</li>
							<li>
								特价商品、分期付款商品、高校代理订单不享受价格保护。
							</li>
						</ul>
					</div>
				</div>
				</div>
			</div>
		</div>


	</body>
</html>
<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->