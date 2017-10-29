  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>Your position:</td>
          <td><a href="<?=site_url('globals/index/eng');?>">Home</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/infolist/eng');?>">Technical Data</a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody" class="info_list">
    	<div id="news_info">
      <h1>Technical data</h1>
      <ul>
      	<?php foreach ($datas as $data) { ?>
      	<li>
        	<a href="<?=site_url("skill/skillview/eng/".$data->id )?>"><?=$data->subject_eng?></a>
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