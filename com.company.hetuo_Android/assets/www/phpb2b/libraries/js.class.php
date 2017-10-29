<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision$
 */

// 禁止直接访问该页面
if (basename($_SERVER['PHP_SELF']) == "js.class.php") {
    header("HTTP/1.0 404 Not Found");
    exit;
}
class JS extends PbObject
{
    function JS(){}
    
    /**
     *　返回上页
     * @param $step 返回的层数 默认为1
     */
    function Back($step = -1)
    {
        $msg = "history.go(".$step.");";
        JS::_Write($msg);
        JS::FreeResource();
        exit;
    }

    /**
     * 弹出警告的窗口
     * @param $msg 警告信息
     */
    function Alert($msg)
    {
        $msg = "alert(\"".$msg."\");";
        JS::_Write($msg);
    }
    /**
     * 写js
     * @param $msg
     */
    function _Write($msg)
    {
        echo "<script language=\"javascript\">\n";
        echo $msg;
        echo "\n<\/script>";
    }

    /**
     * 刷新当前页
     */
    function Reload()
    {
        $msg = "location.reload();";
        JS::FreeResource();
        JS::_Write($msg);
        exit;
    }
    /**
     * 刷新弹出父页
     */
    function ReloadOpener()
    {
        $msg = "if (opener)    opener.location.reload();";
        JS::_Write($msg);
    }

    /**
     * 跳转到url
     * @param $url 目标页
     */
    function Goto($url)
    {
        $msg = "location.href = '$url';";
        JS::FreeResource();
        JS::_Write($msg);
        exit;
    }
    /**
     * 关闭窗口
     */
     function Close()
     {
         $msg = "window.opener=null;window.close()";
        JS::FreeResource();
        JS::_Write($msg);
        exit;
        
     }
    /**
     * 提交表单
     * @param $frm 表单名
     */
    function Submit($frm)
    {
        $msg = $frm.".submit();";
        JS::_Write($msg);
    }
}
?>