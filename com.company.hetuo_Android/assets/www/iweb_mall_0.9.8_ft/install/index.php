<?php
define('iPATH',substr(dirname(strtr(__FILE__,'\\','/')), 0,-8)."/");
$IWEB_SHOP_IN = true;

require("../foundation/fstring.php");

$lockfile = '../cache/install.lock';
if(file_exists($lockfile)) {
	echo '警告!您已经安装过iweb_shop&mall<br>
		为了保证数据安全，请立即手动删除 install 文件夹<br>
		如果您想重新安装iweb_shop&mall，请删除 cache/install.lock 文件，再运行安装文件';
	exit;
}

// 防止 PHP 5.1.x 使用时间函数报错
function_exists('date_default_timezone_set') && date_default_timezone_set("PRC");
unset($_ENV,$HTTP_ENV_VARS,$_REQUEST,$HTTP_POST_VARS,$HTTP_GET_VARS,$HTTP_POST_FILES,$HTTP_COOKIE_VARS,$HTTP_SESSION_VARS,$HTTP_SERVER_VARS);
unset($GLOBALS['_ENV'],$GLOBALS['HTTP_ENV_VARS'],$GLOBALS['_REQUEST'],$GLOBALS['HTTP_POST_VARS'],$GLOBALS['HTTP_GET_VARS'],$GLOBALS['HTTP_POST_FILES'],$GLOBALS['HTTP_COOKIE_VARS'],$GLOBALS['HTTP_SESSION_VARS'],$GLOBALS['HTTP_SERVER_VARS']);

if (ini_get('register_globals')){
	isset($_REQUEST['GLOBALS']) && die('发现试图覆盖 GLOBALS 的操作');
	// Variables that shouldn't be unset
	$noUnset = array('GLOBALS', '_GET', '_POST', '_COOKIE','_SERVER', '_ENV', '_FILES');
	$input = array_merge($_GET, $_POST, $_COOKIE, $_SERVER, $_FILES, isset($_SESSION) && is_array($_SESSION) ? $_SESSION : array());
	foreach ( $input as $k => $v ){
		if ( !in_array($k, $noUnset) && isset($GLOBALS[$k]) ) {
			$GLOBALS[$k] = NULL;
			unset($GLOBALS[$k]);
		}
	}
}
// Fix for IIS, which doesn't set REQUEST_URI
if ( empty( $_SERVER['REQUEST_URI'] ) ) {

	// IIS Mod-Rewrite
	if (isset($_SERVER['HTTP_X_ORIGINAL_URL'])) {
		$_SERVER['REQUEST_URI'] = $_SERVER['HTTP_X_ORIGINAL_URL'];
	}
	// IIS Isapi_Rewrite
	else if (isset($_SERVER['HTTP_X_REWRITE_URL'])) {
		$_SERVER['REQUEST_URI'] = $_SERVER['HTTP_X_REWRITE_URL'];
	}else{
		// Some IIS + PHP configurations puts the script-name in the path-info (No need to append it twice)
		if ( $_SERVER['PATH_INFO'] == $_SERVER['SCRIPT_NAME'] )
			$_SERVER['REQUEST_URI'] = $_SERVER['PATH_INFO'];
		else
			$_SERVER['REQUEST_URI'] = $_SERVER['SCRIPT_NAME'] . $_SERVER['PATH_INFO'];

		// Append the query string if it exists and isn't null
		if (isset($_SERVER['QUERY_STRING']) && !empty($_SERVER['QUERY_STRING'])) {
			$_SERVER['REQUEST_URI'] .= '?' . $_SERVER['QUERY_STRING'];
		}
	}
}

// Fix for PHP as CGI hosts that set SCRIPT_FILENAME to something ending in php.cgi for all requests
if ( isset($_SERVER['SCRIPT_FILENAME']) && ( strpos($_SERVER['SCRIPT_FILENAME'], 'php.cgi') == strlen($_SERVER['SCRIPT_FILENAME']) - 7 ) )
	$_SERVER['SCRIPT_FILENAME'] = $_SERVER['PATH_TRANSLATED'];

