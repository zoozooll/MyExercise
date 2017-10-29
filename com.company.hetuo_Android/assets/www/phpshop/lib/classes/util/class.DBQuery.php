<?php
/*
	class.BBQuery.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class SQLException extends Exception { }

class DBQuery {
	
	private static $conn;
	private static $DB;
	
    public function __construct() {
		$this -> connect();
    }
    private function connect() {
       	if(self::$conn == NULL) {
			$startTime = getMicrotime();
			$this -> CConnect(DB_HOST, DB_USER, DB_PASSWORD);
			logDebug("connect use time: " . (getMicrotime() - $startTime));
		}
		if(self::$conn == false) {
			throw new SQLException("can't connect database, please check setting.");	
		}
		if(mysql_select_db(DB_NAME, self::$conn) == false) {
			throw new SQLException("can't select db, please check setting.");	
		}
        if (0 == strcmp(strtoupper(__CHARSET), strtoupper("UTF-8"))) {
			$this->_execute("SET NAMES UTF8;");
        	/*$this->_execute("SET CHARACTER_SET_CLIENT = utf8, " .
        					"CHARACTER_SET_CONNECTION = utf8, " .
        					"CHARACTER_SET_DATABASE   = utf8, " .
        					"CHARACTER_SET_RESULTS    = utf8, " .
        					"CHARACTER_SET_SERVER     = utf8, " .
        					"COLLATION_CONNECTION     = utf8_general_ci, " .
        					"COLLATION_DATABASE       = utf8_general_ci, " .
        					"COLLATION_SERVER         = utf8_general_ci ");*/
        }
	}
	
	public static function DB() {
		if(is_object(self::$DB)) { //and is_object(self::$DB)) {
			return self::$DB;
		}
		return self::$DB = new DBQuery;
	}

	/*将数组转换到以','分隔的字符串*/
	public static function arrSqlToSString($arr) {
		if(!is_array($arr)) {
			return $arr;
		}
		$str = $arr[0];
		$num = count($arr);
		for($loop=1; $loop<$num; $loop++) {
			$str .= ',' . $arr[$loop];
		}
		return $str;
	}

    public function getOne($sql) {
		if($arrOne = mysql_fetch_row($this-> _execute($sql))) {
			return $arrOne[0];
		}
		return NULL;
    }
    
    public function getList($sql) {
		$rs = NULL;
    	if($list = $this-> _execute($sql)) {
			while($row = mysql_fetch_array($list, MYSQL_ASSOC)) {
			   $rs[] = $row;
			}
		}
		return $rs;
    }
    
    public function getRow($sql) {
    	if($row = $this-> _execute($sql)) {
			return mysql_fetch_array($row, MYSQL_ASSOC);
		}
		return NULL;
    }
    
    public function getInsertID() {
    	if($id = mysql_insert_id()) {
			return $id;
		}
		return NULL;
    }
    
    public function execute($sql) {
    	return $this->_execute($sql);
    }//modify delete insert update
        
	public function mysqlVer() {
		return mysql_get_server_info();
	}

	public function close() {
		return mysql_close();
	}

    private function _execute($sql) {
		if (false == ($result = mysql_query($sql, self::$conn))) {
			throw new SQLException("error: SQL=$sql");
		}
		return $result;
	}
	
	private function CConnect($host, $username, $password) {
		if(self::$conn == NULL) {
			if((self::$conn = mysql_connect($host, $username, $password)) == false) {
				throw new SQLException("can't link to db host, please check setting.");	
			}
		}
		return self::$conn;
	} 

	private function PConnect($host, $username, $password) {
		if(self::$conn == NULL) {
			if((self::$conn = mysql_pconnect($host, $username, $password)) == false) {
				throw new SQLException("can't link to db host, please check setting.");	
			}
		}
		return self::$conn;
	} 
 }
?>