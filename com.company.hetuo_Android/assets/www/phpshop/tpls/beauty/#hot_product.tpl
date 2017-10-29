<div id="proList">
    <h2><a href="newproduct.php" title="查看更多商品">查看更多</a></h2>
    <div>       
    	{$loop hotproduct}
        <div class="mallList">
            <div class="pdetail">
              <p class="img"><A href="{$hotproduct.loop.url}" target="_blank" title="{$hotproduct.loop.name}"><IMG alt="查看 {$hotproduct.loop.name}" src="{$hotproduct.loop.m_pic}" onerror="this.src='product/nopic.gif'" border="0"></A></p>
              <p class="name"><A href="{$hotproduct.loop.url}" target="_blank" title="{$hotproduct.loop.name}">{$hotproduct.loop.name}</A></p>
              <p class="pricelabel">{if hotproduct.loop.market_offer==1}<STRIKE>市场价:&#165;{$hotproduct.loop.price_market}</STRIKE>{/if}&nbsp;</p>
              {if hotproduct.loop.special_offer==1}
              <p class="pricelabel">特　价:<span class="priceRed">&#165;{$hotproduct.loop.price_special}</span></p>
              {elseif hotproduct.loop.member_offer==1}
              <p class="pricelabel">会员价:<span class="priceRed">&#165;{$hotproduct.loop.price_member}</span></p>
              {else}
              <p class="pricelabel">售　价:<font color="#FF0000"> &yen; </font>{$hotproduct.loop.price}</p>
			  {/if}
            </div>
        </div>
        {/loop}  
    </div><div class="clear"></div>
</div>