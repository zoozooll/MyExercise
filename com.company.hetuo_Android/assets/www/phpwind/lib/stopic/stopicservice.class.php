<?php
!defined('P_W') && exit('Forbidden');
/*
dbs:
	pw_stopic
	pw_stopicCategory
	pw_stopicPicture

	pw_stopicBlock
	pw_stopicUnit
*/
class PW_STopicService {
	var $_stopicConfig = null;

	function getLayoutList() {
		$layoutTypes= $this->_getSTopicConfig('layoutTypes');
		$layoutList	= array ();
		foreach ( $layoutTypes as $typeName => $typeDesc ) {
			$tmp = $this->getLayoutInfo ($typeName);
			if ($tmp)
				$layoutList [$typeName] = $tmp;
		}
		return $layoutList;
	}

	function getLayoutInfo($typeName) {
		$stopicConfig = $this->_getSTopicConfig ();
		$checkDir = $stopicConfig ['layoutPath'] . $typeName . "/";
		if (! is_dir ( $checkDir ))
			return false;

		foreach ( $stopicConfig ['layoutConfig'] as $checkFile ) {
			if (! is_file ( $checkDir . $checkFile ))
				return false;
		}
		$checkData = array ();
		$checkData ['logo'] = $stopicConfig ['layoutBaseUrl'] . $typeName . "/" . $stopicConfig ['layoutConfig'] ['logo'];
		$checkData ['html'] = $checkFile . $stopicConfig ['layoutConfig'] ['html'];
		$checkData ['desc'] = $stopicConfig ['layoutTypes'] [$typeName];
		return $checkData;
	}
	function getLayoutDefaultSet($defaultStyle = 'baby_org') {
		$styleConfig = $this->getStyleConfig('baby_org');
		if (empty($styleConfig)) { 
			return $this->_getSTopicConfig('layout_set');
		} else {
			$layoutSet = $styleConfig['layout_set'];
			$layoutSet['bannerurl'] = $this->getStyleBanner($defaultStyle);
			return $layoutSet;
		}
	}

	function getLayoutSet($style) {
		$stylePath = $this->_getSTopicConfig('stylePath');
		if ($style && is_dir($stylePath.$style)) {
			return $this->getStyleConfig($style,'layout_set');
		}
		return $this->getLayoutDefaultSet();
	}

	function getStyles() {
		$stylePath = $this->_getSTopicConfig('stylePath');
		$fp	= opendir($stylePath);
		$styles	= array();
		while ($styleDir = readdir($fp)) {
			if (in_array($styleDir,array('.','..')) || strpos($styleDir,'.')!==false) continue;
			$styles[$styleDir] = array(
				'name'=>$this->getStyleConfig($styleDir,'name'),
				'minipreview'=>$this->getStyleMiniPreview($styleDir),
				'preview'=>$this->getStylePreview($styleDir),
			);
		}
		return $styles;
	}

	function getStyleMiniPreview($style) {
		return $this->_getSTopicConfig('styleBaseUrl').$style.'/'.$this->_getSTopicConfig('styleMiniPreview');
	}
	function getStylePreview($style) {
		return $this->_getSTopicConfig('styleBaseUrl').$style.'/'.$this->_getSTopicConfig('stylePreview');
	}
	function getStyleBanner($style) {
		$temp = $this->getStyleConfig($style,'banner');
		if ($temp) {
			if (strpos($temp,'http')===false) {
				$temp = $GLOBALS['db_bbsurl'].'/'.$temp;
			}
			return $temp;
		}
		if ($style && file_exists($this->_getSTopicConfig('stylePath').$style.'/'.$this->_getSTopicConfig('styleBanner'))) {
			return $this->_getSTopicConfig('styleBaseUrl').$style.'/'.$this->_getSTopicConfig('styleBanner');
		}
		return 'http://';
	}

