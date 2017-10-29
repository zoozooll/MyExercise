<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 02:38 05/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class News extends Controller{
	var $folder = NULL;
	function News(){
		parent::Controller();
		$this->folder = $this->uri->segment(3) == NULL ? 'chi' : $this->uri->segment(3);
	}
	
	function newsview(){
		$this->load->model('backend/shows_model','shows', TRUE);
		$news_vo = array (
			'id' => $this->uri->segment(4)
		);
		$data['data'] = $this->shows->shows_data($news_vo);
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/newsview", $data);
	}
}