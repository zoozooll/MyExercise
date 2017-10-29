<?php
!function_exists('readover') && exit('Forbidden');

/*The app client*/
if ($db_siteappkey && ($db_apps_list['17']['status'] == 1 || is_array($db_threadconfig))) {
	$appclient = L::loadClass('appclient');
	if (is_array($db_threadconfig)) {
		$threadright = array();
		$threadright = $appclient->getThreadRight();
	}
}
/*The app client*/

$newpic= (int)GetCookie('newpic');
$query = $db->query("SELECT f.fid,f.logo,f.name, f.descrip,f.forumadmin,f.password,f.allowvisit,f.f_type,f.ifcms,fd.tpost,fd.topic,fd.article,fd.subtopic,fd.lastpost FROM pw_forums f LEFT JOIN pw_forumdata fd USING(fid) WHERE f.fup=".pwEscape($fid)."ORDER BY f.vieworder");
while($child = $db->fetch_array($query)){
	if(empty($child['allowvisit']) || strpos($child['allowvisit'],','.$groupid.',')!==false){
		list($f_a,$child['au'],$f_c,$child['ft'])=explode("\t",$child['lastpost']);
		$child['pic'] = $newpic<$f_c && ($f_c+172800>$timestamp) ? 'new' : 'old';
		$child['newtitle']=get_date($f_c);
		$child['t']=substrs($f_a,21);
	} else{
		if($child['f_type']==='hidden'){
			continue;
		}
		$child['pic']="lock";
	}
	$child['topics']=$child['topic']+$child['subtopic'];

	
	if ($db_indexfmlogo == 1 && file_exists("$imgdir/$stylepath/forumlogo/$child[fid].gif")) {
		$child['logo'] = "$imgpath/$stylepath/forumlogo/$child[fid].gif";
	} elseif ($db_indexfmlogo == 2) {
		if(!empty($child['logo']) && strpos($child['logo'],'http://') === false && file_exists($attachdir.'/'.$child['logo'])){
			$child['logo'] = "$attachpath/$child[logo]";
		}
	} else {
		$child['logo'] = '';
	}
	
	
	if($child['forumadmin']){
		$forumadmin=explode(",",$child['forumadmin']);
		foreach($forumadmin as $key=> $value){
			if($value){
				if(!$db_adminshow){
					//if ($key==4) {$child['admin'].='...'; break;}
					$child['admin'].="<a href=u.php?action=show&username=".rawurlencode($value).">$value</a> ";
				} else{
					$child['admin'].="<option value=$value>$value</option>";
				}
			}
		}
		$db_adminshow && $child['admin'].='</select>';
	}

	/*The app client*/
	if ($db_siteappkey && $db_apps_list['17']['status'] == 1) {
		$child['forumappinfo'] = $appclient->showForumappinfo($child['fid'],'subforum_erect,subforum_across','17');
	}
	/*The app client*/

	$forumdb[]=$child;
}
$db->free_result($query);
$forumdb && $thread_children='thread_children';
if($foruminfo['viewsub']==1){
	require_once PrintEot('thread_childmain');footer();
}
?>