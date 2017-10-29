<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_remind_user = $tablePreStr."remind_user";

$sql="delete from $t_remind_user where user_id=$user_id";
//echo $sql;
$dbo->exeUpdate($sql);

$sql="insert into $t_remind_user(site,im,mail,mobile,remind_id,user_id) values";
foreach($_POST as $k=>$v){
	if(!empty($v['site'])){
		$site=$v['site'];
	}else{
		$site=0;
	}
	if(!empty($v['im'])){
		$im=$v['im'];
	}else{
		$im=0;
	}
	if(!empty($v['mail'])){
		$mail=$v['mail'];
	}else{
		$mail=0;
	}
	if(!empty($v['mobile'])){
		$mobile=$v['mobile'];
	}else{
		$mobile=0;
	}
	
	$sql.="($site,$im,$mail,$mobile,$k,$user_id),";


}
$sql=substr($sql,0,-1);    
//echo $sql;
if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_upd_suc,'-1');
} else {
	action_return(0,$m_langpackage->m_upd_lose,'-1');
}
?>