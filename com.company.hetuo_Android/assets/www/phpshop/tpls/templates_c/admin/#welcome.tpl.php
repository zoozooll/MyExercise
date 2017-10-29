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
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div>欢迎使用本购物系统 PHPShop v1.0</div>
</div>
<div id="divMainBodyContent">
    <div id="divMainBodyContentTitle">快捷菜单:</div>
    <div class="fLeft"><?php $adminsetinfoinum = count($this->__muant["adminsetinfo"]); for($adminsetinfoi = 0; $adminsetinfoi<$adminsetinfoinum; $adminsetinfoi++) { ?><?php if($this->__muant["adminsetinfo"]["$adminsetinfoi"]["orderid"]==0) { ?><br /><b><?php } ?><?php if($this->__muant["adminsetinfo"]["$adminsetinfoi"]["orderid"]>0) { ?><a href="javascript:void(0);" onClick="parent.show('','<?php echo $this->__muant["adminsetinfo"]["$adminsetinfoi"]["fsid"] ?>','<?php echo $this->__muant["adminsetinfo"]["$adminsetinfoi"]["id"] ?>'); return false"><?php } ?><?php echo $this->__muant["adminsetinfo"]["$adminsetinfoi"]["name"] ?><?php if($this->__muant["adminsetinfo"]["$adminsetinfoi"]["orderid"]>0) { ?></a><?php } ?><?php if($this->__muant["adminsetinfo"]["$adminsetinfoi"]["orderid"]=='0') { ?>→</b><?php } ?>　<?php } ?><br /><br /></div>
    <div class="cls"></div>
</div>
<div id="BodyContent">
</div>

</body></html>