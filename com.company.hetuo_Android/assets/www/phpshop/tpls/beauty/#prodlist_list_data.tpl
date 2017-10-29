{if product!=''}
	{if hotproduct!=''}
	<div class="cl" style="height:3px;"></DIV>
	{else}
	<div class="cl"></div>
	{/if}
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="prolist">
		 <form action="./sidebyside.php" name="byside" method="post" target="_blank" >
			<input type="hidden" name="cid" value="{$ChannelID}">
		  <tr>
		    <td colspan="4" class="pbg ph" style="text-align:left;">
			<div  class="pageb" style="padding:5px 0 5px 0;">
			<input type="image" src="images/default/but_compare.gif" align="middle" />
				<span style="float:right;">
                显示方式：
                <a href="{$caturl.listurl}"><img src="images/default/p.gif" width="13" height="15" border="0" align="absmiddle" alt="栅格显示" /></a> 
				<a href="{$caturl.norurl}"><img src="images/default/l.gif" width="13" height="15" border="0" align="absmiddle" alt="列表显示" /></a>　
				排序方式：
                <a href="{$caturl.hoturl}"><img src="images/default/h.gif" width="13" height="15" border="0" align="absmiddle" alt="最热商品" /></a> 
				<a href="{$caturl.newurl}"><img src="images/default/n.gif" width="13" height="15" border="0" align="absmiddle" alt="最新商品" /></a>　
				每页显示数量：<a href="{$caturl.s_url}">12</a> | <a href="{$caturl.m_url}">30</a>
                </span>		    
			 </div>
            </td>
	       </tr>
	
		  <tr style="height:1px;">
		    <td width="20" style="background:#c8dcb9;"></td>
		    <td colspan="3" class="bggrey" style="background:#c8dcb9;"></td>
	       </tr>
		  <tr>
		    <td colspan="4"  style="text-align:left; border-left:1px solid #eeeeee;">
		{$loop product}	
		<div class="clpro">
			<div class="proimg"><a href="{$product.loop.url}" target="_blank"><img src="{$product.loop.m_pic}" alt="{$product.loop.name}" onerror="this.src='product/nopic.gif'" /></a>
			</div>
			<div id="product_name"><a href="{$product.loop.url}" class="green b">{$product.loop.name}</a></div>
			<div id="proprice">
			{if product.loop.market_offer==1}<div id="price_l"><STRIKE>市场价</STRIKE></div>
			<div id="price_r"><STRIKE> &yen; {$product.loop.price_market}</STRIKE></div>{/if}
			<div id="price_l">售　价</div><div id="price_r">{if product.loop.special_offer==1}<STRIKE>{/if}<font color="#FF0000"> &yen; </font>{$product.loop.price}{if product.loop.special_offer==1}</STRIKE>{/if}</div>
			{if product.loop.special_offer==1}
			<div id="price_l">特　价</div><div id="price_r"><font color="#FF0000"> &yen; {$product.loop.price_special}</font></div>
			{else}{if product.loop.member_offer==1}<div id="price_l">会员价</div><div id="price_r"><font color="#FF0000"> &yen; 
			{$product.loop.price_member}</font></div>{/if}
			{/if}
			<div class="cl"></div>
			</div>
			<div class="clbut">
			{if product.loop.store>0}<a href="#shop" onclick="setUserShopFrame('{$product.loop.id}');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/ban_product.gif" border="0" alt="点击购买" /></a>{else}<img src="images/default/no_ban_product.gif" alt="暂时缺货" border="0" />{/if}
			</div>
		</div>
		{/loop}
			  </td>
	       </tr>
		  <tr>
		    <td colspan="4" class="pbg ph">
			<div class="pageb" style="padding:5px 0 5px 0;">
			  <input type="image" src="images/default/but_compare.gif" align="middle" />
			</div>
            </td>
	       </tr>
		 </form>
	  </table>
      <div class="pages" id="pages_pg_2" style="text-align:right;">
            <span class="count">Pages: {$pn} / {$allpage}</span>
            <span class="number">
              {if pn==1}
                <span class="disabled" title="First Page">&laquo;</span>
              {else}
                <a href="{$firstpage}">&laquo;</a>
              {/if}
              {if pn==1}
                <span class="disabled" title="Prev Page">&#8249;</span>
              {else}
                <a href="{$prvepage}">&#8249;</a>
              {/if}
              {$loop page}
               {if page.loop.pn==pn}
                <span class="current" title="Page">{$page.loop.pn}</span>
               {else}
                <a href="{$page.loop.url}">{$page.loop.pn}</a>
               {/if}
              {/loop}
              {if pn==allpage}
                <span class="disabled" title="Next Page">&#8250;</span>
              {else}
                <a href="{$nextpage}">&#8250;</a>
              {/if}
                {if pn==allPage}
                <span class="disabled" title="Last Page">&raquo;</span>
                {else}
                <a href="{$lastpage}">&raquo;</a>
                {/if}
            </span>
        </div>
{else}
	<b>无商品</b>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td class="pbg ph">&nbsp;</td>
	  </tr>
	  <tr>
		<td height="150" align="center" class="pd prbo"><span style="text-align:center">无搜索结果，请修改您的检索条件并重新搜索。</span></td>
	  </tr>
	  <tr>
		<td class="pbg ph">&nbsp;</td>
	  </tr>
</table>
{/if}