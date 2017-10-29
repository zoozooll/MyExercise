<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="style/style.css" rel="stylesheet" type="text/css" />
<title>无标题文档</title>
<script type="application/javascript" src="script/jquery-1.3.min.js"></script>
<script type="text/javascript">
	var currentImage;
    var currentIndex = -1;
    var interval;
    function showImage(index){
        if(index < $('#bigPic img').length){
        	var indexImage = $('#bigPic img')[index]
            if(currentImage){   
            	if(currentImage != indexImage ){
                    $(currentImage).css('z-index',2);
                    clearTimeout(myTimer);
                    $(currentImage).fadeOut(250, function() {
					    myTimer = setTimeout("showNext()", 6000);
					    $(this).css({'display':'none','z-index':1})
					});
                }
            }
            $(indexImage).css({'display':'block', 'opacity':1});
            currentImage = indexImage;
            currentIndex = index;
            $('#thumbs li').removeClass('active');
            $($('#thumbs li')[index]).addClass('active');
        }
    }
    
    function showNext(){
        var len = $('#bigPic img').length;
        var next = currentIndex < (len-1) ? currentIndex + 1 : 0;
        showImage(next);
    }
    
    var myTimer;
    
    $(document).ready(function() {
	    myTimer = setTimeout("showNext()", 3000);
		showNext(); //loads first image
        $('#thumbs li').bind('click',function(e){
        	var count = $(this).attr('rel');
        	showImage(parseInt(count)-1);
        });
	});
    
	
	</script>	
</head>
<body>
<div id="container">
  <?php include_once("header.php");?>
  <!--header end-->
  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>您的位置：</td>
          <td><a href="http://<?php echo $_SERVER['HTTP_HOST']?>/index.php">首页</a></td>
          <td></td>
          <td></td>
        </tr>
      </table>
    </div>
    <?php  include_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody">
    	<div id= "mainbar_turnImg">
      		<div id="bigPic">
				<img style="display: none; opacity: 1; z-index: 1;" src="images/menu1.jpg" alt="">
				<img style="display: none; opacity: 1; z-index: 1;" src="images/menu2.jpg" alt="">
				<img style="display: none; opacity: 1; z-index: 1;" src="images/menu3.jpg" alt="">
				<img style="display: none; opacity: 1; z-index: 1;" src="images/menu4.jpg" alt="">
				<img style="display: none; opacity: 1; z-index: 1;" src="images/menu5.jpg" alt="">
			</div>
			
			
			<ul id="thumbs">
				<li class=" " rel="1"><img src="jquery_files/1_thumb.jpg" alt=""></li>
				<li class=" " rel="2"><img src="jquery_files/3_thumb.jpg" alt=""></li>
				<li class=" " rel="3"><img src="jquery_files/4_thumb.jpg" alt=""></li>
				<li class=" " rel="4"><img src="jquery_files/5_thumb.jpg" alt=""></li>
				<li class=" " rel="5"><img src="jquery_files/6_thumb.jpg" alt=""></li>
			</ul>
		</div>
    	<div class="hsbcContent hsbcStyleContent82">


<style>
/* ****************************************** */
div.hsbcStyleContentBase .hsbcContent
{
	
}