// Fix for Dreamhost and other PHP as CGI hosts
if (strpos($_SERVER['SCRIPT_NAME'], 'php.cgi') !== false)
	unset($_SERVER['PATH_INFO']);

// Fix empty PHP_SELF
$PHP_SELF = $_SERVER['PHP_SELF'];
if ( empty($PHP_SELF) ){
	$_SERVER['PHP_SELF'] = $PHP_SELF = preg_replace("/(\?.*)?$/",'',$_SERVER["REQUEST_URI"]);
}

if ( version_compare( '4.3', phpversion(), '>' ) ) {
	die( '您的服务器运行的 PHP 版本是' . phpversion() . ' 但 iweb_shop&mall 要求至少 4.3。' );
}

if ( !extension_loaded('mysql')){
	die( '您的 PHP 安装看起来缺少 MySQL 数据库部分，这对 iweb_shop&mall 来说是必须的。' );
}

require_once(iPATH.'install/common.php');
if ( get_magic_quotes_gpc() ) {
	$_GET    = stripslashes_deep($_GET);
	$_POST   = stripslashes_deep($_POST);
	$_COOKIE = stripslashes_deep($_COOKIE);
}
$_GET    = add_magic_quotes($_GET);
$_POST   = add_magic_quotes($_POST);
$_COOKIE = add_magic_quotes($_COOKIE);
$_SERVER = add_magic_quotes($_SERVER);

