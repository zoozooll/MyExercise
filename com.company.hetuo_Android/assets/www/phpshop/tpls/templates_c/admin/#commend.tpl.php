<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
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
		<div id="divMainBodyContentTitle"> 首页产品个数:</div>
	    <div class="fLeft"><input name="value1" value="<?php echo $this->__muant["value"]["value1"] ?>" type="text">
		</div>
		<div class="fLeft"></div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> channel页产品个数 1:</div>
	    <div class="fLeft"><input name="value2" value="<?php echo $this->__muant["value"]["value2"] ?>" type="text">
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> channel页产品个数 2:</div>
	    <div class="fLeft"><input name="value3" value="<?php echo $this->__muant["value"]["value3"] ?>" type="text"></div>
		<div class="cls"></div>
    </div>
	<!--<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 底部广告内容:</div>
	    <div class="fLeft"><input name="" type="text"></div>
		<div class="cls"></div>
    </div>-->
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