<?php
/**
 * Smarty change design-time path to runtime path plugin
 *
 * File:     prefilter.chpath.php<br>
 * Type:     prefilter<br>
 * Name:     chpath<br>
 * Date:     May 11, 2006<br>
 * Purpose:  change design-time path to runtime path such as <link> <img src>               
 *           
 * Install:  Drop into the plugin directory, call
 *           <code>$smarty->load_filter('pre','chpath');</code>
 *           from application.
 * @author   sleetdrop <sleetdrop at gmail dot com>
 * @author Contributions from Gdj [http://community.csdn.net/Expert/TopicView3.asp?id=4645962]
 * @version  1.0
 * @param string
 * @param Smarty
 */

function smarty_prefilter_chpath($tpl_source,&$smarty)
{
    return preg_replace("/(<(img.*?src|link.*?href|script.*?src)=([\"']))(.*?)(\\3.*?>)/is","$1".TMP_DIR."$4$5",$tpl_source);
}       


?>