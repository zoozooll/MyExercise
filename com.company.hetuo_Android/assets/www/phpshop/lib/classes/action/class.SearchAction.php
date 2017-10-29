<?php
/**
 * class.SearchAction.php
 *-------------------------
 *
 * The show the detail of search product
 *
 * PHP versions 5
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class SearchAction extends BaseAction {

	/**
	 * 入力check
	 */
	protected function check($objRequest, $objResponse) {
		if(($objRequest->se) == NULL) {
			//throw new Exception("parameter error.");
		}
	}

	/**
	 * 分支
	 */
	protected function service($objRequest, $objResponse) {
	
		switch($objRequest->getSwitch()) {
		default:
			$this->doSearchAction($objRequest, $objResponse);	
		}
	}
	
	/**
	 * 初始化
	 */
	protected function doSearchAction($objRequest, $objResponse) {
		//设置显示对象
		$keyword = trim($objRequest->se);
		$arrCatProduct = SearchDao::getSearchProduct($keyword);

		if($arrCatProduct[0]['prodnum'] == '0') {
			$arrCatProduct = ''; 
		} else {
			$arrParam['se'] = urlencode($keyword);
			$num = count($arrCatProduct);
			for($i = 0; $i<$num; $i++) {
				$arrCatProduct[$i]['enname'] = empty($arrCatProduct[$i]['enname']) ? $arrCatProduct[$i]['cid'] : $arrCatProduct[$i]['enname'];
				if($arrCatProduct[$i]['name'] == $keyword) {
					$arrCatProduct[$i]['url'] = Patch::getCategoryUrl($arrCatProduct[$i]['enname'], $arrCatProduct[$i]['cid'],$arrCatProduct[$i]['enname']);
				} else {
					$arrCatProduct[$i]['url'] = Patch::getCategoryUrl($arrCatProduct[$i]['enname'], $arrCatProduct[$i]['cid'],$arrCatProduct[$i]['enname'], $arrParam);
				}
			}
		}
		$objResponse->setTplValue("categoryproduct", $arrCatProduct);
		$objResponse->setTplValue("keyword", htmlspecialchars($keyword));
		//设置tpl文件
		$objResponse->setTplName("#Search");
		//设置Meta(共通)
		$objResponse -> setTplValue("__Meta", Common::getMeta($keyword));
	}
	
}
?>