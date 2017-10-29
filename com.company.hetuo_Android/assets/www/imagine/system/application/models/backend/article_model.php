<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Article_model extends Model{
var $subject_eng,
		$subject_chi,
		$time,
		$content_eng,
		$content_chi,
		$img;
	function Article_model(){
		parent :: Model();
	}
	function add($article){
		$this->subject_eng = $article['subject_eng'];
		$this->subject_chi = $article['subject_chi'];
		$this->time = now();
		$this->content_eng = $article['content_eng'];
		$this->content_chi = $article['content_chi'];
		$this->img = $article['img'];
		return $this->db->insert('article', $this);
	}
	function article_count(){
		$query = mysql_query( "select count(*) as count from article" );
		$result = mysql_fetch_array($query);
		return $result['count'];
	}
	function delete_data($article){
		$this->db->where('id', $article['id']);
		$this->db->delete('article');
		href($_SERVER['HTTP_REFERER']);
	}
	function article_data($article){
		$this->db->where('id', $article['id']);
		return $this->db->get('article')->result();
	}
	function edit($article){
		$this->subject_eng = $article['subject_eng'];
		$this->subject_chi = $article['subject_chi'];
		$this->time = now();
		$this->content_eng = $article['content_eng'];
		$this->content_chi = $article['content_chi'];
		$this->img = $article['img'];
		return $this->db->update('article', $this, array('id' => $article['id']));
	}
	function all($num, $offset){
		$this->db->orderby('time', 'desc');
		return $this->db->get('article',$num, $offset)->result();
	}
	function sort($key, $value) {
			$data = $this->article_data(array('id' => $key));
			$this->subject = $data[0]->subject;
			$this->content = $data[0]->content;
			$this->img     = $data[0]->img;
			$this->time    = $data[0]->time;
			$this->no      = $value;
			return $this->db->update('article', $this, array('id' => $key));
	}
	function hot_article(){
		$this->db->limit(1);
		$this->db->orderby('no', 'desc');
		return $this->db->get('article')->result();
	}
}