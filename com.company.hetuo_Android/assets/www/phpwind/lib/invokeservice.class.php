<?php
!defined('P_W') && exit('Forbidden');
/*
dbs:
	pw_tpl
	pw_invoke
	pw_invokepiece

	pw_pushdata

	pw_stamp
	pw_cachedata

	pw_mpageconfig
*/
class PW_InvokeService {
	var $_tplDB;
	var $_invokeDB;
	var $_invokePieceDB;

	var $_pushDataDB;
	var $_cacheDataDB;
	var $_stampDB;

	var $_mpageConfigDB;
/** public **/
	function inportDefaultConfig($dir) {
		$themeconfig = L::loadClass('ThemeConfig');
		$configfile	= $themeconfig->getThemeConfigFile($dir);
		if ($configfile) {
			$invokeDB = $this->_getInvokeDB();
			$invokePieceDB = $this->_getInvokePieceDB();
			$this->deleteThemeConfig($dir);
			include Pcv($configfile);
			foreach ($defaultInvokes as $value) {
				unset($value['id']);
				$invokeDB->replaceData($value);
			}
			$invokePieceDB->updateInvokeNameKey();
			foreach ($defaultInvokePieces as $value) {
				unset($value['id']);
				$invokePieceDB->replaceData($value);
			}
			$this->updateModeTpl('area');
		}
	}

	function exportThemeConfig($theme) {
		$invokeDB	= $this->_getInvokeDB();
		$invokePieceDB = $this->_getInvokePieceDB();

		$invokes = $this->_getThemeInvokes($theme);

		$defaultInvokes = $invokeDB->getDatesByNames_2($invokes);
		$defaultInvokePieces = $invokePieceDB->getDatasByInvokeNames($invokes);

		$temp_str = "<?php\r\n!defined('P_W') && exit('Forbidden');\r\n";
		$temp_str .= '$defaultInvokes='.pw_var_export($defaultInvokes).";\r\n";
		$temp_str .= '$defaultInvokePieces='.pw_var_export($defaultInvokePieces).";\r\n?>";
		$this->_exportHeader($temp_str);
	}

	function _getThemeInvokes($theme) {
		$invokes = array();
		$themeconfig= L::loadClass('ThemeConfig');
		$parsepw	= L::loadClass('parsepw');
		$themepages = $themeconfig->getPages($theme);
		foreach ($themepages as $page) {
			$file_str	= readover($page);
			$temp	= $parsepw->getInvokes($file_str);
			$invokes = array_merge($invokes,$temp);
		}
		return $invokes;
	}

	function _exportHeader($words) {
		header('Last-Modified: '.gmdate('D, d M Y H:i:s',$timestamp+86400).' GMT');
		header('Cache-control: no-cache');
		header('Content-Encoding: none');
		header('Content-Disposition: attachment; filename=config.php');
		header('Content-type: txt');
		header('Content-Length: '.strlen($words));
		echo $words;exit;
	}

	function deleteThemeConfig($theme) {
		$invokeDB	= $this->_getInvokeDB();
		$invokePieceDB = $this->_getInvokePieceDB();
		$invokes = $this->_getThemeInvokes($theme);
		$invokeDB->deleteByNames($invokes);
		$invokePieceDB->deleteByInvokeNames($invokes);
	}

	function updateModeTpl($mode) {
		$fp = opendir(D_P.'data/tplcache/');
		while ($filename = readdir($fp)) {
			if ($filename == '..' || $filename == '.' || strpos($filename,'.htm') === false) continue;
			if (strpos($filename,$mode.'_') === 0) {
				P_unlink(Pcv(D_P.'data/tplcache/'.$filename));
			}
		}
		closedir($fp);
	}
/**	pw_tpl **/
	function getTpl($tplid) {
		$tplDb = $this->_getTplDB();
		return $tplDb->getData($tplid);
	}
	function insertTpl($data) {
		$tplDb = $this->_getTplDB();
		$tplDb->insertData($data);
	}
	function updateTpl($id,$data) {
		$tplDb = $this->_getTplDB();
		$tplDb->updataById($id,$data);
		$this->_updateInvokeByTplId($id);
	}
/**	pw_invoke **/
	function getInvokeByName($invokename,$cateid=0) {
		$invokeDB = $this->_getInvokeDB();
		return $invokeDB->getDataByName($invokename,$cateid);
	}
	function getInvokesByNames($names,$cateid=0,$type='') {
		$invokeDB = $this->_getInvokeDB();
		return $invokeDB->getDatasByNames($names,$cateid,$type);
	}
	function updateInvokeByName($name,$data) {
		$invokeDB = $this->_getInvokeDB();
		$invokeDB->updateByName($name,$data);
	}
	function getInvokeByTplId($tplid) {
		$invokeDB = $this->_getInvokeDB();
		return $invokeDB->getByTplId($tplid);
	}
	function getInvokeByLike($rule) {
		$invokeDB = $this->_getInvokeDB();
		return $invokeDB->getDatesByLike($rule);
	}