!$_SERVER['PHP_SELF'] && $_SERVER['PHP_SELF']=$_SERVER['SCRIPT_NAME'];
$_URI  = $_SERVER['PHP_SELF'].'?'.$_SERVER['QUERY_STRING'];
$isnsDIR=substr(dirname($_URI),0,-8);
$isnsURL= 'http://'.$_SERVER['HTTP_HOST'].$isnsDIR.'/';
$step = isset($_POST['step']) ? $_POST['step'] : '';
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>iweb_shop&mall - 安装向导</title>
<link href="css/install.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="head"></div>
<div class="main">
<form method="post" action="<?php echo $PHP_SELF;?>">
<?php
if ($step == '1') {
?>
<?php
}elseif (empty($step)) {
	$check=1;
	$no_write=$isnsDIR."程序根目录无法书写,请速将根目录属性设置为可写";
	$correct='*<font style="color:green;">ok</font>';
	$incorrect='*<font style="color:red;">no</font>';
	$uncorrect='<font style="color:red;">× 文件不存在请上传此文件</font>';
	$file_attribute='(包括本目录、子目录和文件)';
	$file_rw='*<font style="color:green;">读/写</font>';
	$file_rwd='*<font style="color:green;">读/写/删</font>';
	$file_attribute_1='*系统配置文件';
	$file_attribute_2='*附件目录';
	$file_attribute_3='*站点数据目录';
	$file_attribute_4='*uc_client数据目录';
	$w_check=array(
		'langpackage/',
		'templates/',
		'skin/',
		'plugins/',
		'uploadfiles/',
		'modules/',
		'cache/',
		'shop/',
		'brand.php',
		'index.php',
		'category.php',
		'login.php',
		'configuration.php',
		'search.php',
		'search_ad.php',
		'iweb_mini_lib/conf/dbconf.php'
	);
	if($fp=@fopen(iPATH.'test.txt',"wb")){
		$state=$correct;

		fclose($fp);
	} else{
		$state=$incorrect.$no_write;
	}
	$_count=count($w_check);
	for($i=0; $i<$_count; $i++){
		if(!file_exists(iPATH.$w_check[$i])){                //检查文件或目录是否存在
			$w_check[$i].= $uncorrect;
			$check=0;
		} elseif(is_writable(iPATH.$w_check[$i])){           //判断给定的文件名是否可写
			if(is_dir(iPATH.$w_check[$i])) {				//判断给定文件名是否是一个目录

				if(checkdirs(iPATH.$w_check[$i])) {
					$w_check[$i].= @$file_attribute;
					$w_check[$i].= $correct;
					$w_check[$i].= $file_rw;
					$w_check[$i].= $file_attribute_3;
				} else {
					$w_check[$i].=$incorrect."(检查您的目录及其下级目录)";
					$check=0;
				}
			} else {
				$w_check[$i].= $correct;
				$w_check[$i].= $file_rw;
				$w_check[$i].= $file_attribute_1;
			}
		} else {
			if(is_dir(iPATH.$w_check[$i])) {
				$w_check[$i].=$incorrect;
			$w_check[$i].= $file_rw;
				$w_check[$i].= $file_attribute_3;
			} else {
				$w_check[$i].=$incorrect;
			$w_check[$i].= $file_rw;
				$w_check[$i].= $file_attribute_1;
			}
			$check=0;
		}
	}
	$check && @unlink(iPATH.'test.txt');
?>
<div class="nav"></div>
<div class="top"></div>
<div class="center">
    	<div class="tips"><p><strong>夺彩互联网，创新IT动力</strong></p></div>
        <table class="list" width="100%">
              <tr>
                <th>名称</th>
                <th>所需权限属性</th>
                <th>说明</th>
                <th>检测结果</th>
              </tr>
               <?php foreach($w_check as $value){
               	$pieces = explode("*", $value);
              	?>
               	<tr>
                <td><?php echo $pieces[0]; ?></td>
                <td><?php echo $pieces[2]; ?></td>
                <td><?php echo $pieces[3]; ?></td>
                <td><?php echo $pieces[1]; ?></td>
               </tr>
               <?php }?>
         </table>
         <div class="lang">请选择安装过程中的界面语言：
         						<select name="lp">
                            	 　<option value="ft" selected="selected" >中文繁体</option>
								</select>
		</div>
         <div class="clear"></div>
         <input type="hidden" name="step" value="2" />
         <?php
			if(!$check){
		 ?>
     	 <input onclick='window.location="<?php echo $PHP_SELF;?>"' type='button' value='重新检查'>
     	  <?php }else{?>
         	<div class="agree"><input type="submit" class="button" value="接受授权协议，开始安装" /><span>请先认真阅读我们的<a href="protocol.html">《软件使用授权协议》</a></span></div>
          <?php }?>
         <div class="clear"></div>
   	 </div>
    <div class="bottom"></div>
</div>
    <?php
}elseif ($step == '2') {
?><div class="nav nav_2"></div>
	<div class="top"></div>
    <div class="center">
		<h3>设置系统信息</h3>
     	<table class="data_set">
     		<tr><th colspan="3"></th></tr>
              <tr>
                <td width="13%">web根目录</td>
                <td width="38%"><input type="text" class="setup_input" name="isnsurl" value="<?php echo $isnsURL?>" /></td>
                <td width="47%" class="lightcolor">一般不用修改</td>
              </tr>
        </table>
	 	<h3>设置数据库链接信息</h3>
     	<table class="data_set">
              <tr><th colspan="3"></th></tr>
              <tr>
                <td width="13%">数据库地址</td>
                <td width="38%"><input type="text" class="setup_input" name="dbhost" value="localhost" /></td>
                <td width="47%" class="lightcolor">数据库服务器地址，一般为localhost </td>
              </tr>
              <tr><th colspan="3"></th></tr>
              <tr>
                <td>数据库名称</td>
                <td><input type="text" class="setup_input" name="dbname" value="iwebmall" /></td>
                <input name="create" type="hidden" id="create" value="1"/>
                <td class="lightcolor">如果不存在，则自动被创建</td>
              </tr>
              <tr><th height="13" colspan="3"></th></tr>
              <tr>
                <td>数据库用户名</td>
                <td><input type="text" class="setup_input" name="dbuser" value="root" /></td>
                <td class="lightcolor">您的MySQL 用户名 </td>
              </tr>
              <tr><th colspan="3"></th></tr>
              <tr>
                <td>数据库密码</td>
                <td><input type="password" class="setup_input" name="dbpw" value="" /></td>
                <td class="lightcolor">...以及MySQL密码</td>
              </tr>
              <tr><th colspan="3"></th></tr>
              <tr>
                <td>数据表前缀</td>
                <td><input type="text" class="setup_input" name="tablepre" value="imall_" /></td>
                <td class="lightcolor">同一数据库安装多个iWeb产品时可改变默认前缀 </td>
              </tr>
              <tr><th colspan="3"></th></tr><tr><th colspan="3"></th></tr>
  			</table>
		<h3>设置管理员信息</h3>
        <table class="data_set"> <tr><th colspan="2"></th></tr>
                  <tr>
                    <td width="13%">管理员账户</td>
                    <td width="87%"><input type="text" class="setup_input" name="admin" value="admin" /></td>
                  </tr>
                  <tr><th colspan="2"></th></tr>
                  <tr>
                    <td>管理员密码</td>
                    <td><input type="text" class="setup_input" name="password" value="admin" /></td>
                  </tr>
                  <tr><th colspan="2"></th></tr><tr><th colspan="2"></th></tr>
		</table>
        <h3>是否安装测试数据&nbsp;<input type="checkbox" checked="checked" name="testdata" value="1"/></h3>
        <?php $lp = $_POST['lp']; ?>
		<input type="hidden" name="lp" value="<?php echo $lp; ?>" />
         <input type="hidden" name="step" value="3" />
         <div class="agree"><input type="submit" class="button" value="提交设置信息，开始创建数据库" /></div>
			<div class="clear"></div>
            </div>
         <div class="bottom"></div>
    </div>
    <?php
} elseif ($step == '3') {
	?>
	<div class="nav nav_3"></div>
<div class="top"></div>
<div class="center">
<?php
	if(trim($_POST['dbname']) == "" || trim($_POST['dbhost']) == "" || trim($_POST['dbuser']) == "" ){
?>
       <h3>请返回并确认所有选项均已填写.</h3>
       <div class="agree">
    <p>
      <input class="button" type="button" value="上一步" onclick="history.back(1)" />
    </p>
		</div>
		</div>
         <div class="bottom"></div>
    <?php
	} elseif(!@mysql_connect($_POST['dbhost'],$_POST['dbuser'],$_POST['dbpw'])) {
?>
    <h3>数据库不能连接.</h3>
 <div class="agree">
    <p align="right">
      <input class="button" type="button" value="上一步" onclick="history.back(1)" />
    </p>
		</div>
		</div>
         <div class="bottom"></div>
    <?php
	} elseif(!@mysql_select_db($_POST['dbname'])&&!$_POST['create']) {

?>
    <h3>数据库<?php echo $_POST['dbname'];?>不存在.</h3>
<div class="agree">
    <p align="right">
      <input class="button" type="button" value="上一步" onclick="history.back(1)" />
    </p>
 		</div>
		</div>
         <div class="bottom"></div>
    <?php
	} elseif(strstr($_POST['tablepre'], '.')) {
?>
   <h3>您指定的数据表前缀包含点字符，请返回修改.</h3>
<div class="agree">
    <p align="right">
      <input class="button" type="button" value="上一步" onclick="history.back(1)" />
    </p>
    </div>
		</div>
         <div class="bottom"></div>
    <?php
	} else {
?>
<div class="data_create">
    <?php
		$dbconffile=iPATH.'iweb_mini_lib/conf/dbconf.php';
		if(is_writeable($dbconffile)) {

			$dbhost 	= trim($_POST['dbhost']);
			$dbuser 	= trim($_POST['dbuser']);
			$dbpw 		= trim($_POST['dbpw']);
			$dbname 	= trim($_POST['dbname']);
			$dbprefix	= trim($_POST['tablepre']);

			$isnsURL	= trim($_POST['isnsurl']);
			$admin		= trim($_POST['admin']);
			$password	= trim($_POST['password']);

//			$testdata	= isset($_POST['testdata']) ? trim($_POST['testdata']) : 0;
			if(empty($password)){
				echo "<script> alert('管理员密码不能为空！');history.go(-1); </script>";
			}

			//配置文件静态化
			$config_content = file_get_contents($dbconffile);
			$update_arr = array(
				'host' => "'".$dbhost."'",
				'user' => "'".$dbuser."'",
				'pwd' => "'".$dbpw."'",
				'db' => "'".$dbname."'",
				'tablePreStr' => "'".$dbprefix."'",
			);
			$new_config_content = update_config_file($config_content,$update_arr);
			file_put_contents($dbconffile,$new_config_content);
		}
//---------------------------------------------------------------------
		$configfile=iPATH.'configuration.php';
		if(is_writeable($configfile)) {
			$isnsURL = trim($_POST['isnsurl']);
			$lp = trim($_POST['lp']);
			//配置文件静态化
			$config_content = file_get_contents($configfile);
			$update_arr = array(
				'baseUrl' => "'".$isnsURL."'",
				'langPackagePara' => "'".$lp."'",
			);

			$new_config_content = update_config_file($config_content,$update_arr);
			file_put_contents($configfile,$new_config_content);
		}
//------------------------------------------------------------------------------------------
    	mysql_query("set names 'UTF8'");
		if(!@mysql_select_db($dbname)&&$_POST['create']){
    		$database=addslashes($dbname);
    		if(version_compare(mysql_get_server_info(), '4.1.0', '>=')){
//		    	$DATABASESQL=$dbcharset=='gbk'?"DEFAULT CHARACTER SET gbk COLLATE gbk_chinese_ci":"DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci";
		    	$DATABASESQL="DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci";
    		}
    		mysql_query("CREATE DATABASE `$database` ".$DATABASESQL);
    		@mysql_select_db($database);
		}
		require_once ($dbconffile);
		$installSQL=iPATH.'docs/install.sql';
		!is_readable($installSQL)&&exit('数据库文件不存在或者读取失败');
		require_once(iPATH.'install/cdbex.class.php');
		$db = new dbex;
		runquery(openfile($installSQL),$tablePreStr,$isnsURL);
		$sql="INSERT INTO imall_admin_user(`admin_name`,`admin_password`,`add_time`) VALUES('$admin','".md5($password)."',NOW())";
		$sql=str_replace('imall_',$tablePreStr,$sql);
		$sqla="UPDATE `imall_settings` SET `value` = '$lp' WHERE CONVERT( `variable` USING utf8 ) = 'lp' LIMIT 1  ";
		$sqla=str_replace('imall_',$tablePreStr,$sqla);
//		$db->query($sqla);
		if(!$db->query($sqla)){
			echo '<li>没有选择语言!!</li>';
			exit;
		}
?>
 <ul>
 <?php
 		if(!$db->query($sql)){
			echo '<li>创建后台管理员失败！</li>';
			exit;
		}
//		$testdata = "";
$testdata	= isset($_POST['testdata']) ? trim($_POST['testdata']) : 0;
//		$testdata = $_POST['testdata'];

		/* 写入测试数据 */
		if($testdata) {
			$testdataSQL=iPATH.'docs/testdata.sql';
			runquery(openfile($testdataSQL),$tablePreStr,$isnsURL);
		}
 ?>
        <p>共创建了<?php echo $tablenum;?>个数据表.</p>
        </ul>
       </div>
       <div class="agree">
		<input type="hidden" name="step" value="4" />
		<input class="button" type="submit"  value="完成" />
       </div>
       <div class="clear"></div>
    </p>
    </div>
    <div class="bottom"></div>
<?php
	}
}elseif ($step == '4'){?>
	<div class="nav nav_4"></div>
	<div class="top"></div>
	<div class="center">
	<p style="display:none;">
	<?php
	/* 编译模板 */
	require("../foundation/ftpl_compile.php");
	$template_dir = dirfiles("../templates/default");
	foreach($template_dir as $value) {
		$value = str_replace("../templates/default/",'',$value);
		//模板编译成功显示
		tpl_engine('default',$value,0,"debug",true);
	}
	?>
	</p>
	</p>
		<div class="data_create">
       		<ul>
            	<li>恭喜，iWebMALL安装程序已经顺利执行完毕！</li>
            	<li>为了您的数据安全，请尽快删除整个 install 目录</li>
            </ul>
       </div>
	<?php
	/* 生成 asd js */
	@mkdir("../uploadfiles/asd/");
	@file_put_contents("../uploadfiles/asd/1.js","document.write('<a href=\"http://www.jooyea.net\"><img src=\"docs/images/asd/2009/12/28/2009122801533971.jpg\" width=\"960\" height=\"90\" alt=\"iwebshop\"></a>');");
	@file_put_contents("../uploadfiles/asd/3.js","document.write('<a href=\"http://www.jooyea.net\"><img src=\"docs/images/asd/2009/12/26/2009122609394762.jpg\" width=\"200\" height=\"120\" alt=\"广告\"></a>');");
	//文件锁
	file_put_contents('../cache/install.lock',"");
	?>
	<div class="agree">
		<div class="btn"><div class="btn_right"><a href="../">进入首页</a></div></div>
		<div class="btn"><div class="btn_right"><a href="../sysadmin/login.php">直接进入管理后台</a></div></div>
	    </div>
	</div>
   <div class="bottom"></div>
<?php
}
?>
  </form>
