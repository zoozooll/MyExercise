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
      <table border="0" cellpadding="0" cellspacing="0" 
width="690px" height="380px" id="show_pro">
        <tbody>
          <tr>
            <th colspan="3">产品介绍</th>
          </tr>
          <tr>
            <td colspan="3" align="left" valign="top" height="10">&nbsp;</td>
          </tr>
          <tr>
            <td class="iopbodytexttd" align="left" valign="top" 
width="335" height="120"><div class="iopsectiontitlebackground" 
id="Item1titlebackground"><img 
src="images/section_header.jpg" width="335" 
height="20" /></div>
              <div class="iopsectiontitle" id="Item1title"><a href="productview.php" > Turnkey 
                solutions for industry &amp; research </a> </div>
              <a 
href="productview.php"><img
 src="images/iop_section_image_solutions.jpg" 
alt="" class="imgsectionFloatLeft" id="Item1image" 
width="100" height="120" /></a>
              <p class="iopSectionText">&nbsp;</p>
              <p class="iopSectionText">Our turnkley solutions 
                combine quality construction and ease of use with cutting-edge features 
                you won't won't find anywhere else. <a 
href="productview.php" 
target="_self" class="iopbodytextlink" title="Our turnkey wavefront 
analysis and adaptive optics solutions for industry and research">Click 
                  to learn more</a>. </p></td>
            <td align="left" valign="top" width="20" height="120">&nbsp;</td>
            <td class="iopbodytexttd" align="left" valign="top" 
width="335" height="120"><div class="iopsectiontitlebackground" 
id="Item1titlebackground"><img 
src="images/section_header.jpg" width="335" 
height="20" /></div>
              <div class="iopsectiontitle" id="Item1title"><a href="productview.php" > Turnkey 
                solutions for industry &amp; research </a></div>
              <a 
href="productview.php"><img
 src="images/iop_section_image_solutions.jpg" 
alt="" class="imgsectionFloatLeft" id="Item1image" 
width="100" height="120" /></a>
              <p class="iopSectionText">&nbsp;</p>
              <p class="iopSectionText">Our turnkley solutions 
                combine quality construction and ease of use with cutting-edge features 
                you won't won't find anywhere else. <a 
href="productview.php" 
target="_self" class="iopbodytextlink" title="Our turnkey wavefront 
analysis and adaptive optics solutions for industry and research">Click 
                  to learn more</a>. </p></td>
          </tr>
          <tr>
            <td colspan="3" align="left" valign="top" height="10">&nbsp;</td>
          </tr>
          <tr>
            <td class="iopbodytexttd" align="left" valign="top" 
width="335" height="120"><div class="iopsectiontitlebackground" 
id="Item1titlebackground"><img 
src="images/section_header.jpg" width="335" 
height="20" /></div>
              <div class="iopsectiontitle" id="Item1title"><a href="productview.php" > Turnkey 
                solutions for industry &amp; research </a></div>
              <a 
href="productview.php"><img
 src="images/iop_section_image_solutions.jpg" 
alt="" class="imgsectionFloatLeft" id="Item1image" 
width="100" height="120" /></a>
              <p class="iopSectionText">&nbsp;</p>
              <p class="iopSectionText">Our turnkley solutions 
                combine quality construction and ease of use with cutting-edge features 
                you won't won't find anywhere else. <a 
href="productview.php" 
target="_self" class="iopbodytextlink" title="Our turnkey wavefront 
analysis and adaptive optics solutions for industry and research">Click 
                  to learn more</a>. </p></td>
            <td align="left" valign="top" height="120">&nbsp;</td>
            <td class="iopbodytexttd" align="left" valign="top" 
width="335" height="120"><div class="iopsectiontitlebackground" 
id="Item1titlebackground"><img 
src="images/section_header.jpg" width="335" 
height="20" /></div>
              <div class="iopsectiontitle" id="Item1title"><a href="productview.php" > Turnkey 
                solutions for industry &amp; research </a> </div>
              <a 
href="productview.php"><img
 src="images/iop_section_image_solutions.jpg" 
alt="" class="imgsectionFloatLeft" id="Item1image" 
width="100" height="120" /></a>
              <p class="iopSectionText">&nbsp;</p>
              <p class="iopSectionText">Our turnkley solutions 
                combine quality construction and ease of use with cutting-edge features 
                you won't won't find anywhere else. <a 
href="productview.php" 
target="_self" class="iopbodytextlink" title="Our turnkey wavefront 
analysis and adaptive optics solutions for industry and research">Click 
                  to learn more</a>. </p></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <?php include_once("footer.php");?>
</div>
</body>
</html>