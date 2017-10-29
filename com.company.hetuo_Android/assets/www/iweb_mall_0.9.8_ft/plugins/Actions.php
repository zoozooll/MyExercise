<?php
//实现对象的自动加载
$plugin="";
//读取Action的配制文件
class Actions
{
	private static $obj;
	private static $action_info;
	private function __construct(){}
	private function __clone(){}
	public static  function getInstance($path)
	{
		global $plugin;
		$plugin=$path;
		if(!self::$obj instanceof self)
		{
			self::$obj=new self();
			$dom = new DOMDocument();
			$dom->load(dirname(__file__)."/$path/actions.xml");
			if(@$dom->validate())
			{
				$packages=$dom->getElementsByTagName("package");
				foreach($packages as $package)
				{
					$actions=$package->getElementsByTagName('action');
					$action_tem=array();
					foreach($actions as $action)
					{
						$action_tem['file']=$action->getAttributeNode('file')->value;
						unset($action_tem['class']);
						if($action->hasAttribute('class'))$action_tem['class']=$action->getAttributeNode('class')->value;
						$action_tem['method']=$action->getAttributeNode('method')->value;
						
						$returns=$action->getElementsByTagName('return');
						$return_tem=array();  
						foreach($returns as $return)
						{
							$return_tem[$return->getAttributeNode('name')->value]=$return->nodeValue;
						}
						$action_tem['returns']=$return_tem;
						self::$action_info[$package->getAttributeNode('name')->value.'_'.$action->getAttributeNode('name')->value]=$action_tem;
					}
					//	var_dump( self::$action_info);
				}
			}
			else
			{
				echo "此XML文件不符合规范！";
				exit;
			}
		}
		return self::$obj;
	}
	public function doaction($name)
	{
		if(isset(self::$action_info[$name]))
		{
		try
		{
			global $plugin;
			//var_dump(self::$action_info);
			if(file_exists(dirname(__file__)."/$plugin/".self::$action_info[$name]['file']))
			{
				include_once(dirname(__file__)."/$plugin/".self::$action_info[$name]['file']);
			}
			
			if(isset(self::$action_info[$name]['class']))
			{
				$classname=self::$action_info[$name]['class'];
				$class= new ReflectionClass($classname);
				if ( $class -> isInstantiable ())
				{
					$methodname=self::$action_info[$name]['method'];
					$method = $class -> newInstance ();
					$path=$method->$methodname();
					//global $plugin;
					//header("Location: ".$plugin."/".self::$action_info[$name]['returns'][$path]);
					require(dirname(__file__)."/$plugin/".self::$action_info[$name]['returns'][$path]);
				} 
			}
			else
			{
				$method=self::$action_info[$name]['method'];
				$path=$method();
				global $plugin;
				//header("Location: ".$plugin."/".self::$action_info[$name]['returns'][$path]);
				require(dirname(__file__)."/$plugin/".self::$action_info[$name]['returns'][$path]);
			}

		}catch(Exception $e)
		{
			echo "在反射解释$name 出现错误\n";
		}
	}else
	{
	  header("Location: help/noaction.html");
	}
	}
}
?>