<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
</head>
<body id="index">
<div id="wrapper">
	<?php include("inc/#body_top.tpl.php") ?>
	<div id="ps"><span id="locate"><a href="./">首页</a><?php $catnavinum = count($this->__muant["catnav"]); for($catnavi = 0; $catnavi<$catnavinum; $catnavi++) { ?> &raquo; <a href="<?php echo $this->__muant["catnav"]["$catnavi"]["url"] ?>"><?php echo $this->__muant["catnav"]["$catnavi"]["name"] ?></a><?php } ?> &raquo; <?php echo $this->__muant["product"]["name"] ?></span></div>
	<?php include("#product_info.tpl.php") ?>
	<?php if($this->__muant["arrProductRelated"]!=NULL) { ?>
	<?php include("#productrelated.tpl.php") ?>
	<div class="cl"></div>
	<?php } ?>
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body>
</html>