
<?php

/**
* ���ƣ����ݿ⹤����
* ���ߣ�shadow
* ���ڣ�2007-08-27
* ���ܣ����Mysql���з�װ���ṩ�����ĺ����������ݲ����������
*/
class MysqlDB{

/**
* �����ݿ����ӵĹ��� �κ��й����ݿ������Ҫ�ȵ��ô˺����ɹ�
*
* @return trueΪ���ӳɹ� falseΪ����ʧ��
*/
  private static $link=null;
  function do_connect($databaseHost,$databaseUser,$databasePassword,$databaseName)
  {
	  self::$link = mysql_connect($databaseHost,$databaseUser,$databasePassword);
	  //mysql_query('SET NAMES UTF8');
	  if (!mysql_select_db($databaseName))
	  {
	  WriteLog("�������ݿ�ʧ�ܡ�", ERROR_LOG);   
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
  * ��֤���ݰ�ȫ����ֹSQLע��
  *
  * @param�ַ��� $value
  * @return ����ֵ�ַ�����''
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
  // �����ַ����鴮
  /**
  * ��֤���ݰ�ȫ����ֹSQLע��
  *
  * @param�ַ��� $value
  * @return ����ֵ�ַ�������''
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
  * �ַ�����ѯ
  *
  * @param��ѯ�ַ��� $sqlstr
  * @return ���ַ�����ʽ���ز�ѯ�Ľ��
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
	  WriteLog("���ݿ����$tmp����䣺$sqlstr", ERROR_LOG);
	 }
  
  }
  /**
  * �������������ַ��ָ���ַ��������
  *
  * @param��ѯ�ַ��� $sqlstr
  * @param�зָ��ַ� $rowflag
  * @param�ֶηָ��ַ� $dataflag
  * @return �ַ���
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
	  WriteLog("���ݿ����$tmp����䣺$sqlstr", ERROR_LOG);
	 }
  
  }
  
  
  /**
  * �������鷵�ز�ѯ���
  *
  * @param��ѯ�ַ��� $sqlstr
  * @return �������͵Ľ����
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
	  echo ("���ݿ����$tmp����䣺$sqlstr");
	 }
  
  
  }
  
  /**
  * ֻȡ����������ܺ�
  *
  * @param��ѯ�ַ��� $sqlstr
  * @return �������͵���Ӧ������
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
  * ֻȡ������ֶ��ܺ�
  *
  * @param ��ѯ�ַ��� $sqlstr
  * @return �������͵��ֶ�����
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
  * ����һ����¼
  *
  * @param �����¼��SQL��� $sqlstr
  * @return ����ֵ�ǲ�����������ɵ�ID���
  */
  function insertrow($sqlstr)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  mysql_query($sqlstr);
  $myout = mysql_insert_id();
	 
	  if ($myout===-1 && defined('_DEBUG_'))
	  {
		 WriteLog("��ѯ������".$sqlstr."ʱ�䣺".date("YmdHms") , ERROR_LOG);
	  return $myout;       
	  }
  return $myout;
  }
  
  /**
  * ɾ��һ����¼
  *
  * @param �����¼��SQL��� $sqlstr
  * @return ����ֵ����Ӱ�������
  */
  function del_modifiedrow($sqlstr)
  {
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  mysql_query($sqlstr);
  $myout = mysql_affected_rows();
	 
	  if ($myout===-1 && defined('_DEBUG_'))
	  {
		 WriteLog("��ѯ������".$sqlstr."ʱ�䣺".date("YmdHms") , ERROR_LOG);
	  return $myout;       
	  }
  return $myout;
  }
  
  /**
  * ��ȡ���ݿ����б������
  *
  * @return ����ֵ������
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
  * ���ָ�����ݿ�ı������
  *
  * @param ���ݱ��� $tables
  * @return �����ֶ����ַ���
  */
  function getPrimaryField($tables){
  mysql_select_db(DATABASE_NAME);
  mysql_query('SET NAMES GB2312');
  $result = mysql_query("select * from {$tables}");   
	
  for($i = 0;$i < mysql_num_fields($result);$i++)
  {
		   $meta = mysql_fetch_field($result);
	   if (!$meta) {
	  
		   return "û���ֶ���Ϣ\n";
	   }
	  if($meta->primary_key === 1)
	  {
	  $tmp =$meta->name;
	 
	  return $tmp;
	  }  
  }
  
  }
  
  /**
  * �������ݱ�ļܹ���Ϣ
  *
  * @param Ҫ��ѯ�ı��� $tablename
  * @return ������ʾ������Ϣ
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
  * ֻȡ����������ܺ�
  *
  * @param��ѯ�ַ��� $sqlstr
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
  * �ر����ݿ�����
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