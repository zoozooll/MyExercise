<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("m/toolsbox/proxy.php");
require("../foundation/module_admin_logs.php");

//权限管理
$right=check_rights("download_exe");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
//数据表定义区
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('w',$dbServs);
$dbo=new dbex;

//语言包引入
$t_langpackage=new toollp;
$er_langpackage=new errorlp;
$tool_id=get_args('id');
if($tool_id==''){
	echo '<script type="text/javascript">alert("'.$t_langpackage->t_id_wrong.'");window.history.go(-1);</script>';exit;
}

$serv_url=act_substitue("tools","&folder=".$tool_id);//远程工具箱代理地址
$client_tool_root='toolsBox';//本地工具箱路径

$xmlDom=new DOMDocument;
$xmlDom->load($client_tool_root.'/tool.xml');
if(!$xmlDom->validate()){//检测本地工具箱规范
	echo '<script type="text/javascript">alert("'.$t_langpackage->t_not_stand.'");window.history.go(-1);</script>';exit;
}

$tool_client=file_get_contents($client_tool_root."/tool.xml");//取得本地的工具箱列表
if(!$tool_client){
	echo '<script type="text/javascript">alert("'.$t_langpackage->t_not_find.'");window.history.go(-1);</script>';exit;
}

$tool_list=file_get_contents($serv_url);//取得代理返回的数据

if($tool_list==''){
	echo '<script type="text/javascript">alert("'.$t_langpackage->t_false_connect.'");window.history.go(-1);</script>';exit;
}

if(preg_match("/error\_\d/",$tool_list)){
	echo '<script type="text/javascript">alert("'.$er_langpackage->{"er_".$tool_list}.'");window.history.go(-1);</script>';exit;
}

preg_match_all("/<programList>[\w\.]+<\/programList>/",$tool_list,$download_file_array);//取得要下载的文件名


foreach($download_file_array[0] as $rs){//文件下载
	$file_url=str_replace("</programList>","",$rs);
	$file_url=str_replace("<programList>","",$file_url);
	$download_content=file_get_contents($serv_url."&fileName=".$file_url);
	if(!file_exists($client_tool_root."/".$tool_id)){
		mkdir($client_tool_root."/".$tool_id);
	}
	$f_ref=fopen($client_tool_root."/".$tool_id."/".$file_url,'w+');
	$write_num=fwrite($f_ref,$download_content);
	if($write_num==0){
		echo '<script type="text/javascript">alert("'.$t_langpackage->t_not_stand.'");window.history.go(-1);</script>';exit;
	}
	if(preg_match("/\.sql/",$file_url)){
		$dbo = new dbex;
		dbtarget('w',$dbServs);
		$sql_str=file_get_contents($client_tool_root."/".$tool_id."/".$file_url);
		$queries = explode(";\n", trim($sql_str));
		foreach($queries as $query){
			$query=str_replace("isns_",$tablePreStr,$query);
			if(!$dbo->exeUpdate($query)){
				$result='<font color="red">'.$t_langpackage->t_sql_false.'</font>';exit;
			}
		}
	}
}
/** 添加log */
$admin_log ="下载工具";
admin_log($dbo,$t_admin_log,$admin_log);

//添加到本地xml配置文件内
$tmp_dom=new DomDocument;
$tmp_dom->loadXML($tool_list);
$xmlDom->getElementsByTagName('toolbox')->item(0)->appendChild($xmlDom->importNode($tmp_dom->getElementsByTagName('tool_item')->item(0),true));
if(!$xmlDom->validate()){
	echo '<script type="text/javascript">alert("'.$t_langpackage->t_isset_tool.'");window.history.go(-1);</script>';exit;
}
$xmlDom->save($client_tool_root."/tool.xml");
echo '<script type="text/javascript">alert("'.$t_langpackage->t_success.'");window.history.go(-1);</script>';

?>