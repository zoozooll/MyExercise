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
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<!-- 
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">启用 Archiver:</div>
		<div>是<input type="radio" name="value1" value="1"<?php if($this->__muant["value"]["value1"]==1) { ?> checked<?php } ?>>
			 否<input type="radio" name="value1" value="0"<?php if($this->__muant["value"]["value1"]==0) { ?> checked<?php } ?>>
		</div>
	</div>
	-->
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">标题附加字:</div>
		<div><input name="value2" type="text" class="txInput" id="value2" value="<?php echo $this->__muant["value"]["value2"] ?>" size="35"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Meta Keywords:</div>
		<div><input name="value3" type="text" class="txInput" id="value3" value="<?php echo $this->__muant["value"]["value3"] ?>" size="35"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Meta Description:</div>
		<div><input name="value4" type="text" class="txInput" id="value4" value="<?php echo $this->__muant["value"]["value4"] ?>" size="35"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">其他头部信息:</div>
		<div><input name="value5" type="text" class="txInput" id="value5" value="<?php echo $this->__muant["value"]["value5"] ?>" ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">静态页面:</div>
		<div><input name="value6" type="radio" value="0"<?php if($this->__muant["value"]["value6"]==0) { ?> checked<?php } ?>>不做任何静态处理<input name="value6" type="radio" value="1"<?php if($this->__muant["value"]["value6"]==1) { ?> checked<?php } ?>>仿静态处理<input name="value6" type="radio" value="2"<?php if($this->__muant["value"]["value6"]==2) { ?> checked<?php } ?>>静态处理(需服务器支持)</div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
</body></html>