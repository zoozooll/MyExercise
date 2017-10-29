<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//语言包引入
$pl_langpackage=new pluginslp;

dbtarget('w',$dbServs);
$dbo=new dbex;

$check_sql=true;
//安装SQL函数,参数$force控制是否强制安装
function install_sql($sqls,$force=false)
{
	$flag=true;
	global $plugin;
	global $dbo;
	if(is_array($sqls))
	{
		if($force)
		{
			foreach($sqls as $sql)
			{
				$sql= preg_replace("/(create|drop|insert)([^`]+`)([a-zA-Z]*_)?(\w+)(`.*)/i","$1$2{$plugin}_$4$5",$sql);
				if($dbo->exeUpdate($sql)) echo "<div style='color:#009900'>".htmlspecialchars($sql).$pl_langpackage->pl_success."</div>";
				else
				{
					echo "<div style='color:#FF0000'>".htmlspecialchars($sql).$pl_langpackage->pl_failure."</div>";
					$flag=false;
				}
			}
		}
		else
		{
			foreach($sqls as $sql)
			{
				if($dbo->exeUpdate($sql)) echo "<div style='color:#009900'>".htmlspecialchars($sql).$pl_langpackage->pl_success."</div>";
				else
				{
					echo "<div style='color:#FF0000'>".htmlspecialchars($sql).$pl_langpackage->pl_failure."</div>";
					$flag=false;
				}
			}
		}
	}
	return $flag;
}

//提取SQL文件函数,
function refine($filename,&$create_tables=array(),&$drop_tables=array())
{
	$lines=file($filename);
	$lines[0]=str_replace(chr(239).chr(187).chr(191),"",$lines[0]);//去除BOM头
	$flage=true;
	$sqls=array();
	$sql="";
	foreach($lines as $line)
	{
		$line=trim($line);
		$char=substr($line,0,1);
		if($char!='#' && strlen($line)>0)
		{
			$prefix=substr($line,0,2);
			switch($prefix)
			{
				case '/*':
				{
				$flage=(substr($line,-3)=='*/;'||substr($line,-2)=='*/')?true:false;
				break 1;
				}
				case '--': break 1;
				default :
				{
					if($flage)
					{
						$sql.=$line;
						if(substr($line,-1)==";")
						{
							$sqls[]=$sql;
							$sql="";
						}
						if(stristr($line,'create'))
						{
							$tem=preg_replace("/CREATE\s+TABLE[^']*`(\w+)`.*/i","$1",$line);
							$create_tables[$tem]=$tem;
						}
						if(stristr($line,'drop'))
						{
							$tem=preg_replace("/DROP\s+TABLE[^']*`(\w+)`.*/i","$1",$line);
							$drop_tables[$tem]=$tem;
						}
					}
					if(!$flage)$flage=(substr($line,-3)=='*/;'||substr($line,-2)=='*/')?true:false;
				}
			}
		}
	}
	return $sqls;
}

$plugin=get_args('path');
$sqlfile=get_args('sql');
$force=get_args('force');
$type=get_args('type');
if(is_null($force))$force=false;
else $force=true;
$sqlpath=$plugin.'/'.$sqlfile;
if($sqlfile)
{
	$sqls=refine($webRoot."plugins/$sqlpath",$create_tables,$drop_tables);
	$dbo=new dbex;
	dbtarget('r',$dbServs);
	$sql="show tables";
	$result = $dbo -> getRs($sql);
	foreach($result as $row)
	{

		if(isset($drop_tables[$row[0]]) || isset($create_tables[$row[0]]))
		{
			$check_sql=false;
			break 1;
		}
	}
	if($force)
	{
		if(install_sql($sqls,$force))
		{
			$config=$webRoot."plugins/$plugin/config.php";
			$str=file_get_contents($config);
			$str=preg_replace("/table_prefix=[^;]*;/","table_prefix='{$plugin}_';",$str);
			file_put_contents($config,$str);
			$tables="";
			foreach($create_tables as $table)
			{
				$tables.=preg_replace("/^([a-zA-Z]*_)?(\w+)$/i","{$plugin}_$2",$table).";";
			}
			if(substr($tables,-1)==';')$tables=substr($tables,0,-1);
			file_put_contents($webRoot."plugins/$plugin/plugin_create_tables.php",$tables);
			//file_put_contents(dirname(__file__)."/../plugins/$plugin/plugin_create_tables.php","{$plugin}_".implode(";{$plugin}_",$create_tables));
			echo "<div style='text-align:center'>".$pl_langpackage->pl_database_prepared_succ."</div><script>parent.diag_install.URL='m.php?app=plugin_install_3&type={$type}&path={$plugin}';</script>";
		}
		else
		{
			echo "<div style='text-align:center'>".$pl_langpackage->pl_database_prepared_fail."</div>";
		}
	}
	else
	{
		if($check_sql)
		{
			if(install_sql($sqls))
			{
				file_put_contents($webRoot."plugins/$plugin/plugin_create_tables.php",implode(";",$create_tables));
				echo "<div style='text-align:center'>".$pl_langpackage->pl_database_prepared_succ."<script>parent.diag_install.URL='m.php?app=plugin_install_3&type={$type}&path={$plugin}';</script></div>";
			}
			else
			{
				echo "<div style='text-align:center'>".$pl_langpackage->pl_database_prepared_fail."</div>";
			}
		}
		else
		{
			echo"<div style='text-align:center;color:red'>";
			foreach($result as $row)
			{
				if(isset($drop_tables[$row[0]]) || isset($create_tables[$row[0]])) echo $pl_langpackage->pl_table." $row[0]".$pl_langpackage->pl_already_exists."<br/>";
			}
			echo $pl_langpackage->pl_not_install_plugins."</div>";
			$config=$webRoot."plugins/$plugin/config.php";
			if(file_exists($config))
			{
				require_once($config);
				if(isset($table_prefix)) echo "<div style='text-align:center'>".$pl_langpackage->pl_forced_install."<script>parent.diag_install.URL='m.php?app=plugin_install_2&type={$type}&path={$plugin}&sql={$sqlfile}&force=true';</script></div>";
				else  echo "<div style='text-align:center'>".$pl_langpackage->pl_forced_install_fail."</div>";
			}
			else
			{
				echo "<div style='text-align:center'>".$pl_langpackage->pl_forced_install_fail."</div>";
			}
		}
	}
}
else
{
	header("Location: m.php?app=plugin_install_3&type={$type}&path=".$plugin);
}
?>
