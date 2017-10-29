<?php if($this->__muant["arrProductRelated"]!='') { ?>
<div class="cl"></div>
<div class="content">
    <ul style="width: 944px;" class="shoppingTab">
      <li id="tab1" class="cur">相关产品</li>
    </ul>
    <div class="productList" style="width:950px;">
          <ul><?php $arrProductRelatedinum = count($this->__muant["arrProductRelated"]); for($arrProductRelatedi = 0; $arrProductRelatedi<$arrProductRelatedinum; $arrProductRelatedi++) { ?>
                  <li> <a title="<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["name"] ?>" class="productImg" target="_blank" href="<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["url"] ?>"><img onerror="this.src='product/nopic.gif'" alt="<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["name"] ?>" src="<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["m_pic"] ?>"></a>
           		  <p class="name"><a title="<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["name"] ?>" target="_blank" href="<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["url"] ?>"><?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["name"] ?></a></p>
                  <?php if($this->__muant["arrProductRelated"]["$arrProductRelatedi"]["market_offer"]==1) { ?>
                  <p class="pricelabel"><STRIKE>市场价:&#165;<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["price_market"] ?></STRIKE></p>
                  <?php } ?>
                  <p class="pricelabel">
                  售　价:<?php if($this->__muant["arrProductRelated"]["$arrProductRelatedi"]["special_offer"]==1) { ?><STRIKE><?php } ?>
                  <font color="#FF0000"> &yen; </font><?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["price"] ?>
                  <?php if($this->__muant["arrProductRelated"]["$arrProductRelatedi"]["special_offer"]==1) { ?></STRIKE><?php } ?>
                  </p>
                  <?php if($this->__muant["arrProductRelated"]["$arrProductRelatedi"]["special_offer"]==1) { ?>
                  <p class="pricelabel">特　价:<span class="priceRed">&#165;<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["price_special"] ?></span></p>
                  <?php } else { ?><?php if($this->__muant["arrProductRelated"]["$arrProductRelatedi"]["member_offer"]==1) { ?>
                  <p class="pricelabel">会员价:<span class="priceRed">&#165;<?php echo $this->__muant["arrProductRelated"]["$arrProductRelatedi"]["price_member"] ?></span></p>
                  <?php } ?><?php } ?>
                  </li><?php } ?>
            </ul>
    </div>
</div>
<?php } ?>