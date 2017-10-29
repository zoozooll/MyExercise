<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<link href="../../css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script language="JavaScript" src="./jscript/comm/divwindows.js"></script>
</head>
<body style="overflow:hidden; border:hidden;" scroll="no">
<div id="dvTop">
 <div id="divTopMsg">
     <a href="http://www.phpshop.cn/" target="_blank" id="aLogo">
	 <img class="logo" src="images/default/admin/logo.gif" alt="logo" border="0"></a>
	<!--用户信息-->
	<div id="divUsrMsg" class="fLeft">
	  您好，<b>{$admin_name}</b> [ <a href="./admin/checklogin.php?logout=yes" class="toplink">退出</a>，
	  <a href="admincp.php?mid={$mid}&lid=39" target="adminMain">我的帐户</a>，
	  <a onclick="checmenu(1)" href="admincp.php?mid=&lid=" target="adminMain">欢迎页</a>，<a href="http://{$web_url}" target="_blank">网站首页</a> ]
	</div>
	<!--右上小连接-->
	<div id="divToplink" class="fRig">
	   <a href="#"></a>
	   <span id="spnNewFunc">
	   <a href="#"  target="_blank"></a>
	   <a href="#"></a>
	   <a href="#" target="_blank"></a>
	   </span>	
	</div>
  </div>
  <div id="divTopNav">
    <!--主导航按钮onClick="checmenu('{$admin_menu.loop.id}');return false;" -->
	<ul id="ulTopNav">
	{$loop admin_menu}
		{if admin_menu.loop.orderid==0}
		<li id="admin_menu">
		<a href="javascript:void(0)" id="amenu{$admin_menu.loop.id}" class="{if admin_menu.loop.id==mid}navOn f14e{elseif admin_menu.loop.id=='1'}navOn f14e{elseif admin_menu.next.id>'0'}navNml f14c{else}last f14c{/if}" >{$admin_menu.loop.name}</a>
			<ul>{/if}{if admin_menu.loop.orderid>0}
				<li> <a href="/admin#" onClick="show('','{$admin_menu.loop.fsid}','{$admin_menu.loop.id}'); return false" id="aMenuInbox">{$admin_menu.loop.name}</a></li>{/if}{if admin_menu.next.orderid==0}
			</ul>
		</li>
		{/if}
	{/loop}
    	<!--li id="admin_menu"><a href="javascript:void(0)" class="f14c">快捷菜单</a>
        	<ul><div>
            {$loop admin_menu}{if admin_menu.loop.orderid==0}<b>{/if}{if admin_menu.loop.orderid>0}<a href="javascript:void(0);" onClick="show('','{$admin_menu.loop.fsid}','{$admin_menu.loop.id}'); return false">{/if}{$admin_menu.loop.name}{if admin_menu.loop.orderid>0}</a>{/if}{if admin_menu.loop.orderid=='0'}→</b>{/if}　{/loop}
            </div></ul>
        </li-->
    </ul>
    <div id="divSearch">
	  <div class="fLeft"></div>
	  <div class="bgcolor9 f12w" id="tbMsg">
	  	<span id="msg"></span>	  
	  </div>
    </div>
  </div>
</div>
<!--页面顶部结束-->

<script language="javascript">
function checmenu(v){
	for (i = 1; i < 40; i++) {
		var n = 'amenu'+i;
		if(document.getElementById(n) != null) {
			document.getElementById(n).className="navNml f14c";
		}
	}
	document.getElementById('amenu'+v).className='navOn f14e';
}
function show(name,mid,id) {
	document.getElementById("adminMain").src = "admincp.php?mid="+mid+"&lid="+id;
	SetWinHeight(document.getElementById("adminMain"));
	checmenu(mid);
}

window.status = "Welcome to {$site}";
</script>
<!-- bottom -->
<iframe id="adminMain" name="adminMain" src="admincp.php?mid={$mid}&lid={$lid}" frameborder="0" width="100%" height="350" onload="Javascript:SetWinHeight(this)"></iframe>
<div class="cls"></div>
<div id="bottom"></div>
<script type=text/javascript><!--//--><![CDATA[//><!--
var iframe_height = document.documentElement.clientHeight - 116;//window.screen.availHeight - 250;//document.body.height - 82;
//alert(iframe_height + '==' + document.body.offsetHeight + '++' + document.body.clientHeight + '--' + document.documentElement.clientHeight + '..' + window.screen.availHeight)
var body_height = document.body.offsetHeight;
//(document.body.offsetHeight);
function SetWinHeight(obj) {
var win=obj;
//alert(document.body.clientHeight);
win.height = iframe_height;
}
window.onload=menuFix;
//--><!]]></script>
</body>
</html>