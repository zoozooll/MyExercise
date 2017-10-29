{if product!=''}
	{if hotproduct!=''}
	<div class="cl" style="height:3px;"></DIV>
	{else}
	<div class="cl"></div>
	{/if}
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="prolist">
		 <form action="./sidebyside.php" name="byside" method="post" target="_blank" >
			<input type="hidden" name="retpage" value="{$retpage}">
		  <tr>
		    <td colspan="4" class="pbg ph" style="text-align:left;">
			<div  class="pageb" style="padding:5px 0 5px 0;">
				<input type="image" src="images/default/but_compare.gif" align="absmiddle" />
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
	{$loop product}
		  <tr style="height:1px;">
		    <td width="20" style="background:#EEEEEE;"></td>
		    <td colspan="3" style="background:#EEEEEE;"></td>
	       </tr>
		  <tr>
		    <td class="pbg prbord">
			    <input type="checkbox" name="sdid[]" value="{$product.loop.id}" onclick="storeID(this)" />
			</td>
		    <td width="103" class="pd prbo">
				<a href="{$product.loop.url}" target="_blank"><img src="{$product.loop.m_pic}" onerror="this.src='product/nopic.gif'" title="查看{$categoryinfo.name}  {$product.loop.name}商品" style="border:1px solid #CCC; padding:2px; margin:5px 0px 5px 5px;" /></a>
		    </td>
		    <td width="274" valign="top" class="pd prbo" style="text-align:left">
			<ul class="nolist" style="margin-top:5px;">
			  	<li class="pdb">
			  		<a href="{$product.loop.url}" class="green b" target="_blank">{$product.loop.name}</a></li>{if product.loop.brand!=''}<li>品牌：{$product.loop.brand}</li>{/if}
				<li>{if product.loop.market_offer==1}<STRIKE>市场价 &yen; {$product.loop.price_market}</STRIKE>{/if}
			        {if product.loop.special_offer==1}<STRIKE>{/if}<font color="#FF0000">　价　格 &yen; </font>{$product.loop.price}{if product.loop.special_offer==1}</STRIKE>{/if}
				   {if product.loop.special_offer==1}　<font color="#FF0000">特　价 &yen; {$product.loop.price_special}</font>{else}{if product.loop.member_offer==1}　会员价<font color="#FF0000"> &yen; </font>{$product.loop.price_member}<br />{/if}{/if}
				</li>
				<li class="pdb">
			  		{$product.loop.describe}
			  		<a href="{$product.loop.url}" class="green" target="_blank">更多信息</a>
				</li>
			</ul>			</td>
		    <td width="132" class="pd prbo">
			  <ul class="nolist">
			     <li class="pdb">
				 {if product.loop.store>0}<a href="javascript:void(0)" onclick="setUserShopFrame('{$product.loop.id}');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/ban_product.gif" border="0" alt="点击购买" /></a>{else}<img src="images/default/no_ban_product.gif" alt="暂时缺货" border="0" />{/if}</li>
			  </ul>			
			 </td>
	       </tr>
	{/loop}
		  <tr>
		    <td colspan="4" class="pbg ph" style="text-align:left;">
			<div  class="pageb">
			  <input type="image" src="images/default/but_compare.gif" align="absmiddle" />
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