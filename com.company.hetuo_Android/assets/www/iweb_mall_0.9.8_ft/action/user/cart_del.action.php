<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件


//语言包引入
$m_langpackage=new moduleslp;

//定义文件表
$t_cart = $tablePreStr."cart";
$t_goods = $tablePreStr."goods";

// 处理post变量
if(empty($_POST)){
	$cart_id = intval(get_args('id'));
}else{
	$goods=get_args('goods');
	$cart_id=implode(',',$goods);
}
$goods_id = is_array(get_args('goods_id'))?get_args('goods_id'):array(get_args('goods_id'));
//数据库操作
if ($user_id) {
	dbtarget('r',$dbServs);
	$dbo=new dbex();
	$sql = "select * from `$t_cart` where user_id='$user_id' and cart_id in($cart_id)";
	//echo $sql;
	//exit;
	$result = $dbo->getRs($sql);
	foreach ($result as $key=>$value){
		if (isset($_SESSION['cart'][$value['goods_id']])) {
			unset($_SESSION['cart'][$value['goods_id']]);
		}
		
	}
	if(!$result) {
		action_return(0,$m_langpackage->m_cartnotthis_goods);
	}
}elseif ((!$user_id)&&(!empty($goods_id))){
	foreach ($goods_id as $value){
		unset($_SESSION['cart'][$value]);
	}
}
//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();
$err_str=0;
if ($user_id) {
	foreach ($result as $key=>$value){
		$sql = "delete from `$t_cart` where user_id='$user_id' and cart_id ='{$value['cart_id']}'";
		if($dbo->exeUpdate($sql)) {
			if(!$dbo->exeUpdate("update `$t_goods` set goods_number=goods_number+{$value['goods_number']} where goods_id='{$value['goods_id']}' ")){
				$err_str+=$value['goods_id'];
			}
		}else{
			$err_str+=$key;
		}
	}
}
if ($err_str) {
	action_return(0,$m_langpackage->m_cartgoods_delfail,'-1');
}else {
	action_return(1,$m_langpackage->m_cartgoods_delsuceess);
}
exit;
?>