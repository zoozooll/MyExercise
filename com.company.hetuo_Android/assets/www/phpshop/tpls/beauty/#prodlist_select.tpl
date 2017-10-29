<div id="ttl">
	<h2>商品类别</h2>
	<div id="divClass"><img src="images/default/023527109.gif" width="3" height="5" align="absmiddle" /> {$categoryinfo.name}</div> 
	<div id="divCat">
{if isleaf=='0'}
		<div class="bggyls">
			<div class="opt">
			<h3>按类别浏览</h3>
			<ul>
			{$loop category}
			<li><a href="{$category.loop.url}" class="whiter" title="查看{$category.loop.name}商品">{$category.loop.name}</a></li>
			{/loop}
			</ul>
			</div>
		</div>
{else}
	{if brand!=''}
		<div class="bggyls opbg">
			<div class="opt">
				<h3>品牌类别</h3>
			 <div class="sl"> 
				<ul>{if nobrandurl!=''}<li><a href="{$nobrandurl.url}">{$nobrandurl.name} <img src="./images/default/close.gif" border="0" id="close_des" /></a></li>{/if}
				{$loop brand}<li><a href="{$brand.loop.url}">{$brand.loop.name}</a></li>{if brand.loop.more=='1'}</ul><ul id="brandfilter" style="display:none;">{/if}{if brand.loop.more=='2'}</ul><ul><li id="more"><a href="javascript:void(0)" onclick="document.getElementById('brandfilter').style.display='';this.style.display='none'">...more</a></li>{/if}{/loop}
				</ul>
			</div>
			</div>
		</div>
	{/if}
	{$loop filter}
		{if filter.loop.ar==1}
		<div class="bggyls opbg">
			<div class="opt">
			<h3>{$filter.loop.name}</h3>
			<div id="atr" class="sl scroll">
			  <ul>
			  {if filter.loop.select!=''}<li id="closeatr"><a href="{$filter.loop.selecturl}">{$filter.loop.select} <img src="./images/default/close.gif" border="0" id="close_des" /></a></li>{/if}
			  {if filter.loop.url!=''}<li><a href="{$filter.loop.url}">{$filter.loop.value}</a></li>{/if}
		{else}
			  {if filter.loop.url!=''}
			  {if filter.loop.more=='1'}</ul><ul id="atr{$filter.loop.id}" style="display:none;">{/if}
			  <li><a href="{$filter.loop.url}">{$filter.loop.value}</a></li>
			  {if filter.loop.more=='2'}</ul><ul><li id="more"><a href="javascript:void(0)" onclick="document.getElementById('atr{$filter.loop.id}').style.display='';this.style.display='none'">...more</a></li>{/if}
			  {/if}
		{/if}
		{if filter.loop.br==2}
			  </ul>
		   </div>
		   </div>
		</div>
		{/if}
	{/loop}
	{if rangeprice!=''}
		<div class="bggyls opbg">
			<div class="opt">
				<h3>价格范围</h3>
				<ul>
				{if nopriceurl!=''}<li><a href="{$nopriceurl}">去掉价格选项 <img src="./images/default/close.gif" border="0" id="close_des" /></a></li>{/if}
				{$loop rangeprice}{if rangeprice.loop.url!=''}<li><a href="{$rangeprice.loop.url}">{if rangeprice.first.price==rangeprice.loop.price}小于 {$rangeprice.loop.price}{elseif rangeprice.last.price==rangeprice.loop.price}大于{$rangeprice.loop.price}{else}{$rangeprice.preve.price} ~ {$rangeprice.loop.price}{/if}</a></li>{/if}{/loop}
				</ul>
			</div>
		</div>
	{/if}
{/if}
	 <form name="prodlist" action="{$CategoryUrl}" method="post">
	   <div class="bggyls opbg">
		   <div class="opt">
			  <h3>在结果中搜索</h3>
			  <input type="text" class="opin" name="see" value="{$se}" />
			  <input type="submit" border="0" class="buts" value="搜　索">
			  <br />
		   </div>
	   </div>
	  </form>
	   <div><br />
	   </div>
	</div>		
	<div id="ad_catl">
    {$loop adinfo.value2}<a href="{$adinfo.value2.loop.url}" target="_blank"><img title="{$adinfo.value2.loop.text}" alt="{$adinfo.value2.loop.text}" src="{$adinfo.value2.loop.pic}" border="0" onerror="this.style.display='none'"/></a>{/loop}</div>
</div>