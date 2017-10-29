<?php
!function_exists('readover') && exit('Forbidden');

class PW_Navmenu{
	var $type;
	var $db;

	function PW_Navmenu($type=null) {
		$type && $this->type = $type;
		$this->db = $GLOBALS['db'];
	}

	function settype($type) {
		$this->type = $type;
	}

	function get($nkey=null) {
		if ($nkey === null) {
			$navs = array();
			$query = $this->db->query("SELECT * FROM pw_nav WHERE type=".pwEscape($this->type,false));
			while ($rt = $this->db->fetch_array($query)) {
				$navs[$rt['nid']] = $rt;
			}
		} else {
			$navs = $this->db->get_one("SELECT * FROM pw_nav WHERE type=".pwEscape($this->type,false)."AND nkey=".pwEscape($nkey,false));
		}
		return $navs;
	}

	function update($nkey,$array) {
		$array['nkey'] || $array['nkey'] = $nkey;
		$array['type'] || $array['type'] = $this->type;
		$array = $this->_checkData($array);
		$this->db->pw_update(
			"SELECT nid FROM pw_nav WHERE type=".pwEscape($this->type,false)."AND nkey=".pwEscape($nkey,false),
			"UPDATE pw_nav SET".pwSqlSingle($array,false)." WHERE type=".pwEscape($this->type,false)."AND nkey=".pwEscape($nkey,false),
			"INSERT INTO pw_nav SET".pwSqlSingle($array,false)
		);
	}

	function del($nkey) {
		$this->db->update("DELETE FROM pw_nav WHERE type=".pwEscape($this->type,false)."AND nkey=".pwEscape($nkey,false));
	}

	function clear() {
		$this->db->update("DELETE FROM pw_nav WHERE type=".pwEscape($this->type,false));
	}

	function setshow($nkey,$isshow) {
		$isshow = $isshow ? 1 : 0;
		$this->db->update("UPDATE pw_nav SET isshow=".pwEscape($isshow,false)."WHERE type=".pwEscape($this->type,false)."AND nkey=".pwEscape($nkey,false));
	}

	function setupnav($nkey,$upkey=null) {
		if ($upkey) {
			$nav = $this->get($upkey);
			$upid = (int)$nav['nid'];
		} else {
			$upid = 0;
		}
		$this->db->update("UPDATE pw_nav SET upid=".pwEscape($upid,false)." WHERE type=".pwEscape($this->type,false)."AND nkey=".pwEscape($nkey,false));
	}

	function cache(){
		$query = $this->db->query("SELECT * FROM pw_nav WHERE isshow=1 ORDER BY type,upid,view");
		$nav_cache = $nav_other = $nav_main = $nav_head = $nav_foot = array();
		while ($navdb=$this->db->fetch_array($query)) {
			list($navdb['color'],$navdb['b'],$navdb['i'],$navdb['u']) = explode('|',$navdb['style']);
			$key = $navdb['nkey'] ? 'KEY'.$navdb['nkey'] : 'ID'.$navdb['nid'];
			if ($navdb['type'] == "foot") {
				$nav_foot[$key] = array('html'=>$this->_getHtmlText($navdb),'pos'=>$navdb['pos']);
			} elseif ($navdb['type'] == "head"){
				$nav_head[$key] = array('html'=>$this->_getHtmlText($navdb),'pos'=>$navdb['pos']);
			} elseif ($navdb['type'] == "main") {
				$nav_main[$key] = array('html'=>$this->_getHtmlText($navdb),'pos'=>$navdb['pos']);
			} else {
				$nav_other[$navdb['nid']] = $navdb;
			}
		}

		foreach ($nav_other as $value) {
			$key = $value['nkey'] ? 'KEY'.$value['nkey'] : 'ID'.$value['nid'];
			if (!$value['upid']) {
				$nav_cache[$value['type']][$key] = array('html' => $this->_getHtmlText($value));
			} else {
				$upkey = $nav_other[$value['upid']]['nkey'] ? 'KEY'.$nav_other[$value['upid']]['nkey'] : 'ID'.$nav_other[$value['upid']]['nid'];
				$nav_cache[$value['type']][$upkey]['child'][$key] = array('html' => $this->_getHtmlText($value));
			}
		}
		require_once R_P.'admin/cache.php';
		setConfig('db_headnav', $nav_head);
		setConfig('db_footnav', $nav_foot);
		setConfig('db_mainnav', $nav_main);

		foreach ($nav_cache as $key => $value) {
			if (strpos($key,'bbs_') !== false) {
				$key = str_replace('bbs_','db_',$key);
				setConfig($key, $value);
			} else {
				$this->db->update("REPLACE INTO pw_hack SET hk_name=".pwEscape($key,false).",vtype='array',hk_value=".pwEscape(serialize($value),false));
			}
		}
		foreach (array_keys($GLOBALS['db_modes']) as $value) {
			if ($value != 'bbs') {
				updatecache_conf($value,true);
			}
		}
		updatecache_c();
	}

	function _getHtmlText($nav) {
		$html = strip_tags($nav['title']);
		$nav['b'] && $html = "<b>$html</b>";
		$nav['i'] && $html = "<i>$html</i>";
		$nav['u'] && $html = "<u>$html</u>";
		$nav['color'] && $html = "<font color=\"$nav[color]\">$html</font>";
		$target = $nav['target'] ? " target=\"_blank\"" : '';
		$alt = $nav['alt'] ? " title=\"$nav[alt]\"" : '';
		$key = $nav['nkey'] ? 'KEY'.$nav['nkey'] : 'ID'.$nav['nid'];
		if ($nav['link'] == './') {
			$nav['link'] = $GLOBALS['db_bbsurl'].'/';
		} elseif (empty($nav['link'])) {
			$nav['link'] = 'javascript:;';
		}
		$html = "<a href=\"{$nav['link']}\" id=\"td_{$key}\"$alt$target>$html</a>";
		return $html;
	}

	function _getStruct(){
		return array('nid','nkey','type','pos','title','style','link','alt','target','view','upid','isshow');
	}

	function _checkData($array){
		if (!is_array($array)) Showmsg('data_is_not_array');
		$strtct = $this->_getStruct();
		foreach ($array as $key=>$value) {
			if (!in_array($key,$strtct)) {
				unset($array[$key]);
			}
		}
		return $array;
	}
}
?>