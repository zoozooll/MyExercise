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
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&bid="+val;
}
</script>
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 商品品牌:</div>
	    <div id="hiddenselect">   <select name="bid"  class="txInput" id="bid" onChange="MP_jumpMenu(this.value)">
				  <option value="">....请选择</option>
				  <?php $brandinum = count($this->__muant["brand"]); for($brandi = 0; $brandi<$brandinum; $brandi++) { ?>
				  <option value="<?php echo $this->__muant["brand"]["$brandi"]["id"] ?>"<?php if($this->__muant["brand"]["$brandi"]["id"]==$this->__muant["bid"]) { ?> selected<?php } ?>>
				  <?php echo $this->__muant["brand"]["$brandi"]["name"] ?></option>
				  <?php } ?>
				</select>
		</div>
		<div class="cls"></div>
    </div>
	<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 商品品牌系列:</div>
	    <div></div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 系列名称:</div>
	    <div>中文：<input name="name" type="text"> 英文：<input name="enname" type="text"> </div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div><input name="add" type="submit" class="txInput" value="添加" onClick="this.value='yes';"> <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
		  <input type="hidden" name="bid" value="<?php echo $this->__muant["bid"] ?>">
		</div>
		<div class="cls"></div>
    </div>
	</form>
	<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">所有系列</div>
	    <div></div>
		<div class="cls"></div>
    </div>
	<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div> 中文：<input name="name[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["name"] ?>"> 英文：<input name="enname[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["enname"] ?>"> 
				   <input name="oid[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>">
				   <input name="id[]" type="checkbox" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"> 修改 <a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&delsid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>&act=del&bid=<?php echo $this->__muant["bid"] ?>" target="_self">删除</a>
		</div>
		<div class="cls"></div>
    </div>
	<?php } ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">所有系列</div>
	    <div><input name="modify" type="submit" value="modify" class="txInput">&nbsp;
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
		  <input type="hidden" name="bid" value="<?php echo $this->__muant["bid"] ?>">
        </div>
		<div class="cls"></div>
    </div>
	</form>
</div>	
<div id="divMainBodyContentSumbit"></div>
</body></html>