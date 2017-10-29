<?php
/*+-----------------------------*
-*IDE	 DW4 			`	    *
-*Author Billy 				    *
-*Time   GMT+8 17:43 17/12/2010 *
-*Msn	 billykwong@live.cn	    *
-*Email  yeksing@aim.com	    *
-*+-----------------------------*/
class Head_model extends Model{
	var $picpath,
		$picname,

		$link,
		$time,
		$sort;
	function Head_model(){
		parent :: Model();
	}
	function add($head){
		$this->picpath = $head['picpath'];
		$this->picname = $head['picname'];

		$this->sort = $head['sort'];
		$this->link = $head['link'];
		$this->time = now();
		return $this->db->insert('head', $this);
	}
	function head_count(){
		$query = mysql_query( "select count(*) as count from head" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function delete_data($head){
		$this->db->where('id', $head['id']);
		$this->db->delete('head');
		href($_SERVER['HTTP_REFERER']);
	}
	function head_data($head){
		$condition = array_keys($head);
		$this->db->where($condition[0], $head[$condition[0]]);
		return $this->db->get('head')->result();
	}
	function edit($head){
		$this->picpath = $head['picpath'];
		$this->picname = $head['picname'];

		$this->link = $head['link'];
		$this->sort = $head['sort'];
		$this->time = now();
		return $this->db->update('head', $this, array('id' => $head['id']));
	}
	function all($num, $offset){
		$this->db->orderby('sort', 'desc');
		return $this->db->get('head',$num, $offset)->result();
	}
	function all_data(){
		return $this->db->get('head')->result();
	}
}