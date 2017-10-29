<?php
!defined('P_W') && exit('Forbidden');
$purview_index	= 0;
if ($admin_gid=='3' || $admin_gid=='4') {
	$purview_index	= 1;
}
include_once(D_P.'data/bbscache/area_config.php');
require_once(M_P.'require/invokeconfig.php');
require_once(M_P.'require/tagrelate.php');

$invokeService = L::loadClass('InvokeService');
if (!$action) {
	InitGP(array('step'));
	if (!$step) {
		$catedb = $forumdb = $subdb1 = $subdb2 = array();
		$space  = '<i class="lower lower_a"></i>';
		$query = $db->query("SELECT fid,fup,type,name,vieworder,forumadmin,f_type,cms FROM pw_forums WHERE type='category' AND cms!='1' ORDER BY vieworder");
		while ($forums = $db->fetch_array($query)) {
			if (!$purview_index && !checkEditAdmin($admin_name,$forums['fid'])) continue;
			$forums['name'] = preg_replace("/\<(.+?)\>/is","",$forums['name']);//去除html标签
			$forums['name'] = str_replace("<","&lt;",$forums['name']);
			$forums['name'] = str_replace(">","&gt;",$forums['name']);

			$catedb[] = $forums;
		}
		$ajax_basename = EncodeUrl($basename);
	} else {
		InitGP(array('editadmin'));
		$update	= array('area_editadmin','array',serialize($editadmin),'');
		$db->update("REPLACE INTO pw_hack VALUES (".pwImplode($update).')');
		updatecache_conf('area',true);
		adminmsg('operate_success');
	}
} elseif ($action == 'madmin') {
	InitGP(array('step','scr','cateid','user','mgid'));
	$gids = array(3,4);
	$query = $db->query("SELECT gid FROM pw_permission WHERE uid=0 and fid=0 and rkey='areapush' and rvalue=1");
	while ($rt = $db->fetch_array($query)) {
		if (!in_array($rt['gid'],array(3,4,5))) $gids[] = $rt['gid'];
	}
	if (empty($step)) {

		$ajax_basename = EncodeUrl($basename."&action=madmin");
		require_once PrintMode('ajax_tplcontent');
	} elseif ($step == 2) {
		if (empty($user)) {
			adminmsg('设置失败,用户名不能为空');
		}

		$rt = $db->get_one("SELECT uid,groupid FROM pw_members WHERE username=".pwEscape($user));
		if (empty($rt)) {
			adminmsg('用户名不存在');
		}
		if ($mgid && $ltitle[$mgid] && in_array($mgid,$gids)) {//TODO 权限
			$db->update("UPDATE pw_members SET groupid=".intval($mgid)." WHERE uid=".intval($rt['uid']));
		}
		$rt = $db->get_value("SELECT hk_value FROM pw_hack WHERE hk_name='area_editadmin'");
		$area_editadmin = $rt ? unserialize($rt) : array();
		$index = $scr == 'index' ? 'index' : intval($cateid);
		$editadmin = explode(',',$area_editadmin[$index]);
		$editadmin[] = $user;
		$editadmin = array_unique($editadmin);
		$area_editadmin[$index] = trim(implode(',',$editadmin),',');
		$update	= array('area_editadmin','array',serialize($area_editadmin),'');
		$db->update("REPLACE INTO pw_hack VALUES (".pwImplode($update,false).')');
		updatecache_conf('area',true);
		adminmsg('operate_success');
	}
	ajax_footer();exit;
} elseif ($action == 'mgroup') {//会员所在用户组
	$user = GetGP('user');
	if (empty($user)) {
		echo '请输入用户名';ajax_footer();
	}
	$mgid = $db->get_one("SELECT uid,groupid FROM pw_members WHERE username=".pwEscape($user));
	$ltitle['-1'] = '普通会员';
	if ($mgid && $ltitle[$mgid['groupid']]) {
		if ($mgid['groupid'] != -1) {
			echo '属于'.$ltitle[$mgid['groupid']];
		} else {
			echo '属于非系统组用户';
		}
	} else {
		echo '当前用户不存在';
	}
	ajax_footer();
} elseif ($action=='show') {
	InitGP(array('scr','showid'));
	$showid = intval($showid);
	include_once(D_P.'data/bbscache/forum_cache.php');
	$paramfid = 0;
	if ($scr == 'cate') {
		$paramfid	= $invokeService->getMPageConfigFid($showid);
	}
	$invokenames	= $invokeService->getMPageConfigInvoke('area',$scr,$paramfid);
	if (!$invokenames) {
		adminmsg('no_invoke_in_this_page');
	}
	$invokes = $invokeService->getInvokesByNames($invokenames,$showid);
	$invokepieces	= $invokeService->getInvokePiecesByInvokeNames($invokenames);
	$haveDelays	= $invokeService->getHaveDelayPushData(array_keys($invokepieces),$showid);
	foreach ($invokepieces as $key=>$value) {
		$invokes[$value['invokename']]['pieces'][] = $value;
	}
	if ($scr == 'index') {
		$thistitle = $db_modes['area']['m_name'].getLangInfo('other','pushto_index');
	} elseif ($scr == 'cate') {
		$thistitle = $forum[$showid]['name'];
	}
} elseif ($action=='editinvoke') {
	InitGP(array('step','name','scr','cateid'),'GP');

	$invokedata	= $invokeService->getInvokeByName($name);

	if (!$step) {
		if ($invokedata['ifloop']) {
			if (!$cateid) {
				@include_once(D_P.'data/bbscache/forumcache.php');
			} else {
				$forumcache	= getForumOption($cateid);
			}
			foreach ($invokedata['loops'] as $value) {
				$forumcache = str_replace('<option value="'.$value.'">','<option value="'.$value.'" selected>',$forumcache);
			}
			$loopmode	= '<select name="loops[]" class="select_wa" size="16" multiple>'.$forumcache."</select>";
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

		$tpldata	= $invokeService->getTpl($invokedata['tplid']);
		$invokeimage= $tpldata['image'];
		$invokepieces = $invokeService->getInvokePieceByInvokeName($name);
		foreach ($invokepieces as $key => $piece) {
			$temp = $invokeService->getStampBlocks($piece['action']);
			if ($piece['action']=='subject' || $piece['action']=='image') {
				$temp_func = getLangInfo('other','set_invoke_type').'<select class="select_wa" name="func['.$piece['id'].']">';
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
					$invokepieces[$key]['rang'] = getLangInfo('other','set_invoke_fid').'<select class="select_wa" name="rang['.$piece['id'].']">'.$piece['rang'].'</select>';
				}
			} else {
				$invokepieces[$key]['func'] = '<input type="hidden" name="func['.$piece['id'].']" value="'.$piece['func'].'">';
				$temp_rang = getLangInfo('other','set_invoke_type').'<select class="select_wa" name="rang['.$piece['id'].']">';
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
					$temp_param[] = '<tr class="tr1 vt"><td class="td1">'.getParamDiscrip($k,$piece['action']).'</td><td class="td2"><input type="text" class="input input_wa" value="'.$value.'" name="param['.$piece['id'].']['.$k.']"></td></tr>';
				} else {
					$temp_param[] = '<tr><td colspan="2"><input type="hidden" name="param['.$piece['id'].']['.$k.']" value="'.$value.'"></td></tr>';
				}
			}
			$invokepieces[$key]['param'] = $temp_param;
		}
	} else {
		InitGP(array('rang','func','num','param','loops','descrip','cachetime'),'GP');
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
		$update_invoke	= array('descrip'=>$descrip);
		if ($invokedata['ifloop']) {
			@include_once(D_P.'data/bbscache/forum_cache.php');
			foreach ($loops as $key=>$value) {
				if ($forum[$value]['type'] == 'category') {
					unset($loops[$key]);
				}
			}
			if ($cateid) {
				$invoke = $invokeService->getInvokeByName($name);
				!$invoke['loops'] && $invoke['loops'] = array();
				foreach ($invoke['loops'] as $key=>$value) {
					if (getCateid($value)==$cateid) {
						unset($invoke['loops'][$key]);
					}
				}
				$loops = array_merge($invoke['loops'],$loops);
			}
			$update_invoke['loops']	= $loops;
		}
		$invokeService->updateInvokeByName($name,$update_invoke);
		adminmsg('operate_success',$basename.'&action=show&scr='.$scr.'&showid='.$cateid);
	}
} elseif ($action == 'editinvoketpl') {
	InitGP(array('step','name','scr','cateid'),'GP');

	$invokedata	= $invokeService->getInvokeByName($name);
	if ($step) {
		$tagcode = miniChar_cv($_POST['tagcode']);
		if (checkTplContent($tagcode)) {
			adminmsg('tpl_string_error');
		}
		$invokeService->updateInvokeTagCode($name,$tagcode);
		adminmsg('operate_success',$basename.'&action=show&scr='.$scr.'&showid='.$cateid);
	}
} elseif ($action=='edit') {
	include_once(D_P.'data/bbscache/forum_cache.php');
	InitGP(array('invokepieceid','fid','loopid','scr','viewtype','page'));

	$invokepiece = $invokeService->getInvokePieceByInvokeId($invokepieceid);

	if ($scr == 'index') {
		$thistitle = $db_modes['area']['m_name'].getLangInfo('other','pushto_index');
	} elseif ($scr == 'cate') {

		$thistitle = $forum[$fid]['name'];
	}
	$thistitle = '<a href="'.$basename.'&action=show&scr='.$scr.'&showid='.$fid.'">'.$thistitle.'</a>';
	$thistitle	.= ' -&gt; '.$invokepiece['invokename'].' -&gt; '.$invokepiece['title'];
	if ($loopid) {
		$thistitle	.= ' -&gt; '.$forum[$loopid]['name'];
	}

	if ($invokepiece['rang']!='fid') {
		$fid = 0;
	}
	$param	= $invokepiece['param'];
	!$viewtype && $viewtype = 'effect';
	$count	= $invokeService->countOverduePushData($viewtype,$invokepieceid,$fid,$loopid);
	$page	= (int)$page;
	$page<1	&& $page = 1;
	$perpage= 20;
	$numofpage = ceil($count/$perpage);
	$start	= ($page-1)*$perpage;
	$pages	= numofpage($count,$page,$numofpage,"$basename&action=edit&invokepieceid=$invokepieceid&fid=$fid&loopid=$loopid&scr=$scr&viewtype=".$viewtype."&");
	$pushs	= $invokeService->getPushDatasByType($viewtype,$invokepieceid,$fid,$loopid,$start.','.$perpage);
	$effectStyle = $overdueStyle = $delayStyle = '';
	if ($viewtype == 'effect') {
		$effectStyle = ' class="current"';
	} elseif ($viewtype == 'overdue') {
		$overdueStyle = ' class="current"';
	} elseif ($viewtype == 'delay') {
		$delayStyle = ' class="current"';
	}
	$custom = array();
	foreach ($pushs as $value) {
		$custom[$value['id']] = $value['data'];
	}

	$ajax_basename = EncodeUrl($basename);
} elseif ($action=='editpush') {
	define('AJAX',1);
	InitGP(array('id','step'),'GP');
	$push	= $invokeService->getPushDataById($id);
	$invokepiece = $invokeService->getInvokePieceByInvokeId($push['invokepieceid']);
	if (!$step) {
		if (!$push)  adminmsg('error');
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
		$ajax_basename = EncodeUrl($basename."&action=editpush");
		require_once PrintMode('ajax_tplcontent');ajax_footer();
	} else {
		InitGP(array('param','offset','endtime','starttime','title1','title2','title3','title4','titletime'),'GP');
		$offset = (int) $offset;

		if (isset($invokepiece['param']['tagrelate'])) {
			InitGP(array('tagrelate'));
			$param['tagrelate']	= getTagRelate($tagrelate);
		}
		if(isset($param['image']) && count($_FILES) && $_FILES["uploadpic"]["name"] && $_FILES["uploadpic"]["size"]) {
			$uploadPicUrl = $invokeService->uploadPicture($_FILES, $push['invokepieceid'], $admin_name);
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

		echo getLangInfo('msg','operate_success')."\treload";
		ajax_footer();
	}
} elseif ($action=='addpush') {
	define('AJAX',1);
	InitGP(array('invokepieceid','fid','loopid','step'));
	$invokepiece = $invokeService->getInvokePieceByInvokeId($invokepieceid);
	if ($invokepiece['rang']!='fid') {
		$fid = 0;
	}
	if (!$step) {
		InitGP(array('pushtid'));
		$default	= array();
		if ($pushtid) {
			require(R_P.'lib/tplgetdata.class.php');
			$default	= getSubjectByTid($pushtid,$invokepiece['param']);
		} else {
			foreach ($invokepiece['param'] as $key=>$value) {
				$default[$key] = '';
			}
		}
		$ajax_basename	= EncodeUrl($basename."&action=addpush");
		require_once PrintMode('ajax_tplcontent');ajax_footer();
	} else {
		InitGP(array('param','offset','endtime','starttime','title1','title2','title3','title4','titletime'),'GP');

		if (isset($invokepiece['param']['tagrelate'])) {
			InitGP(array('tagrelate'));
			$param['tagrelate']	= getTagRelate($tagrelate);
		}
		$titlecss = $invokeService->pushDataTitleCss($title1,$title2,$title3,$title4,$titletime);

		if(isset($param['image']) && count($_FILES) && $_FILES["uploadpic"]["name"] && $_FILES["uploadpic"]["size"]) {
			$uploadPicUrl = $invokeService->uploadPicture($_FILES, $invokepieceid, $admin_name);
			$param['image'] = $uploadPicUrl ? $uploadPicUrl : $param['image'];
		}
		$offset = (int) $offset;
		$invokeService->insertPushData(array('invokename'=>$invokepiece['invokename'],'invokepieceid'=>$invokepieceid,'fid'=>$fid,'loopid'=>$loopid,'starttime'=>$starttime,'endtime'=>$endtime,'offset'=>$offset,'data'=>$param,'editor'=>$admin_name,'titlecss'=>$titlecss));

		$invokeService->updateCacheDataPiece($invokepieceid,$fid,$loopid);

		echo getLangInfo('msg','operate_success')."\treload";
		ajax_footer();
	}
} elseif ($action == 'delpush') {
	define('AJAX',1);
	InitGP(array('pushid'),'',2);
	$pushdata	= $invokeService->getPushDataById($pushid);
	$invokeService->deletePushData($pushid);
	$invokeService->updateCacheDataPiece($pushdata['invokepieceid'],$pushdata['fid'],$pushdata['loopid']);

	echo getLangInfo('msg','operate_success')."\treload";
	ajax_footer();
} elseif ($action == 'delpushs') {
	define('AJAX',1);
	InitGP(array('invokepieceid','fid','loopid'));

	$invokeService->deleteOverduePushData($invokepieceid,$fid,$loopid);

	echo getLangInfo('msg','operate_success')."\treload";
	ajax_footer();
} elseif ($action == 'updatecache') {
	define('AJAX',1);
	InitGP(array('scr','fid'));
	$fid	= (int) $fid;
	$config = $invokeService->getMPageConfig('area',$scr,$fid);
	foreach ($config as $key=>$value) {
		if ($value == 1) {
			$invokeService->updateCacheDataPiece($key,$fid,0,$fid);
		} else {
			$invokeService->updateCacheDataPiece($key,0,0,$fid);
		}
	}
	echo getLangInfo('msg','operate_success')."\treload";
	ajax_footer();
}

include PrintMode('tplcontent');exit;
function miniChar_cv($mixed) {
	$mixed = str_replace(array("\0","%00","\r"),'',$mixed);
	$mixed = preg_replace(
		array('/[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]/','/&(?!(#[0-9]+|[a-z]+);)/is'),
		array('','&amp;'),
		$mixed
	);
	$mixed = stripslashes($mixed);
	return $mixed;
}
function checkTplContent ($content) {
	return strpos($content,'EOT;') !== false || strpos($content,'<!--#') !== false || strpos($content,'#-->') !== false || strpos($content,'<<<') !==false || strpos($content,'?>') !== false || strpos($content,'<?') !== false || strpos($content,'write') !== false || strpos($content,'file_put_contents') !== false || strpos($content,'..') !== false;
}
?>