<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>订单列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<script type="text/javascript">
	function show(obj) {
		var elem = document.getElementById(obj);
		if (elem.style.display == '') {
			elem.style.display = 'none';
			return;
		}
		elem.style.display = '';
	}
</script>
		<style type="text/css">
.required {
	border: 1px solid black;
	width: 200px;
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
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
				href="index.jsp">首页</a>&gt;&gt;<a href="Unaudited_order.jsp">订单中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">交货单信息</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
				<div id="latest" style="height: 240px;">
		<center>
			<!--head-->
			<DIV id="container">
				<hr width="110%" />
				<div style="padding-left: 200px;">
					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder;">
								<span>&nbsp;&nbsp;订单编号</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
								&nbsp;<span> <INPUT maxLength=100 size=32 name="amount" value="${order_id}"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;交货单编号</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp; 
								&nbsp;<span> <INPUT maxLength=100 size=32 name="amount" value="${delivery_code}"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;所属产品组</span>
								<span class="red">: </span> &nbsp;&nbsp; &nbsp;&nbsp;<span> <SELECT name="productgroup" class="required">
										<OPTION selected class="required">
											**组
										</OPTION>
									</SELECT> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;总数量</span>
								<span class="red">: </span> 
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								&nbsp;&nbsp;<span>&nbsp; <INPUT maxLength=100 size=32 name="amount"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;客户名称</span>
								<span class="red">: </span>  
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <span><INPUT maxLength=100 size=32 name="clientname"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;客户编号</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;<span> <INPUT maxLength=100 size=32 name="amount" value="${clientcode}"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;发票编号</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>&nbsp; <INPUT maxLength=100 size=32 name="amount" value="${invoiceno}"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;公司所在地</span>
								<span class="red">: </span> &nbsp;&nbsp; &nbsp;&nbsp; 
								<span> <SELECT name="province">
										<OPTION selected>
											省份
										</OPTION>
									</SELECT> &nbsp;&nbsp;&nbsp; <SELECT name="city">
										<OPTION selected>
											城市
										</OPTION>
									</SELECT> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;公司地址</span>
								<span class="red">: </span> 
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="address"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;联系人</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="contactor"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;发送方式</span>
								<span class="red">: &nbsp; &nbsp;&nbsp;</span> &nbsp;&nbsp; &nbsp;  
								<span> <SELECT name="sendtype">
										<OPTION selected>
											快递
										</OPTION>
									</SELECT> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;运送地点</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								&nbsp; &nbsp;<span> <INPUT maxLength=100 size=32 name="carryplace"
										class="required">
								</span>
							</li>
						</ul>
					</div>
					<div>
						<span></span>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder; text-align: ">
								<div align="left">
									<span style="vertical-align: top;">&nbsp; 特别说明<span
										class="red">: </span> &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;   <textarea
											name="specialnote" style="height: 25px; width: 200px;"
											class="required"></textarea> </span>
								</div>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;收签标准</span>
								<span class="red">: </span> 
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="signstandard"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;承运商派车</span> 
								<span class="red">:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span><INPUT maxLength=100 size=32 name="sendcarnote"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;签收备注</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								&nbsp; &nbsp; <span><input maxlength=100 size=32 name="signnote"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;提货人</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
								<span><input maxlength=100 size=32 name="pickupman"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;联系方式</span>
								<span class="red">: </span> &nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span> <select name="contacttype">
										<option selected>
											手机
										</option>
										<option>
											固定电话
										</option>
										<option>
											公司电话
										</option>
									</select> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;送达方电话</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp; &nbsp; 
								<span><input maxlength=100 size=32 name="contactphone"
										class="required">
								</span>
							</li>
						</ul>
					</div>
					<div style="width: 950px; height: 50px;">
					</div>

					<div align="center">
						<input style="font-size: 14px; height: 30px" type=submit
							value="确认提交" name=submit class="required">
					</div>
				</div>
			</DIV>
		</center>
		</div>
		</div>
		</div>
	</body>
</html>
<%@ include file="footer.jsp"%>