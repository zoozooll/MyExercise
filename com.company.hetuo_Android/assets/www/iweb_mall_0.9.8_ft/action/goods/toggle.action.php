<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_goods.php");

$name_array = array('on_sale','hot','new','best','promote');
/* post 数据处理 */
$id = intval(get_args('id'));
$s = intval(get_args('s'));
$name = short_check(get_args('name'));

if(!$id) {
	exit();
}

if(!in_array($name,$name_array)) {
	exit();
}
//数据表定义区
$t_goods = $tablePreStr."goods";

if($s==1 && $name!='on_sale'){
	//定义写操作
	dbtarget('r',$dbServs);
	$dbo=new dbex;
	
	$is_num=get_goods_isname_num($dbo,$t_goods,$shop_id,$name);
	if($name=='best'){
		$actId=4;
	}elseif($name=='promote'){
		$actId=5;
	}elseif($name=='new'){
		$actId=6;
	}elseif($name=='hot'){
		$actId=7;
	}
	if(!isset($user_privilege[$actId])) {
		$user_privilege[$actId] = 0;
	}
	if($is_num>=$user_privilege[$actId]){
		echo -1;
		exit;
	}
}

//$sql="select * from $t_goods "

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_goods` set is_".$name."='$s' where goods_id='$id' and shop_id='$shop_id'";

if($dbo->exeUpdate($sql)) {
	echo $s ? 'yes' : 'no';
}
?>