<?php

//取日期
function get_date($v,$n=19){
  return substr(preg_replace('/.*(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2}).*/','$1/$2/$3 $4:$5:$6',$v),0,$n);
}

?>