<div id="footer">
  <div class="flogo"><a href="http://www.phpshop.cn" target="_blank" title="返回首页"><img src="images/beauty/powerby.phpshop.cn.gif" title="power by www.phpshop.cn"></a>
  </div>
  <div class="fright">
  	<div class="flink">
        <a href="./link.php" class="gy">友情链接</a>
        <!--<a href="./product/sitemap/sitemap.html" class="gy">站点地图</a>-->
        <a href="./contactus.php" class="gy">联系我们</a>
        <a href="./order.php" class="gy">订购方式</a>
        <a href="./deliver.php" class="gy">送货方式</a>
        <a href="./paymethod.php" class="gy">付款方式</a>
        <a href="./service.php" class="gy">服务保证</a>
        <a href="#" class="gotop"><img src="images/beauty/ico_top.gif" title="返回顶部"></a>
        <br />
        <a href="./faq.php" class="gy">什么是<?php echo $this->__muant["__Meta"]["Site"] ?></a>
        <b>在线客服</b>
        <?php $msninum = count($this->__muant["__Meta"]["msn"]); for($msni = 0; $msni<$msninum; $msni++) { ?><a href="msnim:chat?contact=<?php echo $this->__muant["__Meta"]["msn"]["$msni"] ?>" class="gy"><img src="images/default/msnlogo.gif" alt="MSN:<?php echo $this->__muant["__Meta"]["msn"]["$msni"] ?>" width="37" height="16" border="0" /></a><?php } ?>
        <?php $taobaoinum = count($this->__muant["__Meta"]["taobao"]); for($taobaoi = 0; $taobaoi<$taobaoinum; $taobaoi++) { ?><a class="gy" target="_blank" href="http://amos1.taobao.com/msg.ww?v=2&uid=<?php echo $this->__muant["__Meta"]["taobao"]["$taobaoi"] ?>&site=cntaobao&s=2" ><img border="0" src="http://amos1.taobao.com/online.ww?v=2&uid=<?php echo $this->__muant["__Meta"]["taobao"]["$taobaoi"] ?>&site=cntaobao&s=2" alt="taobao:<?php echo $this->__muant["__Meta"]["taobao"]["$taobaoi"] ?>" /></a><?php } ?>
        <?php $qqinum = count($this->__muant["__Meta"]["qq"]); for($qqi = 0; $qqi<$qqinum; $qqi++) { ?><a target="blank" href="tencent://message/?uin=<?php echo $this->__muant["__Meta"]["qq"]["$qqi"] ?>&amp;Site=<?php echo $this->__muant["__Meta"]["Url"] ?>&amp;Menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=1:<?php echo $this->__muant["__Meta"]["qq"]["$qqi"] ?>:9" alt="QQ:<?php echo $this->__muant["__Meta"]["qq"]["$qqi"] ?>" /></a><?php } ?>
		服务电话：<?php echo $this->__muant["__Meta"]["web_phone"] ?>
    </div>
    <div class="copyright"><?php echo $this->__muant["__Meta"]["Copyright"] ?> <?php echo $this->__muant["__Meta"]["Icp"] ?> 电话：<?php echo $this->__muant["__Meta"]["web_phone"] ?> <?php echo $this->__muant["__Meta"]["web_free_mobile"] ?>　　 地址： <?php echo $this->__muant["__Meta"]["web_address"] ?></div>
    <?php echo $this->__muant["__Meta"]["Stat"] ?>
  </div>
</div>
<div id="usershop" style="display:none;position:absolute; width:834px; height:510px; margin:0px; padding:0px;" onmouseover="drag(this ,this);">
	<div id="s_content">
		<div id="s_nav" style=" cursor:move;">
			<ul>
				<li id="n1" class="s_nav_cur">购物车</li>
				<li id="n2"></li>
			</ul>
			<div id="close"><img src="images/default/close_win.gif" border="0" alt="关闭窗口" onclick="dispalymyshow('usershop')" /></div>
		</div>
		<div id="c3" class="s_div">
			<div class="decss tags_css">
				<div id="ordersuccess">
				 &#8226; 您订购的资料如下：您可选择 <b>柜台结帐</b> 进行下一步，如果下单有问题可使用<a href="mailto:<?php echo $this->__muant["__Meta"]["web_email"] ?>">E-mail</a>下单   
				</div><span id="loadingiframe"></span>
				<div id="iframeshop" style="height:378px; margin:0px; padding:0px;"></div>
			</div>
		</div>
		<div id="check_out" style="height:30px; margin:0px; padding:0px; text-align:center;">
		<a href="buy.php"><img src="images/default/icostore.gif" alt="柜台结帐" width="79" height="27" border="0" /></a>
		<img src="images/default/icoreg.gif" style="cursor:pointer;" alt="继续采购" width="79" height="27" border="0" onclick="dispalymyshow('usershop')" />		</div>
	</div>
</div>