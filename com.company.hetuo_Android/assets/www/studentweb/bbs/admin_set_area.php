<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员中心 - 管理设置栏目 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script language="javascript" type="text/javascript">
<!--
//添加分论坛
function addColumn(){
  var par=document.getElementById('area');
  var tar=document.getElementsByName('column[]');
  var end=tar.length;
  num=(end>0) ? parseInt(tar[end-1].title) : 0;
  num=num+1;
  par.innerHTML+='\
  <div id="column[]" name="column[]" title="'+num+'" style="width:80%;border:1px #666 solid;margin-top:5px;padding:5px;">\
    分论坛 <input type="text" name="column_id['+num+']" id="column_id['+num+']" value="'+num+'" size="3" onBlur="isDigit(this,'+num+',0)" />\
    名称 <input type="text" name="column_name['+num+']" id="column_name['+num+']" value="" size="30" />\
    <div id="clumn_'+num+'"></div>\
    <input type="button" value="为此分论坛添加版区" onclick="javascript:addClass(\''+num+'\');" />\
    <input type="button" value="删除此分论坛" onclick="javascript:removeOption(this);" />\
  </div>';
  //window.location.href='#column_id['+num+']';
}

//添加版区
function addClass(column_id){
  var par=document.getElementById('clumn_'+column_id+'');
  var tar=document.getElementsByName('class['+column_id+'][]');
  var end=tar.length;
  num=(end>0) ? parseInt(tar[end-1].title) : 0;
  num=num+1;
  par.innerHTML+='\
    <div id="class['+column_id+'][]" name="class['+column_id+'][]" title="'+num+'" style="margin-left:24px">\
      版区 <input type="text" name="class_id['+column_id+']['+num+']" value="'+num+'" size="3" onBlur="isDigit(this,'+num+',0)" />\
      名称 <input type="text" name="class_name['+column_id+']['+num+']" id="class_name['+column_id+']['+num+']" value="" size="30" />\
      <input type="button" value="删除分版区" onclick="javascript:removeOption(this);">\
    </div>';

  //window.location.href='#class_name['+column_id+']['+num+']';
}

//删除栏目
function removeOption(obj){
  var tar=obj.parentNode;
  var par=tar.parentNode;
  if(confirm('确定删除此栏目分类吗？！')){
    try{
      par.removeChild(tar);
    }catch(err){
    }
  }
}

// 只允许输入数字
function isDigit(obj,starVal,n){
  if(obj.value!="" && (!/^\d+$/.test(obj.value) || obj.value<=n)){
    alert("你输入的值不对，应填大于"+n+"的数字！");
    obj.value=starVal;
  }
}
//-->
</script>
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top">

<?php
require('inc/set_area.php');
require('inc/function/confirm_manager.php');
require('inc/function/user_class.php');
require('inc/function/getarea.php');

if(confirm_manager()==true && $cookie[0]==$web['manager']){
  $manage='&manage=yes';
  require('inc/require/admin_left_menu.txt');
  $yes='管理设置栏目';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
<a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'<br />
<br />
';
?>

<br /><b>提示：</b>
<br />以下信息必须认真填写，必须
<br />尽量不要用特殊符号，如 \ : ; * ? ' &lt; &gt; | ，必免导致错误。
<form action="run.php?run=admin_set_area" method="post">
<div id="area">
<?php
  $text='';
  @ksort($web['area']);

  foreach($web['area'] as $fid=>$f){
    $text.='
  <div id="column[]" name="column[]" title="'.$fid.'" style="width:80%;border:1px #666 solid;margin-top:5px;padding:5px;">
    分论坛 <input type="text" name="column_id['.$fid.']" id="column_id['.$fid.']" value="'.$fid.'" size="3" onBlur="isDigit(this,'.$fid.',0)" />
    名称 <input type="text" name="column_name['.$fid.']" id="column_name['.$fid.']" value="'.$f[0].'" size="30" /><br />
    <div id="clumn_'.$fid.'">';
    
    foreach((array)$web['area'][$fid] as $cid=>$c){
      if($cid==0) continue;
      $text.='
    <div id="class['.$fid.'][]" name="class['.$fid.'][]" title="'.$cid.'" style="margin-left:24px">
      版区 <input type="text" name="class_id['.$fid.']['.$cid.']" value="'.$cid.'" size="3" onBlur="isDigit(this,'.$cid.',0)" />
      名称 <input type="text" name="class_name['.$fid.']['.$cid.']" id="class_name['.$fid.']['.$cid.']" value="'.$c.'" size="30" />
      <input type="button" value="删除分版区" onclick="javascript:removeOption(this);">
    </div>';
    }
    $text.='
    </div>
    <input type="button" value="为此分论坛添加版区" onclick="javascript:addClass(\''.$fid.'\');">
    <input type="button" value="删除此分论坛" onclick="javascript:removeOption(this);" />
  </div>';
  }
  echo $text=='' ? '<div style="color:#FF6600">您还未设置分类！请先添加分类</div>' : $text;
?>
</div>
<br />
<br />

<input type="button" value="添加分论坛" onClick="addColumn();"><br />
<input type="submit" value="★★★★★★ 栏目分类设置完毕，提交 ★★★★★★" onClick="javascript:return confirm('确定提交吗？！');">
<input type="button" value="★★★★★★  设162100网址导航站为主页  ★★★★★★" onclick="SetHome(this,'http://www.162100.com/');" />
<br />
<a href="http://www.162100.com/" target="_blank">浏览 162100 网址导航 - 一路爱意浓浓 - 最简炼的网址站 - 带在线音乐盒的网址站</a>
</form>



<?php
}else{
  echo '<img src="images/i.gif" align="absmiddle" /> 请以基本管理员'.$web['manager'].'<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录</b></a>，以获得管理权限';
}

?>

    </td>
  </tr>
</table>
<br />
<br />
<br />
<br />

<div id="foot"><?php require('inc/require/foot.txt'); ?></div>
</body>
</html>
