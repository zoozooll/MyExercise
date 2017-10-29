<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<style type="text/css">
<!--
body{ margin:0px;}
#ordersuccess{ color:#FF0000; border:1px solid #93D900; padding:5px; margin-bottom:2px; font-size:14px;}
form{margin:0px;}
-->
</style>
</head>

<body>
<?php if($this->__muant["tmpshop"]==NULL) { ?>
<div id="ordersuccess">
 &#8226; 未选购任何商品
</div>
<div id="ordersuccess">
 &#8226; <a href="#close" onClick="parent.dispalymyshow('usershop');return false;">点击这里关闭窗口</a>
</div>
<?php } else { ?><?php if($this->__muant["addproduct"]!=NULL) { ?><div id="ordersuccess">
 &#8226; 商品 <?php echo $this->__muant["addproduct"]["name"] ?> 编号 <?php echo $this->__muant["addproduct"]["number"] ?> 已经放入购物车了 !!  如要增加数量请填入数量后按修改订单即可 
</div><?php } ?>
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
  <?php $tmpshopinum = count($this->__muant["tmpshop"]); for($tmpshopi = 0; $tmpshopi<$tmpshopinum; $tmpshopi++) { ?>
  <TR bgcolor="#EAF7EC" align="center"> 
	<TD width="600" height="1" ><img src="<?php echo $this->__muant["tmpshop"]["$tmpshopi"]["m_pic"] ?>" width="40" alt="<?php echo $this->__muant["tmpshop"]["$tmpshopi"]["name"] ?> 购物时间：<?php echo $this->__muant["tmpshop"]["$tmpshopi"]["add_date"] ?>" onerror="this.src='product/nopic.gif'" ></td>
	<TD width="600" height="1" align="center"><?php echo $this->__muant["tmpshop"]["$tmpshopi"]["name"] ?></td>
	<TD width="600" height="1" ><?php echo $this->__muant["tmpshop"]["$tmpshopi"]["number"] ?></td>
	<TD width="600" height="1" > 
	  <input type="hidden" name="pid[]" value="<?php echo $this->__muant["tmpshop"]["$tmpshopi"]["pid"] ?>">
	  <input type="text" name="num[]" value="<?php echo $this->__muant["tmpshop"]["$tmpshopi"]["num"] ?>" size="2" maxlength="5">	</td>
	<TD width="600" ><?php if($this->__muant["tmpshop"]["$tmpshopi"]["store"]>4) { ?>充足<?php } elseif($this->__muant["tmpshop"]["$tmpshopi"]["store"]<1) { ?>暂时无货<?php } else { ?>现货<?php } ?></td>
	<TD width="600" height="1" ><?php echo $this->__muant["tmpshop"]["$tmpshopi"]["price"] ?></td>
	<TD width="600" height="1" ><?php echo $this->__muant["tmpshop"]["$tmpshopi"]["allprice"] ?></td>
	<td width="600" height="1" ><a href="shop.php?switch=iframeshop&pid=<?php echo $this->__muant["tmpshop"]["$tmpshopi"]["pid"] ?>&act=del" target="UserShop_iframe">删除</a></td>
  </TR>
  <?php } ?>
  <tr bgcolor="#EAF7EC"> 
	<td colspan="9" width="600" height="28" align="right">
	  
合计:￥<?php echo $this->__muant["allprice"] ?><!-- +<?php echo $this->__muant["freight"] ?>(消费未满100元加运费<?php echo $this->__muant["freight"] ?>元)=<span style="background-color: #FFFF00">总价￥<font color=red><?php echo $this->__muant["sellprice"] ?></font>(元)</span>&nbsp;&nbsp;&nbsp;	--> <input type="submit" name="modify" value="修改订单">
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
<?php } ?>
<script language="javascript">
parent.displayDiv('loadingiframe', false);
function clearShop() {
	document.form1.action = 'shop.php?switch=iframeshop&act=clear';
	document.form1.submit();
}
</script>
</body>
</html>