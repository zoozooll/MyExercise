<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
</head>
<body id="index">
<div id="wrapper">
    <?php include("inc/#body_top.tpl.php") ?>
	<div id="locate"><span id="topnav"><a href="./">首页</a><?php $catnavinum = count($this->__muant["catnav"]); for($catnavi = 0; $catnavi<$catnavinum; $catnavi++) { ?> &raquo; <a href="<?php echo $this->__muant["catnav"]["$catnavi"]["url"] ?>"><?php echo $this->__muant["catnav"]["$catnavi"]["name"] ?></a><?php } ?></span> &raquo; 搜索 <?php echo $this->__muant["keyword"] ?>  </div>
    <div>
	<div class="mallLogin"> <span class="welcome">搜索 " <?php echo $this->__muant["keyword"] ?> " 的商品</span></div>
	<div class="content">
		<div class="compareList">
			<div style="padding:20px 0px 10px 50px;">
			<?php if($this->__muant["categoryproduct"]=='') { ?>
			 关键字  <?php echo $this->__muant["keyword"] ?> 无搜索到相关商品，请更改搜索条件！
			<?php } else { ?>
			 <div> <b>搜索 <?php echo $this->__muant["keyword"] ?> 找到以下商品：</b></div>
			 <ul>
			<?php $categoryproductinum = count($this->__muant["categoryproduct"]); for($categoryproducti = 0; $categoryproducti<$categoryproductinum; $categoryproducti++) { ?> 
			<li><a href="<?php echo $this->__muant["categoryproduct"]["$categoryproducti"]["url"] ?>" target="_blank"> <?php echo $this->__muant["keyword"] ?> <?php echo $this->__muant["categoryproduct"]["$categoryproducti"]["name"] ?>(<?php echo $this->__muant["categoryproduct"]["$categoryproducti"]["prodnum"] ?>)</a></li>
			<?php } ?>
			</ul>
			<?php } ?>
			</div>
		</div>
	</div>
    <div class="cl"></div>
  </div>
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body>
</html>
