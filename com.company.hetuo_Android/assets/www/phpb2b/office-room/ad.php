<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
require("../libraries/common.inc.php");
require("room.share.php");
uses("adzone");
$tpl_file = "ads";
$adzone = new Adzones();
$result = $adzone->findAll("*",null, $conditions, " id desc");
setvar("Adzones",$result);
template($tpl_file);
?>