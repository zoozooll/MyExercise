<?php
!defined('M_P') && exit('Forbidden');
include_once(D_P.'data/bbscache/forum_cache.php');
require_once(M_P.'require/invokeconfig.php');
require_once(M_P.'require/tagrelate.php');

InitGP(array('id','step'),'GP');

$push	= $invokeService->getPushDataById($id);
if (!$push)  showmsg('error');


$invokepiece = $invokeService->getInvokePieceByInvokeId($push['invokepieceid']);

if (!$step) {
	if ($push['endtime']) {
		$push['endtime'] = get_date($push['endtime'],'Y-m-d H:i');
	} else {
		$push['endtime'] = '';
	}
	if ($push['starttime']) {
		$push['starttime'] = get_date($push['starttime'],'Y-m-d H:i');
	} else {
		$push['starttime'] = '';
	}

	$titlecolor	= $push['titlecss']['color'];
	$titletime	= $push['titlecss']['endtime'] ? get_date($push['titlecss']['endtime'],'Y-m-d H:i') : '';
	if ($titlecolor && !preg_match('/\#[0-9A-F]{6}/is',$titlecolor)) {
		$titlecolor = '';
	}
	if ($push['titlecss']['b']=='1') {
		$stylename[1]='b one';
	} else {
		$stylename[1]='b';
	}
	if ($push['titlecss']['u']=='1') {
		$stylename[2]='u one';
	} else {
		$stylename[2]='u';
	}
	if ($push['titlecss']['i']=='1') {
		$stylename[3]='one';
	} else {
		$stylename[3]='';
	}

	if (isset($invokepiece['param']['tagrelate'])) {
		$iftagrelate = 1;
	} else {
		$iftagrelate = 0;
	}

	include areaLoadFrontView($action);
} else {
	InitGP(array('param','offset','endtime','starttime','title1','title2','title3','title4','titletime'),'GP');
	$offset = (int) $offset;

	if (isset($invokepiece['param']['tagrelate'])) {
		InitGP(array('tagrelate'));
		$param['tagrelate']	= getTagRelate($tagrelate);
	}
	if ($param['url']) {
		$param['url'] = str_replace('&#61;','=',$param['url']);
	}
	if(isset($param['image']) && count($_FILES) && $_FILES["uploadpic"]["name"] && $_FILES["uploadpic"]["size"]) {
		$uploadPicUrl = $invokeService->uploadPicture($_FILES, $push['invokepieceid'], $windid);
		$param['image'] = $uploadPicUrl ? $uploadPicUrl : $param['image'];
	}
	$titlecss = $invokeService->pushDataTitleCss($title1,$title2,$title3,$title4,$titletime);
	$invokeService->updatePushData($id,array('starttime'=>$starttime,'endtime'=>$endtime,'offset'=>$offset,'data'=>$param,'titlecss'=>$titlecss));

	$loopid		= $push['loopid'];
	if ($invokepiece['rang']!='fid') {
		$fid = 0;
	} else {
		$fid = $push['fid'];
	}
	$invokeService->updateCacheDataPiece($push['invokepieceid'],$fid,$loopid);
}


ajax_footer();
?>