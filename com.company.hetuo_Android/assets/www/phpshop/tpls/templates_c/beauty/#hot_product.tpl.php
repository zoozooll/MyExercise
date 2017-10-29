<div id="proList">
    <h2><a href="newproduct.php" title="查看更多商品">查看更多</a></h2>
    <div>       
    	<?php $hotproductinum = count($this->__muant["hotproduct"]); for($hotproducti = 0; $hotproducti<$hotproductinum; $hotproducti++) { ?>
        <div class="mallList">
            <div class="pdetail">
              <p class="img"><A href="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["url"] ?>" target="_blank" title="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["name"] ?>"><IMG alt="查看 <?php echo $this->__muant["hotproduct"]["$hotproducti"]["name"] ?>" src="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["m_pic"] ?>" onerror="this.src='product/nopic.gif'" border="0"></A></p>
              <p class="name"><A href="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["url"] ?>" target="_blank" title="<?php echo $this->__muant["hotproduct"]["$hotproducti"]["name"] ?>"><?php echo $this->__muant["hotproduct"]["$hotproducti"]["name"] ?></A></p>
              <p class="pricelabel"><?php if($this->__muant["hotproduct"]["$hotproducti"]["market_offer"]==1) { ?><STRIKE>市场价:&#165;<?php echo $this->__muant["hotproduct"]["$hotproducti"]["price_market"] ?></STRIKE><?php } ?>&nbsp;</p>
              <?php if($this->__muant["hotproduct"]["$hotproducti"]["special_offer"]==1) { ?>
              <p class="pricelabel">特　价:<span class="priceRed">&#165;<?php echo $this->__muant["hotproduct"]["$hotproducti"]["price_special"] ?></span></p>
              <?php } elseif($this->__muant["hotproduct"]["$hotproducti"]["member_offer"]==1) { ?>
              <p class="pricelabel">会员价:<span class="priceRed">&#165;<?php echo $this->__muant["hotproduct"]["$hotproducti"]["price_member"] ?></span></p>
              <?php } else { ?>
              <p class="pricelabel">售　价:<font color="#FF0000"> &yen; </font><?php echo $this->__muant["hotproduct"]["$hotproducti"]["price"] ?></p>
			  <?php } ?>
            </div>
        </div>
        <?php } ?>  
    </div><div class="clear"></div>
</div>