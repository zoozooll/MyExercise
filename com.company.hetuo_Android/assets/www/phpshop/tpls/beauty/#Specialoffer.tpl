<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
    <div id="locate"><div id="ps"><span id="topnav"><a href="./">首页</a>{$loop catnav} &raquo; <a href="{$catnav.loop.url}">{$catnav.loop.name}</a>{/loop} &raquo; 商品 </span> </div></div>
    <!-- -->
	<div id="container"><div class='mallLogin'> <span class='welcome'>您可能感兴趣的商品</span></div>
    <div class="content">  
      <div class="compareList">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">商品</li>
        </ul>
        <div id="con1">
            <!-- -->
            <div class="list">       
    <div class="productList">
      <ul>
      {$loop product}
        <li> <a href="{$product.loop.url}" target="_blank" class="productImg" title="{$product.loop.name}"><img src="{$product.loop.m_pic}" alt="{$product.loop.name}" onerror="this.src='product/nopic.gif'" /></a>
       <p class="name"><A href="{$product.loop.url}" target="_blank" title="{$product.loop.name}">{$product.loop.name}</A></p>
              {if product.loop.market_offer==1}
              <p class="pricelabel"><STRIKE>市场价:&#165;{$product.loop.price_market}</STRIKE></p>
              {/if}
              <p class="pricelabel">
              售　价:{if product.loop.special_offer==1}<STRIKE>{/if}
              <font color="#FF0000"> &yen; </font>{$product.loop.price}
              {if product.loop.special_offer==1}</STRIKE>{/if}
              </p>
              {if product.loop.special_offer==1}
              <p class="pricelabel">特　价:<span class="priceRed">&#165;{$product.loop.price_special}</span></p>
              {else}{if product.loop.member_offer==1}
              <p class="pricelabel">会员价:<span class="priceRed">&#165;{$product.loop.price_member}</span></p>
              {/if}{/if}
        </li>
      {/loop}         
      </ul>
          <div class="pages" id="pages_pg_2">
            <span class="count">Pages: {$pn} / {$allPage}</span>
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
              {$loop arrPages}
               {if arrPages.loop.pn==pn}
                <span class="current" title="Page">{$arrPages.loop.pn}</span>
               {else}
                <a href="{$arrPages.loop.url}">{$arrPages.loop.pn}</a>
               {/if}
              {/loop}
              {if pn==allPage}
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
    </div>
  </div>
            <!-- -->
        </div>
      </div>
    </div>
</div>
	<!-- -->
{inc file=inc/#body_bottom.tpl}
</div>
</body></html>