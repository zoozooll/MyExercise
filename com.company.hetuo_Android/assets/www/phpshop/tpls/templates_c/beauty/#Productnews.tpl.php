<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
<?php include("inc/#head_common.tpl.php") ?>
</head>
<body id="index">
<div id="wrapper">
    <?php include("inc/#body_top.tpl.php") ?>
    <div id="locate"><div id="ps"><span id="topnav"><a href="./">首页</a> &raquo; 新闻 </span> </div></div>
    <!-- -->
	<div id="container"><div class='mallLogin'> <span class='welcome'>最新的商品新闻，最关注的商品新闻</span></div>
    <div class="content">  
      <div class="compareList">
        <ul class="shoppingTab" style="width:945px;">
          <li class="cur" id="tab1">商品新闻</li>
        </ul>
        <div id="con1">
        	<div style="padding:20px;">
            <div class="news_list">
            	<ul><?php $newsinum = count($this->__muant["news"]); for($newsi = 0; $newsi<$newsinum; $newsi++) { ?>
                	<li>&#8226; <a href="<?php echo $this->__muant["news"]["$newsi"]["url"] ?>" target="_blank"><?php echo $this->__muant["news"]["$newsi"]["title"] ?></a> <?php echo $this->__muant["news"]["$newsi"]["add_date"] ?></li>
                    <?php if($this->__muant["news"]["$newsi"]["br"]==1) { ?><h2><a></a></h2><br /><?php } ?>
                    <?php } ?>
                </ul>
            </div>
            <div>
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
        </div>
      </div>
    </div>
</div>
	<!-- -->
<?php include("inc/#body_bottom.tpl.php") ?>
</div>
</body></html>