<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
</head>
<body id="index">
<div id="wrapper">
    <?php include("inc/#body_top.tpl.php") ?>
    <div id="locate"><div id="ps"><span id="topnav"><a href="./">首页</a><?php $catnavinum = count($this->__muant["catnav"]); for($catnavi = 0; $catnavi<$catnavinum; $catnavi++) { ?> &raquo; <a href="<?php echo $this->__muant["catnav"]["$catnavi"]["url"] ?>"><?php echo $this->__muant["catnav"]["$catnavi"]["name"] ?></a><?php } ?> &raquo; 商品 </span> </div></div>
    <!-- -->
	<div id="container"><div class='mallLogin'> <span class='welcome'>您可能感兴趣的商品</span></div>
    <div class="content">  
      <div class="compareList">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">商品</li>
        </ul>
        <div id="con1">
            <!-- -->
            <div class="list">       
    <div class="productList">
      <ul>
      <?php $productinum = count($this->__muant["product"]); for($producti = 0; $producti<$productinum; $producti++) { ?>
        <li> <a href="<?php echo $this->__muant["product"]["$producti"]["url"] ?>" target="_blank" class="productImg" title="<?php echo $this->__muant["product"]["$producti"]["name"] ?>"><img src="<?php echo $this->__muant["product"]["$producti"]["m_pic"] ?>" alt="<?php echo $this->__muant["product"]["$producti"]["name"] ?>" onerror="this.src='product/nopic.gif'" /></a>
       <p class="name"><A href="<?php echo $this->__muant["product"]["$producti"]["url"] ?>" target="_blank" title="<?php echo $this->__muant["product"]["$producti"]["name"] ?>"><?php echo $this->__muant["product"]["$producti"]["name"] ?></A></p>
              <?php if($this->__muant["product"]["$producti"]["market_offer"]==1) { ?>
              <p class="pricelabel"><STRIKE>市场价:&#165;<?php echo $this->__muant["product"]["$producti"]["price_market"] ?></STRIKE></p>
              <?php } ?>
              <p class="pricelabel">
              售　价:<?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?><STRIKE><?php } ?>
              <font color="#FF0000"> &yen; </font><?php echo $this->__muant["product"]["$producti"]["price"] ?>
              <?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?></STRIKE><?php } ?>
              </p>
              <?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?>
              <p class="pricelabel">特　价:<span class="priceRed">&#165;<?php echo $this->__muant["product"]["$producti"]["price_special"] ?></span></p>
              <?php } else { ?><?php if($this->__muant["product"]["$producti"]["member_offer"]==1) { ?>
              <p class="pricelabel">会员价:<span class="priceRed">&#165;<?php echo $this->__muant["product"]["$producti"]["price_member"] ?></span></p>
              <?php } ?><?php } ?>
        </li>
      <?php } ?>         
      </ul>
          <div class="pages" id="pages_pg_2">
            <span class="count">Pages: <?php echo $this->__muant["pn"] ?> / <?php echo $this->__muant["allPage"] ?></span>
            <span class="number">
              <?php if($this->__muant["pn"]==1) { ?>
                <span class="disabled" title="First Page">&laquo;</span>
              <?php } else { ?>
                <a href="<?php echo $this->__muant["firstpage"] ?>">&laquo;</a>
              <?php } ?>
              <?php if($this->__muant["pn"]==1) { ?>
                <span class="disabled" title="Prev Page">&#8249;</span>
              <?php } else { ?>
                <a href="<?php echo $this->__muant["prvepage"] ?>">&#8249;</a>
              <?php } ?>
              <?php $arrPagesinum = count($this->__muant["arrPages"]); for($arrPagesi = 0; $arrPagesi<$arrPagesinum; $arrPagesi++) { ?>
               <?php if($this->__muant["arrPages"]["$arrPagesi"]["pn"]==$this->__muant["pn"]) { ?>
                <span class="current" title="Page"><?php echo $this->__muant["arrPages"]["$arrPagesi"]["pn"] ?></span>
               <?php } else { ?>
                <a href="<?php echo $this->__muant["arrPages"]["$arrPagesi"]["url"] ?>"><?php echo $this->__muant["arrPages"]["$arrPagesi"]["pn"] ?></a>
               <?php } ?>
              <?php } ?>
              <?php if($this->__muant["pn"]==$this->__muant["allPage"]) { ?>
                <span class="disabled" title="Next Page">&#8250;</span>
              <?php } else { ?>
                <a href="<?php echo $this->__muant["nextpage"] ?>">&#8250;</a>
              <?php } ?>
                <?php if($this->__muant["pn"]==$this->__muant["allPage"]) { ?>
                <span class="disabled" title="Last Page">&raquo;</span>
                <?php } else { ?>
                <a href="<?php echo $this->__muant["lastpage"] ?>">&raquo;</a>
                <?php } ?>
            </span>
        </div>
    </div>
  </div>
            <!-- -->
        </div>
      </div>
    </div>
</div>
	<!-- -->
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body></html>