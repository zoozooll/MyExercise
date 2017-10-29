<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script language="javascript">
var isfilter=document.getElementsByName('isfilter[]');
var mainatr=document.getElementsByName('mainatr[]');
var i = <?php echo $this->__muant["attnum"] ?> - 0 + 1;
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
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<ul id="ClassesMenu">
		<li id="menutitle"><a href="javascript:void(0)">选择 → 商品类别属性列表</a>
			<ul id="title" class="scroll">
			<?php $classesinum = count($this->__muant["classes"]); for($classesi = 0; $classesi<$classesinum; $classesi++) { ?>
				<li class="<?php if($this->__muant["classes"]["$classesi"]["cpid"]=='0') { ?>fb <?php } ?>l">
				<span class="fLeft"><?php echo $this->__muant["classes"]["$classesi"]["_name"] ?></span> <?php if($this->__muant["classes"]["$classesi"]["ccidnum"]==0) { ?><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["classes"]["$classesi"]["id"] ?>" target="adminMain" class="fRig"><font color="#FF0000">属性管理</font></a><?php } ?>
				</li>
			<?php } ?>
			</ul>
		</li>
	</ul>
	<div class="cls"></div>
	<div class="divMsg"><?php echo $this->__muant["attclass"]["name"] ?> 的属性管理  → <font color="#FF0000">一级属性不能做为Filter 做多8个Filter</font></div>
	<div id="addclassattr">
		<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">value<?php echo $this->__muant["value"]["$valuei"]["orderid"] ?>:</div>
			<div>
			<input name="value[]" type="text" class="txInput" id="value[]" value="<?php echo $this->__muant["value"]["$valuei"]["name"] ?>" size="35">
			<input name="attid[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"> 
			<input name="isfilter[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["isfilter"] ?>"> 
			<input name="cid" type="hidden" value="<?php echo $this->__muant["cid"] ?>">
			<input name="mainatr[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["mainatr"] ?>">
			<input name="mainatrs" type="checkbox" <?php if($this->__muant["value"]["$valuei"]["mainatr"]=='1') { ?> checked<?php } ?>
			 onClick="if(this.checked==true)
			{mainatr[<?php echo $this->__muant["value"]["$valuei"]["orderid"] ?>-1].value=1}else{mainatr[<?php echo $this->__muant["value"]["$valuei"]["orderid"] ?>-1].value=0}">做为一级属性
			<input name="filters" type="checkbox" <?php if($this->__muant["value"]["$valuei"]["isfilter"]=='1') { ?> checked<?php } ?>
			 onClick="if(this.checked==true)
			 {isfilter[<?php echo $this->__muant["value"]["$valuei"]["orderid"] ?>-1].value=1}else{isfilter[<?php echo $this->__muant["value"]["$valuei"]["orderid"] ?>-1].value=0}">做为Filter筛选(按属性选择商品) 
			 <a onClick="return delYesOrNo('删除会改变商品现有属性！')" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["value"]["$valuei"]["cid"] ?>&orderid=<?php echo $this->__muant["value"]["$valuei"]["orderid"] ?>&act=delattr" target="adminMain">删除</a>
			</div>
			<div class="cls"></div>
		</div>
		<?php } ?>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div><?php if($this->__muant["cid"]>0) { ?><div id="addattr"><a href="#" onClick="addValueDiv();parent.SetWinHeight(parent.document.adminMain);return false;">增加属性</a></div> 
		<?php } ?></div>
		<div class="cls"></div>
	</div>
	<div class="cls"></div>
</div>
<div id="divMainBodyContentSumbit">
	<input name="ok" type="submit" value="  o  k  " class="txInput">&nbsp;
	<input name="reset" type="reset" value="reset" class="txInput">
	<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
	<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
	<input type="hidden" name="cid" value="<?php echo $this->__muant["cid"] ?>">
</div>
</form>
</body></html>