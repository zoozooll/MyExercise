<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//require("foundation/fsession.php");
//引入语言包
$i_langpackage=new indexlp;

/* 定义文件表 */
$t_shop_inquiry = $tablePreStr."shop_inquiry";
$t_users = $tablePreStr."users";

$user_id=get_sess_user_id();

$post['shop_id'] = intval(get_args('shop_id'));
$post['goods_id'] = intval(get_args('goods_id'));
$post['goods_name'] = short_check(get_args('goods_name'));
$post['user_id'] = intval($user_id);
$post['name'] = short_check(get_args('user_truename'));
$post['email'] = short_check(get_args('user_email'));
$post['mobile'] = short_check(get_args('user_mobile'));
$post['telphone'] = short_check(get_args('user_telphone'));
$post['title'] = short_check(get_args('title'));
$post['content'] = long_check(get_args('content'));
$post['add_time'] = $ctime->long_time();
$post['add_ip'] = $_SERVER['REMOTE_ADDR'];
$shop_id=$post['shop_id'];

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$item_sql = get_insert_item($post);
$sql = "insert into `$t_shop_inquiry` $item_sql";
$dbo->exeUpdate($sql);

$sql="update $t_users set inquiry_num=inquiry_num+1 where user_id=$shop_id ";
$dbo->exeUpdate($sql);

$nav_selected='1';
?>