<%@ page language="java" pageEncoding="gbk"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0066)http://china.alibaba.com/member/join.htm?tracelog=main_toolbar_reg -->
<HTML><HEAD><TITLE>�޽���������</TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<link href="css/register.css" rel="stylesheet" type="text/css">

</HEAD>
<BODY>

<center><!--head-->
<DIV id="container">
<DIV style="font:bolder; font-size:x-large">
	<ul>	  
	  <LI>�տ</LI>
	</ul>
</DIV>


<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			�տ��Ϣ
		</li>
	</ul> 
</div>
<hr />
<div style="float:left; width:950px; height:20px;">
	<ul>
		<li style="float:left; font:bolder;">
  			<span>&nbsp;&nbsp;�������</span>
			<span class="red">: </span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>${order_id}</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px; height:20px;">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;�տ���</span>
			<span class="red">: </span>
			&nbsp;&nbsp;&nbsp;
			<span>${receiptcode}</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px; height:20px;">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;��Ʊ��</span>
			<span class="red">: </span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
			<span>${invoiceno}</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left;">
  			<span style="font:bolder">&nbsp;&nbsp;��Ʊ����</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="invoicedate"></span>
			<span class="red">�����ʽΪxxxx-xx-xx</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;��Ʒ����</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="productcode"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;��������</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="purchasername"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;����</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="amount"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;���ۼ�</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="price"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;���</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			<span><INPUT maxLength=100 size=32 name="money"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left;">
  			<span style="font:bolder">&nbsp;&nbsp;�տ�����</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="receiptdate"></span>
			<span class="red">�����ʽΪxxxx-xx-xx</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left;">
  			<span style="font:bolder">&nbsp;&nbsp;��������</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="salesdate"></span>
			<span class="red">�����ʽΪxxxx-xx-xx</span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder;">
  			<span>&nbsp;&nbsp;��ϸ˵��</span>
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
  			<span>&nbsp;&nbsp;Ƿ����</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="owemoney"></span>
		</li>
	</ul> 
</div>

<div style="float:left; width:950px">
	<ul>
		<li style="float:left; font:bolder">
  			<span>&nbsp;&nbsp;�Ѹ����</span>
			<span class="red">: </span> 
			&nbsp;&nbsp;&nbsp;&nbsp; <span><INPUT maxLength=100 size=32 name="alreadymoney"></span>
		</li>
	</ul> 
</div>

<div style="width:950px; height:50px;"> </div>

<DIV><INPUT style="FONT-SIZE: 14px; HEIGHT: 30px" type=submit value="ȷ���ύ" name=Submit></DIV>

</DIV>
</center>
</BODY>
</HTML>

<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->