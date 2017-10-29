<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Company_model extends Model{
var $subject_eng,
		$subject_chi,
		$time,
		$content_eng,
		$content_chi,
		$img;
	function Company_model(){
		parent :: Model();
	}
	function add($company){
		$this->subject_eng = $company['subject_eng'];
		$this->subject_chi = $company['subject_chi'];
		$this->time = now();
		$this->content_eng = $company['content_eng'];
		$this->content_chi = $company['content_chi'];
		$this->img = $company['img'];
		return $this->db->insert('company', $this);
	}
	function company_count(){
		$query = mysql_query( "select count(*) as count from company" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function delete_data($company){
		$this->db->where('id', $company['id']);
		$this->db->delete('company');
		href($_SERVER['HTTP_REFERER']);
	}
	function company_data($company){
		$this->db->where('id', $company['id']);
		return $this->db->get('company')->result();
	}
	function edit($company){
		$this->subject_eng = $company['subject_eng'];
		$this->subject_chi = $company['subject_chi'];
		$this->time = now();
		$this->content_eng = $company['content_eng'];
		$this->content_chi = $company['content_chi'];
		$this->img = $company['img'];
		return $this->db->update('company', $this, array('id' => $company['id']));
	}
	function all($num, $offset){
		$this->db->orderby('time', 'desc');
		return $this->db->get('company',$num, $offset)->result();
	}
	function sort($key, $value) {
			$data = $this->company_data(array('id' => $key));
			$this->subject = $data[0]->subject;
			$this->content = $data[0]->content;
			$this->img     = $data[0]->img;
			$this->time    = $data[0]->time;
			$this->no      = $value;
			return $this->db->update('company', $this, array('id' => $key));
	}
	function hot_company(){
		$this->db->limit(1);
		$this->db->orderby('no', 'desc');
		return $this->db->get('company')->result();
	}
}