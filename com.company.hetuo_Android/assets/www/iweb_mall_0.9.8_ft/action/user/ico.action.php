<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_users.php");

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_users = $tablePreStr."users";

$cupload = new upload();
$cupload->set_dir("uploadfiles/userico/","{y}/{m}/{d}");
$setthumb = array(
	'width' => array(100),
	'height' => array(100),
	'name' => array('thumb')
);
$cupload->set_thumb($setthumb);
$file = $cupload->execute();
if($file) {
	if($file[0]['flag']==1) {
		$post['user_ico'] = $file[0]['dir'].$file[0]['thumb'];
		if(update_user_info($dbo,$t_users,$post,$user_id)) {
			if($im_enable) {
				$sql1 = "UPDATE chat_users SET u_ico='".$post['user_ico']."' WHERE uid=$user_id";
				$dbo->exeUpdate($sql1);
				$sql2 = "UPDATE chat_pals SET pals_ico='".$post['user_ico']."' WHERE pals_id=$user_id";
				$dbo->exeUpdate($sql2);
			}
			action_return(1);
		}
	}
}
action_return(0,$m_langpackage->m_ico_set_lose,'-1');
?>