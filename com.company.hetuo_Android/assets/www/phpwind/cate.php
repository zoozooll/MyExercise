<?php
define('SCR','cate');
require_once('global.php');
$_GET['fid'] = (int)GetGP('cateid');

$m = 'area';
selectMode($m);

if (defined('M_P') && file_exists(M_P.'cate.php')) {
	require_once M_P.'cate.php';
} else {
	InitGP(array('cateid'),'GP',2);
	empty($cateid) && Showmsg('data_error');
	ObHeader('index.php?m=bbs&cateid='.$cateid);
}
?>