<?php
!defined('PW_UPLOAD') && exit('Forbidden');

$invokeService = L::loadClass('InvokeService');

$insertdata = array(
		'tplid' => '2',
		'type' => 'subject',
		'name' => '帖子列表2',
		'descrip' => '由标题和摘要组成',
		'tagcode' => '<list action="subject" num="3" title="帖子及摘要" />
<loop>
<h4><a href="{url}" target="_blank">{title,25}</a></h4>
<p>{descrip,40}</p>
<ul class="cc area-list-tree">
{tagrelate}
</ul>
</loop>',
		'image' => '2.jpg',
);

$invokeService->updateTpl(2,$insertdata);

?>