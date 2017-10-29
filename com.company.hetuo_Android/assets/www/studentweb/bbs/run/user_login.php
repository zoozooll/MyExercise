<?php
require('inc/function/filter.php');
require('inc/function/write_file.php');
function trim_all($text){
  return strtolower(filter1($text));
}

//基础管理员任何故障下登录
function base_manager_login($name,$password){
  global $web,$nowdate,$loc;
    if($name==$web['manager'] && $password==$web['password']){
      $you['name']=$name;
      $you['id']='10000_'.$nowdate.'_'.mt_rand(100000,999999).'';
      $you['power']='manager';
	  if(!file_exists('power')){
	    @mkdir('power',0777);
	  }
	  if(file_exists('power')){
	    @chmod('power',0777);
        write_file('power/'.urlencode($you['name']).'.php','<?php die(); ?>'.$you['id'].''); //登录密钥记录
	  }
      //用js设置cookie，因为前面已有输出
      echo '
      <script language="javascript" type="text/javascript">
      <!--
	  //
	  '.(($_POST['save_cookie']==1) ? '
      var expiration=new Date((new Date()).getTime()+1209600*1000);
      document.cookie="usercookie="+encodeURIComponent(\''.@implode('|',$you).'\')+"; expires="+expiration.toGMTString()+"; path=/;";' : '
      document.cookie="usercookie="+encodeURIComponent(\''.@implode('|',$you).'\')+"; path=/;";').'
      -->
      </script>';
      alert('以基础管理员身份登录成功！欢迎您 '.$you['name'].'',$loc);
    }else{
      err('以基础管理员身份登录失败！原因分析：1、您输入的用户名或邮箱不对');
    }
}



$_REQUEST=array_map('trim_all',$_REQUEST);

$parameter='http://'.$_SERVER['HTTP_HOST'].rtrim(dirname($_SERVER['PHP_SELF']),'/\\').'/';
$_REQUEST['location']=str_replace($parameter,'',$_REQUEST['location']);
if(file_exists(preg_replace('/(\?|#).*/','',$_REQUEST['location'])))
  $loc=$_REQUEST['location'];
else
  $loc='index.php';


//退出
if($_REQUEST['act']=='logout'){
  echo '
  <script language="javascript" type="text/javascript">
  <!--
  var expiration=new Date((new Date()).getTime()-10000);
  document.cookie="usercookie="+""+"; expires="+expiration.toGMTString()+"; path="+"/"+";";
  /*
  if(top==self){
    location.href="user_login.php?'.$_REQUEST['loc'].'";
  }else{
    top.location.href=top.location.href.replace(/(#.*)$/,\'\');
  }
  */
  -->
  </script>';
  alert('注销成功，欢迎再来',$loc);

//登录
}elseif($_POST['act']=='login'){


  if(($name=$_POST['username'])=='' || ($password=$_POST['password'])==''){
    err('用户名、密码都不能空！');
  }
  if($_COOKIE['usercookie']){
    $cookie=@explode('|',$_COOKIE['usercookie']);
    err('您已经以 用户名：'.$cookie[0].' 登录过了！<br />要想更换用户名登录，请先<a href="run.php?run=user_login&act=logout&loc='.$loc.'">退出</a>');
  }




  $err='';


  //连接mysqkl数据库
  if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
    if(!@mysql_select_db($sql['name'],$db)){
      $err='数据库['.$sql['name'].']连接不成功！';
    }
  }else{
    $err='数据库['.$sql['host'].']连接不成功！';
  }

  $nowdate=gmdate('Y/m/d H:i:s',time()+(floatval($web['time_pos'])*3600));
  if($err==''){
    mysql_query('SET NAMES '.$sql['char'].'');

    if($_POST['logintype']=='username'){
      $result=mysql_query('SELECT * FROM yzsoumember WHERE username="'.$name.'"',$db);
    }elseif($_POST['logintype']=='email'){
      $result=mysql_query('SELECT * FROM yzsoumember WHERE email="'.$name.'"',$db);
    }
	if($result){
      if($row=@mysql_fetch_assoc($result)){
	    if($row['password']==$password){
          mysql_free_result($result);
          //用js设置cookie，因为前面已有输出
          $row['point']=abs($row['point']+$web['loginadd']);
	      $you['name']=$row['username'];
	      $you['id']=''.$row['point'].'_'.$nowdate.'_'.mt_rand(100000,999999).'';
          $you['power']=$row['power'];
          if($row['power']=='manager'){
	        if(!file_exists('power')){
	          @mkdir('power',0777);
	        }
		    if(file_exists('power')){
		      @chmod('power',0777);
              write_file('power/'.urlencode($you['name']).'.php','<?php die(); ?>'.$you['id'].''); //登录密钥记录
		    }
		  }
          echo '
      <script language="javascript" type="text/javascript">
      <!--
	  //
	  '.(($_POST['save_cookie']==1) ? '
      var expiration=new Date((new Date()).getTime()+1209600*1000);
      document.cookie="usercookie="+encodeURIComponent(\''.@implode('|',$you).'\')+"; expires="+expiration.toGMTString()+"; path=/;";' : '
      document.cookie="usercookie="+encodeURIComponent(\''.@implode('|',$you).'\')+"; path=/;";').'
      -->
      </script>';
          mysql_query('UPDATE `yzsoumember` SET `point`="'.$row['point'].'",`thisdate`="'.$nowdate.'" WHERE `username`="'.$row['username'].'"',$db); //更新最后访问时间
		  if(mysql_affected_rows()>0){
          }else{
            echo mysql_errno() . ": " . mysql_error() . "\n";
          }
          alert('登录成功！欢迎您 '.$you['name'].'',$loc);
	    }else{
          err('密码不符！');
	    }
      }else{
        echo '<div id="output">登录失败！原因：<br />1、用户尚未注册；<br />2、表连接不成功或尚未建立</div>';
	  }
    }else{
      echo '<div id="output">登录失败！原因：<br />1、用户尚未注册；<br />2、表连接不成功或尚未建立</div>';
      base_manager_login($name,$password);
	}
    mysql_close();
  }else{
    base_manager_login($name,$password);
  }













//找回密码
}elseif($_POST['act']=='foundpassword'){
  if($_POST['password_question']==''){
    err('密码问题不能留空！');
  }
  if($_POST['password_answer']==''){
    err('密码答案不能留空！');
  }
  if($_POST['email']==''){
    err('email不能留空！');
  }
  if(!preg_match('/[\w\.]+@[\w\.]+\.[\w\.]/',$_POST['email'])){
    err('email填：xxx@xxx.xxx(.xx) 格式');
  }
  if(isset($_COOKIE['regimcode'])){
    if($_POST['imcode']!=$_COOKIE['regimcode'])
      err('验证码不符！');
  }

  //连接mysqkl数据库
  if(!$db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
    err('数据库['.$sql['host'].']连接不成功！');
  }
  //选择数据库并判断
  if(!@mysql_select_db($sql['name'],$db)){
    err('数据库['.$sql['name'].']连接不成功！');
  }
  mysql_query('SET NAMES '.$sql['char'].'');
  
  $result=mysql_query('SELECT * FROM yzsoumember WHERE username="'.$_POST['username'].'"',$db);
  if($result){
    if($row=mysql_fetch_assoc($result)){
	  if($row['password_question']!=$_POST['password_question']){
	    err('密码问题与注册时所填不符！');
	  }elseif($row['password_answer']!=$_POST['password_answer']){
	    err('密码答案不对！');
	  }elseif($row['email']!=$_POST['email']){
	    err('邮箱与注册邮箱不符！');
	  }else{
	    err('为你找回密码为['.$row['password'].'] 请前往<a href="user_login.php">登录</a>');
	  }

    }else{
      err('数据库查无结果！');
    }
  }else{
    err('用户名：'.$_POST['username'].' 数据库查无结果！');
  }








}else{
  err('参数出错！');
}


?>



