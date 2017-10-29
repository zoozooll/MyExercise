<?php if($this->__muant["product"]!='') { ?>
	<?php if($this->__muant["hotproduct"]!='') { ?>
	<div class="cl" style="height:3px;"></DIV>
	<?php } else { ?>
	<div class="cl"></div>
	<?php } ?>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="prolist">
		 <form action="./sidebyside.php" name="byside" method="post" target="_blank" >
			<input type="hidden" name="cid" value="<?php echo $this->__muant["ChannelID"] ?>">
		  <tr>
		    <td colspan="4" class="pbg ph" style="text-align:left;">
			<div  class="pageb" style="padding:5px 0 5px 0;">
			<input type="image" src="images/default/but_compare.gif" align="middle" />
				<span style="float:right;">
                显示方式：
                <a href="<?php echo $this->__muant["caturl"]["listurl"] ?>"><img src="images/default/p.gif" width="13" height="15" border="0" align="absmiddle" alt="栅格显示" /></a> 
				<a href="<?php echo $this->__muant["caturl"]["norurl"] ?>"><img src="images/default/l.gif" width="13" height="15" border="0" align="absmiddle" alt="列表显示" /></a>　
				排序方式：
                <a href="<?php echo $this->__muant["caturl"]["hoturl"] ?>"><img src="images/default/h.gif" width="13" height="15" border="0" align="absmiddle" alt="最热商品" /></a> 
				<a href="<?php echo $this->__muant["caturl"]["newurl"] ?>"><img src="images/default/n.gif" width="13" height="15" border="0" align="absmiddle" alt="最新商品" /></a>　
				每页显示数量：<a href="<?php echo $this->__muant["caturl"]["s_url"] ?>">12</a> | <a href="<?php echo $this->__muant["caturl"]["m_url"] ?>">30</a>
                </span>		    
			 </div>
            </td>
	       </tr>
	
		  <tr style="height:1px;">
		    <td width="20" style="background:#c8dcb9;"></td>
		    <td colspan="3" class="bggrey" style="background:#c8dcb9;"></td>
	       </tr>
		  <tr>
		    <td colspan="4"  style="text-align:left; border-left:1px solid #eeeeee;">
		<?php $productinum = count($this->__muant["product"]); for($producti = 0; $producti<$productinum; $producti++) { ?>	
		<div class="clpro">
			<div class="proimg"><a href="<?php echo $this->__muant["product"]["$producti"]["url"] ?>" target="_blank"><img src="<?php echo $this->__muant["product"]["$producti"]["m_pic"] ?>" alt="<?php echo $this->__muant["product"]["$producti"]["name"] ?>" onerror="this.src='product/nopic.gif'" /></a>
			</div>
			<div id="product_name"><a href="<?php echo $this->__muant["product"]["$producti"]["url"] ?>" class="green b"><?php echo $this->__muant["product"]["$producti"]["name"] ?></a></div>
			<div id="proprice">
			<?php if($this->__muant["product"]["$producti"]["market_offer"]==1) { ?><div id="price_l"><STRIKE>市场价</STRIKE></div>
			<div id="price_r"><STRIKE> &yen; <?php echo $this->__muant["product"]["$producti"]["price_market"] ?></STRIKE></div><?php } ?>
			<div id="price_l">售　价</div><div id="price_r"><?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?><STRIKE><?php } ?><font color="#FF0000"> &yen; </font><?php echo $this->__muant["product"]["$producti"]["price"] ?><?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?></STRIKE><?php } ?></div>
			<?php if($this->__muant["product"]["$producti"]["special_offer"]==1) { ?>
			<div id="price_l">特　价</div><div id="price_r"><font color="#FF0000"> &yen; <?php echo $this->__muant["product"]["$producti"]["price_special"] ?></font></div>
			<?php } else { ?><?php if($this->__muant["product"]["$producti"]["member_offer"]==1) { ?><div id="price_l">会员价</div><div id="price_r"><font color="#FF0000"> &yen; 
			<?php echo $this->__muant["product"]["$producti"]["price_member"] ?></font></div><?php } ?>
			<?php } ?>
			<div class="cl"></div>
			</div>
			<div class="clbut">
			<?php if($this->__muant["product"]["$producti"]["store"]>0) { ?><a href="#shop" onclick="setUserShopFrame('<?php echo $this->__muant["product"]["$producti"]["id"] ?>');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/ban_product.gif" border="0" alt="点击购买" /></a><?php } else { ?><img src="images/default/no_ban_product.gif" alt="暂时缺货" border="0" /><?php } ?>
			</div>
		</div>
		<?php } ?>
			  </td>
	       </tr>
		  <tr>
		    <td colspan="4" class="pbg ph">
			<div class="pageb" style="padding:5px 0 5px 0;">
			  <input type="image" src="images/default/but_compare.gif" align="middle" />
			</div>
            </td>
	       </tr>
		 </form>
	  </table>
      <div class="pages" id="pages_pg_2" style="text-align:right;">
            <span class="count">Pages: <?php echo $this->__muant["pn"] ?> / <?php echo $this->__muant["allpage"] ?></span>
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
              <?php $pageinum = count($this->__muant["page"]); for($pagei = 0; $pagei<$pageinum; $pagei++) { ?>
               <?php if($this->__muant["page"]["$pagei"]["pn"]==$this->__muant["pn"]) { ?>
                <span class="current" title="Page"><?php echo $this->__muant["page"]["$pagei"]["pn"] ?></span>
               <?php } else { ?>
                <a href="<?php echo $this->__muant["page"]["$pagei"]["url"] ?>"><?php echo $this->__muant["page"]["$pagei"]["pn"] ?></a>
               <?php } ?>
              <?php } ?>
              <?php if($this->__muant["pn"]==$this->__muant["allpage"]) { ?>
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
<?php } else { ?>
	<b>无商品</b>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td class="pbg ph">&nbsp;</td>
	  </tr>
	  <tr>
		<td height="150" align="center" class="pd prbo"><span style="text-align:center">无搜索结果，请修改您的检索条件并重新搜索。</span></td>
	  </tr>
	  <tr>
		<td class="pbg ph">&nbsp;</td>
	  </tr>
</table>
<?php } ?>