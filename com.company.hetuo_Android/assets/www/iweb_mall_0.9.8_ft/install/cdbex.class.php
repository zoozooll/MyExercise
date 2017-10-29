<?php

class dbex
{
   public function query($sql)
   {
      if(mysql_query($sql)){
      	return true;
      }else{
      	return false;
      }
   }
}
?>