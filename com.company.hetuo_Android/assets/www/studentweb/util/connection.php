
<?php

/**
* 名称：数据库工具类
* 作者：shadow
* 日期：2007-08-27
* 功能：针对Mysql进行封装，提供基本的函数进行数据层物理操作。
*/
class MysqlDB{

/**
* 做数据库连接的工作 任何有关数据库操作都要先调用此函数成功
*
* @return true为连接成功 false为连接失败
*/
  private static $link=null;
  function do_connect($databaseHost,$databaseUser,$databasePassword,$databaseName)
  {
	  self::$link = mysql_connect($databaseHost,$databaseUser,$databasePassword);
	  //mysql_query('SET NAMES UTF8');
	  if (!mysql_select_db($databaseName))
	  {
	  WriteLog("连接数据库失败。", ERROR_LOG);   
	  return false;
	  }
	  mysql_query('SET NAMES GBK');
	  return true;
  }
  
  function __construct()
  {
  if(!(isset(self::$link)) || self::$link == null)
  {
	 $this->do_connect();
  }
  }
  
  function __destruct()
  {
  $this->do_close();
  }
  
  /**
  * 保证数据安全，防止SQL注入
  *
  * @param字符串 $value
  * @return 返回值字符串带''
  */
  function quote_smart($value)
  {
	 // Stripslashes
	 if (get_magic_quotes_gpc())
	 {
		 $value = stripslashes($value);
	 }
	 // Quote if not integer
	 if (!is_numeric($value) || $value[0] == '0')
	 {
		 $value = "'" . mysql_real_escape_string($value) . "'";
	 }
	 return $value;
  }
  
  //
  //
  // 方便字符串组串
  /**
  * 保证数据安全，防止SQL注入
  *
  * @param字符串 $value
  * @return 返回值字符串不带''
  */
  function quote_smart_no_quotation($value)
  {
	 // Stripslashes
	 if (get_magic_quotes_gpc())
	 {
		 $value = stripslashes($value);
	 }
	 // Quote if not integer
	 if (!is_numeric($value) || $value[0] == '0')
	 {
		 $value = mysql_real_escape_string($value);
	 }
	 return $value;
  }
  
  
  /**
  * 字符串查询
  *
  * @param查询字符串 $sqlstr
  * @return 以字符串形式返回查询的结果
  */
  function getstring($sqlstr)
  {
  mysql_select_db(DATABASE_NAME);
	 mysql_query('SET NAMES GB2312');
	 $result = mysql_query($sqlstr);
	
	 $outstr = '';
	 if(isset($result) && $result!=null)
	 for($i=0;$i<mysql_num_rows($result);$i++){
	  if(($row_data = mysql_fetch_row($result)) != NULL)
	  {
	   if(isset($row_data) && $row_data != null && count($row_data) > 0)
	   foreach($row_data as $datafeilds)
	   {
		return $datafeilds;
	   }
	  
	  }
	 }
	
	 if(_DEBUG_)
	 {
	  $tmp = mysql_error();
	  WriteLog("数据库错误：$tmp。语句：$sqlstr", ERROR_LOG);
	 }
  
  }
  /**
  * 返回利用特殊字符分割的字符串结果集
  *
  * @param查询字符串 $sqlstr
  * @param行分隔字符 $rowflag
  * @param字段分割字符 $dataflag
  * @return 字符串
  */
  function getstringex($sqlstr,$rowflag,$dataflag)
  {
	 mysql_select_db(DATABASE_NAME);
	 mysql_query('SET NAMES GB2312');
	 $result = mysql_query($sqlstr);
	
	 $outstr = '';
	 if(isset($result) && $result!=null)
	 for($i=0;$i<mysql_num_rows($result);$i++){
	  if(($row_data = mysql_fetch_row($result)) != NULL)
	  {
	   if(isset($row_data) && $row_data != null && count($row_data) > 0)
	   foreach($row_data as $datafeilds)
	   {
		$outstr .= $datafeilds.$dataflag;
	   }
	   $outstr .= $rowflag;
		
	  
	  }
	 }
	 return $outstr;
	 if(_DEBUG_)
	 {
	  $tmp = mysql_error();
	  WriteLog("数据库错误：$tmp。语句：$sqlstr", ERROR_LOG);
	 }
  
  }
  
  
  /**
  * 按照数组返回查询结果
  *
  * @param查询字符串 $sqlstr
  * @return 数组类型的结果集
  */
  function getarray($sqlstr)
  {
  
	 mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');  
	 $result = mysql_query($sqlstr);
	 if(isset($result) && $result!=null)
	 {
	  for($i=0;$i<mysql_num_rows($result);$i++){
	   if(($row_data = mysql_fetch_row($result)) != NULL)
	   {   
		$outstr[$i] = $row_data;
		//
	   }
	  }
	  return $outstr;
	 }
	
	 if(_DEBUG_)
	 {
	  $tmp = mysql_error();
	  echo ("数据库错误：$tmp。语句：$sqlstr");
	 }
  
  
  }
  
