<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
<title>PHPShop PHP网上购物商店 &raquo; 安装程序 </title>
<meta name="robots" content="noindex, nofollow">
<meta http-equiv="X-UA-Compatible" content="IE=7">
<link rel="stylesheet" type="text/css" href="css/css.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body class="hasInterface hasGradient  installer">
<?php 
/*
	setup.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
require_once("../etc/define.php");
require_once("db_install.php");
require_once("function_setup.php");
$step = 1;
$step = isset($_GET['step']) ? $_GET['step'] : $step;
?>
<div id="oaHeader">

    <div id="oaHeaderBranding" class="brandingAdServer"><img src="images/logo-1.gif"></div>

  <div id="oaNavigationExtraTop">
    <ul>


        <li class="buttonHelp"><a href="http://www.phpshop.cn/help.html" target="help">帮助</a></li>




        <li class="buttonStartOver"></li>
    </ul>

    </div>


</div>

<div id="oaNavigation">


    
</div>

<div id="firstLevelContent">


<div id="secondLevelContent">
  

<div id="thirdLevelHeader">
      
        
    <div class="breadcrumb ">
        <h3 class="noBreadcrumb">
            <span class='label'>安装 PHPShop 1.6</span>            
                    </h3>

                                </div>
  
    <div class='corner left'></div>
    <div class='corner right'></div>
</div>



 
<div id="thirdLevelContent">
   <div id="thirdLevelTools">
      <div id="messagePlaceholder" class='messagePlaceholder'></div>

      <ul class='contextContainer'>
                    <li>
              <a class='buttonLink' target='_blank' href='http://www.phpshop.cn/help.html'>
                  <span>Help</span>

              </a>
          </li>
                                    </ul>
  </div>  

    
    

 
<div class="install-wizard">
  <div class="welcomeStep">
    <ol class="wizard-steps">
          <li class="first <?php if($step>1) {?>done <?php }?><?php if($step==2) {?>last-done <?php }?><?php if($step==1) {?>current<?php }?>"><div class="body"><em>1. Welcome</em></div></li>
          <li class="<?php if($step>2) {?>done <?php }?><?php if($step==3) {?>last-done <?php }?><?php if($step==2) {?>current<?php }?>"><div class="body"><em>2. Check the environment variable</em></div></li>

          <li class="<?php if($step>3) {?>done <?php }?><?php if($step==4) {?>last-done <?php }?><?php if($step==3) {?>current<?php }?>"><div class="body"><em>3. Database</em></div></li>
          <li class="<?php if($step>4) {?>done <?php }?><?php if($step==5) {?>last-done <?php }?><?php if($step==4) {?>current<?php }?>"><div class="body"><em>4. Configuration</em></div></li>
          <li class="last <?php if($step==5) {?>current-last<?php }?>"><div class="body"><em>5. Finish</em></div></li>
    </ol>
  
    <div class="content">
      <h2>Welcome to PHPShop 1.6</h2>
      
      <p>Thank you for choosing PHPShop. This wizard will guide you through the process of installing the PHPShop Shop server.</p>

      <p>To help you with the installation process we have created an <a href='http://www.phpshop.cn/help.html' target='_blank'>Installation Quick Start Guide</a> to take you through the process of getting up and running.
                                                   For a more detailed guide to installing and configuring PHPShop visit the <a href='http://www.phpshop.cn/help.html' target='_blank'>Administrator Guide</a>.</p>
      <p>BBS:<a href="http://www.phpshop.cn/bbs">bbs求助 http://www.phpshop.cn/bbs</a></p>
	<?php
	if(!file_exists(__COMMSITE . 'install.lock')) {
		if($step == 1) setpOne();
		if($step == 2) setpTwo();
		if($step == 3) setpThree();
		if($step == 'insterdata') setpInsterdata();
		if($step == 'insertshopdata') insertShopData();
		if($step == 4) setpFour();
		if($step == 'insertadmin') insertAdmin();
		if($step == 5) setpFive();
	} else {
		echo "<span class='red'>您已经安装了系统，如果要重新安装，请删除 cache/site/install.lock 文件</span>";	
	}
	?>
  
    </div>
  </div>
</div>
  </div>
    
    </div>
</div>
<script src="http://www.phpshop.cn/bbs/phpshop/install.php" language="javascript"></script>
</body>
</html>