<?php


/**
 * class.AdminAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 */

class AdminAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {
		switch($objRequest->getSwitch()) {
			case 'checkloin':
				$this -> checkLogin($objRequest, $objResponse);
				break;
				
			case 'admin':
				$this -> doAdmin($objRequest, $objResponse);
				break;
				
			default:
				$this -> doShowPage($objRequest, $objResponse);
				break;
		}
	}

	/**
	 * ��ʾ 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$objResponse->setTplValue("__Meta", Meta::getLogin());
		//set tpl
		$objResponse->setTplName("../admin/#login");
	}
	
	protected function doAdmin($objRequest, $objResponse) {
		$objSession = new Session;
		self::checkLoginAdmin($objSession);
		
		$arrSite = Common::getSiteInfo(NULL, 'baseset');
		$id = $objSession -> glo_id;
		$md5id = md5($id);
		if(Cache::isValid($md5id, 30)) {
			$arrMenu = Cache::fetch($md5id);
		} else {
			if(!($arrMenu = AdminDao::getAdminInfo($id))) {
				throw new Exception("error: get menu error.<br>");
			}
			Cache::cachePage($md5id, $arrMenu);
		}

		$__Meta = Meta::getAdmin($objSession -> glo_adminname);
		$objResponse -> setTplValue("site", $arrSite['value1']);
		$objResponse -> setTplValue("web_url", $arrSite['value3']);
		$objResponse -> setTplValue("admin_name", $objSession -> glo_adminname);
		$objResponse -> setTplValue("admin_menu", $arrMenu);
		$objResponse -> setTplValue("lid", '');
		$objResponse -> setTplValue("mid", '');
		$objResponse -> setTplValue("__Meta", $__Meta);
		//set tpl
		$objResponse -> setTplName("../admin/#index");
	}
	
	protected function checkLogin($objRequest, $objResponse) {
		$objSession = new Session;
		
		if(($objRequest -> logout) != NULL) {
			unset($objSession -> glo_id);
			unset($objSession -> glo_adminname); 
			unset($objSession -> glo_adminpassword); 
			redirect("login.php"); 
		}
		
		$name = $objRequest -> name;
		$pass = $objRequest -> pass;
		$vali = strtolower($objRequest -> validate);
		if($name == "" or $pass == "" or $vali == "" or $vali != ($objSession -> sessionvalidate)) {
			alert(conutf8('�û�����������������֤�벻��ȷ��'), 0);
			redirect("login.php"); 
		}
		if((getHis() - $objSession -> glo_sessiontime) > 200) {
			alert(conutf8('��֤����ڣ������µ�¼'));
			redirect("login.php"); 
		}
		if($arrAdmin = AdminDao::getAdmin($name, $pass)) {
			$objSession -> glo_adminname = $arrAdmin["name"];
			$objSession -> glo_id = $arrAdmin["id"];
			$objSession -> glo_adminpassword = $pass;
			unset($objSession -> validate);
			$rValue['ip'] = onLineIp();
			$rValue['time'] = Common::getCommonTime();
			AdminDao::updateAdminlogin($arrAdmin["id"], $rValue);
		}
		if($objSession -> glo_adminname != $arrAdmin["name"]) {
			unset($objSession -> glo_id);
			unset($objSession -> glo_adminname); 
			unset($objSession -> glo_adminpassword); 
			alert(conutf8('ϵͳ�����������ĵ�¼����'), 0);
			redirect("login.php"); 
		} else {
			redirect("../admin.php"); 
		}
	}
	
	public static function checkLoginAdmin($objSession = NULL) {
		if(!is_object($objSession)) {
			$objSession = new Session;
		}
		$admin_name = $objSession -> glo_adminname;
		if(empty($admin_name)) {
			alert(conutf8('��¼��ʱ�������µ�¼��'), '0');
			redirect("admin/login.php");
		}
		$arrAdmin = AdminDao::getAdmin($admin_name, $objSession -> glo_adminpassword);
		if(!$arrAdmin) {
			alert(conutf8('��¼�������������û��������룡'), '0');
			redirect("admin/login.php");
		}
		if($objSession -> glo_id != $arrAdmin["id"]) {
			alert(conutf8('��¼�����������Ƿ���Ȩ�޵�¼���ϵͳ��'), '0');
			redirect("admin/login.php"); 
		}
	}
}
?>