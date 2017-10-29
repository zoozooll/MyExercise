<?php
//声明路径是全局的，防止在函数里进行include调用的时候出现访问不到的现象
global $basesupport_root;
$basesupport_root=dirname(__file__);
//实现唯一的方式引入文件
global $api_includes;
if(!isset($api_includes))$api_includes=array();
//session开关
if(isset($session_power) && $session_power)
{
	if(!isset($api_includes['session']))
	{
		if(!isset($_SESSION))session_start();
		include($basesupport_root."/../configuration.php");
		include_once($basesupport_root."/../foundation/fsession.php");
		$api_includes['session']=true;
	}
}
//数据库配置及连接文件 
if(isset($iweb_power) && $iweb_power)
{
	include($basesupport_root."/includes.php");
}
//封装的get和post的方法
if(isset($getpost_power) && $getpost_power)
{
	if(!isset($api_includes['getandpost']))
	{
		include($basesupport_root."/../foundation/fgetandpost.php");
		$api_includes['getandpost']=true;
	}
	
}
//API代理函数
if(!isset($api_includes['api_proxy']))
{
	function Api_Proxy()
	{
		global $basesupport_root;
		
		$args = func_get_args();
		if(count($args)>=1)
		{
			$function=$args[0];
			unset($args[0]);
			if(is_string($function))
			{
				$fun=explode("_",$function);
				if(file_exists($basesupport_root."/$fun[0]/{$fun[0]}_{$fun[1]}.php"))
				{
					include_once($basesupport_root."/$fun[0]/{$fun[0]}_{$fun[1]}.php");
					return  call_user_func_array($function,$args);
				}
			}
		}
		return null;
	}
	$api_includes['api_proxy']=true;
}
?>