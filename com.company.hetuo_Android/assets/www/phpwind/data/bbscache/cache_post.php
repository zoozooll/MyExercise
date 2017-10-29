<?php
$forum=array(
	'1' => array(
		'fid' => '1',
		'fup' => '0',
		'ifsub' => '0',
		'childid' => '1',
		'type' => 'category',
		'name' => '默认分类',
		'style' => '0',
		'f_type' => 'forum',
		'cms' => '0',
		'ifhide' => '1',
		'title' => '',
		'metadescrip' => '',
		'descrip' => '',
		'keywords' => '',
	),
	'2' => array(
		'fid' => '2',
		'fup' => '1',
		'ifsub' => '0',
		'childid' => '0',
		'type' => 'forum',
		'name' => '默认版块',
		'style' => '0',
		'f_type' => 'forum',
		'cms' => '0',
		'ifhide' => '1',
		'title' => '',
		'metadescrip' => '',
		'descrip' => '',
		'keywords' => '',
	),
);
$pwForumList=array(
	'1' => array(
		'name' => '默认分类',
		'child' => array(
			'2' => '默认版块',
		),
	),
);
$pwForumAllList = array(
);

$ltitle=$lpic=$lneed=array();
/**
* default
*/
$ltitle[1]='default';		$lpic[1]='8';
$ltitle[2]='游客';		$lpic[2]='8';
$ltitle[6]='禁止发言';		$lpic[6]='8';
$ltitle[7]='未验证会员';		$lpic[7]='8';

/**
* system
*/
$ltitle[3]='管理员';		$lpic[3]='3';
$ltitle[4]='总版主';		$lpic[4]='4';
$ltitle[5]='论坛版主';		$lpic[5]='5';
$ltitle[17]='门户编辑';		$lpic[17]='5';

/**
* special
*/
$ltitle[16]='荣誉会员';		$lpic[16]='5';

/**
* member
*/
$ltitle[8]='新手上路';		$lpic[8]='8';		$lneed[8]='0';
$ltitle[9]='侠客';		$lpic[9]='9';		$lneed[9]='100';
$ltitle[10]='骑士';		$lpic[10]='10';		$lneed[10]='300';
$ltitle[11]='圣骑士';		$lpic[11]='11';		$lneed[11]='600';
$ltitle[12]='精灵王';		$lpic[12]='12';		$lneed[12]='1000';
$ltitle[13]='风云使者';		$lpic[13]='13';		$lneed[13]='5000';
$ltitle[14]='光明使者';		$lpic[14]='14';		$lneed[14]='10000';
$ltitle[15]='天使';		$lpic[15]='14';		$lneed[15]='50000';


$faces=array(
	'default'=>array(
		'name'=>'默认表情',
		'child'=>array('2','3','4','5','6','7','8','9','10','11','12','13','14','15',),
	),
);
$face=array(
	'2'=>array('default/1.gif','',''),
	'3'=>array('default/2.gif','',''),
	'4'=>array('default/3.gif','',''),
	'5'=>array('default/4.gif','',''),
	'6'=>array('default/5.gif','',''),
	'7'=>array('default/6.gif','',''),
	'8'=>array('default/7.gif','',''),
	'9'=>array('default/8.gif','',''),
	'10'=>array('default/9.gif','',''),
	'11'=>array('default/10.gif','',''),
	'12'=>array('default/11.gif','',''),
	'13'=>array('default/12.gif','',''),
	'14'=>array('default/13.gif','',''),
	'15'=>array('default/14.gif','',''),
);

?>