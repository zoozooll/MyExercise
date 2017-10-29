<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$a_langpackage=new adminlp;

/* post 数据处理 */
$id = intval(get_args('id'));
$type = intval(get_args('type'));
$group_id = get_args('group_id');


//数据表定义区
$t_admin_group=$tablePreStr."admin_group";
$t_admin_user = $tablePreStr."admin_user";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "select * from `$t_admin_group` where del_flg=0 ";
$result_group = $dbo->getRs($sql);

if($type==1){
	$select="";
	$select .= '<select name="group" onchange="update_group(this.value)" >';
	$select .= '<option value="">'.$a_langpackage->a_select_admin_group.'</option>';
	foreach($result_group as $key => $value){
		$select .= '<option value="'.$value['id'].'">'.$value['group_name'].'</option>';
	}
	echo $select;
}
if($type==2){
	$sql = "update `$t_admin_user`  set group_id='$group_id' where admin_id='$id'";
	if($dbo->exeUpdate($sql)){
		$sql = "select * from `$t_admin_group` where id='$group_id'";
		$result_group = $dbo->getRow($sql);
		echo $result_group['group_name'];
	}

}
?>