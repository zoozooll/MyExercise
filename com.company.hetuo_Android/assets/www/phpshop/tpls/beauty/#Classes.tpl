<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
    <div id="locate"><div id="ps"><span id="topnav"><a href="./">首页</a>{$loop catnav} &raquo; <a href="{$catnav.loop.url}">{$catnav.loop.name}</a>{/loop} &raquo; 类别 </span> </div></div>
    <!-- -->
	<div id="container"><div class='mallLogin'> <span class='welcome'>所有的商品类别</span></div>
    <div class="content">  
      <div class="compareList">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">商品分类</li>
        </ul>
        <div id="con1">
            {$loop class}
                {if class.loop.cpid=='0'}
                <div class="categoryList">
                    <h2><a href="{$class.loop.url}">{$class.loop.name}</a></h2>
                    <ul>
                        {else}<li><a href="{$class.loop.url}">{$class.loop.name}</a></li>{/if}
                        {if class.loop.end==1}
                    </ul>
                </div>{/if}
            {/loop}			
        </div>
      </div>
    </div>
</div>
	<!-- -->
{inc file=inc/#body_bottom.tpl}
</div>
</body></html>