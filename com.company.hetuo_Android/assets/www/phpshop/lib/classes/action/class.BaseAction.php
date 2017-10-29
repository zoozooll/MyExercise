<?php
/**
 * class.BaseAction.php
 * --------------------
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 * @date 2006-06-17
 */


/**
 * �������,�����ڸ���ģ��֮�䴫�ݲ���
 * @date 2006-06-17
 */
class HttpRequest {
	/** ��֧KEY,��$_REQUEST['switch'] **/
	private static $SWITCH_KEY = "switch";
	/** �����������ύ����,��$_REQUEST.�����޸� **/
	private $parameters = NULL;
	/** ��֧ **/
	private $switchValue = NULL;
	
	private $pname = NULL;
	
	public function __construct() {
		$this -> parameters = $_REQUEST;
		if(isset($this -> parameters["param"])) {
			if($this -> parameters["param"] != NULL) {
				$this -> parameters = array_merge($this -> getParse($this -> __get("param")), $this -> parameters);
				unset($this -> parameters["param"]);
			}
		}
	}
	
	public function __get($pname) {
		if(isset($this -> parameters[$pname])) {
			if (get_magic_quotes_gpc()) {
				return $this -> parameters[$pname];
			}
			if(is_array($this -> parameters[$pname]) && !empty($this -> parameters[$pname])) {
				return $this -> addArraySlashes($this -> parameters[$pname]);
			}
			return addslashes($this -> parameters[$pname]);
		} else {
			return NULL;
		}
	}
	
	public function stripTagsParameters($str) {
		if(is_array($this -> parameters) && !empty($this -> parameters)) {
			$this -> parameters = $this -> stripTags($this -> parameters, $str);
		}
	}
	
	public function stripTags($arrRs, $str) {
		foreach($arrRs as $k => $v) {
			if(is_array($v)) {
				$arrRs[$k] = $this -> stripTags($v, $str);
			} else {
				$arrRs[$k] = preg_replace($str, '', $v);
			}
		}
		return $arrRs;
	}
	
	private function addArraySlashes($arrRs) {	
		foreach($arrRs as $k => $v) {
			if(is_array($v)) {
				$arrRs[$k] = $this -> addArraySlashes($v);
			} else {
				$arrRs[$k] = addslashes($v);
			}
		}
		return $arrRs;
	}
		
	public 	function __set($pname, $value) {
		if(empty($pname)){
			return false;
		} else {
			$this -> parameters[$pname] = $value;
		}
	}
	
	public function __isset($pname) {
	   return isset($this -> parameters[$pname]);
	}
	
	public function __unset($pname) {
	   unset($this -> parameters[$pname]);
	}
	
	public function getParse($arg) {
		$ret = array();
		$param = explode("-", $arg);
		foreach($param as $str) {
			$tmp = explode("_", $str, 2);
			$ret[$tmp[0]] = $tmp[1];
		}
		return $ret;
	}
	/**
	 * ȡ���ڲ���֧
	 */
	public function getSwitch() {
		if($this -> switchValue == NULL) {
			if(isset($this -> parameters[self::$SWITCH_KEY])) {
				$this -> switchValue = $this -> parameters[self::$SWITCH_KEY];
			}
		}
		return $this -> switchValue;
	}

	/**
	 * ȡ���ڲ���֧
	 */
	public function setSwitch($switchValue) {
		$this -> switchValue = $switchValue;
	}

}

/**
 * ��Ӧ����,����������View�㴫�ݵĲ���
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 * @date 2006-06-17
 */
class HttpResponse {
	/** ģ���ļ��� **/
	private $tplName = NULL;
	/** ģ����� **/
	private $tplValues = NULL;
	
	/**
	 * ���캯��
	 */
	public function __construct() {
		$this -> tplName = NULL;
		$this -> tplValues = array();
	}

	/**
	 * ȡ��ģ����
	 */
	public function getTplName() {
		return $this -> tplName;
	}

	/**
	 * �趨ģ����
	 */
	public function setTplName($tplName) {
		$this -> tplName = $tplName;
	}

