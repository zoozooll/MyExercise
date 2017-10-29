<?php

/*网站设置*/
 require('inc/set_sql.php');
 require('inc/function/confirm_manager.php');
 require('inc/function/filter.php');
 require('inc/function/write_file.php');
  if(!(confirm_manager()==true && $cookie[0]==$web['manager'])){
    err('该命令必须以基本管理员'.$web['manager'].'身份登录！请重登录');
  }
  //论坛相关
  
  if(!($_POST['manager']!='' && preg_match('/^[\x80-\xff\w]{3,45}$/',$_POST['manager']))){
    err('管理员起始名称不能空且请用汉字、数字、英文及下划线组成！长度范围为3-45字符！');
  }
  if($_POST['password']==''){
    err('管理员密码不能留空！');
  }
  if(preg_match('/[\s\r\n]+/',$_POST['password'])){
    err('管理员密码不能有空格！');
  }
  if(strlen($_POST['password'])>30 || strlen($_POST['password'])<3){
    err('管理员密码长度是3-30字符！');
  }
  $_POST['_17_type']=strtolower(preg_replace('/^\||\|$/','',preg_replace('/\|{2,}/','|',$_POST['_17_type'])));
  $_POST['_18_type']=strtolower(preg_replace('/^\||\|$/','',preg_replace('/\|{2,}/','|',$_POST['_18_type'])));
  if(preg_match('/[^a-zA-Z0-9\|]+/',$_POST['_17_type']))
    err('允许上传的影音文件只应用合理的后缀名及分隔符|填写！');
  if(preg_match('/[^a-zA-Z0-9\|]+/',$_POST['_18_type']))
    err('允许上传的其它文件只应用合理的后缀名及分隔符|填写！');
  //过滤词汇
  $_POST['badwords']=preg_replace('/[^0-9a-z\/\x80-\xff]+/i','',$_POST['badwords']);
  $badwords_arr=@explode('/',preg_replace('/^\/+|\/+$/','',preg_replace('/\/{2,}/','/',$_POST['badwords'])));
  $badwords_arr=array_unique(array_filter($badwords_arr));
  $badwords=@implode('/',$badwords_arr);
  unset($badwords_arr);
  
  $text_web='
