<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Shows_model extends Model{
var $subject_eng,
		$subject_chi,
		$time,
		$content_eng,
		$content_chi,
		$img;
	function showss_model(){
		parent :: Model();
	}
	function add($shows){
		$this->subject_eng = $shows['subject_eng'];
		$this->subject_chi = $shows['subject_chi'];
		$this->time = now();
		$this->content_eng = $shows['content_eng'];
		$this->content_chi = $shows['content_chi'];
		$this->img = $shows['img'];
		return $this->db->insert('shows', $this);
	}
	function shows_count(){
		$query = mysql_query( "select count(*) as count from shows" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function delete_data($shows){
		$this->db->where('id', $shows['id']);
		$this->db->delete('shows');
		href($_SERVER['HTTP_REFERER']);
	}
	function shows_data($shows){
		$this->db->where('id', $shows['id']);
		return $this->db->get('shows')->result();
	}
	function edit($shows){
		$this->subject_eng = $shows['subject_eng'];
		$this->subject_chi = $shows['subject_chi'];
		$this->time = now();
		$this->content_eng = $shows['content_eng'];
		$this->content_chi = $shows['content_chi'];
		$this->img = $shows['img'];
		return $this->db->update('shows', $this, array('id' => $shows['id']));
	}
	function all($num, $offset){
		$this->db->orderby('time', 'desc');
		return $this->db->get('shows',$num, $offset)->result();
	}
	function sort($key, $value) {
			$data = $this->shows_data(array('id' => $key));
			$this->subject = $data[0]->subject;
			$this->content = $data[0]->content;
			$this->img     = $data[0]->img;
			$this->time    = $data[0]->time;
			$this->no      = $value;
			return $this->db->update('shows', $this, array('id' => $key));
	}
	function hot_shows(){
		$this->db->limit(1);
		$this->db->orderby('no', 'desc');
		return $this->db->get('shows')->result();
	}
}