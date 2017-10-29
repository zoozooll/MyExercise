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
			会员id: <input name="uid" type="text" size="4"> 
			会员名: <input name="keyword" type="text"> <input name="keywordsubmit" type="submit" value="搜索">
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
{if act=='edit'}{$loop user}
  <table width="90%" border="1">
    <tr>
      <td width="9%" scope="row">姓名</thd>
      <td width="91%"><input name="name" value="{$user.loop.name}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">Email</td>
      <td><input name="email" value="{$user.loop.email}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">联系电话</td>
      <td><input name="phone" value="{$user.loop.phone}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">移动电话</td>
      <td><input name="mobile" value="{$user.loop.mobile}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">邮寄地址</td>
      <td><input name="address" value="{$user.loop.address}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">邮编</td>
      <td><input name="postcode" value="{$user.loop.postcode}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">性别</td>
      <td><input name="sex" value="{$user.loop.sex}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">生日</td>
      <td><input name="birthday" value="{$user.loop.birthday}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">QQ</td>
      <td><input name="qq" value="{$user.loop.qq}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">MSN</td>
      <td><input name="msn" value="{$user.loop.msn}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">taobao旺旺</td>
      <td><input name="taobao" value="{$user.loop.taobao}" type="text"></td>
    </tr>
    <tr>
      <td scope="row">Skype</td>
      <td><input name="skype" value="{$user.loop.skype}" type="text"></td>
    </tr>
  </table>{/loop}
{else}
<table width="90%" >
  <tr>
    <td>id</td>
    <td>姓名</td>
    <td>Email</td>
    <td>联系电话</td>
    <td>移动电话</td>
    <td>邮寄地址</td>
    <td>邮编</td>
    <td>性别</td>
    <td>生日</td>
    <td>QQ</td>
    <td>MSN</td>
    <td>taobao旺旺</td>
    <td>Skype</td>
    <td>修改</td>
    <td>删除</td>
  </tr>
{$loop user}
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type='hidden' value="{$user.loop.id}">{$user.loop.id}</td>
    <td>{$user.loop.name}&nbsp;</td>
    <td>{$user.loop.email}&nbsp;</td>
    <td>{$user.loop.phone}&nbsp;</td>
    <td>{$user.loop.mobile}&nbsp;</td>
    <td>{$user.loop.address}&nbsp;</td>
    <td>{$user.loop.postcode}&nbsp;</td>
    <td>{$user.loop.sex}&nbsp;</td>
    <td>{$user.loop.birthday}&nbsp;</td>
    <td>{$user.loop.qq}&nbsp;</td>
    <td>{$user.loop.msn}&nbsp;</td>
    <td>{$user.loop.taobao}&nbsp;</td>
    <td>{$user.loop.skype}&nbsp;</td>
    <td><a href="admincp.php?mid={$mid}&lid={$lid}&uid={$user.loop.id}&act=edit" target="_self">修改</a></td>
    <td><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&deluid={$user.loop.id}" target="_self">删除</a></td>
  </tr>
{/loop}
</table>
{/if}
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