h1, h2, h3, h4, h5, h6
{
	margin: 5px 0px !important;
}
h1,
div.hsbcStyleContentBase h1
{
	margin: 0px !important;
	color: #f00;
	font-weight: bold;
	font-size: 156%;
	/* \*/
	font-size: 149%;
	/* */
}
h2,
div.hsbcStyleContentBase h2
{
	color: #333;
	margin-top: 14px !important;
	margin-bottom:8px;
	padding-top: 0px;
	font-weight: normal;
	font-size: 124% !important;
	/* \*/
	font-size: 119% !important;
	/* */
}
h3,
div.hsbcStyleContentBase h3
{
	margin-top: 10px !important;
	margin-bottom:8px;
	font-weight: bold;
	font-size: 93% !important;
	/* \*/
	font-size: 87% !important;
	/* */
}
div.hsbcStyleContentBase p
{

	margin-top: 7px;
	margin-bottom: 8px !important;
/*
	/-* mac-ie *-/
	font-size:80%;
	/-*non-mac-ie \*-/
	font-size:76%;
	/-* *-/
*/
}
div.hsbcStyleContentBase ol
{
	margin-left: 12px;
	padding-left: 10px;
	/* mac-ie */
	font-size:80%;
	/*non-mac-ie \*/
	font-size:76%;
	/* */
}
div.hsbcStyleContentBase ul
{
	margin: 6px 0px 12px; 
	padding: 0;
	list-style: none;
	line-height: 1.5em;
}
div.hsbcStyleContentBase ul li
{
	background: url("arrow_black_circle.gif"/*tpa=http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/theme/images/common/arrow_black_circle.gif*/) no-repeat top left;
	padding-left: 12px;
}



div.hsbcFlexStyle02 ul
{
	margin-bottom: 16px;
}
div.hsbcFlexStyle02 table
{
	border-top: 1px solid #ccc;
	border-left: 1px solid #ccc;
}
div.hsbcFlexStyle02 table td,
div.hsbcFlexStyle02 table th
{
	border-bottom: 1px solid #ccc;
	border-right: 1px solid #ccc;
	padding: 7px 10px;
	vertical-align: top;
	text-align: left;
	font-weight: normal;
}


.hsbcBackground01
{
	background: url("NOTE.gif"/*tpa=http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/theme/images/common/divlets/NOTE.gif*/) no-repeat bottom right !important;
}
div.hsbcBackground01
{
	background: url("faded_bg.jpg"/*tpa=http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/theme/images/common/faded_bg.jpg*/) no-repeat bottom right !important;
	padding-bottom: 16px !important;
}

.hsbcSpacing19
{
	margin-top: 6px;
	padding-bottom: 10px;
}

div.hsbcActions
{
	padding-top: 14px;
	/* \*/
	padding-top: 4px;
	/* */
	text-transform:uppercase;
	font-weight: bold;
	font-size: 68%;
	/* \*/
	font-size: 62%;
	/* */
	margin-bottom:2px;
	padding-right:10px;
}

span.hsbcActions
{
	text-transform:uppercase;
	font-size: 94%;
}



div.hsbcActions a,
span.hsbcActions a
{
	text-decoration: none;
}

div.hsbcStyleContent16 span.hsbcAlign03
{
	text-transform:uppercase;
	font-size: 68%;
	/* \*/
	font-size: 61%;
	/* */
	padding-top:5px;
}
div.hsbcStyleContent16 span.hsbcAlign03 a
{
	color:#666;
}
.hsbcAlign03
{
	float: right;
}

.hsbcAlign08
{
	float: left; 
	width:100%;
}
</style>


