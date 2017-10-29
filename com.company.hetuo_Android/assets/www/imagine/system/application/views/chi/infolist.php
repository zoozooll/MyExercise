  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>您的位置：</td>
          <td><a href="<?=site_url('globals/index/chi');?>">首页</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/infolist/chi');?>">技术资料</a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody" class="info_list">
    	<div id="news_info">
      <h1>技术资料</h1>
      <ul>
      	<?php foreach ($datas as $data) { ?>
      	<li>
        	<a href="<?=site_url("skill/skillview/chi/".$data->id )?>"><?=$data->subject_chi?></a>
            <span><?=date('Y-m-d', $data->time)?></span>
        </li>
        <?php } ?>
      </ul>
      </div>
  
    </div>
    <?=$page?>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>