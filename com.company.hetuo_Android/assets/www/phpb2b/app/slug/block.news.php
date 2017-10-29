<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 615 $
 */
function smarty_block_news($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("News")) {
		$news = new Newses();
		$news_controller = new News();
	}else{
	    uses("news");
	    $news = new Newses();
		$news_controller = new News();
	}
	$orderby = array();
	require(CACHE_PATH. "cache_type.php");
	$conditions[] = "n.status=1";
	if(isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'image':
					$conditions[] = "n.picture!=''";
					break;
				case 'commend':
					$conditions[] = "if_commend>0";
					break;
				case 'hot':
					$orderby[] = 'clicked DESC';
					break;
				case 'focus':
					$conditions[] = "n.if_focus='1'";
					break;
				default:
					break;
			}
		}
	}
	if (isset($params['flag'])) {
		$conditions[] = "n.flag='".$params['flag']."'";
	}
	if (isset($params['tag'])) {
		$conditions[] = "n.title like '%".$params['tag']."%'";
	}
	if (isset($params['typeid'])) {
		$conditions[] = "n.type_id=".$params['typeid'];
	}
	if (isset($params['id'])) {
		$conditions[] = "n.id='".$params['id']."'";
	}
	if (isset($params['orderby'])) {
		$orderby[] = trim($params['orderby']);
	}else{
		$orderby[] = "id DESC ";
	}
	if (!empty($params['industryid'])) {
		$conditions[] = "n.industry_id='".$params['industryid']."'";
	}
	if (!empty($params['areaid'])) {
		$conditions[] = "n.area_id='".$params['areaid']."'";
	}
	$news->setOrderby($orderby);
	$news->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$news->setLimitOffset($row, $col);
	$sql = "SELECT *,title as fulltitle,content as fullcontent FROM {$news->table_prefix}newses n ".$news->getCondition().$news->getOrderby().$news->getLimitOffset();
	$result = $news->dbstuff->GetArray($sql);
	$return = $link_title = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$style = null;
			$dt = @getdate($result[$i]['created']);
			$url = $news_controller->rewrite($result[$i]['id'], $result[$i]['title'], $result[$i]['created']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}
	    	$result[$i]['content'] = strip_tags($result[$i]['content']);
	    	if (isset($params['infolen'])) {
	    		$result[$i]['content'] = mb_substr($result[$i]['content'], 0, $params['infolen']);
	    	}
	    	$image_type = isset($params['imagetype'])?trim($params['imagetype']):"small";
	    	$img = (empty($result[$i]['picture']))?pb_get_attachmenturl('', '', $image_type):pb_get_attachmenturl($result[$i]['picture'], '', $image_type);
	    	if (isset($params['magic']))  {
	    		if ($i==0){
	    			if(!empty($result[$i]['picture'])) {
	    				$style = " style=\"height:70px; background:url(".URL."attachment/".$result[$i]['picture'].".small.jpg".") no-repeat; padding:0 0 0 90px;overflow:hidden; width:120px;\"";
	    				$h3_style = " style=\"padding:0 0 0 5px;\"";
	    			}else{
	    				$style = " style=\"height:70px; background:url(".URL.$img.") no-repeat; padding:0 0 0 90px;\"";
	    			}
	    			$link_title = "<h3".$h3_style."><a href='{$url}'>".$result[$i]['title']."</a></h3>".$result[$i]['content'];
	    		}else{
	    			$link_title = "<a href='{$url}'>".$result[$i]['title']."</a>";
	    		}
			}
			if ($result[$i]['type'] == 1) {
				$link_title = "<a href='".$result[$i]['fullcontent']."'>".$result[$i]['title']."</a>";
				$url = $result[$i]['fullcontent'];
			}
			$return.= str_replace(array("[link:title]", "[link:url]",  "[field:title]", "[img:src]", "[field:fulltitle]", "[field:typename]", "[field:pubdate]", "[field:id]", "[field:type_id]", "[field:style]", "[field:url]", "[field:content]"), array($url, '<a href="'.$url.'" title="'.$result[$i]['fulltitle'].'">'.$result[$i]['title'].'</a>', $result[$i]['title'], $img, $result[$i]['fulltitle'], $result[$i]['type_id']?$_PB_CACHE['newstype'][$result[$i]['type_id']]:L("undefined_sort", "tpl"), @date("Y-m-d", $result[$i]['created']),$result[$i]['id'], $result[$i]['type_id'], $style, $link_title, $result[$i]['content']), $content);
		}
	}
	return $return;
}
?>