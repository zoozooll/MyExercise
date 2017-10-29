<?php
class Mysql
{
	private $link;
	function connect($hostname,$username,$password,$database,$chartset)
	{
		$this->$link= @mysql_connect($hostname,$username,$password) or die(mysql_errno());
		@mysql_select_db($database) or die(mysql_error());
		if(mysql_get_server_info()>="4.1")
		{
			@mysql_query("set names GB2312");	
		}
	}
	
	function query($link)
	{
		return @mysql_query($link) or die (mysql_error());
	}
	
	static function close()
	{
		@mysql_close($this->$link) or die(mysql_error());
	}
}
?>