  /**
  * 只取结果集行数总合
  *
  * @param查询字符串 $sqlstr
  * @return 整数类型的响应行总数
  */
  function getrowcount($sqlstr)
  {
	 mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
	 $result = mysql_query($sqlstr);
	 $outstr = mysql_num_rows($result);
	  
  
  return $outstr;
  }
  
  
  /**
  * 只取结果集字段总合
  *
  * @param 查询字符串 $sqlstr
  * @return 整数类型的字段总数
  */
  function getfeildcount($sqlstr)
  {
	 mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
	 $result = mysql_query($sqlstr);
	 $outstr = mysql_num_fields($result);
	 
  
  return $outstr;
  }
  
  /**
  * 插入一条记录
  *
  * @param 插入记录的SQL语句 $sqlstr
  * @return 返回值是插入操作所生成的ID编号
  */
  function insertrow($sqlstr)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  mysql_query($sqlstr);
  $myout = mysql_insert_id();
	 
	  if ($myout===-1 && defined('_DEBUG_'))
	  {
		 WriteLog("查询语句错误。".$sqlstr."时间：".date("YmdHms") , ERROR_LOG);
	  return $myout;       
	  }
  return $myout;
  }
  
  /**
  * 删除一条记录
  *
  * @param 插入记录的SQL语句 $sqlstr
  * @return 返回值是受影响的行数
  */
  function del_modifiedrow($sqlstr)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  mysql_query($sqlstr);
  $myout = mysql_affected_rows();
	 
	  if ($myout===-1 && defined('_DEBUG_'))
	  {
		 WriteLog("查询语句错误。".$sqlstr."时间：".date("YmdHms") , ERROR_LOG);
	  return $myout;       
	  }
  return $myout;
  }
  
  /**
  * 获取数据库所有表的名称
  *
  * @return 返回值是数组
  */
  function gettablesname(){
  $sqlstr = "show tables;";
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  $result = mysql_query($sqlstr);   
	 $i = 0;
	 $alltable = array();
	 while ($row = mysql_fetch_object($result)) {
	 
		foreach($row as $datafeilds)
	   {
		$alltable[$i] = $datafeilds;
		$i++;
	   }  
	 }
	
  return $alltable;
  }
  
  /**
  * 获得指定数据库的表的主键
  *
  * @param 数据表名 $tables
  * @return 主键字段名字符串
  */
  function getPrimaryField($tables){
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  $result = mysql_query("select * from {$tables}");   
	
  for($i = 0;$i < mysql_num_fields($result);$i++)
  {
		   $meta = mysql_fetch_field($result);
	   if (!$meta) {
	  
		   return "没有字段信息\n";
	   }
	  if($meta->primary_key === 1)
	  {
	  $tmp =$meta->name;
	 
	  return $tmp;
	  }  
  }
  
  }
  
  /**
  * 返回数据表的架构信息
  *
  * @param 要查询的表名 $tablename
  * @return 数组显示所有信息
  */
  function gettableinfo($tablename)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  $sqlstr = "select * from $tablename";
	
	 $result = mysql_query($sqlstr);
	  
  $feilds = array();
  
  for($o = 0;$o < mysql_num_fields($result);$o++)
  {  
	 $feild = mysql_fetch_field($result);
	 $feilds[$o] = $feild->name;
  }
  
  return $feilds;
  }
  function gettableinfoex($tablename)
  {
	 mysql_select_db(DATABASE_NAME);
	 mysql_query('SET NAMES GB2312');
	 $sqlstr = "describe {$tablename};";
	
	 $result = mysql_query($sqlstr);      
	 $i = 0;
	 $alltable = array();
	 while ($row = mysql_fetch_object($result)) {
	  $j = 0;
		  foreach($row as $datafeilds)
	   {
	   
	   
		 $alltable[$i][$j] = $datafeilds;
		$j++;
	   
	   }
	   $i++;
	  
	 }
	
	
	 return $alltable;
  }
  
  function getcomment($tablename)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  $sqlstr = <<<ETC
  
  SELECT COLUMN_NAME,COLUMN_COMMENT,DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '{$tablename}';
  ETC;
  //show create table & SELECT COLUMN_NAME,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '{$tablename}'; ";
	 $result = mysql_query($sqlstr);   
	 $i = 0;
	 $alltable = array();
	 while ($row = mysql_fetch_object($result)) {
	  $j = 0;
		foreach($row as $datafeilds)
	   {
	   
	   
		 $alltable[$i][$j] = $datafeilds;
		$j++;
	   
	   }
	   $i++;  
	 }
	
	 return $alltable;
  }
  function getcommentEX($tablename,$feildname)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  $sqlstr = <<<ETC
  
  SELECT COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '{$tablename}' && column_name = '{$feildname}';
  ETC;
  //show create table & SELECT COLUMN_NAME,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '{$tablename}'; ";
  
	 $result = mysql_query($sqlstr);   
  
	 $alltable = '';
	 while ($row = mysql_fetch_object($result)) {
	  foreach($row as $datafeilds)
	   {
		 $alltable = $datafeilds;     
	   }
	 }
	
	 return $alltable;
  }
  /**
  * 只取结果集行数总合
  *
  * @param查询字符串 $sqlstr
  * @return $result
  */
  
  function getresult($sqlstr)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  $result = mysql_query($sqlstr);
  
  return $result;
  }
  
  
  /**
  * 关闭数据库连接
  *
  */
  function do_close()
  {
	  if(self::$link!=null)
	  {
		  @mysql_close(self::$link);
	  }
  }
  }

?>