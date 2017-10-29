<?php
/*+-------
-*IDE	 DW4 
-*Author Billy 
-*Time   GMT+8 15:36 17/12/2010
-*Msn	 billykwong@live.cn
-*Email  yeksing@aim.com
-+--------*/
class Head extends Controller{
	function Head(){
		parent::Controller();
		if ( !$this->session->userdata('userdata') ) {
			href(base_url().'index.php/backend/head/login_view');
		}
	}
	function add_view(){
		$data = array ();
		//$this->load->model('backend/downclass_model', 'downclass', TRUE);
		//$this->load->model('backend/server_model', 'server', TRUE);
		$this->load->view('backend/header');
		//$data['downclass'] = $this->downclass->all_data();
		//$data['server'] = $this->server->all_data();
		$this->load->view('backend/head/head_add', $data);
	}
	function edit_view(){
		$this->load->model('backend/head_model', 'head', TRUE);
	//	$this->load->model('backend/downclass_model', 'downclass', TRUE);
		//$this->load->model('backend/server_model', 'server', TRUE);
		$head_vo = array(
			'id' => $this->uri->segment(4)
		);
		$data['data'] = $this->head->head_data($head_vo);
		//$data['downclass'] = $this->downclass->all_data();
		//$data['server'] = $this->server->all_data();
		$this->load->view('backend/header');
		$this->load->view('backend/head/head_edit', $data);		
	}
	function all_view(){
		$this->load->model('backend/head_model', 'head', TRUE);
		$config['base_url'] = base_url() . './index.php/backend/head/all_view';
		$config['total_rows'] = $this->head->head_count();
		$config['per_page'] = '14';
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
		$data['datas'] = $this->head->all($config['per_page'], $this->uri->segment(4));
		$data['href'] = site_url('backend/head/all_view');
		$data['page'] = $this->pagination->create_links();
		$this->load->view('backend/header');
		$this->load->view('backend/head/head_all', $data);
	}
	function add(){
		$this->load->model('backend/head_model', 'head', TRUE);
		//$this->load->model('backend/server_model', 'server', TRUE);
		/*不同服务器存放*/
		#$config['upload_path'] = $this->config->item('stauts') . $this->input->post('serverpath') .':' . $this->config->item('port');
		#$config['upload_path'] .= '/' . $this->config->item('site_name') . '/system/images/backend/uploads';
		$config['upload_path'] = './system/images/backend/uploads';
		$config['allowed_types'] = 'gif|jpg|png|txt|rar|doc';
		$config['max_size'] = $this->config->item('max_size');
		$config['max_width'] = '1024';
		$config['max_height'] = '768';
		$this->load->library('upload', $config);
		$result = $this->upload->do_upload('img');
		if ( ! $result['success'] ) {
			echo $this->upload->display_errors();	
		}
		if (!stristr($link = $this->input->post('link'), 'http:') ) {
			$link = 'http://'.$this->input->post('link'); 
		}
		//$servername = $this->server->server_data( array ( 'serverpath' => $this->input->post('serverpath') ) );
		$head_vo = array(
			'picname' => $this->input->post('picname'),
			'sort' => $this->input->post('sort'),
			'picpath' => $result['data']['0']['file_name'] !=NULL ? $result['data']['0']['file_name'] : '',
			'link' => $link
		);
		$data['content'] = $this->head->add($head_vo) ? 'Upload Success' : 'Faild';
		$data['href'] = site_url('backend/head/all_view');
		$this->load->view('backend/success', $data);
	}
	function del(){
		$this->load->model('backend/head_model', 'head', TRUE);
		$head_vo = array(
			'id' => $this->uri->segment(4)
		);
		$this->head->delete_data($head_vo);
		href($_SERVER['HTTP_REFERER']);
	}
	function edit(){
		$this->load->model('backend/head_model', 'head', TRUE);
		$id = $this->input->post('id');
		$data = $this->head->head_data(array('id' => $id));
		//$this->load->model('backend/server_model', 'server', TRUE);
		/*不同服务器存放*/
		#$config['upload_path'] = $this->config->item('stauts') . $this->input->post('serverpath') .':' . $this->config->item('port');
		#$config['upload_path'] .= '/' . $this->config->item('site_name') . '/system/images/backend/uploads';
		$config['upload_path'] = './system/images/backend/uploads';
		$config['allowed_types'] = 'gif|jpg|png|txt|rar|doc';
		$config['max_size'] = $this->config->item('max_size');
		$config['max_width'] = '1024';
		$config['max_height'] = '768';
		$this->load->library('upload', $config);
		$result = $this->upload->do_upload('img');
		if ( ! $result['success'] ) {
			echo $this->upload->display_errors();	
		}
	//	$servername = $this->server->server_data( array ( 'serverpath' => $this->input->post('serverpath') ) );
		$head_vo = array(
			'id' => $id,
			'picname' => $this->input->post('picname'),
			'sort' => $this->input->post('sort'),
			'picpath' => $result['data']['0']['file_name'] !=NULL ? $result['data']['0']['file_name'] : $data[0]->picpath,
			'link' => $this->input->post('link'),
		);
		$data['content'] = $this->head->edit($head_vo) ? 'Upload Success' : 'Faild';
		$data['href'] = site_url('backend/head/all_view');
		$this->load->view('backend/success', $data);
	}
	function head_info(){
		$this->load->model('backend/head_model', 'head', TRUE);
		$head_vo = array(
			'id' => $this->uri->segment(4)
		);
		$data['data'] = $this->head->head_data($head_vo);
		$data['type'] = type();
		$this->load->view('backend/header');
		$this->load->view('backend/head/head_edit', $data);		
	}
	function head_type(){
		$this->load->model('backend/head_model', 'head', TRUE);
		$config['total_rows'] = $this->head->head_count();
		$config['per_page'] = '10';
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
		$config['base_url'] = base_url() . './index.php/frontend/head/head_type/'.$this->uri->segment(4);
		$this->pagination->initialize($config);
		$data['datas'] = $this->head->all($config['per_page'], $this->uri->segment(5));
		$data['href'] = site_url('frontend/head/all_view');
		$data['page'] = $this->pagination->create_links();
		$data['type'] = type();
		$this->load->view('frontend/header');
		$this->load->view('frontend/head/head_all', $data);
	}
}