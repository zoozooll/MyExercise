<?php
/*
 * Smarty plugin
 * -------------------------------------------------------------
 * Type:     modifier
 * Name:     truncate
 * Purpose:  Truncate a string to a certain length if necessary,
 *           optionally splitting in the middle of a word, and 
 *           appending the $etc string.
 * Update:   change substr to msubstr and add fun msubstr.so it
 *           can bu used in chinese charset.
 * -------------------------------------------------------------
 */
function smarty_modifier_truncate($string, $length = 80, $etc = '...',
                                  $break_words = false)
{
    if ($length == 0)
        return '';

    if (strlen($string) > $length) {
        $length -= strlen($etc);    
        if (!$break_words)
        $string = preg_replace('/s+?(S+)?$/', '', msubstr($string, 0, $length+1));
      
        return msubstr($string, 0, $length).$etc;
    } else
        return $string;
}

function msubstr($str,$start,$len){
	for($i=0;$i<$len;$i++)
	{
		$temp_str=substr($str,0,1);
		if(ord($temp_str) > 127)
		{
			$i++;
			if($i<$len)
			{
				$new_str[]=substr($str,0,3);
				$str=substr($str,3);
			}
		}
		else
		{
			$new_str[]=substr($str,0,1);
			$str=substr($str,1);
		}
	}
	return join($new_str);
}

/* vim: set expandtab: */


?>
