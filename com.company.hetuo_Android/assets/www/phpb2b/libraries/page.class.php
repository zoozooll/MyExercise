<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
class Pages extends PbController {
	var $total_record;
	var $total_page;
	var $firstcount;
	var $displaypg = 9;
	var $current_page;
	var $pagenav;
	var $pagetpl_dir = '';
	var $pagetpl = "element.pages";
	var $_url;
	var $nextpage_link = "javascript:;";
	var $previouspage_link = "javascript:;";
	var $page_option = array(10,20,30);
	var $is_rewrite = false;
	
	function Pages() {
		$this->_url = pb_getenv('PHP_SELF');
	}
	
	function setPagenav($total_record)
	{
		global $smarty;
		$params = $pagenav = null;
        if (isset($_REQUEST['page'])) {
        	if (!intval($_REQUEST['page'])) {
        		$page = 1;
        	}else {
        		$page = $_REQUEST['page'];
        	}
        }else{
        	$page = 1;
        }
		$this->total_record = $total_record;
		$this->current_page = $page;
		$lastpg = ceil($this->total_record / $this->displaypg);
		$this->total_page = $lastpg;
		$page = min($lastpg, $page);
        $firstcount = ($page-1) * $this->displaypg;
		if($firstcount<0) {
			$firstcount = 0;
		}
		$this->firstcount = $firstcount;
		if($lastpg<=1) {
			$this->pagenav = null;
			return;
		}
		if($page>$lastpg) $page = $lastpg;
		$get_params = array_filter($_GET);
		$params = http_build_query($get_params);
		$params = ereg_replace("(^|&)page=$page", "", $params);
		if (!empty($params)) {
			$params = '?'.$params."&";
		}else{
			$params = '?';
		}
		if($page>1){
			$prev_begin = ($page-5)<=0?1:($page-5);
			$prev_end = ($page-1)<=0?1:($page-1);
			$prevs = range($prev_begin, $prev_end);
			$previous_page = $page-1;
			$this->previouspage_link = $this->rewriteList($previous_page, $params);
			if ($prev_begin>1) {
				$pagenav.="<a href='".$this->rewriteList(1,$params)."' title='".L('first_page', 'tpl')."'>1</a>... ";
			}
			foreach ($prevs as $val) {
				$pagenav.="<a href='".$this->rewriteList($val, $params)."'>$val</a>";
			}
		}
		$pagenav.="<span class='current'>{$page}</span>";
		if($page<$lastpg){
			$next_begin = ($page+1)>$lastpg?$lastpg:($page+1);
			$next_end = ($page+5)>$lastpg?$lastpg:($page+5);
			$nexts = range($next_begin, $next_end);
			$next_page = $page+1;
			$this->nextpage_link = $this->rewriteList($next_page, $params);
			foreach ($nexts as $val) {
				$pagenav.="<a href='".$this->rewriteList($val, $params)."'>{$val}</a>";
			}
			if($next_end<$lastpg) {
				$pagenav.="... <a href='".$this->rewriteList($lastpg, $params)."' title='".L('last_page', 'tpl')."'>{$lastpg}</a>";
			}
		}
		$tpl_file = $this->pagetpl.$smarty->tpl_ext;
		$smarty->assign("pages", $pagenav);
		if (!empty($this->pagetpl_dir)) {
			$tpl_file = $this->pagetpl_dir.DS.$this->pagetpl.$smarty->tpl_ext;
		}
		if (!$smarty->template_exists($tpl_file)) {
			$tpl_file = 'default'.DS.$this->pagetpl.$smarty->tpl_ext;
		}
		$this->pagenav = $smarty->fetch($tpl_file);
	}
	
	function rewriteList($page = 1, $params = null)
	{
		if ($this->is_rewrite) {
			$url = $this->_url."list-".$page;
			if (!empty($params)) {
				;
			}
			$url.=".html";
		}else{
			if (strpos($this->_url, "?")  === false) {
				if (!empty($params)) {
					$url = $this->_url.$params."page={$page}";
				}else{
					$url = $this->_url."?page={$page}";
				}
			}else{
				$url = rtrim($this->_url, "&")."&page={$page}";
			}
			
		}
		return $url;
	}
	
	function getPagenav()
	{
		return $this->pagenav;
	}
}
?>