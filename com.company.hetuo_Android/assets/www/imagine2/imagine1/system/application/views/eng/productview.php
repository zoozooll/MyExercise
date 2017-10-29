  <!--header end-->
  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>Your position:</td>
          <td><a href="<?=site_url('globals/index/eng');?>">Home</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/prolist/eng');?>">Product introduction</a></td>
          <td>>></td>
          <td><a href=""><?=$data[0]->subject_eng?></a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody">
      <div id="pro_cont">
        <h1><?=$data[0]->subject_eng?></h1>
        <div class="pro_cont_l">
          <div id="pro_img"> <img src="system/images/backend/uploads/<?=$data[0]->img1?>" scr="产品名" height="180px" width="180px"/> </div>
          <dl>
            <dt>Product Name</dt>
            <dd><?=$data[0]->subject_eng?></dd>
            <dt>Product Type</dt>
            <dd><?=$title?></dd>
            <dt>Product No.</dt>
            <dd><?=$data[0]->number?></dd>
          </dl>
        </div>
      </div>
      <div id="pro_info">
      <h2>Detailed introduction<i> </i></h2>
 
<pre>
<?=$data[0]->content_eng?>
【Technical parameters】
 <div id="pro_img"> <img src="system/images/backend/uploads/<?=$data[0]->img1?>" scr="产品名" height="180px" width="180px"/> </div>
 </pre>
      </div>
    </div>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>