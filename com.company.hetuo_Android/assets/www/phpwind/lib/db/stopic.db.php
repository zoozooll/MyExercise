<?php
!defined('P_W') && exit('Forbidden');

class PW_STopicDB extends BaseDB {
	/**
	 * 表名
	 *
	 * @var string
	 * @access private
	 */
	var $_tableName = "pw_stopic";
	var $_bgTableName = "pw_stopicpictures";
	var $_cateTableName = "pw_stopiccategory";

	/**
	 * 添加专题记录
	 *
	 * @param array $fieldsData 专题记录数据数组
	 * @return int 成功返回专题id，否则返回0
	 */
	function add($fieldsData) {
		$fieldsData = $this->_checkData($fieldsData);
		if (!$fieldsData) return null;
		$this->_db->update("INSERT INTO ".$this->_tableName." SET " . $this->_getUpdateSqlString($fieldsData));
		$insertId = $this->_db->insert_id();
		return $insertId;
	}

	/**
	 * 删除专题记录
	 *
	 * @param int $stopicId 专题id
	 * @return int 删除行数
	 */
	function delete($stopicId) {
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE stopic_id=". intval($stopicId) ." LIMIT 1");
		return $this->_db->affected_rows();
	}

	/**
	 * 更新专题记录
	 *
	 * @param int $stopicId 专题id
	 * @param array $updateData 更新数据数组
	 * @return int 更新行数
	 */
	function update($stopicId, $updateData) {
		$updateData = $this->_checkData($updateData);
		if (!$updateData) return null;
		$this->_db->update("UPDATE ".$this->_tableName." SET " . $this->_getUpdateSqlString($updateData) . " WHERE stopic_id=". intval($stopicId) ." LIMIT 1");
		return $this->_db->affected_rows();
	}
	
	function updateFileName($stopicId, $fileName) {
		$stopicId = intval($stopicId);
		if ($stopicId <= 0 || '' == $fileName) return 0;
		
		return $this->_db->update("UPDATE ".$this->_tableName." SET file_name='' WHERE file_name=".$this->_addSlashes($fileName)." AND stopic_id!=".$stopicId);
	}

	function increaseField($stopicId, $fieldName) {
		if (!in_array($fieldName, array('used_count', 'view_count'))) return 0;
		$this->_db->update("UPDATE ".$this->_tableName." SET $fieldName=$fieldName+1 WHERE stopic_id=". intval($stopicId) ." LIMIT 1");
		return $this->_db->affected_rows();
	}

	/**
	 * 获取专题记录
	 *
	 * @param int $stopicId 专题id
	 * @return array/null 找到返回专题数据，否则返回null
	 */
	function get($stopicId) {
		$data = $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE stopic_id=".intval($stopicId));
		if (!$data) return null;
		return $this->_unserializeData($data);
	}
	
	function getEmpty() {
		$data = $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE layout_config='' AND block_config=''");
		if (!$data) return null;
		return $this->_unserializeData($data);
	}

	/**
	 * 根据关键字查询专题数
	 *
	 * @param string $keyword 关键字，为空则查找所有
	 * @return int 专题数
	 */
	function countByKeyWord($keyword = '', $categoryId = 0) {
		$sqlAdd = array();
		if ('' != $keyword) $sqlAdd[] = " title LIKE ".$this->_addSlashes('%' . $keyword . '%')." ";
		if ($categoryId > 0) $sqlAdd[] = " category_id=".$this->_addSlashes($categoryId)." ";
		$sqlAdd = count($sqlAdd) ? " WHERE " . implode(" AND ", $sqlAdd) : "";
		$rt = $this->_db->get_one("SELECT COUNT(*) AS total_num FROM ".$this->_tableName." ".$sqlAdd);
		return $rt['total_num'];
	}

	/**
	 * 根据关键字分页查询专题记录
	 *
	 * @param int $page 页数>=1
	 * @param int $perPage 每页记录数>=1
	 * @param string $keyword 关键字，为空则查找所有
	 * @return array 专题数据数组
	 */
	function findByKeyWordInPage($page, $perPage, $keyword = '', $categoryId = 0) {
		$page = intval($page);
		$perPage = intval($perPage);
		if ($page <= 0 || $perPage <= 0) return array();

		$offset = ($page - 1) * $perPage;

		$sqlAdd = array();
		if ('' != $keyword) $sqlAdd[] = " a.title LIKE ".$this->_addSlashes('%' . $keyword . '%')." ";
		if ($categoryId > 0) $sqlAdd[] = " a.category_id=".$this->_addSlashes($categoryId)." ";
		$sqlAdd = count($sqlAdd) ? " WHERE " . implode(" AND ", $sqlAdd) : "";
		
		$query = $this->_db->query("SELECT a.*,c.title as catetitle FROM ".$this->_tableName." a LEFT JOIN ".$this->_cateTableName." c ON a.category_id=c.id $sqlAdd ORDER BY a.create_date DESC LIMIT $offset,$perPage");
		return $this->_getAllResultFromQuery($query);
	}