</div>

</body>
</html>
<?php
function runquery($sql,$tablePreStr,$isnsURL) {
	global  $db, $tablenum;
	$sql = str_replace("\r", "\n", str_replace('http://localhost/iweb_mall/',$isnsURL,str_replace('imall_',$tablePreStr,$sql)));
	$ret = array();
	$num = 0;
	foreach(explode(";\n", trim($sql)) as $query) {
		$queries = explode("\n", trim($query));
		foreach($queries as $query) {
			if(!isset($ret[$num]))
			{
				$ret[$num] = substr($query,0,2) == '/*' ? '' : $query;
			}
			else
			{
				$ret[$num] .= substr($query,0,2) == '/*' ? '' : $query;
			}
		}
		$num++;
	}
	unset($sql);
	foreach($ret as $query) {
		$query = trim($query);
		if($query) {
			if(substr($query, 0, 12) == 'CREATE TABLE') {
				preg_match("|CREATE TABLE (.*) \(  |i",$query, $name);flush();
				if($db->query($query)){
					echo '<li>创建表 '.$name[1].' ... <font color="#0000EE">成功</font><br /></li>';
					$tablenum++;
				} else {
					echo '<li>创建表 '.$name[1].'失败！</li>';
					exit;
				}

			} else {
				if($db->query($query)){
//					echo '<li>插入测试数据成功！<br /></li>';
				} else {
					echo '<li>插入测试数据失败！<br /></li>';
				}
			}
		}
	}

}

function checkdirs($dir) {
	$templist = scandir($dir);
	foreach($templist as $value) {
		if(substr($value,0,1)!='.') {
			if(!is_writeable($dir.'/'.$value)) {
				echo $dir.'/'.$value;
				return false;
			}
			if(is_dir($dir.'/'.$value)) {

				return checkdirs($dir.'/'.$value);
			}
		}
	}
	return true;
}

function dirfiles($dir) {
	$templist = scandir($dir);
	$dir_arr = array();
	foreach($templist as $value) {
		if(substr($value,0,1)!='.') {
			if(is_dir($dir.'/'.$value)) {
				$array = dirfiles($dir.'/'.$value);
				if($array) {
					$dir_arr = array_merge($dir_arr,$array);
				}
			} else {
				$dir_arr[] = $dir.'/'.$value;
			}
		}
	}
	return $dir_arr;
}
function showjsmessage($message)
{
	echo '<script type="text/javascript">showmessage(\''.addslashes($message).' \');</script>'."\r\n";
	flush();
	ob_flush();
}
?>
