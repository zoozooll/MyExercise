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
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div>
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家名称:</div>
		<div><input name="name" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家联系电话:</div>
		<div><input name="phone" class="txInput" type="text"> 移动电话:<input name="mobile" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家联系Email:</div>
		<div><input name="email" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">货源商家联系地址:</div>
		<div><input name="address" class="txInput" type="text"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">描述:</div>
		<div>
		  <textarea name="describes" class="txInput"></textarea>
		</div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
<input name="add" type="submit" class="txInput" value="添加" onClick="this.value='yes';">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
<div id="divMainBodyContent">
	<div id="divMainBodyContentTitle">
	所有货源商家:
	</div>
	<div></div>
	<div class="cls"></div>
</div>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
		<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 
			货源商家名称:
			</div>
			<div><input name="name[]" type="text" class="txInput" value="<?php echo $this->__muant["value"]["$valuei"]["name"] ?>"> 
			
			
			    
				<input name="oid[]" type="hidden" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>">
				<input name="id[]" type="checkbox" value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"> 修改  <a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&act=del&sid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>" target="adminMain">删除</a>
			</div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">
			货源商家联系电话:
			</div>
			<div><input name="phone[]" class="txInput" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["phone"] ?>"> 移动电话:<input name="mobile[]" type="text" class="txInput" value="<?php echo $this->__muant["value"]["$valuei"]["mobile"] ?>"> </div>
			<div class="cls"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">货源商家联系Email:</div>
			<div><input name="email[]" class="txInput" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["email"] ?>"></div>
		</div>
		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle">货源商家联系地址:</div>
			<div><input name="address[]" class="txInput" type="text" value="<?php echo $this->__muant["value"]["$valuei"]["address"] ?>"></div>
		</div>

		<div id="divMainBodyContent">
			<div id="divMainBodyContentTitle"> 描述:</div>
			<div>
			  <textarea name="describes[]" class="txInput"><?php echo $this->__muant["value"]["$valuei"]["describes"] ?></textarea> 
		  </div>
			<div class="cls"></div>
		</div>
		<?php } ?>
<div id="divMainBodyContentSumbit">
<input name="modify" type="submit" value="modify" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
</body></html>