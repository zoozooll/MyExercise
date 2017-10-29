<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<script type="text/javascript" src="jscript/comm/sidebyside.js"></script>
<script laguage="javascript">
function rediret(url) {
	location.href=url+'&retpage={$retpage}';
}
</script>
</head>
<body id="index">
<div id="wrapper">
	{inc file=inc/#body_top.tpl}
	<div id="locate"><span id="topnav"><a href="./">首页</a> &raquo; 比较您选择的商品 &raquo; <a href="{$retpage}">返回比较更多的商品</a> | <a onclick="removeAllID();" href="{$retpage}">返回并清空比较的商品</a></span> </div>
	<!-- -->
	<div id="container"><div class='mallLogin'> <span class='welcome'>比较这几种您选择的商品</span></div>
    <div class="content">  
      <div class="compareList">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">商品比较</li>
        </ul>
        <div id="con1">
            <!-- -->
	   <div id="tabb">
	        <div id="tab">
			   <ul>
				 <li class="bggnls"><b></b></li>
			   </ul>
		    </div>
		    <div id="labinfo" style="margin-top:0px; margin-right:3px; float:right">
		    <input type="button" border="0" value="返回查询结果" class="butl" onclick="removeAllID();location.href='{$retpage}';">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input name="重填" type="button" class="butl" value="比较更多的型号" onclick="location.href='{$retpage}';"/>  
		    </div>
			<div class="cl"></div>
	   </div>
       <div class="clselect bggner" style="height:10px;">
		       <div class="cl"></div>
  </div> 
	  <div class="cl"></div>
	  <table width="100%" border="0" cellspacing="0" cellpadding="0" id="plist" style="margin-top:-2px;table-layout:fixed">
          <tr style="height:25px;">
			<td width="15%" class="b" bgcolor="#F7F7F7" style="border-right:#EFEFEF 1px solid;border-left:#EFEFEF 1px solid;border-top:#EFEFEF 1px solid;" align="center">商品</td>
			{$loop product}
			<td width="15%" align="center" style="border-right:#EFEFEF 1px solid; line-height:18px" valign="top">
			<div style="vertical-align:top; font-family:'宋体';">
			[<a href="#" onclick="removeID('{$product.loop.id}');rediret('{$product.loop.reurl}')" >去掉该商品</a>]
			</div>
			<div style="width:154px; height:0px;font: 0px/0px sans-serif;clear: both;display: block"></div>
			<a href="{$product.loop.url}" target="_blank"><img src="{$product.loop.m_pic}" border="0" alt="{$product.loop.name}" onerror="this.src='product/nopic.gif'" /></a>
			<div style="height:66px;{if $Col == 5}width:154px;{/if}word-break:break-all;overflow:auto;line-height:16px">
			<a href="{$product.loop.url}" class="green" target="_blank">
			<b>{$product.loop.name}</b></a>
			</div>
			<div id="proprice">
			  <div id="price_l">售　价</div><div id="price_r">{if product.loop.special_offer==1}<STRIKE>{/if}<font color="#FF0000"> &yen; </font>{$product.loop.price}{if product.loop.special_offer==1}</STRIKE>{/if}</div>
			  {if product.loop.special_offer==1}<div class="cl"></div>
			  <div id="price_l">特　价</div><div id="price_r"><font color="#FF0000"> &yen; {$product.loop.price_special}</font></div>
			  {else}{if product.loop.member_offer==1}<div id="price_l">会员价</div><div id="price_r"><font color="#FF0000"> &yen; 
			  {$product.loop.price_member}</font></div>{/if}
			  {/if}
			  <div class="cl"></div>
		    </div>
			<div style="vertical-align:bottom;">
			<a href="#shop" onclick="setUserShopFrame('{$product.loop.id}');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/ban_product.gif" border="0" /></a>			
			</div>
			</td>
			{/loop}
		  </tr>
          <!--tr>
            <td class="b bggrey" align="center">品牌</td>
			{$loop product}
            <td align="left" style="border-right:#EFEFEF 1px solid;">{$ProductInfo[proA].MfName}&nbsp;</td>
			{/loop}
          </tr-->
          <!--tr style="height:25px;">
            <td class="b bggrey" align="center">评分</td>
			{$loop product}
            <td align="left" style="border-right:#EFEFEF 1px solid;">
			{if $ProductInfo[proA].AvgRatingImg}
			<a href="{$ProductInfo[proA].ProdReviewUrl}" class="green">
			<img src="images/default/{$ProductInfo[proA].AvgRatingImg}" alt="{$ProductInfo[proA].AvgRating}" border="0"/>
			</a>
			{else}未评分<br /><a href="{$ProductInfo[proA].ProdWriteReviewUrl}" class="green">在这里发表看法!</a>{/if}
			</td>
			{/loop}
          </tr-->
		  {$loop xml}
		  {if xml.preve.title_name!=xml.loop.title_name}
		  <tr style="height:20px;" class="bggrey">
		  <td style="height:20px;">
		  <div style="width:150px; height:0px;font: 0px/0px sans-serif;clear: both;display: block"></div>
		  <strong><font color="#333333">{$xml.loop.title_name}</font></strong>
		  </td>
		  <td colspan="{$cols}" align="left" style="border-right:#EFEFEF 1px solid;">&nbsp;</td>
		  </tr>
		  {/if}
          <tr style="height:20px;">
            <td class="bggrey"{if xml.loop.eq==0} style="color:#FF0000;"{/if} align="center">{$xml.loop.name}</td>
		  	{$loop xml.loop.value}
            <td align="left" style="border-right:#ECF0E8 1px solid;"><div style="{if $Col == 5}width:154px;{/if}word-break:break-all;overflow:auto;">
			{$xml.loop.value.loop}
			</div></td>
			{/loop}
          </tr>
		  {/loop}
		  <tr style="height:25px;">
			<td width="15%" class="b" align="center" bgcolor="#F7F7F7" style="border-right:#EFEFEF 1px solid;border-left:#EFEFEF 1px solid;border-top:#EFEFEF 1px solid;">商品</td>
			{$loop product}
			<td width="15%" align="center" style="border-right:#EFEFEF 1px solid; line-height:18px" valign="top">
			<div style="vertical-align:top; font-family:'宋体';">
			[<a href="#" onclick="removeID('{$product.loop.id}');rediret('{$product.loop.reurl}')" >去掉该商品</a>]
			</div>
			<div style="width:154px; height:0px;font: 0px/0px sans-serif;clear: both;display: block"></div>
			<a href="{$product.loop.url}" target="_blank"><img src="{$product.loop.m_pic}" border="0" alt="{$product.loop.name}" onerror="this.src='product/nopic.gif'" /></a>
			<div style="height:66px;{if cols>5}width:154px;{/if}word-break:break-all;overflow:auto;line-height:16px">
			<a href="{$product.loop.url}" class="green" target="_blank">
			<b>{$product.loop.name}</b></a>
			</div>
			<div id="proprice">
			  <div id="price_l">售　价</div><div id="price_r">{if product.loop.special_offer==1}<STRIKE>{/if}<font color="#FF0000"> &yen; </font>{$product.loop.price}{if product.loop.special_offer==1}</STRIKE>{/if}</div>
			  {if product.loop.special_offer==1}<div class="cl"></div>
			  <div id="price_l">特　价</div><div id="price_r"><font color="#FF0000"> &yen; {$product.loop.price_special}</font></div>
			  {else}{if product.loop.member_offer==1}<div id="price_l">会员价</div><div id="price_r"><font color="#FF0000"> &yen; 
			  {$product.loop.price_member}</font></div>{/if}
			  {/if}
			  <div class="cl"></div>
		    </div>
			<div style="vertical-align:bottom;">
			<a href="#shop" onclick="setUserShopFrame('{$product.loop.id}');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/ban_product.gif" border="0" /></a>			
			</div>
			</td>
			{/loop}
		  </tr>
  </table>	  
  		<div style="margin-top:10px; margin-right:3px; float:right; width:8px;" >
		</div>
		<div style="margin-top:10px; margin-right:3px; float:right" >
		    <input type="button" border="0" value="返回查询结果" class="butl" onclick="removeAllID();location.href='{$retpage}';">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input name="重填" type="button" class="butl" value="比较更多的型号" onclick="location.href='{$retpage}';"/>  
		</div>
		<div style="height:40px"></div>
        <div  style="height:10px; margin-top:5px;"></div>
        <div class="spons">
	  </div>
            <!-- -->
        </div>
      </div>
    </div>
	</div>
	<!-- -->
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>