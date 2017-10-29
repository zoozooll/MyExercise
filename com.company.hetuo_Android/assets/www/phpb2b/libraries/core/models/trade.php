<?php
class Trades extends PbModel {
 	var $name = "Trade";
 	var $info;
 	var $fields = "m.space_name as userid,m.membertype_id,m.username,m.trusttype_ids,m.credits,m.membergroup_id,t.*,t.cache_companyname as companyname";

 	function Trades()
 	{
		parent::__construct();
 	}
 	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Trades();
		}
		return $instance[0];
	}
 	
 	function checkExist($id, $extra = false)
 	{
 		$id = intval($id);
 		$info = $this->dbstuff->GetRow("SELECT title FROM {$this->table_prefix}trades WHERE id={$id}");
 		if (empty($info) or !$info) {
 			return false;
 		}else{
 			return true;
 		}
 	}
 	
	function getInfoById($pid)
	{
		$sql = "select tf.*,t.id,t.industry_id1,t.industry_id2,t.id,t.title,t.content,t.company_id,t.member_id,t.picture,t.area_id1,t.area_id2,t.status,t.type_id,t.submit_time AS pubdate,expire_time AS expdate,require_membertype,require_point,t.tag_ids,t.formattribute_ids,t.if_urgent from {$this->table_prefix}trades t left join {$this->table_prefix}tradefields tf on  tf.trade_id=t.id WHERE t.id=".$pid;
		$result = $this->dbstuff->GetRow($sql);
		$result['tel'] = $result['prim_telnumber'];
		if (!empty($result['picture'])) {
			$result['image'] = pb_get_attachmenturl($result['picture'], '');
			$result['image_url'] = rawurlencode($result['picture']);
		}
		if (!empty($result['tag_ids'])) {
			uses("tag");
			$tag = new Tags();
			$tag_res = $tag->getTagsByIds($result['tag_ids']);
			if (!empty($tag_res)) {
				$tags = null;
				foreach ($tag_res as $key=>$val){
					$tags.='<a href="offer/list.php?do=search&q='.urlencode($val).'" target="_blank">'.$val.'</a>&nbsp;';	
				}
				$result['tag'] = $tags;
				unset($tags, $tag_res, $tag);
			}
		}
		return $result;
	}

	function checkAccess($trade_info_un){
		$trade_info = unserialize($trade_info_un);
		global $tmp_status;
		global $pb_userinfo;
		if($trade_info['TradeStatus']!=1){
			$tmp_key = intval($trade_info['TradeStatus']);
			flash(urlencode($trade_info['Name'].$tmp_status[$tmp_key]));
		}
		if($trade_info['require_membertype']>0){
			if(empty($pb_userinfo['user_type'])) {
				flash("no_perm");
			}
		}
		$t_point = intval($trade_info['require_point']);
		if($t_point>0){
			if($pb_userinfo['points']<$t_point){
			    flash("not_enough_point");
			}else{
			    $sql = "update {$this->table_prefix}members set credits=credits-".$t_point;
			    $this->dbstuff->Execute($sql);
			}
		}
	}
	
	function Delete($ids, $conditions = array())
	{
		$condition = array();
		if (is_array($ids)) {
			$condition[] = "id IN (".implode(",", $ids).")";
		}else{
			$condition[] = "id=".$ids;
		}
		$condition = am($condition, $conditions);
		$this->setCondition($condition);
		$this->dbstuff->Execute("DELETE FROM {$this->table_prefix}trades,{$this->table_prefix}tradefields USING {$this->table_prefix}trades LEFT JOIN {$this->table_prefix}tradefields ON {$this->table_prefix}tradefields.trade_id={$this->table_prefix}trades.id ".$this->getCondition());
		return true;
	}
	
	function Add($params = '')
	{
		$result = false;
		if (!empty($this->params['expire_days'])) {
			$trade_controller = & Trade::getInstance();
			if (array_key_exists($this->params['expire_days'],$trade_controller->getOfferExpires())) {
				$this->params['data']['trade']['expire_time'] = $this->timestamp+(24*3600*$_POST['expire_days']);
				$this->params['data']['trade']['expire_days'] = $_POST['expire_days'];
			}else{
				$this->params['data']['trade']['expire_time'] = $this->timestamp+(24*3600*10);
				$this->params['data']['trade']['expire_days'] = 10;
			}
		}
		$this->params['data']['trade']['submit_time'] = $this->params['data']['trade']['created'] = $this->params['data']['trade']['modified'] = $this->timestamp;
		$this->params['data']['trade']['ip_addr'] = pb_get_client_ip('str');
		if (isset($this->params['data']['trade']['title'])) {
			$trade_info = $this->params['data']['trade'];
		    $result = $this->save($trade_info);
		    $key = $this->table_name."_id";
		    $last_tradeid = $this->$key;
			$_this = & Tradefields::getInstance();
			$_this->params['data']['tradefield']['trade_id'] = $last_tradeid;
			$tradefield_info = $_this->params['data']['tradefield']+$this->params['data']['tradefield'];
			$_this->primaryKey = "trade_id";
			$_this->save($tradefield_info);
		}
		return $result;
	}
	
	function refresh($ids)
	{
		if (empty($ids)) {
			return false;
		}
		if (is_array($ids)) {
			$condition = "id IN (".implode(",", $ids).")";
		}else{
			$condition = "id=".$ids;
		}
		return $this->dbstuff->Execute("UPDATE {$this->table_prefix}trades SET expire_time=expire_days*86400+".$this->timestamp.",submit_time=".$this->timestamp." WHERE ".$condition);
	}
	
	function formatIM($record)
	{
		$return = $code = null;
		$record = unserialize($record);
		if (!is_array($record)) {
			return false;
		}
		foreach ($record as $key=>$val) {
			if(!empty($val)) {
				switch (strtolower($key)) {
					case "qq":
						$code = 'href="http://wpa.qq.com/msgrd?V=1&Uin='.$val.'&Site='.URL.'&Menu=yes"';
						break;		
					case "skype":
						$code = 'href="skype:'.$val.'?call" onclick="return skypeCheck();"';
						break;		
					case "msn":
						$code = 'href="msnim:chat?contact='.$val.'"';
						break;
					case "icq":
						$code = 'href="http://wwp.icq.com/scripts/search.dll?to='.$val.'"';
						break;
					case "yahoo":
						$code = 'href="http://edit.yahoo.com/config/send_webmesg?.target='.$val.'&.src=pg"';
						break;
					default:
						$code = $val;
						break;
				}
				$return.='<a '.$code.' target="_blank"><span class="im_'.$key.'">'.strtoupper($key).'</span></a>';
			}
		}
		return $return;
	}
	
	function formatResult($result)
	{
		global $_PB_CACHE, $form;
		if(class_exists("Trade")){
			$trade_controller = new Trade();
		}else{
			uses("trade");
			$trade_controller = new Trade();
		}
		if(!empty($result)){
			if (empty($_PB_CACHE['trusttype'])) {
				require(CACHE_PATH. 'cache_trusttype.php');
			}
			$result_count = count($result);
			for ($i=0; $i<$result_count; $i++){
				if (empty($result[$i]['userid'])) {
					$result[$i]['userid'] = $result[$i]['username'];
				}
				if(!empty($result[$i]['formattribute_ids'])) {
					$tmp_arr = $form->getAttribute(explode(",", $result[$i]['formattribute_ids']));
					if(!empty($tmp_arr)){
						foreach ($tmp_arr as $key=>$val) {
							$result[$i][$key] = $val;
						}
					}
				}
				$result[$i]['im'] = $this->formatIM($result[$i]['cache_contacts']);
				$result[$i]['pubdate'] = @date("Y-m-d", $result[$i]['submit_time']);
				$result[$i]['content'] = strip_tags($result[$i]['content']);
				$result[$i]['url'] = $trade_controller->rewrite($result[$i]['id'], $result[$i]['type_id']);
				if(!empty($result[$i]['membergroup_id'])) {
					$result[$i]['gradeimg'] = 'images/group/'.$_PB_CACHE['membergroup'][$result[$i]['membergroup_id']]['avatar'];
					$result[$i]['gradename'] = $_PB_CACHE['membergroup'][$result[$i]['membergroup_id']]['name'];
				}
				$result[$i]['image'] = pb_get_attachmenturl($result[$i]['picture'], '', 'middle');
				$trusttype_images = null;
				if(!empty($result[$i]['trusttype_ids'])){
					$tmp_trusttype = explode(",", $result[$i]['trusttype_ids']);
					foreach ($tmp_trusttype as $val) {
						$trusttype_images.='<img src="'.$_PB_CACHE['trusttype'][$val]['avatar'].'" alt="'.$_PB_CACHE['trusttype'][$val]['name'].'" />';
					}
				}
				$result[$i]['trusttype'] = $trusttype_images;
			}
			return $result;
		}else{
			return null;
		}
	}
	
	function getRenderDatas($conditions = null, $filter = false)
	{
		global $page;
		if (!empty($conditions)) {
			$this->setCondition($conditions);
		}
		$sql = "SELECT ".$this->fields." FROM ".$this->table_prefix."trades t LEFT JOIN ".$this->table_prefix."members m ON m.id=t.member_id ".$this->getCondition()." ORDER BY t.id DESC LIMIT ".$page->firstcount.",".$page->displaypg;
		if (!isset($_GET['page'])) {
			if ($page->total_record>$page->displaypg && $filter) {
				$sql = "SELECT ".$this->fields." FROM ".$this->table_prefix."trades t LEFT JOIN ".$this->table_prefix."members m ON m.id=t.member_id ".$this->getCondition()." GROUP BY t.member_id ORDER BY t.id DESC LIMIT ".$page->firstcount.",".$page->displaypg;
			}
		}
		$result = $this->dbstuff->GetArray($sql);
		return $this->formatResult($result);
	}
	
	function getStickyDatas()
	{
		$condition = null;
		if (!isset($_GET['page']) || $_GET['page']==1) {
			if (isset($_GET['typeid'])) {
				$type_id = intval($_GET['typeid']);
				$condition = " AND t.type_id='".$type_id."'";
			}
			$sql = "SELECT ".$this->fields." FROM ".$this->table_prefix."trades t LEFT JOIN ".$this->table_prefix."members m ON m.id=t.member_id WHERE t.display_order>0 {$condition} AND t.display_expiration>".$this->timestamp." ORDER BY t.id DESC LIMIT 0,5";
			$result = $this->dbstuff->GetArray($sql);
			if(!empty($result)){
				return $this->formatResult($result);
			}
		}
		return false;
	}
}
?>