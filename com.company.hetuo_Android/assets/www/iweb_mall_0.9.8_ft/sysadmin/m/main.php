<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$a_langpackage=new adminlp;

//数据表定义区
$t_users = $tablePreStr."users";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";
$t_shop_category = $tablePreStr."shop_category";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
$sql = "select count(*) from `$t_users`";
$register_num = $dbo->getRow($sql);

$sql = "select count(*) from `$t_shop_info`";
$shop_num = $dbo->getRow($sql);

$sql = "select count(*) from `$t_goods`";
$goods_num = $dbo->getRow($sql);

$sql = "select count(*) from `$t_shop_category`";
$ucategory_num = $dbo->getRow($sql);

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<link rel="stylesheet" type="text/css" href="skin/css/main.css">
<style>

</style>
</head>
<body>
<div id="maincontent">
	<div class="wrap">
		<div class="crumbs"><?php echo $a_langpackage->a_location; ?> &gt;&gt; <?php echo $a_langpackage->a_global_settings; ?> &gt;&gt; <?php echo $a_langpackage->a_manager_index; ?></div>
        <hr />
        <div class="infobox left" style="width:48.5%">
                	<!--<div class="handle"></div>-->
                	<h3><?php echo $a_langpackage->a_site_statistics; ?></h3>
                    <div class="content2">
                    	<table>
                            <tbody>
                            <tr>
                                <td width="50%"><span style="color:#F67A06;"><?php echo $a_langpackage->a_register_num; ?>: <?php echo $register_num[0]; ?></span></td>
                                <td width="50%"><span style="color:#F67A06;"><?php echo $a_langpackage->a_register_shopnum; ?>:<?php echo $shop_num[0]; ?></span></td>
                            </tr>
                            <tr>
                                <td><span style="color:#F67A06;"><?php echo $a_langpackage->a_goodsnum; ?>: <?php echo $goods_num[0]; ?></span></td>
                                <td><span style="color:#F67A06;"><?php echo $a_langpackage->a_user_defined_categorynum; ?>: <?php echo $ucategory_num[0]; ?></span></td>
                            </tr>
                        	</tbody>
                        </table>
                    </div>
                </div>
                <div class="infobox right" style="width:48.5%">
                	<h3><?php echo $a_langpackage->a_official_news; ?></h3>
					<div class="content2">
						<table>
							<tr>
								<td><?php echo $a_langpackage->a_news_overview; ?>&nbsp;&nbsp;<a href="http://www.jooyea.net" target="_blank">http://www.jooyea.net</a></td>
							</tr>
							<tr>
								<td><script src="http://tech.jooyea.com/customer/mall.php?version=<?php echo $SYSINFO['version']; ?>&t=<?php echo time(); ?>"></script></td>
							</tr>
						</table>
					</div>
                </div>
                <div class="clear"></div>
                <div class="infobox">
                	<h3><?php echo $a_langpackage->a_database_version; ?></h3>
                    <div class="content2">
                    	<p><?php echo $a_langpackage->a_webserver_opratesys; ?>: <?php echo PHP_OS; ?>(<?php echo $_SERVER['SERVER_ADDR']; ?>)</p>
                        <p>Web <?php echo $a_langpackage->a_server; ?>: <?php echo $_SERVER['SERVER_SOFTWARE']; ?></p>
                        <p>PHP <?php echo $a_langpackage->a_version; ?>: <?php echo PHP_VERSION; ?></p>
                        <p>MySQL <?php echo $a_langpackage->a_version; ?>: 5.1.33-community</p>
                        <p><?php echo $a_langpackage->a_safe_mode; ?>: <?php echo (boolean) ini_get('safe_mode') ?  $a_langpackage->a_yes:$a_langpackage->a_no; ?></p>
                        <p><?php echo $a_langpackage->a_safe_mode; ?>GID: <?php echo (boolean) ini_get('safe_mode_gid') ?  $a_langpackage->a_yes:$a_langpackage->a_no; ?></p>
						<p>Socket <?php echo $a_langpackage->a_support; ?>:<?php echo (boolean) ini_get('fsockopen') ?  $a_langpackage->a_yes:$a_langpackage->a_no; ?></p>
						<p>Zlib <?php echo $a_langpackage->a_support; ?>:<?php echo function_exists('gzclose') ?  $a_langpackage->a_yes:$a_langpackage->a_no; ?></p>
						<p><?php echo $a_langpackage->a_file_max_upload; ?>:<?php echo ini_get('upload_max_filesize'); ?></p>
						<p><?php echo $a_langpackage->a_timezone_set; ?>:<?php echo function_exists("date_default_timezone_get") ? date_default_timezone_get().' '.$SYSINFO['timezone'] : $a_langpackage->a_no_timezone; ?></p>
						<p>iweb Mall<?php echo $a_langpackage->a_version; ?>:<?php echo $SYSINFO['version']; ?></p>
						<p><?php echo $a_langpackage->a_install_datetime; ?>:<?php echo date("Y-m-d H:i:s",@filemtime("../cache/install.lock")+($SYSINFO['timezone']*3600)); ?></p>
                    </div>
                </div>
                <div class="infobox">
                	<h3><?php echo $a_langpackage->a_development_testing; ?></h3>
                    <div class="content2">
                    	<p><?php echo $a_langpackage->a_copyright_info; ?>：<?php echo $a_langpackage->a_home; ?></p>
						<p><?php echo $a_langpackage->a_system_design; ?>：erysin </p>
    					<p><?php echo $a_langpackage->a_development_testing; ?>：nswe ，Cain ，bevin ，Jack，E.T</p>
						<p><?php echo $a_langpackage->a_theme_ui; ?>：Eric，Mike  </p>
						<p><?php echo $a_langpackage->a_product_web_site; ?>：<a href="http://www.jooyea.net" target="_blank">http://www.jooyea.net</a></p>
                    </div>
                </div>
            </div>
		</div>
</div>
</body>
</html>