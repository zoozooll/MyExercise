  <div id="pagebody">
    <div id="body_site">
      <table>
        <tr>
          <td>您的位置：</td>
          <td><a href="<?=site_url('globals/index/chi');?>">首页</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/news/chi');?>">新闻中心</a></td>
          <td>>></td>
          <td><a href="<?=site_url('globals/index/chi');?>"><?=$data[0]->subject_chi?></a></td>
        </tr>
      </table>
    </div>
    <?php  require_once("leftbar.php")?>
    <!--leftbar end-->
    <div id="mainbody">
      <table border="0" cellpadding="0" cellspacing="0" 
width="690px" id="new_pro">
        <tbody>
          <tr>
            <th ><?=$data[0]->subject_chi?></th>
          </tr>
          <tr>
            <td  align="left" valign="top" height="10">&nbsp;</td>
          </tr>
          <tr>
            <td align="left" valign="top" ><pre style=" width:600px;font:14px/24px normal;padding:10px;margin:5px 0;	border:1px #eee solid;	color:#333;
	text-align:left;">
					<?=$data[0]->content_chi?>
            </pre>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <?php require_once("footer.php");?>
</div>
</body>
</html>