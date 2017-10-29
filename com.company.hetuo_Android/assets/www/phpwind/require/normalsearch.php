<?php
	!function_exists('readover') && exit('Forbidden');
	$sid = (int)$sid;
	
	if ($sid>0) {
		@extract($db->get_one("SELECT sorderby,total,schedid FROM pw_schcache WHERE sid='$sid'"));
		$total = (int)$total;
		list($orderway,$asc) = explode('|',$sorderby);
		$orderby = "ORDER BY t.$orderway $asc";
	} else {
		$sch_area>2 && $sch_area = 2;
		InitGP(array('ttable'));

		$_POST && empty($keyword) && empty($pwuser) && $sch_time=='all' && Showmsg('no_condition');

		$method!='AND' && $method = 'OR';
		$schline = 'search|'.trim($sch_type).'|'.trim($keyword).'|'.trim($method).'|'.trim($sch_area).'|'.trim($seekfid).'|'.trim($pwuser).'|'.trim($authorid).'|'.trim($sch_time).'|'.trim($digest);
		$db_plist && $schline .= '|'.$ptable;
		$db_tlist && $schline .= '|'.$ttable;
		$schline = md5($schline.'|search');
		($orderway!='replies' && $orderway!='hits' && $orderway!='favors') && $orderway = 'lastpost';
		$asc!='ASC' && $asc = 'DESC';
		$sorderby = $orderway.'|'.$asc;
		$orderby = "ORDER BY t.$orderway $asc";
		if (!$authorid) {
			@extract($db->get_one("SELECT sid,schtime,total,schedid FROM pw_schcache WHERE schline=".pwEscape($schline).' LIMIT 1'));
			if ($newatc && $timestamp-$schtime>1800) {
				$db->update("DELETE FROM pw_schcache WHERE sid=".pwEscape($sid));
				$schedid = '';
			}
		}
		if (!$schedid) {
			$db_schwait = (int)$db_schwait;
			if (file_exists(D_P.'data/bbscache/schwait_cache.php')) {
				if ($timestamp-pwFilemtime(D_P.'data/bbscache/schwait_cache.php')>$db_schwait) {
					P_unlink(D_P.'data/bbscache/schwait_cache.php');
				} else {
					Showmsg('search_wait');
				}
			}
			$db->update("DELETE FROM pw_schcache WHERE schtime<".pwEscape($timestamp-3600));
			if (($keyword || $pwuser || !is_numeric($authorid)) && $_G['searchtime']!=0) {
				@extract($db->get_one("SELECT lasttime FROM pw_memberinfo WHERE uid=".pwEscape($winduid)));
				if ($timestamp-$lasttime<$_G['searchtime']) {
					Showmsg('search_limit');
				}
				$db->update("UPDATE pw_memberinfo SET lasttime=".pwEscape($timestamp).' WHERE uid='.pwEscape($winduid));
			}
			$sqlwhere = "t.ifcheck='1' ";
			if ($seekfid!='all') {
				if ($forum[$seekfid]['type']=='category') {
					Showmsg('search_cate');
				}
				if (strpos(",$fidout,",",'$seekfid',")===false) {
					$sqlwhere .= 'AND t.fid='.pwEscape($seekfid);
				} else {
					Showmsg('search_forum_right');
				}
			} elseif ($fidout) {
				$sqlwhere .= "AND t.fid NOT IN ($fidout)";
			}
			$distinct = '';
			$sqltable = 'pw_threads t';
			$tablepre = '';
			if ($_G['allowsearch']==2 && $sch_area>0) {
				if ($sch_area==1) {
					$tlistdb = $db_tlist ? $db_tlist : array();
					unset($tlistdb[0]);
					$pw_tmsgs = isset($tlistdb[$ttable]) ? "pw_tmsgs$ttable" : 'pw_tmsgs';
					$keyword && $sqltable .= " LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid";
					$tablepre = 'tm';
				} elseif ($sch_area==2) {
					$pw_posts = GetPtable($ptable);
					if ($db_plist && count($db_plist)>1 && is_numeric($authorid)) {
						${'selected'.$ptable} = 'SELECTED';
						$p_table = "<select name=\"ptable\" onChange=\"window.location='search.php?authorid=$authorid&sch_area=2&ptable='+this.options[this.selectedIndex].value;\">";
						foreach ($db_plist as $key => $val) {
							$name = $val ? $val : ($key != 0 ? getLangInfo('other','posttable').$key : getLangInfo('other','posttable'));
							$p_table .= "<option value=\"$key\" ${'selected'.$key}>$name</option>";
						}
						$p_table .= "</select>";
					}
					$distinct = 'DISTINCT';
					$sqltable = "$pw_posts t";
					$sorderby = 'tid|'.$asc;
					$orderby  = "ORDER BY tid $asc";
					$tablepre = 't';
				}
			}
			if ($keyword || $pwuser) {
				if ($keyword) {
					$keywhere = '';
					$keyword = str_replace(array('%','_'),array('\%','\_'),trim($keyword));
					$keyword_A = explode('|',$keyword);
					foreach ($keyword_A as $value) {
						if ($value) {
							$value = addslashes($value);
							$keywhere .= $method;
							if (!$sch_area) {
								$keywhere .= " t.subject LIKE ".pwEscape("%$value%");
							} elseif ($tablepre) {
								$keywhere .= " (t.subject LIKE ".pwEscape("%$value%")." OR $tablepre.content LIKE ".pwEscape("%$value%").")";
							}
						}
					}
					$keywhere && $keywhere = substr_replace($keywhere,'',0,3);
					!$keywhere && Showmsg('illegal_keyword');
					$sqlwhere .= " AND ($keywhere)";
				}
				if ($pwuser) {
					!str_replace('*','',$pwuser) && Showmsg('illegal_author');
					$uids = array();
					$pwuser = str_replace(array('%','_'),array('\%','\_'),addslashes(trim($pwuser)));
					$pwuser = str_replace('*','_',$pwuser);//noizyfeng
					$query = $db->query("SELECT uid FROM pw_members WHERE username LIKE ".pwEscape($pwuser));
					while ($rt = $db->fetch_array($query)) {
						$uids[] = $rt['uid'];
					}
					$uids && $uids = pwImplode($uids);
					!$uids && Showmsg('user_not_exists');
					$sqlwhere .= " AND t.authorid IN ($uids)";
				}
			}elseif (is_numeric($authorid)) {
				$sqlwhere .= " AND t.authorid = ".pwEscape($authorid);
			}
			$digest && $sch_area<2 && $sqlwhere .= " AND t.digest>0";
			if (!is_numeric($authorid) && (!$digest || $sch_time)) {
				if ($sch_time!='all') {
					!is_numeric($sch_time) && $sch_time = 86400;
					$_G['schtime']=='all' && $_G['schtime'] = 31536000;
					$sch_time>$_G['schtime'] && $sch_time = $_G['schtime'];
					$sqlwhere .= " AND t.postdate > ".pwEscape($timestamp-$sch_time);
				} elseif ($_G['schtime']!='all') {
					$sqlwhere .= " AND t.postdate > ".pwEscape($timestamp-$_G['schtime']);
				}
			}
			$limit = 'LIMIT 50';
			if (!$newatc) {
				!$db_maxresult && $db_maxresult = 500;
				$limit = pwLimit($db_maxresult);
			}
			$schsql = " t.tid FROM $sqltable WHERE $sqlwhere $orderby";
			if (strpos($sqltable,'tm')===false) {
				$schsql = str_replace(array(' t.',' t ','(t.'),array(' ',' ','('),$schsql);
			}
			$query = $db->query("SELECT $distinct$schsql $limit");
			while ($rt = $db->fetch_array($query)) {
				$total++;
				$schedid .= ",$rt[tid]";
			}
			$db->free_result($query);
			if ($schedid) {
				$schedid = substr($schedid,1);
				if ((int)$authorid<1) {
					$pwSQL = pwSqlSingle(array(
						'sorderby'	=> $sorderby,
						'schline'	=> $schline,
						'schtime'	=> $timestamp,
						'total'		=> $total,
						'schedid'	=> $schedid
					));
					$db->update("INSERT INTO pw_schcache SET $pwSQL");
					$sid = $db->insert_id();
					$db_schwait && writeover(D_P.'data/bbscache/schwait_cache.php','');
				}
			}
		}
	}
	if ($schedid) {
		$schdb = array();
		$pages = $addpage = $rawkeyword = $htmlurl = '';
		(int)$page<1 && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		if ($keyword) {
			$keyword_A = explode('|',$keyword);
			$rawkeyword = rawurlencode($keyword);
			$htmlurl = "&keyword=$rawkeyword";
		}
		$addpage = "&keyword=$rawkeyword";
		$schedid = pwImplode(explode(',',$schedid));
		$sqlwhere = "tid IN ($schedid)";
		$fidout && $sqlwhere .= " AND fid NOT IN ($fidout)";
		$orderby = str_replace('t.','',$orderby);
		$query = $db->query("SELECT tid,fid,titlefont,author,authorid,subject,postdate,lastpost,lastposter,hits,replies,locked,special,anonymous,topped,digest FROM pw_threads WHERE $sqlwhere $orderby $limit");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['anonymous'] && $rt['author']!=$windid) {
				if ($groupid!='3') continue;
				$rt['author'] = $db_anonymousname;
				$rt['authorid'] = 0;
			}
			if ($rt['titlefont']) {
				$titledetail = explode('~',$rt['titlefont']);
				if ($titledetail[0]) $rt['subject'] = "<font color=\"$titledetail[0]\">$rt[subject]</font>";
				if ($titledetail[1]) $rt['subject'] = "<b>$rt[subject]</b>";
				if ($titledetail[2]) $rt['subject'] = "<i>$rt[subject]</i>";
				if ($titledetail[3]) $rt['subject'] = "<u>$rt[subject]</u>";
			}
			foreach ($keyword_A as $value) {
				$value && $rt['subject'] = preg_replace('/(?<=[^\w=]|^)('.preg_quote($value,'/').')(?=[^\w=]|$)/si','<font color="red"><u>\\1</u></font>',$rt['subject']);
			}
			if ($rt['special']==1) {
				$rt['status'] = !$rt['locked'] ? 'vote' : 'votelock';
			} elseif ($rt['locked']>0) {
				$rt['status'] = $rt['locked']==1 ? 'topiclock' : 'topicclose';
			} else {
				$rt['status'] = $rt['replies']>=10 ? 'topichot' : 'topicnew';
			}
			$rt['forumname'] = $forum[$rt['fid']]['name'];
			$rt['postdate'] = get_date($rt['postdate'],"Y-m-d");
			$rt['lastpost'] = get_date($rt['lastpost']);
			$rt['lastposterraw'] = rawurlencode($rt['lastposter']);
			$schdb[] = $rt;
		}
		$db->free_result($query);
		if ($newatc && $total > 50) {
			$total = 50;
			$db->update("UPDATE pw_schcache SET total='50' WHERE sid=".pwEscape($sid));
		}
		if ($total > $db_perpage) {
			if (!$sid) {
				$ptable && $addpage .= "&ptable=$ptable";
				$digest && $addpage .= "&digest=$digest";
			} else {
				$addpage .= "&sid=$sid";
			}
			$authorid && $addpage .= "&authorid=$authorid";
			$seekfid && $addpage .= "&seekfid=$seekfid";
			$sch_area && $addpage .= "&sch_area=$sch_area";
			$pwuser && $addpage .= "&pwuser=$pwuser";/*用户*/
			$numofpage = ceil($total/$db_perpage);
			$pages = numofpage($total,$page,$numofpage,"search.php?step=2$addpage&#submit");
		}
	} else {
		Showmsg('search_none');
	}
	
?>