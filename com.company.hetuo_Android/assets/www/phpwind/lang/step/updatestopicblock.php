<?php
!defined('PW_UPLOAD') && exit('Forbidden');
$stopic	= L::loadClass('stopicservice','stopic');

$insertdata = array();
$insertdata[] = array(
	'block_id'	=> '1',
	'name'		=> '帖子列表',
	'tagcode'	=> <<<EOT
<ul>
<loop>
<li><a href="{url}" target="_blank">{title}</a></li>
</loop>
</ul>
EOT
,
);

$insertdata[] = array(
	'block_id'	=> '2',
	'name'		=> '帖子及摘要',
	'tagcode'	=> <<<EOT
<loop>
<h2><a href="{url}" target="_blank">{title}</a></h2>
<p>{descrip}</p>
</loop>
EOT
,
);
$insertdata[] = array(
	'block_id'	=> '3',
	'name'		=> '图片',
	'tagcode'	=> <<<EOT
<ul class="list-img-a">
<loop>
<li><a href="{url}" target="_blank"><img src="{image}" /></a></li>
</loop>
</ul>
EOT
,
);
$insertdata[] = array(
	'block_id'	=> '4',
	'name'		=> '图片标题',
	'tagcode'	=> <<<EOT
<ul class="list-img-a">
<loop>
<li><a href="{url}" target="_blank"><img src="{image}" /></a><p><a href="{url}" target="_blank">{title}</a></p></li>
</loop>
</ul>
EOT
,
);

$insertdata[] = array(
	'block_id'	=> '5',
	'name'		=> '图文',
	'tagcode'	=> <<<EOT
<table width="100%">
<loop>
<tr>
<th><div><a href="{url}" target="_blank"><img src="{image}" /></a></div></th>
<td><h4><a href="{url}" target="_blank">{title}</a></h4>
<p>{descrip}</p></td>
</tr>
</loop>
</table>
EOT
,
);

$insertdata[] = array(
	'block_id'	=> '6',
	'name'		=> '自定义html',
	'tagcode'	=> <<<EOT
<loop>
{html}
</loop>
EOT
,
);


$insertdata[] = array(
	'block_id'	=> '7',
	'name'		=> '图片播放器',
	'tagcode'	=> <<<EOT
<style type="text/css">
.pwSlide {background:#fff;position:relative;width:100%;height:240px;overflow:hidden;text-align:left;}
.pwSlide a:hover{text-decoration:none;}
.pwSlide .bg {position:absolute;left:0;bottom:0;width:100%;height:40px;background:#333333;filter:alpha(opacity=39);-moz-opacity:0.39;opacity:0.39;}
.pwSlide h4 {position:absolute;left:10px;bottom:15px;_bottom:1px;width:95%;height:20px;line-height:16px;z-index:2;color:#fff;}
.pwSlide ul {margin:0;padding:0;position:absolute;right:5px;bottom:5px;_bottom:2px;z-index:2;}
.pwSlide ul li {list-style:none;float:left;width:18px;height:13px;line-height:15px;text-align:center;margin-left:1px;}
.pwSlide ul li a {display:block;width:18px;height:13px;line-height:13px;font-size:10px;font-family:Tahoma;color:#000;background:#f7f7f7;}
.pwSlide ul li a:hover, .pwSlide ul li a.sel {color:#fff;text-decoration:none;background:#ff6600;color:#fff;}
</style>
<div id="pwSlidePlayer" class="pwSlide">
<loop>
<div class="tac" style="display:none;">
<a href="{url}" target="_blank"><img class="pwSlideFilter" src="{image}" title="{title}" height="240" width="100%" />
<h4 class="fn f12 tal">{title}</h4></a>
</div>
</loop>
<ul id="SwitchNav"></ul>
<div class="bg"></div>
</div>
<script language="JavaScript" src="js/picplayer.js"></script>
<script language="JavaScript">pwSlidePlayer("play",1);</script>
EOT
,
);

foreach ($insertdata as $value) {
	$stopic->replaceBlock($value);
}

?>