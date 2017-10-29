<?php
!defined('P_W') && exit('Forbidden');

/**
 * 后台管理-搜索引擎设置
 * @author papa
 * 
 */
class PW_SEOSetDB extends BaseDB{
	
	var $_tableName = 'pw_seoset';
	var $_types = array('title','metadesc','metakeywords');
	
	
	/**
	 * @return unknown_type
	 */
	function getDefaultType(){
		return $this->_types[0];
	}
	
	/**
	 * @return unknown_type
	 */
	function getMetaDescType(){
		return $this->_types[1];
	}
	
	/**
	 * @return unknown_type
	 */
	function getMetaKeywordsType(){
		return $this->_types[2];
	}
	
	/**
	 * @param $data
	 * @return boolean
	 */
	function replaceSEOSet($type,$data){
		if ($data && $type && in_array($type,$this->_types)) {
			$this->_db->update("REPLACE INTO " . $this->_tableName . " (mode,page,type,content) VALUES " . pwSqlMulti($data));
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @param $data
	 * @return unknown_type
	 */
	function updateSEOSet($data){
		$this->_db->update("UPDATE " . $this->_tableName . " SET " . $this->_getUpdateSqlString($data));
	}
	
	/**
	 * 根据类型获取SEO设置信息
	 * @param $type
	 * @return unknown_type
	 */
	function getSEOSetsByType($type){
		$sql = "SELECT * FROM " . $this->_tableName . " WHERE type = " . pwEscape($type);
		$query = $this->_db->query($sql);
		$contents = array();
		while ($rt = $this->_db->fetch_array($query)) {
			$key = $rt['mode'] . '_' . $rt['page'];
			$contents[$key] = $rt['content'];
		}
		return $contents;
	}
	
	/**
	 * 获得页面的SEO设置信息
	 * @param $mode
	 * @param $page
	 * @param $type
	 * @return String
	 */
	function getSEOSetForPage($mode,$page,$type){
		$r = $this->_db->get_value("SElECT content FROM " . $this->_tableName . " WHERE mode = " . pwEscape($mode) . " AND page = " . pwEscape($page) . " AND type = " . pwEscape($type));
		if (!$r && strpos($page,'_') === false) {
			$r = $this->_db->get_value("SElECT content FROM " . $this->_tableName . " WHERE mode = " . pwEscape($mode) . " AND page = 'thread' AND type = " . pwEscape($type));
		}
		return $r;
	}
	
}
?>