	/**
	 * �趨(���)ģ�����
	 */
	public function setTplValue($name, $value) {
		if(empty($name)) {
			if(__Debug){
				throw new Exception("tpl value's name cann't empty.");
			}
			error("tpl value's name cann't empty.");
			return NULL;
		}
		$this -> tplValues[$name] = $value;
	}

	/**
	 * ȡ��ģ���е�ֵ(��������)
	 */
	public function getTplValues($name = NULL) {
		if(!empty($name)) return $this -> tplValues[$name];
		return $this -> tplValues;
	}
}

/**
 * ��Ӧ����,������������View����ʾ������
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 * @date 2006-06-17
 */
abstract class BaseAction {
	private $displayDisabled = false;
	private $isHeader = false;
	private $compiler = NULL;
	private $iscreatefile = false;
	private $filename = '';
	private $dir = '';
	/**
	 * �����������,����ϵͳ����(���ش���,���׳��쳣)
	 */
	protected abstract function check($objRequest, $objResponse);

	/**
	 * ִ��Ӧ���߼�
	 */
	protected abstract function service($objRequest, $objResponse);
	
	/**
	 * ��Դ����
	 */
	protected function release($objRequest, $objResponse) { }

	/**
	 * ������ʾ
	 */
	public function setDisplay($flag = true) {
		$this -> displayDisabled = $flag;
	}
	/**
	 * �Ƿ�Header
	 */
	public function sendHeader($flag = true) {
		$this -> isHeader = $flag;
	}
	/*
	 * �Ƿ������ļ�
	*/
	public function createfile($flag = true, $filename, $dir = __PRODUCT_XML) {
		$this -> iscreatefile = $flag;
		$this -> filename = $filename;
		$this -> dir = $dir;
	}
	/***
	 * �ܷ����ģ��
	 */
	public function setCompiler($flag = true) {
		$this -> compiler = $flag;
	}
	/**
	 * Controller��ĵ�����ں���,��scripts�е���
	 */
	public function execute($switch = NULL) {
		$startTime = getMicrotime();
		try {
			$objRequest = new HttpRequest();
			$objResponse = new HttpResponse();
			//ָ��switch
			if($switch != NULL) {
				$objRequest -> setSwitch($switch);
			}
			//�������
			$this -> check($objRequest, $objResponse);
			//ִ�з���
			$this -> service($objRequest, $objResponse);
			if($this -> displayDisabled == false) {
				ob_start();
				$this -> display($objResponse, $this -> compiler);
				if($this -> iscreatefile) {
					$filecontent = ob_get_contents();
					File::creatFile($this -> filename, $filecontent, $this -> dir);
				}
				ob_implicit_flush(1);
				ob_end_flush();
			}
			//��Դ����
			$this->release($objRequest, $objResponse);

		} catch (Exception $e) {
			try {
				//��Դ����
				$this -> release($objRequest, $objResponse);

			} catch(Exception $e) {
				logError($e -> getMessage(), __MODEL_EXCEPTION);
			}
			//������־
			logError($e -> getMessage(), __MODEL_EXCEPTION);
			logError($e -> getTraceAsString(), __MODEL_EMPTY);
			//�ض��򵽴���ҳ��
			redirect("errorpage.htm");
		}
		//debug...
		if(__Debug) {
			$endTime = getMicrotime();
			$useTime = $endTime - $startTime;
			logDebug("excute time $useTime s");
		}
	}

	/**
	 * ����View�����
	 */
	private function display($objResponse, $compiler = true) {
		if($this -> isHeader == false) {
			header("Content-type: text/html; charset=".__CHARSET);
		}
		$tplName = $objResponse->getTplName();
		if(empty($tplName)) {
			throw new Exception("template name cann't empty.");
		}
		// dispaly
		$temp = new Template;
		$temp -> setTpl($tplName.".tpl");
		$temp -> setVar($objResponse -> getTplValues());
		$temp -> setVar("__CHARSET", __CHARSET);
		$temp -> setVar("__LANGUAGE", __LANGUAGE);
		$temp -> diapaly($compiler);
	}
}
?>