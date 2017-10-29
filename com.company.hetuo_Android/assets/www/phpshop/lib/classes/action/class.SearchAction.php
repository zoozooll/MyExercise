<?php
/**
 * class.SearchAction.php
 *-------------------------
 *
 * The show the detail of search product
 *
 * PHP versions 5
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 */

class SearchAction extends BaseAction {

	/**
	 * ����check
	 */
	protected function check($objRequest, $objResponse) {
		if(($objRequest->se) == NULL) {
			//throw new Exception("parameter error.");
		}
	}

	/**
	 * ��֧
	 */
	protected function service($objRequest, $objResponse) {
	
		switch($objRequest->getSwitch()) {
		default:
			$this->doSearchAction($objRequest, $objResponse);	
		}
	}
	
	/**
	 * ��ʼ��
	 */
	protected function doSearchAction($objRequest, $objResponse) {
		//������ʾ����
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
		//����tpl�ļ�
		$objResponse->setTplName("#Search");
		//����Meta(��ͨ)
		$objResponse -> setTplValue("__Meta", Common::getMeta($keyword));
	}
	
}
?>