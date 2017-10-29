<?php

//身份图像
function user_class($point){
  global $web,$cookie;
  if($cookie[2]=='manager'){
    $image='<a href="admin.php" title="管理员，点击进入管理后台"><img src="images/manager.gif" align="texttop" alt="管理员，点击进入管理后台"></a>';
  }else{
    if($point>=0 && $point<=abs($web['class_iron'])){
      $image='<img src="images/iron_l.gif"><img src="images/iron.gif" width="'.(ceil($point/(abs($web['class_iron'])/10)*3)+3).'" height="10" alt="积分'.$point.'-铁级用户"><img src="images/iron_r.gif">';
    }elseif($point>abs($web['class_iron']) && $point<=abs($web['class_silver'])){
      $image='<img src="images/silver_l.gif"><img src="images/silver.gif" width="'.(ceil($point/(abs($web['class_silver'])/10)*3)+3).'" height="10" alt="积分'.$point.'-银级用户"><img src="images/silver_r.gif">';
    }elseif($point>abs($web['class_slive']) && $point<=abs($web['class_gold'])){
      $image='<img src="images/gold_l.gif"><img src="images/gold.gif" width="'.(ceil($point/(abs($web['class_gold'])/10)*3)+3).'" height="10" alt="积分'.$point.'-金级用户"><img src="images/gold_r.gif">';
    }else{
      $image='<img src="images/diamond_'.(ceil($point/abs($web['class_gold']))-1).'.gif" align="texttop" alt="积分'.$point.'-'.(ceil($point/abs($web['class_gold']))-1).'钻级用户">';
    }
  }
  return $image;
}

?>