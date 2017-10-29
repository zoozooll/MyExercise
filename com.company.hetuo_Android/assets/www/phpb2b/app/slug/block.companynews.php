<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 657 $
 */
function smarty_block_companynews($params, $content, &$smarty) {
   if ($content === null) return;
   if (class_exists("Companynews")) {
		$companynews = new Companynewses();
		$companynews_controller = new Companynews();
	}else{
	    uses("companynews");
	    $companynews = new Companynewses();
		$companynews_controller = new Companynews();
	}
   if (class_exists("Space")) {
		$space_controller = new Space();
	}else{
	    uses("space");
		$space_controller = new Space();
	}
	$conditions = array();
	$orderby = array();
	$conditions[] = "cn.status=1";
	if(isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'image':
					$conditions[] = "cn.picture!=''";
					break;
				case 'hot':
					$orderby[] = 'clicked DESC';
				default:
					break;
			}
		}
	}
	if (isset($params['tag'])) {
		$conditions[] = "cn.title like '%".$params['tag']."%'";
	}
	if (!empty($params['companyid'])) {
		$conditions[] = "cn.company_id='".$params['company_id']."'";
	}
	if (isset($params['orderby'])) {
		$orderby[] = trim($params['orderby']);
	}else{
		$orderby[] = "id DESC ";
	}
		$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$companynews->setLimitOffset($row, $col);
	$sql = "SELECT cn.id,cn.company_id,cn.title as fulltitle,cn.title,cn.picture,cn.created,cn.content as fullcontent,cn.content,m.username AS userid FROM {$companynews->table_prefix}companynewses cn LEFT JOIN {$companynews->table_prefix}members m ON m.id=cn.member_id ".$companynews->getCondition().$companynews->getOrderby().$companynews->getLimitOffset();
	$result = $companynews->dbstuff->GetArray($sql);
	$return = $link_title = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$style = null;
			$dt = @getdate($result[$i]['created']);
			$space_controller->setBaseUrlByUserId($result[$i]['userid'], "news");
			$url = $space_controller->rewriteDetail("news", $result[$i]['id']);
			$result[$i]['title'] = strip_tags($result[$i]['title']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    		
	    	}	    	
	    	$result[$i]['content'] = strip_tags($result[$i]['content']);
	    	if (isset($params['infolen'])) {
	    		$result[$i]['content'] = mb_substr($result[$i]['content'], 0, $params['infolen']);
	    		
	    	}
	    	if (isset($params['magic']))  {
	    		if ($i==0){
	    			if(!empty($result[$i]['picture'])) {
	    				$style = " style=\"height:70px; background:url(".URL."attachment/".$result[$i]['picture'].".small.jpg".") no-repeat; padding:0 0 0 90px;overflow:hidden; width:120px;\"";
	    				$h3_style = " style=\"padding:0 0 0 5px;\"";
	    			}else{
	    				$style = " style=\"height:70px; background:url(".URL.pb_get_attachmenturl('', '', 'small').") no-repeat; padding:0 0 0 90px;\"";
	    			}
	    			$link_title = "<h3".$h3_style."><a href='{$url}'>".$result[$i]['title']."</a></h3>".$result[$i]['content'];
	    		}else{
	    			$link_title = "<a href='{$url}'>".$result[$i]['title']."</a>";
	    		}
			}
			
			$return.= str_replace(array("[link:title]", "[field:title]", "[img:src]", "[field:fulltitle]", "[field:pubdate]", "[field:id]",  "[field:style]", "[field:url]"), array($url, $result[$i]['title'], "attachment/".$result[$i]['picture'].".small.jpg", $result[$i]['fulltitle'],  @date("Y-m-d", $result[$i]['created']),$result[$i]['id'], $style, $url), $content);
			
		}
	}
	
	return $return;
	
}
?>