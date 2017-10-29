<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
</head>
<body id="index">
<div id="wrapper">
  <?php include("inc/#body_top.tpl.php") ?>
<div id="locate"><a href="./">首页</a><?php $catnavinum = count($this->__muant["catnav"]); for($catnavi = 0; $catnavi<$catnavinum; $catnavi++) { ?> &raquo; <a href="<?php echo $this->__muant["catnav"]["$catnavi"]["url"] ?>"><?php echo $this->__muant["catnav"]["$catnavi"]["name"] ?></a><?php } ?></div>
<div class="channel_ad_top"><?php $value1inum = count($this->__muant["adinfo"]["value1"]); for($value1i = 0; $value1i<$value1inum; $value1i++) { ?><a href="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["url"] ?>" target="_blank"><img title="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["text"] ?>" alt="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["text"] ?>" src="<?php echo $this->__muant["adinfo"]["value1"]["$value1i"]["pic"] ?>" border="0" onerror="this.style.display='none'" /></a><?php } ?></div>
<div id="container">
<div class="column1">
  <div class="box1" style="margin-bottom: 10px;">
    <h2>商品分类<span><img src="images/beauty/cateh2.gif" alt="商品分类"></span></h2>
    <div class="channel_list"><img src="images/default/023527109.gif" width="3" height="5" align="absmiddle" /> <a href="<?php echo $this->__muant["channelinfo"]["cat_url"] ?>" title="<?php echo $this->__muant["channel"]["$channeli"]["name"] ?>"><?php echo $this->__muant["channelinfo"]["name"] ?></a></div> 
	<?php $channelinum = count($this->__muant["channel"]); for($channeli = 0; $channeli<$channelinum; $channeli++) { ?>
     <?php if($this->__muant["channel"]["$channeli"]["cpid"]==$this->__muant["channel"]["$channeli"]["cid"]) { ?><div class="hotCate"><div class="hotTitle"><a href="<?php echo $this->__muant["channel"]["$channeli"]["url"] ?>" title="<?php echo $this->__muant["channel"]["$channeli"]["name"] ?>"><?php echo $this->__muant["channel"]["$channeli"]["name"] ?></a></div></div><?php if($this->__muant["channel"]["$channeli"]["ccidnum"]>'0') { ?>
			  <div class="cateList"><?php } ?><?php } ?><?php if($this->__muant["channel"]["$channeli"]["depth"]>1) { ?><a href="<?php echo $this->__muant["channel"]["$channeli"]["url"] ?>" title="<?php echo $this->__muant["channel"]["$channeli"]["name"] ?>"><?php echo $this->__muant["channel"]["$channeli"]["name"] ?></a><?php if($this->__muant["channel"]["$channeli"+1]["depth"]<2) { ?></div>
	 <?php } ?><?php } ?>
    <?php } ?>
  </div>
  <div class="box1">
    <h2><span><img src="images/beauty/pinpaih2.gif" alt="品牌推荐"></span></h2>
    <div class="brand"><!-- 广告3 -->
    		<?php $value3inum = count($this->__muant["adinfo"]["value3"]); for($value3i = 0; $value3i<$value3inum; $value3i++) { ?><div style=" padding-bottom:1px;"><a href="<?php echo $this->__muant["adinfo"]["value3"]["$value3i"]["url"] ?>" target="_blank"><img src="<?php echo $this->__muant["adinfo"]["value3"]["$value3i"]["pic"] ?>" border="0" onerror="this.style.display='none'" title="<?php echo $this->__muant["adinfo"]["value3"]["$value3i"]["text"] ?>" alt="<?php echo $this->__muant["adinfo"]["value3"]["$value3i"]["text"] ?>" width="231"/></a></div><?php } ?>
    </div>
  </div>
</div>
<div class="column2">
  <div class="activity" style="padding-bottom: 10px;"> 
    <div id="MainPromotionBanner">
      <div id="SlidePlayer">
      	<div id="channel_ad_flash"></div>
      </div>
    </div>
   
  </div>
  <div style="border-right: 1px solid rgb(0, 160, 233);">
    <h2><img src="images/beauty/h1.gif" alt="最新商品"></h2>
    <?php $productinum = count($this->__muant["product"]); for($producti = 0; $producti<$productinum; $producti++) { ?>
    <div class="proList">
    <p class="img"><A href="<?php echo $this->__muant["product"]["$producti"]["url"] ?>" target="_blank"><img alt="查看 <?php echo $this->__muant["product"]["$producti"]["name"] ?>" src="<?php echo $this->__muant["product"]["$producti"]["m_pic"] ?>" onerror="this.src='product/nopic.gif'" border="0"></A></p>
    <p class="name"><A href="<?php echo $this->__muant["product"]["$producti"]["url"] ?>" target="_blank"><?php echo $this->__muant["product"]["$producti"]["name"] ?></A></p>
    <?php if($this->__muant["product"]["$producti"]["market_offer"]==1) { ?><p class="pricelabel"><STRIKE>市场价 &yen; <?php echo $this->__muant["product"]["$producti"]["price_market"] ?></STRIKE></p><?php } ?>
    <p class="pricelabel">售　价:<span class="priceRed"><?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?><STRIKE><?php } ?><font color="#FF0000"> &yen; </font><?php echo $this->__muant["product"]["$producti"]["price"] ?><?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?></STRIKE><?php } ?></span></p>
    <?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?>
    <p class="pricelabel">特　价:<span class="priceRed"> &yen; <?php echo $this->__muant["product"]["$producti"]["price_special"] ?></span></p>
    <?php } else { ?><?php if($this->__muant["product"]["$producti"]["member_offer"]==1) { ?>
    <p class="pricelabel">会员价:<span class="priceRed"> &#165; <?php echo $this->__muant["product"]["$producti"]["price_member"] ?></span></p>
    <?php } ?>
    <?php } ?>
    </div>
    <?php } ?>
	<div class="clear"></div>
  </div>
