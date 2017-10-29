<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
	<div id="locate"><span id="topnav"><a href="./">首页</a>{$loop catnav} &raquo; <a href="{$catnav.loop.url}">{$catnav.loop.name}</a>{/loop} &raquo; 新闻详情</span> </div>
    <!-- -->
    <div id="container"><div class='mallLogin'> <span class='welcome'>商品新闻</span></div>
        <div class="content">  
          <div class="compareList">
            <ul class="shoppingTab" style="width:945px;">
              <li class="cur" id="tab1">新闻</li>
            </ul>
            <div id="con1">
                <!-- -->
                <div class="list">       
                    <div class="productList">
                        <div style="text-align:center">{$news.title}</div>
                        <div style="text-align:center">作者：{$news.author} 来源：{$news.source_from} 日期：{$news.add_date}</div>
                        <div>{$news.content}</div>
                    </div>
                </div>
                <!-- -->
            </div>
          </div>
        </div>
          {inc file=#productrelated.tpl}
    </div>
    <!-- -->
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>
