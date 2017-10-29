<?php

function getarea(){
  global $web,$option,$manage;
  foreach((array)$web['area'] as $i=>$area){
    $text.='<h6><a href="index.php?area_id='.$i.'&list_type='.$_REQUEST['list_type'].''.$manage.'">'.$area[0].'</a></h6> ';
    $option.='<option value="'.$i.'">　'.$area[0].'</option>';
    foreach((array)$area as $j=>$class){
      if($j!=0){
        $text.='<a href="index.php?area_id='.$i.'_'.$j.'&list_type='.$_REQUEST['list_type'].''.$manage.'" class="class">'.$class.'</a> ';
      }
    }
    $text.='<br />';
  }
  return $text;
}
?>