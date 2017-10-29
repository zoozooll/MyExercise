  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>您的位置：</td>
          <td><a href="<?=site_url('globals/index/chi');?>">首页</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/prolist/chi');?>">产品介绍</a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody">
      <table border="0" cellpadding="0" cellspacing="0" 
width="690px" height="380px" id="new_pro">
        <tbody>
          <tr>
            <th colspan="3">搜索结果</th>
          </tr>
          <tr>
            <td colspan="3" align="left" valign="top" height="10">&nbsp;</td>
          </tr>
<?php 
$i = 1;
foreach ($datas as $data) {
	if ($i % 2 == '1') {echo '<tr>';} 
?>  
<td class="iopbodytexttd" align="left" valign="top" 
width="335" height="120"><div class="iopsectiontitlebackground" 
id="Item1titlebackground"><img 
src="system/images/section_header.jpg" width="335" 
height="20"></div>
              <div class="iopsectiontitle" id="Item1title"><a href="<?=site_url('product/productview/chi/'.$data->id)?>"><?=$data->subject_chi?></a></div>
              <a href="<?=site_url('product/productview/chi/'.$data->id)?>"><img
 src="<img src="system/images/backend/uploads/<?=$data->img1?>" 
alt="" class="imgsectionFloatLeft" id="Item1image" 
width="100" height="120"></a>
              <p class="iopSectionText">&nbsp;</p>
              <p class="iopSectionText"><?=strcut($data->content_chi, 0, 100)?>. <a 
href="<?=site_url('product/productview/chi/'.$data->id)?>" 
target="_self" class="iopbodytextlink" title="<?=$data->subject_chi?>">Click 
                to learn more</a>. </p></td>
<?php if ($i % 2 == '0') {echo '</tr>';} $i++;}?> 

				 </tbody>
      </table>
      </div>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>