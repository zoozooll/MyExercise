<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 186 $
 */
if (!class_exists("Multibytes")) {
	require(LIB_PATH. "multibyte.class.php");
}
class PbView extends PbObject
{
	var $admin_dir = 'pb-admin';
	var $office_dir = 'office-room';
	var $homepage_file_name = "index.php";
	var $titles = array();
	var $pageTitle = null;
	var $url_container = null;
	var $webroot;
	var $here = null;
	var $position = array();
	var $addParams;
	var $metaKeyword;
	var $metaDescription;
	var $caching = false;

	function PbView(){
		global $_PB_CACHE;
		if (!empty($_REQUEST['page'])) {
			$this->addParams = "&page=".intval($_REQUEST['page']);
		}
		$this->setPosition($_PB_CACHE['setting']['site_name'], URL."index.php");
	}
	
	function setMetaDescription($meta_description)
	{
		$this->metaDescription = mb_substr(pb_strip_spaces(strip_tags($meta_description)), 0, 100);
	}
	
	function setMetaKeyword($meta_keyword)
	{
		$this->metaKeyword = str_replace(array(), ",", $meta_keyword);
	}	

    function setTitle($title, $image = 0)
    {
    	$this->titles[] = $title.($image?"[".L('have_picture', 'tpl')."]":null);
    }

    function getTitle($seperate = ' - ')
    {
        $pageTitle = null;
    	krsort($this->titles);
    	$pageTitle = implode($seperate, $this->titles);
    	if (strpos($pageTitle, $seperate) == 0) {
    		;
    	}
    	$this->pageTitle = $pageTitle;
    	return $pageTitle;
    }
    
	function bread_compare($a, $b){
	    if ($a['displayorder'] == $b['displayorder']) return 0;
	    return ($a['displayorder'] < $b['displayorder']) ? -1 : 1;
	}
	
	function queryString($q, $extra = array(), $escape = false) {
		if (empty($q) && empty($extra)) {
			return null;
		}
		$join = '&';
		if ($escape === true) {
			$join = '&amp;';
		}
		$out = '';
	
		if (is_array($q)) {
			$q = array_merge($extra, $q);
		} else {
			$out = $q;
			$q = $extra;
		}
		$out .= http_build_query($q, null, $join);
		if (isset($out[0]) && $out[0] != '?') {
			$out = '?' . $out;
		}
		return $out;
	}

    function setPosition($title, $url = null, $displayorder = 0, $additonalParams = array())
    {
        $this->position[] = array('displayorder'=>$displayorder, 'title'=>$title, 'url'=>$url, 'params'=>$additonalParams);
    }

    function getPosition($seperate = ' &raquo; ', $show_last = false)
    {
    	$position = array();
    	$current_position = null;
    	$positions = $this->position;
        if (!empty($positions)) {
	        foreach ($positions as $key=>$val){
	            if(!empty($val['url'])) {
	                if(isset($val['params'])) $position[] = "<a href='".$val['url'].$this->queryString($val['params'])."' target='_self' title='".$val['title']."'>".$val['title']."</a>";
	                else $position[] = "<a href='".$val['url']."' target='_self' title='".$val['title']."'>".$val['title']."</a>";
	            }else {
	                $position[] = $val['title'];
	            }
	        }
	        $heres = implode($seperate, $position);
	        $current_position = "<em>".L("your_current_position", "tpl")."</em>".$heres;
	        $this->here = $current_position;
	        if ($show_last) {
	        	$this->here.=$seperate;
	        }
        }
        if (empty($this->metaDescription)) {
        	$this->metaDescription = mb_substr(strip_tags(end($position)), 0, 100);
        }
        return $this->here;
    }
	
	function Start()
	{
		global $topleveldomain_support, $pdb, $tb_prefix;
		if ($topleveldomain_support) {
			$host = isset($_SERVER['HTTP_X_FORWARDED_HOST']) ? $_SERVER['HTTP_X_FORWARDED_HOST'] : (isset($_SERVER['HTTP_HOST']) ? $_SERVER['HTTP_HOST'] : '');
			$result = $pdb->GetRow("SELECT id,cache_spacename FROM {$tb_prefix}companies WHERE topleveldomain='".$host."' AND status='1'");
			if (!empty($result)) {
				pheader("HTTP/1.1 301 Moved Permanently");
				pheader("location:".URL."space.php?id=".$result['id']);
				exit();
			}
		}
		formhash();
	}
	
	function redirect($url, $type=301)
	{
		if ($type == 301) header("HTTP/1.1 301 Moved Permanently");
		header("Location: $url");
		echo 'This page has moved to <a href="'.$url.'">'.$url.'</a>';
		exit();
	}
}
?>