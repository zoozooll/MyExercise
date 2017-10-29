<?php
/**
 * class.BaseAction.php
 * --------------------
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 * @date 2006-06-17
 */


/**
 * 请求对象,用于在各个模块之间传递参数
 * @date 2006-06-17
 */
class HttpRequest {
	/** 分支KEY,即$_REQUEST['switch'] **/
	private static $SWITCH_KEY = "switch";
	/** 保存从浏览器提交变量,即$_REQUEST.不可修改 **/
	private $parameters = NULL;
	/** 分支 **/
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
	 * 取得内部分支
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
	 * 取得内部分支
	 */
	public function setSwitch($switchValue) {
		$this -> switchValue = $switchValue;
	}

}

/**
 * 响应对象,用于设置向View层传递的参数
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 * @date 2006-06-17
 */
class HttpResponse {
	/** 模板文件名 **/
	private $tplName = NULL;
	/** 模板参数 **/
	private $tplValues = NULL;
	
	/**
	 * 构造函数
	 */
	public function __construct() {
		$this -> tplName = NULL;
		$this -> tplValues = array();
	}

	/**
	 * 取得模板名
	 */
	public function getTplName() {
		return $this -> tplName;
	}

	/**
	 * 设定模板名
	 */
	public function setTplName($tplName) {
		$this -> tplName = $tplName;
	}

	/**
	 * 设定(添加)模板参数
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
	 * 取得模板中的值(返回数组)
	 */
	public function getTplValues($name = NULL) {
		if(!empty($name)) return $this -> tplValues[$name];
		return $this -> tplValues;
	}
}

/**
 * 响应对象,保存了用于在View层显示的数据
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
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
	 * 检查入力参数,若是系统错误(严重错误,则抛出异常)
	 */
	protected abstract function check($objRequest, $objResponse);

	/**
	 * 执行应用逻辑
	 */
	protected abstract function service($objRequest, $objResponse);
	
	/**
	 * 资源回收
	 */
	protected function release($objRequest, $objResponse) { }

	/**
	 * 禁用显示
	 */
	public function setDisplay($flag = true) {
		$this -> displayDisabled = $flag;
	}
	/**
	 * 是否Header
	 */
	public function sendHeader($flag = true) {
		$this -> isHeader = $flag;
	}
	/*
	 * 是否生成文件
	*/
	public function createfile($flag = true, $filename, $dir = __PRODUCT_XML) {
		$this -> iscreatefile = $flag;
		$this -> filename = $filename;
		$this -> dir = $dir;
	}
	/***
	 * 受否编译模板
	 */
	public function setCompiler($flag = true) {
		$this -> compiler = $flag;
	}
	/**
	 * Controller层的调用入口函数,在scripts中调用
	 */
	public function execute($switch = NULL) {
		$startTime = getMicrotime();
		try {
			$objRequest = new HttpRequest();
			$objResponse = new HttpResponse();
			//指定switch
			if($switch != NULL) {
				$objRequest -> setSwitch($switch);
			}
			//入力检查
			$this -> check($objRequest, $objResponse);
			//执行方法
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
			//资源回收
			$this->release($objRequest, $objResponse);

		} catch (Exception $e) {
			try {
				//资源回收
				$this -> release($objRequest, $objResponse);

			} catch(Exception $e) {
				logError($e -> getMessage(), __MODEL_EXCEPTION);
			}
			//错误日志
			logError($e -> getMessage(), __MODEL_EXCEPTION);
			logError($e -> getTraceAsString(), __MODEL_EMPTY);
			//重定向到错误页面
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
	 * 调用View层输出
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