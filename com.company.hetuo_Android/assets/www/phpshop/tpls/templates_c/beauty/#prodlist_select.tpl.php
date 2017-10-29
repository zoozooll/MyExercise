<div id="ttl">
	<h2>商品类别</h2>
	<div id="divClass"><img src="images/default/023527109.gif" width="3" height="5" align="absmiddle" /> <?php echo $this->__muant["categoryinfo"]["name"] ?></div> 
	<div id="divCat">
<?php if($this->__muant["isleaf"]=='0') { ?>
		<div class="bggyls">
			<div class="opt">
			<h3>按类别浏览</h3>
			<ul>
			<?php $categoryinum = count($this->__muant["category"]); for($categoryi = 0; $categoryi<$categoryinum; $categoryi++) { ?>
			<li><a href="<?php echo $this->__muant["category"]["$categoryi"]["url"] ?>" class="whiter" title="查看<?php echo $this->__muant["category"]["$categoryi"]["name"] ?>商品"><?php echo $this->__muant["category"]["$categoryi"]["name"] ?></a></li>
			<?php } ?>
			</ul>
			</div>
		</div>
<?php } else { ?>
	<?php if($this->__muant["brand"]!='') { ?>
		<div class="bggyls opbg">
			<div class="opt">
				<h3>品牌类别</h3>
			 <div class="sl"> 
				<ul><?php if($this->__muant["nobrandurl"]!='') { ?><li><a href="<?php echo $this->__muant["nobrandurl"]["url"] ?>"><?php echo $this->__muant["nobrandurl"]["name"] ?> <img src="./images/default/close.gif" border="0" id="close_des" /></a></li><?php } ?>
				<?php $brandinum = count($this->__muant["brand"]); for($brandi = 0; $brandi<$brandinum; $brandi++) { ?><li><a href="<?php echo $this->__muant["brand"]["$brandi"]["url"] ?>"><?php echo $this->__muant["brand"]["$brandi"]["name"] ?></a></li><?php if($this->__muant["brand"]["$brandi"]["more"]=='1') { ?></ul><ul id="brandfilter" style="display:none;"><?php } ?><?php if($this->__muant["brand"]["$brandi"]["more"]=='2') { ?></ul><ul><li id="more"><a href="javascript:void(0)" onclick="document.getElementById('brandfilter').style.display='';this.style.display='none'">...more</a></li><?php } ?><?php } ?>
				</ul>
			</div>
			</div>
		</div>
	<?php } ?>
	<?php $filterinum = count($this->__muant["filter"]); for($filteri = 0; $filteri<$filterinum; $filteri++) { ?>
		<?php if($this->__muant["filter"]["$filteri"]["ar"]==1) { ?>
		<div class="bggyls opbg">
			<div class="opt">
			<h3><?php echo $this->__muant["filter"]["$filteri"]["name"] ?></h3>
			<div id="atr" class="sl scroll">
			  <ul>
			  <?php if($this->__muant["filter"]["$filteri"]["select"]!='') { ?><li id="closeatr"><a href="<?php echo $this->__muant["filter"]["$filteri"]["selecturl"] ?>"><?php echo $this->__muant["filter"]["$filteri"]["select"] ?> <img src="./images/default/close.gif" border="0" id="close_des" /></a></li><?php } ?>
			  <?php if($this->__muant["filter"]["$filteri"]["url"]!='') { ?><li><a href="<?php echo $this->__muant["filter"]["$filteri"]["url"] ?>"><?php echo $this->__muant["filter"]["$filteri"]["value"] ?></a></li><?php } ?>
		<?php } else { ?>
			  <?php if($this->__muant["filter"]["$filteri"]["url"]!='') { ?>
			  <?php if($this->__muant["filter"]["$filteri"]["more"]=='1') { ?></ul><ul id="atr<?php echo $this->__muant["filter"]["$filteri"]["id"] ?>" style="display:none;"><?php } ?>
			  <li><a href="<?php echo $this->__muant["filter"]["$filteri"]["url"] ?>"><?php echo $this->__muant["filter"]["$filteri"]["value"] ?></a></li>
			  <?php if($this->__muant["filter"]["$filteri"]["more"]=='2') { ?></ul><ul><li id="more"><a href="javascript:void(0)" onclick="document.getElementById('atr<?php echo $this->__muant["filter"]["$filteri"]["id"] ?>').style.display='';this.style.display='none'">...more</a></li><?php } ?>
			  <?php } ?>
		<?php } ?>
		<?php if($this->__muant["filter"]["$filteri"]["br"]==2) { ?>
			  </ul>
		   </div>
		   </div>
		</div>
		<?php } ?>
	<?php } ?>
	<?php if($this->__muant["rangeprice"]!='') { ?>
		<div class="bggyls opbg">
			<div class="opt">
				<h3>价格范围</h3>
				<ul>
				<?php if($this->__muant["nopriceurl"]!='') { ?><li><a href="<?php echo $this->__muant["nopriceurl"] ?>">去掉价格选项 <img src="./images/default/close.gif" border="0" id="close_des" /></a></li><?php } ?>
				<?php $rangepriceinum = count($this->__muant["rangeprice"]); for($rangepricei = 0; $rangepricei<$rangepriceinum; $rangepricei++) { ?><?php if($this->__muant["rangeprice"]["$rangepricei"]["url"]!='') { ?><li><a href="<?php echo $this->__muant["rangeprice"]["$rangepricei"]["url"] ?>"><?php if($this->__muant["rangeprice"]["0"]["price"]==$this->__muant["rangeprice"]["$rangepricei"]["price"]) { ?>小于 <?php echo $this->__muant["rangeprice"]["$rangepricei"]["price"] ?><?php } elseif($this->__muant["rangeprice"][count($this->__muant["rangeprice"])-1]["price"]==$this->__muant["rangeprice"]["$rangepricei"]["price"]) { ?>大于<?php echo $this->__muant["rangeprice"]["$rangepricei"]["price"] ?><?php } else { ?><?php echo $this->__muant["rangeprice"]["$rangepricei"-1]["price"] ?> ~ <?php echo $this->__muant["rangeprice"]["$rangepricei"]["price"] ?><?php } ?></a></li><?php } ?><?php } ?>
				</ul>
			</div>
		</div>
	<?php } ?>
<?php } ?>
	 <form name="prodlist" action="<?php echo $this->__muant["CategoryUrl"] ?>" method="post">
	   <div class="bggyls opbg">
		   <div class="opt">
			  <h3>在结果中搜索</h3>
			  <input type="text" class="opin" name="see" value="<?php echo $this->__muant["se"] ?>" />
			  <input type="submit" border="0" class="buts" value="搜　索">
			  <br />
		   </div>
	   </div>
	  </form>
	   <div><br />
	   </div>
	</div>		
	<div id="ad_catl">
    <?php $value2inum = count($this->__muant["adinfo"]["value2"]); for($value2i = 0; $value2i<$value2inum; $value2i++) { ?><a href="<?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["url"] ?>" target="_blank"><img title="<?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["text"] ?>" alt="<?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["text"] ?>" src="<?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["pic"] ?>" border="0" onerror="this.style.display='none'"/></a><?php } ?></div>
</div>