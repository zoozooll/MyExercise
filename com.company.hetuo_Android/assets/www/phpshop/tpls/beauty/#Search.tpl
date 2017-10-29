<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
	<div id="locate"><span id="topnav"><a href="./">首页</a>{$loop catnav} &raquo; <a href="{$catnav.loop.url}">{$catnav.loop.name}</a>{/loop}</span> &raquo; 搜索 {$keyword}  </div>
    <div>
	<div class="mallLogin"> <span class="welcome">搜索 " {$keyword} " 的商品</span></div>
	<div class="content">
		<div class="compareList">
			<div style="padding:20px 0px 10px 50px;">
			{if categoryproduct==''}
			 关键字  {$keyword} 无搜索到相关商品，请更改搜索条件！
			{else}
			 <div> <b>搜索 {$keyword} 找到以下商品：</b></div>
			 <ul>
			{$loop categoryproduct} 
			<li><a href="{$categoryproduct.loop.url}" target="_blank"> {$keyword} {$categoryproduct.loop.name}({$categoryproduct.loop.prodnum})</a></li>
			{/loop}
			</ul>
			{/if}
			</div>
		</div>
	</div>
    <div class="cl"></div>
  </div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>
