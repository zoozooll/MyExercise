<?php 
/*
	class.Meta.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class Meta {
	public static function getAdmin($name){
		$rs['Title'] = "【后台管理--登录为 : $name 】";
		$rs['Description'] = "";
		$rs['Keywords'] = "";
		$rs['Content'] = "  ";
		return $rs;
	}
	public static function getLogin(){
		$rs['Title'] = "【后台管理--登录】";
		$rs['Description'] = "";
		$rs['Keywords'] = "";
		$rs['Content'] = "  ";
		return $rs;
	}
}
?>