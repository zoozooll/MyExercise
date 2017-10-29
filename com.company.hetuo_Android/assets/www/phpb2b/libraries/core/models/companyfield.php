<?php
class Companyfields extends PbModel {
 	var $name = "Companyfield";

 	function Companyfields()
 	{
 		parent::__construct();
 	}

	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Companyfields();
		}
		return $instance[0];
	}
}
?>