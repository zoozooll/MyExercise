<?php


/**
 * class.SendMessageAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 */

class SendMessageAction extends BaseAction {
	
	protected function check($request, $response) {
	}

	protected function service($request, $response) {

		switch($request->getSwitch()) {
			case "msg":
				$this -> sendMsg($request, $response);
			break;
			default:
				$this-> sendMail($request, $response);
			break;
		}
	}

	/**
	 * ��ʾ 
	 */
	protected function sendMail($request, $response) {
		require_once(FRONT_END_ROOT."/etc/smtp_define.php");
		
		$objCookie = new Cookie;
		$arrCookieUser = CommonDao::getLoginUser($objCookie);	
		
		$femail = $request -> femail;
		$myemail = $request -> myemail;
		$sharefriendcontent = $request -> sharefriendcontent;
		$title = $request -> title;
		$myname = $request -> myname;
		$mailsubject = "$title";
		$datetime = Common::getCommonTime();
		
		$style = $request -> style;
		$width = $request -> width;
		$height = $request -> height;
		$xmlwebfile = PathManager::getXMLWebPath($arrCookieUser["loginnameid"], $arrCookieUser["tableid"], $request -> sll);

		require_once(FRONT_END_ROOT."/etc/config/htmlmail.php");
		$content = $html;
		
		$objSmtp = new SmtpMail;
		$objSmtp -> smtp(__SMTPserver, __SMTPserverport, __SMTPverify, __SMTPuser, __SMTPpass);

		if($objSmtp->sendmail($femail, __SMTPusermail, $mailsubject, $content, __SMTPtype, __SMTPsendfrom)) {
			$response->setTplValue("semdmail", 'yes');
		}
		$response->setTplName("semdmail");
	}
	
	protected function sendMsg($request, $response) {
		$objCookie = new Cookie;
		$arrCookieUser = CommonDao::getLoginUser($objCookie);	
		$femail = $request -> femail;
		$myemail = $request -> myemail;
		$sharefriendcontent = $request -> sharefriendcontent;
		$xmlfilenameid = $request -> sll;
		File::writeFile("mail.txt",Utilities::arrayTurnStr($_REQUEST));
		
		$this -> setDisplayDisabled(true);
	}
	
}
?>