<?php
header("Expires: Thu, 01 Jan 1970 00:00:01 GMT");
header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");

require('inc/set.php');
require('inc/set_sql.php');


//输出信息
function alert($text,$href){
  echo '
  <script language="javascript" type="text/javascript">
  <!--
  document.getElementById("transition").innerHTML="";
  setTimeout("location.href=\''.$href.'\';",3000);
  -->
  </script>
  <div id="output">'.$text.'<br />或点击以下链接进入...<a href="'.$href.'">'.$href.'</a></div>

</div></div>
</body></html>';
  die;
}

//输出错误
function err($text){
  echo '
  <script language="javascript" type="text/javascript">
  <!--
  document.getElementById("transition").innerHTML="";
  -->
  </script>
  <div id="output">'.$text.'<br />点此可<a href="javascript:window.history.back();"><u>返回</u></a></div>

	</td>
    <td class="right gray">&nbsp;</td>
  </tr>
</table>

</body>
</html>';
  die;
}



?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>程序命令运行 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">

</head>
<body>
<div style="margin:100px;">
<h1>程序命令运行</h1><br /><br />
<span id="transition">程序处理中，请稍候...<br /><br />
<font color="#CCCCCC">如果程序长时间停止响应，请检查目录写入权限<br />具体可参安装说明</font></span>
</div>

<?php
if($_REQUEST['run'] && file_exists('run/'.$_REQUEST['run'].'.php'))
  require_once('run/'.$_REQUEST['run'].'.php');
else
  alert('命令错误或功能尚未开通！','index.php');
?>

	

</body>
</html>