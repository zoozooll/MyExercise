<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision$
 */
class Payments extends PbObject
{
    var $names;
    var $config;
    var $payment_dir;
    var $payment_path;
    var $db;
    var $table_prefix;

    function __construct()
    {
        $this->Payments();
    }

    function Payments()
    {
    	global $pdb, $tb_prefix;
    	$this->db =& $pdb;
    	$this->table_prefix = $tb_prefix;
		$this->payment_dir = 'payments'; 
		$this->payment_path = PHPB2B_ROOT. 'api'.DS.'payments'.DS; 
    }

	function install($entry)
	{
		$tpldir = realpath($this->payment_path.$entry);
		if (is_dir($tpldir)) {
			$this->params['data']['name'] = $entry;
			$this->params['data']['title'] = strtoupper($entry);
			$this->params['data']['available'] = 1;
			$this->params['data']['created'] = $this->params['data']['modified'] = $_SERVER['REQUEST_TIME'];
			$this->save($this->params['data']);
		}
	}
	
	function uninstall($id)
	{
		$sql = "DELETE FROM {$this->table_prefix}payments WHERE id=".$id;
		return $this->db->Execute($sql);
	}
	
	function getPayments(){
		$installed = $this->getInstalled();
		$not_installed = $this->getUninstalled();
		$all = array_merge($installed, $not_installed);
		return $all;
	}
	
	function getInstalled()
	{
		$sql = "SELECT * FROM {$this->table_prefix}payments WHERE available=1";
		$result = $this->db->GetArray($sql);
		return $result;
	}
	
	function getUninstalled(){
		$uninstalled = $temp = array();
		$installed = $this->getInstalled();
		foreach($installed as $key=>$val){
			$temp[] = $val['name'];
		}
		$template_dir = dir($this->payment_path);
		while($entry = $template_dir->read())  {
			$tpldir = realpath($this->payment_path.$entry);
			if((!in_array($entry, array('.', '..', '.svn'))) && (!in_array($entry, $temp)) && is_dir($tpldir)) {
				require($this->payment_path.$entry.DS."info.inc.php");
				$uninstalled[] = array(
				'name' => $entry,
				'title' => $cfg['title'],
				'description' => $cfg['description'],
				'available' => 0,
				);
			}
		}
		return $uninstalled;
	}
	
	function writeConfig($file_name, $config)
	{
		if(empty($config) || !is_array($config)) return false;
		$pattern = $replacement = array();
		foreach($config as $k=>$v)
		{
			$pattern[$k] = "/define\(\s*['\"]".strtoupper($k)."['\"]\s*,\s*([']?)[^']*([']?)\s*\)/is";
	        $replacement[$k] = "define('".$k."', \${1}".$v."\${2})";
		}
		$str = file_get_contents($this->payment_path.$file_name);
		$str = preg_replace($pattern, $replacement, $str);
		return file_put_contents($this->payment_path.$file_name, $str);
	}
}
?>