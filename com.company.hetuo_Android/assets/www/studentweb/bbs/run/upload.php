<?php


require('inc/function/confirm_login.php');
if(confirm_login()==false)
  die('<script>alert("提示：只有注册会员能上传，不过你可以选择链接形式");</script>');
if($cookie[2]!='manager' && (strstr($cookie[2],'u') || strstr($cookie[2],'a') || strstr($cookie[2],'t')))
  die('<script> alert("上传失败！你已被限制上传！请查看自己权限，或联系管理员"); </script>');
if(abs($cookie[1])<$web['up_start'])
  die('<script> alert("上传失败！系统限定用户上传等级分为'.$web['up_start'].'，您现在的等级分为'.abs($cookie[1]).'"); </script>');
if(!($_SERVER['HTTP_REFERER'] && strpos($_SERVER['HTTP_REFERER'],'http://'.$_SERVER['HTTP_HOST'])===0))
  die('<script>alert("跨域操作越权！");</script>');
if(!$_GET['in_id'] || $_GET['in_id']<15 || $_GET['in_id']>18)
  die('<script>alert("出现错误！请暂时停止上传。文件类型参数缺失");</script>');

$inis = ini_get_all();
$uploadmax=$inis['upload_max_filesize'];
/*
[global_value] => 2M
[local_value] => 2M
[access] => 6
*/
if(!is_array($_FILES['uploadfile']) || !$_FILES['uploadfile']['size'])
  die('<script>alert("出现错误！请暂时停止上传。\n原因分析：\n1、上传数组参数为空 \n2、文件超过系统最大上传限量'.$uploadmax['global_value'].'（'.$uploadmax['local_value'].'）");</script>');
if($_FILES['uploadfile']['size']>$web['max_file_size'][$_GET['in_id']]*1024){
  die('<script> alert("对不起，上传的文件请小于'.$web['max_file_size'][$_GET['in_id']].'KB"); </script>');
}

switch($_GET['in_id']){
  case 15:
    $reg='gif|jpg|jpeg';
    break;
  case 16:
    $reg='swf';
    break;
  case 17:
    if($web['_17_type'])
      $reg=$web['_17_type'];
    else
      $reg='wav|wma|wmv|mid|midi|avi|mp3|mpg|mpeg|asf|asx|mov|rm|rmvb|ram|ra';
    break;
  default:
    if($web['_18_type'])
      $reg=$web['_18_type'];
    else
      $reg='rar|zip|exe|doc|xls|chm|hlp';
    break;
}
if(!preg_match('/\.('.$reg.')$/i',$_FILES['uploadfile']['name'],$matches))
  die('<script>alert("提示！请选择一个有效的文件：允许的格式有（'.$reg.'）");</script>');

$date=gmdate('YmdHis',time()+(floatval($web['time_pos'])*3600));
if($pics=@glob('data/upload/'.urlencode($cookie[0]).'_'.$date.'_*')){
  $nn=count($pics);
  $up_max=ceil(substr(strpos($cookie[1],'_'))/20);
  if($nn>$up_max){
    die('<script> alert("对不起，您每日最多只能上传'.$up_max.'个文件。不过在修改该文时你可更换图片"); </script>');
  }
  //文件命名
  $upload_filename=urlencode($cookie[0]).'_'.$date.'_'.($nn+1).'.'.strtolower($matches[1]);
}else{
  $upload_filename=urlencode($cookie[0]).'_'.$date.'_1.'.strtolower($matches[1]);
}
//上传临时路径

if(!file_exists('data')){
  if(!mkdir('data')){
    die('<script>alert("无法创建上传目录data！可能权限不足");</script>');
  }
}
$upload_dir='data/upload';
if(!file_exists($upload_dir)){
  if(!mkdir($upload_dir)){
    die('<script>alert("无法创建上传目录'.$upload_dir.'！可能权限不足");</script>');
  }
}



@chmod($upload_dir,0777);

if(move_uploaded_file($_FILES['uploadfile']['tmp_name'],$upload_dir.'/'.$upload_filename)) {
  if($web['water']==1 && @extension_loaded("gd")){ //加水印
    require('inc/function/img.php');
    run_img_watermark($upload_dir.'/'.$upload_filename,$web['pic_markwords'],$web['pic_quality']); //处理水印
  }

  die('
  <script language="javascript" type="text/javascript">
  <!--
  try{
    window.parent.insertFile("'.$_REQUEST['pathUrl'].''.$upload_dir.'/'.$upload_filename.'",'.$_GET['in_id'].');
    alert("上传成功！");
  }catch(err){
    alert(err);
  }
  -->
  </script>');
}else{
  die('<script>alert("出现错误！请暂时停止上传");</script>');
}


?>



