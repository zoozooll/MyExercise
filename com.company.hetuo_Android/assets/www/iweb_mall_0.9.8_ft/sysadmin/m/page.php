<?php echo  str_replace("{num}",$result['countnum'],$a_langpackage->a_page_num);?> 
<a href="<?php echo  $result['firsturl'];?>"><?php echo  $a_langpackage->a_page_first;?></a> 
<a href="<?php echo  $result['preurl'];?>"><?php echo  $a_langpackage->a_page_pre;?></a> 
<a href="<?php echo  $result['nexturl'];?>"><?php echo  $a_langpackage->a_page_next;?></a> 
<a href="<?php echo  $result['lasturl'];?>"><?php echo  $a_langpackage->a_page_last;?></a> 
<?php echo  str_replace("{num}",$result['page'],$a_langpackage->a_page_now);?>/<?php echo  str_replace("{num}",$result['countpage'],$a_langpackage->a_page_count);?>