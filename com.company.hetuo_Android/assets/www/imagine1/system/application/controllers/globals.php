<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 23:50 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Globals extends Controller{
	var $folder = NULL;
	function Globals(){
		parent::Controller();
		$this->folder = $this->uri->segment(3) == NULL ? 'chi' : $this->uri->segment(3);
	}
	
	function index(){
		$this->load->model('backend/product_model','product', TRUE);
		$this->load->model('backend/head_model', 'head', TRUE);
		$data['select'] = $this->uri->segment(4) == NULL ? 'index' : $this->uri->segment(4);
		$data['datas'] = $this->product->all('4', '0');
		$data['hot_datas'] = $this->product->all('4', '0', 'click');
		$data['img'] = $this->head->all(7, 0);
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/index", $data);
	}
	function company(){
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/company");
	}
	function prolist(){
		$this->load->model('backend/product_model','product', TRUE);
		$data['select'] = $this->uri->segment(4) == NULL ? 'index' : $this->uri->segment(4);
		$data['title'] = product_title(array('string' => $this->uri->segment(4), 'language' => $this->folder));
		$config['base_url'] = base_url() . "./index.php/globals/prolist/{$this->folder}";
		$config['total_rows'] = $this->product->product_count();
		$config['per_page'] = '8';
		$config['num_links'] = '10';
		$config['uri_segment'] = '4';
		$config['first_link'] = 'First';
		$config['first_tag_open'] = ' ';
		$config['first_tag_close'] = ' ';
		$config['last_link'] = 'Last';
		$config['last_tag_open'] = ' ';
		$config['last_tag_close'] = ' ';
		$config['next_link'] = '>>';
		$config['next_tag_open'] = ' ';
		$config['next_tag_close'] = ' ';
		$config['prev_link'] = '<<';
		$config['prev_tag_open'] = ' ';
		$config['prev_tag_close'] = ' ';
		$this->pagination->initialize($config);
		$data['datas'] = $this->product->all($config['per_page'], $this->uri->segment(4));
		$data['page'] = $this->pagination->create_links();
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/prolist", $data);
	}
	function infolist(){
		$this->load->model('backend/skill_model','skill', TRUE);
		$config['base_url'] = base_url() . "./index.php/globals/infolist/{$this->folder}";
		$config['total_rows'] = $this->skill->skill_count();
		$config['per_page'] = '30';
		$config['num_links'] = '10';
		$config['uri_segment'] = '4';
		$config['first_link'] = 'First';
		$config['first_tag_open'] = ' ';
		$config['first_tag_close'] = ' ';
		$config['last_link'] = 'Last';
		$config['last_tag_open'] = ' ';
		$config['last_tag_close'] = ' ';
		$config['next_link'] = '>>';
		$config['next_tag_open'] = ' ';
		$config['next_tag_close'] = ' ';
		$config['prev_link'] = '<<';
		$config['prev_tag_open'] = ' ';
		$config['prev_tag_close'] = ' ';
		$this->pagination->initialize($config);
		$data['datas'] = $this->skill->all($config['per_page'], $this->uri->segment(4));
		$data['page'] = $this->pagination->create_links();
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/infolist", $data);
	}
	function news(){
		$this->load->model('backend/shows_model','shows', TRUE);
		$config['base_url'] = base_url() . "./index.php/globals/infolist/{$this->folder}";
		$config['total_rows'] = $this->shows->shows_count();
		$config['per_page'] = '30';
		$config['num_links'] = '10';
		$config['uri_segment'] = '4';
		$config['first_link'] = 'First';
		$config['first_tag_open'] = ' ';
		$config['first_tag_close'] = ' ';
		$config['last_link'] = 'Last';
		$config['last_tag_open'] = ' ';
		$config['last_tag_close'] = ' ';
		$config['next_link'] = '>>';
		$config['next_tag_open'] = ' ';
		$config['next_tag_close'] = ' ';
		$config['prev_link'] = '<<';
		$config['prev_tag_open'] = ' ';
		$config['prev_tag_close'] = ' ';
		$this->pagination->initialize($config);
		$data['datas'] = $this->shows->all($config['per_page'], $this->uri->segment(4));
		$data['page'] = $this->pagination->create_links();
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/newslist", $data);		
	}
	function contact(){
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/contact");		
	}
}