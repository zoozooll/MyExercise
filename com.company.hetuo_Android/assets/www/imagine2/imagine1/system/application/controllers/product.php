<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 02:38 05/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Product extends Controller{
	var $folder = NULL;
	function Product(){
		parent::Controller();
		$this->folder = $this->uri->segment(3) == NULL ? 'chi' : $this->uri->segment(3);
	}
	
	function productview(){
		$this->load->model('backend/product_model','product', TRUE);
		$product_vo = array (
			'id' => $this->uri->segment(4)
		);
		$this->product->product_click_update($product_vo);
		$data['data'] = $this->product->product_data($product_vo);
		$data['type'] = $this->product->product_type($data['data'][0]->id);
		$data['title'] = product_title(array('string' => $data['type'][0], 'language' => $this->folder));
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/productview", $data);
	}
	
	function product_type(){
		$this->load->model('backend/product_model','product', TRUE);
		$data['title'] = product_title(array('string' => $this->uri->segment(4), 'language' => $this->folder));
		$data['select'] = $this->uri->segment(4);
		$config['per_page'] = '8';
		$product_vo = array (
			'type' => $this->uri->segment(4),
			'num' => $config['per_page'],
			'offset' => $this->uri->segment(5)
		);
		$result = $this->product->product_type_all($product_vo);
		$config['base_url'] = base_url() . "./index.php/product/product_type/{$this->folder}/{$this->uri->segment(4)}";
		$config['total_rows'] = $result['count'];
		$config['num_links'] = '10';
		$config['uri_segment'] = '5';
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
		$data['datas'] = $result['datas'];
		$data['page'] = $this->pagination->create_links();
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/prolist_type", $data);
	}
	function product_search(){
		$product_vo = array (
			'condition' => 'subject_'.$this->uri->segment(3),
			'keyword' => $this->input->post('keyword') 
		);
		$this->load->model('backend/product_model','product', TRUE);
		$data['datas'] = $this->product->product_data_array($product_vo);
		$this->load->view("{$this->folder}/header");
		$this->load->view("{$this->folder}/pro_serach", $data);
	}
}