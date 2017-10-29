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
<script language="javascript">
function addAdmin() {
	document.getElementById('add_admin').style.display = 'block';
	document.getElementById('show_admin').style.display = 'none';
	document.form1.addadmin.checked = true;
}
function setAddAdmin(obj) {
	if(obj.checked == true) {
		document.getElementById('add_admin').style.display = 'block';
		document.getElementById('show_admin').style.display = 'none';
	} else {
		document.getElementById('add_admin').style.display = 'none';
		document.getElementById('show_admin').style.display = 'block';
	}
}
</script>
</head>
<body>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div id="hiddenselect">
			{if aid==''}<a onClick="addAdmin();return false;" href="admin#add">增加新管理员</a><input name="addadmin" type="checkbox" onClick="setAddAdmin(this)" value="1" class="txInput" >{/if} 用户名: <input name="keyword" type="text"> <input name="keywordsubmit" type="submit" onClick="document.form1.action='admincp.php'" value="搜索" class="txInput" >
		</div>
		<div class="cls"></div>
	</div>
<div id="show_admin">
{if aid==''}
	<div id="divMainBodyContent">
<table width="90%" border="1">
  <tr>
    <td>id</td>
    <td>用户名</td>
    <td>email</td>
    <td>添加时间</td>
    <td>登录IP</td>
    <td>登录时间</td>
    <td>修改</td>
    <td>删除</td>
  </tr>
{$loop admin}
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type="checkbox" value="{$admin.loop.id}" class="txInput" ></td>
    <td>{$admin.loop.name}</td>
    <td>{$admin.loop.email}&nbsp;</td>
    <td>{$admin.loop.add_date}&nbsp;</td>
    <td>{$admin.loop.lastloginip}&nbsp;</td>
    <td>{$admin.loop.lastlogintime}&nbsp;</td>
    <td>{if admin.loop.id!=1}{if admin.loop.id!=loginaid}<a href="admincp.php?mid={$mid}&lid={$lid}&nid={$admin.loop.id}&modify=yes&aid={$admin.loop.id}" target="_self">修改</a>{/if}{/if}&nbsp;</td>
    <td>{if admin.loop.id!=1}{if admin.loop.id!=loginaid}<a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&delaid={$admin.loop.id}" target="_self">删除</a>{/if}{/if}&nbsp;</td>
  </tr>
{/loop}
</table>
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
{/if}
</div>
<div id="add_admin"{if aid==''} style="display:none"{/if}>
	{if aid!=''}<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 修改用户:<b>{$admin.name}</b> <input name="cache" value="取消修改" type="button" onClick="location.href='admincp.php?mid={$mid}&lid={$lid}'" class="txInput" ></div>
		<div class="cls"></div><input name="addadmin" type="hidden" value="1">
	</div>{/if}
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 用户名:</div>
		<div><input name="name" type="text" class="txInput" id="name" value="{$admin.name}"{if aid!=''} readonly{/if} >
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Email:</div>
	    <div><input name="email" type="text" class="txInput" id="email" value="{$admin.email}"{if aid!=''} readonly{/if} ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">密码:</div>
		<div><input name="password" type="password" class="txInput" id="password" value="" > 
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">再输入一次密码:</div>
		<div><input name="pass" type="password" class="txInput" id="pass" value="" ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">管理员权限设置:</div>
		<div class="fLeft">{$loop adminsetinfo}{if adminsetinfo.loop.orderid==0}<br /><b>{/if}{$adminsetinfo.loop.name}{if adminsetinfo.loop.orderid=='0'}</b>{/if}<input name="power[]" type="checkbox" value="{$adminsetinfo.loop.id}"{if adminsetinfo.loop.id==adminsetinfo.loop.fid} checked{/if}>{/loop}<br /><br /></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContentSumbit">
			  <input type="hidden" name="aid" value="{$aid}">
			  <input name="ok" type="submit" value="  o  k  " class="txInput">
			  <input name="reset" type="reset" value="reset" class="txInput">
			  <input type="hidden" name="lid" value="{$lid}">
			  <input type="hidden" name="mid" value="{$mid}">
	</div>
</div>
</div>
</form>
</body></html>