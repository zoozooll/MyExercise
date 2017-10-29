<?php
!defined('P_W') && exit('Forbidden');

require_once(R_P . 'lib/upload.class.php');

class PhotoUpload extends uploadBehavior {
	
	var $db;
	var $aid;
	var $attachs;
	var $pid = null;

	function PhotoUpload($aid) {
		global $db,$o_maxfilesize;
		parent::uploadBehavior();
		$this->aid = (int)$aid;
		$this->db =& $db;
		
		!$o_maxfilesize && $o_maxfilesize = 1000;

		$this->ftype = array(
			'gif'  => $o_maxfilesize,				'jpg'  => $o_maxfilesize,
			'jpeg' => $o_maxfilesize,				'bmp'  => $o_maxfilesize,
			'png'  => $o_maxfilesize
		);
	}

	function allowType($key) {
		return true;
	}

	function allowThumb() {
		return true;
	}
	
	function getThumbSize() {
		return "100\t100";
	}

	function getFilePath($currUpload) {
		global $timestamp,$o_mkdir;
		$prename	= randstr(4) . $timestamp . substr(md5($timestamp . $currUpload['id'] . randstr(8)),10,15);
		$filename	= $this->aid . "_$prename." . $currUpload['ext'];
		$savedir	= 'photo/';
		if ($o_mkdir == '2') {
			$savedir .= 'Day_' . date('ymd') . '/';
		} elseif ($o_mkdir == '3') {
			$savedir .= 'Cyid_' . $this->aid . '/';
		} else {
			$savedir .= 'Mon_'.date('ym') . '/';
		}
		return array($filename, $savedir, 's_' . $filename, $savedir);
	}

	function update($uploaddb) {
		global $windid,$timestamp,$pintro;
		foreach ($uploaddb as $key => $value) {
			$this->attachs[] = array(
				'aid'		=> $this->aid,
				'pintro'	=> $pintro[$value['id']],
				'path'		=> $value['fileuploadurl'],
				'uploader'	=> $windid,
				'uptime'	=> $timestamp,
				'ifthumb'	=> $value['ifthumb']
			);
		}
		if ($this->attachs) {
			$this->db->update("INSERT INTO pw_cnphoto (aid,pintro,path,uploader,uptime,ifthumb) VALUES " . pwSqlMulti($this->attachs));
			$this->pid = $this->db->insert_id();
		}
	}

	function getAttachs() {
		return $this->attachs;
	}

	function getNewID() {
		return $this->pid;
	}

	function getLastPhoto() {
		$tmp = end($this->attachs);
		//$lastpos = strrpos($tmp['path'],'/') + 1;
		//$tmp['ifthumb'] && $tmp['path'] = substr($tmp['path'], 0, $lastpos) . 's_' . substr($tmp['path'], $lastpos);
		return $tmp['path'];
	}
}
?>