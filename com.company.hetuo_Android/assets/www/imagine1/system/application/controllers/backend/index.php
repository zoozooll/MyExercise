<?php
/*+-------
-*IDE	UltraEdit-32 
-*Author Billy 
-*Time GMT+8 03:03 04/10/2010
-*Msn	billykwong@live.cn
-+--------*/
class Index extends Controller{
	function Index(){
		parent::Controller();
		if ( !$this->session->userdata('userdata') ) {
				href(base_url().'index.php/backend/user/login_view');
		}
	}
	function outlook(){
		$this->load->view('backend/outlook');
	}
	function dummy(){
		$this->load->view('backend/dummy');
	}
	function welcome(){
		$this->load->view('backend/index');
	}
	function logout(){
		$this->session->unset_userdata('userdata');
		echo "<script>parent.location.reload();</script>";
	}
}
?>