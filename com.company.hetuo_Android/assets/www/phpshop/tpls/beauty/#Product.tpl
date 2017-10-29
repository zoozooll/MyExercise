<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
	{inc file=inc/#body_top.tpl}
	<div id="ps"><span id="locate"><a href="./">首页</a>{$loop catnav} &raquo; <a href="{$catnav.loop.url}">{$catnav.loop.name}</a>{/loop} &raquo; {$product.name}</span></div>
	{inc file=#product_info.tpl}
	{if arrProductRelated!=NULL}
	{inc file=#productrelated.tpl}
	<div class="cl"></div>
	{/if}
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>