	function getStyleConfig($style,$key='') {
		static $styles = array();
		if (!isset($styles[$style])) {
			$stylePath = $this->_getSTopicConfig('stylePath');
			if (file_exists($stylePath.$style.'/config.php')) {
				$styles[$style] = include Pcv($stylePath.$style."/config.php");
			} else {
				$styles[$style] = array();
			}
		}
		if ($key) {
			return isset($styles[$style][$key]) ? $styles[$style][$key] : '';
		}
		return $styles[$style];
	}

	function creatStopicHtml($stopic_id) {
		global $db_charset,$wind_version,$db_bbsurl;
		$stopic	= $this->getSTopicInfoById($stopic_id);
		if (!$stopic) return false;
		$tpl_content	= $this->getStopicContent($stopic_id,0);
		@extract($stopic, EXTR_SKIP);
		if (defined('A_P')) {
			include(A_P.'template/stopic.htm');
		} else {
			include(R_P.'apps/stopic/template/stopic.htm');
		}
		$output = str_replace(array('<!--<!---->','<!---->'),array('',''),ob_get_contents());
		ob_end_clean();
		$stopicDir	= $this->getStopicDir($stopic_id, $stopic['file_name']);
		writeover($stopicDir,$output);

		ObStart();
	}

	function addSTopic($fieldsData) {
		if (!is_array($fieldsData) || !count($fieldsData)) return 0;
		$fieldsData['create_date'] = time();

		$stopicDB = $this->_getSTopicDB();
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$stopicId = $stopicDB->add($fieldsData);
		//if ($stopicId && isset($fieldsData['copy_from']) && $fieldsData['copy_from']) $stopicDB->increaseField($fieldsData['copy_from'], 'used_count');
		if ($stopicId && isset($fieldsData['bg_id']) && $fieldsData['bg_id']) $stopicPicturesDB->increaseField($fieldsData['bg_id'], 'num');
		return $stopicId;
	}

	function deleteSTopics($stopicIds) {
		$success = 0;
		foreach ( $stopicIds as $stopicId ) {
			$success += $this->deleteSTopicById ( $stopicId );
		}
		return $success;
	}

	function deleteSTopicById($stopicId) {
		$stopicDB = $this->_getSTopicDB();
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$stopicUnitDB = $this->_getSTopicUnitDB();

		$stopicData = $stopicDB->get($stopicId);
		if (null == $stopicData) return false;
		$isSuccess = (bool) $stopicDB->delete($stopicId);
		if ($isSuccess && $stopicData['bg_id']) $stopicPicturesDB->increaseField($stopicData['bg_id'], 'num', -1);
		if ($isSuccess) {
			$stopicUnitDB->deleteAll($stopicId);
			$this->_delFile($this->getStopicDir($stopicId, $stopicData['file_name']));
		}
		return $isSuccess;
	}

	function _delFile($fileName) {
		return P_unlink($fileName);
	}

	function updateSTopicById($stopicId, $updateData) {
		$stopicDB = $this->_getSTopicDB();
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$stopicData = $stopicDB->get($stopicId);
		if (null == $stopicData) return false;

		$isSuccess = (bool) $stopicDB->update($stopicId,$updateData);
		if (isset($updateData['bg_id']) && $updateData['bg_id'] != $stopicData['bg_id']) {
			if ($stopicData['bg_id']) $stopicPicturesDB->increaseField($stopicData['bg_id'], 'num', -1);
			if ($updateData['bg_id']) $stopicPicturesDB->increaseField($updateData['bg_id'], 'num');
		}
		if (isset($updateData['file_name'])) {
			$stopicDB->updateFileName($stopicId, $updateData['file_name']);
			if ($updateData['file_name'] != $stopicData['file_name'] && '' != $stopicData['file_name']) {
				$this->_delFile($this->getStopicDir($stopicId, $stopicData['file_name']));
			}
		}
		return $isSuccess;
	}

