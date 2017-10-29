<html>
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
<script language="javascript">
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid={$mid}&lid={$lid}&cid="+val;
}
</script>
</head>
<body>
<form method="post" action="admincp.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">

	<ul id="ClassesMenu" onMouseOut="setSelectElement(document)" onMouseOver="setSelectElement(document, false)">
		<li id="menutitle"><a href="javascript:void(0)">选择 → 商品类别列表</a>
			<ul id="title" class="scroll">
			{$loop classes}
				<li class="{if classes.loop.cpid=='0'}fb {/if}l">
				<span class="fLeft">{$classes.loop._name}</span> {if classes.loop.ccidnum==0}<a title="类别商品" href="admincp.php?mid={$mid}&lid={$lid}&cid={$classes.loop.id}&act=add" class="fRig" target="adminMain"> 查看商品</a>{/if}
				</li>
			{/loop}
			</ul>
		</li>
	</ul>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb">类别属于:</div>
		<div id="hiddenselect">
			<select name="cid"  class="txInput" id="cid">
			  <option value="">....请选择</option>
			  {$loop classes}
			  <option value="{$classes.loop.id}"{if classes.loop.id==cid} selected{/if}>
			  {$classes.loop._name}</option>
			  {/loop}
			</select>
			
			&nbsp;按商品名称搜索: <input name="keyword" type="text" value="{$keyword}"> <input name="keywordsubmit" type="submit" value="搜索"> (共{$productnum}个商品)
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div class="fLeft" style="width:80px;">商品id</div>
		<div class="fLeft" style="width:250px;">品名</div>
		<div><ul id="productlist"><li style="width:100px;">编号</li><li style="width:100px;">厂编</li><li>进价</li><li>售价</li><li>特价</li><li>市场价</li><li>库存</li><li>出货量</li><li>货源</li><li>修改</li><li>删除</li></ul></div>
		<div class="cls"></div>
	</div>
	{$loop products}
	<div id="divMainBodyContent">
		<div class="fLeft" style="width:50px;">
		<input name="id[]" type="checkbox" value="{$products.loop.id}">{$products.loop.id}
		</div>
		<div class="fLeft" style="width:280px;"><a href="product.php?pid={$products.loop.id}" target="_blank">{$products.loop.name}</a></div>
		<div>
		<ul id="productlist">
		<li style="width:100px;">{$products.loop.number}</li>
		<li style="width:100px;">{$products.loop.factory_number}</li>
		<li>{$products.loop.price_mill}</li>
		<li>{$products.loop.price}</li>
		<li>{$products.loop.price_special}</li>
		<li>{$products.loop.price_market}</li>
		<li>{$products.loop.store}</li>
		<li></li>
		<li>{$products.loop.psid}</li>
	    <li><a href="admincp.php?mid={$mid}&lid={$lid}&cid={$products.loop.cid}&pid={$products.loop.id}" target="_self">修改</a></li>
		<li><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&cid={$products.loop.cid}&delpid={$products.loop.id}" target="_self">删除</a></li>
	   </ul>
		</div>
		<div class="cls"></div>
	</div>
	{/loop}
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div>
			    共{$allpage}页
				<span id="up">
				<a href="admincp.php?mid={$mid}&lid={$lid}&cid={$cid}&pn=1" target="_self">首页</a>
				</span>
				{if allpage>1}
				{$loop page}
				<span{if page.loop==pn} class="f14c fb"{/if}> 
				{if page.loop!=''}
				<a href="admincp.php?mid={$mid}&lid={$lid}&cid={$cid}&pn={$page.loop}" target="_self">{$page.loop}</a>
				{else}... {/if}
				</span>
				{/loop}
				{/if}
		</div>
		<div class="cls"></div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
		  <input name="ok" type="submit" value="  o  k  " class="txInput">
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="{$lid}">
		  <input type="hidden" name="mid" value="{$mid}">
</div>
</form>
<script laguage="javascript">classesFix();</script>
</body></html>