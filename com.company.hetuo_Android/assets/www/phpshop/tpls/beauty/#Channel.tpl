<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
  {inc file=inc/#body_top.tpl}
<div id="locate"><a href="./">首页</a>{$loop catnav} &raquo; <a href="{$catnav.loop.url}">{$catnav.loop.name}</a>{/loop}</div>
<div class="channel_ad_top">{$loop adinfo.value1}<a href="{$adinfo.value1.loop.url}" target="_blank"><img title="{$adinfo.value1.loop.text}" alt="{$adinfo.value1.loop.text}" src="{$adinfo.value1.loop.pic}" border="0" onerror="this.style.display='none'" /></a>{/loop}</div>
<div id="container">
<div class="column1">
  <div class="box1" style="margin-bottom: 10px;">
    <h2>商品分类<span><img src="images/beauty/cateh2.gif" alt="商品分类"></span></h2>
    <div class="channel_list"><img src="images/default/023527109.gif" width="3" height="5" align="absmiddle" /> <a href="{$channelinfo.cat_url}" title="{$channel.loop.name}">{$channelinfo.name}</a></div> 
	{$loop channel}
     {if channel.loop.cpid==channel.loop.cid}<div class="hotCate"><div class="hotTitle"><a href="{$channel.loop.url}" title="{$channel.loop.name}">{$channel.loop.name}</a></div></div>{if channel.loop.ccidnum>'0'}
			  <div class="cateList">{/if}{/if}{if channel.loop.depth>1}<a href="{$channel.loop.url}" title="{$channel.loop.name}">{$channel.loop.name}</a>{if channel.next.depth<2}</div>
	 {/if}{/if}
    {/loop}
  </div>
  <div class="box1">
    <h2><span><img src="images/beauty/pinpaih2.gif" alt="品牌推荐"></span></h2>
    <div class="brand"><!-- 广告3 -->
    		{$loop adinfo.value3}<div style=" padding-bottom:1px;"><a href="{$adinfo.value3.loop.url}" target="_blank"><img src="{$adinfo.value3.loop.pic}" border="0" onerror="this.style.display='none'" title="{$adinfo.value3.loop.text}" alt="{$adinfo.value3.loop.text}" width="231"/></a></div>{/loop}
    </div>
  </div>
</div>
<div class="column2">
  <div class="activity" style="padding-bottom: 10px;"> 
    <div id="MainPromotionBanner">
      <div id="SlidePlayer">
      	<div id="channel_ad_flash"></div>
      </div>
    </div>
   
  </div>
  <div style="border-right: 1px solid rgb(0, 160, 233);">
    <h2><img src="images/beauty/h1.gif" alt="最新商品"></h2>
    {$loop product}
    <div class="proList">
    <p class="img"><A href="{$product.loop.url}" target="_blank"><img alt="查看 {$product.loop.name}" src="{$product.loop.m_pic}" onerror="this.src='product/nopic.gif'" border="0"></A></p>
    <p class="name"><A href="{$product.loop.url}" target="_blank">{$product.loop.name}</A></p>
    {if product.loop.market_offer==1}<p class="pricelabel"><STRIKE>市场价 &yen; {$product.loop.price_market}</STRIKE></p>{/if}
    <p class="pricelabel">售　价:<span class="priceRed">{if product.loop.special_offer==1}<STRIKE>{/if}<font color="#FF0000"> &yen; </font>{$product.loop.price}{if product.loop.special_offer==1}</STRIKE>{/if}</span></p>
    {if product.loop.special_offer==1}
    <p class="pricelabel">特　价:<span class="priceRed"> &yen; {$product.loop.price_special}</span></p>
    {else}{if product.loop.member_offer==1}
    <p class="pricelabel">会员价:<span class="priceRed"> &#165; {$product.loop.price_member}</span></p>
    {/if}
    {/if}
    </div>
    {/loop}
	<div class="clear"></div>
  </div>
</div>
<div class="column3">
  <div class="box2">
    <h3>产品资讯</h3>
    <ol>
     {$loop news}
     <li> <a href="{$news.loop.url}" target="_blank" title="{$news.loop.title}">{$news.loop.shot_title}</a><br></li>
    </li>
    {/loop}
    </ol>
  </div>
  <div class="box2">
    <h3>热销产品</h3>
    <ul class="related">
    {$loop recproduct}
        <li><p class="img"><A href="{$recproduct.loop.url}" target="_blank"><img alt="查看 {$recproduct.loop.name}" src="{$recproduct.loop.m_pic}" onerror="this.src='product/nopic.gif'" border="0"></A></p>
            <p class="name"><A href="{$recproduct.loop.url}" target="_blank">{$recproduct.loop.name}</A></p>
            {if recproduct.loop.market_offer==1}<p><STRIKE>市场价 &yen; {$recproduct.loop.price_market}</STRIKE></p>{/if}
            <p>{if recproduct.loop.special_offer==1}<STRIKE>{/if}售　价 &yen; <span class="priceRed">{$recproduct.loop.price}</span>{if recproduct.loop.special_offer==1}</STRIKE>{/if}</p>
             {if recproduct.loop.special_offer==1}<p>特　 价&yen; <span class="priceRed">{$recproduct.loop.price_special}</span></p>{else}{if recproduct.loop.member_offer==1}<p>会员价 &yen; <span class="priceRed">{$recproduct.loop.price_member}</span></p>{/if}{/if}
            <p class="img">{if recproduct.loop.store>0}<a href="#shop" onclick="setUserShopFrame('{$recproduct.loop.id}');myshow('usershop');callLoad('loadingiframe');"><IMG {$recproduct.loop.name}" src="images/default/ban_product.gif" alt="点击购买"></a>{else}<img src="images/default/no_ban_product.gif" alt="暂时缺货" border="0" />{/if}</p>
        </li>
    {/loop}
    </ul>
    <ul class="hotLink">
	  	<li></li>
    </ul>
            <!-- 广告4 -->
    		{$loop adinfo.value4}<a href="{$adinfo.value4.loop.url}" target="_blank"><img src="{$adinfo.value4.loop.pic}" border="0" onerror="this.style.display='none'" width="196"/></a>{/loop}
            
	 
    <div class="clear"></div>
  </div>
</div>
</div>
<script type="text/javascript" src="./jscript/comm/swfobject.js"></script>
<script type="text/javascript">
//<!-- 广告2 -->
var focus_width=500;
var focus_height=210;
var text_height=0;
var swf_height = focus_height+text_height;
var pics="{$loop adinfo.value2}{$adinfo.value2.loop.pic}|{/loop}";
pics = pics.substr(0, pics.length -1);
var links="{$loop adinfo.value2}{$adinfo.value2.loop.url}|{/loop}";
links = links.substr(0, links.length -1);
var texts="成人用品|情趣内衣|情趣用品";
var so = new SWFObject("images/flash/default/picviewer.swf", "mymovie", focus_width, focus_height, "7", "#FFFFFF");
so.addParam("movie", "images/flash/default/picviewer.swf");
so.addParam("menu", "false");
so.addParam("wmode", "opaque");
so.addParam("FlashVars", 'pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height);
so.write("channel_ad_flash");
</script>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>