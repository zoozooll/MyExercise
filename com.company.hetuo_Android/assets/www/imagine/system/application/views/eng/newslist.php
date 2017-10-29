  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>Your position:</td>
          <td><a href="<?=site_url('globals/index/eng');?>">Home</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/infolist/eng');?>">News </a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody" class="info_list">
    	<div id="news_info">
      <h1>News List</h1>
      <ul>
      <ul>
      	<?php foreach ($datas as $data) { ?>
      	<li>
        	<a href="<?=site_url("news/newsview/eng/".$data->id )?>"><?=$data->subject_eng?></a>
            <span><?=date('Y-m-d', $data->time)?></span>
        </li>
        <?php } ?>
      </ul>

      </ul>
      </div>
  
    </div>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>