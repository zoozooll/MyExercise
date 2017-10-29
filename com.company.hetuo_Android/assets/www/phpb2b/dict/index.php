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
define('CURSCRIPT', 'index');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("dicttype","dict");
$dict = new Dicts();
$dicttype = new Dicttypes();
//get dictionary types.
$dict_types = $dicttype->getAllTypes();
setvar("Dictypes", $dict_types);
render("dict.index");
?>