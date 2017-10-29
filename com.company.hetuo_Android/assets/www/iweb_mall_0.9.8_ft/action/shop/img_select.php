<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
/* post 数据处理 */
$ii = intval(get_args('i'));

//数据表定义区
$t_img_size = $tablePreStr."img_size";

//定义写操作
dbtarget('r',$dbServs);
$dbo=new dbex;

$sql="select * from $t_img_size where uid=$shop_id ";

//echo $sql;
$img_list= $dbo->getRs($sql);
$str= "<table><tr>";
$i=1;
foreach($img_list as $val){
	if($ii==2){
		$str.= "<td><img ondblclick=\"parent.AddContentImg('".$val['img_url']."','1')\" src='".$val['img_url']."' height='100' width='100'/></td>";
	}else
	if($ii==1){
		$str.= "<td><img ondblclick=\"showimg('".$val['img_url']."')\" src='".$val['img_url']."' height='100' width='100'/></td>";
	}

	if($i%6==0){
		$str.="</tr><tr>";
	}
	$i++;
}
	$str.="</table>";
echo $str;

?>