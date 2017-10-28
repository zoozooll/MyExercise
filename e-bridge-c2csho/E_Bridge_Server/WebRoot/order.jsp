<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>�޽�����</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />	
		<!-- ����֤ -->
		<script src="js/prototype.js" type="text/javascript"></script>
		<script src="js/validation_cn.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="js/style_min.css" />	
		
		<script type="text/javascript">
				function show(obj){		
		    	var elem = document.getElementById(obj);		    	
		    	if (elem.style.display=='') {elem.style.display='none'; return;}
		    	elem.style.display='';
		    }			
			</script>
		<style type="text/css">

.red{
	color:red;
	font-weight: bolder;
}
.bor {
	margin: 10px 2px 3px 12px;
	padding-left: 10px;
	text-align: center;
}
</style>

	</head>
	<body>

		<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a href="Unaudited_order.jsp">��������</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">�ύ����</a>&gt;&gt;</span>
			<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>			
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
				<div id="latest" style="height: 240px;">
					<form action="createOrderAction.action" method="post" class='required-validate'>
						
						<div>
							<div>
								������Ϣ
							</div>
							<div>
								<hr width="100%"/>
							</div>
							<div class="bor">
								<span>&nbsp;&nbsp;�������</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span> <input name="ordercode" value="${ordercode }"
										class="required" readonly="readonly"/> </span>
							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;��������</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><input name="ordername" class="required min-length-4 max-length-20" /> </span><span class="red">*</span>
							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;������Դ</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><input name="orderSource"
										value="${purchaser.purName }" class="required"
										readonly="readonly" /> </span>

							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;���ʽ</span>
								<span class="red">: </span>&nbsp;&nbsp;&nbsp;
								<span> <select name="pay" class="required">
										<c:forEach var="p" items="${payway}">
											<option value="${p.payName}">
												${p.payName}
											</option>
										</c:forEach>
									</select>
									</span>
							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;��������</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;
								<span><jsp:include page="calendar.jsp"></jsp:include> </span>
							</div>
							<div class="bor">
								<span>&nbsp;&nbsp;��Ʊ����</span>
								<span class="red">: </span> &nbsp;
								<span> <select name="invoicetype" class="required">
										<option selected="selected">
											��ֵ˰ר�÷�Ʊ
										</option>
										<option>
											��������ͳһ��Ʊ
										</option>
										<option>
											��������ҵר�÷�Ʊ
										</option>
									</select> </span>

							</div>


							<div class="bor">
								<span>&nbsp;&nbsp;��Ʊͷ</span>
								<span class="red">: </span>&nbsp;&nbsp;&nbsp;&nbsp;
								<span><input name="invoicehead"
										value="${purchaser.purName}" class="required" readonly="readonly"/> </span>

							</div>


							<div class="bor">
								<span>&nbsp;&nbsp;�ʹ﷽</span>
								<span class="red">: </span>&nbsp;&nbsp;&nbsp;&nbsp;
								<span><input name="sendto" class="required" /> </span><span class="red">*</span>
							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;Ҫ�󵽻�����</span>
								<span class="red">: </span> &nbsp;
								<span><jsp:include page="calendar2.jsp"></jsp:include> </span>

							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;�ͻ��������</span>
								<span class="red">: </span> &nbsp;
								<span><input name="doselfcode" class="required" /> </span><span class="red">*</span>

							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;������ע</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><textarea name="ordermemo"
										style="height: 50px; width: 220px;" class="required"></textarea>
								</span>

							</div>

							<div class="bor">
								<span>&nbsp;&nbsp;����֪ͨ</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span> <input name="seensms" type="radio" value="yes"
										checked="checked" />��Ҫ &nbsp;&nbsp; <input name="seensms"
										type="radio" value="no" />����Ҫ </span>

							</div>

							<div class="bor">
								<span style="font: bolder">&nbsp;&nbsp;�ֻ�����1</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;
								<span><input name="mobile1" class="required validate-phone" /> </span><span class="red">*</span>

							</div>

							<div class="bor">
								<span style="font: bolder">&nbsp;&nbsp;�ֻ�����2</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;
								<span><input name="mobile2" class="required" /> </span>
							</div>

							<hr />
							<a href="javascript:void(0)" onclick="show('showdesc')";
>
								&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;��ʾ������ϸ��Ϣ</a>
							<div id="showdesc" style="display: none; padding-top: 20px;">
								<div
									style="float: inherit; font-size: 16px; font-weight: bold; margin: 10px;">
									������ϸ��Ϣ
								</div>
								<table class="datalist" style="width: 100%;">
									<tr>
										<th>
											��Ʒ����
										</th>
										<th>
											��Ʒ����
										</th>
										<th>
											���ҹ�˾����
										</th>
										<th>
											��Ʒ���
										</th>
										<th>
											����
										</th>
										<th>
											�ܽ��
										</th>
									</tr>
									<c:set var="flag" value="true" />
									<c:forEach var="map" items="${sessionScope.cart.cart}">
										<c:choose>
											<c:when test="${flag==true}">
												<tr>
													<td>
														${map.value.product.proName}
													</td>
													<td>
														${map.value.productSum}
													</td>
													<td>
														${map.value.product.productGroup.vender.venShortname}
													</td>
													<td>
														${map.value.product.proId}
													</td>
													<td>
														${map.value.product.stock.storeHouse.storeAddress}
													</td>
													<td>
														${map.value.sumMoney}
													</td>
												</tr>
												<c:set var="flag" value="false" />
											</c:when>
											<c:otherwise>
												<tr class="altrow">
													<td>
														${map.value.product.proName}
													</td>
													<td>
														${map.value.productSum}
													</td>
													<td>
														${map.value.product.productGroup.vender.venShortname}
													</td>
													<td>
														${map.value.product.proId}
													</td>
													<td>
														${map.value.product.stock.storeHouse.storeAddress}
													</td>
													<td>
														${map.value.sumMoney}
													</td>
												</tr>
												<c:set var="flag" value="true" />
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</table>
							</div>
							<div style="text-align: center; margin-top: 10px;">
								<input class="required"
									style="font-size: 14px; height: 30px; width: 100px;"
									type="submit" value="ȷ���ύ" />
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</body>
</html>
<%@ include file="footer.jsp"%>
