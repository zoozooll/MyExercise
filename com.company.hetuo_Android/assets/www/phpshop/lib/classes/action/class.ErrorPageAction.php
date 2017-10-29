<?php


/**
 * class.ErrorPageAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
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
	 * 首页显示 
	 */
	protected function doShowPage($request, $response) {
		$response->setTplName("Homepage");
	}

}
?>