<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>�����б�</title>
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
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a href="Unaudited_order.jsp">��������</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">��������Ϣ</a>&gt;&gt;</span>
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
								<span>&nbsp;&nbsp;�������</span>
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
								<span>&nbsp;&nbsp;���������</span>
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
								<span>&nbsp;&nbsp;������Ʒ��</span>
								<span class="red">: </span> &nbsp;&nbsp; &nbsp;&nbsp;<span> <SELECT name="productgroup" class="required">
										<OPTION selected class="required">
											**��
										</OPTION>
									</SELECT> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;������</span>
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
								<span>&nbsp;&nbsp;�ͻ�����</span>
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
								<span>&nbsp;&nbsp;�ͻ����</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;<span> <INPUT maxLength=100 size=32 name="amount" value="${clientcode}"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;��Ʊ���</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>&nbsp; <INPUT maxLength=100 size=32 name="amount" value="${invoiceno}"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;��˾���ڵ�</span>
								<span class="red">: </span> &nbsp;&nbsp; &nbsp;&nbsp; 
								<span> <SELECT name="province">
										<OPTION selected>
											ʡ��
										</OPTION>
									</SELECT> &nbsp;&nbsp;&nbsp; <SELECT name="city">
										<OPTION selected>
											����
										</OPTION>
									</SELECT> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;��˾��ַ</span>
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
								<span>&nbsp;&nbsp;��ϵ��</span>
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
								<span>&nbsp;&nbsp;���ͷ�ʽ</span>
								<span class="red">: &nbsp; &nbsp;&nbsp;</span> &nbsp;&nbsp; &nbsp;  
								<span> <SELECT name="sendtype">
										<OPTION selected>
											���
										</OPTION>
									</SELECT> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;���͵ص�</span>
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
									<span style="vertical-align: top;">&nbsp; �ر�˵��<span
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
								<span>&nbsp;&nbsp;��ǩ��׼</span>
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
								<span>&nbsp;&nbsp;�������ɳ�</span> 
								<span class="red">:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span><INPUT maxLength=100 size=32 name="sendcarnote"
										class="required">
								</span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;ǩ�ձ�ע</span>
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
								<span>&nbsp;&nbsp;�����</span>
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
								<span>&nbsp;&nbsp;��ϵ��ʽ</span>
								<span class="red">: </span> &nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span> <select name="contacttype">
										<option selected>
											�ֻ�
										</option>
										<option>
											�̶��绰
										</option>
										<option>
											��˾�绰
										</option>
									</select> </span>
							</li>
						</ul>
					</div>

					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;�ʹ﷽�绰</span>
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
							value="ȷ���ύ" name=submit class="required">
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