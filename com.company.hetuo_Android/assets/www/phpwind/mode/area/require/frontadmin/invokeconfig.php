<?php
!defined('M_P') && exit('Forbidden');
InitGP(array('step','invokename'),'GP');

require_once(M_P.'require/invokeconfig.php');
$invokedata	= $invokeService->getInvokeByName($invokename);
if (!$step) {
	if ($invokedata['ifloop']) {
		if (!((int)$cateid)) {
			@include_once(D_P.'data/bbscache/forumcache.php');
		} else {
			$forumcache	= getForumOption($cateid);
		}
		foreach ($invokedata['loops'] as $value) {
			$forumcache = str_replace('<option value="'.$value.'">','<option value="'.$value.'" selected>',$forumcache);
		}
		$loopmode	= '<select name="loops[]" size="16" multiple>'.$forumcache."</select>";
		$loopmode	.= '<input type="hidden" name="cateid" value="'.$cateid.'">';
	} else {
		@include_once(D_P.'data/bbscache/forum_cache.php');
		$forumoptions = '<option value="">'.getLangInfo('other','maketpl_siteinvoke').'</option><option value="fid">'.getLangInfo('other','maketpl_forumcommon').'</option>';
		if (!intval($cateid)) {
			foreach ($forum as $key => $value) {
				if ($value['type'] == 'forum') {
					$forumoptions .= '<option value="'.$value['fid'].'">'.$value['name'].'</option>';
				}
			}
		} else {
			foreach ($forum as $key => $value) {
				if ($value['type'] == 'forum' && getCateid($key) == $cateid) {
					$forumoptions .= '<option value="'.$value['fid'].'">'.$value['name'].'</option>';
				}
			}
		}

	}
	$invokepieces = $invokeService->getInvokePieceByInvokeName($invokename);
	foreach ($invokepieces as $key => $piece) {
		$temp = $invokeService->getStampBlocks($piece['action']);
		if ($piece['action']=='subject' || $piece['action']=='image') {
			$temp_func = getLangInfo('other','set_invoke_type').'<select name="func['.$piece['id'].']">';
			foreach ($temp as $value) {
				$selected = $value['func'] == $piece['func'] ? 'selected' : '';
				$temp_func .= '<option value="'.$value['func'].'" '.$selected.'>'.$value['name'].'</option>';
			}
			$temp_func .= '</select>';
			$invokepieces[$key]['func'] = $temp_func;
			if ($invokedata['ifloop']) {
				$invokepieces[$key]['rang'] = '<tr><td colspan="2"><input type="hidden" name="rang['.$piece['id'].']" value="0"></td></tr>';
			} else {
				$piece['rang'] = str_replace('<option value="'.$piece['rang'].'">','<option value="'.$piece['rang'].'" selected>',$forumoptions);
				$invokepieces[$key]['rang'] = getLangInfo('other','set_invoke_fid').'<select name="rang['.$piece['id'].']">'.$piece['rang'].'</select>';
			}
		} else {
			$invokepieces[$key]['func'] = '<input type="hidden" name="func['.$piece['id'].']" value="'.$piece['func'].'">';
			$temp_rang = getLangInfo('other','set_invoke_type').'<select name="rang['.$piece['id'].']">';
			foreach ($temp as $value) {
				$selected = $value['rang'] == $piece['rang'] ? 'selected' : '';
				$temp_rang .= '<option value="'.$value['rang'].'" '.$selected.'>'.$value['name'].'</option>';
			}
			$temp_rang .= '</select>';
			$invokepieces[$key]['rang'] = $temp_rang;
		}
		$temp_param = array();
		foreach ($piece['param'] as $k=>$value) {
			if ($value!='default') {
				$temp_param[] = '<tr class="tr1 vt"><td class="td1">'.getParamDiscrip($k,$piece['action']).'</td><td class="td2"><input type="text" class="input" value="'.$value.'" name="param['.$piece['id'].']['.$k.']"></td></tr>';
			} else {
				$temp_param[] = '<tr><td colspan="2"><input type="hidden" name="param['.$piece['id'].']['.$k.']" value="'.$value.'"></td></tr>';
			}
		}
		$invokepieces[$key]['param'] = $temp_param;
	}
	include areaLoadFrontView($action);
	ajax_footer();
} else {
	InitGP(array('rang','func','num','param','loops','cachetime'),'GP');
	foreach ($num as $key=>$value) {
		$temp = array();
		$temp['num']	= (int)$value;
		$temp['func']	= $func[$key];
		$temp['rang']	= $rang[$key];
		$temp['param']	= $param[$key];
		$temp['cachetime']	= $cachetime[$key];
		$invokeService->updateInvokePieceById($key,$temp);
		$piece = $invokeService->getInvokePieceByInvokeId($key);
		$invokeService->updateCacheDataPiece($piece['id']);
	}
	//$update_invoke	= array('descrip'=>$descrip);
	if ($invokedata['ifloop']) {
		$update_invoke = array();
		@include_once(D_P.'data/bbscache/forum_cache.php');
		foreach ($loops as $key=>$value) {
			if ($forum[$value]['type'] == 'category') {
				unset($loops[$key]);
			}
		}
		if ((int)$cateid) {
			$invoke = $invokeService->getInvokeByName($invokename);
			!$invoke['loops'] && $invoke['loops'] = array();
			foreach ($invoke['loops'] as $key=>$value) {
				if (getCateid($value)==$cateid) {
					unset($invoke['loops'][$key]);
				}
			}
			$loops = array_merge($invoke['loops'],$loops);
		}
		$update_invoke['loops']	= $loops;
		$invokeService->updateInvokeByName($invokename,$update_invoke);
	}


}
?>