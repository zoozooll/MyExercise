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
          <td>您的位置：</td>
          <td><a href="<?=site_url('globals/index/chi')?>">首页</a></td>
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
width="690px" id="show_pro">
        <tbody>
          <tr>
            <th colspan="3">产品介绍</th>
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
width="335" height="120"><div class="iopsectiontitle" style="width:315px;height:20px; margin-top:10px; padding-left:20px; padding-top:px; background:url(system/images/section_header.jpg); overflow:hidden"><a
 	href="<?=site_url('product/productview/chi/'.$data->id)?>"><?=$data->subject_chi?></a></div>
                <div style="width: 334px; height: 150px; position: absolute; overflow: hidden;  ">
                	<div style="width:100px; height:100px; margin:0 10px; border:#CB0447 1px solid; float:left; position: absolute; padding-top:40px">
                    	<a href="<?=site_url('product/productview/chi/'.$data->id)?>"><img width="100" height="100" src="system/images/backend/uploads/<?=$data->img1?>" /></a>
                    </div>
                    <div style="position: absolute; left: 113px; top: 36px; height: 110px; width: 211px; padding:5px; overflow:hidden;white-space:pre-wrap;word-wrap:break-word;"><p><?=strcut($data->content_eng, 0, 100)?></p><p><a href="<?=site_url('product/productview/chi/'.$data->id)?>" target="_self" class="iopbodytextlink" title="<?=$data->subject_eng?>">了解更多</a></p></div>
                </div></td>
<?php
if ($i % 2 == '1') {echo ' <td align="left" valign="top" width="20" height="150">&nbsp;</td>';} 
if ($i % 2 == '0') {echo '</tr>';} $i++;}?>    
        </tbody>
      </table>
    </div>
  </div>
  
  <?php include_once("footer.php");?>
</div>
</body>
</html>