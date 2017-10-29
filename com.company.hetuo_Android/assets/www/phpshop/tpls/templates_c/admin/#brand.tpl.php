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
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 品牌名称:</div>
	    <div>中文：<input name="name" type="text"> 英文：<input name="enname" type="text"></div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div><input name="add" type="submit" class="txInput" value="添加" onClick="this.value='yes';"> <input name="reset" type="reset" value="reset" class="txInput">
		<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
		</div>
		<div class="cls"></div>
    </div>
	</form>
	<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 所有品牌:</div>
	    <div></div>
		<div class="cls"></div>
    </div>
	<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div>中文：<input name="name[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["name"] ?>"> 英文：<input name="enname[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["enname"] ?>">
				   <input name="oid[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>">
				   <input name="id[]" type="checkbox" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"> 修改 <a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&delbid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>&act=del" target="_self">删除</a></div>
		<div class="cls"></div>
    </div>
	<?php } ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div><input name="modify" type="submit" value="modify" class="txInput">
		<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
		</div>
		<div class="cls"></div>
    </div>
	</form>
</div>
<div id="divMainBodyContentSumbit">
</div>
</body></html>