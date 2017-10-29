<?php
class Products extends PbModel {
 	var $name = "Product";
 	var $info;

 	function Products()
 	{
		parent::__construct();
 	}

	function checkProducts($id = null, $status = null)
	{
		if(is_array($id)){
			$checkId = "id IN (".implode(",",$id).")";
		}else {
			$checkId = "id=".$id;
		}
		$sql = "UPDATE ".$this->getTable()." SET status=".$status." WHERE ".$checkId;
		$return = $this->dbstuff->Execute($sql);
		if($return){
			return true;
		}else {
			return false;
		}
	}
	
	function getInfo($id)
	{
		$sql = "SELECT p.*,m.username,c.name AS companyname FROM {$this->table_prefix}products p LEFT JOIN {$this->table_prefix}members m ON m.id=p.member_id LEFT JOIN {$this->table_prefix}companies c ON c.member_id=p.member_id WHERE p.id=".$id;
		$result = $this->dbstuff->GetRow($sql);
		return $result;
	}
	
	function getSimilarByMemberId($member_id)
	{
		return $this->findAll("id,name", null, "Product.state=1 AND Product.status=1 AND Product.member_id={$member_id}", "Product.id DESC",0,8);
	}
	
	function getProductById($product_id)
	{
		$sql = "SELECT p.* FROM {$this->table_prefix}products p WHERE p.id=".$product_id;
		$result = $this->dbstuff->GetRow($sql);
		if (empty($result) || !$result) {
			return false;
		}
		$result['pubdate'] = @date("Y-m-d", $result['created']);
		if (!empty($result['picture'])) {
			$result['imgsmall'] = "attachment/".$result['picture'].".small.jpg";
			$result['imgmiddle'] = "attachment/".$result['picture'].".middle.jpg";
			$result['image'] = "attachment/".$result['picture'];
			$result['image_url'] = rawurlencode($result['picture']);
		}else{
			$result['image'] = pb_get_attachmenturl('', '', 'middle');
		}
		if (!empty($result['tag_ids'])) {
			uses("tag");
			$tag = new Tags();
			$tag_res = $tag->getTagsByIds($result['tag_ids']);
			if (!empty($tag_res)) {
				$tags = null;
				foreach ($tag_res as $key=>$val){
					$tags.='<a href="product/list.php?do=search&q='.urlencode($val).'" target="_blank">'.$val.'</a>&nbsp;';	
				}
				$result['tag'] = $tags;
				unset($tags, $tag_res, $tag);
			}
		}
		$this->info = $result;
		return $result;
	}
	
	function formatResult($result)
	{
		global $rewrite_able;
		require(CACHE_PATH. 'cache_membergroup.php');
		if (!empty($result)) {
			$count = count($result);
			for($i=0; $i<$count; $i++){
				$result[$i]['pubdate'] = @date("Y-m-d", $result[$i]['created']);
				$result[$i]['content'] = strip_tags($result[$i]['content']);
				$result[$i]['url'] = ($rewrite_able)? "product/detail/".$result[$i]['id'].".html":"product/content.php?id=".$result[$i]['id'];;
				$result[$i]['gradeimg'] = 'images/group/'.$_PB_CACHE['membergroup'][$result[$i]['membergroup_id']]['avatar'];
				$result[$i]['gradename'] = $_PB_CACHE['membergroup'][$result[$i]['membergroup_id']]['name'];
				$result[$i]['image'] = pb_get_attachmenturl($result[$i]['picture']);
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
}
?>