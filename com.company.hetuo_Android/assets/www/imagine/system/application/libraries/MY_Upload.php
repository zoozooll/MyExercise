<?php
class MY_Upload extends CI_Upload {
	function MY_Upload($props = array ()) {
		parent :: CI_Upload($props);
	}
	function do_upload($field = 'userfile') {
		$result = array('success' => FALSE, 'data' => array());
		if (isset ($_FILES[$field]) and is_array($_FILES[$field]['error'])) {
			for ($i = 0; $i < count($_FILES[$field]['error']); $i++) {
				$pseudo_field_name = '_psuedo_' . $field . '_' . $i;
				$_FILES[$pseudo_field_name] = array (
					'name' => $_FILES[$field]['name'][$i],
					'size' => $_FILES[$field]['size'][$i],
					'type' => $_FILES[$field]['type'][$i],
					'tmp_name' => $_FILES[$field]['tmp_name'][$i],
					'error' => $_FILES[$field]['error'][$i]
				);
				$result['success'] = $_FILES[$field]['name'][$i] != NULL ? parent :: do_upload($pseudo_field_name) : '0';
				$result['data'][$i] = $_FILES[$field]['name'][$i] != NULL ? parent :: data() : NULL;
				}
		} else {
			$result['success'] = parent :: do_upload($field);
		}
		return $result;
	}
}