</div>
<div class="column3">
  <div class="box2">
    <h3>产品资讯</h3>
    <ol>
     <?php $newsinum = count($this->__muant["news"]); for($newsi = 0; $newsi<$newsinum; $newsi++) { ?>
     <li> <a href="<?php echo $this->__muant["news"]["$newsi"]["url"] ?>" target="_blank" title="<?php echo $this->__muant["news"]["$newsi"]["title"] ?>"><?php echo $this->__muant["news"]["$newsi"]["shot_title"] ?></a><br></li>
    </li>
    <?php } ?>
    </ol>
  </div>
  <div class="box2">
    <h3>热销产品</h3>
    <ul class="related">
    <?php $recproductinum = count($this->__muant["recproduct"]); for($recproducti = 0; $recproducti<$recproductinum; $recproducti++) { ?>
        <li><p class="img"><A href="<?php echo $this->__muant["recproduct"]["$recproducti"]["url"] ?>" target="_blank"><img alt="查看 <?php echo $this->__muant["recproduct"]["$recproducti"]["name"] ?>" src="<?php echo $this->__muant["recproduct"]["$recproducti"]["m_pic"] ?>" onerror="this.src='product/nopic.gif'" border="0"></A></p>
            <p class="name"><A href="<?php echo $this->__muant["recproduct"]["$recproducti"]["url"] ?>" target="_blank"><?php echo $this->__muant["recproduct"]["$recproducti"]["name"] ?></A></p>
            <?php if($this->__muant["recproduct"]["$recproducti"]["market_offer"]==1) { ?><p><STRIKE>市场价 &yen; <?php echo $this->__muant["recproduct"]["$recproducti"]["price_market"] ?></STRIKE></p><?php } ?>
            <p><?php if($this->__muant["recproduct"]["$recproducti"]["special_offer"]==1) { ?><STRIKE><?php } ?>售　价 &yen; <span class="priceRed"><?php echo $this->__muant["recproduct"]["$recproducti"]["price"] ?></span><?php if($this->__muant["recproduct"]["$recproducti"]["special_offer"]==1) { ?></STRIKE><?php } ?></p>
             <?php if($this->__muant["recproduct"]["$recproducti"]["special_offer"]==1) { ?><p>特　 价&yen; <span class="priceRed"><?php echo $this->__muant["recproduct"]["$recproducti"]["price_special"] ?></span></p><?php } else { ?><?php if($this->__muant["recproduct"]["$recproducti"]["member_offer"]==1) { ?><p>会员价 &yen; <span class="priceRed"><?php echo $this->__muant["recproduct"]["$recproducti"]["price_member"] ?></span></p><?php } ?><?php } ?>
            <p class="img"><?php if($this->__muant["recproduct"]["$recproducti"]["store"]>0) { ?><a href="#shop" onclick="setUserShopFrame('<?php echo $this->__muant["recproduct"]["$recproducti"]["id"] ?>');myshow('usershop');callLoad('loadingiframe');"><IMG <?php echo $this->__muant["recproduct"]["$recproducti"]["name"] ?>" src="images/default/ban_product.gif" alt="点击购买"></a><?php } else { ?><img src="images/default/no_ban_product.gif" alt="暂时缺货" border="0" /><?php } ?></p>
        </li>
    <?php } ?>
    </ul>
    <ul class="hotLink">
	  	<li></li>
    </ul>
            <!-- 广告4 -->
    		<?php $value4inum = count($this->__muant["adinfo"]["value4"]); for($value4i = 0; $value4i<$value4inum; $value4i++) { ?><a href="<?php echo $this->__muant["adinfo"]["value4"]["$value4i"]["url"] ?>" target="_blank"><img src="<?php echo $this->__muant["adinfo"]["value4"]["$value4i"]["pic"] ?>" border="0" onerror="this.style.display='none'" width="196"/></a><?php } ?>
            
	 
    <div class="clear"></div>
  </div>
</div>
</div>
<script type="text/javascript" src="./jscript/comm/swfobject.js"></script>
<script type="text/javascript">
//<!-- 广告2 -->
var focus_width=500;
var focus_height=210;
var text_height=0;
var swf_height = focus_height+text_height;
var pics="<?php $value2inum = count($this->__muant["adinfo"]["value2"]); for($value2i = 0; $value2i<$value2inum; $value2i++) { ?><?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["pic"] ?>|<?php } ?>";
pics = pics.substr(0, pics.length -1);
var links="<?php $value2inum = count($this->__muant["adinfo"]["value2"]); for($value2i = 0; $value2i<$value2inum; $value2i++) { ?><?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["url"] ?>|<?php } ?>";
links = links.substr(0, links.length -1);
var texts="成人用品|情趣内衣|情趣用品";
var so = new SWFObject("images/flash/default/picviewer.swf", "mymovie", focus_width, focus_height, "7", "#FFFFFF");
so.addParam("movie", "images/flash/default/picviewer.swf");
so.addParam("menu", "false");
so.addParam("wmode", "opaque");
so.addParam("FlashVars", 'pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height);
so.write("channel_ad_flash");
</script>
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body>
</html>