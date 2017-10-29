<?php
$xmlDom=new DomDocument;
$xmlDom->load('toolsBox/tool.xml');
$tool_item=$xmlDom->getElementsByTagName('tool_item');
$tools_box_array=array();
$content_index=array();
$content_act=array();
foreach($tool_item as $val){
	$content_index[]=$val->getElementsByTagName('contentIndex')->item(0)->nodeValue;
	$content_act[]="toolsBox/".$val->getElementsByTagName('contentIndex')->item(0)->nodeValue."/".$val->getElementsByTagName('contentAct')->item(0)->nodeValue;
}
$tools_box_array=array_combine($content_index,$content_act);
?>