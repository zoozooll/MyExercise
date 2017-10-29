<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<link href="../../css/beauty/reset.css" rel="stylesheet" type="text/css">
<link href="../../css/beauty/master.css" rel="stylesheet" type="text/css">
<link href="../../css/beauty/mall.css" rel="stylesheet" type="text/css">

</head>
<body id="index">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">全部产品</li>
        </ul>
        <div id="con1">
        	<div style="padding:20px;">
            <div class="news_list">
            	<ul>{$loop arrPruduct}
                	<li>&#8226; <a href="{$arrPruduct.loop.url}" target="_blank">{$arrPruduct.loop.name},{$__Meta.Title}</a></li>
                    {/loop}
                </ul>
            </div>
            </div>		
        </div>
</body></html>