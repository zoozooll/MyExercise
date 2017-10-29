<?php


  //友情链接
require('inc/function/confirm_manager.php');
require('inc/function/filter.php');
  if(!(confirm_manager()==true && $cookie[0]==$web['manager'])){
    err('该命令必须以基本管理员'.$web['manager'].'身份登录！请重登录');
  }
  if(!$_POST['linkname'] || !$_POST['linkhttp'])
    err('参数不全！');
  //if(count((array)$_POST['linkhttp'])>count(array_unique(array_filter((array)$_POST['linkhttp']))))
  //  err('相同的网址只允许加入一个！且不能写空值');
  $link=array();
  foreach($_POST['linkhttp'] as $key=>$val){
    $linkname=filter1($_POST['linkname'][$key]);
    if($linkname!='' && ($val=trim($val))!='' && (strlen($val)<100 && preg_match('/^[a-zA-Z]+\:\/\/[\w\-\.]+\S{2,}$/',$val))){
	  if(!preg_match('/(162100|furuijinzhao|yzsou)\.com/i',$val)){
        $link[]="<a href=\"".$val."\" target=\"_blank\">".$linkname."</a>";
	  }
    }
    unset($linkname);
  }
  require('inc/function/write_file.php');
  @chmod('inc',0777);
  @chmod('inc/require',0777);
  write_file('inc/require/frienlink.txt','<a href="http://www.furuijinzhao.com/" target="_blank">沈阳保洁公司</a>　<a href="http://www.furuijinzhao.com/" target="_blank">沈阳保安公司</a>　<a href="http://www.162100.com/" target="_blank">162100网址导航</a>　<a href="http://www.yzsou.com/" target="_blank">一站搜网</a>　'.@implode("　",$link));
  alert('友情链接设置成功(官方链接默认保留，谢谢支持)！刷新首页显示','admin_friendlink.php');


?>



