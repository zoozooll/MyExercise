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
        <a href="./faq.php" class="gy">什么是{$__Meta.Site}</a>
        <b>在线客服</b>
        {$loop __Meta.msn}<a href="msnim:chat?contact={$__Meta.msn.loop}" class="gy"><img src="images/default/msnlogo.gif" alt="MSN:{$__Meta.msn.loop}" width="37" height="16" border="0" /></a>{/loop}
        {$loop __Meta.taobao}<a class="gy" target="_blank" href="http://amos1.taobao.com/msg.ww?v=2&uid={$__Meta.taobao.loop}&site=cntaobao&s=2" ><img border="0" src="http://amos1.taobao.com/online.ww?v=2&uid={$__Meta.taobao.loop}&site=cntaobao&s=2" alt="taobao:{$__Meta.taobao.loop}" /></a>{/loop}
        {$loop __Meta.qq}<a target="blank" href="tencent://message/?uin={$__Meta.qq.loop}&amp;Site={$__Meta.Url}&amp;Menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=1:{$__Meta.qq.loop}:9" alt="QQ:{$__Meta.qq.loop}" /></a>{/loop}
		服务电话：{$__Meta.web_phone}
    </div>
    <div class="copyright">{$__Meta.Copyright} {$__Meta.Icp} 电话：{$__Meta.web_phone} {$__Meta.web_free_mobile}　　 地址： {$__Meta.web_address}</div>
    {$__Meta.Stat}
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
				 &#8226; 您订购的资料如下：您可选择 <b>柜台结帐</b> 进行下一步，如果下单有问题可使用<a href="mailto:{$__Meta.web_email}">E-mail</a>下单   
				</div><span id="loadingiframe"></span>
				<div id="iframeshop" style="height:378px; margin:0px; padding:0px;"></div>
			</div>
		</div>
		<div id="check_out" style="height:30px; margin:0px; padding:0px; text-align:center;">
		<a href="buy.php"><img src="images/default/icostore.gif" alt="柜台结帐" width="79" height="27" border="0" /></a>
		<img src="images/default/icoreg.gif" style="cursor:pointer;" alt="继续采购" width="79" height="27" border="0" onclick="dispalymyshow('usershop')" />		</div>
	</div>
</div>