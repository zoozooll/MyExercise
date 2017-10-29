<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//数据表定义区
$t_keywords_count = $tablePreStr."keywords_count";

//定义操作
dbtarget('r',$dbServs);
$dbo = new dbex;
$sql = "select id,keywords from $t_keywords_count order by `month` desc";
$key_rs = $dbo->getRs($sql);

$keyword = '';
$str_len = 0;
foreach($key_rs as $k=>$v) {
	$str_len += strlen($v['keywords']);
	if($str_len > 50) { break; }
	$keyword .= $v['keywords'].'___'.urlencode($v['keywords'])." ";
}
$keyword = preg_replace("/([^ ]+)___([^ ]+)/","<a href='search.php?k=$2'>$1</a>",$keyword);
$file = $webRoot.'cache/tag_keyword.js';
$data = 'document.write("'.$keyword.'");';
if(!file_put_contents($file,$data)) {
	exit("-1");
}
?>