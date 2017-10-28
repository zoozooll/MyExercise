<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0066)http://china.alibaba.com/member/join.htm?tracelog=main_toolbar_reg -->
<html>
	<head>
		<title>购物详细了解</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="css/style_min.css" />
		<style type="text/css">
		.shophelp{
			font-size: 14px;
		}
			.shophelp table{
			text-align: left;border: 1px solid #AACDED;
			width: 100%;
			}
			.shophelp table td{
			text-align: left;border: 1px solid #AACDED;
			}
			
		</style>
</head>
	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
				href="index.jsp">首页</a>&gt;&gt;<a href="myshopcartproductlist.jsp">我的商铺</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">产品中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">增加产品</a>&gt;&gt;</span>
	<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>
			<div id="main">				
		<div class="shophelp">
		<!--[if !ie]>内容 开始<![endif]-->
		<h3 style="background-color: #AACDED;text-align: left;">快递运输：</h3>
			<table>
				<tr>
					<th height="30">区域划分</th>
					<th height="30">包含地区</th>
					<th width="32%">运费标准（元）</th>
				</tr>
				<tr>
					<td width="21%" rowspan="3" align="center">一区</td>
					<td width="47%" height="20">北京</td>
					<td rowspan="3" align="center">5</td>
				</tr>
				<tr>
					<td height="20">上海</td>
				</tr>
				<tr>
					<td height="20">广州</td>
				</tr>
				<tr>
					<td rowspan="17" align="center">二区</td>
					<td height="20">江苏</td>
					<td rowspan="17" align="center">6</td>
				</tr>
				<tr>
					<td height="20">浙江</td>
				</tr>
				<tr>
					<td height="20">安徽</td>
				</tr>
				<tr>
					<td height="20">天津</td>
				</tr>
				<tr>
					<td height="20">山东</td>
				</tr>
				<tr>
					<td height="20">广西</td>
				</tr>
				<tr>
					<td height="20">湖南</td>
				</tr>
				<tr>
					<td height="20">江西</td>
				</tr>
				<tr>
					<td height="20">海南</td>
				</tr>
				<tr>
					<td height="20">河南</td>
				</tr>
				<tr>
					<td height="20">广东（除广州外）</td>
				</tr>
				<tr>
					<td height="20">河北</td>
				</tr>
				<tr>
					<td height="20">福建</td>
				</tr>
				<tr>
					<td height="20">辽宁</td>
				</tr>
				<tr>
					<td height="20">山西</td>
				</tr>
				<tr>
					<td height="20">黑龙江</td>
				</tr>
				<tr>
					<td height="20">吉林</td>
				</tr>
				<tr>
					<td rowspan="12" align="center">三区</td>
					<td height="20">甘肃</td>
					<td rowspan="12" align="center">15</td>
				</tr>
				<tr>
					<td height="20">湖北</td>
				</tr>
				<tr>
					<td height="20">四川</td>
				</tr>
				<tr>
					<td height="20">重庆</td>
				</tr>
				<tr>
					<td height="20">新疆</td>
				</tr>
				<tr>
					<td height="20">陕西</td>
				</tr>
				<tr>
					<td height="20">云南</td>
				</tr>
				<tr>
					<td height="20">内蒙</td>
				</tr>
				<tr>
					<td height="20">宁夏</td>
				</tr>
				<tr>
					<td height="20">西藏</td>
				</tr>
				<tr>
					<td height="20">青海</td>
				</tr>
				<tr>
					<td height="20">贵州</td>
				</tr>
		</table>		
		<h4 class="margin_t20"  id="sfbz">快递运输收取标准一览表</h4>
	<table width="98%" class="tablecss">
				<tr>
					<th height="30" colspan="2">地区</th>
					<th width="24%">钻石（双钻）会员</th>
					<th width="24%">金牌会员</th>
					<th width="26%">其他会员</th>
				</tr>
				<tr>
					<td width="10%" align="center">一区</td>
					<td width="16%" height="70">北京（含郊县）、上海（包括外环以外的郊区，除三岛地区）、广州市</td>
					<td align="left">不计重量，单张订单满50元（含）以上快递运输运费全免</td>
					<td align="left">不计重量，单张订单金额满200元（含）以上快递运输运费全免；单张订单金额不足200元收取快递运输运费5元</td>
					<td align="left">不计重量，单张订单金额满400元（含）以上快递运输运费全免；单张订单金额不足400元收取快递运输运费5元</td>
				</tr>
				<tr>
					<td align="center">二区</td>
					<td height="70">江苏、浙江、安徽、天津、山东、广西、湖南、江西、海南、河南、广东（除广州外）、河北、福建、辽宁、山西、黑龙江、吉林</td>
					<td align="left">不计重量，单张订单满100元（含）以上快递运输运费全免</td>
					<td align="left">不计重量，单张订单满200元（含）以上快递运输运费全免；单张订单金额不足200元收取快递运输运费6元</td>
					<td align="left">不计重量，单张订单金额满400元（含）以上快递运输运费全免；单张订单金额不足400元收取快递运输运费6元</td>
				</tr>
				<tr>
					<td align="center">三区</td>
					<td height="70">甘肃、湖北、四川、重庆、新疆、陕西、云南、内蒙、贵州、宁夏、西藏、青海</td>
					<td align="left">不计重量，单张订单满400（含）以上快递运输运费全免；单张订单金额不足400元收取快递运输运费15元</td>
					<td align="left">不计重量，单张订单金额满800（含）以上快递运输运费全免；单张订单金额不足800元，收取快递运输运费15元</td>
					<td align="left">不计重量，不计金额，每张订单固定收取快递运输运费15元</td>
				</tr>
			</table>
				<ul class="margin_t20 margin_b20">
					<li>提醒注意：</li>
					<li>1.如果您选择的是指定的圆通、申通或者顺丰快递，则没有免运费优惠；快递运输指京东快递或京东选用的价格较低廉的快递公司、邮局快包、中铁快运等；
