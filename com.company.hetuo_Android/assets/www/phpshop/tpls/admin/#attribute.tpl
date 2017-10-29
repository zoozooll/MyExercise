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
var isfilter=document.getElementsByName('isfilter[]');
var mainatr=document.getElementsByName('mainatr[]');
var i = {$attnum} - 0 + 1;
function addValueDiv() {
	var html = '<div id="divMainBodyContent">'
				+'<div id="divMainBodyContentTitle"> value'+i+':</div>'
				+'<div>'
				+'  <input name="value[]" type="text" class="txInput" id="value[]" value="" size="35">'
				+'  <input name="mainatr[]" type="hidden" value="0">'
				+'  <input name="mainatrs" type="checkbox" onClick="if(this.checked==true)'
				+'  {mainatr['+(i-1)+'].value=1}else{mainatr['+(i-1)+'].value=0}">做为一级属性'
				+'	<input name="filters" type="checkbox"'
				+'	 onClick="if(this.checked==true)'
				+'	 {isfilter['+(i-1)+'].value=1}else{isfilter['+(i-1)+'].value=0}"> '
				+'	<input name="attid[]" type="hidden" value=""> '
				+'	<input name="isfilter[]" type="hidden" value="0">做为筛选 '
				+'</div>'
				+'<div class="cls"></div></div>';
	var div = document.createElement("div"); 
	div.innerHTML = html;
	var parNode = document.getElementById("addclassattr");
	parNode.appendChild(div);
	i++;
}
function chechData() {
	var k = 0;
	var isfilter=document.getElementsByName('isfilter[]');
	var mainatr=document.getElementsByName('mainatr[]');
	for(i = 0; i<isfilter.length; i++) {
		if(isfilter[i].value == 1) k++;
		if(k > 8) {
			alert('做为Filter的属性超过8个，请减少做为Filter的个数！');
			return false;
		}
		if(isfilter[i].value == 1 && mainatr[i].value == 1) {
			alert('一级属性不能做为Filter！');
			return false;
		}
	}
	if(mainatr[0].value != 1) {
		alert('第一个属性必须做为一级属性！');
		return false;
	}
}
window.onload=classesFix;
</script>
</head>
<body>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain" onSubmit="return chechData()">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">
	<ul id="ClassesMenu">
		<li id="menutitle"><a href="javascript:void(0)">选择 → 商品类别属性列表</a>
			<ul id="title" class="scroll">
			{$loop classes}
				<li class="{if classes.loop.cpid=='0'}fb {/if}l">
				<span class="fLeft">{$classes.loop._name}</span> {if classes.loop.ccidnum==0}<a href="admincp.php?mid={$mid}&lid={$lid}&cid={$classes.loop.id}" target="adminMain" class="fRig"><font color="#FF0000">属性管理</font></a>{/if}
				</li>
			{/loop}
			</ul>
		</li>
	</ul>
	<div class="cls"></div>
	<div class="divMsg">{$attclass.name} 的属性管理  → <font color="#FF0000">一级属性不能做为Filter 做多8个Filter</font></div>
	<div id="addclassattr">
		{$loop value}
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">value{$value.loop.orderid}:</div>
			<div>
			<input name="value[]" type="text" class="txInput" id="value[]" value="{$value.loop.name}" size="35">
			<input name="attid[]" type="hidden" value="{$value.loop.id}"> 
			<input name="isfilter[]" type="hidden" value="{$value.loop.isfilter}"> 
			<input name="cid" type="hidden" value="{$cid}">
			<input name="mainatr[]" type="hidden" value="{$value.loop.mainatr}">
			<input name="mainatrs" type="checkbox" {if value.loop.mainatr=='1'} checked{/if}
			 onClick="if(this.checked==true)
			{mainatr[{$value.loop.orderid}-1].value=1}else{mainatr[{$value.loop.orderid}-1].value=0}">做为一级属性
			<input name="filters" type="checkbox" {if value.loop.isfilter=='1'} checked{/if}
			 onClick="if(this.checked==true)
			 {isfilter[{$value.loop.orderid}-1].value=1}else{isfilter[{$value.loop.orderid}-1].value=0}">做为Filter筛选(按属性选择商品) 
			 <a onClick="return delYesOrNo('删除会改变商品现有属性！')" href="admincp.php?mid={$mid}&lid={$lid}&cid={$value.loop.cid}&orderid={$value.loop.orderid}&act=delattr" target="adminMain">删除</a>
			</div>
			<div class="cls"></div>
		</div>
		{/loop}
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div>{if cid>0}<div id="addattr"><a href="#" onClick="addValueDiv();parent.SetWinHeight(parent.document.adminMain);return false;">增加属性</a></div> 
		{/if}</div>
		<div class="cls"></div>
	</div>
	<div class="cls"></div>
</div>
<div id="divMainBodyContentSumbit">
	<input name="ok" type="submit" value="  o  k  " class="txInput">&nbsp;
	<input name="reset" type="reset" value="reset" class="txInput">
	<input type="hidden" name="lid" value="{$lid}">
	<input type="hidden" name="mid" value="{$mid}">
	<input type="hidden" name="cid" value="{$cid}">
</div>
</form>
</body></html>