	function getSTopicInfoById($stopicId) {
		$stopicDB = $this->_getSTopicDB();

		$stopic = $stopicDB->get($stopicId);
		if ($stopic) $stopic['bg_url'] = $stopic['bg_id'] ? $this->_getBackgroundUrl($stopic['bg_id']) : "";

		return $stopic;
	}

	function getEmptySTopic() {
		$stopicDB = $this->_getSTopicDB();
		$stopic = $stopicDB->getEmpty();
		return $stopic;
	}

	function countSTopic($keyword = '', $categoryId = 0) {
		$stopicDB = $this->_getSTopicDB();
		return $stopicDB->countByKeyWord ($keyword, $categoryId);
	}

	function findSTopicInPage($page, $perPage, $keyword = '', $categoryId = 0) {
		$stopicDB = $this->_getSTopicDB();
		$page = intval ( $page );
		$perPage = intval ( $perPage );
		if ($page <= 0 || $perPage <= 0) return array ();
		$result	= $stopicDB->findByKeyWordInPage($page, $perPage, $keyword, $categoryId);
		foreach ($result as $key=>$value) {
			$result[$key]['url'] = $this->getStopicUrl($value['stopic_id'], $value['file_name']);
			$result[$key]['create_date'] = get_date($value['create_date']);
		}
		return $result;
	}

	function findValidCategorySTopicInPage($page, $perPage, $categoryId = 0) {
		$stopicDB = $this->_getSTopicDB();
		$page = intval ( $page );
		$perPage = intval ( $perPage );
		if ($page <= 0 || $perPage <= 0)
			return array ();

		return $stopicDB->findValidByCategoryIdInPage ( $page, $perPage, $categoryId );
	}

	function findUsefulSTopicInCategory($limit, $categoryId = 0) {
		$stopicDB = $this->_getSTopicDB();
		$limit = intval ( $limit );
		if ($limit <= 0) return array ();

		return $this->_lardBackground($stopicDB->findByCategoryIdOrderByUsedInPage(1, $limit, $categoryId));
	}

	function getStopicDir($stopic_id, $file_name='') {
		$stopic_id = (int) $stopic_id;
		if (!$stopic_id) return false;
		if ('' == $file_name) $file_name = $stopic_id;
		$stopicDir	= Pcv($this->_getSTopicConfig('htmlDir'));
		if (!file_exists($stopicDir)) {
			if (mkdir($stopicDir)) {
				@chmod($stopicDir,0777);
			} else {
				showmsg('stopic_htm_is_not_777');
			}
		}
		return $stopicDir.'/'.$file_name.$this->_getSTopicConfig('htmlSuffix');
	}

	function getStopicUrl($stopic_id, $file_name) {
		if ('' == $file_name) return false;
		$stopicDir = $this->getStopicDir($stopic_id, $file_name);
		if ($stopicDir && file_exists($stopicDir)) {
			return $this->_getSTopicConfig('htmlUrl').'/'.$file_name.$this->_getSTopicConfig('htmlSuffix');
		} else {
			return false;
		}
	}

	function getStopicContent($stopic_id,$ifadmin) {
		$stopic	= $this->getSTopicInfoById($stopic_id);
		$units	= $this->getStopicUnitsByStopicId($stopic_id);
		$blocks	= $this->getBlocks();

		$parseStopicTpl	= L::loadClass('ParseStopicTpl','stopic');
		$tpl_content	= $parseStopicTpl->exute($this,$stopic,$units,$blocks,$ifadmin);
		return $tpl_content;
	}
	
	function isFileUsed($stopicId, $fileName) {
		$stopicId = intval($stopicId);
		$stopicDB = $this->_getSTopicDB();
		$isFind = $stopicDB->getByFileNameAndExcept($stopicId, $fileName);
		return $isFind && file_exists($this->getStopicDir($stopicId, $fileName));
	}

