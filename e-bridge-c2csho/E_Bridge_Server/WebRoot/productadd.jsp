<%@ page language="java"  pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    
    <title>My JSP 'product_add.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
	function $(){return document.getElementById?document.getElementById(arguments[0]):eval(arguments[0]);}
var OverH,OverW,ChangeDesc,ChangeH=50,ChangeW=50;
function OpenDiv(_Dw,_Dh,_Desc) {
$("Loading").innerHTML="";
OverH=_Dh;OverW=_Dw;ChangeDesc=_Desc;
$("Loading").style.display='';
if(_Dw>_Dh){ChangeH=Math.ceil((_Dh-10)/((_Dw-10)/50))}else if(_Dw<_Dh){ChangeW=Math.ceil((_Dw-10)/((_Dh-10)/50))}
$("Loading").style.top=(document.documentElement.clientHeight-10)/2+"px";
$("Loading").style.left=(document.documentElement.clientWidth-10)/2+"px";
OpenNow()
}
var Nw=10,Nh=10;
function OpenNow() {
if (Nw>OverW-ChangeW)ChangeW=2;
if (Nh>OverH-ChangeH)ChangeH=2;
Nw=Nw+ChangeW;Nh=Nh+ChangeH;

if(OverW>Nw||OverH>Nh) {
	if(OverW>Nw) {
	$("Loading").style.width=Nw+"px";
	$("Loading").style.left=(document.documentElement.clientWidth-Nw)/2+"px";
	}
	if(OverH>Nh) {
	$("Loading").style.height=Nh+"px";
	$("Loading").style.top=(document.documentElement.clientHeight-Nh)/2+"px"
	}
	window.setTimeout("OpenNow()",10)
	}else{
	Nw=10;Nh=10;ChangeH=50;ChangeW=50;
	$("Loading").innerHTML=ChangeDesc;
	}
}
</script>
		<style type="text/css">
.addproductpage table input,.addproductpage table select,.addproductpage table textarea{
	border: 1px solid black;
	width: 200px;
	margin: 10px 5px;
}
</style>
 </head>
  
  <body>
  <div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">��Ʒ����</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">��Ӳ�Ʒ</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
				<div id="latest" style="height: 240px; top: 67px; left: 152px;">
  <div class="addproductpage" align="center">
  <form action="addProduct.action" method="post">
  <table width="390" border="1" align="center">
<tbody><tr>
<td width="98">&nbsp;��Ʒ����</td>
<td width="276"><label>
  <input name="proName" type="text" id="proName">
</label></td>
</tr>
<tr>
<td>&nbsp;��Ʒ����</td>
<td><label>
  <input type="text" name="proCode">
</label></td></tr>
<tr>
<td>&nbsp;��ѡ���Ʒ��</td>
<td><label>
  <select name="progId">
    <option>��ѡ���Ʒ��</option>
    <c:forEach var="group" items="${productGroups}">
    	<option value="${ group.progId}">${group.progGroupname }</option>
    </c:forEach>
    </select>
</label></td></tr>
<tr>
<td>&nbsp;ѡ���Ʒ���</td>
<td><label>
  <select name="protypeId">
    <option>��ѡ���Ʒ����</option>
    <c:forEach var="type" items="${types}">
    	<option value="${type.protypeId }">${type.typeName }</option>
    </c:forEach>
  </select>
</label></td></tr>
<tr><td>Ʒ��</td><td>
	<select name="brandId">
		<c:forEach var="brand" items="${brands}">
		<option value="${brand.brandId }">${brand.brandName }</option>
		</c:forEach>
	</select>
</td></tr>
<tr>
<td>&nbsp;��Ʒ����</td>
<td><label>
  <input name="proPrice" type="text" id="proPrice">
</label></td></tr>
<tr>
<td>&nbsp;���Ӳ�Ʒ����</td>
<td><label>
  <input name="stoAmount" type="text" id="stoAmount">
</label></td></tr>
<tr>
<td>&nbsp;��Ʒ��λ</td>
<td><label>
  <input name="proUnit" type="text" id="proUnit">
</label></td></tr>
<tr>
<td>&nbsp;��ѡ��ͼƬ</td>
<td><label>
<div id="Loading" style="display:none" ondblclick="this.style.display='none'"></div>
  <input type="button" name="Submit" value="ѡ��ͼƬ" onclick="javascript:OpenDiv(500,500,'')">
</label><a href="MutiFileUpload.jsp">�ϴ�ͼƬ</a></td></tr>
<tr>
<td>&nbsp;��Ʒ�ص�</td>
<td><label>
  <input name="proFeature" type="text" id="proFeature">
</label></td></tr>
<tr>
<td>&nbsp;��ע</td>
<td><label>
  <textarea name="proRemark" cols="30" rows="5" id="proRemark"></textarea>
</label></td></tr>
<tr>
<td>&nbsp;</td>
<td>&nbsp;</td></tr>
<tr>
<td><input type="submit" name="Submit2" value="�ύ"></td>
<td><label>
  <input type="reset" name="Submit3" value="����">
</label></td></tr>
</tbody></table>
  </form>
  </div>
  </div>
  </div>
  </div>
  
   </body>
</html>
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->