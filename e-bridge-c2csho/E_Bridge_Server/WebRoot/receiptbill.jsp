<%@ page language="java" pageEncoding="gbk"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0066)http://china.alibaba.com/member/join.htm?tracelog=main_toolbar_reg -->
<HTML><HEAD><TITLE>巨匠电子商务</TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<link href="css/register.css" rel="stylesheet" type="text/css">

</HEAD>
<BODY>

<center><!--head-->
<DIV id="container">
<DIV style="font:bolder; font-size:x-large">
	<ul>	  
	  <LI>收款单</LI>
	</ul>
</DIV>


<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			收款单信息
		</li>
	</ul> 
</div>
<hr />
<div style="float:left; width:950px; height:20px;">
	<ul>
		<li style="float:left; font:bolder;">
  			<span>&nbsp;&nbsp;订单编号</span>
			<span class="red">: </span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>${order_id}</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px; height:20px;">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;收款单编号</span>
			<span class="red">: </span>
			&nbsp;&nbsp;&nbsp;
			<span>${receiptcode}</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px; height:20px;">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;发票号</span>
			<span class="red">: </span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
			<span>${invoiceno}</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left;">
  			<span style="font:bolder">&nbsp;&nbsp;核票日期</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="invoicedate"></span>
			<span class="red">输入格式为xxxx-xx-xx</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;产品代码</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="productcode"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;卖家名称</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="purchasername"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;数量</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="amount"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;销售价</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="price"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;金额</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			<span><INPUT maxLength=100 size=32 name="money"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left;">
  			<span style="font:bolder">&nbsp;&nbsp;收款日期</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="receiptdate"></span>
			<span class="red">输入格式为xxxx-xx-xx</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left;">
  			<span style="font:bolder">&nbsp;&nbsp;销售日期</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="salesdate"></span>
			<span class="red">输入格式为xxxx-xx-xx</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder;">
  			<span>&nbsp;&nbsp;详细说明</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; 
			<span>
				<textarea name="detailmemo" style="height:50px; width:400px;" ></textarea>
			</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;欠款金额</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="owemoney"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;已付金额</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="alreadymoney"></span>
		</li>
	</ul> 
</div>

<div style="width:950px; height:50px;"> </div>

<DIV><INPUT style="FONT-SIZE: 14px; HEIGHT: 30px" type=submit value="确认提交" name=Submit></DIV>

</DIV>
</center>
</BODY>
</HTML>

<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->