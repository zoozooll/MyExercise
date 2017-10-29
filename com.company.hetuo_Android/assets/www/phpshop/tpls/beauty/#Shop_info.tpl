<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
    <!--head -->
    {inc file=inc/#body_top.tpl}
	<div id="chanr">
	   <div id="divClass">{$channelinfo.name}商品分类</div> 
	  {$loop channel}
	   	   {if channel.loop.cpid==channel.loop.cid}<div id="divClassUB"><a href="{$channel.loop.url}" title="{$channel.loop.name}">{$channel.loop.name}</a></div>{if channel.loop.ccidnum>'0'}
			 <div id="chanb"> 
				    <div id="divClassul" class="chanp">
						<ul>{/if}{/if}{if channel.loop.depth>1}<li><a href="{$channel.loop.url}" title="{$channel.loop.name}">{$channel.loop.name}</a></li>{if channel.next.depth<2}</ul>
						<div class="cl"></div>
			   		</div>
			 </div>{/if}{/if}
	   {/loop}
   </div>
    <div id="chanl">
      <div id="chantop">
	  	 <div id="toplf">
		 	<div style="margin-top:0px;">
	  {section name=index loop=$channelInfo}
		 {if $channelInfo[index].cDisplayB}<a href="{$channelInfo[index].cWebLinkB}" class="green b">{$channelInfo[index].cDisplayB}</a><br />{/if}	
		 <a href="{$channelInfo[index].cWebLink}">{$channelInfo[index].cDisplay}</a>{if $channelInfo[index.index_next].cDisplayB}<br /><br />{elseif  $smarty.section.index.last}{else},{/if}
	  {/section}	  		</div>
        </div>

		 <div id="toprt">
		 <h3>产品资讯</h3>
		 {$loop news}
		  <a href="{$news.loop.url}" class="green" target="_blank" title="{$news.loop.title}">{$news.loop.shot_title}</a><br />
		 {/loop}		 
		 </div>
	  </div>
	  <div class="cl"></div>
      <div id="comd">
					
			<div class="boxcoml">
			<h3>推荐品牌</h3>
			<p>
			{section name=propin loop=$channelPageEntrance}
			<a href="{$channelPageEntrance[propin].cWebLink}">{$channelPageEntrance[propin].cDisplay}</a>{if $smarty.section.propin.last}{else},{/if}
			{/section}			</p>
			</div>
			<div class="boxcomr">
			<h3>推荐商家</h3>
			<p>
			{section name=mer loop=$channelMer}
			<a href="{$channelMer[mer].url}">{$channelMer[mer].MerchantName}</a>{if $smarty.section.mer.last}{else},{/if}
			{/section}			</p>
			<a href="{$ChannelMerUrl}" class="green">更多...</a>			</div>		 
			<div class="cl"></div>
	  </div>
	{$AdsScript}	</div>
    

	
		
    <div class="cl"></div>   
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>