	function updateInvokeTagCode($name,$tagCode) {
		$parseTagCode	= L::loadClass('ParseTagCode');
		$temp	= $this->getInvokeByName($name);
		$ifLoop = $temp['ifloop'];
		$parseTagCode->init($name,$tagCode,$ifLoop);
		$parsecode = $parseTagCode->getParseCode();
		$this->updateInvokeByName($name,array('tagcode'=>$tagCode,'parsecode'=>$parsecode));
		$newInvokePieces= $parseTagCode->getConditoin();
		$this->_updateInvokePieceTagCode($name,$newInvokePieces);
	}
	function _updateInvokePieceTagCode($name,$newInvokePieces) {
		$oldInvokePieces= $this->getInvokePieceByInvokeName($name);
		foreach ($newInvokePieces as $newpiece) {
			$mark = 0;
			foreach ($oldInvokePieces as $key=>$oldpiece) {
				if ($newpiece['title'] == $oldpiece['title']) {
					if ($newpiece['action'] != $oldpiece['action']) {
						$pw_stamp	= L::loadDB('stamp');
						$block	= $pw_stamp->getDefaultBlockByStamp($newpiece['action']);
						$newpiece['func']	= $block['func'];
						$newpiece['cachetime'] = $block['cachetime'];
						$newpiece['rang']	= $block['rang'];
					}
					$mark = 1;
					$this->updateInvokePieceById($oldpiece['id'],$newpiece);
					unset($oldInvokePieces[$key]);
					break;
				}
			}
			if (!$mark) {
				$this->insertInvokePiece($newpiece);
			}
		}
		if ($oldInvokePieces) {
			foreach ($oldInvokePieces as $key=>$value) {
				$this->deleteInvokePieceById($value['id']);
				$this->deletePushDataByPiecesId($value['invokepieceid']);
			}
		}
	}
	function _updateInvokeByTplId($tplid) {
		$parsetpl	= L::loadClass('ParseTagCode');
		$tempInvokes	= $this->getInvokeByTplId($tplid);
		$tplDb = $this->_getTplDB();
		foreach ($tempInvokes as $invoke) {
			$tplData	= $tplDb->getData($tplid);
			$parsetpl->init($invoke['name'],$tplData['tagcode'],$invoke['ifloop']);
			$parsecode	= $parsetpl->getParseCode();
			$this->updateInvokeByName($invoke['name'],array('parsecode'=>$parsecode));

			$this->deleteInovkePieceByInvokeName($invoke['name']);		//更新所属的invokepiece
			$invokepiece = $parsetpl->getConditoin();
			$this->insertInvokePieces($invokepiece);
		}
	}
/**	pw_invokepiece **/
	function getInvokePieceByInvokeId($id) {
		$invokePieceDB = $this->_getInvokePieceDB();
		return $invokePieceDB->getDataById($id);
	}

	function getInvokePieceByInvokeName($invokename) {
		$invokePieceDB = $this->_getInvokePieceDB();
		return $invokePieceDB->getDatasByInvokeName($invokename);
	}

	function getInvokePiecesByInvokeNames($names) {
		$invokePieceDB = $this->_getInvokePieceDB();
		return $invokePieceDB->getDatasByInvokeNames($names);
	}

	function getInvokePieceByNameAndTitle($invokename,$title) {
		$invokePieceDB = $this->_getInvokePieceDB();
		return $invokePieceDB->getDataByInvokeNameAndTitle($invokename,$title);
	}

	function updateInvokePieceById($id,$array) {
		$invokePieceDB = $this->_getInvokePieceDB();
		$invokePieceDB->updateById($id,$array);
	}

	function insertInvokePiece($array) {
		$invokePieceDB = $this->_getInvokePieceDB();
		return $invokePieceDB->insertData($array);
	}

