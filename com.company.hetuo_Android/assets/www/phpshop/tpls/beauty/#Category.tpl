<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<script type="text/javascript" src="jscript/comm/sidebyside.js"></script>
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl} 
	<div id="locate"><a href="./">首页</a>{$loop catnav} &raquo; <a href="{$catnav.loop.url}">{$catnav.loop.name}</a>{/loop} </div><span id="topnav"></span>
    <div>
	{inc file=#prodlist_select.tpl}
	<div id="ttm">
	{if hotproduct!=''}
		<h2>热销产品</h2>
		<div id="clprolist">
		{$loop hotproduct}
		<div class="clpro">
		  <div class="proimg"><a href="{$hotproduct.loop.url}" target="_blank"><img src="{$hotproduct.loop.m_pic}" alt="{$hotproduct.loop.name}" onerror="this.src='product/nopic.gif'" /></a>
		  </div>
		  <div id="product_name"><a href="{$hotproduct.loop.url}" class="green b">{$hotproduct.loop.name}</a></div>
		  <div id="proprice">
			  {if hotproduct.loop.market_offer==1}<div id="price_l"><STRIKE>市场价</STRIKE></div>
			  <div id="price_r"><STRIKE> &yen; {$hotproduct.loop.price_market}</STRIKE></div><div class="cl"></div>{/if}
			  <div id="price_l">售　价</div><div id="price_r">{if hotproduct.loop.special_offer==1}<STRIKE>{/if}<font color="#FF0000"> &yen; </font>{$hotproduct.loop.price}{if hotproduct.loop.special_offer==1}</STRIKE>{/if}</div><div class="cl"></div>
			  {if hotproduct.loop.special_offer==1}
			  <div id="price_l">特　价</div><div id="price_r"><font color="#FF0000"> &yen; {$hotproduct.loop.price_special}</font></div>
			  {else}{if hotproduct.loop.member_offer==1}<div id="price_l">会员价</div><div id="price_r"><font color="#FF0000"> &yen; 
			  {$hotproduct.loop.price_member}</font></div>{/if}
			  {/if}
			  <div class="cl"></div>
		  </div>
		  <div class="clbut">
		  {if hotproduct.loop.store>0}<a href="#shop" onclick="setUserShopFrame('{$hotproduct.loop.id}');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/ban_product.gif" border="0" /></a>{else}<img src="images/default/no_ban_product.gif" alt="暂时缺货" border="0" />{/if}
		  </div>
		</div>
		{/loop}
		</div>
	{/if}
	{if view!='list'}{inc file=#prodlist_data.tpl}{else}{inc file=#prodlist_list_data.tpl}{/if}
    <br />
	</div>
    <div id="ttr">
	   <h2>产品资讯</h2>
	   <ul>{if news!=''}
	   {$loop news}
	   <li><a href="{$news.loop.url}" target="_blank" title="{$news.loop.title}">{$news.loop.shot_title}</a></li>
	   {/loop}{/if}
	   </ul>
	<div id="ad_catr">{$loop adinfo.value1}<a href="{$adinfo.value1.loop.url}" target="_blank"><img title="{$adinfo.value1.loop.text}" alt="{$adinfo.value1.loop.text}" src="{$adinfo.value1.loop.pic}" border="0" onerror="this.style.display='none'"/></a>{/loop}</div>
	</div>
    <div class="cl"></div>
  </div>
{inc file=inc/#body_bottom.tpl}
</div>
<script language="javascript">
checkSelect('sdid[]');
</script>
</body>
</html>