	/**
	 * 按分类分页查找有效专题记录
	 *
	 * @param int $page 页数>=1
	 * @param int $perPage 每页记录数>=1
	 * @param int $categoryId 分类，0则查找所有
	 * @return array 专题数据数组
	 */
	function findValidByCategoryIdInPage($page, $perPage, $categoryId = 0) {
		$page = intval($page);
		$perPage = intval($perPage);
		$categoryId = intval($categoryId);
		if ($page <= 0 || $perPage <= 0) return array();

		$offset = ($page - 1) * $perPage;
		$sqlAdd = $categoryId ? " AND category_id=$categoryId " : "";
		$nowTime = time();
		$query = $this->_db->query("SELECT * FROM ".$this->_tableName."  WHERE start_date<=$nowTime AND end_date>$nowTime $sqlAdd ORDER BY create_date DESC LIMIT $offset,$perPage");
		return $this->_getAllResultFromQuery($query);
	}

	/**
	 * 按分类按使用次数排序分页查找专题记录
	 *
	 * @param int $page 页数>=1
	 * @param int $perPage 每页记录数>=1
	 * @param int $categoryId 分类，0则查找所有
	 * @return array 专题数据数组
	 */
	function findByCategoryIdOrderByUsedInPage($page, $perPage, $categoryId = 0) {
		$page = intval($page);
		$perPage = intval($perPage);
		$categoryId = intval($categoryId);
		if ($page <= 0 || $perPage <= 0) return array();

		$offset = ($page - 1) * $perPage;
		$sqlAdd = $categoryId ? " WHERE category_id=$categoryId " : "";
		$query = $this->_db->query("SELECT a.*, b.path FROM ".$this->_tableName." a LEFT JOIN ".$this->_bgTableName." b ON a.bg_id=b.id $sqlAdd ORDER BY used_count DESC LIMIT $offset,$perPage");
		return $this->_getAllResultFromQuery($query);
	}

	/**
	 * 统计使用某背景的专题数
	 *
	 * @param int $backgroundId 背景id
	 * @return int 使用个数
	 */
	function countByBackgroundId($backgroundId) {
		$backgroundId = intval($backgroundId);
		if ($backgroundId <= 0) return 0;

		return $this->_db->get_value("SELECT COUNT(*) FROM ".$this->_tableName." WHERE bg_id=$backgroundId");
	}

	function countByCategoryId($categoryId) {
		$categoryId = intval($categoryId);
		if ($categoryId <= 0) return 0;

		return $this->_db->get_value("SELECT COUNT(*) FROM ".$this->_tableName." WHERE category_id=$categoryId");
	}
	
	function getByFileNameAndExcept($stopicId, $fileName) {
		$stopicId = intval($stopicId);
		if ($stopicId <= 0 || '' == $fileName) return null;
		
		return $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE file_name=".$this->_addSlashes($fileName)." AND stopic_id!=".$stopicId);
	}

	function getStruct() {
		return array('stopic_id','title','category_id','bg_id','copy_from','layout','create_date',
		'start_date','end_date','used_count','view_count','block_config','layout_config', 'nav_config',
		'banner_url', 'seo_keyword', 'seo_desc', 'file_name');
	}

	function _checkData($data){
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		$data = $this->_serializeData($data);
		return $data;
	}

	function _serializeData($data) {
		if (isset($data['layout_config']) && is_array($data['layout_config'])) $data['layout_config'] = serialize($data['layout_config']);
		if (isset($data['block_config']) && is_array($data['block_config'])) $data['block_config'] = serialize($data['block_config']);
		if (isset($data['nav_config']) && is_array($data['nav_config'])) $data['nav_config'] = serialize($data['nav_config']);
		return $data;
	}

	function _unserializeData($data) {
		if ($data['layout_config']) $data['layout_config'] = unserialize($data['layout_config']);
		if ($data['block_config']) $data['block_config'] = unserialize($data['block_config']);
		if ($data['nav_config']) $data['nav_config'] = unserialize($data['nav_config']);
		return $data;
	}
}

?>