	function insertInvokePieces($array) {
		$invokePieceDB = $this->_getInvokePieceDB();
		$invokePieceDB->insertDatas($array);
	}
	function deleteInovkePieceByInvokeName($name) {
		$invokePieceDB = $this->_getInvokePieceDB();
		$invokePieceDB->deleteByInvokeName($name);
	}
	function deleteInvokePieceById($id) {
		$invokePieceDB = $this->_getInvokePieceDB();
		$invokePieceDB->deleteById($id);
	}
	function updateInvokePieces($array) {
		if (!is_array($array) || !$array) return false;
		foreach ($array as $key=>$value) {
			if (!is_array($value)) return false;
			$this->_updateInvokePiece($value);
		}
	}
	function getInvokePieceByLike($rule) {
		$invokePieceDB = $this->_getInvokePieceDB();
		return $invokePieceDB->getDatesByLike($rule);
	}
	function _updateInvokePiece($array) {
		if (!isset($array['invokename']) || !isset($array['title'])) return false;
		$temp = $this->getInvokePieceByNameAndTitle($array['invokename'],$array['title']);
		if ($temp) {
			$this->updateInvokePieceById($temp['id'],$array);
		} else {
			$this->insertInvokePiece($array);
		}
	}

/**	pw_pushdata **/

	function getPushDataById($id) {
		$pushDataDB = $this->_getPushDataDB();
		return $pushDataDB->getDataById($id);
	}

	function getPushDatasByType($type,$invokepieceid,$fid=0,$loopid=0,$num = 10) {
		$pushDataDB = $this->_getPushDataDB();
		if ($type == 'effect') {
			return $pushDataDB->getEffectData($invokepieceid,$fid,$loopid,$num);
		} elseif ($type == 'overdue') {
			return $pushDataDB->getOverdueData($invokepieceid,$fid,$loopid,$num);
		} elseif ($type == 'delay') {
			return $pushDataDB->getDelayData($invokepieceid,$fid,$loopid,$num);
		}
	}

	function countPushDataByType($type,$invokepieceid,$fid,$loopid) {
		$pushDataDB = $this->_getPushDataDB();
		if ($type == 'effect') {
			return $pushDataDB->countEffect($invokepieceid,$fid,$loopid);
		} elseif ($type == 'overdue') {
			return $pushDataDB->countOverdue($invokepieceid,$fid,$loopid);
		} elseif ($type == 'delay') {
			return $pushDataDB->countDelay($invokepieceid,$fid,$loopid);
		}
	}
	function getPushDataEffect($invokepieceid,$fid=0,$loopid=0,$num = 10) {
		$pushDataDB = $this->_getPushDataDB();
		return $pushDataDB->getEffectData($invokepieceid,$fid,$loopid,$num);
	}
	function getPushDataOverdue($invokepieceid,$fid=0,$loopid=0,$num = 10) {
		$pushDataDB = $this->_getPushDataDB();
		return $pushDataDB->getOverdueData($invokepieceid,$fid,$loopid,$num);
	}
	function countEffectPushData($invokepieceid,$fid,$loopid) {
		$pushDataDB = $this->_getPushDataDB();
		return $pushDataDB->countEffect($invokepieceid,$fid,$loopid);
	}
	function countOverduePushData($invokepieceid,$fid,$loopid) {
		$pushDataDB = $this->_getPushDataDB();
		return $pushDataDB->countOverdue($invokepieceid,$fid,$loopid);
	}
	function insertPushData($array) {
		$pushDataDB = $this->_getPushDataDB();
		$array['endtime']	= $this->_endTimeToTime($array['endtime']);
		$array['starttime'] = $this->_startTimeToTime($array['starttime']);
		return $pushDataDB->insertData($array);
	}
	function addPushData($array) {
		$pushDataDB = $this->_getPushDataDB();
		$pushDataDB->increaseOffset($array['invokepieceid'],$array['offset']);
		$array['endtime']	= $this->_endTimeToTime($array['endtime']);
		$array['starttime'] = $this->_startTimeToTime($array['starttime']);
		return $pushDataDB->insertData($array);
	}
	function updatePushData($id,$array) {
		global $timestamp;
		$array['endtime']	= $this->_endTimeToTime($array['endtime']);
		$array['starttime'] = $this->_startTimeToTime($array['starttime']);
		$pushDataDB = $this->_getPushDataDB();
		$pushDataDB->update($id,$array);
	}

	function pushDataTitleCss($color,$b,$u,$i,$endtime=0) {
		$endtime = $this->_endTimeToTime($endtime);
		return array(
			'color' => $color,
			'b' => $b,
			'i' => $i,
			'u' => $u,
			'endtime' => $endtime
		);
	}

