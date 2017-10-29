<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<style type="text/css">
#divClassUB{ padding:5px; margin-left:10px; font-size:14px;}
#divUserHome{ display:none;}
</style>
</head>
<body id="index">
<div id="wrapper">
    <!--head -->
    {inc file=inc/#body_top.tpl}
	<div id="locate"><span id="topnav"><a href="./">首页</a></span> </div>
    <!-- -->
    <div id="container"><div class='mallLogin'> <span class='welcome'>{$__Meta.Site}</span></div>
        <div class="content">  
              <div class="compareList">
                <ul class="shoppingTab" style="width:945px;">
                  <li class="cur" id="tab1">{$__Meta.Site}</li>
                </ul>
                <div id="con1">
                    <div style="float:left; width:250px;">
                       <div id="divClassUB"><a href="./faq.php" class="gy">什么是{$__Meta.Site}</a></div>
                       <div id="divClassUB"><a href="./link.php" class="gy">友情链接</a></div>
                       <!--<div id="divClassUB"><a href="./product/sitemap/sitemap.html" class="gy">站点地图</a></div>-->
                       <div id="divClassUB"><a href="./contactus.php" class="gy">联系我们</a></div>
                       <div id="divUserHome">购物相关</div> 
                       <div id="divClassUB"><a href="./order.php" class="gy">订购方式</a></div>
                       <div id="divClassUB"><a href="./deliver.php" class="gy">送货方式</a></div>
                       <div id="divClassUB"><a href="./paymethod.php" class="gy">付款方式</a></div>
                       <div id="divClassUB"><a href="./service.php" class="gy">服务保证</a></div>
                    </div>
                    <div style="float:left; width:650px;">       
                        {$shop_about_all}
                    </div>
              </div>
            </div>
        </div>    
    <!-- -->
    </div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>
