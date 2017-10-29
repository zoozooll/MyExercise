<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
</head>
<body id="index">
<div id="wrapper">
    <?php include("inc/#body_top.tpl.php") ?>
    <div id="locate"><div id="ps"><span id="topnav"><a href="./">首页</a><?php $catnavinum = count($this->__muant["catnav"]); for($catnavi = 0; $catnavi<$catnavinum; $catnavi++) { ?> &raquo; <a href="<?php echo $this->__muant["catnav"]["$catnavi"]["url"] ?>"><?php echo $this->__muant["catnav"]["$catnavi"]["name"] ?></a><?php } ?> &raquo; 类别 </span> </div></div>
    <!-- -->
	<div id="container"><div class='mallLogin'> <span class='welcome'>所有的商品类别</span></div>
    <div class="content">  
      <div class="compareList">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">商品分类</li>
        </ul>
        <div id="con1">
            <?php $classinum = count($this->__muant["class"]); for($classi = 0; $classi<$classinum; $classi++) { ?>
                <?php if($this->__muant["class"]["$classi"]["cpid"]=='0') { ?>
                <div class="categoryList">
                    <h2><a href="<?php echo $this->__muant["class"]["$classi"]["url"] ?>"><?php echo $this->__muant["class"]["$classi"]["name"] ?></a></h2>
                    <ul>
                        <?php } else { ?><li><a href="<?php echo $this->__muant["class"]["$classi"]["url"] ?>"><?php echo $this->__muant["class"]["$classi"]["name"] ?></a></li><?php } ?>
                        <?php if($this->__muant["class"]["$classi"]["end"]==1) { ?>
                    </ul>
                </div><?php } ?>
            <?php } ?>			
        </div>
      </div>
    </div>
</div>
	<!-- -->
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body></html>