<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 02:38 05/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Skill extends Controller{
	var $folder = NULL;
	function Skill(){
		parent::Controller();
		$this->folder = $this->uri->segment(3) == NULL ? 'chi' : $this->uri->segment(3);
	}
	
	function skillview(){
		$this->load->model('backend/skill_model','skill', TRUE);
		$skill_vo = array (
			'id' => $this->uri->segment(4)
		);
		$data['data'] = $this->skill->skill_data($skill_vo);
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/skillview", $data);
	}
}