<?php
!defined('M_P') && exit('Forbidden');
include_once(D_P.'data/bbscache/forum_cache.php');
require_once(M_P.'require/invokeconfig.php');
require_once(M_P.'require/tagrelate.php');

InitGP(array('invokepieceid','loopid','step'));

$invokepiece = $invokeService->getInvokePieceByInvokeId($invokepieceid);

$tempfid	= $fid;
if ($invokepiece['rang']!='fid') {
	$tempfid = 0;
}

if (!$step) {
	InitGP(array('pushtid','addtype','offset'),null,2);
	$default	= array();
	if ($pushtid) {
		require(R_P.'lib/tplgetdata.class.php');
		$default	= getSubjectByTid($pushtid,$invokepiece['param']);
	} else {
		foreach ($invokepiece['param'] as $key=>$value) {
			$default[$key] = '';
		}
	}
	if (isset($invokepiece['param']['tagrelate'])) {
		$iftagrelate = 1;
	} else {
		$iftagrelate = 0;
	}
	$stroffset = $offset+1;
	$addtype ? $addtype_Y = 'checked' : $addtype_N = 'checked';
	include areaLoadFrontView($action);
} else {
	InitGP(array('param','offset','addtype','starttime','endtime','title1','title2','title3','title4','titletime'),'GP');

	if (isset($invokepiece['param']['tagrelate'])) {
		InitGP(array('tagrelate'));
		$param['tagrelate']	= getTagRelate($tagrelate);
	}
	if ($param['url']) {
		$param['url'] = str_replace('&#61;','=',$param['url']);
	}
	if(isset($param['image']) && count($_FILES) && $_FILES["uploadpic"]["name"] && $_FILES["uploadpic"]["size"]) {
		$uploadPicUrl = $invokeService->uploadPicture($_FILES, $invokepieceid, $windid);
		$param['image'] = $uploadPicUrl ? $uploadPicUrl : $param['image'];
	}
	$titlecss = $invokeService->pushDataTitleCss($title1,$title2,$title3,$title4,$titletime);
	$offset = (int) $offset;
	if ($addtype) {
		$invokeService->addPushData(array('invokename'=>$invokepiece['invokename'],'invokepieceid'=>$invokepieceid,'fid'=>$tempfid,'loopid'=>$loopid,'editor'=>$windid,'starttime'=>$starttime,'endtime'=>$endtime,'offset'=>$offset,'data'=>$param,'titlecss'=>$titlecss));
	} else {
		$invokeService->insertPushData(array('invokename'=>$invokepiece['invokename'],'invokepieceid'=>$invokepieceid,'fid'=>$tempfid,'loopid'=>$loopid,'editor'=>$windid,'starttime'=>$starttime,'endtime'=>$endtime,'offset'=>$offset,'data'=>$param,'titlecss'=>$titlecss));
	}

	$invokeService->updateCacheDataPiece($invokepieceid,$tempfid,$loopid);
}

ajax_footer();
?>