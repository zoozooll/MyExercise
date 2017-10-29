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
$tplname = "invite";
$invitecode = authcode($_SESSION['MemberID'].$time_stamp.pb_radom(6));
setvar("InviteCode", $invitecode);
template($tplname);
?>