<?php
!defined('M_P') && exit('Forbidden');
!defined('USED_HEAD') && define('USED_HEAD', 1);
define('F_M',true);

extract(L::style(null, 'wind'));
include_once (D_P.'data/bbscache/area_config.php');
include_once (M_P.'require/core.php');
$ifEditAdmin = 0;

switch ($SCR) {
	case 'index' :
		$ifEditAdmin= checkEditAdmin($windid,'index');
		$cateid = 'index';

		$db_tplstyle= $area_indextpl ? $area_indextpl : 'default';
		$csspath	= $area_indexcss ? $area_indexcss : 'default';
		$db_tplpath = $db_mode.'_'.$db_tplstyle.'_';
		break;
	case 'thread' :
	case 'read' :
		//$SCR = 'm_home';
		$db_tplstyle= 'default';
		$csspath	= 'default';
		$db_tplpath = $db_mode.'_'.$db_tplstyle.'_';
		break;
	case 'cate' :
		$ifEditAdmin	= checkEditAdmin($windid,$fid);
		$cateid = $fid;

		if (isset($area_cateinfo[$fid]['tpl'])) {
			$db_tplstyle= $area_cateinfo[$fid]['tpl'];
			$db_tplpath = $db_mode.'_'.$db_tplstyle.'_'.$fid.'_';
		} else {
			$db_tplstyle= $area_catetpl ? $area_catetpl : 'default';
			$db_tplpath = $db_mode.'_'.$db_tplstyle.'_';
		}
		$csspath	= $area_cateinfo[$fid]['css'] ? $area_cateinfo[$fid]['css'] : 'default';
		/*seo set for area nav*/
		$areatitle = (isset($area_navinfo['KEY'.$cateid]['html'])) ? $area_navinfo['KEY'.$cateid]['html'] : $forum[$cateid]['name'];
		$webPageTitle = $pw_seoset->getPageTitle($forum[$cateid]['title'],$areatitle);
		unset($areatitle);
		break;
	default :
}
$db_menuinit .= ",'td_userinfomore' : 'menu_userinfomore'";

$pwModeImg = "mode/$db_mode/themes/$db_tplstyle/images/$csspath";

list($_Navbar,$_LoginInfo) = pwNavBar();

$jobnum = ($winddb['jobnum']>0) ? "(".$winddb['jobnum'].")" : "";

if (!$pw_seoset && $pw_seoset = L::loadClass('seoset')) {
	$webPageTitle = $pw_seoset->getPageTitle();
	$metaDescription = $pw_seoset->getPageMetadescrip();
	$metaKeywords = $pw_seoset->getPageMetakeyword();
}

require_once PrintEot('header');

if (isset($area_refresh_static) && $area_refresh_static && function_exists("area_static_deal")) {
	$area_static_header = ob_get_contents();
	ob_clean();
}

unset($_Navbar,$pwModeCss);
?>