	function _endTimeToTime($endtime) {
		if ($endtime && !is_numeric($endtime)) {
			$endtime	= PwStrtoTime($endtime);
			if ($endtime == -1) {
				$endtime = 0;
			}
		} else {
			$endtime = 0;
		}
		return $endtime;
	}

	function _startTimeToTime($starttime) {
		global $timestamp;
		if ($starttime && !is_numeric($starttime)) {
			$starttime	= PwStrtoTime($starttime);
			if ($starttime == -1) {
				$starttime = $timestamp;
			}
		} else {
			$starttime = $timestamp;
		}
		return $starttime;
	}
	function deletePushData($id) {
		$pushDataDB = $this->_getPushDataDB();
		$data = $pushDataDB->getDataById($id);
		$pushDataDB->delete($id);
		$pushDataDB->decrementOffset($data['invokepieceid'],$data['offset']+1);
	}

	function deletePushDataByPiecesId($id) {
		$pushDataDB = $this->_getPushDataDB();
		$pushDataDB->deleteByPiecesId($id);
	}
	function deleteOverduePushData($invokepieceid,$fid,$loopid) {
		$pushDataDB = $this->_getPushDataDB();
		$pushDataDB->deleteOverdues($invokepieceid,$fid,$loopid);
	}

	function getHaveDelayPushData($invokepieces) {
		$pushDataDB = $this->_getPushDataDB();
		return $pushDataDB->getHaveDelays($invokepieces);
	}

/** pw_pushpic **/
	function uploadPicture($fileArray,$invokePieceId,$creator) {
		global $timestamp;
		$pushPicDB = $this->_getPushPicDB();
		$uploadPictureClass = $this->_setUploadPictureClass();
		if (count ( $fileArray ) < 0 || !intval($invokePieceId) || trim ( $creator ) == "") {
			return null;
		}
		$filename = $uploadPictureClass->upload ( $fileArray );
		if ($filename === FALSE) {
			return null;
		}
		$fieldData = array (
			'invokepieceid'	=> intval($invokePieceId),
			'path' => trim ($filename),
			'creator' => $creator,
			'createtime'=> $timestamp,
		);
		$pushPicDB->add($fieldData);
		return "attachment/pushpic/".$filename;
	}

	function _setUploadPictureClass() {
		$tempUpdatePicture = L::loadClass('UpdatePicture');
		$tempUpdatePicture->init(R_P . "attachment/pushpic/");
		$tempUpdatePicture->isThumb = false;
		return $tempUpdatePicture;
	}
/**	pw_stamp **/

	function getStampBlocks($stamp) {
		$stampDB = $this->_getStampDB();
		return $stampDB->getBlocksByStamp($stamp);
	}
/**	pw_cachedata **/
	function updateCacheDataPiece($invokepieceid,$fid=0,$loopid=0) {
		$this->deleteCacheData($invokepieceid,$fid,$loopid);
		updateAreaStaticRefreshTime();
	}
	function deleteCacheData($invokepieceid,$fid=0,$loopid=0) {
		$cacheDataDB = $this->_getCacheDataDB();
		$cacheDataDB->deleteData($invokepieceid,$fid,$loopid);
	}

/**	pw_mpageconfig **/

	function getMPageConfig($mode,$SCR,$fid=0) {
		$temp_fid	= $this->getMPageConfigFid($fid);
		$mpageConfigDB = $this->_getMPageConfigDB();
		return $mpageConfigDB->getConfig($mode,$SCR,$temp_fid);
	}
	function getMPageConfigFid($fid) {
		$mpageConfigDB = $this->_getMPageConfigDB();
		return $mpageConfigDB->getAreaFid($fid);
	}
	function getMPageConfigInvoke($db_mode,$SCR,$fid=0) {
		$mpageConfigDB = $this->_getMPageConfigDB();
		return $mpageConfigDB->getInvokes($db_mode,$SCR,$fid);
	}


/** getDBs **/
	function _getTplDB() {
		return L::loadDB('Tpl');
	}
	function _getInvokeDB() {
		return L::loadDB('Invoke');
	}
	function _getInvokePieceDB() {
		return L::loadDB('InvokePiece');
	}

	function _getPushDataDB() {
		return L::loadDB('PushData');
	}
	function _getPushPicDB() {
		return L::loadDB('PushPic');
	}
	function _getStampDB() {
		return L::loadDB('Stamp');
	}
	function _getCacheDataDB() {
		return L::loadDB('CacheData');
	}
	function _getMPageConfigDB() {
		return L::loadDB('MPageConfig');
	}
}
?>