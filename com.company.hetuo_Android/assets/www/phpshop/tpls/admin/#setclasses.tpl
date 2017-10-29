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
<script language="JavaScript" src="./jscript/comm/simpleajax.js"></script>
<script language="javascript">
function checkData() {
	if(document.form1.name.value == '') {
		alert('类别名称不能为空！');
		document.form1.name.focus;
		return false;
	}
	if(document.form1.enname.value != '') {
		//alert('类别英文名不能为空！');
		//document.form1.enname.focus;
		//return false;
		var patrn=/^[0-9a-z]{1,100}$/i; 
		if (!patrn.exec(document.form1.enname.value)) {
			alert('类别英文名只能是数字和字母！');
			document.form1.enname.focus;
			return false;
		}
	}
}
function sort_sub_class(id, val) {
	var str_value = '&scid='+id+'&sortval='+val;
	callLoad('divClasses');
	callServer('admin/adminajaxset.php?switch=sortclass&mid={$mid}&lid={$lid}&t='+Math.random()+str_value, 'divClasses');
}
function sort_class() {
	var sort_id = document.getElementsByName('sort_id[]');
	var sort_class = document.getElementsByName('sort_class[]');
	var arrSortId = new Array();
	var arrSortClass = new Array();
	for(i = 0; i < sort_id.length; i++) {
		arrSortId[i] = sort_id[i].value;
		arrSortClass[i] = sort_class[i].value;
	}
	var str_value = 'sortid='+arrSortId+'&sortclass='+arrSortClass;
	callLoad('divClasses');
	postServer('admin/adminajaxset.php?switch=sortclass&lid={$lid}&t='+Math.random(), str_value, 'divClasses');
}
</script>
</head>
<body style="overflow-x:hidden;">
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain" onSubmit="return checkData();">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>{$fun_name}</div>
</div>
<div id="BodyContent">
	<div id="divClasses" class="fLeft">
	{$loop value}
		<div>
			<div class="fLeft{if value.loop.cpid==0} fb{/if}" id="classes">
			<a>{$value.loop._name}</a>
			</div>
			<div id="sortid" class="fLeft">
			<a title="修改此类别" href="admincp.php?mid={$mid}&lid={$lid}&cid={$value.loop.id}" target="adminMain">修改</a>
            <a title="移动此类别" href="admincp.php?mid={$mid}&lid={$lid}&cid={$value.loop.id}&mv=1" target="adminMain">移动</a> 
			{if value.loop.ccidnum==0}
			<a onClick="return delYesOrNo();" title="删除" href="admincp.php?mid={$mid}&lid={$lid}&cid={$value.loop.id}&act=del" target="adminMain">删除</a>{else}<font color="#CCCCCC">删除</font>
			{/if}
			</div>
			<div>
				{if value.loop.cpid>'0'}
				{if value.loop.up=='1'}<a href="#" onClick="sort_sub_class('{$value.loop.id}','up')" title="同级上移">↑</a>{/if}
				{if value.loop.down=='1'}<a href="#" onClick="sort_sub_class('{$value.loop.id}','down')" title="同级下移">↓</a>{/if}
				{else}
				<input name="sort_id[]" type="hidden" value="{$value.loop.id}">
				<input name="sort_class[]" type="text" size="1" maxlength="3" class="txInput" value="{$value.loop.sort}">
				{/if}
			</div>
		{if value.next.cpid==0}
		{/if}
		</div>
		<div class="cls"></div>
	{/loop}
		<div id="classes" class="fLeft">&nbsp;</div><div id="sortid" class="fLeft">&nbsp;</div>
		<div class="fLeft"><input onClick="sort_class()" name="sortclass" type="button" class="txInput" value="排序"></div>
		<div class="cls"></div>
	</div>
	<div class="fLeft" style="width:50%;">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">{if cid>0}{if mv==''}<a href="javascript:void(0);" onClick="newClasses();">新建类别</a>{/if}{/if}　　类别属于:</div>
			<div id="hiddenselect">{if mv!=''}"{$rsname.name}" 移动到:{/if}
                  <span id="add_cid" style="{if cid>0}{if mv==''}display:none;{/if}{/if}">
                  <select name="classesinfo"  class="txInput" id="classesinfo" onChange="countTotalElement(this.value);">
                  {if mv!=''}<option value="mv">--选择移到--</option>{/if}
                  <option value="0">{if mv==''}新类别{else}顶级类别{/if}</option>
				  {$loop classes}{if mv==classes.loop.cpid}{else}{if mv==classes.loop.id}{else}
				  <option value="{$classes.loop.id}"{if classes.loop.id==cpid} selected{/if}{if classes.loop.id==cid} disabled{/if}>
				  {$classes.loop._name}</option>{/if}{/if}
				  {/loop}
				  </select>
                  </span>
				<!--<input name="modifyclass" type="checkbox" value="1">移动类别-->
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"></div>
			<div>{if mv==''}类别名称:　<input name="name" type="text" class="txInput" id="name" value="{$rsname.name}" >
			　　　英文名:　<input name="enname" type="text" class="txInput" id="enname" value="{$rsname.enname}" >{/if}
                 
			</div>
			<div class="cls"></div>
		</div>
		<!--
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别图片:</div>
			<div><input name="picurl" type="text" class="txInput" id="picurl" value="{$rsname.picurl}" size="35"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别描述:</div>
			<div> <textarea name="des" cols="35" rows="2" class="txInput" id="des">{$rsname.des}</textarea></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别URl地址(可指向外部):</div>
			<div><input name="url" type="text" class="txInput" id="url" value="{$rsname.url}" size="35"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别公告:</div>
			<div><textarea name="affiche" cols="35" rows="2" class="txInput" id="affiche">{$rsname.affiche}</textarea></div>
		</div>
		-->
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"></div>
			<div>
				<input name="ok" type="submit" value="  o  k  " class="txInput">
				<input name="reset" type="reset" value="reset" class="txInput">
				<input type="hidden" name="lid" value="{$lid}">
				<input type="hidden" name="mid" value="{$mid}">
				<input type="hidden" name="cid" value="{$cid}">
                <input type="hidden" name="ismv" value="{$mv}">                
			</div>
		</div>
	</div>
	<div class="cls"></div>
</div>
</form>
<script language="javascript">
function countTotalElement(cid, node) { 
	if(cid == 0) {
		document.getElementById('name').value = '';
		document.getElementById('enname').value = '';
		document.form1.cid.value='';
	}
	if(node) {
		var total = 0;
		if(node.nodeType == 1) { 
			if((node.tagName == 'INPUT' || node.tagName == 'TEXTAREA') && node.id != '') {
				total++;			
				//node.setAttribute("value","");
			}
		}
		var childrens = node.childNodes;		
		for(var i=0;i<childrens.length;i++) {
			if(childrens[i].disabled == true) {childrens[i].disabled = false;}
			total += countTotalElement(cid, childrens[i]); 
		}
	}
}
function newClasses() {
	document.getElementById('add_cid').style.display='';
	countTotalElement(0, document.getElementById('classesinfo'));	
}
</script>
</body></html>
