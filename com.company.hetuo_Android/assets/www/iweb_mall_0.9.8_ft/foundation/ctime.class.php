<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

class time_class {
	public $format;
	public $time_difference = 0;

	function __construct($timezone=8) {
		$this->time_difference = $timezone*3600;
	}

	//参数定制的时间
	function custom($format='Y-m-d H:i:s') {
		return date($format,time()+$this->time_difference);
	}

	//完整时间
	function long_time() {
		return date("Y-m-d H:i:s",time()+$this->time_difference);
	}

	//短时间
	function short_time() {
		return date("Y-m-d",time()+$this->time_difference);	
	}

	//时间戳
	function time_stamp() {
		return time()+$this->time_difference;
	}
}
?>