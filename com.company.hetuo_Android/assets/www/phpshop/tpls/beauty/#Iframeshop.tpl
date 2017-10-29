<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<style type="text/css">
<!--
body{ margin:0px;}
#ordersuccess{ color:#FF0000; border:1px solid #93D900; padding:5px; margin-bottom:2px; font-size:14px;}
form{margin:0px;}
-->
</style>
</head>

<body>
{if tmpshop==NULL}
<div id="ordersuccess">
 &#8226; 未选购任何商品
</div>
<div id="ordersuccess">
 &#8226; <a href="#close" onClick="parent.dispalymyshow('usershop');return false;">点击这里关闭窗口</a>
</div>
{else}{if addproduct!=NULL}<div id="ordersuccess">
 &#8226; 商品 {$addproduct.name} 编号 {$addproduct.number} 已经放入购物车了 !!  如要增加数量请填入数量后按修改订单即可 
</div>{/if}
<div id="ordersuccess"> &#8226; 订购数量如超过库存请与客服联系</div>
<form action="shop.php?switch=iframeshop&act=num" method="post" name="form1" target="UserShop_iframe">
<table border="1" width="100%" bordercolor="#CEE3D0" cellspacing="0" cellpadding="0" style="border-collapse:collapse; margin:0px; padding:0px;">
  <tr>
	<td width="100%" align="center">    
<table  width="100%" height="92" cellspacing="1" cellpadding="1" >
  <TR BGCOLOR=#CEE3D0 > 
	<TD WIDTH=600 height="28" align="center"></td>
	<TD WIDTH=600 height="28" align="center"><font size="2">产&nbsp; 
	  品&nbsp; 名&nbsp; 称</font></td>
	<TD WIDTH=600 height="28" align="center"><font size="2">编&nbsp; 
	  号</font></TD>
	<TD WIDTH=600 height="28" align="center"><font size="2">数量</font></TD>
	<TD WIDTH=600 align="center"><font size="2">库存</font></TD>
	<TD WIDTH=600 height="28" align="center"><font size="2">单价</font></TD>
	<TD WIDTH=600 height="28" align="center"><font size="2">总价</font></TD>
	<td width=600 height="28" align="center"><font size="2">删除</font></td>
  </TR>
  {$loop tmpshop}
  <TR bgcolor="#EAF7EC" align="center"> 
	<TD width="600" height="1" ><img src="{$tmpshop.loop.m_pic}" width="40" alt="{$tmpshop.loop.name} 购物时间：{$tmpshop.loop.add_date}" onerror="this.src='product/nopic.gif'" ></td>
	<TD width="600" height="1" align="center">{$tmpshop.loop.name}</td>
	<TD width="600" height="1" >{$tmpshop.loop.number}</td>
	<TD width="600" height="1" > 
	  <input type="hidden" name="pid[]" value="{$tmpshop.loop.pid}">
	  <input type="text" name="num[]" value="{$tmpshop.loop.num}" size="2" maxlength="5">	</td>
	<TD width="600" >{if tmpshop.loop.store>4}充足{elseif tmpshop.loop.store<1}暂时无货{else}现货{/if}</td>
	<TD width="600" height="1" >{$tmpshop.loop.price}</td>
	<TD width="600" height="1" >{$tmpshop.loop.allprice}</td>
	<td width="600" height="1" ><a href="shop.php?switch=iframeshop&pid={$tmpshop.loop.pid}&act=del" target="UserShop_iframe">删除</a></td>
  </TR>
  {/loop}
  <tr bgcolor="#EAF7EC"> 
	<td colspan="9" width="600" height="28" align="right">
	  
合计:￥{$allprice}<!-- +{$freight}(消费未满100元加运费{$freight}元)=<span style="background-color: #FFFF00">总价￥<font color=red>{$sellprice}</font>(元)</span>&nbsp;&nbsp;&nbsp;	--> <input type="submit" name="modify" value="修改订单">
		<input type="button" name="clear" value="清空购物车" onclick="clearShop()"></td>
  </tr>
</table>      
</td>
  </tr>
</table>  
<br /><center>
		
		<!--<input onClick="parent.dispalymyshow('usershop')" type="button" value="继续采购">
		<input onClick="parent.dispalymyshow('usershop')" type="button" value="关闭窗口">-->
</center>
</form>
{/if}
<script language="javascript">
parent.displayDiv('loadingiframe', false);
function clearShop() {
	document.form1.action = 'shop.php?switch=iframeshop&act=clear';
	document.form1.submit();
}
</script>
</body>
</html>