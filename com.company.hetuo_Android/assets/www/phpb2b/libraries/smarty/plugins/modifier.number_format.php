<?php
/*
 * Smarty plugin
 *
-------------------------------------------------------------
 * File:     modifier.number_format.php
 * Type:     modifier
 * Name:     number_format
 * Version:  1.0
 * Date:     May 1st, 2002
 * Purpose:  pass value to PHP number_format() and return result
 * Install:  Drop into the plugin directory.
 * Author:   Jason E. Sweat <jsweat_php@yahoo.com>
 *
-------------------------------------------------------------
 */
function smarty_modifier_number_format($string, $places)
{
    return number_format($string, $places);
}

/* vim: set expandtab: */

?>
