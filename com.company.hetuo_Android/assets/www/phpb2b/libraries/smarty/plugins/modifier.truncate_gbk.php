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
    $strlen=$start+$len;
    for($i=0;$i<$strlen;$i++){
        if(ord(substr($str,$i,1))>0xa0){
            $tmpstr.=substr($str,$i,2);
            $i++;
        }else {
            $tmpstr.=substr($str,$i,1);
        }
    }
    return $tmpstr;
}

/* vim: set expandtab: */


?>
