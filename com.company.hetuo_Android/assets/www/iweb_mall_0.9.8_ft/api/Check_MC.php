<?php
global $mc;
$mc=null;
if($baseLibsPath=='iweb_si_lib/')
{
	$mc=new memcached($options);
}
function  updateCache($key_mt)
{
	global $mc;
	if($mc) updateCacheEvent($mc,$key_mt);
}
function model_cache($key,$key_mt,&$dbo,&$sql)
{
	global $mc;
	global $cache_update_delay_time;
	if(!is_null($mc))
	{
		//根据延时设置判断所缓存内容的更新状态
		//,,$cache_update_delay_time";
		if(updateCacheStatus($mc,$key_mt,$cache_update_delay_time)){
			$mc->delete($key);
		}
		//得到缓存数据
		//function get_plugin_rs(&$dbo,$sql){
		//	return $dbo->getRs($sql);
		//} 
		$plugin_rs='';
		if($mc->get($key)){	
			$plugin_rs=$mc->get($key);
		}else{
			$plugin_rs=$dbo->getRs($sql);//get_plugin_rs($dbo,$sql);
			$mc->set($key,$plugin_rs);
		}
		return $plugin_rs;
	}
}
?>