<?php


/**
 * class.ErrorPageAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 */

class ErrorPageAction extends BaseAction {
	
	protected function check($request, $response) {
	}

	protected function service($request, $response) {

		switch($request->getSwitch()) {
			default:
			$this->doShowPage($request, $response);
				break;
		}
	}

	/**
	 * ��ҳ��ʾ 
	 */
	protected function doShowPage($request, $response) {
		$response->setTplName("Homepage");
	}

}
?>