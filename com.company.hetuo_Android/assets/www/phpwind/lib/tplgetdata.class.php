<?php
!defined('P_W') && exit('Forbidden');
class PW_tplGetData{
	var $db;
	var $cache = array();
	var $invokepieces = array();
	var $updates;
	var $index=0;
	function PW_tplGetData(){
		global $db;
		$this->db = &$db;
		$this->updates = array();
		$this->_init();
	}

	function getData($invokename,$title,$loopid=0){
		$temp = $this->_getDataFromCache($invokename,$title,$loopid);
		if ($temp === false) {
			if (L::config('area_static_ifon','area_config')!=1) {
				$this->index++;
			}
			$temp = $this->_getDataFromBBS($invokename,$title,$loopid);
		}
		return $temp;
	}

	/*
	 * private functions
	 */
	function _init(){
		$config	= $this->_getPageConfig();
		$pw_invokepiece	= L::loadDB('invokepiece');
		$invokepieces	= $pw_invokepiece->getDatasByIds(array_keys($config));
		foreach ($invokepieces as $key=>$value) {
			$this->invokepieces[$key] = md5($value['invokename'].$value['title']);
		}
		$pw_cachedata	= L::loadDB('cachedata');
		$this->cache	= $pw_cachedata->getDatasByInvokepieceids($config);
	}
	function _getPageConfig(){
		global $db_mode,$SCR;
		$pw_mpageconfig = L::loadDB('mpageconfig');
		$temp_fid = 0;
		if ($db_mode=='area' && $SCR=='cate') {
			global $fid;
			$temp_fid = $pw_mpageconfig->getAreaFid($fid);
		}
		$config = $pw_mpageconfig->getConfig($db_mode,$SCR,$temp_fid);
		return $config;
	}

	function _getDataFromCache($invokename,$title,$loopid=0){
		global $timestamp;
		$key = $this->_getKey($invokename,$title,$loopid);

		if (isset($this->cache[$key]) && ($this->cache[$key]['cachetime'] == 0 || $this->cache[$key]['cachetime']>$timestamp || $this->index >4)) {
			return $this->cache[$key]['data'];
		}
		return false;
	}

	function _getDataFromBBS($invokename,$title,$loopid=0){
		$pw_invkoepiece = L::loadDB('invokepiece');
		$config	= $pw_invkoepiece->getDataByInvokeNameAndTitle($invokename,$title);
		$tempElement= $this->_getDataFromElement($config,$loopid);
		$tempPush	= $this->_getDataFromPush($config,$loopid);
		$data		= $this->_combinElementAndPush($tempElement,$tempPush,$config['num']);
		if (!$data && $config['action']=='image') {
			global $imgpath;
			$data[]	= array('image'=>"$imgpath/nopic.gif");
		}
		$this->_updateCache($config,$loopid,$data);
		return $data;
	}

	function _updateCache($config,$loopid,$data){
		global $timestamp;
		$invokename = $config['invokename'];
		$title	= $config['title'];
		$key	= $this->_getKey($invokename,$title,$loopid);
		if ($config['rang']=='fid') {
			global $fid;
			$temp_fid	= $fid;
		} else {
			$temp_fid	= 0;
		}
		$temp_key = $this->_getKey($invokename,$title);
		//!$config['cachetime'] && $config['cachetime'] = 10;
		$config['cachetime'] = (int) $config['cachetime'];
		$cachetime = $config['cachetime'] ? $timestamp+$config['cachetime'] : 0;
		$this->cache[$key] = $this->updates[] = array(
			'mark'	=> $temp_key,
			'fid'	=> $temp_fid,
			'loopid'=> (int)$loopid,
			'data'	=> $data,
			'cachetime'	=> $cachetime,
		);
	}