<h2>Our Services</h2>
  <div class="hsbcFlexStyle02 hsbcBackground01 hsbcSpacing19">
	<div class="hsbcActions hsbcAlign03"><a class="hsbcLinkStyle09" href="premier.htm" tppabs="http://www.hsbc.com.hk/1/2/hk/premier" title="Info | Apply"><img src="info_apply_btn.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/info_apply_btn.gif" alt="Info | Apply" height="15" width="70" /></a></div>
	<h3 class="hsbcSizeText04 hsbcColorText03 hsbcTextStyle03"><a href="premier.htm" tppabs="http://www.hsbc.com.hk/1/2/hk/premier">HSBC Premier</a></h3>
	<div class="hsbcAlign08">
	  <a href="premier.htm" tppabs="http://www.hsbc.com.hk/1/2/hk/premier" title="Info | Apply"><img src="premier_logo.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/premier_logo.gif" class="hsbcSpacing17" alt="" align="right" /></a>

	  <p class="hsbcSizeText01 hsbcColorText04 hsbcSpacing18">World-class wealth management service with:</p>
	  <ul class="hsbcSizeText01 hsbcColorText04 hsbcSpacing18">
		<li>Global investment opportunities</li>
		<li>Worldwide recognition and benefits</li>
	  </ul>
	</div>
	<div class="hsbcEndFloat"></div>

  </div>
  <div class="extHrType02"></div>
  
  <div class="hsbcFlexStyle02 hsbcBackground01 hsbcSpacing19">
	<div class="hsbcActions hsbcAlign03"><a class="hsbcLinkStyle09" href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hsbcadvance/home  \n\nThis file was not retrieved by Teleport Pro, because it is addressed on a domain or path outside the boundaries set for its Starting Address.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hsbcadvance/home'" tppabs="http://www.hsbc.com.hk/1/2/hsbcadvance/home" title="Info | Apply"><img src="info_apply_btn.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/info_apply_btn.gif" alt="Info | Apply" height="15" width="70" /></a></div>
	<h3 class="hsbcSizeText04 hsbcColorText03 hsbcTextStyle03"><a href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hsbcadvance/home  \n\nThis file was not retrieved by Teleport Pro, because it is addressed on a domain or path outside the boundaries set for its Starting Address.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hsbcadvance/home'" tppabs="http://www.hsbc.com.hk/1/2/hsbcadvance/home">HSBC Advance</a></h3>
	<div class="hsbcAlign08">
	  <a href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hsbcadvance/home  \n\nThis file was not retrieved by Teleport Pro, because it is addressed on a domain or path outside the boundaries set for its Starting Address.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hsbcadvance/home'" tppabs="http://www.hsbc.com.hk/1/2/hsbcadvance/home" title="Info | Apply"><img src="advance_logo.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/advance_logo.gif" class="hsbcSpacing17" alt="" align="right" /></a>
	  <p class="hsbcSizeText01 hsbcColorText04 hsbcSpacing18">HSBC Advance is a powerful integrated financial service to offer you:</p>

	  <ul class="hsbcSizeText01 hsbcColorText04 hsbcSpacing16">
		<li>Dedicated wealth management service from our financial professional team</li>
		<li>Powerful self-service financial management tools</li>
	  </ul>
	</div>
	<div class="hsbcEndFloat"></div>
  </div>
  <div class="extHrType02"></div>

  <div class="hsbcFlexStyle02 hsbcBackground01 hsbcSpacing19">
	<div class="hsbcActions hsbcAlign03"><a class="hsbcLinkStyle09" href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hk/banking/sva  \n\nThis file was not retrieved by Teleport Pro, because it was unavailable, or its retrieval was aborted, or the project was stopped too soon.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hk/banking/sva'" tppabs="http://www.hsbc.com.hk/1/2/hk/banking/sva" title="Info | Apply"><img src="info_apply_btn.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/info_apply_btn.gif" alt="Info | Apply" height="15" width="70" /></a></div>
	<h3 class="hsbcSizeText04 hsbcColorText03 hsbcTextStyle03"><a href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hk/banking/sva  \n\nThis file was not retrieved by Teleport Pro, because it was unavailable, or its retrieval was aborted, or the project was stopped too soon.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hk/banking/sva'" tppabs="http://www.hsbc.com.hk/1/2/hk/banking/sva">SmartVantage</a></h3>
	<div class="hsbcAlign08">
	  <a href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hk/banking/sva  \n\nThis file was not retrieved by Teleport Pro, because it was unavailable, or its retrieval was aborted, or the project was stopped too soon.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hk/banking/sva'" tppabs="http://www.hsbc.com.hk/1/2/hk/banking/sva" title="Info | Apply"><img src="smartvantage_logo.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/smartvantage_logo.gif" class="hsbcSpacing17" alt="" align="right" /></a>
	  <p class="hsbcSizeText01 hsbcColorText04 hsbcSpacing18">Self-directed integrated financial banking service:</p>

	  <ul class="hsbcSizeText01 hsbcColorText04 hsbcSpacing16">
		<li>Full range of self-directed money management tools</li>
		<li>Consolidated statement for total control</li>
	  </ul>
	</div>
	<div class="hsbcEndFloat"></div>
  </div>
 <div class="extHrType02"></div>
  
  <div class="hsbcFlexStyle02 hsbcBackground01 hsbcSpacing19">
	<div class="hsbcActions hsbcAlign03"><a class="hsbcLinkStyle09" href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hk/banking/accounts  \n\nThis file was not retrieved by Teleport Pro, because it was unavailable, or its retrieval was aborted, or the project was stopped too soon.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hk/banking/accounts#hkd_time'" tppabs="http://www.hsbc.com.hk/1/2/hk/banking/accounts#hkd_time" title="Info | Apply"><img src="info_apply_btn.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/info_apply_btn.gif" alt="Info | Apply" height="15" width="70" /></a></div>
	<h3 class="hsbcSizeText04 hsbcColorText03 hsbcTextStyle03"><a href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hk/banking/accounts  \n\nThis file was not retrieved by Teleport Pro, because it was unavailable, or its retrieval was aborted, or the project was stopped too soon.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hk/banking/accounts#hkd_time'" tppabs="http://www.hsbc.com.hk/1/2/hk/banking/accounts#hkd_time">Time Deposits</a></h3>
	<div class="hsbcAlign08">
	  <a href="javascript:if(confirm('http://www.hsbc.com.hk/1/2/hk/banking/accounts  \n\nThis file was not retrieved by Teleport Pro, because it was unavailable, or its retrieval was aborted, or the project was stopped too soon.  \n\nDo you want to open it from the server?'))window.location='http://www.hsbc.com.hk/1/2/hk/banking/accounts#hkd_time'" tppabs="http://www.hsbc.com.hk/1/2/hk/banking/accounts#hkd_time" title="Info | Apply"><img src="financial_planning_p6_f49.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/financial_planning_p6_f49.gif" class="hsbcSpacing17" alt="" align="right" /></a>
	  <p class="hsbcSizeText01 hsbcColorText04 hsbcSpacing18">Get a higher rate of interest by placing your deposit for a set period of time.</p>  
	  <ul class="hsbcSizeText01 hsbcColorText04 hsbcSpacing16">
		<li>Deposit tenor from 1 day to 36 months available</li>
		<li>Place and manage your deposits conveniently with HSBC Internet Banking</li>
	  </ul>
	</div>
	<div class="hsbcEndFloat"></div>
  </div>
  <div class="extHrType02"></div>
  
  <div class="hsbcFlexStyle02 hsbcBackground01 hsbcSpacing19">
	<div class="hsbcActions hsbcAlign03"><a class="hsbcLinkStyle09" href="fcy.htm" tppabs="http://www.hsbc.com.hk/1/2/hk/investments/fcy" title="Info | Apply"><img src="info_apply_btn.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/info_apply_btn.gif" alt="Info | Apply" height="15" width="70" /></a></div>

	<h3 class="hsbcSizeText04 hsbcColorText03 hsbcTextStyle03"><a href="fcy.htm" tppabs="http://www.hsbc.com.hk/1/2/hk/investments/fcy">Currency Exchange</a></h3>
	<div class="hsbcAlign08">
	  <a href="fcy.htm" tppabs="http://www.hsbc.com.hk/1/2/hk/investments/fcy" title="Info | Apply"><img src="financial_planning_p6_f34.gif" tppabs="http://www.hsbc.com.hk/1/PA_1_3_S5/content/hongkongpws/common/images/financial_planning_p6_f34.gif" class="hsbcSpacing17" alt="" align="right" /></a>
	  <p class="hsbcSizeText01 hsbcColorText04 hsbcSpacing18">Whether it is for travelling or foreign currency investment, you can always benefit from our 24 hours currency exchange services.</p>
	  <ul class="hsbcSizeText01 hsbcColorText04 hsbcSpacing16">
		<li>11 currencies available</li>
		<li>Competitive exchange rates</li>
	  </ul>

	</div>
	<div class="hsbcEndFloat"></div>
  </div>
  <div class="extHrType02"></div>

</div>
    </div>
  </div>
  <?php include_once("footer.php");?>
</div>
</body>
</html>