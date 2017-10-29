<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
<script src="jscript/comm/swfobject.js" type="text/javascript"></script>
</head>
<body id="index">
<div id="wrapper">
    <?php include("inc/#body_top.tpl.php") ?>
    <div id="locate">&nbsp;</div>
  <div id="container"><div class="left">
  <div id="flash">
  <embed type="application/x-shockwave-flash" src="swf/newPlayer.swf" id="mymovie" name="mymovie" quality="high" wmode="transparent" width="670" height="240">  
  </div>
  <div id="flash_index"></div>
  <script language="javascript" type="text/javascript">
		(function(){
		var xmlP="index.php?switch=getadxml";
		xmlP=escape(xmlP);
		var swfPath = "swf/focuspic_one.swf";
		var so_ad1 = new SWFObject(swfPath, 'focusPlayer', '670', '304', '8', '');
		so_ad1.addParam('allowScriptaccess', 'always');
		so_ad1.addParam('FlashVars', 'xmlP='+xmlP);
		so_ad1.addParam('wmode', 'opaque');
		//so_ad1.write('flash');
		})();
  </script>
  <?php include("#hot_product.tpl.php") ?>
  <div class="h5"></div>
  <div class="onlineInner">
<?php $value3inum = count($this->__muant["adinfo"]["value3"]); for($value3i = 0; $value3i<$value3inum; $value3i++) { ?><a href="<?php echo $this->__muant["adinfo"]["value3"]["$value3i"]["url"] ?>"><img src="<?php echo $this->__muant["adinfo"]["value3"]["$value3i"]["pic"] ?>" border="0" onerror="this.style.display='none'" /></a><?php } ?>
  </div><div class="h5"></div>
  <?php $arrChannelProductinum = count($this->__muant["arrChannelProduct"]); for($arrChannelProducti = 0; $arrChannelProducti<$arrChannelProductinum; $arrChannelProducti++) { ?>
  <div id="container"><div class="mallLogin"> <span class="welcome"><a class="topName" href="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["url"] ?>" target="_blank"><?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["name"] ?></a></span> <span style="float:right"><a class="topName" href="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["url"] ?>" target="_blank">更多&raquo;</a></span></div>
    <div class="content">  
      <div class="compareList">
        <div>
            <div class="list">
                <div class="productList" style="width:670px;">
                  <ul><?php $productinum = count($this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]); for($producti = 0; $producti<$productinum; $producti++) { ?>
                      <li style="padding-left:20px;">
                      <a href="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["url"] ?>" target="_blank" class="productImg" title="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["name"] ?>"><img src="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["m_pic"] ?>" alt="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["name"] ?>" onerror="this.src='product/nopic.gif'" /></a>
                      <p class="name"><A href="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["url"] ?>" target="_blank" title="<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["name"] ?>"><?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["name"] ?></A></p>
                      <?php if($this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["market_offer"]==1) { ?>
                      <p class="pricelabel"><STRIKE>市场价:&#165;<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["price_market"] ?></STRIKE></p>
                      <?php } ?>
                      <p class="pricelabel">
                      售　价:<?php if($this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["special_offer"]==1) { ?><STRIKE><?php } ?>
                      <font color="#FF0000"> &yen; </font><?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["price"] ?>
                      <?php if($this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["special_offer"]==1) { ?></STRIKE><?php } ?>
                      </p>
                      <?php if($this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["special_offer"]==1) { ?>
                      <p class="pricelabel">特　价:<span class="priceRed">&#165;<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["price_special"] ?></span></p>
                      <?php } else { ?><?php if($this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["member_offer"]==1) { ?>
                      <p class="pricelabel">会员价:<span class="priceRed">&#165;<?php echo $this->__muant["arrChannelProduct"]["$arrChannelProducti"]["product"]["$producti"]["price_member"] ?></span></p>
                      <?php } ?><?php } ?>
                      </li>
                      <?php } ?>
                  </ul>
                </div>
  			</div>
        	</div>
      	</div>
       </div>
	</div>
    <?php } ?>
