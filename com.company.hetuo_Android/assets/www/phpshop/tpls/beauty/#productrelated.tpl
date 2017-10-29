{if arrProductRelated!=''}
<div class="cl"></div>
<div class="content">
    <ul style="width: 944px;" class="shoppingTab">
      <li id="tab1" class="cur">相关产品</li>
    </ul>
    <div class="productList" style="width:950px;">
          <ul>{$loop arrProductRelated}
                  <li> <a title="{$arrProductRelated.loop.name}" class="productImg" target="_blank" href="{$arrProductRelated.loop.url}"><img onerror="this.src='product/nopic.gif'" alt="{$arrProductRelated.loop.name}" src="{$arrProductRelated.loop.m_pic}"></a>
           		  <p class="name"><a title="{$arrProductRelated.loop.name}" target="_blank" href="{$arrProductRelated.loop.url}">{$arrProductRelated.loop.name}</a></p>
                  {if arrProductRelated.loop.market_offer==1}
                  <p class="pricelabel"><STRIKE>市场价:&#165;{$arrProductRelated.loop.price_market}</STRIKE></p>
                  {/if}
                  <p class="pricelabel">
                  售　价:{if arrProductRelated.loop.special_offer==1}<STRIKE>{/if}
                  <font color="#FF0000"> &yen; </font>{$arrProductRelated.loop.price}
                  {if arrProductRelated.loop.special_offer==1}</STRIKE>{/if}
                  </p>
                  {if arrProductRelated.loop.special_offer==1}
                  <p class="pricelabel">特　价:<span class="priceRed">&#165;{$arrProductRelated.loop.price_special}</span></p>
                  {else}{if arrProductRelated.loop.member_offer==1}
                  <p class="pricelabel">会员价:<span class="priceRed">&#165;{$arrProductRelated.loop.price_member}</span></p>
                  {/if}{/if}
                  </li>{/loop}
            </ul>
    </div>
</div>
{/if}