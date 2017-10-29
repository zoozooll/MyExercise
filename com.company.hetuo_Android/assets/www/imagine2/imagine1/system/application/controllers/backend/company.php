<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Company extends Controller{
	function Company(){
		parent::Controller();
		if ( !$this->session->userdata('userdata') ) {
				href(base_url().'index.php/backend/user/login_view');
		}
	}
	function add_view(){
		$this->load->view('backend/header');
		$this->load->view('backend/company/company_add');
	}
	function edit_view(){
		$this->load->model('backend/company_model', 'company', TRUE);
		$company_vo = array(
			'id' => $this->uri->segment(4)
		);
		$data['data'] = $this->company->company_data($company_vo);
		$data['type'] = type();
		$this->load->view('backend/header');
		$this->load->view('backend/company/company_edit', $data);		
	}
	function all_view(){
		$this->load->model('backend/company_model', 'company', TRUE);
		$config['base_url'] = base_url() . './index.php/backend/company/all_view';
		$config['total_rows'] = $this->company->company_count();
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
		$this->pagination->initialize($config);
		$data['datas'] = $this->company->all($config['per_page'], $this->uri->segment(4));
		$data['href'] = site_url('backend/company/all_view');
		$data['page'] = $this->pagination->create_links();
		$this->load->view('backend/header');
		$this->load->view('backend/company/company_all', $data);
	}
	function add(){
		$img = array();
		$this->load->model('backend/company_model', 'company', TRUE);
		$config['upload_path'] = './system/images/backend/uploads';
		$config['allowed_types'] = 'gif|jpg|png';
		$config['max_size'] = '2048';
		$config['max_width'] = '1024';
		$config['max_height'] = '768';
		$this->load->library('upload', $config);
		$result = $this->upload->do_upload('img');
		if ( ! $result['success'] ) {
			echo $this->upload->display_errors();	
		}
		$company_vo = array(
			'subject_eng' => $this->input->post('subject_eng'),
			'subject_chi' => $this->input->post('subject_chi'),
			'content_eng' => $this->input->post('content_eng'),
			'content_chi' => $this->input->post('content_chi'),
			'now' => now(),
			'img' => $result['data']['0']['file_name']
		);
		$data['content'] = $this->company->add($company_vo) ? 'Upload Success' : 'Faild';
		$data['href'] = site_url('backend/company/all_view');
		$this->load->view('backend/success', $data);
	}
	function del(){
		$this->load->model('backend/company_model', 'company', TRUE);
		$company_vo = array(
			'id' => $this->uri->segment(4)
		);
		$this->company->delete_data($company_vo);
		href($_SERVER['HTTP_REFERER']);
	}
	function edit(){
		$this->load->model('backend/company_model', 'company', TRUE);
		$id = $this->input->post('id');
		$data = $this->company->company_data(array('id' => $id));
		$config['upload_path'] = './system/images/backend/uploads';
		$config['allowed_types'] = 'gif|jpg|png';
		$config['max_size'] = '2048';
		$config['max_width'] = '1024';
		$config['max_height'] = '768';
		$this->load->library('upload', $config);
		$result = $this->upload->do_upload('img');
		if ( ! $result['success']) {
			echo $this->upload->display_errors();
		}
		$company_vo = array(
			'id' => $this->input->post('id'),
			'subject_eng' => $this->input->post('subject_eng'),
			'subject_chi' => $this->input->post('subject_chi'),
			'content_eng' => $this->input->post('content_eng'),
			'content_chi' => $this->input->post('content_chi'),
			'now' => now(),
			'img' => $result['data']['0']['file_name'] != NULL ? $result['data']['0']['file_name'] : $data['0']->img
		);
		$data['content'] = $this->company->edit($company_vo) ? 'Update Success' : 'Update Faild';
		$data['href'] = site_url('backend/company/all_view');
		$this->load->view('backend/success', $data);
	}
	function update_sort(){
		$this->load->model('backend/company_model', 'company', TRUE);
		foreach ( $this->input->post('no') as $key => $value ) {
				$this->company->sort($key, $value);
		}
		href($_SERVER['HTTP_REFERER']);
	}	
}