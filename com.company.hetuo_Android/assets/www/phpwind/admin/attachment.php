<?php
!function_exists('adminmsg') && exit('Forbidden');
@set_time_limit(0);
$db_perpage=50;
$basename="$admin_file?adminjob=attachment";

include(D_P.'data/bbscache/forum_cache.php');

if($admin_gid == 5){
	list($allowfid,$forumcache) = GetAllowForum($admin_name);
	$sql = $allowfid ? "fid IN($allowfid)" : '0';
} else{
	include D_P.'data/bbscache/forumcache.php';
	list($hidefid,$hideforum) = GetHiddenForum();
	if($admin_gid == 3){
		$forumcache .= $hideforum;
		$sql = '1';
	} else{
		$sql = $hidefid ? "fid NOT IN($hidefid)" : '1';
	}
}

if(empty($action)){
	$postdate2 = get_date($timestamp+24*3600,'Y-m-d');
	include PrintEot('attachment');exit;
} elseif($action=='search'){
	InitGP(array('fid','username','uid','filename','hits','ifmore','filesize','ifless','postdate1','postdate2','orderway','asc','pernum','page'));
	if(is_numeric($fid)){
		$sql .= " AND fid=".pwEscape($fid);
	}
	$username = trim($username);
	if($username){
		$rt  = $db->get_one("SELECT uid FROM pw_members WHERE username=".pwEscape($username));
		$uid = $rt['uid'];
	}
	if(is_numeric($uid)){
		$sql .= " AND uid=".pwEscape($uid,false);
	}
	$filename = trim($filename);
	if($filename!=''){
		$filename = str_replace('*','%',$filename);
		$sql .= " AND (name LIKE ".pwEscape("%$filename%").")";
	}
	if($hits){
		if($ifmore){
			$sql.=" AND (hits<".pwEscape($hits).')';
		} else{
			$sql.=" AND (hits>".pwEscape($hits).')';
		}
	}
	if($filesize){
		if($ifless){
			$sql.=" AND (size<".pwEscape($filesize).')';
		} else{
			$sql.=" AND (size>".pwEscape($filesize).')';
		}
	}
	if($postdate1){
		$uploadtime = PwStrtoTime($postdate1);
		is_numeric($uploadtime) && $sql.=" AND uploadtime>".pwEscape($uploadtime);
	}
	if($postdate2){
		$uploadtime = PwStrtoTime($postdate2);
		is_numeric($uploadtime) && $sql.=" AND uploadtime<".pwEscape($uploadtime);
	}

	if(CkInArray($orderway,array('uploadtime','size','rvrc','name','hits'))){
		$order = "ORDER BY $orderway";
		$asc=='DESC' && $order.=' '.$asc;
	} else{
		$order = '';
	}
	$pernum=is_numeric($pernum) ? $pernum : 20;
	$page < 1 && $page=1;
	$limit = pwLimit(($page-1)*$pernum,$pernum);

	$rt=$db->get_one("SELECT COUNT(*) AS count FROM pw_attachs WHERE $sql");
	$sum=$rt['count'];
	$numofpage=ceil($sum/$pernum);
	$pages=numofpage($sum,$page,$numofpage,"$basename&action=search&fid=$fid&uid=$uid&filename=".rawurlencode($filename)."&hits=$hits&ifmore=$ifmore&filesize=$filesize&ifless=$ifless&orderway=$orderway&asc=$asc&postdate1=$postdate1&postdate2=$postdate2&pernum=$pernum&");

	$attachdb=$thread=array();
	$query=$db->query("SELECT * FROM pw_attachs WHERE $sql $order $limit");
	while(@extract($db->fetch_array($query))){
		if($_POST['direct']){
			if(file_exists("$attachdir/$attachurl")){
				P_unlink("$attachdir/$attachurl");
				$ifthumb && P_unlink("$attachdir/thumb/$attachurl");
			}
		} else{
			$thread['url']=$attachurl;
			$thread['imgurl'] = geturl($attachurl,'show');
			$thread['imgurl'] = is_array($thread['imgurl']) ? $thread['imgurl'][0] : '';
			$thread['name']=$name;
			$thread['aid']=$aid;
			$thread['tid']=$tid;
			$thread['where']="thread.php?fid=$fid";
			$thread['forum']=$forum[$fid]['name'];
			$thread['filezie']=$size;
			$thread['uploadtime']=get_date($uploadtime);
			$attachdb[]=$thread;
		}
	}
	if($_POST['direct']){
		$db->update("DELETE FROM pw_attachs WHERE $sql".pwLimit($pernum));
		adminmsg('operate_success');
	} else{
		include PrintEot('attachment');exit;
	}
} elseif($action=='schdir'){

	InitGP(array('filename','filesize','ifless','postdate1','postdate2','pernum','direct','start'));
	if(!$filename && !$filesize && !$postdate1 && !$postdate2){
		adminmsg('noenough_condition');
	}
	$cache_file = D_P."data/bbscache/att_".substr(md5($admin_name),10,10).".txt";
	if(!$start){
		$start = 0;
		if(file_exists($cache_file)){
			P_unlink($cache_file);
		}
	}
	$num = 0;
	!$pernum && $pernum = 1000;
	$dir1 = opendir($attachdir);
	while(false !== ($file1 = readdir($dir1))){
		if($file1!='' && $file1!='.' && $file1!='..' && !eregi("\.html$",$file1)){
			if(is_dir("$attachdir/$file1")){
				$dir2 = opendir("$attachdir/$file1");
				while(false !==($file2=readdir($dir2))){
					if(is_file("$attachdir/$file1/$file2") && $file2!='' && $file2!='.' && $file2!='..' && !eregi("\.html$",$file2)){
						$num++;
						if($num > $start){
							attachcheck("$file1/$file2");
							if($num-$start>=$pernum){
								if($direct){
									adminmsg('attach_delfile');
								} else{
									adminmsg('attach_step',"$basename&action=$action&filename=$filename&filesize=$filesize&ifless=$ifless&postdate1=$postdate1&postdate2=$postdate2&start=$num&pernum=$pernum&direct=$direct",0);
								}
							}
						}
					}
				}
			} elseif(is_file("$attachdir/$file1")){
				$num++;
				if($num > $start){
					attachcheck("$file1");
					if($num-$start>=$pernum){
						if($direct){
							adminmsg('attach_delfile');
						} else{
							adminmsg('attach_step',"$basename&action=$action&filename=$filename&filesize=$filesize&ifless=$ifless&postdate1=$postdate1&postdate2=$postdate2&start=$num&pernum=$pernum&direct=$direct",0);
						}
					}
				}
			}
		}
	}

	adminmsg('attach_success',"$basename&action=list",0);
} elseif($action=='list'){
	$cache_file = D_P."data/bbscache/att_".substr(md5($admin_name),10,10).".txt";
	InitGP(array('page'),'GP',2);
	$page<1 && $page=1;
	$start=($page-1)*$db_perpage*50;
	$readsize=$db_perpage*50;

	$sum=floor(@filesize($cache_file)/50);
	$numofpage=ceil($sum/$db_perpage);
	$pages=numofpage($sum,$page,$numofpage,"$basename&action=list&");

	if($fp=@fopen($cache_file,"rb")){
		flock($fp,LOCK_SH);
		fseek($fp,$start);
		$readdb=fread($fp,$readsize);
		fclose($fp);
	}
	$readdb=explode("\n",$readdb);
	foreach($readdb as $key => $value){
		$value=trim($value);
		if($value){
			$attach['name']=$value;
			if(file_exists("$attachdir/$value")){
				$attach['size']=round(filesize("$attachdir/$value")/1024,1);
				$attach['time']=get_date(fileatime("$attachdir/$value"));
				$attach['exists']=1;
			} else{
				$attach['size']='-';
				$attach['time']='-';
				$attach['exists']=0;
			}
			$attachdb[]=$attach;
		}
	}
	include PrintEot('attachment');exit;
} elseif($_POST['action']=='delfile'){
	InitGP(array('delfile'),'P');
	if($delfile){
		foreach($delfile as $key => $value){
			if(file_exists("$attachdir/$value")){
				P_unlink("$attachdir/$value");
				P_unlink("$attachdir/thumb/$value");
			}
		}
	}
	$basename="$admin_file?adminjob=attachment&action=list";
	adminmsg('attach_delfile');
} elseif($_POST['action']=='delete'){
	InitGP(array('aidarray'),'P');
	$delnum = $count = 0;
	if($aidarray){
		$count   = count($aidarray);
		$attachs = array();
		foreach($aidarray as $value){
			is_numeric($value) && $attachs[] = $value;
		}
		$attachs = pwImplode($attachs);
		$query   = $db->query("SELECT attachurl FROM pw_attachs WHERE $sql AND aid IN($attachs)");
		while($rs=$db->fetch_array($query)){
			if(P_unlink("$attachdir/$rs[attachurl]")){
				$rs['ifthumb'] && P_unlink("$attachdir/thumb/$rs[attachurl]");
				$delnum ++;
				$delname .= "$rs[attachurl]<br>";
			}
		}
		$db->update("DELETE FROM pw_attachs WHERE $sql AND aid IN($attachs)");
	}
	adminmsg('attachstats_del');
} elseif($action=='delattach'){
	$pwServer['REQUEST_METHOD']!='POST' && PostCheck($verify);
	InitGP(array('pernum','start','deltotal'));
	if(!$start){
		$start=0;
		$deltotal=0;
	}
	$num	= 0;
	$delnum	= 0;
	!$pernum && $pernum = 1000;
	$dir1 = opendir($attachdir);
	while(false !== ($file1 = readdir($dir1))){
		if($file1!='' && $file1!='.' && $file1!='..' && !eregi("\.html$",$file1)){
			if(is_dir("$attachdir/$file1")){
				if(in_array($file1,array('upload','photo','cn_img','forumlogo'))){
					continue;
				}
				$dir2 = opendir("$attachdir/$file1");
				while(false !==($file2=readdir($dir2))){
					if(is_file("$attachdir/$file1/$file2") && $file2!='' && $file2!='.' && $file2!='..' && !eregi("\.html$",$file2)){
						$num++;
						if($num > $start){
							$rt = $db->get_one("SELECT aid,ifthumb FROM pw_attachs WHERE attachurl=".pwEscape("$file1/$file2"));
							if(!$rt){
								$delnum++;
								$deltotal++;
								P_unlink("$attachdir/$file1/$file2");
								P_unlink("$attachdir/thumb/$file1/$file2");
							}
							if($num-$start >= $pernum){
								$start = $num-$delnum;
								$j_url = "$basename&action=$action&start=$start&pernum=$pernum&deltotal=$deltotal";
								adminmsg('delattach_step',EncodeUrl($j_url),0);
							}
						}
					}
				}
			} elseif(is_file("$attachdir/$file1")){
				$num++;
				if($num > $start){
					$rt = $db->get_one("SELECT aid,ifthumb FROM pw_attachs WHERE attachurl=".pwEscape($file1));
					if(!$rt){
						$delnum++;
						$deltotal++;
						P_unlink("$attachdir/$file1");
						P_unlink("$attachdir/thumb/$file1");
					}
					if($num-$start>=$pernum){
						$start = $num-$delnum;
						$j_url = "$basename&action=$action&start=$start&pernum=$pernum&deltotal=$deltotal";
						adminmsg('delattach_step',EncodeUrl($j_url),0);
					}
				}
			}
		}
	}
	adminmsg('operate_success');
}

function attachcheck($file){
	global $cache_file,$attachdir,$admin_pwd,$filename,$filesize,$ifless,$postdate1,$postdate2,$direct,$attachdir;

	if($filename && strpos($file,$filename)===false){
		return;
	}
	if($filesize){
		if($ifless && filesize("$attachdir/$file") >= $filesize * 1024){
			return;
		} elseif(!$ifless && filesize("$attachdir/$file") <= $filesize * 1024){
			return;
		}
	}
	if($postdate1){
		$visittime = PwStrtoTime($postdate1);
		if(is_numeric($visittime) && fileatime("$attachdir/$file") < $visittime){
			return;
		}
	}
	if($postdate2){
		$visittime = PwStrtoTime($postdate2);
		if(is_numeric($visittime) && fileatime("$attachdir/$file") > $visittime){
			return;
		}
	}
	if($_POST['direct']){
		P_unlink("$attachdir/$file");
		P_unlink("$attachdir/thumb/$file");
	} else{
		strlen($file)>49 && $file=substr($file,0,49);
		writeover($cache_file,str_pad($file,49)."\n","ab");
	}
}
?>