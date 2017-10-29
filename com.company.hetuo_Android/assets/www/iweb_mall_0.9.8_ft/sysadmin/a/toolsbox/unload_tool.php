<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("../foundation/module_admin_logs.php");
//语言包引入
$t_langpackage=new toollp;

//数据表定义区
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('w',$dbServs);
$dbo=new dbex;

//权限管理
$right=check_rights("tools_Uninstall");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
$tool_id=get_args('id');
$client_url="toolsBox/tool.xml";
if($tool_id==''){
	echo $t_langpackage->t_code_wrong;
	exit;
}
$xml_client=new DOMDocument;
$xml_client->load("$client_url");
$xpath=new DOMXpath($xml_client);
$element = $xpath->query("//tool_item[@id='$tool_id']");
$file_list=$element->item(0)->getElementsByTagName("programList");

foreach($file_list as $rs){
	$file_url=$rs->nodeValue;
	unlink('toolsBox/'.$tool_id."/".$file_url);
}
@rmdir('toolsBox/'.$tool_id);
$xml_client->getElementsByTagName('toolbox')->item(0)->removeChild($element->item(0));
$write_num=$xml_client->save($client_url);
if($write_num>0){
	/** 添加log */
	$admin_log ="卸载工具";
	admin_log($dbo,$t_admin_log,$admin_log);

	echo '<script type="text/javascript">alert("'.$t_langpackage->t_unload_sucess.'");window.history.go(-1);</script>';
}else{
	echo '<script type="text/javascript">alert("'.$t_langpackage->t_unload_false.'");window.history.go(-1);</script>';
}
?>
