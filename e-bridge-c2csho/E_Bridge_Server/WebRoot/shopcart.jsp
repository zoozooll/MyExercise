<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
	�ҵĹ��ﳵ
</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="css/shopcart/shopcart.css" rel="stylesheet" type="text/css"/>
</head>
  <body>
     <div class="container">
		<div class="top">
		<div class="logo" style="margin:0px;"><a href="index.jsp"><img src="images/common/logo.jpg" alt="logoͼ��" style="width:150px;border: none;"/></a></div>		
		<div class="title">			
					&nbsp;&nbsp;1.�ҵĹ��ﳵ 
					&nbsp;&nbsp;2.��д�˶Զ�����Ϣ
					&nbsp;&nbsp;&nbsp;3.�ɹ��ύ����
			</div>
		</div>
		<span></span>
		<div class="mycart"><img src="images/shopcart/cart_001.gif" style="vertical-align: middle;"/>������400Ԫ�����ֵ������˷ѣ�
		<a href="shophelp.jsp" target="_blank" style="font-size: 13px;color:#8BB4F3;">��ϸ�˽�&gt;&gt;</a>
		</div>
		<!--mycart_tip end -->
	<div>	
	<div class="myselect">
	<h3 style="padding:8px;"><strong>����ѡ����Ʒ</strong></h3></div>
	<div class="listcart" style="border:3px solid #AACDED; width:100%;
	 border:3px solid #AACDED;
	border-top: none;margin-top:-19px;">
	 <!--��Ʒ�б�ʼ-->
	    <div style="padding-top: 20px;">
				    
				<table width="95%" cellspacing="0" cellpadding="0" border="1px" bordercolor="#AACDED" style="margin:20px 25px;background-color: #EBF4FB;font-size: 13px;">
				<tr>
				    <td width="7%" style="height: 30px;">��Ʒ���</td>
				    <td>��Ʒ����</td>
				    <td width="10%">��Ʒ�۸�</td>
				    <td width="8%">��Ʒ����</td>
				    <td width="8%">��Ʒ�ܼ�</td>
				    <td width="9%">ɾ����Ʒ</td>
				    <td width="7%">�޸���Ʒ����</td>
				 </tr>
				    <c:forEach var="map" items="${sessionScope.cart.cart}">
		<tr>   
		<form method="post" action="updateProductSumFromCartAction.action?productId=${map.value.product.proId}" name="f1">
			<input type="hidden" name="productid" value="2">
			<input type="hidden" name="number" value="1">
			<td>${map.value.product.proId}</td>
			<td>&nbsp;&nbsp;<a href="showProduct.action?proId=${map.value.product.proId}">${map.value.product.proName}</a></td>
			<td><br>${map.value.baseprice}</td>			
			<td><input type="text" name="productSum" id=${map.value.product.proId} value="${map.value.productSum}" size="8" /></td>
			<td>&nbsp;${map.value.productSum*map.value.baseprice} </td>
			<td>
			<input type="button" value="ɾ��" onClick="javascript:window.location='delProductFormCartAction.action?productId=${map.value.product.proId}';"></td>
			<td> <input type="submit" value="�����޸�"></td>
    	 </form>
		</tr> 
            </c:forEach> 
				 <tr>
				    <td style="height: 30px;text-align: right;" colspan="7">ԭʼ����${sessionScope.cart.allMoneyOnCart}Ԫ - ���֣���0.00Ԫ<br/>��Ʒ�ܽ��(�����˷�)��<span style="color:red;font-weight: bolder;font-size: 16">��${sessionScope.cart.allMoneyOnCart}</span>Ԫ</td>
				 </tr>				
				</table>
	
	    </div>

    	<div>
		    <!--��Ʒ�б����-->
				<ul style="margin-bottom: 0px; list-style-type: none;font-size:13px;">
					<li style="float: left;"><img src="images/shopcart/gwc_03.jpg" style="vertical-align: middle;"/>&nbsp; <a href="index.jsp">��������</a>&nbsp;</li>
					<li style="float: left;"><img src="images/shopcart/gwc_05.jpg" style="vertical-align: middle;"/>&nbsp;&nbsp;<a href="javascript:void(0);" onclick="this.blur();saveCart();">�Ĵ湺�ﳵ</a> <br></li>
					<li><img src="images/shopcart/gwc_07.jpg" style="vertical-align: middle;"/>&nbsp; <a href="removeCartAction.action">��չ��ﳵ</a>&nbsp;</li>
				</ul>
					<div style="text-align: right;">
						<img alt="������ڶ���" style="border: medium none ; cursor: pointer;" src="images/shopcart/fqbtn.gif"/>
				<a href="beforeCreateOrderAction.action?method=beforeCreateOrder" ><img alt="���붩����Ϣҳ" style="border: medium none ; cursor: pointer;"  src="images/shopcart/gwc_10.jpg"/>
				</a>	</div>					
				
		</div>
	
		<div style="background:url('images/shopcart/shopcartcorner.jpg') no-repeat;margin: 5px 25px;padding: 0px 10px;color:#2D72B6;height: 200px;">
			<!--������Ʒ���û���������...��ʼ-->
			<h3 style="margin: 20"><img src="images/shopcart/arrow_2.gif">ѡ��ֵ��Ʒ��������Ż�</h3>			
			<hr style="margin-top:-25px;"/>
			<div style="margin-top: -30px;">
				<ul style="list-style-type: none;">
					<li style="float:left;font-size: 13px;width: 200px;margin-right: 15px;margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/shoubiao.jpg" style="border:none;"/></div>
					<div><a href="">����ŷ(Casio)10����ϵ���б�AW-80D-7A</a></div>
						<div>���裤169.00Ԫ</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
					
					<li style="float:left;font-size: 13px;width: 200px;margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/yumaoqiu.jpg" /></div>
						<div><a href="">��˫ϲ12֧װ��ë��402</a></div>
						<div>���裤32.00Ԫ</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
					
					<li style="float:left;font-size: 13px;width: 200px; margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/shoubiao.jpg" /></div>
						<div><a href="">����ŷ(Casio)10����ϵ���б�AW-80D-1A</a></div>
						<div>���裤169.00Ԫ</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
					
					<li style="float:left;font-size: 13px;width: 200px;margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/shoudiantong.jpg"/></div>
						<div><a href="">������������ˤ�ֵ�ͲCF06</a></div>
						<div>���裤21.00Ԫ</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
				</ul>
				<!--[if !IE]>������Ʒ���û���������...����<![endif]-->
				</div>	
			</div>		
		 </div>
	   </div>	
	</div>
	</body>
</html>
<jsp:include page="footer.jsp"></jsp:include>