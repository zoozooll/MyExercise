<?php

//页数
function get_page($n){
  global $web;
  $_REQUEST['p']=abs($_REQUEST['p']);
  if(!$_REQUEST['p'] || $_REQUEST['p']<1){
    return 1;
  }elseif($n && $_REQUEST['p']>ceil($n/$web['pagesize'])){
    return ceil($n/$web['pagesize']);
  }else{
    return floor($_REQUEST['p']);
  }
}

//页码
function get_page_foot($p,$totallists,$t){
  global $web;
  $text='';
  if($totallists>0 && ($pagesize=abs($web['pagesize']))){
    $totalpages=ceil($totallists/$pagesize);
    if($totalpages==1){
      $text.='<span title="第一页
First Page">|&lt</span> <span title="上一页
Previous Page">&lt</span> <span title="下一页
Next Page">&gt</span> <span title="最后页
Last Page">&gt|</span>';
    }else{
      $first='?p=1'.$t.'';
      $up='?p='.($p-1).''.$t.'';
      $down='?p='.($p+1).''.$t.'';
      $end='?p='.$totalpages.''.$t.'';
      $go='?p=\'+pId.value+\''.$t.'';
      if($p==1){
        $text.='<span title="第一页
First Page">|&lt</span> <span title="上一页
Previous Page">&lt</span> <a href="'.$down.'" title="下一页
Next Page">&gt</a> <a href="'.$end.'" title="最后页
Last Page">&gt|</a>';
      }elseif($p==$totalpages){
        $text.='<a href="'.$first.'" title="第一页
First Page">|&lt</a> <a href="'.$up.'" title="上一页
Previous Page">&lt</a> <span title="下一页
Next Page">&gt</span> <span title="最后页
Last Page">&gt|</span>';
      }else{
        $text.='<a href="'.$first.'" title="第一页
First Page">|&lt</a> <a href="'.$up.'" title="上一页
Previous Page">&lt</a> <a href="'.$down.'"title="下一页
Next Page">&gt</a> <a href="'.$end.'" title="最后页
Last Page">&gt|</a>';
      }
      $text2=' <input id="pageGoBtn" type="button" value="跳至" title="Skip" onclick="var pId=document.getElementById(\'pageGo\');if(!isNaN(pId.value)&&pId.value>=1&&pId.value<='.$totalpages.')location.href=\''.$go.'\';" /><input id="pageGo" name="pageGo" type="text" size="1" />页';
    }
    return '<br />
  <div id="page"'.($totallists<=$pagesize?' class="page"':'').'>
  '.$pagesize.'条/页 共'.$totallists.'条 <font color="#FF6600"> '.$p.' </font>/'.$totalpages.'页 '.$text.' 
   '.$text2.' 
  </div>';
  }
}

?>