</li>
					<li>2.北京郊县不提供货到付款的配送方式；</li>
					<li>3.如订单收货地址超出所选快递的配送范围，京东商城有可能直接转运邮局包裹等全国各地可到达的运输方式；</li>
					<li>4.香港、澳门、台湾、钓鱼岛地区的快递费一律按实际运费收取；</li>
					<li>5.不支持跨各物流中心所覆盖的城市发货，如您的收货地址是天津，则不能从上海物流中心或广州物流中心发货；</li>
					<li>6.下订单是系统自动计算运费，发货过程中若实际运费低于系统自动计算的运费，我司会将多出的运费以积分的形式返还到您的京东账户中。（1元人民币=10个积分，遇“分”则四舍五入。例如：实际运费多收取1.58元，则返还积分16分；实际运费多收取了1.32元，则返还积分13分）。</li>
		</ul>
				<table width="98%" class="tablecss">
				<tr>
					<th width="18%" height="30">物流中心</th>
					<th width="82%">覆盖省份</th>
				</tr>
				<tr>
					<td height="30" align="center">北京</td>
					<td align="left">北京、天津、河北、山西、河南、辽宁、吉林、黑龙江、内蒙古、山东、湖北、四川、重庆、贵州、云南、西藏、陕西、甘肃、青海、宁夏、新疆</td>
				</tr>
				<tr>
					<td height="30" align="center">上海</td>
					<td align="left">江苏、浙江、上海、安徽、钓鱼岛</td>
				</tr>
				<tr>
					<td height="30" align="center">广州</td>
					<td align="left">广东、广西、福建、湖南、江西、海南、台湾、香港、澳门</td>
				</tr>
		</table>
		<div class="align_Right margin_t5"><a href="#">返回顶部</a></div>
				<!--[if !ie]>内容 结束<![endif]-->
				<!--[if !ie]>help_tips 开始<![endif]-->
		
	</div>
	</div>
</div>
</body>
</html>

<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->