<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 495 $
 */
function smarty_function_category($params) {
	$conditions = array();
	$orderby = $table_name = $op = null;
	if (isset($params['type'])){
		global $_PB_CACHE;
		switch ($params['type']) {
			case "language":
				$languages = unserialize($_PB_CACHE['setting']['languages']);
				if (!empty($languages)) {
					foreach ($languages as $lang_key=>$lang_val) {
						$op.="<a style=\"float:right;\" href='".pb_getenv("PHP_SELF")."?app_lang=".$lang_key."'><img src='".$lang_val['img']."' alt='".$lang_val['title']."' /></a>";
					}
				}
				return $op;
				break;
			case "style":
				break;
			case "area":
				require(CACHE_PATH. "cache_area.php");
				foreach ($_PB_CACHE['area'][1] as $key=>$val) {
					$op.="<a href='product/price.php?do=search&areaid=".$key."'><span>".$val."</span></a>";
				}
				return $op;
				break;
			default:
				break;
		}
	}
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY display_order ASC,id ASC";
	}
	if (isset($_GET['type'])){
		if(in_array($_GET['type'], array("area", "industry", "product", "job", "brand"))) {
			$table_name = trim($_GET['type']);		
		}
	}elseif (isset($params['from'])) {
		$table_name = trim($params['from']);
	}else{
		return;
	}
	switch ($table_name) {
		case "product":
			break;
		case "job":
			break;
		case "brand":
			break;
		case "industry":
			if (class_exists("Industries")) {
				$obj = new Industries();
				$obj_controller = & Industry::getInstance();
			}else{
				uses("industry");
				$obj = new Industries();
				$obj_controller = & Industry::getInstance();
			}
			$tb_name = $obj->table_prefix."industries";
			break;
		case "area":
			if (class_exists("Areas")) {
				$obj = new Areas();
				$obj_controller = & Area::getInstance();
			}else{
				uses("area");
				$obj = new Areas();
				$obj_controller = & Area::getInstance();
			}
			$tb_name = $obj->table_prefix."areas";
			break;
		default:
			die(L("special_name_not_exists"));
			return;
			break;
	}
	$fields = "id,name,highlight,url";
	$op = '<ul>';
	//$result1 = $obj->dbstuff->CacheGetArray("SELECT $fields FROM {$tb_name} WHERE available=1 {$orderby}");
	switch ($params['depth']) {
		case 3:
			$result1 = $obj->dbstuff->GetArray("SELECT $fields FROM {$tb_name} WHERE level=1 AND available=1 {$orderby}");
			foreach ($result1 as $key1=>$val1){
				$result2 = array();
				$url = (!empty($val1['url']))?$val1['url']:$obj_controller->rewriteUrl($table_name, "special/".$table_name.".php", $val1['id'], $val1['name']);
				$op.='<li class="level-1"><h3><a href="'.$url.'">'.$val1['name'].'</a></h3><ul>';
				$result2 = $obj->dbstuff->GetArray("SELECT $fields FROM {$tb_name} WHERE level=2 AND parent_id=".$val1['id']." {$orderby}");
				foreach ($result2 as $key2=>$val2) {
					$result3 = array();
					$url = (!empty($val2['url']))?$val2['url']:$obj_controller->rewriteUrl($table_name, "special/".$table_name.".php", $val2['id'], $val2['name']);
					$op.='<li class="level-2"><h4>[<a href="'.$url.'">'.$val2['name'].'</a>]</h4>
				<ul class="clearfix">';
					$result3 = $obj->dbstuff->GetArray("SELECT $fields FROM {$tb_name} WHERE level=3 AND parent_id=".$val2['id']." {$orderby}");
					foreach ($result3 as $key3=>$val3) {
						$url = (!empty($val3['url']))?$val3['url']:$obj_controller->rewriteUrl($table_name, "special/".$table_name.".php", $val3['id'], $val3['name']);
						$op.='<li class="level-3" title="'.$val3['name'].'"><a href="'.$url.'">'.$val3['name'].'</a></li>';
					}
					$op.='</ul>';
					$op.='</li>';
				}
				$op.='</ul>';
				$op.='</li>';
			}
			break;
		case 2:
			$result1 = $obj->dbstuff->GetArray("SELECT $fields FROM {$tb_name} WHERE level=1 {$orderby}");
			foreach ($result1 as $key1=>$val1){
				$result2 = array();
				$url = (!empty($val1['url']))?$val1['url']:$obj_controller->rewriteUrl($table_name, "special/".$table_name.".php", $val1['id'], $val1['name']);
				$op.='<li class="level-1"><h3><a href="'.$url.'">'.$val1['name'].'</a></h3><ul>';
				$result2 = $obj->dbstuff->GetArray("SELECT $fields FROM {$tb_name} WHERE level=2 AND parent_id=".$val1['id']." {$orderby}");
				foreach ($result2 as $key2=>$val2) {
					$url = (!empty($val2['url']))?$val2['url']:$obj_controller->rewriteUrl($table_name, "special/".$table_name.".php", $val2['id'], $val2['name']);
					$op.='<li class="level-2"><h4>[<a href="'.$url.'">'.$val2['name'].'</a>]</h4>';
					$op.='</li>';
				}
				$op.='</ul>';
				$op.='</li>';
			}			
			break;
		case 1:
			$result1 = $obj->dbstuff->GetArray("SELECT $fields FROM {$tb_name} WHERE level=1 {$orderby}");
			foreach ($result1 as $key1=>$val1){
				$url = (!empty($val1['url']))?$val1['url']:$obj_controller->rewriteUrl($table_name, "special/".$table_name.".php", $val1['id'], $val1['name']);
				$op.='<li class="level-1"><h3><a href="'.$url.'">'.$val1['name'].'</a></h3>';
				$op.='</li>';
			}	
			break;
		default:
			break;
	}
	unset($result1, $result2, $result3);
	$op.= '</ul>';
	echo $op;
}
?>