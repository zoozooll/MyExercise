<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class User_model extends Model{
	var $user,
			$pw;
	function User(){
		parnt :: Model();
	}
		function add($user){
		$this->user = $user['user'];
		$this->pw = $user['pw'];
		return $this->db->insert('user', $this);
	}
	function user_count(){
		$query = mysql_query( "select count(*) as count from user" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function delete_data($user){
		$this->db->where('id', $user['id']);
		$this->db->delete('user');
		href($_SERVER['HTTP_REFERER']);
	}
	function user_data($user){
		$this->db->where('id', $user['id']);
		return $this->db->get('user')->result();
	}
	function edit($user){
		$this->user = $user['user'];
		$this->pw = $user['pw'];
		return $this->db->update('user', $this, array('id' => $user['id']));
	}
	function all($num, $offset){
		return $this->db->get('user',$num, $offset)->result();
	}
	function login($user){
		$this->db->where('user', $user['user']);
		$this->db->where('pw', $user['pw']);
		return $this->db->get('user')->result();
	}
}