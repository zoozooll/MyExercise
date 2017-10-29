<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, PHPB2B.com. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 378 $
 */
if (!class_exists("L10n")) {
	require("l10n.class.php");
}

class PbController extends PbObject
{
	var $params;
	var $default_headers = array(
		'Name' => 'The Name',
		'URI' => 'The URI',
		'Description' => 'Description',
		'Author' => 'Author',
		'AuthorURI' => 'Author URI',
		'Version' => 'Version',
		);

	function generateList($result)
	{
		$return  = null;
		foreach ($result as $key=>$val) {
			$return[$val['OptionId']] = $val['OptionName'];
		}
		return $return;
	}

	function stripWhitespace($str) {
		$r = preg_replace('/[\n\r\t]+/', '', $str);
		return preg_replace('/\s{2,}/', ' ', $r);
	}
	
	function getSkinData( $theme_file ) {
		$theme_data = $this->getFileData( $theme_file, $this->default_headers);
		if ( $theme_data['Author'] == '' ) {
			$theme_data['Author'] = 'Anonymous';
		}
		return $theme_data;
	}
	
	function getFileData( $file, $default_headers) {
		if(file_exists($file)){
			$fp = fopen( $file, 'r' );
			$file_data = fread( $fp, 8192 );
			fclose( $fp );
		}else{
			return false;
		}
		$all_headers = $default_headers;

		foreach ( $all_headers as $field => $regex ) {
			preg_match( '/' . preg_quote( $regex, '/' ) . ':(.*)$/mi', $file_data, ${$field});
			if ( !empty( ${$field} ) )
			${$field} = trim(preg_replace("/\s*(?:\*\/|\?>).*/", '', ${$field}[1] ));
			else
			${$field} = '';
		}

		$file_data = compact( array_keys( $all_headers ) );

		return $file_data;
	}
	
	function rewriteUrl($model, $model_url, $id, $name = null)
	{
		global $rewrite_able, $rewrite_compatible;
 		if ($rewrite_able) {
 			if ($rewrite_compatible && !empty($name)) {
 				return $model."/".rawurlencode($name)."/";
 			}else{
 				return $model."/".$id."/";
 			}
 		}else{
 			if ($rewrite_compatible && !empty($name)) {
 				return $model_url."?name=".rawurlencode($name);
 			}else{
 				return $model_url."?id=".$id;
 			}
 		}
	}
}
?>