	function _getDataFromElement($config,$loopid=0){
		$element= L::loadClass('element');
		$func	= $config['func'];
		$num	= $config['num'];
		if ($loopid && (is_numeric($config['rang']) || $config['rang']=='fid')) {
			$rang	= $loopid;
		} elseif ($config['rang']=='fid') {
			$rang	= $this->_getFids();
		} else {
			$rang	= $config['rang'];
		}
		$parameter	= $config['param'];
		if (!$element->checkFunction($func)) {
			global $func;
			Showmsg('undefined_function');
		}
		$temp = $element->{$func}($rang,$num);
		return $this->_analyseResults($temp,$parameter);
	}

	function _getKey($invokename,$title,$loopid=0){
		$encode = md5($invokename.$title);

		if ($temp	= array_search($encode,$this->invokepieces)) {
			return $loopid ? $temp."_".$loopid : $temp;
		}
		return false;
	}

	function _getFids(){
		static $fids;
		global $fid,$SCR;
		if ($SCR == 'thread' || $SCR =='read') {
			return $fid;
		} elseif ($SCR == 'cate') {
			if (!isset($fids)) {
				$query = $this->db->query("SELECT fid FROM pw_forums WHERE type<>'category' AND cms<>1 AND password='' AND forumsell='' AND f_type<>'hidden' AND allowvisit='' AND fup=".pwEscape($fid));
				while ($rt = $this->db->fetch_array($query)) {
					$fids .= ",'$rt[fid]'";
				}
				$fids && $fids = substr($fids,1);
			}
			return $fids;
		} else {
			return 0;
		}
	}

	function _getDataFromPush($config,$loopid=0){
		global $SCR,$fid;
		$pw_pushdata= L::loadDB('pushdata');
		$temp_fid = 0;
		if ($config['rang']=='fid' && $SCR == 'cate') {
			$temp_fid = $fid;
		}
		$invokepieceid	= $config['id'];
		$num	= $config['num'];
		return $pw_pushdata->getEffectData($invokepieceid,$temp_fid,$loopid,$num);
	}

	function _combinElementAndPush($elements,$pushs,$num){
		$temp	= array();
		$pushs	= array_reverse($pushs);
		foreach ($pushs as $value) {
			if ($value['offset']) {
				$temp[$value['offset']] = $value['data'];
			} else {
				array_unshift($elements,$value['data']);
			}
		}
		if ($temp) {
			foreach ($temp as $key=>$value) {
				if ($key<=$num) {
					$elements[$key] = $value;
				}
			}
		}
		return array_slice($elements,0,$num);
	}

	function _analyseResults($result,$parameter){
		if (!is_array($result)) return array();
		if ($parameter && is_array($parameter)) {
			$temp = array();
			foreach ($result as $key=>$value) {
				foreach ($parameter as $k=>$val) {
					if (in_array($k,array('url','title','image','value','forumname','forumurl'))) {
						$temp_2 = $value[$k];
					} elseif ($k == 'descrip') {
						$temp_2 = getDescripByTid($value['addition']['tid']);
					} elseif ($k == 'tagrelate') {
						$temp_2 = array();
					} elseif (isset($value['addition'][$k])) {
						$temp_2 = $value['addition'][$k];
					} else {
						$temp_2 = '';
					}
					$temp[$key][$k] = $this->_analyseResultByParameter($temp_2,$val,$k);
				}
			}
			$result = $temp;
		}
		return $result;
	}
	function _analyseResultByParameter($result,$param,$addtion=''){
		if ($param =='default') {
			$temp = $result;
		} elseif (is_numeric($param)) {
			$temp = substrs($result,$param);
		} elseif (preg_match('/^\d{1,3},\d{1,3}$/',$param) && $addtion =='image') {
			list($width,$height) = explode(',',$param);
			$temp = minImage($result,$width,$height);
		} elseif (preg_match('/^\w{1,4}(:|-)\w{1,4}((:|-)\w{1,4})?$/',$param)) {
			$temp = get_date($result,$param);
		}
		return $temp;
	}
}
?>