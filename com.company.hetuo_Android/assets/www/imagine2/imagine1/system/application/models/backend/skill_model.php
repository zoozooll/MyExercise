<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Skill_model extends Model{
var $subject_eng,
		$subject_chi,
		$time,
		$content_eng,
		$content_chi,
		$img;
	function Skill_model(){
		parent :: Model();
	}
	function add($skill){
		$this->subject_eng = $skill['subject_eng'];
		$this->subject_chi = $skill['subject_chi'];
		$this->time = now();
		$this->content_eng = $skill['content_eng'];
		$this->content_chi = $skill['content_chi'];
		$this->img = $skill['img'];
		return $this->db->insert('skill', $this);
	}
	function skill_count(){
		$query = mysql_query( "select count(*) as count from skill" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function delete_data($skill){
		$this->db->where('id', $skill['id']);
		$this->db->delete('skill');
		href($_SERVER['HTTP_REFERER']);
	}
	function skill_data($skill){
		$this->db->where('id', $skill['id']);
		return $this->db->get('skill')->result();
	}
	function edit($skill){
		$this->subject_eng = $skill['subject_eng'];
		$this->subject_chi = $skill['subject_chi'];
		$this->time = now();
		$this->content_eng = $skill['content_eng'];
		$this->content_chi = $skill['content_chi'];
		$this->img = $skill['img'];
		return $this->db->update('skill', $this, array('id' => $skill['id']));
	}
	function all($num, $offset){
		$this->db->orderby('time', 'desc');
		return $this->db->get('skill',$num, $offset)->result();
	}
	function sort($key, $value) {
			$data = $this->skill_data(array('id' => $key));
			$this->subject = $data[0]->subject;
			$this->content = $data[0]->content;
			$this->img     = $data[0]->img;
			$this->time    = $data[0]->time;
			$this->no      = $value;
			return $this->db->update('skill', $this, array('id' => $key));
	}
	function hot_skill(){
		$this->db->limit(1);
		$this->db->orderby('no', 'desc');
		return $this->db->get('skill')->result();
	}
}