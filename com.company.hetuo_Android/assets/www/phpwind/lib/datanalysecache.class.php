<?php
!defined ('P_W') && exit('Forbidden');
class DatanalyseCache {
	var $fdir;
	var $filepath;
	var $filename;
	function DatanalyseCache() {
		$this->fdir = D_P . "data/bbscache/";
		$this->filepath = $this->fdir . "datanalyse_cache.php";
		$this->filename = "datanalyse_cache";
	}

	/**
	 * @param $tag
	 * @param $result
	 * @return unknown_type
	 */
	function writeCache($result, $_md5key) {
		$data = var_export ( $result, TRUE );
		$timestamp = PwStrtoTime ( get_date ( time (), "Y-m-d H:i:s" ) );
		$file = @fopen ( $this->filepath, "w" );
		if ($file) {
			fwrite ( $file, "<?php\r\n", 100 );
			fwrite ( $file, "\$_overtime='" . $timestamp . "';\r\n", 100 );
			fwrite ( $file, "\$_md5key='" . $_md5key . "';\r\n" );
			fwrite ( $file, "\$_result=" . $data . ";\r\n" );
			fwrite ( $file, "?>\r\n", 100 );
			fclose ( $file );
		}
	}

	/**
	 * @param $tag
	 * @param $result
	 * @return unknown_type
	 */
	function ifUpdateCache($md5key) {
		if (file_exists ( $this->filepath )) {
			$dcache = L::config(null,$this->filename);
			$overtime = (int)($dcache['_overtime'] + (60*10));
			$nowtime = PwStrtoTime ( get_date ( time (), "Y-m-d H:i:s" ) );
			if ($overtime <= $nowtime || $md5key != $dcache['_md5key']) {
				return true;
			}
			return false;
		}else{
			return true;
		}
	}

	/**
	 * @return unknown_type
	 */
	function setResult(&$result) {
		$dcache = L::config(null,$this->filename);
		$result = $dcache['_result'];
	}

}
?>