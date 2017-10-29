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
function setPic(val) {
alert('上传成功！');
}
</script>
</head>
<body>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">为商品图片打上水印:</div>
		<div class="fLeft">
		<input type="radio" name="value1" value="-1"<?php if($this->__muant["value"]["value1"]=='-1') { ?> checked<?php } ?>>不打<br />
		<input type="radio" name="value1" value="0"<?php if($this->__muant["value"]["value1"]=='0') { ?> checked<?php } ?>>水印随机
		<input type="radio" name="value1" value="5"<?php if($this->__muant["value"]["value1"]=='5') { ?> checked<?php } ?>>水印中部居中<br />
		<input type="radio" name="value1" value="1"<?php if($this->__muant["value"]["value1"]=='1') { ?> checked<?php } ?>>水印顶端居左 
		<input type="radio" name="value1" value="2"<?php if($this->__muant["value"]["value1"]=='2') { ?> checked<?php } ?>>水印顶端居中 
		<input type="radio" name="value1" value="3"<?php if($this->__muant["value"]["value1"]=='3') { ?> checked<?php } ?>>水印顶端居右<br>
		<input type="radio" name="value1" value="7"<?php if($this->__muant["value"]["value1"]=='7') { ?> checked<?php } ?>>水印底端居左 
		<input type="radio" name="value1" value="8"<?php if($this->__muant["value"]["value1"]=='8') { ?> checked<?php } ?>>水印底端居中 
		<input type="radio" name="value1" value="9"<?php if($this->__muant["value"]["value1"]=='9') { ?> checked<?php } ?>>水印底端居右 
		
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">水印图片(png格式 200X200px):</div>
	  <div class="fLeft">&nbsp;<!--input name="value2" type="text" class="txInput" id="value2" value="<?php echo $this->__muant["value"]["value2"] ?>" -->
		</div>
		<div><iframe src="./admin/adminajaxset.php?switch=uploadwaterimg&lid=<?php echo $this->__muant["lid"] ?>" width="380" frameborder="0" height="25" allowTransparency='true' name="uploadpic"></iframe></div>
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