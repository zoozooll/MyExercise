<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<style type="text/css">
body{font-size:12px;}
</style>
<script language="JavaScript" src="../jscript/admin/js.js"></script>
</head>
<body leftmargin="0" topmargin="0">
{if del=='yes'}删除成功！{elseif del=='isdel'}此订单已删除或者不存在！{else}
<form action="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}" method="post" target="_self">
{$loop mybillno}{if mybillno.loop.billno!=mybillno.preve.billno}<div id="mybillno"><div>订单号：{$mybillno.loop.billno} {if uid>1}会员{else}非会员{/if} 订单日期:{$mybillno.loop.add_date} 出货日期:{if mybillno.loop.shipment_date==''}未出货{else}{$mybillno.loop.shipment_date}{/if} 支付价格:￥{$mybillno.loop.pay_price}</div></div>
  <table width="100%" cellspacing="1" cellpadding="1" style="border:1px solid #E7FEE0; padding:5px;">
    <tr>
      <td>图片</td>
      <td>商品名称</td>
      <td>产品编号</td>
      <td>数量</td>
      <td>价格</td>
      <td>会员价</td>
      <td>特价</td>
      <td>售价</td>
      <td>总价</td>
      <td>删除</td>
    </tr>
    <tr>
      <td><a href="../{$mybillno.loop.url}" target="_blank"><img src="../{$mybillno.loop.m_pic}" width="50" onerror="this.src='../product/nopic.gif'" border="0" /></a></td>
      <td><a href="../{$mybillno.loop.url}" target="_blank">{$mybillno.loop.name}</a></td>
      <td>{$mybillno.loop.number}</td>
      <td><input name="num[]" type="text" size="3" value="{$mybillno.loop.num}" /></td>
      <td>{$mybillno.loop.price}</td>
      <td>{$mybillno.loop.price_member}</td>
      <td>{$mybillno.loop.price_special}</td>
      <td><input name="pid[]" type="hidden" value="{$mybillno.loop.pid}" /><input name="sellprice[]" type="text" size="4" value="{$mybillno.loop.sell_price}" /></td>
      <td>{$mybillno.loop.all_price}</td>
      <td>{if checkuser.state=='0'}{if productnum>1}<a onClick="return delYesOrNo()" title="删除" href="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}&deluid={$mybillno.loop.pid}&uid={$uid}" target="_self">删除</a>{/if}{/if}</td>
    </tr>{else}
    <tr>
      <td><a href="../{$mybillno.loop.url}" target="_blank"><img src="../{$mybillno.loop.m_pic}" width="50" onerror="this.src='../product/nopic.gif'" border="0" /></a></td>
      <td><a href="../{$mybillno.loop.url}" target="_blank">{$mybillno.loop.name}</a></td>
      <td>{$mybillno.loop.number}</td>
      <td><input name="num[]" type="text" size="3" value="{$mybillno.loop.num}" /></td>
      <td>{$mybillno.loop.price}</td>
      <td>{$mybillno.loop.price_member}</td>
      <td>{$mybillno.loop.price_special}</td>
      <td><input name="pid[]" type="hidden" value="{$mybillno.loop.pid}" /><input name="sellprice[]" type="text" size="4" value="{$mybillno.loop.sell_price}" /></td>
      <td>{$mybillno.loop.all_price}</td>
      <td>{if checkuser.state=='0'}{if productnum>1}<a onClick="return delYesOrNo()" title="删除" href="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}&deluid={$mybillno.loop.pid}&uid={$uid}" target="_self">删除</a>{/if}{/if}</td>
    </tr>
    {/if}{if mybillno.loop.billno!=mybillno.next.billno}
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td colspan="3"><input name="uid" type="hidden" value="{$uid}" /><input name="update" type="hidden" value="price" /><input type="submit" name="Submit2" value="更新"{if checkuser.state!='0'} disabled{/if} /></td>
    </tr>
  </table>
  {/if}{/loop}
