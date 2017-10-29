<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Product_model extends Model{
	var $subject_eng,
			$subject_chi,
			$img1,
			$img2,
			$content_eng,
			$content_chi,
			$trait_eng,
			$trait_chi,
			$app_eng,
			$app_chi,
			$time,
			$number,
			$corporate,
			$tv,
			$print,
			$advertising,
			$promotion,
			$event,
			$premeium,
			$video;
	function Product_model(){
		parent::Model();
	}
	function add($product){
		$this->subject_eng = $product['subject_eng'];
		$this->subject_chi = $product['subject_chi'];
		$this->img1 = $product['img1'];
		
		$this->content_eng = $product['content_eng'];
		$this->content_chi = $product['content_chi'];

		$this->number = $product['number'];
		$this->time = now();
		$this->corporate = $product['corporate'];
		$this->tv = $product['tv'];
		$this->print = $product['print'];
		$this->advertising = $product['advertising'];
		$this->promotion = $product['promotion'];
		$this->event = $product['event'];
		$this->premeium = $product['premeium'];
		$this->video = $product['video'];
		return $this->db->insert('product', $this);
	}
	function product_count(){
		$query = mysql_query( "select count(*) as count from product" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function delete_data($product){
		$this->db->where('id', $product['id']);
		$this->db->delete('product');
		href($_SERVER['HTTP_REFERER']);
	}
	function product_data($product){
		$this->db->where('id', $product['id']);
		return $this->db->get('product')->result();
	}
	function edit($product){
		$this->subject_eng = $product['subject_eng'];
		$this->subject_chi = $product['subject_chi'];
		$this->img1 = $product['img1'];
		
		$this->content_eng = $product['content_eng'];
		$this->content_chi = $product['content_chi'];

		$this->time = now();
		$this->number = $product['number'];
		$this->corporate = $product['corporate'];
		$this->tv = $product['tv'];
		$this->print = $product['print'];
		$this->advertising = $product['advertising'];
		$this->promotion = $product['promotion'];
		$this->event = $product['event'];
		$this->premeium = $product['premeium'];
		$this->video = $product['video'];
		return $this->db->update('product', $this, array('id' => $product['id']));
	}
	function all($num, $offset, $condition = 'time'){
		$this->db->orderby($condition, 'desc');
		return $this->db->get('product',$num, $offset)->result();
	}
	function product_type_count($product){
		$query = mysql_query( "select count(*) as count from product where ".$product['type']." = '1'" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function product_type_all($product){
		$this->db->where($product['type'], '1');
		$data['datas'] = $this->db->get('product', $product['num'], $product['offset'])->result();
		$query = mysql_query( "select count(*) as count from product where {$product['type']} = '1'" );
		$result = mysql_fetch_array($query);
		$data['count'] = $result['count'];
		return $data;
	}
	function product_type($id){
		$result_type = array();
		$type_list = array ('corporate', 'tv', 'print', 'advertising', 'promotion', 'event', 'premeium', 'video');
		foreach ($type_list as $type) {
			$query = mysql_query( "select * from product where id = {$id} and $type = '1' " );
			if ( mysql_fetch_array($query) !=NULL ) {
					$result_type[0] = $type;
					break;
			}
		}	
		return $result_type;
	}
	function product_data_array($product){
		$this->db->where($product['condition'], $product['keyword']);
		return $this->db->get('product')->result();
	}
	function product_click_update($product) {
		$query = mysql_query( "UPDATE  `imagine`.`product` SET  `click` = `click` + 1  WHERE  `product`.`id` = {$product['id']} LIMIT 1 ;");
	}
}
?>