//基本设置：
$web[\'code_author\']=\' - power by 162100.com\';
$web[\'code_version\']=\'V5\';
$web[\'sitehttp\']=\'http://\'.(!empty($_SERVER[\'HTTP_X_FORWARDED_HOST\'])?$_SERVER[\'HTTP_X_FORWARDED_HOST\']:$_SERVER[\'HTTP_HOST\']).\'/\';  //站点网址
$web[\'path\']=dirname(trim($web[\'sitehttp\'],\'/\').$_SERVER[\'SCRIPT_NAME\']).\'/\';  //路径
$web[\'sitename\']=\''.(($_POST['sitename']!='')?filter2($_POST['sitename']):'我的网站').'\';  //站点名称
$web[\'description\']=\''.filter2($_POST['description']).'\';  //站点描述
$web[\'keywords\']=\''.filter2($_POST['keywords']).'\';  //关键字
$web[\'manager\']=\''.$_POST['manager'].'\';  //基础管理员名称
$web[\'password\']=\''.$_POST['password'].'\';  //基础管理员密码，注：系统出现一切故障时以基础管理员名称和密码为准
$web[\'time_pos\']=\''.filter2($_POST['time_pos']).'\';  //服务器时区调整

//控制设置：
$web[\'stop_reg\']='.abs((int)$_POST['stop_reg']).';  //禁止用户注册1禁止0不禁止
$web[\'pagesize\']='.(abs((int)$_POST['pagesize'])>0?abs((int)$_POST['pagesize']):15).';  //每页显示数量
$web[\'list_wordcount\']='.(abs((int)$_POST['list_wordcount'])>0?abs((int)$_POST['list_wordcount']):50000).';  //发表文章最多字符数
$web[\'re_wordcount\']='.(abs((int)$_POST['re_wordcount'])>0?abs((int)$_POST['re_wordcount']):5000).';  //评论回复最多字符数
$web[\'pagesize\']='.(abs((int)$_POST['pagesize'])>0?abs((int)$_POST['pagesize']):15).';  //每页显示数量
$web[\'badwords\']=\''.$badwords.'\';  //过滤词汇
$web[\'link_start\']='.abs((int)$_POST['link_start']).';          //文章允许加链接起始分

//上传设置
$web[\'max_file_size\'][15]='.abs((int)$_POST['max_face_size_15']).';  //限定上传图片尺寸（单位KB）
$web[\'max_file_size\'][16]='.abs((int)$_POST['max_face_size_16']).';  //限定上传动画尺寸（单位KB）
$web[\'max_file_size\'][17]='.abs((int)$_POST['max_face_size_17']).';  //限定上传影音文件尺寸（单位KB）
$web[\'max_file_size\'][18]='.abs((int)$_POST['max_face_size_18']).';  //限定上传其它文件尺寸（单位KB）
$web[\'_17_type\']=\''.$_POST['_17_type'].'\';  //设置允许上传的影音文件类型，用|分开写
$web[\'_18_type\']=\''.$_POST['_18_type'].'\';  //设置允许上传的其它文件类型，用|分开写

//图片处理设置
$web[\'water\']='.($_POST['water']?abs((int)$_POST['water']):0).';                //1图片加水印 0否
$web[\'spic_w\']='.($_POST['spic_w']?abs((int)$_POST['spic_w']):140).';  //缩略图宽
$web[\'spic_h\']='.($_POST['spic_h']?abs((int)$_POST['spic_h']):105).';  //缩略图高
$web[\'pic_markwords\']=\''.filter2($_POST['pic_markwords']).'\';  //水印文字
$web[\'pic_quality\']='.(abs((int)$_POST['pic_quality'])).';  //上传图片质量

//发表设置
$web[\'guest_write\']='.abs((int)$_POST['guest_write']).';  //1允许过客发表 0不允许
$web[\'write_start\']='.abs((int)$_POST['write_start']).';           //发表权限起始分，如：禁止新人发表可设高一点分值
$web[\'up_start\']='.abs((int)$_POST['up_start']).';           //上传权限起始分
$web[\'re_update\']='.abs((int)$_POST['re_update']).';  //1回复时将主题文章提前 0自然顺序
$web[\'loginadd\']='.abs((int)$_POST['loginadd']).';  //用户登录加等级分数
$web[\'writeadd\']='.abs((int)$_POST['writeadd']).';  //用户发表主题加等级分数
$web[\'class_iron\']='.abs((int)$_POST['class_iron']).';  //铁级用户等级分标准
$web[\'class_silver\']='.abs((int)$_POST['class_silver']).';  //银级用户等级分标准
$web[\'class_gold\']='.abs((int)$_POST['class_gold']).';  //金级用户等级分标准，大于此数量的为砧级用户

//广告设置
$web[\'top_expires\']='.abs((int)$_POST['top_expires']).';  //置顶期限，一般30天
$web[\'top_price\']='.preg_replace('/[^\d\.]+/','',$_POST['top_price']).';  //置顶每条每天价格
';

  $text='<?php
error_reporting(E_ERROR | E_WARNING | E_PARSE);
if(PHP_VERSION<"4.1.0"){
  $_GET=&$HTTP_GET_VARS;
  $_POST=&$HTTP_POST_VARS;
  $_COOKIE=&$HTTP_COOKIE_VARS;
  $_SERVER=&$HTTP_SERVER_VARS;
  $_ENV=&$HTTP_ENV_VARS;
  $_FILES=&$HTTP_POST_FILES;
}



/* ----------【网站设置】能不用尽量不要用特殊符号，如 \ / : ; * ? \' < > | ，必免导致错误--------- */
'.$text_web.'

?>';

  
  $sqlaccess='';
  if($_POST['manager']!=$web['manager'] || $_POST['password']!=$web['password']){
    if($db=@mysql_connect($sql['host'],$sql['user'],$sql['pass'])){
      if(@mysql_select_db($sql['name'],$db)){
        @mysql_query('SET NAMES '.$sql['char'].'');
        @mysql_query('UPDATE yzsoumember SET username="'.$_POST['manager'].'",password="'.$_POST['password'].'" WHERE username="'.$web['manager'].'"',$db);
      }
      if(@mysql_affected_rows()>0){
        $sqlaccess='<br />数据库管理员名称和密码副本同时更新成功。由于管理员信息被改动，建议重新登录以使新设置生效！';
      }
      @mysql_close();
    }
    if($sqlaccess==''){
      err('<font color=#FF5500>由于基础管理员信息(如名称或密码)有变，但数据库(或数据表)连接不成功或被删除或尚未安装，拒绝更改设置！<br />请检查数据库及数据表</font>');
    }
  }
  /* 写 */
  @chmod('inc',0777);
  write_file('inc/set.php',$text);
  alert('执行成功！'.$sqlaccess,'admin_set.php');




?>