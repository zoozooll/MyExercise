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
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div id="hiddenselect">
			标题: <input name="keyword" type="text"> <input name="keywordsubmit" type="submit" value="搜索">
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
<table width="90%" border="1">
  <tr>
    <td>新闻id</td>
    <td>标题</td>
    <td>添加日期</td>
    <td>作者</td>
    <td>来源</td>
    <td>修改</td>
    <td>删除</td>
  </tr>
{$loop news}
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type="checkbox" value="{$news.loop.id}">{$news.loop.id}</td>
    <td><a href="news.php?id={$news.loop.id}" target="_blank">{$news.loop.title}</a></td>
    <td>{$news.loop.add_date}&nbsp;</td>
    <td>{$news.loop.author}&nbsp;</td>
    <td>{$news.loop.source_from}&nbsp;</td>
    <td><a href="admincp.php?mid={$mid}&lid={$lid}&nid={$news.loop.id}&modify=yes" target="_self">修改</a>&nbsp;</td>
    <td><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&delnid={$news.loop.id}" target="_self">删除</a></td>
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
		  <input name="ok" type="submit" value="  o  k  " class="txInput">
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="{$lid}">
		  <input type="hidden" name="mid" value="{$mid}">
</div>
</form>
</body></html>