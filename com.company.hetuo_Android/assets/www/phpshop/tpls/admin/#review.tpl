<html>
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script language="JavaScript" src="./jscript/comm/simpleajax.js"></script>
</head>
<body>
<form method="post" action="admincp.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
<table width="90%" >
  <tr>
    <td>id</td>
    <td width="50">看产品</td>
    <td width="100">标题</td>
    <td>优点</td>
    <td>不足</td>
    <td>结论</td>
    <td width="60">ip</td>
     <td width="60">客户订单号</td>
    <td width="50">审核</td>
    <td width="30">删除</td>
  </tr>
{$loop value}
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type='hidden' value="{$value.loop.id}">{$value.loop.id}</td>
    <td><a href="../product.php?pid={$value.loop.pid}" target="_blank">看产品</a></td>
    <td>{$value.loop.title}&nbsp;</td>
    <td>{$value.loop.good}&nbsp;</td>
    <td>{$value.loop.bad}&nbsp;</td>
    <td>{$value.loop.contents}&nbsp;</td>
    <td>{$value.loop.ip}&nbsp;</td>
    <td>{$value.loop.billno}&nbsp;</td>
    <td><input name="c{$value.loop.id}" onClick="var v = '&v=0';if(this.checked == true) {v = '&v=1';};postServer('admin/adminajaxset.php?mid={$mid}&lid={$lid}&switch=fun&id={$value.loop.id}',v,'r{$value.loop.id}')" type="checkbox" {if value.loop.init==1}checked{else}{/if}><span id="r{$value.loop.id}">{if value.loop.init==1}已审{else}未审{/if}</span></td>
    <td><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&delrid={$value.loop.id}" target="_self">删除</a></td>
  </tr>
{/loop}
</table>
	  <div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div>
			    共{$allpage}页
				<span id="up">
				<a href="admincp.php?mid={$mid}&lid={$lid}&pn=1" target="_self">首页</a>
				</span>
				{if allpage>1}
				{$loop page}
				<span{if page.loop==pn} class="f14c fb"{/if}> 
				{if page.loop!=''}
				<a href="admincp.php?mid={$mid}&lid={$lid}&pn={$page.loop}" target="_self">{$page.loop}</a>
				{else}... {/if}
				</span>
				{/loop}
				{/if}
		</div>
		<div class="cls"></div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
		  <input name="ok" type="submit" value="{if act=='edit'}修　改{else}  o  k  {/if}" class="txInput">
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="{$lid}">
		  <input type="hidden" name="mid" value="{$mid}">
          <input type="hidden" name="uid" value="{$uid}">
          {if act=='edit'}<input type="hidden" name="act" value="edituser">{/if}
</div>
</form>
</body></html>