<?php
!defined('P_W') && exit('Forbidden');

class PW_SEOSet{
	var $_page = 'index';	//default page
	var $_mode = 'bbs';	//default mode
	var $_sitename = ''; //default sitename
	var $_tags;
	var $_types;
	var $_default;
	var $_ifCMS;
	var $_summary;
	var $_targetIndex = array('{wzmc}');
	var $_targetThread = array('{wzmc}','{bkmc}','{flmc}');
	var $_targetRead = array('{wzmc}','{bkmc}','{flmc}','{tzmc}','{tmc}','{wzgy}');
	
	function PW_SEOSet(){
		global $SCR;
		global $db_mode;
		$this->_page = $SCR;
		$db_mode && $this->_mode = $db_mode;
		$this->_setSitName ();

	}
	
	/**
	 * @param $_mode the $_mode to set
	 */
	function set_mode($_mode) {
		$this->_mode = $_mode;
		$this->_setSitName();
	}
	
	/**
	 * @return the $_summary
	 */
	function get_summary() {
		return $this->_summary;
	}

	
	
	/**
	 * @param $_summary the $_summary to set
	 */
	function set_summary($_summary,$convert) {
		if ($_summary) {
			$_summary = stripWindCode($_summary);
			$_summary = strip_tags($_summary);
			$_summary = str_replace(array('"',"\n","\r",'&nbsp;','&amp;','&lt;','','&#160;'),'',$_summary);
			$_summary = substrs($_summary,255);
			if ($convert) {
				$wordsfb = L::loadClass('FilterUtil');
				$_summary = $wordsfb->convert($_summary);
			}
			$this->_summary = trim($_summary);
		}
	}

	
	/**
	 * 
	 */
	function _setSitName() {
		global $db_areaname;
		global $db_modename;
		if ($this->_mode == 'area' && ! empty ( $db_areaname )) {
			$this->_sitename = $db_areaname;
		} elseif ($this->_mode == 'o' && ! empty ( $db_modename )) {
			$this->_sitename = $db_modename;
		} else {
			global $db_bbsname;
			$this->_sitename = $db_bbsname;
		}
	}

	
	
	/**
	 * @param $_ifCMS the $_ifCMS to set
	 */
	function set_ifCMS($_ifCMS) {
		$this->_ifCMS = $_ifCMS;
		if ($this->_ifCMS) {
			$this->_mode = 'area';
		}
		$this->_setSitName();
	}

	
	/**
	 * @param $_default the $_default to set
	 */
	function set_default($_default) {
		$this->_default = $_default;
	}

	
	/**
	 * @param $content
	 * @param $fname
	 * @param $subject
	 * @return unknown_type
	 */
	function getPageMetakeyword($content='',$fname='',$subject=''){
		global $db_metakeyword;
		global $db_areaMetakeyword;
		$db_metakeyword = $this->_validatDefaultValue($db_metakeyword);
		$db_areaMetakeyword = $this->_validatDefaultValue($db_areaMetakeyword);
		$subject && $subject = strip_tags($subject);
		$fname && $fname = strip_tags($fname);
		if ($this->_page == 'index') { 
			$content = $this->_mode == 'area' ? $db_areaMetakeyword['index'] : $db_metakeyword['index'];
		} elseif (($this->_page == 'thread' || $this->_page == 'cate') && !$content) {
			$content = $this->_mode == 'area' ? $db_areaMetakeyword['thread'] : $db_metakeyword['thread'];
		} elseif ($this->_page == 'read'){
			$content = $this->_mode == 'area' ? $db_areaMetakeyword['read'] : $db_metakeyword['read'];
		}
		if ($content) {
			return $this->_getPageSEOSettings($content,$fname,$subject);
		}else{
			return $this->_getDefaultSettings('metakeyword',$subject,$fname);
		}
	}
	
	/**
	 * @param $content
	 * @param $fname
	 * @param $subject
	 * @return unknown_type
	 */
	function getPageMetadescrip($content='',$fname='',$subject=''){
		global $db_metadescrip;
		global $db_areaMetadescrip;
		$db_metadescrip = $this->_validatDefaultValue($db_metadescrip);
		$db_areaMetadescrip = $this->_validatDefaultValue($db_areaMetadescrip);
		$subject && $subject = strip_tags($subject);
		$fname && $fname = strip_tags($fname);
		if ($this->_page == 'index') { 
			$content = $this->_mode == 'area' ? $db_areaMetadescrip['index'] : $db_metadescrip['index'];
		} elseif (($this->_page == 'thread' || $this->_page == 'cate') && !$content) {
			$content = $this->_mode == 'area' ? $db_areaMetadescrip['thread'] : $db_metadescrip['thread'];
		} elseif ($this->_page == 'read'){
			$content = $this->_mode == 'area' ? $db_areaMetadescrip['read'] : $db_metadescrip['read'];
		}
		
		if (!$this->_default && $content) {
			return $this->_getPageSEOSettings($content,$fname,$subject);
		}else{
			return $this->_getDefaultSettings('metadescrip',$subject,$fname);
		}
	}
	
