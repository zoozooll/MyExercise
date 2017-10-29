<?php
!defined('P_W') && exit('Forbidden');
InitGP(array('keyword'));
require_once(R_P."lib/adminsearch.class.php");
$searchpurview = new AdminSearch($keyword);
$result = $searchpurview->search();
include PrintEot('search');exit;
?>