<?php

//栏目分类设置
  require('inc/set_area.php');
  require('inc/function/confirm_manager.php');
  require('inc/function/filter.php');
  require('inc/function/write_file.php');
  if(!(confirm_manager()==true && $cookie[0]==$web['manager'])){
    err('该命令必须以基本管理员'.$web['manager'].'身份登录！请重登录');
  }
  $_POST['column_id']=(array)$_POST['column_id'];
  $area_new=array();
  if(count($_POST['column_id'])>0){
    if(count($_POST['column_id'])>count(array_unique($_POST['column_id'])))
      err('新分论坛不能有相同的ID序号！');
    if(count($_POST['column_name'])>count(array_filter(preg_replace('/^\s+|\s+$/','',$_POST['column_name']))))
      err('新分论坛名称不能有空值！');
    foreach($_POST['column_id'] as $fid_old=>$fid_new){
      $area_new[$fid_new][0]=filter1($_POST['column_name'][$fid_old]); //新子论坛名称
	
	  $_POST['class_id'][$fid_old]=(array)$_POST['class_id'][$fid_old];
      if(count($_POST['class_id'][$fid_old])>0){
        if(count($_POST['class_id'][$fid_old])>count(array_unique($_POST['class_id'][$fid_old])))
          err('新分论坛'.$fid_new.'的版区不能有相同的ID序号！');
        if(count($_POST['class_name'][$fid_old])>count(array_filter(preg_replace('/^\s+|\s+$/','',$_POST['class_name'][$fid_old]))))
          err('新分论坛'.$fid_new.'的版区'.$cid_new.'名称不能有空值！');
        foreach($_POST['class_id'][$fid_old] as $cid_old=>$cid_new){
          $area_new[$fid_new][$cid_new]=filter1($_POST['class_name'][$fid_old][$cid_old]);
        }
        @ksort($area_new[$fid_new]);
	  }else{
        err('新分论坛'.$fid_new.'的版区不能没有栏目！');
	  }
	}
    @ksort($area_new);
  }

  if($web['area']==$area_new){
    err("您对版区设置未做任何更改！");
  }
  if(!$area_new){
    $errr='<br /><font color=red>注：你的设置已造成全部栏目被清空</font>';
  }
  $text='<?php
$web[\'area\'] = '.var_export($area_new,true).';
?>';


  //写
  write_file('inc/set_area.php',$text);
  alert('栏目分类设置成功！'.$errr,'admin_set_area.php');






?>