  <!--header end-->
  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>您的位置：</td>
          <td><a href="<?=site_url('globals/index/chi');?>">首页</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/prolist/chi');?>">产品介绍</a></td>
          <td>>></td>
          <td><a href=""><?=$data[0]->subject_chi?></a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody">
      <div id="pro_cont">
        <h1><?=$data[0]->subject_chi?></h1>
        <div class="pro_cont_l">
          <div id="pro_img"> <img src="system/images/backend/uploads/<?=$data[0]->img1?>" scr="产品名" height="180px" width="180px"/> </div>
          <dl>
            <dt>产品名称</dt>
            <dd><?=$data[0]->subject_chi?></dd>
            <dt>产品分类</dt>
            <dd><?=$title?></dd>
            <dt>产品编号</dt>
            <dd><?=$data[0]->number?></dd>
          </dl>
        </div>
      </div>
      <div id="pro_info">
      <h2>详细介绍<i> </i></h2>
 
<pre>
<?=$data[0]->content_chi?>

 </pre>
      </div>
    </div>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>