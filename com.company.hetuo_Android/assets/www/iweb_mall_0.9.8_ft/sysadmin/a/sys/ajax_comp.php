<?php
require("../foundation/ftpl_compile.php");
require("../foundation/module_admin_logs.php");

//获得方案名
$loc = short_check(get_args('tpl'));
$mode = strtolower(short_check(get_args('mode')));

if(!$loc) { exit('-1'); }
if(!$mode) { exit('-1'); }

//数据表定义
$t_settings=$tablePreStr."settings";
$t_admin_log = $tablePreStr."admin_log";

//数据库连接
$dbo=new dbex;
dbtarget('w',$dbServs);

$sql="REPLACE INTO `$t_settings` (variable,`value`) VALUES('template_mode','$mode')";
$dbo->exeUpdate($sql);

function list_child_file($local){
	$ref=opendir("../templates/".$local);
	while($tp_dir=readdir($ref)){
		if(!preg_match("/^\./",$tp_dir)){
			if(filetype("../templates/".$local."/".$tp_dir)=="dir"){
				list_child_file($local."/".$tp_dir);
			}
			if(filetype("../templates/".$local."/".$tp_dir)=="file"){
				global $loc;
				global $mode;
				$show_local=$local.'/'.$tp_dir;
				$show_local=preg_replace("/$loc\//","",$show_local);
				if($mode=='service') {
					tpl_engine($loc,$show_local,0,'service');
				} else {
					tpl_engine($loc,$show_local);
				}
			}
		}
	}
}

list_child_file($loc);
/** 添加log */
$admin_log ="更新模板运行模式";
admin_log($dbo,$t_admin_log,$admin_log);

echo "1";
?>