<style type="text/css" media="all">
#con1 table td{ border-bottom:0px dotted #dfdfdf; padding:0px 0px; text-align:center}
ul.tab{width:944px;}
#con1 table {width:954px;}
.product_safe{border:1px solid #E4E4E4; background-color:#F7F7F7; color:#999; padding:0 5px 0 5px; margin-top:10px; float:right; width:366px; height:30px;}
.minh300{min-height:180px;}
.more_img img {width:50xp; height:35px; width:35px; border:1px solid #EBEBEB; padding:1px; cursor:pointer; margin-top:10px;}
.more_img ul{ list-style:none; text-align:left;}
.prod_img{text-align:center;}
</style>
<div class="mallLogin"> <div class="welcome" style="float:left; width:748px; height:24px; overflow:hidden;">{$product.name}的详细信息，{$product.name}的价格，{$product.name}的购买途径</div> &nbsp;&nbsp;&nbsp;&nbsp;  <span class="welcome">下一个产品</span></div>
<div class="content">
  <div class="compareList">
    <div style="padding: 10px; width:748px; float:left;">
      <div><!-- onmouseover="showRegisterTip(true);" onmouseout="showRegisterTip(false); -->
        <div class="compareImg minh300 prod_img"><a href="{$product.src_pic}" class="MagicThumb" ><img src="{$product.b_pic}" title="点击查看大图 {$product.name}" onerror="this.src='product/nopic.gif';this.parentNode.href='product/nopic.gif'" border="0" alt="点击查看大图 {$product.name}" /></a>
        <div class="more_img"><ul><li><img src="{$product.b_pic}" onerror="this.src='product/nopic.gif'" /></li></ul></div>
        </div>
        <div class="compareInfo">
          <h1>{$product.name}</h1>
          {if brand!=NULL}
		  <p><a href='{$brand.url}' class='green'>所属品牌 <font color="#FF0000">{$brand.name}</font></a></p>
		  {/if}
          {if product.market_offer==1}<p><STRIKE>市场价：&yen; {$product.price_market}</STRIKE></p>{/if}
		  <p>售　价：{if product.special_offer==1}<STRIKE>{/if}<font color="#FF0000">&yen;</font> {$product.price}{if product.special_offer==1}</STRIKE>{/if}</p>
		  {if product.special_offer==1}<p>特　价：<span class="price24">&#165; {$product.price_special}</span></p>{else}
		  {if product.member_offer==1}<p>会员价：<span class="price24">&#165; {$product.price_member}</span></p>{/if}{/if}
          <p><!--商品热度：		商品购买度：-->{if product.online!='0'}{if product.store>0}<a href="#shop" onclick="setUserShopFrame('{$product.id}');myshow('usershop');callLoad('loadingiframe');"><img src="images/default/buy.gif" border="0" alt="点击购买" /></a>{else}<img src="images/default/nobuy.gif" border="0" alt="暂时缺货" />{/if}{/if}</p>
          <p class="product_safe">
          卖家承诺：<img src="images/default/xin.jpg" align="absmiddle" />诚信保障  <img src="images/default/7day.jpg" align="absmiddle" />7天退换货 {if usealipay==1}<img src="images/default/icon_alipay_16x16_v2.gif" align="absmiddle" />支付宝担保交易(安全){/if}
          </p>
          <p>商品浏览量：{$product.hit_times}</p>
          {if product.online=='0'}
		  <p><span style="color:#FF0000">此产品已经下线！</span></p>
		  {/if}
          </div>
      </div>
    </div>
    <div class="clpro" style="float:left; border-left: #DFDFDF solid 1px; border-right:none;">{if arrNextProduct!=''}
		  <div class="proimg"><a target="_blank" href="{$arrNextProduct.url}"><img onerror="this.src='product/nopic.gif'" alt="{$arrNextProduct.name}" src="{$arrNextProduct.m_pic}"></a>
		  </div>
		  <div id="product_name"><a class="green b" href="{$arrNextProduct.url}">{$arrNextProduct.name}</a></div>
		  <div id="proprice">
		  </div>
		  <div class="clbut">
		  <a href="{$arrNextProduct.url}" target="_blank"><img border="0" src="images/default/look.gif"></a>
          </div>{/if}
	</div>
    <div class="clear"></div>
    <div>
    {if product.online=='0'}
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;此产品暂无信息。谢谢！
    {else}
<script language="javascript">
function setTabProduct(id) {
	for(var i = 1; i < 4; i++) {
		displayDiv('con'+i, true);
		changeClass('tab'+i, '');
	}
	if(id == 3) {displayDiv('con1', false);displayDiv('con2', false);}
	if(id == 2) {displayDiv('con1', false);}
	displayDiv('con'+id, true);
	changeClass('tab'+id, 'cur');
}
</script>
      <ul class="tab">
        <li class="cur" id="tab1" onclick="setTabProduct('1');">商品详情</li>
        <li class="" id="tab2" onclick="setTabProduct('2');">商品参数</li>
        <li class="" id="tab3" onclick="setTabProduct('3');">用户评价</li>
      </ul>
      <div id="con1">
        <table cellpadding="0" cellspacing="0">
          <tbody><tr>
            <th height="0">&nbsp;</th>
            </tr>
          <tr>
            <td style="text-align:left; font-size:14px;padding:10px;">
            {$product.describes}
            </td>
          </tr>
        </tbody></table>
      </div>
      <div id="con2">
      <p class="pcontent"></p>
      <table cellpadding="0" cellspacing="0">
      <tbody>
        <tr><th class="leftBorder" width="130">属性</th><th>参数</th></tr>
        {$loop productspec}
        {if productspec.loop.title_name!=NULL}<tr><th>{$productspec.loop.title_name}</th><td></td></tr>
        {else}
        <tr><td>{$productspec.loop.name}</td><td>{$productspec.loop.value}</td></tr>
        {/if}
        {/loop}
      </tbody></table> 
      </div>
      <div class="h10"></div>
      <div id="con3">
      <p class="pcontent"></p><!--
      <table cellpadding="0" cellspacing="0">
      <tbody><tr><th class="leftBorder" width="130">类别</th><th>分数</th></tr>
        <tr><td>好评：</td><td></td></tr>
        <tr><td>中评：</td><td></td></tr>
        <tr><td>差评：</td><td></td></tr>
      </tbody></table>-->
      <table cellpadding="0" cellspacing="0">
      <tbody><tr><th class="leftBorder" width="130">用户评论</th><th>评论内容</th></tr>
      {$loop arrReview}
        <tr>
        	<td rowspan="5">订单号：{$arrReview.loop.str_billno}***** <br />用户评价<br /><font color="#CCCCCC">({$arrReview.loop.add_date})</font></td>
            <td>平分：<img src="images/beauty/prg_00{$arrReview.loop.point}.jpg" /></td>
        </tr>
        <tr>
        	 <td>标题：{$arrReview.loop.title}</td>
        </tr>
        <tr>
        	 <td>优点：{$arrReview.loop.good}</td>
        </tr>
        <tr>
        	<td>不足：{$arrReview.loop.bad}</td>
        </tr>
        <tr>
        	<td>总结：{$arrReview.loop.contents}</td>
        </tr>
      {/loop}
      <tr><td></td><td></td></tr>
      </tbody></table>
      <table cellpadding="0" cellspacing="0" id="write_review" style="display:">
      <tbody>
      <form name="review" action="product.php?switch=review&pid={$product.id}" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return checkData();">
      	<tr><th class="leftBorder" width="130" height="0">参与评价</th><th>对{$product.name}的评论</th></tr>
        <tr><td >标题:</td><td><input name="title" type="text" size="30" /></td></tr>
        <tr><td >平分:</td><td><input type="radio" name="point" value="1" />1分 <input type="radio" name="point" value="2" />2分
         <input type="radio" name="point" value="3" />3分 <input type="radio" name="point" value="1" />4分 <input name="point" type="radio" value="5" checked="checked" />5分</td></tr>
        <tr><td >订单号:</td><td><input name="billno" type="text" /></td></tr>
        <tr><td >优点:</td><td><textarea name="good" cols="50" rows="2"></textarea></td></tr>
        <tr><td >不足:</td><td><textarea name="bad" cols="50" rows="2">暂时没发现。</textarea></td></tr>
        <tr><td >总结:</td><td><textarea name="contents" cols="50" rows="2"></textarea></td></tr>
        <tr><td ></td><td><input name="" type="submit" value="提交" /></td></tr>
       </form>
      </tbody></table>
      </div>
     <div class="rightBar">
        <div class="ader">
        </div>
        <!-- {inc file=#productnew.tpl} -->
      </div>
    </div>
    {/if}
   </div>
</div>
<link rel="stylesheet" href="css/beauty/magicthumb.css" type="text/css" media="screen, projection"/>
<script type="text/javascript" src="jscript/beauty/magicthumb-packed.js"></script>
<script type="text/javascript">
	MagicThumb.options = {
		keepThumbnail: true,
		zoomPosition: 'center'
	}
</script>
<script language="javascript" src="jscript/comm/simpleajax.js"></script>
<script language="javascript">
function showRegisterTip(show) {
	var addslide = document.getElementById('info');
	if(show == true) {
		document.onmousemove = mouseMove;
		addslide.style.display = '';
	} else {
		addslide.style.display = 'none';
		document.onmousemove = null;
	}
} 
function checkData() {
	var form = document.review;
	if(form.title.value =='') {
		alert('标题不能为空！');
		return false;	
	}
	if(form.good.value =='' && form.bad.value =='') {
		alert('优点和不足至少填1个！');
		return false;	
	}
	if(form.billno.value =='') {
		alert('订单号不能为空！');
		return false;	
	}
	if(form.title.value.length > 50) {
		alert('标题只能在50个字符或者25个汉字以内！');
		return false;
	}
	if(form.good.value.length > 250) {
		alert('优点只能在250个字符或者125个汉字以内！');
		return false;
	}
	if(form.bad.value.length > 250) {
		alert('不足只能在250个字符或者125个汉字以内！');
		return false;
	}
	if(form.contents.value.length > 250) {
		alert('总结只能在250个字符或者125个汉字以内！');
		return false;
	}
}
</script>