	/**
	 * 新增一个专题分类
	 *
	 * @param array $fieldData
	 * @return lastInsertId or null
	 */
	function addCategory($fieldData) {
		$stopicCategoryDB = $this->_getSTopicCategoryDB();
		return $stopicCategoryDB->add($fieldData);
	}

	/**
	 * 更新一个专题分类
	 *
	 * @param array $fieldData
	 * @param int $categoryId
	 * @return affected_rows or null
	 */
	function updateCategory($fieldData, $categoryId) {
		$stopicCategoryDB = $this->_getSTopicCategoryDB();
		$categoryId = intval ( $categoryId );
		if ($categoryId<= 0) {
			return NULL;
		}
		return $stopicCategoryDB->update($fieldData,$categoryId);
	}

	/**
	 * 删除一个专题分类 同时更新背景分类
	 *
	 * @param int $categoryId
	 * @return affected_rows or null
	 */
	function deleteCategory($categoryId) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$stopicCategoryDB = $this->_getSTopicCategoryDB();

		$categoryId = intval ( $categoryId );
		if ($categoryId <= 0 || ! $this->isAllowDeleteCategory ( $categoryId )) {
			return NULL;
		}
		return ($stopicCategoryDB->delete ( $categoryId )) ? $stopicPicturesDB->updateByCategoryId ( array("categoryid"=>0),$categoryId ) : NULL;
	}

	/**
	 * 是否允许删除分类
	 * 默认专题不能删除/分类下如果有专题不能删除
	 *
	 * @param int $categoryId
	 * @return bool
	 */
	function isAllowDeleteCategory($categoryId) {
		$stopicDB = $this->_getSTopicDB();
		$stopicCategoryDB = $this->_getSTopicCategoryDB();
		if ($stopicDB->countByCategoryId($categoryId)) return false;
		$category = $stopicCategoryDB->get($categoryId);
		if (!$category || $category['status'] == 1) return false;
		return true;
	}

	/**
	 * 获取所有专题分类
	 *
	 * @return array
	 */
	function getCategorys() {
		$stopicCategoryDB = $this->_getSTopicCategoryDB();
		return $stopicCategoryDB->gets ();
	}

	/**
	 * 获取某个分类信息
	 *
	 * @return array
	 */
	function getCategory($categoryId) {
		$stopicCategoryDB = $this->_getSTopicCategoryDB();
		return $stopicCategoryDB->get ( $categoryId );
	}

	function isCategoryExist($categoryName) {
		$stopicCategoryDB = $this->_getSTopicCategoryDB();
		return $stopicCategoryDB->getByName($categoryName) ? true : false;
	}

	/**
	 * 上传背景图片 并增加一条图片 记录
	 *
	 * @param array $fileArray
	 * @return picture name like[20090819152809.jpg]
	 */
	function uploadPicture($fileArray, $categoryId, $creator) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$uploadPictureClass = $this->_setUploadPictureClass();
		if (count ( $fileArray ) < 0 || intval ( $categoryId ) < 0 || trim ( $creator ) == "") {
			return null;
		}
		$filename = $uploadPictureClass->upload ( $fileArray );
		if ($filename === FALSE) {
			return null;
		}
		$fieldData = array (
			"title" => time(),
			"categoryid" => intval($categoryId),
			"path" => trim ($filename),
			"creator" => $creator
		);
		return $stopicPicturesDB->add ( $fieldData );
	}

	function _setUploadPictureClass() {
		$tempUpdatePicture = L::loadClass('UpdatePicture');
		$tempUpdatePicture->init($this->_getSTopicConfig ('bgUploadPath'));
		return $tempUpdatePicture;
		//return new UpdatePicture ($this->_getSTopicConfig ('bgUploadPath'));
	}

	/**
	 * 更新背景图片
	 *
	 * @param int $fieldData
	 * @param int $pictureId
	 * @return affected_rows or null
	 */
	function updatePicture($fieldData, $pictureId) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$pictureId = intval ( $pictureId );
		if ($pictureId <= 0) {
			return NULL;
		}
		return $stopicPicturesDB->update($fieldData,$pictureId);
	}

	/**
	 * 删除背景图片 删除数据并删除物理图片
	 *
	 * @param int $pictureId
	 * @return affected_rows or null
	 */
	function deletePicture($pictureId) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$uploadPictureClass = $this->_setUploadPictureClass();
		$pictureId = intval ( $pictureId );
		if ($pictureId <= 0) return null;
		if (!$this->isAllowDeletePicture($pictureId)) return null;
		$picture = $stopicPicturesDB->get($pictureId);
		if (!$picture) return null;
		return ($stopicPicturesDB->delete ( $pictureId )) ? $uploadPictureClass->delete ( $picture ['path'] ) : "";
	}

	/**
	 * 是否允许删除背景图片
	 *
	 * @param int $pictureId
	 * @return bool
	 */
	function isAllowDeletePicture($pictureId) {
		$stopicDB = $this->_getSTopicDB();
		return $stopicDB->countByBackgroundId($pictureId) ? false : true;
	}

	/**
	 * 获取背景图片
	 *
	 * @param int $categoryId 分类id，为0则找所有
	 * @return array
	 */
	function getPictures($categoryId = 0) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		$categoryId = intval ( $categoryId );
		if ($categoryId < 0) return array();

		return $this->_lardBackground( $categoryId
			? $stopicPicturesDB->getsByCategoryId ($categoryId)
			: $stopicPicturesDB->gets() );
	}

	function getBackgroundsInPage($page, $perPage, $categoryId=0) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		return $this->_lardBackground($stopicPicturesDB->getsInPage($page, $perPage, $categoryId));
	}

	function getPicturesAndDefaultBGs($categoryId = 0) {
		$defaults = $this->_getDefaultBackGrounds();
		$thisTypePictures = $this->getPictures($categoryId);
		return array_merge($defaults,$thisTypePictures);
	}

	function _getBackgroundUrl($bgId) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		if ($bgId<0) return $this->_getDefaultBackgroundUrl($bgId);

		$bg = $stopicPicturesDB->get($bgId);
		return $bg['path'] ? $this->_getSTopicConfig ('bgBaseUrl') . $bg ['path'] : "";
	}

	function _getDefaultBackgroundUrl($bgId) {
		$bgId = (int) $bgId;
		$bgId = abs($bgId);
		if (file_exists($this->_getSTopicConfig('bgDefalutPath').$bgId.'.jpg')) {
			return $this->_getSTopicConfig('bgDefalutUrl').$bgId.'.jpg';
		}
		return '';
	}

	function _getDefaultBackGrounds() {
		$backPath = $this->_getSTopicConfig('bgDefalutPath');
		$fp	= opendir($backPath);
		$backs	= array();

		while ($back = readdir($fp)) {
			if (in_array($back,array('.','..')) || !strpos($back,'.jpg')) continue;
			$id	= $this->_getDefaultBackGroudId($back);
			$backs[] = array(
				'id'	=> $id,
				'categoryid'	=> 'defalut',
				'thumb_url'	=> $this->_getDefaultBackgroundUrl($id)
			);
		}
		return $backs;
	}
	function _getDefaultBackGroudId($filename) {
		$temp = (int) $filename;
		if (!$temp || $temp<0) return false;
		return 0-$temp;
	}

	/**
	 * 统计背景图片个数
	 *
	 * @param int $categoryId 分类id，为0则统计所有
	 * @return number
	 */
	function countPictures($categoryId = 0) {
		$stopicPicturesDB = $this->_getSTopicPicturesDB();
		return $categoryId ? $stopicPicturesDB->countByCategoryId($categoryId) : $stopicPicturesDB->count();
	}

	function _lardBackground($bgList) {
		foreach ($bgList as $key => $bg) {
			$bgList[$key]['thumb_url'] = $bg['path'] ? $this->_getSTopicConfig('bgBaseUrl') . "thumb_" . $bg ['path'] : "";
		}
		return $bgList;
	}

	function addBlock($fieldData) {
		$fieldData = $this->_completeBlockFields($fieldData);

		$stopicBlockDB = $this->_getSTopicBloackDB();
		return $stopicBlockDB->add($fieldData);
	}

	function replaceBlock($fieldData) {
		$fieldData = $this->_completeBlockFields($fieldData);
		$stopicBlockDB = $this->_getSTopicBloackDB();
		return $stopicBlockDB->replace($fieldData);
	}

	function updateBlock($block_id,$fieldData) {
		$fieldData = $this->_completeBlockFields($fieldData);
		$stopicBlockDB = $this->_getSTopicBloackDB();
		return $stopicBlockDB->update($block_id,$fieldData);
	}

	function getBlocks() {
		return $this->_getSTopicConfig('blockTypes');
	}

	function getBlockById($typeId) {
		$blockTypes = $this->_getSTopicConfig('blockTypes');
		return $blockTypes[$typeId];
	}

	function _completeBlockFields($fieldData) {
		$parseStopicBlock = L::loadClass('ParseStopicBlock','stopic');
		$parseStopicBlock->execut($fieldData['tagcode']);
		$fieldData['begin']	= $parseStopicBlock->getBegin();
		$fieldData['loops']	= $parseStopicBlock->getLoops();
		$fieldData['end']	= $parseStopicBlock->getEnd();
		$fieldData['config']	= $parseStopicBlock->getConfig();
		$fieldData['replacetag']	= $parseStopicBlock->getReplacetag();
		return $fieldData;
	}

	function addUnit($fieldData) {
		$stopicUnitDB = $this->_getSTopicUnitDB();
		return $stopicUnitDB->add($fieldData);
	}

	function updateUnitByFild($stopic_id,$html_id,$fieldData) {
		$stopicUnitDB = $this->_getSTopicUnitDB();
		return $stopicUnitDB->updateByFild($stopic_id,$html_id,$fieldData);
	}

	function deleteUnits($stopic_id,$html_ids) {
		$stopicUnitDB = $this->_getSTopicUnitDB();
		return $stopicUnitDB->deletes($stopic_id,$html_ids);
	}

	function getStopicUnitsByStopicId($stopic_id) {
		$stopicUnitDB = $this->_getSTopicUnitDB();
		return $stopicUnitDB->getStopicUnits($stopic_id);
	}

	function getStopicUnitByStopic($stopic_id,$html_id) {
		$stopicUnitDB = $this->_getSTopicUnitDB();
		return $stopicUnitDB->getByStopicAndHtml($stopic_id,$html_id);
	}

	function getHtmlData($block_data, $block_type, $block_id=null) {
		$block_job = 'show';
		include Pcv(A_P."/template/admin/block/$block_type.htm");
		$output = ob_get_contents();
		ob_clean();
		return $output;
	}


	function _getUnitLoopsData($data,$block) {
		$temp = '';
		foreach ($data as $key=>$value) {
			$temp .= str_replace($block['replacetag'],$value,$block['loops']);
		}
		return $temp;
	}
/*
调用各个DB相对应的DB类
*/
	function _getSTopicDB() {
		return L::loadDB('STopic');
	}
	function _getSTopicPicturesDB() {
		return L::loadDB('STopicPictures');
	}
	function _getSTopicCategoryDB() {
		return L::loadDB('STopicCategory');
	}
	function _getSTopicUnitDB() {
		return L::loadDB('STopicUnit');
	}
	function _getSTopicBloackDB() {
		return L::loadDB('STopicBlock');
	}

	function _getSTopicConfig($key = '') {
		if (null == $this->_stopicConfig) {
			$this->_stopicConfig = include A_P."config.php";
		}
		if ($key) {
			return $this->_stopicConfig[$key];
		}
		return $this->_stopicConfig;
	}
}
?>