</form>
	<table style="border:1px solid #E7FEE0; padding:5px;" width="100%" cellspacing="0" cellpadding="0">              
		<tr>              
		<td width="100%">  
		  <form action="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}" method="post" target="_self">
          订单处理：<br />
          <input type="radio" value="0" {if checkuser.state=='0'}checked {/if}name="state">处理中
          <input type="radio" value="1" {if checkuser.state=='1'}checked {/if}name="state">已确认
          <input type="radio" value="2" {if checkuser.state=='2'}checked {/if}name="state">出货完成
          <input type="radio" value="3" {if checkuser.state=='3'}checked {/if}name="state">联系不上
          <input type="radio" value="4" {if checkuser.state=='4'}checked {/if}name="state">订单已取消
          <input type="radio" value="5" {if checkuser.state=='5'}checked {/if}name="state">用户退货
		  <input type="submit" value="确定送出" name="Submit4" id="Submit4"> 
		  <a onclick="return delYesOrNo();" href="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}&del=yes" target="_self">删除订单</a>
		  </form>
		</td>              
		</tr>              
	</table>
    <table style="border:1px solid #E7FEE0; padding:5px;" width="100%" cellspacing="0" cellpadding="0">              
		<tr>              
		<td width="100%">  
		  <form action="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}" method="post" target="_self">
          付款状态：<br />
          <input type="radio" value="0" {if checkuser.pay_success=='0'}checked {/if}name="pay_success">未付款
          <input type="radio" value="1" {if checkuser.pay_success=='1'}checked {/if}name="pay_success">网上支付完成
          <input type="radio" value="2" {if checkuser.pay_success=='2'}checked {/if}name="pay_success">银行转账成功
          <input type="radio" value="3" {if checkuser.pay_success=='3'}checked {/if}name="pay_success">邮局电汇成功<br />
          <input type="radio" value="4" {if checkuser.pay_success=='4'}checked {/if}name="pay_success">货到付款成功
          <input type="radio" value="5" {if checkuser.pay_success=='5'}checked {/if}name="pay_success">正在给用户退款
          <input type="radio" value="6" {if checkuser.pay_success=='6'}checked {/if}name="pay_success">给用户退款成功
		  <input type="submit" value="确定送出" name="Submit4" id="Submit4"> 
		  </form>
		</td>              
		</tr>              
	</table>     
	<form name="form1" style="margin:0px; padding:0px;" method="post" target="_self" action="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}">
    <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <!--DWLayoutTable-->
      <tr> 
        <td height="30"><font color="#000099" size="2">姓名：</font> 
          <input size="19" value="{$checkuser.name}" name="name"{if checkuser.state!='0'} disabled{/if}>        <font color="#000099" size="2">电话：</font><font size="2">
        <input name="phone" id="phone" value="{$checkuser.phone}" size="14"{if checkuser.state!='0'} disabled{/if} />
                  </font><font color="#000099" size="2">手机</font><font color="#000099" size="2">：</font><font size="2">
                  <input size="14" value="{$checkuser.mobile}" name="mobile"{if checkuser.state!='0'} disabled{/if} />
                  </font><font size="2">&nbsp;</font></td>
      </tr>
      <tr> 
        <td height="27"><font color="#000099" size="2">地址</font><font color="#000099" size="2">：</font><font size="2"> 
          <input size="70" value="{$checkuser.address}" name="address"{if checkuser.state!='0'} disabled{/if}>
          </font></td>
      </tr>
      <tr> 
        <td height="24"><font color="#000099" size="2">邮编：</font><font size="2">
          <input size="10" name="postcode" value="{$checkuser.postcode}" />
        </font><font color="#000099" size="2">EMAIL</font><font color="#000099" size="2">：</font><font size="2">
        <input value="{$checkuser.email}" name="email" size="26"{if checkuser.state!='0'} disabled{/if} />
          </font></td>
      </tr>
      <tr> 
        <td height="29"><font color="#000099" size="2">提领货号：</font> 
          <input type="text" name="out_number" size="10" value="{$checkuser.billno}">        </td>
      </tr>
      <tr>
        <td height="31" valign="middle"><font color="#000099" size="2">支付方式：</font><font size="2">
        <input name="paymethod" type="text" value="{$checkuser.paymethod}" size="20" />
        {if checkuser.pay_success==1}<font color="#FF0000">(支付成功)</font>{/if}</font>
      	</td>
      </tr>
      <tr>
        <td height="31" valign="middle">
        <font color="#000099" size="2">配送方式：</font>
        <input name="freight" type="text" value="{$checkuser.freight}" size="20" />
        <font color="#000099" size="2">快递运单号：</font>
        <input name="freight" type="text" value="{$checkuser.freight_no}" size="20" />
        <font color="#000099" size="2"> 运费：</font>
        <input name="freightprice" type="text" id="freightprice" value="{$checkuser.freight_price}" size="3"{if checkuser.state!='0'} disabled{/if} /></td>
      </tr>
      <tr> 
        <td height="44" align="left" valign="middle"><font color="#000099" size="2">客户留言：</font>          <textarea rows="2" name="ss" cols="60">{$checkuser.ss}</textarea></td>
      </tr>
      <tr>
        <td height="44" valign="middle"><font color="#000099" size="2">网站备注：
            
        </font><font color="#000099" size="2">
        <textarea name="remark" cols="60" rows="2" id="remark">{$checkuser.remark}</textarea>
        </font></td>
      </tr>
      <tr>
        <td height="21" align="right">
		  <input type="submit" value="修改资料" name="Submit" id="Submit">                 
          <input type="button" name="Submit3" value="关闭窗口" onClick="parent.dispalymyshow('usershop', false)">		
		  <input name="mid" type="hidden" value="{$mid}" /> <input name="lid" type="hidden" value="{$lid}" />
		  <input name="update" type="hidden" value="base" />		</td>
      </tr>
    </table>
	</form>
