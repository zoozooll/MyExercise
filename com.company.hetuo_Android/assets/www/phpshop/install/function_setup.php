<?php
function setpOne() {
?>
<div class="terms">
<h2>PHPShop 1.5 安装系统需求</h2>
      
      <p>PHP 5.0 以上.  (当前PHP版本 :<font color="#009900"><?php echo phpversion();?></font>)</p>

      <p>Mysql 5.0以上 (系统在连接mysql时会进行您的mysql版本检查)</p>
	  
<p></p>
</div>
<form action="install.php?step=2" method="post">
  <input type="hidden" name="action" value="welcome" >
  <div class="controls">
	<input type="submit" id="continue" value="下一步 »" name="continue"/>
  </div>    
</form>
<?php
}

function setpTwo() {
?>
<div class="terms">
<?php
	$arrDir = File::getDir(__PRODUCT_IMG);
	sort($arrDir);
	$num = count($arrDir);
	$is_write = true;
	for($i = 0; $i < $num; $i++) {
		if(!is_dir(__PRODUCT_IMG . $arrDir[$i])) continue;
		if(writeFile(__PRODUCT_IMG . $arrDir[$i] . '/test.php', ' ') == false) {
			echo '<span class="red">' . __PRODUCT_IMG . $arrDir[$i] . " 文件夹不可写,请设置为可写。</span><br>";
			$is_write = false;
			//return;
		} else {
			File::deleteFile('test.php', __PRODUCT_IMG . $arrDir[$i] . '/');
			echo '<span class="green">'. __PRODUCT_IMG . $arrDir[$i] . "/ 文件夹可写。</span> <br>\r\n";
			if($arrDir[$i] == 'waterimage') {
				$arrFile = File::getAllFile(__PRODUCT_IMG . 'waterimage/');
			}
		}
	}
	sort($arrFile);
	$num = count($arrFile);
	
	if(DIRECTORY_SEPARATOR == "/") {
		for($i = 0; $i < $num; $i++) {
			//echo decoct(fileperms(__PRODUCT_IMG . 'waterimage/' . $arrFile[$i]))."\r\n";
			if(substr(decoct(fileperms(__PRODUCT_IMG . 'waterimage/' . $arrFile[$i])), -3) != '777') {
				//echo substr(decoct(fileperms(__PRODUCT_IMG . 'waterimage/' . $arrFile[$i])), -3);
				echo  '<span class="red">' . __PRODUCT_IMG . 'waterimage/' . $arrFile[$i] . " 文件不可写,请设置为可写。</span><br>";
				$is_write = false;
			} else {
				echo  '<span class="green">' . __PRODUCT_IMG . 'waterimage/' . $arrFile[$i] . " 文件可写。</span><br>";
			}
		}
		if(substr(decoct(fileperms(__ROOT_PATH . 'etc/define.php')), -3) != '777') {
			echo  '<span class="red">' . __ROOT_PATH . 'etc/define.php' . " 文件不可写,请设置为可写。</span><br>";
			$is_write = false;
		}
	} else {
		for($i = 0; $i < $num; $i++) {
			echo '<span class="green">' . __PRODUCT_IMG . 'waterimage/' . $arrFile[$i] . " 文件可写。</span><br>\r\n";
		}
		echo '<span class="green">' . __ROOT_PATH . 'etc/define.php' . " 文件可写。</span><br>";
	}
	if($is_write == false) {
		return;
	}		

	if(writeFile(__CACHE . 'test.php', ' ') == false) {
		echo '<span class="red">' . __CACHE . " 文件夹不可写,请设置为可写。</span><br>";
		return;
	} else {
		File::deleteFile('test.php', __CACHE);
		echo '<span class="green">' . __CACHE . " 文件夹可写。</span><br>";
	}
	if(writeFile(__ROOT_LOGS_PATH . 'test.php', ' ') == false) {
		echo '<span class="red">' . __ROOT_LOGS_PATH . " 文件夹不可写,请设置为可写。</span><br>";
		return;
	} else {
		File::deleteFile('test.php', __ROOT_LOGS_PATH);
		echo '<span class="green">' . __ROOT_LOGS_PATH . " 文件夹可写。</span><br>";
	}
	if(writeFile(__COMMSITE . 'test.php', ' ') == false) {
		echo '<span class="red">' . __COMMSITE . " 文件夹不可写,请设置为可写。</span><br>";
		return;
	} else {
		File::deleteFile('test.php', __COMMSITE);
		echo '<span class="green">' . __COMMSITE . " 文件夹可写。</span><br>";
	}
	//__ROOT_TPLS_TPATH . 'templates_c'
	if(writeFile(__ROOT_TPLS_TPATH . 'templates_c/admin/test.php', ' ') == false) {
		echo '<span class="red">' . __ROOT_TPLS_TPATH . 'templates_c/admin' . " 文件夹不可写,请设置为可写。</span><br>";
		return;
	} else {
		File::deleteFile('test.php', __ROOT_TPLS_TPATH . 'templates_c/admin/');
		echo '<span class="green">' . __ROOT_TPLS_TPATH . 'templates_c/admin' . " 文件夹可写。</span><br>";
	}
	
	if(writeFile(__ROOT_TPLS_TPATH . 'templates_c/beauty/test.php', ' ') == false) {
		echo '<span class="red">' . __ROOT_TPLS_TPATH . 'templates_c/beauty' . " 文件夹不可写,请设置为可写。</span><br>";
		return;
	} else {
		File::deleteFile('test.php', __ROOT_TPLS_TPATH . 'templates_c/beauty/');
		echo '<span class="green">' . __ROOT_TPLS_TPATH . 'templates_c/beauty' . " 文件夹可写。</span><br>";
	}
	
	if(writeFile(__ROOT_TPLS_TPATH . 'templates_c/beauty/inc/test.php', ' ') == false) {
		echo '<span class="red">' . __ROOT_TPLS_TPATH . 'templates_c/beauty/inc' . " 文件夹不可写,请设置为可写。</span><br>";
		return;
	} else {
		File::deleteFile('test.php', __ROOT_TPLS_TPATH . 'templates_c/beauty/inc/');
		echo '<span class="green">' . __ROOT_TPLS_TPATH . 'templates_c/beauty/inc' . " 文件夹可写。</span><br>";
	}
?>
<p></p>
</div>
<form action="install.php?step=3" method="post">
  <input type="hidden" name="action" value="welcome" >
  <div class="controls">
	<input type="submit" id="continue" value="下一步 »" name="continue"/>
  </div>    
</form>
<?php
}

