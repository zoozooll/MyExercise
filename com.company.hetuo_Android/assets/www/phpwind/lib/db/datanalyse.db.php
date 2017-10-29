<?php
!defined ('P_W') && exit('Forbidden');
@include_once (R_P . 'lib/base/basedb.php');
class PW_DatanalyseDB extends BaseDB {
	var $_tableName = "pw_datanalyse";
	var $_tag;
	var $_action;
	var $_timeunit;
	var $_num;

	/**
	 * 删除所有过期的数据
	 * @param $overtime
	 * @return unknown_type
	 */
	function clearAllOverdueData($overtime){
		$sql = "DELETE FROM $this->_tableName WHERE timeunit < " . pwEscape($overtime);
		$this->_db->update ( $sql );
	}


	/**
	 * 清理所有对应Action的超时数据，并且保留历史数据
	 * @param $action
	 * @param $otime
	 * @param $htime
	 * @return unknown_type
	 */
	function clearOverdueDataByAction($otime,$htime){
		/* 清理超时数据,保留历史数据 */
		$sql = "DELETE FROM $this->_tableName WHERE action = $this->_action AND timeunit < " . pwEscape($otime) . " AND timeunit != " . pwEscape($htime) ;
		$this->_db->update($sql);
	}


	/**
	 * 清理所有非Top数据（没有进入榜单的数据）
	 * @param $action
	 * @param $dtimes
	 * @param $maxNum
	 * @return unknown_type
	 */
	function clearNotTopDataByAction($dtimes,$maxNum){
		/* 清理所有非Top数据（没有进入榜单的数据） */
		if ($dtimes) {
			foreach($dtimes as $t){
				$sql = "SELECT num FROM $this->_tableName
						WHERE action = $this->_action AND timeunit = ". pwEscape($t) ."
						ORDER BY num DESC LIMIT $maxNum,1";
				$rt = $this->_db->get_one($sql);
				$_w = "";
				if ($rt) {
					$_w .= "( action = $this->_action AND timeunit = ". pwEscape($t) ." AND num < ". pwEscape($rt['num']) ." ) OR ";
				}
			}
		}
		$_w = trim($_w,"OR");
		if ($_w) {
			$this->_db->update("DELETE FROM $this->_tableName WHERE $_w ");
		}
	}

	/**
	 * 更新数据
	 * @param $htime  历史时间戳
	 * @return unknown_type
	 */
	function update($htime){
		$htime = pwEscape($htime);
		if (!empty($this->_tag) && !empty($this->_action) && !empty($this->_timeunit)) {
			$this->_db->pw_update(
				"SELECT num FROM $this->_tableName WHERE tag = $this->_tag AND action = $this->_action AND timeunit = $this->_timeunit",
				"UPDATE $this->_tableName SET num = num+$this->_num WHERE tag = $this->_tag AND action = $this->_action AND timeunit = $this->_timeunit",
				"INSERT INTO $this->_tableName SET tag = $this->_tag , action = $this->_action , timeunit = $this->_timeunit , num = $this->_num"
			);
			/* 更新历史记录 */
			$this->_db->pw_update(
				"SELECT num FROM $this->_tableName WHERE tag = $this->_tag AND action = $this->_action AND timeunit = $htime",
				"UPDATE $this->_tableName SET num = num+$this->_num WHERE tag = $this->_tag AND action = $this->_action AND timeunit = $htime",
				"INSERT INTO $this->_tableName SET tag = $this->_tag , action = $this->_action , timeunit = $htime , num = $this->_num"
			);
		}
	}

	/**
	 * @param $_tag the $_tag to set
	 */
	function set_tag($_tag) {
		$this->_tag = pwEscape($_tag);
	}

	/**
	 * @param $_action the $_action to set
	 */
	function set_action($_action) {
		$this->_action = pwEscape($_action);
	}

	/**
	 * @param $_timeunit the $_timeunit to set
	 */
	function set_timeunit($_timeunit) {
		$this->_timeunit = pwEscape($_timeunit);
	}

	/**
	 * @param $_num the $_num to set
	 */
	function set_num($_num) {
		$this->_num = pwEscape($_num);
	}
}
?>