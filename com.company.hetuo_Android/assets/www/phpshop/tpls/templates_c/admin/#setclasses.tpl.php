<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
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
	callServer('admin/adminajaxset.php?switch=sortclass&mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&t='+Math.random()+str_value, 'divClasses');
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
	postServer('admin/adminajaxset.php?switch=sortclass&lid=<?php echo $this->__muant["lid"] ?>&t='+Math.random(), str_value, 'divClasses');
}
</script>
</head>
<body style="overflow-x:hidden;">
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain" onSubmit="return checkData();">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divClasses" class="fLeft">
	<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
		<div>
			<div class="fLeft<?php if($this->__muant["value"]["$valuei"]["cpid"]==0) { ?> fb<?php } ?>" id="classes">
			<a><?php echo $this->__muant["value"]["$valuei"]["_name"] ?></a>
			</div>
			<div id="sortid" class="fLeft">
			<a title="修改此类别" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>" target="adminMain">修改</a>
            <a title="移动此类别" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>&mv=1" target="adminMain">移动</a> 
			<?php if($this->__muant["value"]["$valuei"]["ccidnum"]==0) { ?>
			<a onClick="return delYesOrNo();" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>&act=del" target="adminMain">删除</a><?php } else { ?><font color="#CCCCCC">删除</font>
			<?php } ?>
			</div>
			<div>
				<?php if($this->__muant["value"]["$valuei"]["cpid"]>'0') { ?>
				<?php if($this->__muant["value"]["$valuei"]["up"]=='1') { ?><a href="#" onClick="sort_sub_class('<?php echo $this->__muant["value"]["$valuei"]["id"] ?>','up')" title="同级上移">↑</a><?php } ?>
				<?php if($this->__muant["value"]["$valuei"]["down"]=='1') { ?><a href="#" onClick="sort_sub_class('<?php echo $this->__muant["value"]["$valuei"]["id"] ?>','down')" title="同级下移">↓</a><?php } ?>
				<?php } else { ?>
				<input name="sort_id[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>">
				<input name="sort_class[]" type="text" size="1" maxlength="3" class="txInput" value="<?php echo $this->__muant["value"]["$valuei"]["sort"] ?>">
				<?php } ?>
			</div>
		<?php if($this->__muant["value"]["$valuei"+1]["cpid"]==0) { ?>
		<?php } ?>
		</div>
		<div class="cls"></div>
	<?php } ?>
		<div id="classes" class="fLeft">&nbsp;</div><div id="sortid" class="fLeft">&nbsp;</div>
		<div class="fLeft"><input onClick="sort_class()" name="sortclass" type="button" class="txInput" value="排序"></div>
		<div class="cls"></div>
	</div>
	<div class="fLeft" style="width:50%;">
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"><?php if($this->__muant["cid"]>0) { ?><?php if($this->__muant["mv"]=='') { ?><a href="javascript:void(0);" onClick="newClasses();">新建类别</a><?php } ?><?php } ?>　　类别属于:</div>
			<div id="hiddenselect"><?php if($this->__muant["mv"]!='') { ?>"<?php echo $this->__muant["rsname"]["name"] ?>" 移动到:<?php } ?>
                  <span id="add_cid" style="<?php if($this->__muant["cid"]>0) { ?><?php if($this->__muant["mv"]=='') { ?>display:none;<?php } ?><?php } ?>">
                  <select name="classesinfo"  class="txInput" id="classesinfo" onChange="countTotalElement(this.value);">
                  <?php if($this->__muant["mv"]!='') { ?><option value="mv">--选择移到--</option><?php } ?>
                  <option value="0"><?php if($this->__muant["mv"]=='') { ?>新类别<?php } else { ?>顶级类别<?php } ?></option>
				  <?php $classesinum = count($this->__muant["classes"]); for($classesi = 0; $classesi<$classesinum; $classesi++) { ?><?php if($this->__muant["mv"]==$this->__muant["classes"]["$classesi"]["cpid"]) { ?><?php } else { ?><?php if($this->__muant["mv"]==$this->__muant["classes"]["$classesi"]["id"]) { ?><?php } else { ?>
				  <option value="<?php echo $this->__muant["classes"]["$classesi"]["id"] ?>"<?php if($this->__muant["classes"]["$classesi"]["id"]==$this->__muant["cpid"]) { ?> selected<?php } ?><?php if($this->__muant["classes"]["$classesi"]["id"]==$this->__muant["cid"]) { ?> disabled<?php } ?>>
				  <?php echo $this->__muant["classes"]["$classesi"]["_name"] ?></option><?php } ?><?php } ?>
				  <?php } ?>
				  </select>
                  </span>
				<!--<input name="modifyclass" type="checkbox" value="1">移动类别-->
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"></div>
			<div><?php if($this->__muant["mv"]=='') { ?>类别名称:　<input name="name" type="text" class="txInput" id="name" value="<?php echo $this->__muant["rsname"]["name"] ?>" >
			　　　英文名:　<input name="enname" type="text" class="txInput" id="enname" value="<?php echo $this->__muant["rsname"]["enname"] ?>" ><?php } ?>
                 
			</div>
			<div class="cls"></div>
		</div>
		<!--
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别图片:</div>
			<div><input name="picurl" type="text" class="txInput" id="picurl" value="<?php echo $this->__muant["rsname"]["picurl"] ?>" size="35"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别描述:</div>
			<div> <textarea name="des" cols="35" rows="2" class="txInput" id="des"><?php echo $this->__muant["rsname"]["des"] ?></textarea></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别URl地址(可指向外部):</div>
			<div><input name="url" type="text" class="txInput" id="url" value="<?php echo $this->__muant["rsname"]["url"] ?>" size="35"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">类别公告:</div>
			<div><textarea name="affiche" cols="35" rows="2" class="txInput" id="affiche"><?php echo $this->__muant["rsname"]["affiche"] ?></textarea></div>
		</div>
		-->
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"></div>
			<div>
				<input name="ok" type="submit" value="  o  k  " class="txInput">
				<input name="reset" type="reset" value="reset" class="txInput">
				<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
				<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
				<input type="hidden" name="cid" value="<?php echo $this->__muant["cid"] ?>">
                <input type="hidden" name="ismv" value="<?php echo $this->__muant["mv"] ?>">                
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
