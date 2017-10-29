<h2 class="h2css2">相关产品</h2>
<ul class="related">
{if $ProductRelated != NULL}
<div id="xgprotit">&nbsp;相关产品</div>
<!--<div id=prorelt>-->
<div style="float:left;width:{$ProductRelatedWidth}px; border-left:1px solid #EAEAEA;border-top:1px solid #EAEAEA;border-bottom:1px solid #EAEAEA; text-align:center;">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr style="height:25px;">
		{section name=index loop=$ProductRelated}
			<td width="15%" class="td_main">
				<div id="div_blank_1"></div>
				<a href="{$ProductRelated[index].ProdUrl}" target="_blank"><img src="{$ProductRelated[index].ProdImg}" border="0" title="{$ProductRelated[index].ProductName}"/></a>
				<div id="div_name">
					<a href="{$ProductRelated[index].ProdUrl}" class="green" target="_blank">
					<b>{$ProductRelated[index].Name}</b></a>
				</div>
				<div style="vertical-align:bottom; height:80px;">
					{if $ProductRelated[index].r_MerchantCount > 1}
						<h5><A class=b href="{$ProductRelated[index].ProdUrl}" target="_blank">{$ProductRelated[index].LowestPrice} - {$ProductRelated[index].HighestPrice}</A></h5>
						<div class=clbut><A href="{$ProductRelated[index].ProdUrl}" target="_blank"><img src="images/default/but_compare_price.gif" /></A> </div>
					{else}
						<h5><A class=b href="{$ProductRelated[index].ProdUrl}" target="_blank">{$ProductRelated[index].LowestPrice}</A></h5>
						<div class=clbut><A href="{$ProductRelated[index].ProdUrl}" target="_blank"><img src="images/default/but_buy.gif" /></A> </div>
					{/if}
				</div>
			</td>
		{/section}
		</tr>
	</table>
</div>
<div class="cl"></div>
<li><a href="http://www.7mai.com/product.php?pid=1855" class="productImg" title="可折叠便捷洗菜篮"> <img src="compare.php_files/1232084591_2.gif"> </a>
    <p class="priceRed">&#165;15.30</p>
    <p class="pname"><a href="http://www.7mai.com/product.php?pid=1855" title="可折叠便捷洗菜篮">可折叠便捷洗菜篮</a></p>
    <p class="c888">已售出<span class="green">0</span>件</p>
    <p><a href="http://www.7mai.com/product.php?pid=1855" class="quickBuy" title="点击购买此商品">快速购买</a></p>
</li>
<li><a href="http://www.7mai.com/product.php?pid=1840" class="productImg" title="厨房防油贴纸（2片装）"> <img src="compare.php_files/1232083985_2.gif"> </a>
    <p class="priceRed">&#165;10.20</p>
    <p class="pname"><a href="http://www.7mai.com/product.php?pid=1840" title="厨房防油贴纸（2片装）">厨房防油贴纸（2片装）</a></p>
    <p class="c888">已售出<span class="green">0</span>件</p>
    <p><a href="http://www.7mai.com/product.php?pid=1840" class="quickBuy" title="点击购买此商品">快速购买</a></p>
</li>
{else}
<li>暂无相关商品</li>
{/if}
</ul>