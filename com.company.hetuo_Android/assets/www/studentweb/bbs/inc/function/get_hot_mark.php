<?php

//热门栏目
function get_hot_mark(){
  $text='暂无记录';
  if($list=file('inc/require/hot_mark.txt')){
    $list=preg_replace('/^\d+\|([\d\_]+)\|([^\|]+)/','<a href="index.php?area_id=$1">$2</a> ',$list);
	$text=@implode('',$list);
  }
  return $text;
}

?>