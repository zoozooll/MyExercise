<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class User extends Controller{
	function User(){
		parent::Controller();
	}
	function add_view(){
		$this->load->view('backend/header');
		$this->load->view('backend/user/user_add');
	}
	function edit_view(){
		$this->load->model('backend/user_model', 'user', TRUE);
		$user_vo = array(
			'id' => $this->uri->segment(4)
		);
		$data['data'] = $this->user->user_data($user_vo);
		$data['type'] = type();
		$this->load->view('backend/header');
		$this->load->view('backend/user/user_edit', $data);		
	}
	function all_view(){
		if ( !$this->session->userdata('userdata') ) {
				href(base_url().'index.php/backend/user/login_view');
		}
		$this->load->model('backend/user_model', 'user', TRUE);
		$config['base_url'] = base_url() . './index.php/backend/user/all_view';
		$config['total_rows'] = $this->user->user_count();
		$config['per_page'] = '20';
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
		$data['datas'] = $this->user->all($config['per_page'], $this->uri->segment(4));
		$data['href'] = site_url('backend/user/all_view');
		$data['page'] = $this->pagination->create_links();
		$this->load->view('backend/header');
		$this->load->view('backend/user/user_all', $data);
	}
	function login_view(){
		$this->load->view('backend/login');
	}
	function add(){
		$this->load->model('backend/user_model', 'user', TRUE);
		$user_vo = array(
			'user' => $this->input->post('user'),
			'pw' => $this->input->post('pw')
		);
		$data['content'] = $this->user->add($user_vo) ? 'Upload Success' : 'Upload Faild';
		$data['href'] = site_url('backend/user/all_view');
		$this->load->view('backend/success', $data);
	}
	function del(){
		$this->load->model('backend/user_model', 'user', TRUE);
		$user_vo = array(
			'id' => $this->uri->segment(4)
		);
		$this->user->delete_data($user_vo);
		href($_SERVER['HTTP_REFERER']);
	}
	function edit(){
		$this->load->model('backend/user_model', 'user', TRUE);
		$id = $this->input->post('id');
		$data = $this->user->user_data(array('id' => $id));
		$user_vo = array(
			'id' => $id,
			'user' => $this->input->post('user'),
			'pw' => $this->input->post('pw')
		);
		$data['content'] = $this->user->edit($user_vo) ? 'Update Success' : 'Update Faild';
		$data['href'] = site_url('backend/user/all_view');
		$this->load->view('backend/success', $data);
	}
	function check_login(){
		$user_vo = array (
			'user' => $this->input->post('user'),
			'pw'   => $this->input->post('pw')
		);
		$this->load->model('backend/user_model', 'user', TRUE);
		if ($user_data =  $this->user->login($user_vo) ) {
				$this->session->set_userdata('userdata', $user_data[0]->user);
				href(base_url().'index.php/backend/index/welcome');
		} else {
			$this->load->view('backend/login');	
		}
	}
}