function setpThree() {
?>
<form action="install.php?step=insterdata" method="post">
<div class="terms">
<table cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td>安装数据库</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="4"> </td>
    <td valign="top" width="120">主机地址</td>
    <td width="462"><input gtbfieldid="9" name="host" id="host" type="text" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">用户名</td>
    <td><input gtbfieldid="9" name="username" id="username" type="text" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">密码</td>
    <td><input gtbfieldid="9" name="password" id="password" type="password" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">数据库名</td>
    <td><input gtbfieldid="9" name="db_name" id="db_name" type="text" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">表前缀</td>
    <td><input gtbfieldid="9" name="db_" id="db_" value="phpshop_" type="text" /></td>
  </tr>
  <tr>
    <td colspan="3"> </td>
  </tr>
  <tr>
    <td width="4"> </td>
    <td colspan="2">
        <label for="updates_signup"></label></td>
  </tr>
</table>
<h2>&nbsp;</h2>
<p></p>
</div>
  <input type="hidden" name="action" value="welcome" >
  <div class="controls">
	<input type="submit" id="continue" value="下一步 »" name="continue"/>
  </div>    
</form>
<?php
}


function setpFour() {
?>
<form action="install.php?step=insertadmin" method="post">
<div class="terms">
<table>
<tr>
    <td></td>
    <td valign="top">&nbsp;</td>
    <td>管理者帐号</td>
  </tr>
  <tr>
    <td></td>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="4"></td>
    <td valign="top" width="120">用户名</td>
    <td width="462"><input gtbfieldid="7" maxlength="26" name="admin" id="admin" type="text" /></td>
  </tr>
  <tr>
    <td colspan="3"> </td>
  </tr>
  <tr>
    <td width="4"> </td>
    <td width="120"><label for="label">密码   </label></td>
    <td width="462"><input name="adminpasswoed" id="label" type="password" /></td>
  </tr>
  <tr>
    <td colspan="3"> </td>
  </tr>
  <tr>
    <td width="4"> </td>
    <td width="120"><label for="label2">再输入一次密码 </label></td>
    <td width="462"><input name="adminpasswoed2" id="label2" type="password" /></td>
  </tr>
  <tr>
    <td colspan="3"> </td>
  </tr>
  <tr>
    <td width="4"> </td>
    <td width="120"><label for="label3">Email </label></td>
    <td width="462"><input gtbfieldid="8" name="email" id="label3" type="text" /></td>
  </tr>
  <tr>
    <td colspan="3"> </td>
  </tr>
</table>
</div>
  <input type="hidden" name="action" value="welcome" >
  <div class="controls">
	<input type="submit" id="continue" value="下一步 »" name="continue"/>
  </div>    
</form>
<?php
}

