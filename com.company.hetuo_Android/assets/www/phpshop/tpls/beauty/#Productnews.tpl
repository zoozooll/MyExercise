<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
</head>
<body id="index">
<div id="wrapper">
    {inc file=inc/#body_top.tpl}
    <div id="locate"><div id="ps"><span id="topnav"><a href="./">首页</a> &raquo; 新闻 </span> </div></div>
    <!-- -->
	<div id="container"><div class='mallLogin'> <span class='welcome'>最新的商品新闻，最关注的商品新闻</span></div>
    <div class="content">  
      <div class="compareList">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">商品新闻</li>
        </ul>
        <div id="con1">
        	<div style="padding:20px;">
            <div class="news_list">
            	<ul>{$loop news}
                	<li>&#8226; <a href="{$news.loop.url}" target="_blank">{$news.loop.title}</a> {$news.loop.add_date}</li>
                    {if news.loop.br==1}<h2><a></a></h2><br />{/if}
                    {/loop}
                </ul>
            </div>
            <div>
				<div class="pages" id="pages_pg_2">
                    <span class="count">Pages: {$pn} / {$allPage}</span>
                    <span class="number">
                      {if pn==1}
                        <span class="disabled" title="First Page">&laquo;</span>
                      {else}
                        <a href="{$firstpage}">&laquo;</a>
                      {/if}
                      {if pn==1}
                        <span class="disabled" title="Prev Page">&#8249;</span>
                      {else}
                        <a href="{$prvepage}">&#8249;</a>
                      {/if}
                      {$loop arrPages}
                       {if arrPages.loop.pn==pn}
                        <span class="current" title="Page">{$arrPages.loop.pn}</span>
                       {else}
                        <a href="{$arrPages.loop.url}">{$arrPages.loop.pn}</a>
                       {/if}
                      {/loop}
                      {if pn==allPage}
                        <span class="disabled" title="Next Page">&#8250;</span>
                      {else}
                        <a href="{$nextpage}">&#8250;</a>
                      {/if}
                        {if pn==allPage}
                        <span class="disabled" title="Last Page">&raquo;</span>
                        {else}
                        <a href="{$lastpage}">&raquo;</a>
                        {/if}
                    </span>
                </div>
     		</div>
            </div>		
        </div>
      </div>
    </div>
</div>
	<!-- -->
{inc file=inc/#body_bottom.tpl}
</div>
</body></html>