<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
<script type="text/javascript" src="jscript/comm/sidebyside.js"></script>
</head>
<body id="index">
<div id="wrapper">
    <?php include("inc/#body_top.tpl.php") ?> 
	<div id="locate"><a href="./">首页</a><?php $catnavinum = count($this->__muant["catnav"]); for($catnavi = 0; $catnavi<$catnavinum; $catnavi++) { ?> &raquo; <a href="<?php echo $this->__muant["catnav"]["$catnavi"]["url"] ?>"><?php echo $this->__muant["catnav"]["$catnavi"]["name"] ?></a><?php } ?> </div><span id="topnav"></span>
    <div>
	<?php include("#prodlist_select.tpl.php") ?>
	<div id="ttm">
	<?php if($this->__muant["hotproduct"]!='') { ?>
		<h2>热销产品</h2>
		<div id="clprolist">
		<?php $hotproductinum = count($this->__muant["hotproduct"]); for($hotproducti = 0; $hotproducti<$hotproductinum; $hotproducti++) { ?>
		<div class="clpro">
		  <div class="proimg"><a href="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["url"] ?>" target="_blank"><img src="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["m_pic"] ?>" alt="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["name"] ?>" onerror="this.src='product/nopic.gif'" /></a>
		  </div>
		  <div id="product_name"><a href="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["url"] ?>" class="green b"><?php echo $this->__muant["hotproduct"]["$hotproducti"]["name"] ?></a></div>
		  <div id="proprice">
			  <?php if($this->__muant["hotproduct"]["$hotproducti"]["market_offer"]==1) { ?><div id="price_l"><STRIKE>市场价</STRIKE></div>
			  <div id="price_r"><STRIKE> &yen; <?php echo $this->__muant["hotproduct"]["$hotproducti"]["price_market"] ?></STRIKE></div><div class="cl"></div><?php } ?>
			  <div id="price_l">售　价</div><div id="price_r"><?php if($this->__muant["hotproduct"]["$hotproducti"]["special_offer"]==1) { ?><STRIKE><?php } ?><font color="#FF0000"> &yen; </font><?php echo $this->__muant["hotproduct"]["$hotproducti"]["price"] ?><?php if($this->__muant["hotproduct"]["$hotproducti"]["special_offer"]==1) { ?></STRIKE><?php } ?></div><div class="cl"></div>
			  <?php if($this->__muant["hotproduct"]["$hotproducti"]["special_offer"]==1) { ?>
			  <div id="price_l">特　价</div><div id="price_r"><font color="#FF0000"> &yen; <?php echo $this->__muant["hotproduct"]["$hotproducti"]["price_special"] ?></font></div>
			  <?php } else { ?><?php if($this->__muant["hotproduct"]["$hotproducti"]["member_offer"]==1) { ?><div id="price_l">会员价</div><div id="price_r"><font color="#FF0000"> &yen; 
			  <?php echo $this->__muant["hotproduct"]["$hotproducti"]["price_member"] ?></font></div><?php } ?>
			  <?php } ?>
			  <div class="cl"></div>
		  </div>
		  <div class="clbut">
		  <?php if($this->__muant["hotproduct"]["$hotproducti"]["store"]>0) { ?><a href="#shop" onclick="setUserShopFrame('<?php echo $this->__muant["hotproduct"]["$hotproducti"]["id"] ?>');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/ban_product.gif" border="0" /></a><?php } else { ?><img src="images/default/no_ban_product.gif" alt="暂时缺货" border="0" /><?php } ?>
		  </div>
		</div>
		<?php } ?>
		</div>
	<?php } ?>
	<?php if($this->__muant["view"]!='list') { ?><?php include("#prodlist_data.tpl.php") ?><?php } else { ?><?php include("#prodlist_list_data.tpl.php") ?><?php } ?>
    <br />
	</div>
    <div id="ttr">
	   <h2>产品资讯</h2>
	   <ul><?php if($this->__muant["news"]!='') { ?>
	   <?php $newsinum = count($this->__muant["news"]); for($newsi = 0; $newsi<$newsinum; $newsi++) { ?>
	   <li><a href="<?php echo $this->__muant["news"]["$newsi"]["url"] ?>" target="_blank" title="<?php echo $this->__muant["news"]["$newsi"]["title"] ?>"><?php echo $this->__muant["news"]["$newsi"]["shot_title"] ?></a></li>
	   <?php } ?><?php } ?>
	   </ul>
	<div id="ad_catr"><?php $value1inum = count($this->__muant["adinfo"]["value1"]); for($value1i = 0; $value1i<$value1inum; $value1i++) { ?><a href="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["url"] ?>" target="_blank"><img title="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["text"] ?>" alt="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["text"] ?>" src="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["pic"] ?>" border="0" onerror="this.style.display='none'"/></a><?php } ?></div>
	</div>
    <div class="cl"></div>
  </div>
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
<script language="javascript">
checkSelect('sdid[]');
</script>
</body>
</html>
