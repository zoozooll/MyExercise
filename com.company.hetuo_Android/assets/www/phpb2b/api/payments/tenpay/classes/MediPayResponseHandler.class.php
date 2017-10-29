<?php
require ("ResponseHandler.class.php");

class MediPayResponseHandler extends ResponseHandler {
	
	function doShow() {
		$strHtml = "<html><head>\r\n" .
			"<meta name=\"TENCENT_ONLINE_PAYMENT\" content=\"China TENCENT\">" .
			"</head><body></body></html>";
			
		echo $strHtml;
		
		exit;		
	}
	/**
	 * @Override
	 * 签名规则,按字母a-z排序,遇到空值不参加签名
	 * @return bool
	 */
	function isTenpaySign() {
	
		$signParameterArray = array(
			'attach',
			'buyer_id',
			'cft_tid',
			'chnid',
			'cmdno',
			'mch_vno',
			'retcode',
			'seller',
			'status',
			'total_fee',
			'trade_price',
			'transport_fee',
			'version'
		);
		
		//按字母a-z排序
		ksort($signParameterArray);
		
		foreach($signParameterArray as $k ) {
			$v = $this->getParameter($k);
			if(isset($v)) {
				$signPars .= $k . "=" . urldecode($v) . "&";
			}
		}
		
		$signPars .= "key=" . $this->getKey();
		
		$sign = strtolower(md5($signPars));
		
		$tenpaySign = strtolower($this->getParameter("sign"));
				
		//debug信息
		$this->_setDebugInfo($signPars . " => sign:" . $sign .
				" tenpaySign:" . $this->getParameter("sign"));
		
		return $sign == $tenpaySign;
	
	}
	
}


?>