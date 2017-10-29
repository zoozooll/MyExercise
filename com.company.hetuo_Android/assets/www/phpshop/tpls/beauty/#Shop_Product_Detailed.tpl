<table border="1" width="100%" bordercolor="#CEE3D0" cellspacing="0" cellpadding="0" style=" border:1px solid #7ECEF4;border-collapse:collapse; margin:0px; padding:0px;">
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
        <TD WIDTH=600 height="28" align="center"><font size="2">单价</font></TD>
        <TD WIDTH=600 height="28" align="center"><font size="2">总价</font></TD>
        <!--td width=600 height="28" align="center"><font size="2">删除</font></td-->
      </TR>
      {$loop tmpshop}
      <TR bgcolor="#EAF7EC" align="center"> 
        <TD width="600" height="1" ><a href="{$weburl}{$tmpshop.loop.url}" target="_blank"><img src="{$weburl}{$tmpshop.loop.m_pic}" width="40" onerror="this.src='{$weburl}product/nopic.gif'" border="0" ></a></td>
        <TD width="600" height="1" align="center"><a href="{$weburl}{$tmpshop.loop.url}" target="_blank">{$tmpshop.loop.name}</a></td>
        <TD width="600" height="1" >{$tmpshop.loop.number}</td>
        <TD width="600" height="1" > 
          <input type="hidden" name="pid[]" value="{$tmpshop.loop.pid}" readonly="readonly">
          <input type="text" name="num[]" value="{$tmpshop.loop.num}" size="3" maxlength="5" readonly="readonly">
        </td>
        <TD width="600" height="1" >{$tmpshop.loop.price}</td>
        <TD width="600" height="1" >{$tmpshop.loop.allprice}</td>
        <!--td width="600" height="1" ><a href="shop.php?switch=iframeshop&pid={$tmpshop.loop.pid}&act=del" target="UserShop_iframe">删除</a></td-->
      </TR>
      {/loop}
      <tr bgcolor="#EAF7EC"> 
        <td colspan="8" width="600" height="28" align="right">
          
    合计:{if buy==1}<span style="background-color: #FFFF00">总价￥<font color=red>{$allprice}</font>(元)</span>{else}<span style="background-color: #FFFF00">商品总价￥<font color=red>{$allprice}</font>(元) + 运费￥<font color=red>{$billnoinfo.freight_price}</font>(元)=￥<font color=red>{$billnoinfo.pay_price}</font>(元)</span>{/if}&nbsp;&nbsp;&nbsp;	</td>
      </tr>
    </table>      
    </td>
      </tr>
</table>