<table border="0" width="95%" cellspacing="0" cellpadding="0" align=center>               
    <tr>               
       <td width="100%" height="10">
       </td>               
    </tr>
</table>  
  <table width="95%" height="1" cellspacing="1" cellpadding="0">              
    <tr>               
      <td width="100%" bgcolor="#DDDDFF" height="29">         
	  <form action="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}&md={$md}" method="post" target="_self">     
              &nbsp;<!--产品编号<input type="radio" name="prodtype" value="1" />--> 
              产品id号<input type="radio" name="prodtype" value="0" checked />
        <input size="10" name="pid"> 数量<input size="5" name="number">
		  额外售价<input name="price" type="text" size="5" /><input name="uid" type="hidden" value="{$uid}" /> 
          <input name="addproduct" type="submit" id="addproduct" value=" 新 增 "{if checkuser.state!='0'} disabled{/if} >
	  </form>
      </td>                
    </tr>                 
    <tr>                 
      <td width="100%" bgcolor="#DDDDFF" height="1">                 
	  </td>               
    </tr>                 

    <tr>                 
        <td width="100%" bgcolor="#DDDDFF" height="29">                 
		
	  <table>
            <tr>
        	<td width="65%">
			<form action="adminajaxset.php?switch=iframecheck&mid={$mid}&lid={$lid}&billno={$billno}" method="post" target="_self">
	            <input type="hidden" name="mailtype" id="mailtype" value="order_del">
	            通知买家
		    	<input size="36" name="title" value="联络不到通知">	 
	            <textarea rows="3" id="message" name="message" cols="43">本站己多次与您电话连络皆无响应 为了您的订购权益本站会将您订单保留三天 请您拨打客服专线:{$webphone}  ; 24H客服专线:{$webmobile}         以确认订单 谢谢您的惠顾</textarea>
	            <input type="Submit" value="送出" name="Submit5"{if checkuser.state=='2'} disabled{/if}>
			  </form>
              </td>
              <td width="35%">
		  <font size="2">退货通知</font><br><font color="#000080" size="1">本订单已完成退货手续，本站如有服务不周之处涵请见谅</font><br><br>
	      <font size="2">取消订单通知</font><br><font color="#000080" size="1">本订单已取消，本站如有服务不周之处涵请见谅</font>
	      </td>
	  <tr>
        </table>
                 
      </td>
	                 
    </tr>
                 
    <tr>                 
      <td width="100%" height="27">

      </td>                 
    </tr>                 
  </table>
{/if}
<script language="javascript">
parent.setHtml('loadingiframe', '');
</script>
</body>
</html> 