<?php 
$ordermail = '<table border="1" width="100%" bordercolor="#017ABC" cellspacing="0" cellpadding="0" style="border-collapse:collapse; margin:0px; padding:0px;">
  <tr>
	<td width="100%" align="center">    
<table  width="100%" height="129" cellspacing="1" cellpadding="1" >
  <TR BGCOLOR=#CEE3D0 > 
	<TD height="32" align="left" bgcolor="#0789E9" style="font-size:16px; font-weight:600; color:#FFFFFF; padding:5px; font-family:\'幼圆\', \'宋体\', \'黑体\';">'.$shop_web_name .' ( 订单确认通知 )</td>
	</TR>
  <TR bgcolor="#EAF7EC" align="center"> 
	<TD height="1" bgcolor="#ACC2FD" style="padding:1px;"></td>
	</TR>
  <TR bgcolor="#EAF7EC" align="center" style="padding:5px;">
    <TD height="1" align="left" valign="middle" bgcolor="#FFFFFF" style="padding:15px; font-family:\'幼圆\', \'宋体\', \'黑体\'; font-size:16px; color:#333333;">
	'.$shop_user_name .'   (先生/女士)您好:<br />
&nbsp;&nbsp;&nbsp;&nbsp;感谢您订购本站商品，本站客服人员会与您电话联络以确认订单，或您可直接拨打客服专线:'.$shop_web_phone .'  24H客服专线:'.$shop_web_mobile .' 即可马上完成确认。</td>
    </TR>
  <TR bgcolor="#EAF7EC" align="center"> 
	<TD height="1" bgcolor="#C9D7FE"></td>
	</TR>
  <tr bgcolor="#EAF7EC"> 
	<td height="28" style="padding:15px 15px 15px 115px; line-height:20px;">
	<div>■您的地址:'.$shop_user_address .'</div>
	<div>■联络电话:'.$shop_user_phone .' '.$shop_user_mobile .'</div>
	<div>■订单总金额: <font color="#FF0000">￥</font>'.$shop_user_pay .'</div>	<div>■取货方式:'.$shop_user_freight .'<br />
	  ■订单查询:<a target="_blank" href="'.$shop_web_billno_url .'">查看'.$shop_user_name .'的订单</a></div></td>
    </tr>
  <TR bgcolor="#EAF7EC" align="center"> 
	<TD height="1" bgcolor="#C9D7FE"></td>
	</TR>
  <tr bgcolor="#EAF7EC">
    <td height="28" bgcolor="#FFFFFF"><center>
      <a target="_blank" href="http://'.$shop_web_url .'/">'.$shop_web_name .'敬上</a><br />
      客服专线:'.$shop_web_phone .' 24H客服专线:'.$shop_web_mobile .' <br />
    </center></td>
    </tr>
</table>      
</td>
  </tr>
</table>';
?>