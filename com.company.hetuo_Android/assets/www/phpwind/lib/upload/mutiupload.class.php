<?php
!defined('P_W') && exit('Forbidden');

require_once(R_P . 'lib/upload.class.php');

class MutiUpload extends uploadBehavior {
	
	var $db;
	var $uid;
	var $attachs;

	function MutiUpload($uid) {
		global $db,$db_uploadfiletype;
		parent::uploadBehavior();
		$this->uid = (int)$uid;
		
		$this->db =& $db;
		$this->ftype =& $db_uploadfiletype;
		$this->ifftp = 0;
	}

	function allowType($key) {
		return true;
	}

	function getFilePath($currUpload) {
		global $timestamp;
		$savedir = '';
		$prename  = substr(md5($timestamp . $currUpload['id'] . randstr(8)),10,15);
		$filename = "0_{$this->uid}_$prename." . preg_replace('/(php|asp|jsp|cgi|fcgi|exe|pl|phtml|dll|asa|com|scr|inf)/i', "scp_\\1", $currUpload['ext']);
		return array($filename, $savedir, '', '');
	}

	function update($uploaddb) {
		global $db_charset,$timestamp;
		foreach ($uploaddb as $key => $value) {
			$value['name'] = pwConvert($value['name'], $db_charset, 'utf-8');
			$this->db->update("INSERT INTO pw_attachs SET " . pwSqlSingle(array(
				'fid'		=> 0,						'uid'		=> $this->uid,
				'tid'		=> 0,						'pid'		=> 0,
				'hits'		=> 0,						'name'		=> $value['name'],
				'type'		=> $value['type'],			'size'		=> $value['size'],
				'attachurl'	=> $value['fileuploadurl'],	'uploadtime'=> $timestamp,
				'ifthumb'	=> $value['ifthumb']
			)));
		}
	}
}
?>