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
	location.href = "admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid="+val;
	// onChange="MP_jumpMenu(this.value)"
}
function setPic(val) {
	document.getElementById('upload_pic').innerHTML = '<a href="'+val+'" target="_blank"><img width="100" src="'+val+'"> 点击看原图图</a>';
	document.getElementById('upload_pic_path').value = val;
}
</script>
</head>
<body>
<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1" onSubmit="return checkData();">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">类别:</div>
	    <div class="fLeft" id="hiddenselect">
		<select class="txInput" name="cid" id="cid" onChange="MP_jumpMenu(this.value)">
		  <option value="">....请选择</option>
		  <option value="1000000000"<?php if($this->__muant["cid"]=='1000000000') { ?> selected<?php } ?>>首页广告</option>
		  <?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
		  <option value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"<?php if($this->__muant["value"]["$valuei"]["id"]==$this->__muant["cid"]) { ?> selected<?php } ?>>
		  <?php echo $this->__muant["value"]["$valuei"]["_name"] ?></option>
		  <?php } ?>
		</select>
		</div>
		<div class="cls"></div>
    </div>
	<?php if($this->__muant["cid"]!='') { ?>
    <div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
	    <div class="fLeft">例：填图片地址|填连接地址|填文字<br>http://wwww.example.com/product/news/example.gif|http://wwww.example.com/ploduct/1.html|文字</textarea>
		</div>
		<div class="fLeft"></div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">广告1<?php if($this->__muant["cid"]==1000000000) { ?>（首页flash焦点广告）<?php } ?><?php if($this->__muant["cid"]<1000000000) { ?><?php if($this->__muant["classinfo"]["orderid"]==0) { ?>（导航下面横幅广告）<?php } else { ?>（右侧广告）<?php } ?><?php } ?>:</div>
	    <div class="fLeft"><textarea name="value1" rows="5" id="leftad" class="w550 f12"><?php echo $this->__muant["advertising"]["value1"] ?></textarea>
		</div>
		<div class="fLeft"></div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 广告2<?php if($this->__muant["cid"]==1000000000) { ?>（右侧广告）<?php } ?><?php if($this->__muant["cid"]<1000000000) { ?><?php if($this->__muant["classinfo"]["orderid"]==0) { ?>（flash焦点广告）<?php } else { ?>（左侧广告）<?php } ?><?php } ?>:</div>
	    <div class="fLeft"><textarea name="value2" rows="4" id="middleflashad" class="w550 f12"><?php echo $this->__muant["advertising"]["value2"] ?></textarea>
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 广告3<?php if($this->__muant["cid"]==1000000000) { ?>（左侧广告）<?php } ?><?php if($this->__muant["cid"]<1000000000) { ?><?php if($this->__muant["classinfo"]["orderid"]==0) { ?>（左侧广告）<?php } else { ?>（无广告）<?php } ?><?php } ?>:</div>
	    <div class="fLeft"><textarea name="value3" rows="4" id="middlead" class="w550 f12"><?php echo $this->__muant["advertising"]["value3"] ?></textarea>
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 广告4<?php if($this->__muant["cid"]==1000000000) { ?>（右侧广告，广告2下面位置）<?php } ?><?php if($this->__muant["cid"]<1000000000) { ?><?php if($this->__muant["classinfo"]["orderid"]==0) { ?>（右侧广告）<?php } else { ?>（无广告）<?php } ?><?php } ?>:</div>
	    <div class="fLeft"><textarea name="value4" rows="4" id="bottomad" class="w550 f12"><?php echo $this->__muant["advertising"]["value4"] ?></textarea>
		</div>
		<div class="cls"></div>
    </div>
	<!--div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 广告5:</div>
	    <div class="fLeft"><textarea name="value5" rows="2" id="bottomad" class="w550"><?php echo $this->__muant["advertising"]["value5"] ?></textarea>
		</div>
		<div class="cls"></div>
    </div-->
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 上传图片:</div>
		<div class="fLeft"><iframe src="./admin/adminajaxset.php?switch=uploadnewspic&lid=<?php echo $this->__muant["lid"] ?>" width="260" frameborder="0" height="22" allowTransparency='true' name="uploadpic"></iframe></div>
		图片路径：<input name="upload_pic_path" type="text" id="upload_pic_path" size="45" class="txInput"><div id="upload_pic"></div>
		<div class="cls"></div>
    </div>
	<?php } ?>
	<div class="cls"></div>
</div>
<div id="divMainBodyContentSumbit">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
</body></html>