<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="css/global.css" rel="stylesheet" type="text/css" />
<link href="css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1[1].2.1.pack.js"></script>
<script type="text/javascript" src="js/test.js"></script>
<script src="js/slide.js" type="text/javascript"></script>
<script type="text/javascript">
var headflag=true;
function stop(obj){
	 document.getElementById(obj).scrollamount="0";
	}
	
	function start(obj){
		document.getElementById(obj).scrollamount="4";
	}
	
	function entityTextfocus(){				
		var con=document.getElementById("word");
		//alert(con.value);
		if(headflag==true){
			con.value='';
			con.style.color='black';
		}
		headflag=false;
	}	 
  function  set(obj)   
  {   
        if(!obj.isHomePage(obj.href))   
        {   
            obj.setHomePage(obj.href);   
            event.returnValue=false;   
        }   
  } 
  
  function safeExit(){
		var sa=window.confirm("真的要退出么?");
		if(sa==true){window.location='removeAttributeAction.action';}
		return;
	}
function show(obj){						
		    	var elem = document.getElementById(obj);	
		    	var x=document.getElementById("Cart").offsetTop;//window.event.x;
		    	var y=document.getElementById("Cart").offsetLeft;;//window.event.y; 
		    		elem.style.visibility='visible';
		    		elem.style.position='absolute';
		    		//alert(elem.style.position);
		    		elem.style.left=y+"px";
		    		//alert(x);alert(y);
		    		elem.style.top=x+240+"px";		    		    	
		    	
		    }
		    
function hide(obj){
	var elem = document.getElementById(obj);			
		elem.style.visibility='hidden';

}		    
function hiden(obj){
		window.setInterval(hide(obj),2000);
		//alert(elem.style.visibility);
}
</script>
<style type="text/css">
  a{behavior:url(#default#homepage);color:#554C11;font-size:12px}
  
#Layer1 {
	position: absolute;
	left: 750px;
	top: 712px;
	width: 162px;
	height: 138px; 
	z-index: 1;
}

.STYLE2 {
	color: #FF0000;
	font-weight: bold;
}
#apDiv1 {		
	float:left;
	background-image: url("images/search/back_search.gif");
	background-repeat: no-repeat;
	clear: both;
	width: 900px;
	height: 70px;
}
#Cart{	
	background-image: url("images/search/cart.gif");
	background-repeat: no-repeat;
	width: 348px;
	height: 25px;
	
}

#cartli{
 float: left;
}

.alin{
	padding-top: 10px;
}

-->
        </style>