	/**
	 * @param $content
	 * @param $fname
	 * @param $subject
	 * @return unknown_type
	 */
	function getPageTitle($content='',$fname='',$subject=''){
		global $db_bbstitle;
		global $db_areaTitle;
		$db_bbstitle = $this->_validatDefaultValue($db_bbstitle);
		$db_areaTitle = $this->_validatDefaultValue($db_areaTitle);
		$subject && $subject = strip_tags($subject);
		$fname && $fname = strip_tags($fname);
		if ($this->_page == 'index') { 
			$content = $this->_mode == 'area' ? $db_areaTitle['index'] : $db_bbstitle['index'];
		} elseif (($this->_page == 'thread' || $this->_page == 'cate' ) && !$content) {
			$content = $this->_mode == 'area' ? $db_areaTitle['thread'] : $db_bbstitle['thread'];
		} elseif ($this->_page == 'read'){
			$content = $this->_mode == 'area' ? $db_areaTitle['read'] : $db_bbstitle['read'];
		}
		if ($content) {
			return $this->_getPageSEOSettings($content,$fname,$subject);
		}else{
			return $this->_getDefaultSettings('title',$subject,$fname);
		}
	}
	
	function _validatDefaultValue($v){
		if (isset($v) && !is_array($v)) {
			$v = array('index'=>$v,	'thread'=>$v,	'read'=>'');
		}
		return $v;
	}
	
	/**
	 * @param $content	//用户自定义部分
	 * @param $subject	//文章主题
	 * @param $fname	//版块名称
	 * @return String
	 */
	function _getPageSEOSettings($content,$fname,$subject){
		if ($this->_page == 'index') {
			$replace = array($this->_sitename);
			$content = str_replace($this->_targetIndex,$replace,$content);
		} elseif ($this->_page == 'thread' || $this->_page == 'cate') {
			$replace = array($this->_sitename,$fname,$this->_types);
			$content = str_replace($this->_targetThread,$replace,$content);
		} elseif ($this->_page == 'read') {
			$replace = array($this->_sitename,$fname,$this->_types,$subject,$this->_tags,$this->_summary);
			$content = str_replace($this->_targetRead,$replace,$content);
		}
		$content = $this->_filterUnsupportTarget($content);
		$content = trim(trim(preg_replace('(\-+)','-',$content)),'-');
		$content = trim(trim(preg_replace('(\,+)',',',$content)),',');
		$content = trim(trim(preg_replace('(\|+)','|',$content)),'|');
		return $content;
	}
	
	/**
	 * @return unknown_type
	 */
	function _filterUnsupportTarget($content){
		$_allTarget = array('{wzmc}','{bkmc}',
							'{flmc}','{tzmc}','{tmc}','{wzgy}');
		$_currentTarget = $this->_targetIndex;
		if ($this->_page == 'index') {
			$_currentTarget = $this->_targetIndex;
		} elseif ($this->_page == 'thread') {
			$_currentTarget = $this->_targetThread;
		} elseif ($this->_page == 'area') {
			$_currentTarget = $this->_targetRead;
		}
		foreach ($_allTarget as $key => $value) {
			if (!in_array($value,$_currentTarget)) {
				$content = trim(str_replace($value,'',$content));
			}
		}
		return $content;
	}
	
	function _getDefaultSettings($type,$subject,$fname){
		if ($this->_default && $type == 'metadescrip') {
			return $this->_default;
		}
		$content = $this->_sitename;
		$fname && $content = $fname.' - '.$content;
		$subject && $content = $subject.' | '.$content;
		if ($type == 'metakeyword') {
			$this->_types && $content =  $this->_types.', '.$content;
			$this->_tags && $content = $this->_tags.', '.$content;
			$content = trim(str_replace(array(' | ',' - ',"\t",' ',',,,',',,'),',',$content),',');
		} elseif ($type == 'metadescrip') {
			$this->_summary && $content = $this->_summary.' '.$this->_sitename;
			$content = trim(str_replace(array(' | ',' - ',"\t",' ',',,,',',,'),',',$content),',');
		}
		$content = trim($content);
		return $content;
	}
	
	/**
	 * @param $_page the $_page to set
	 */
	function set_page($_page) {
		$this->_page = $_page;
	}

	/**
	 * @param $_types the $_types to set
	 */
	function set_types($_types,$_ctype) {
		if (!$_types) {
			if ($_ctype && is_numeric($_ctype)) { 
				$this->_types = $_types[$_ctype]['name'];
			}
		} elseif ($_types && is_array($_types)) { 
			if ($_ctype && is_numeric($_ctype)) { 
				$this->_types = $_types[$_ctype]['name'];
			}
		} else {
			$this->_types = $_types;
		}
		$this->_types = strip_tags($this->_types);
	}

	/**
	 * @param $_tags the $_tags to set
	 */
	function set_tags($_tags) {
		$this->_tags = substr($_tags,0,strpos($_tags,"\t"));
		$this->_tags = strip_tags($this->_tags);
	}
	
}
?>