</div>
<div class="right"> 
  <div class="balls">
    <div class="ballInfo">
      <div class="ballList">
        <div style="" id="ball1">
		<?php $classinum = count($this->__muant["class"]); for($classi = 0; $classi<$classinum; $classi++) { ?>
			<?php if($this->__muant["class"]["$classi"]["cpid"]=='0') { ?><div><h5><a href="<?php echo $this->__muant["class"]["$classi"]["url"] ?>"><?php echo $this->__muant["class"]["$classi"]["name"] ?></a></h5><?php } else { ?><a href="<?php echo $this->__muant["class"]["$classi"]["url"] ?>"><?php echo $this->__muant["class"]["$classi"]["name"] ?></a><?php } ?><?php if($this->__muant["class"]["$classi"]["end"]==1) { ?></div><?php } ?>
		<?php } ?>
        </div>
	<div>
	 <b>在线客服</b>
        <?php $msninum = count($this->__muant["__Meta"]["msn"]); for($msni = 0; $msni<$msninum; $msni++) { ?><a href="msnim:chat?contact=<?php echo $this->__muant["__Meta"]["msn"]["$msni"] ?>" class="gy"><img src="images/default/msnlogo.gif" alt="MSN:<?php echo $this->__muant["__Meta"]["msn"]["$msni"] ?>" width="37" height="16" border="0" /></a><?php } ?>
        <?php $taobaoinum = count($this->__muant["__Meta"]["taobao"]); for($taobaoi = 0; $taobaoi<$taobaoinum; $taobaoi++) { ?><a class="gy" target="_blank" href="http://amos1.taobao.com/msg.ww?v=2&uid=<?php echo $this->__muant["__Meta"]["taobao"]["$taobaoi"] ?>&site=cntaobao&s=2" ><img border="0" src="http://amos1.taobao.com/online.ww?v=2&uid=<?php echo $this->__muant["__Meta"]["taobao"]["$taobaoi"] ?>&site=cntaobao&s=2" alt="taobao:<?php echo $this->__muant["__Meta"]["taobao"]["$taobaoi"] ?>" /></a><?php } ?>
        <?php $qqinum = count($this->__muant["__Meta"]["qq"]); for($qqi = 0; $qqi<$qqinum; $qqi++) { ?><a target="blank" href="tencent://message/?uin=<?php echo $this->__muant["__Meta"]["qq"]["$qqi"] ?>&amp;Site=<?php echo $this->__muant["__Meta"]["Url"] ?>&amp;Menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=1:<?php echo $this->__muant["__Meta"]["qq"]["$qqi"] ?>:9" alt="QQ:<?php echo $this->__muant["__Meta"]["qq"]["$qqi"] ?>" /></a><?php } ?>
	</div>
      </div>
       </div>
    <div class="ballBtm">
      <h2>Shopping with happy!</h2>
      <p>我们将给您最好的服务，订购电话：<?php echo $this->__muant["__Meta"]["web_phone"] ?> ; <?php echo $this->__muant["__Meta"]["web_free_mobile"] ?></p>
    </div>
  </div>
<div class="left_ad"><?php $value2inum = count($this->__muant["adinfo"]["value2"]); for($value2i = 0; $value2i<$value2inum; $value2i++) { ?><a href="<?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["url"] ?>"><img src="<?php echo $this->__muant["adinfo"]["value2"]["$value2i"]["pic"] ?>" border="0" onerror="this.style.display='none'" /></a><?php } ?></div>
<div class="left_new">
<div class="h5"></div>
<div id="container"><div class="mallLogin"> <span class="welcome">特别推荐</span></div>
    <div class="content">  
      <div class="compareList">
        <div>
            <div class="list"><?php if($this->__muant["specialProduct"]!='') { ?>      
                <div class="productList" style="width:245px;">
                  <ul>
                      <li style="padding-left:20px;">
                      <a href="<?php echo $this->__muant["specialProduct"]["url"] ?>" target="_blank" class="productImg" title="<?php echo $this->__muant["specialProduct"]["name"] ?>"><img src="<?php echo $this->__muant["specialProduct"]["m_pic"] ?>" alt="<?php echo $this->__muant["specialProduct"]["name"] ?>" onerror="this.src='product/nopic.gif'" /></a>
                      <p class="name"><A href="<?php echo $this->__muant["specialProduct"]["url"] ?>" target="_blank" title="<?php echo $this->__muant["specialProduct"]["name"] ?>"><?php echo $this->__muant["specialProduct"]["name"] ?></A></p>
                      <?php if($this->__muant["specialProduct"]["market_offer"]==1) { ?>
                      <p class="pricelabel"><STRIKE>市场价:&#165;<?php echo $this->__muant["specialProduct"]["price_market"] ?></STRIKE></p>
                      <?php } ?>
                      <p class="pricelabel">
                      售　价:<?php if($this->__muant["specialProduct"]["special_offer"]==1) { ?><STRIKE><?php } ?>
                      <font color="#FF0000"> &yen; </font><?php echo $this->__muant["specialProduct"]["price"] ?>
                      <?php if($this->__muant["specialProduct"]["special_offer"]==1) { ?></STRIKE><?php } ?>
                      </p>
                      <?php if($this->__muant["specialProduct"]["special_offer"]==1) { ?>
                      <p class="pricelabel">特　价:<span class="priceRed">&#165;<?php echo $this->__muant["specialProduct"]["price_special"] ?></span></p>
                      <?php } else { ?><?php if($this->__muant["specialProduct"]["member_offer"]==1) { ?>
                      <p class="pricelabel">会员价:<span class="priceRed">&#165;<?php echo $this->__muant["specialProduct"]["price_member"] ?></span></p>
                      <?php } ?><?php } ?>
                      </li>
                  </ul>
                </div><?php } ?>
  			</div>
            <div class="news_list">
            	<ul><?php $arrNewsinum = count($this->__muant["arrNews"]); for($arrNewsi = 0; $arrNewsi<$arrNewsinum; $arrNewsi++) { ?>
                	<li>&#8226; <a href="<?php echo $this->__muant["arrNews"]["$arrNewsi"]["url"] ?>" title="<?php echo $this->__muant["arrNews"]["$arrNewsi"]["title"] ?>" target="_blank"><?php echo $this->__muant["arrNews"]["$arrNewsi"]["shot_title"] ?></a></li>
                    <?php } ?>
                </ul>
            </div>
        </div>
      </div>
    </div>
</div>
<div class="right left_ad">
<?php $value4inum = count($this->__muant["adinfo"]["value4"]); for($value4i = 0; $value4i<$value4inum; $value4i++) { ?><a href="<?php echo $this->__muant["adinfo"]["value4"]["$value4i"]["url"] ?>"><img src="<?php echo $this->__muant["adinfo"]["value4"]["$value4i"]["pic"] ?>" border="0" onerror="this.style.display='none'" /></a><?php } ?>
  </div>
</div> 
</div>
<div id="online">
  
  <div class="clear"></div>
</div>
</div>
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body>
</html>