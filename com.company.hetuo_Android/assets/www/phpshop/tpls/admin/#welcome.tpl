<html>
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>欢迎使用本购物系统 PHPShop v1.0</div>
</div>
<div id="divMainBodyContent">
    <div id="divMainBodyContentTitle">快捷菜单:</div>
    <div class="fLeft">{$loop adminsetinfo}{if adminsetinfo.loop.orderid==0}<br /><b>{/if}{if adminsetinfo.loop.orderid>0}<a href="javascript:void(0);" onClick="parent.show('','{$adminsetinfo.loop.fsid}','{$adminsetinfo.loop.id}'); return false">{/if}{$adminsetinfo.loop.name}{if adminsetinfo.loop.orderid>0}</a>{/if}{if adminsetinfo.loop.orderid=='0'}→</b>{/if}　{/loop}<br /><br /></div>
    <div class="cls"></div>
</div>
<div id="BodyContent">
</div>

</body></html>