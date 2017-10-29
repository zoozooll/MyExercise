<!--header end-->
<script type="application/javascript" src="system/js/jy.js"></script>
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
  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>Your Position:</td>
          <td><a href="<?=site_url('globals/index/eng')?>">Home</a></td>
          <td></td>
          <td></td>
        </tr>
      </table>
    </div>
    <?php  include_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody">
          <div id="mainbody">
    	<div id= "mainbar_turnImg">
      		<div id="bigPic">
      			<?php foreach ($img as $i) { ?>
				<a href="<?=$i->link?>"><img style="display: none; opacity: 1; z-index: 1;" src="system/images/backend/uploads/<?=$i->picpath?>" alt=""></a>
				<? } ?>
			</div>
			
			
			<ul id="thumbs">
				<?php $j = 1;foreach ($img as $i) { ?>
				<li class=" " rel="<?=$j++?>"><img src="system/images/backend/uploads/<?=$i->picpath?>" alt=""></li>
				<? } ?>
			</ul>
		</div>
      <table border="0" cellpadding="0" cellspacing="0" 
width="690px" height="380px" id="show_pro">
        <tbody>
          <tr>
            <th colspan="3">Product introduction</th>
          </tr>
          <tr>
            <td colspan="3" align="left" valign="top" height="10">&nbsp;</td>
          </tr>
<?php 
$i = 1;
foreach ($hot_datas as $data) {
	if ($i % 2 == '1') {echo '<tr>';} 
?>    
<td class="iopbodytexttd" align="left" valign="top" 
width="335" height="120"><div class="iopsectiontitlebackground" 
id="Item1titlebackground"><img 
src="system/images/section_header.jpg" width="335" 
height="20" /></div>
<div class="iopsectiontitle" id="Item1title"><a href="<?=site_url('product/productview/eng/'.$data->id)?>" > <?=$data->subject_eng?></a></div>
<a href="<?=site_url('product/productview/eng/'.$data->id)?>"><img src="system/images/backend/<?=$data->img1?>" 
alt="" class="imgsectionFloatLeft" id="Item1image" 
width="100" height="120" /></a><p class="iopSectionText">&nbsp;</p><p class="iopSectionText"><?=strcut($data->content_eng, 0, 100)?>. <a href="<?=site_url('product/productview/eng/'.$data->id)?>" target="_self" class="iopbodytextlink" title="<?=$data->subject_eng?>">Click 
                  to learn more1</a>. </p></td>
<?php if ($i % 2 == '0') {echo '</tr>';} $i++;}?>    
        </tbody>
      </table>
    </div>
  </div>
  <?php include_once("footer.php");?>
</div>
</body>
</html>