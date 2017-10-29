<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

$name_array = array('on_sale','hot','new','best','promote');
/* post 数据处理 */
$id = intval(get_args('id'));
$s = intval(get_args('s'));
$name = get_args('name');

if(!$id) {
	exit();
}

if(!in_array($name,$name_array)) {
	exit();
}

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_remind_user = $tablePreStr."remind_user";
$t_remind = $tablePreStr."remind";
$t_remind_info = $tablePreStr."remind_info";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "update `$t_goods` set is_".$name."='$s' where goods_id='$id'";

if($dbo->exeUpdate($sql)) {
	echo $s ? 'yes' : 'no';

	// 发送提醒
	if($name=='on_sale' && !$s) {
		dbtarget('r',$dbServs);
		
		include("../foundation/module_remind.php");
		$remind_id = 2;

		// 获取提醒信息
		$reminds = get_remind($dbo,$t_remind,$remind_id);
		if(!$reminds['enable']) {
			exit;
		}
		$remind_tpl = $reminds['remind_tpl'];

		// 获取用户id
		$sql = "select shop_id,goods_name from $t_goods where goods_id='$id'";
		$goods_row = $dbo->getRow($sql);
		if(!$goods_row){
			exit;
		}
		$user_id = $goods_row['shop_id'];

		// 获取用户的提醒设置
		$row = get_remind_user($dbo,$t_remind_user,$user_id,$remind_id);
		if(!$row) {
			exit;
		}
		
		$nowtime = $ctime->long_time();
		$remind_info = remind_info_replace($remind_tpl,array('goods'=>$goods_row['goods_name'],'time'=>$nowtime));
		if($row['site']) {
			$array = array(
				'user_id' => $user_id,
				'remind_info' => $remind_info,
				'remind_time' => $nowtime,
			);
			insert_remind_info($dbo,$t_remind_info,$array);
		} elseif($row['mail']) {
			
		} elseif($row['im']) {
			
		} elseif($row['mobile']) {
			exit;
		}
	}
}
?>