function setpFive() {
	writeFile(__COMMSITE . 'install.lock','');
	echo("安装数据成功！<br>点击这里进入后台设置：<a href='../admin' target='_blank'>登陆后台</a> <a href='../' target='_blank'>进入网站</a><br><hr /><br>您需要在后台先设置好网站名称，商品类别 才能正常访问网站。直接访问网站会报错。有问题请联系 <a href='http://www.phpshop.cn/bbs' target='_blank'>http://www.phpshop.cn/bbs</a>");
}
function setpInsterdata() {
	$host = isset($_REQUEST['host']) ? trim($_REQUEST['host']) : NULL;
	$username = isset($_REQUEST['username']) ? trim($_REQUEST['username']) : NULL;
	$password = isset($_REQUEST['password']) ? trim($_REQUEST['password']) : NULL;
	$db_name = isset($_REQUEST['db_name']) ? trim($_REQUEST['db_name']) : NULL;
	$db_ = isset($_REQUEST['db_']) ? trim($_REQUEST['db_']) : NULL;
	$key = md5($host . $username . $password . getDateTimeId());
//
	
//
	
// 
//print_r($arrFile);

$define = "<?php
if( !defined('DEFINE_PHP') ){
define('DEFINE_PHP','YES');
/// physical path ///
define('__ROOT_PATH',substr(dirname(__FILE__), 0, -3));
//
define('__KEY','$key');
/// db connection ///
define('DB_TYPE','mysql');
/** database host */
define('DB_HOST','$host');
/** database name */
define('DB_NAME','$db_name');
/** database db table*/
define('DB_TABLE', '$db_');
/** database user name */
define('DB_USER','$username');
/** database password */
define('DB_PASSWORD','$password');

//web style
define('__SETTING_STYLE','default/');
//serialnumber
define('__SERIAL_NUMBER','B07D2-0AD60-7DB6A-9E4CB-59F37-40CBA');
}
require_once(__ROOT_PATH . \"/functions/func.Common.php\");
?>";

	if(($conn = @mysql_pconnect($host, $username, $password)) == false) {
		alert("错误：连接不上数据库,请检查设置。");
		return;
	} else {
		echo "连接数据库成功！<br>";
	}
	if(substr(mysql_get_server_info(), 0, 1) < 5) {
		alert("错误：mysql版本过低，请升级到mysql5.0以上版本！", 0);
		return;
	}
	if(mysql_select_db($db_name, $conn) == false) {
		alert("错误：连接不上数据库 $db_name,请检查设置。");
		return;
	} else {
		mysql_query("SET NAMES UTF8;");
		echo "连接数据库 $db_name 成功！<br>";
	}
	if(writeFile(__ROOT_PATH . 'etc/define.php', $define) == false) {
		alert('错误：' . __ROOT_PATH . "etc/define.php 文件不可写,请设置为可写。");
		return;
	} else {
		echo "define.php 文件写入成功！<br>";
	}
?>
<script language="javascript">
if(confirm('继续安装会删除相同表明的数据，您是否继续？')) {
	location.href = 'install.php?step=insertshopdata';
} else {
	go(-1);
}
</script>
<?php
//excute sql
}
function insertShopData() {
	require_once("../etc/define.php");
	if(createTable(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME, DB_TABLE)) {
		alert('安装数据成功！', 0);
		echo "<meta http-equiv=refresh content=\"1; url=install.php?step=4\">"; 
	} else {
		alert('错误：安装失败！请检查设置或者联系开发者！');
	}
}
function insertAdmin() {
	require_once("../etc/define.php");
	if(@mysql_pconnect(DB_HOST, DB_USER, DB_PASSWORD) == false) {
		alert('错误：连接数据库失败！');
	}
	
	if(@mysql_select_db(DB_NAME) == false) {
		alert("错误：连接不上数据库 ".DB_NAME.",请检查设置。");
		return;
	} 
	
	$email = isset($_REQUEST['email']) ? trim($_REQUEST['email']) : NULL;
	if(empty($email)) {
		alert('管理者帐号 email不能为空！');
	} 
	$admin = isset($_REQUEST['admin']) ? trim($_REQUEST['admin']) : NULL;
	if(empty($admin)) {
		alert('管理者帐号 用户名不能为空！');
	} 
	$adminpasswoed = isset($_REQUEST['adminpasswoed']) ? trim($_REQUEST['adminpasswoed']) : NULL;
	$adminpasswoed2 = isset($_REQUEST['adminpasswoed2']) ? trim($_REQUEST['adminpasswoed2']) : NULL;
	if(!empty($adminpasswoed)) {
		if($adminpasswoed2 != $adminpasswoed) {
			alert('管理者帐号 2次输入密码不相等！');
		}
		$adminpasswoed = md5($adminpasswoed);
	} else {
		alert('管理者帐号 密码不能为空！');
	}
	$sqlstr = "insert into `" . DB_TABLE . "admin`(`id`,`email`,`name`,`pass`) values (1,'$email','$admin','$adminpasswoed')";
	if (false == mysql_query($sqlstr)) {
		alert("安装数据失败,请检查设置。");
		return;
	}
	$sqlstr = "insert into `" . DB_TABLE . "power`(`aid`,`fid`) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,40);";
	if (false == mysql_query($sqlstr)) {
		echo "安装数据失败,请检查设置。<br>";
		return;
	} else {
		redirect('install.php?step=5');
	}
}
function writeFile($filename, $contant) {
	$fp = @fopen($filename, 'wb');
	if($fp == false) return false;
	flock($fp, 2);
	fwrite($fp, $contant);
	fclose($fp);
	return true;
}
?>