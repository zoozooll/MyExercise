<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<script src="jscript/comm/swfobject.js" type="text/javascript"></script>
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
    <div id="locate">&nbsp;</div>
  <div id="container"><div class="left">
  <div id="flash">
  <embed type="application/x-shockwave-flash" src="swf/newPlayer.swf" id="mymovie" name="mymovie" quality="high" wmode="transparent" width="670" height="240">  
  </div>
  <div id="flash_index"></div>
  <script language="javascript" type="text/javascript">
		(function(){
		var xmlP="index.php?switch=getadxml";
		xmlP=escape(xmlP);
		var swfPath = "swf/focuspic_one.swf";
		var so_ad1 = new SWFObject(swfPath, 'focusPlayer', '670', '304', '8', '');
		so_ad1.addParam('allowScriptaccess', 'always');
		so_ad1.addParam('FlashVars', 'xmlP='+xmlP);
		so_ad1.addParam('wmode', 'opaque');
		//so_ad1.write('flash');
		})();
  </script>
  {inc file=#hot_product.tpl}
  <div class="h5"></div>
  <div class="onlineInner">
{$loop adinfo.value3}<a href="{$adinfo.value3.loop.url}"><img src="{$adinfo.value3.loop.pic}" border="0" onerror="this.style.display='none'" /></a>{/loop}
  </div><div class="h5"></div>
  {$loop arrChannelProduct}
  <div id="container"><div class="mallLogin"> <span class="welcome"><a class="topName" href="{$arrChannelProduct.loop.url}" target="_blank">{$arrChannelProduct.loop.name}</a></span> <span style="float:right"><a class="topName" href="{$arrChannelProduct.loop.url}" target="_blank">更多&raquo;</a></span></div>
    <div class="content">  
      <div class="compareList">
        <div>
            <div class="list">
                <div class="productList" style="width:670px;">
                  <ul>{$loop arrChannelProduct.loop.product}
                      <li style="padding-left:20px;">
                      <a href="{$arrChannelProduct.loop.product.loop.url}" target="_blank" class="productImg" title="{$arrChannelProduct.loop.product.loop.name}"><img src="{$arrChannelProduct.loop.product.loop.m_pic}" alt="{$arrChannelProduct.loop.product.loop.name}" onerror="this.src='product/nopic.gif'" /></a>
                      <p class="name"><A href="{$arrChannelProduct.loop.product.loop.url}" target="_blank" title="{$arrChannelProduct.loop.product.loop.name}">{$arrChannelProduct.loop.product.loop.name}</A></p>
                      {if arrChannelProduct.loop.product.loop.market_offer==1}
                      <p class="pricelabel"><STRIKE>市场价:&#165;{$arrChannelProduct.loop.product.loop.price_market}</STRIKE></p>
                      {/if}
                      <p class="pricelabel">
                      售　价:{if arrChannelProduct.loop.product.loop.special_offer==1}<STRIKE>{/if}
                      <font color="#FF0000"> &yen; </font>{$arrChannelProduct.loop.product.loop.price}
                      {if arrChannelProduct.loop.product.loop.special_offer==1}</STRIKE>{/if}
                      </p>
                      {if arrChannelProduct.loop.product.loop.special_offer==1}
                      <p class="pricelabel">特　价:<span class="priceRed">&#165;{$arrChannelProduct.loop.product.loop.price_special}</span></p>
                      {else}{if arrChannelProduct.loop.product.loop.member_offer==1}
                      <p class="pricelabel">会员价:<span class="priceRed">&#165;{$arrChannelProduct.loop.product.loop.price_member}</span></p>
                      {/if}{/if}
                      </li>
                      {/loop}
                  </ul>
                </div>
  			</div>
        	</div>
      	</div>
       </div>
	</div>
    {/loop}
</div>
<div class="right"> 
  <div class="balls">
    <div class="ballInfo">
      <div class="ballList">
        <div style="" id="ball1">
		{$loop class}
			{if class.loop.cpid=='0'}<div><h5><a href="{$class.loop.url}">{$class.loop.name}</a></h5>{else}<a href="{$class.loop.url}">{$class.loop.name}</a>{/if}{if class.loop.end==1}</div>{/if}
		{/loop}
        </div>
	<div>
	 <b>在线客服</b>
        {$loop __Meta.msn}<a href="msnim:chat?contact={$__Meta.msn.loop}" class="gy"><img src="images/default/msnlogo.gif" alt="MSN:{$__Meta.msn.loop}" width="37" height="16" border="0" /></a>{/loop}
        {$loop __Meta.taobao}<a class="gy" target="_blank" href="http://amos1.taobao.com/msg.ww?v=2&uid={$__Meta.taobao.loop}&site=cntaobao&s=2" ><img border="0" src="http://amos1.taobao.com/online.ww?v=2&uid={$__Meta.taobao.loop}&site=cntaobao&s=2" alt="taobao:{$__Meta.taobao.loop}" /></a>{/loop}
        {$loop __Meta.qq}<a target="blank" href="tencent://message/?uin={$__Meta.qq.loop}&amp;Site={$__Meta.Url}&amp;Menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=1:{$__Meta.qq.loop}:9" alt="QQ:{$__Meta.qq.loop}" /></a>{/loop}
	</div>
      </div>
       </div>
    <div class="ballBtm">
      <h2>Shopping with happy!</h2>
      <p>我们将给您最好的服务，订购电话：{$__Meta.web_phone} ; {$__Meta.web_free_mobile}</p>
    </div>
  </div>
<div class="left_ad">{$loop adinfo.value2}<a href="{$adinfo.value2.loop.url}"><img src="{$adinfo.value2.loop.pic}" border="0" onerror="this.style.display='none'" /></a>{/loop}</div>
<div class="left_new">
<div class="h5"></div>
<div id="container"><div class="mallLogin"> <span class="welcome">特别推荐</span></div>
    <div class="content">  
      <div class="compareList">
        <div>
            <div class="list">{if specialProduct!=''}      
                <div class="productList" style="width:245px;">
                  <ul>
                      <li style="padding-left:20px;">
                      <a href="{$specialProduct.url}" target="_blank" class="productImg" title="{$specialProduct.name}"><img src="{$specialProduct.m_pic}" alt="{$specialProduct.name}" onerror="this.src='product/nopic.gif'" /></a>
                      <p class="name"><A href="{$specialProduct.url}" target="_blank" title="{$specialProduct.name}">{$specialProduct.name}</A></p>
                      {if specialProduct.market_offer==1}
                      <p class="pricelabel"><STRIKE>市场价:&#165;{$specialProduct.price_market}</STRIKE></p>
                      {/if}
                      <p class="pricelabel">
                      售　价:{if specialProduct.special_offer==1}<STRIKE>{/if}
                      <font color="#FF0000"> &yen; </font>{$specialProduct.price}
                      {if specialProduct.special_offer==1}</STRIKE>{/if}
                      </p>
                      {if specialProduct.special_offer==1}
                      <p class="pricelabel">特　价:<span class="priceRed">&#165;{$specialProduct.price_special}</span></p>
                      {else}{if specialProduct.member_offer==1}
                      <p class="pricelabel">会员价:<span class="priceRed">&#165;{$specialProduct.price_member}</span></p>
                      {/if}{/if}
                      </li>
                  </ul>
                </div>{/if}
  			</div>
            <div class="news_list">
            	<ul>{$loop arrNews}
                	<li>&#8226; <a href="{$arrNews.loop.url}" title="{$arrNews.loop.title}" target="_blank">{$arrNews.loop.shot_title}</a></li>
                    {/loop}
                </ul>
            </div>
        </div>
      </div>
    </div>
</div>
<div class="right left_ad">
{$loop adinfo.value4}<a href="{$adinfo.value4.loop.url}"><img src="{$adinfo.value4.loop.pic}" border="0" onerror="this.style.display='none'" /></a>{/loop}
  </div>
</div> 
</div>
<div id="online">
  
  <div class="clear"></div>
</div>
</div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>