<body>
	<div id="container" style="height: auto;padding: 5px 0px 3px 0px;">
		<div style="float:left;">
			<a href="http://localhost:8088/E_Bridge/" onclick="set(this)">设为首页</a>
		</div>
		<div style="float:left;width: 89%">
			<marquee id="mar" scrollamount="4" direction="left"
				onmouseover="stop(this)" onMouseOut="start(this)">
				<c:choose>
					<c:when test="${purchaser==null}">
					您好，欢迎您来到Today！<SPAN>您想继续购物之旅？<A href="login.jsp">[请登录]</A>，您是新用户？<A
							href="register.jsp">[免费注册]</A> </SPAN>
					</c:when>
					<c:otherwise>
						<span style="color: red;">${purchaser.purName}</span>,你好,欢迎光临TODAY电子商务网站!<a
							href="#" onClick="safeExit();"><span
							style="color:red;font-size: 14px;">&nbsp;&nbsp;&nbsp;注销</span> </a>
					</c:otherwise>
				</c:choose>
			</marquee>
		</div>
		<div style="float:left">
			<a href="register.jsp">加入我们</a>
		</div>
		<div id="banner">
			<img src="image/top1.gif" width="901" height="150" />
		</div>
		<div id="globallink">
			<c:choose>
				<c:when
					test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
					<ul>
						<li>
							<a href="index.jsp">首 页</a>
						</li>
						<li>
							<a href="userdata.jsp">个人资料</a>
						</li>
						<li>
							<a href="myshopproductlist.jsp">我的商铺</a>
						</li>
						<li>
							<a href="shopcart.jsp">购 物 车</a>
						</li>
						<li>
							<a href="findAllOrderAction.action">订单中心</a>
						</li>
						<li>
							<a href="findAllRecivery.action">财务中心</a>
						</li>
						<li>
							<a href="findAllDelivery.action">物流中心</a>
						</li>
						<li>
							<a href="myshopproductlist.jsp">产品中心</a>
						</li>
						<li>
							<a href="#" onClick="safeExit();">安全退出</a>
						</li>
					</ul>
				</c:when>
				<c:when
					test="${purchaser.purIsvendot=='no'||(purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus!=1)}">
					<ul>
						<li>
							<a href="index.jsp">首 页</a>
						</li>
						<li>
							<a href="userdata.jsp">个人资料</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">我的商铺</a>
						</li>
						<li>
							<a href="shopcart.jsp">购 物 车</a>
						</li>
						<li>
							<a href="findAllOrderAction.action">订单中心</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">财务中心</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">物流中心</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">产品中心</a>
						</li>
						<li>
							<a href="#" onClick="safeExit();">安全退出</a>
						</li>
					</ul>
				</c:when>
				<c:otherwise>
					<ul>
						<li>
							<a href="index.jsp">首 页</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">个人资料</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">我的商铺</a>
						</li>
						<li>
							<a href="shopcart.jsp">购 物 车</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">订单中心</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">财务中心</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">物流中心</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">产品中心</a>
						</li>
						<li>
							<a onClick="javascript:void(0);">安全退出</a>
						</li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="apDiv1">
			<div id="remen" style="margin-left:20px;padding: 5px 0px 0px 0px;">
				<span>热门搜索：</span><span><marquee id="mar2" scrolldelay="100"
						scrollamount="8" direction="right" behavior=alternate
						onmouseover="stop(this)" onMouseOut="start(this)"
						style="overflow: hidden;color:yellow">
						<c:forEach var="p" items="${hots}">
							<a href="showProduct.action?proId=${p.proId}">${p.proName}</a>&nbsp;&nbsp;</c:forEach>
					</marquee> </span>
			</div>
			<div style="margin-top:0px; float: left">
				<form id="search" action="listSearchAction.action" method="post">
					&nbsp;&nbsp;
					<span style="font-size:13px;font-weight: bold;margin-left: 10px;">产品|买家|卖家搜索:&nbsp;&nbsp;
					</span>
					<input type="text" name="condition" id="word" value="请输入您要查找的内容!"
						style="color:gray;width: 180px;" onFocus="entityTextfocus()" />
					<input type="hidden" name="page" value="1" />
					<input type="hidden" name="pageSize" value="24" />
					<select name="entityName" style="margin-top: 3px;" id="selectName">
						<option value="Product">
							产品
						</option>
						<option value="Purchaser">
							买家|卖家
						</option>
					</select>
					&nbsp;
					<span><input type="image"
							src="images/search/search_submit.jpg"
							style="padding-top:1;height:auto;width:auto;" /> </span>
					<div id="auto" style="background-color: white;z-index: 100"></div>
				</form>
			</div>

			<div id="Cart"
				style="margin:4px 5px 0px 0px;float: right;padding-top:5px;">
				<span style="margin-left: 40px;"> <a href="shopcart.jsp"
					onMouseOver="show('shopcart');">购物车有<strong style="color: red;">
							<c:choose>
								<c:when test="${cart==null}">0</c:when>
								<c:otherwise>${cart.productAllSumOnCart}</c:otherwise>
							</c:choose> </strong>&nbsp;件商品</a> </span>
				<div id="shopcart"
					style="visibility:hidden;border: 1px solid yellow;background-color: white;position: absolute;z-index:6;"
					onClick="hiden(this);">
					<table onClick="hiden('shopcart');"
						style="font-size: 12px;width:400px;" border="1" bordercolor="blue">
						<tr>
							<td style="border:1px dotted blue;">
								商品预览
							</td>
							<td style="border:1px dotted blue;">
								商品编号
							</td>
							<td style="border:1px dotted blue;">
								商品名称
							</td>
							<td style="border:1px dotted blue;">
								商品价格
							</td>
							<td style="border:1px dotted blue;">
								商品总数
							</td>
						</tr>
						<c:forEach var="map" items="${sessionScope.cart.cart}">

							<tr>
								<td style="border:1px dotted blue;">
									<img src="${map.value.product.proImagepath}" alt="图片预览"
										width="30px" height="30px" />
								</td>
								<td style="border:1px dotted blue;">
									${map.value.product.proId}
								</td>
								<td style="border:1px dotted blue;">
									&nbsp;&nbsp;
									<a href="showProduct.action?proId=${map.value.product.proId}"
										onClick="hiden('shopcart');"><font color="red">${map.value.product.proName}</font>
									</a>
								</td>
								<td style="border:1px dotted blue;">
									<br>
									${map.value.baseprice}
								</td>
								<td style="border:1px dotted blue;">
									${map.value.productSum}
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="5" align="right"
								style="border:1px dotted blue;color: red;font-size: 15px;">
								&nbsp;商品总金额：￥${sessionScope.cart.allMoneyOnCart}元
							</td>
						</tr>
					</table>
				</div>
				<span style="margin-left: 65px;"> <a
					href="beforeCreateOrderAction.action?method=beforeCreateOrder">去结算</a>
				</span>
				<span style="margin-left: 24px;"><c:choose>
						<c:when test="${purchaser==null}">
							<a onClick="javascript:void(0);">我的订单</a>
						</c:when>
						<c:otherwise>
							<a href="findAllOrderAction.action">我的订单</a>
						</c:otherwise>
					</c:choose> </span>
			</div>
		</div>