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
		<div id="divMainBodyContentTitle"> 配送方式名称:</div>
	    <div><input name="name" type="text" class="txInput"> 
		价格:<input name="freight" type="text" class="txInput" size="8"> 
		免运费:购物<input name="shop_price" type="text" class="txInput" size="5">
		元以上</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 配送方式描述:</div>
	    <div>
	      <textarea name="describes" class="txInput"></textarea> 
      </div>
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
		<div id="divMainBodyContentTitle">所有配送方式</div>
	    <div></div>
		<div class="cls"></div>
    </div>
	<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 配送方式名称:</div>
	    <div><input name="name[]" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["name"] ?>" class="txInput"> 
		价格：<input name="freight[]" type="text" class="txInput" value="<?php echo $this->__muant["value"]["$valuei"]["freight"] ?>" size="8"> 
		免运费:购物<input name="shop_price[]" type="text" class="txInput" value="<?php echo $this->__muant["value"]["$valuei"]["shop_price"] ?>" size="5">
		元以上
		    <input name="oid[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>">
		    <input name="id[]" type="checkbox" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"> 修改 <a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&act=del&fid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>" target="adminMain">删除</a>
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 配送方式描述:</div>
	    <div>
	      <textarea name="describes[]" class="txInput"><?php echo $this->__muant["value"]["$valuei"]["describes"] ?></textarea> 
      </div>
		<div class="cls"></div>
    </div>
	<?php } ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div><input name="modify" type="submit" value="modify" class="txInput">&nbsp;
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
        </div>
		<div class="cls"></div>
    </div>
	</form>
</div>	
<div id="divMainBodyContentSumbit"></div>
</body></html>