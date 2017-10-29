<?php
require('inc/set.php');
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户中心 - 我要置顶信息 - <?php echo $web['sitename']; ?><?php echo $web['code_author']; ?></title>
<meta name="Description" content="<?php echo $web['description']; ?>" />
<meta name="keywords" content="<?php echo $web['keywords']; ?>" />
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>
<div id="logo"><a href="<?php echo $web['path']; ?>"><img src="images/logo.gif" width="200" height="60" alt="<?php echo $web['sitename']; ?> - 欢迎您" /></a></div>
<div id="banner"><a href="../">回到上级首页</a> <span onclick="document.body.style.fontSize='100%'">大字</span> <span onclick="document.body.style.fontSize='81.25%'">小字</span></div>



<table class="maintable" height="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="left_menu" valign="top"><h5><a href="./">论坛首页</a> &gt; <a href="user.php">用户中心</a></h5>

        <?php
require('inc/function/confirm_login.php');
require('inc/function/user_class.php');
require('inc/function/get_date.php');
if(confirm_login()){
  require('inc/require/user_left_menu.txt');

  $yes='我要置顶信息';
}

?>
    </td>
    <td width="100" valign="middle" align="right" class="pass"> 》</td>
    <td valign="top"><h5><?php echo $yes; ?>&nbsp;</h5>


<?php
if(isset($yes)){
  $you=explode('_',$cookie[1]);
  echo '欢迎：'.$cookie[0].' '.user_class(abs($cookie[1])).'<br />
      <a href="user.php">用户中心</a> | <a href="run.php?run=user_login&act=logout">退出</a> | 您上次访问是'.$you[1].'';
?>
<div class="list_title"><a class="list_title_in">当前权限</a></div>
<script language="javascript" type="text/javascript">
<!--
// 只允许输入数字
function isDigit(obj){
  if(obj.value!="" && !/^\d+$/.test(obj.value)){
    alert("你输入的值不对，应填写数字！");
    obj.value="";
  }
}
-->
</script>
<?php
  require('inc/set_sql.php');
  //连接mysqkl数据库
  if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
    if(@mysql_select_db($sql['name'],$db)){
      mysql_query('SET NAMES '.$sql['char'].'');
      $result=mysql_query('SELECT topcount,topdate FROM yzsoumember WHERE username="'.$cookie[0].'"',$db);
      if($row=mysql_fetch_assoc($result)){
        $text.='您当前的置顶权限为：'.abs($row['topcount']).'条，置顶截止期限为：'.($row['topdate']!='' && $row['topdate']>gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600))?get_date($row['topdate']).'（期限有效）':'（无效）').'';
      }else{
        $text.='<br /><img src="images/i.gif" align="absmiddle" /> 出错！数据库查无数据！';
	  }
      mysql_free_result($result);
    }else{
      $text.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['name'].']连接不成功！';
    }
    mysql_close();
  }else{
    $text.='<br /><img src="images/i.gif" align="absmiddle" /> 数据库['.$sql['host'].']连接不成功！';
  }
  echo $text;
?>
<br />
<br />
<div class="list_title"><a class="list_title_in">购买权限</a></div>
<script language="javascript" type="text/javascript">
<!--
// 只允许输入数字
function isDigit(obj){
  if(!/^([1-9]|[0-9]{2,})$/.test(obj.value)){
    alert("你输入的值不对，应填写大于1的数字！");
    obj.value="";
  }
}

function confirmNum(){
  var startNum=<?php echo $web['top_expires']; ?>;
  var price=<?php echo $web['top_price']; ?>;
  var topcount=  document.getElementById('topcount');
  var topdate=   document.getElementById('topdate');
  var amount=    document.getElementById('amount');
  var amountshow=document.getElementById('amountshow');
  if(topcount.value!='' && topdate.value!=''){
    if(parseFloat(topdate.value)>=parseFloat(startNum)){
	  amount.value=parseFloat(topcount.value)*parseFloat(topdate.value)*parseFloat(price);
      amountshow.innerHTML='总计：'+amount.value+'元';
	  return true;
	}else{
	  amount.value='';
      amountshow.innerHTML='少于起订天数（'+startNum+'）';
	  return false;
	}
  }else{
	amount.value='';
    amountshow.innerHTML='请填好订单！';
	return false;
  }
}
-->
</script>
<b>当前置顶售价：<?php echo $web['top_price']; ?>元/条/天<br />
最低起计天数：<?php echo $web['top_expires']; ?>天</b><br />
<form method="post" name="manageform" action="run.php?run=user_buy_top" onsubmit="return confirmNum()">
我要购买置顶权限：<input name="topcount" id="topcount" type="text" size="2" onKeyUp="isDigit(this)" onpropertychange="confirmNum()" oninput="confirmNum()" />条，置顶有效期：<input name="topdate" id="topdate" type="text" size="2" onKeyUp="isDigit(this)" onpropertychange="confirmNum()" oninput="confirmNum()" />天
<input type="hidden" name="amount" id="amount" /><font id="amountshow" color="#FF3300"></font><br />

<input type="submit" value="提交订单" />
<br />




</form>
<?php
}else{
  echo '欢迎你：Guest匿名用户<br /><a href="user_reg.php?'.basename($_SERVER['REQUEST_URI']).'"><b>先去创建帐号（非常简单）</b></a>或<a href="user_login.php?'.basename($_SERVER['REQUEST_URI']).'"><b>登录帐号</b></a>，以获得更多发表或管理权限';
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
