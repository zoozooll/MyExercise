<div id="top">
    <div id="logo"><a href="./" title="<?php echo $this->__muant["__Meta"]["Title"] ?>"><img src="<?php echo $this->__muant["__Meta"]["Logo"] ?>" alt="<?php echo $this->__muant["__Meta"]["Title"] ?>" border="0" /></a></div>
    <div id="nav">
      <div id="quickLinks">
          <span class="quickInner">
          
          <span id="headerLoginNo">
          <?php if($this->__muant["userinfo"]["name"]=='') { ?><a href="register.php"class="registing">注册</a> <a href="login.php" class="loging">登录</a> <?php } ?>
          </span>
          <a href="myhome.php" title="用户中心" class="myhome"><?php if($this->__muant["userinfo"]["name"]!='') { ?><?php echo $this->__muant["userinfo"]["name"] ?><?php } else { ?>用户中心<?php } ?></a>
          <?php if($this->__muant["userinfo"]["name"]!='') { ?> <a href="./logout.php">[注销]</a><?php } ?>
          <a href="#shop" onclick="setUserShopFrame('');myshow('usershop');callLoad('loadingiframe');" title="我的购物车" class="shopping">我的购物车</a>
          <a href="buy.php" class="car">柜台结帐</a>
          </span>
      </div>
      <div id="menu">
        <ul>
          <li<?php if($this->__muant["menu_nav"]=='homepage') { ?> id="home"<?php } ?>><a href="./index.php" title="商品首页">商品首页<span>商品首页</span></a></li>
          <li<?php if($this->__muant["menu_nav"]=='classes') { ?> id="home"<?php } ?>><a href="./classes.php" title="商品类别">商品类别<span>商品类别</span></a></li>
          <li<?php if($this->__muant["menu_nav"]=='newproduct') { ?> id="home"<?php } ?>><a href="./newproduct.php" title="最新商品">最新商品<span>最新商品</span></a></li>
          <li<?php if($this->__muant["menu_nav"]=='hotproduct') { ?> id="home"<?php } ?>><a href="./hotproduct.php" title="最热商品">最热商品<span>最热商品</span></a></li>
          <li<?php if($this->__muant["menu_nav"]=='specialoffer') { ?> id="home"<?php } ?>><a href="./specialoffer.php" title="特价商品">特价商品<span>特价商品</span></a></li>
          <li<?php if($this->__muant["menu_nav"]=='productnews') { ?> id="home"<?php } ?>><a href="./productnews.php" title="商品新闻">商品新闻<span>商品新闻</span></a></li>
        </ul>
      </div>
    </div>
</div>
<div id="search">
      <div id="searchBox">
        <form action="search.php" method="post" name="cscsearch">
          <div id="selecter">
            <div class="mySelect" id="selectDiv">&nbsp;填入关键字搜索您感兴趣的商品</div>
          </div>
          
          
          <div id="keyword">
            <input name="se" id="search_name" value="请输入关键字"  type="text" onfocus="if(this.value=='请输入关键字') this.value=''; this.style.color='#000';" 
	onblur="
		if(this.value=='') {
			this.value='请输入关键字';
			this.style.color='#CCC';
		}else{
			this.style.color='#000';
		}"
>
          </div>
          <div id="searchBtn">
            <button type="submit" title="折扣商品搜索" onmouseover="this.className='searchBt2'" onmouseout="this.className='searchBt'" class="searchBt"></button>
            <button type="button" title="商品分类" onmouseover="this.className='cateBt2'" onmouseout="this.className='cateBt'" onclick="location.href='classes.php'" class="cateBt"></button>
          </div>
        </form>
      </div>
      <div class="" id="hotKeywords">
        <ul>
          <li></li>
          <li></li>
        </ul>
      </div>
</div>