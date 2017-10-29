<?php
!function_exists('readover') && exit('Forbidden');

class GetInfo{
	var $db;
	var $cachenum;
	var $reality;
	/*
	 * __construct
	 */
	function GetInfo($init,$reality=false){
		global $db,$db_cachenum;
		if (M_E != $init) {
			Showmsg('element_can_not_construct');
		} else {
			$this->db = $db;
			$this->cachenum = intval($db_cachenum) ? intval($db_cachenum) : 20;
			$this->reality = $reality;
		}
	}
	/**
	 * Singleton mode
	 * creat a Singleton object
	 * @return object
	 */
	function &getInstance($reality=false) {
		static $instance = array();
		if (!$instance) $instance[0] =& new GetInfo(M_E,$reality);
		return $instance[0];
	}

	/**
	 * get post lists
	 * $type must in array('newsubject','newreply','replysort','hitsort')
	 *
	 * @param string $type
	 * @param int $fid
	 * @param int $num
	 * @param int $hour
	 * @return array
	 */
	function getPostList($type,$fid,$num=0,$hour=0,$special=0){
		global $db_ptable,$timestamp;
		$posttype = array('newsubject','newreply','replysort','hitsort');

		if (!in_array($type,$posttype)) return false;
		!$fid && $fid = getCommonFid();
		$num	= (int) $num;
		$hour 	= (int) $hour;
		$special= (int) $special;
		!$num && $num = $this->cachenum;
		$time	= $hour ? (strlen($hour)==10 ? $hour :$timestamp-intval($hour)*3600) : 0;
		$sqladd = '';
		if ($type == 'replysort' || $type == 'newsubject') {
			$forceindex = '';
			$sqladd .= 'AND t.special='.pwEscape($special);
			$sqladd .= $time ? ' AND t.postdate>'.pwEscape($time) : '';
			if ($fid) {
				if (strpos($fid,',') === false) {
					$fid = trim($fid,"'");
					$sqladd .= " AND t.fid=".pwEscape($fid,false);
					if ($type == 'newsubject') {
						$forumpost = $this->db->get_value("SELECT topic FROM pw_forumdata WHERE fid=".pwEscape($fid,false));
						if ($forumpost<100) {
							$forceindex = 'FORCE INDEX(lastpost)';
						} else {
							$forceindex = 'FORCE INDEX(postdate)';
						}
					}
				} else {
					$sqladd .= " AND t.fid IN ($fid) ";
					if ($type == 'newsubject') {
						$forceindex = 'FORCE INDEX(postdate)';
					}
				}
			}
		} else {
			$sqladd .= $time ? ' AND postdate>'.pwEscape($time) : '';
			if ($fid) {
				if (is_numeric($fid)) {
					$sqladd .= " AND fid =$fid ";
				} else {
					$sqladd .= " AND fid IN ($fid) ";
				}
			}
		}

		if ($type=='newsubject') {
			if ($this->reality == false) {
				$sql = "SELECT t.tid AS id,t.postdate AS value FROM pw_threads t $forceindex WHERE t.ifcheck=1 AND t.topped=0 AND t.anonymous != 1 $sqladd ORDER BY t.postdate DESC ".pwLimit($num);
			} else {
				if ($special == 2) {
					$sql = "SELECT a.*,t.fid FROM pw_threads t LEFT JOIN pw_activity a ON t.tid=a.tid WHERE t.ifcheck='1' AND t.anonymous != 1 $sqladd ORDER BY t.tid DESC ".pwLimit($num);
				} elseif ($special == 3) {
					global $db_moneyname,$db_rvrcname,$db_creditname,$db_currencyname,$_CREDITDB;
					$cType = array(
								'money'		=> $db_moneyname,
								'rvrc'		=> $db_rvrcname,
								'credit'	=> $db_creditname,
								'currency'	=> $db_currencyname
							);
					foreach ($_CREDITDB as $k => $v) {
						$cType[$k] = $v[0];
					}
					$sql = "SELECT r.tid,r.cbtype,r.catype,r.cbval,r.caval,r.timelimit,t.fid,t.author,t.authorid,t.subject,t.type,t.postdate,t.hits,t.replies FROM pw_threads t LEFT JOIN pw_reward r ON t.tid=r.tid WHERE t.ifcheck='1' AND t.anonymous != 1 $sqladd ORDER BY t.tid DESC ".pwLimit($num);
				} elseif ($special == 4) {
					$sql = "SELECT tr.tid,tr.name,tr.icon,tr.price,t.fid FROM pw_threads t LEFT JOIN pw_trade tr ON t.tid=tr.tid WHERE t.ifcheck='1' AND t.anonymous != 1 $sqladd ORDER BY t.tid DESC ".pwLimit($num);
				} else {
					$sql = "SELECT t.tid,t.fid,t.author,t.authorid,t.subject,t.type,t.postdate,t.hits,t.replies FROM pw_threads t $forceindex WHERE ifcheck=1 AND topped=0 AND t.anonymous != 1 $sqladd ORDER BY postdate DESC ".pwLimit($num);
				}
			}
		} elseif ($type=='newreply') {
			$pw_posts = GetPtable($db_ptable);
			$sql	= "SELECT DISTINCT tid FROM $pw_posts FORCE INDEX(PRIMARY) WHERE ifcheck=1 $sqladd ORDER BY pid DESC ".pwLimit($num);
			$tids 	= array();
			$query 	= $this->db->query($sql);
			while ($reply = $this->db->fetch_array($query)) {
				$tids[] = $reply['tid'];
			}
			if ($tids) {
				if ($this->reality == false) {
					$sql = "SELECT tid AS id,postdate AS value FROM pw_threads WHERE tid IN(".pwImplode($tids).") AND anonymous != 1 ORDER BY lastpost DESC";
				} else {
					$sql = "SELECT tid,fid,author,authorid,subject,type,postdate,hits,replies FROM pw_threads WHERE tid IN(".pwImplode($tids).") AND anonymous != 1 ORDER BY lastpost DESC";
				}
			} else {
				return false;
			}
		} elseif ($type=='replysort') {
			if ($this->reality == false) {
				$sql = "SELECT t.tid AS id,t.replies AS value,t.postdate AS addition FROM pw_threads t WHERE t.ifcheck='1' AND t.replies>0 $sqladd ORDER BY t.replies DESC ".pwLimit($num);
			} else {
				if ($special == 2) {
					$sql = "SELECT a.*,t.fid FROM pw_threads t LEFT JOIN pw_activity a ON t.tid=a.tid WHERE t.ifcheck='1' AND t.replies>0 $sqladd ORDER BY t.replies DESC ".pwLimit($num);
				} elseif ($special == 3) {
					global $db_moneyname,$db_rvrcname,$db_creditname,$db_currencyname,$_CREDITDB;
					$cType = array(
								'money'		=> $db_moneyname,
								'rvrc'		=> $db_rvrcname,
								'credit'	=> $db_creditname,
								'currency'	=> $db_currencyname
							);
					foreach ($_CREDITDB as $k => $v) {
						$cType[$k] = $v[0];
					}
					$sql = "SELECT r.tid,r.cbtype,r.catype,r.cbval,r.caval,r.timelimit,t.fid,t.author,t.authorid,t.subject,t.type,t.postdate,t.hits,t.replies FROM pw_threads t LEFT JOIN pw_reward r ON t.tid=r.tid WHERE t.ifcheck='1' AND t.replies>0 $sqladd ORDER BY t.replies DESC ".pwLimit($num);
				} elseif ($special == 4) {
					$sql = "SELECT tr.tid,tr.name,tr.icon,tr.price,t.fid,t.postdate FROM pw_threads t LEFT JOIN pw_trade tr ON t.tid=tr.tid WHERE t.ifcheck='1' AND t.replies>0 $sqladd ORDER BY t.replies DESC ".pwLimit($num);
				} else {
					$sql = "SELECT t.tid,t.fid,t.author,t.authorid,t.subject,t.type,t.postdate,t.hits,t.replies FROM pw_threads t WHERE t.ifcheck='1' AND t.replies>0 $sqladd ORDER BY t.replies DESC ".pwLimit($num);
				}
			}
		} elseif ($type=='hitsort') {
			if ($this->reality == false) {
				$sql = "SELECT tid AS id,hits AS value,postdate AS addition FROM pw_threads WHERE ifcheck='1' AND hits>0 $sqladd ORDER BY hits DESC ".pwLimit($num);
			} else {
				$sql = "SELECT tid,fid,author,authorid,subject,type,postdate,hits,replies FROM pw_threads WHERE ifcheck='1' AND hits>0 $sqladd ORDER BY hits DESC ".pwLimit($num);
			}
		}
		$posts = array();
		$query = $this->db->query($sql);
		while($post = $this->db->fetch_array($query)){
			if ($this->reality == false) {
				$type !='newreply' && $post['special'] = $special;
				$posts[] = $post;
			} else {
				$tem = array();
				$tem['url'] = 'read.php?tid='.$post['tid'];
				if ($type=='replysort' || $type=='newsubject') {
					if ($special == 2) {
						$tem['title'] 	= $post['subject'];
						$tem['value'] 	= $post['deadline'];
						$tem['image']	= '';
					} elseif ($special == 3) {
						$tem['title'] 	= $post['subject'];
						$tem['value'] 	= $cType[$post['cbtype']].":".$post['cbval'];
						$tem['image']	= '';
					} elseif ($special == 4) {
						$tem['title'] 	= $post['name'];
						$tem['value'] 	= $post['price'];
						$pic = geturl($post['icon'],'show',1);
						if(is_array($pic)){
							$tem['image'] = $pic[0];
						} else {
							$tem['image'] = 'images/noproduct.gif';
						}
					} else {
						$tem['title'] 	= $post['subject'];
						$tem['value'] 	= $type=='replysort' ? $post['replies'] : $post['postdate'];
						$tem['image']	= '';
					}
				} elseif ($type=='hitsort') {
					$tem['title'] 	= $post['subject'];
					$tem['value'] 	= $post['hits'];
					$tem['image']	= '';
				} else {
					$tem['title'] 	= $post['subject'];
					$tem['value'] 	= $post['postdate'];
					$tem['image']	= '';
				}
				$tem['forumname']	= getForumName($post['fid']);
				$tem['forumurl']	= getForumUrl($post['fid']);
				$tem['addition']= $post;
				$posts[] = $tem;
			}
		}
		return $posts;
	}

	/**
	 * get digest lists
	 * $type must in array(1,2)
	 *
	 * @param string $type
	 * @param int $fid
	 * @param int $num
	 * @param int $hour
	 * @return array
	 */
	function digestSubject($type,$fid,$num=0,$hour=0){
		global $timestamp;
		$digesttype = array(1,2);
		!in_array($type,$digesttype) && $type = 0;
		$type	= (int) $type;
		$num	= (int) $num;
		$hour 	= (int) $hour;
		!$fid && $fid = getCommonFid();
		!$num && $num = $this->cachenum;
		$time	= $hour ? (strlen($hour)==10 ? $hour :$timestamp-intval($hour)*3600) : 0;
		$sqladd = '';
		$sqladd .= $time ? ' AND postdate>'.pwEscape($time) : '';
		$fid && $sqladd .= " AND fid IN ($fid) ";
		$sqladd .= $type ? ' AND digest='.pwEscape($type) : ' AND digest IN(1,2)';
		if ($this->reality == false) {
			$sql = "SELECT tid AS id,lastpost AS value FROM pw_threads WHERE ifcheck='1' $sqladd ORDER BY lastpost DESC ".pwLimit($num);
		} else {
			$sql = "SELECT tid,fid,author,authorid,subject,type,postdate,hits,replies FROM pw_threads WHERE ifcheck='1' $sqladd ORDER BY lastpost DESC ".pwLimit($num);
		}
		$posts = array();
		$query = $this->db->query($sql);
		while($rt = $this->db->fetch_array($query)){
			if ($this->reality) {
				$post = array();
				$post['url'] 	= 'read.php?tid='.$rt['tid'];
				$post['title'] 	= $rt['subject'];
				$post['value'] 	= $rt['postdate'];
				$post['image']	= '';
				$post['forumname']	= getForumName($rt['fid']);
				$post['forumurl']	= getForumUrl($rt['fid']);
				$post['addition'] = $rt;
				$posts[] = $post;
			} else {
				$posts[] = $rt;
			}
		}
		return $posts;
	}
	/**
	 * get topped lists
	 * $type must in array(1,2,3)
	 *
	 * @param int $type
	 * @param int $fid
	 * @param int $num
	 * @param int $hour
	 * @return array
	 */
	function topSubject($type,$fid=0,$num=0,$hour=0){
		global $timestamp;
		$toppedtype = array(1,2,3);
		!in_array($type,$toppedtype) && $type = 0;
		$type	= (int) $type;
		$num	= (int) $num;
		$hour 	= (int) $hour;
		!$fid && $fid = getCommonFid();
		!$num && $num = $this->cachenum;
		$time	= $hour ? (strlen($hour)==10 ? $hour :$timestamp-intval($hour)*3600) : 0;
		$sqladd = '';
		$sqladd .= $time ? ' AND postdate>'.pwEscape($time) : '';
		$fid && $sqladd .= " AND fid IN ($fid) ";
		$sqladd .= $type ? ' AND topped='.pwEscape($type) : ' AND topped IN(1,2,3)';
		if ($this->reality == false) {
			$sql = "SELECT tid AS id,lastpost AS value FROM pw_threads WHERE ifcheck='1' $sqladd ORDER BY lastpost DESC ".pwLimit($num);
		} else {
			$sql = "SELECT tid,fid,author,authorid,subject,type,postdate,hits,replies FROM pw_threads WHERE ifcheck='1' $sqladd ORDER BY lastpost DESC ".pwLimit($num);
		}
		$posts = array();
		$query = $this->db->query($sql);
		while($rt = $this->db->fetch_array($query)){
			$post = array();
			$post['url'] 	= 'read.php?tid='.$rt['tid'];
			$post['title'] 	= $rt['subject'];
			$post['value'] 	= $rt['postdate'];
			$post['image']	= '';
			$post['forumname']	= getForumName($rt['fid']);
			$post['forumurl']	= getForumUrl($rt['fid']);
			$post['addition'] = $rt;
			$posts[] = $post;
		}
		return $posts;
	}
	/**
	 * get new attachs
	 * $type must in array('img','txt','zip')
	 * @param int $type
	 * @param int $fid
	 * @param int $num
	 * @param int $hour
	 * @return array
	 */
	function newAttach($type,$fid=0,$num=0,$hour=0){
		global $timestamp,$db_ftpweb,$attachpath;
		$attachtype = array('img','txt','zip');
		if (!in_array($type,$attachtype)) return false;
		$num	= (int) $num;
		$hour 	= (int) $hour;
		!$fid && $fid = getCommonFid();
		!$num && $num = $this->cachenum;
		$time	= $hour ? (strlen($hour)==10 ? $hour :$timestamp-intval($hour)*3600) : 0;
		$sqladd = '';
		$sqladd .= $time ? ' AND a.uploadtime>'.pwEscape($time) : '';
		$fid && $sqladd .= " AND a.fid IN ($fid) ";
		if ($this->reality == false) {
			$sql = "SELECT a.tid as id,a.aid AS value,a.attachurl AS addition,a.ifthumb as special FROM pw_attachs a LEFT JOIN pw_threads t ON a.tid=t.tid WHERE a.type=".pwEscape($type,1)." AND a.pid=0 AND a.needrvrc=0 AND t.ifcheck='1' $sqladd GROUP BY a.tid ORDER BY a.aid DESC ".pwLimit($num);
		} else {
			$sql = "SELECT a.tid,a.attachurl,t.author,t.authorid,t.subject,a.ifthumb FROM pw_attachs a LEFT JOIN pw_threads t ON a.tid=t.tid WHERE a.type=".pwEscape($type,1)." AND a.pid=0 AND a.needrvrc=0 AND t.ifcheck='1' $sqladd ORDER BY a.aid DESC ".pwLimit($num);
		}

		$attachs = array();
		$query = $this->db->query($sql);
		while($attach = $this->db->fetch_array($query)){
			$tid = $attach['tid'] ? $attach['tid'] : $attach['id'];
			$pw_tmsgs = GetTtable($tid);
			$content = $this->db->get_value("SELECT content FROM $pw_tmsgs WHERE tid=".pwEscape($tid));
			$atc_content = substrs(stripWindCode($content),30);
			if ($this->reality == true) {
				$pic = geturl($attach['attachurl'],'show',$attach['ifthumb']);
				if (is_array($pic)) {
					$tem = array();
					$tem['url'] 	= 'read.php?tid='.$attach['tid'];
					$tem['title'] 	= $attach['subject'];
					$tem['value'] 	= $atc_content;
					$tem['image']	= $pic[0];
					$tem['forumname']	= getForumName($attach['fid']);
					$tem['forumurl']	= getForumUrl($attach['fid']);
					$tem['addition']= $attach;
				}
				$attachs[] = $tem;
			} else {
				if ($attachs[$attach['id']]) continue;
				$additions = array('0' => $attach['addition'],'1' => $atc_content);
				$addition = addslashes(serialize($additions));
				$attach['addition'] = $addition;
				$attachs[$attach['id']] = $attach;
			}
		}
		return $attachs;
	}
	/**
	 * get user sort
	 * $type must in array('money','rvrc','credit','currency','todaypost','monthpost','postnum','monoltime','onlinetime','digests')
	 * or is_numeric and must in $GLOBALS['_CREDITDB']
	 *
	 * @param string or int $type
	 * @param int $num
	 * @return array
	 */
	function userSort($type,$num){
		$marktype = array('money','rvrc','credit','currency','todaypost','monthpost','postnum','monoltime','onlinetime','digests','f_num');
		if (!in_array($type,$marktype) && !is_numeric($type)) return false;
		$num = (int) $num;
		!$num && $num = $this->cachenum;
		$this->reality == true && require_once(R_P.'require/showimg.php');
		if (in_array($type,array('postnum','onlinetime','rvrc','money','credit','currency','digests'))) {
			if ($this->reality == false) {
				$sql = "SELECT md.uid as id,md.$type as value,m.username as addition FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.postnum>0 ORDER BY md.$type DESC ".pwLimit($num);
			} else {
				$sql = "SELECT md.uid,md.$type as value,m.username,m.icon,m.groupid,m.memberid FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.postnum>0 ORDER BY md.$type DESC ".pwLimit($num);
			}
		} elseif ($type == 'f_num') {
			if ($this->reality == false) {
				$sql = "SELECT f.uid AS id, COUNT(*) AS value, m.username AS addition FROM pw_friends f LEFT JOIN pw_members m USING(uid) GROUP BY m.uid".pwLimit($num);
			} else {
				$sql = "SELECT f.uid, COUNT(*) AS value, m.username, m.icon, m.groupid, m.memberid FROM pw_friends f LEFT JOIN pw_members m USING(uid) GROUP BY m.uid".pwLimit($num);
			}
		} elseif ($type == 'todaypost') {
			$tdtime	= PwStrtoTime(get_date($GLOBALS['timestamp'],'Y-m-d'));
			if ($this->reality == false) {
				$sql = "SELECT md.uid as id,md.todaypost as value,m.username as addition FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastpost>".pwEscape($tdtime)." AND md.postnum>0 ORDER BY md.todaypost DESC ".pwLimit($num);
			} else {
				$sql = "SELECT md.uid,md.todaypost as value,m.username,m.icon,m.groupid,m.memberid FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastpost>".pwEscape($tdtime)." AND md.postnum>0 ORDER BY md.todaypost DESC ".pwLimit($num);
			}
		} elseif ($type == 'monthpost') {
			$montime = PwStrtoTime(get_date($GLOBALS['timestamp'],'Y-m').'-1');
			if ($this->reality == false) {
				$sql = "SELECT md.uid as id,md.monthpost as value,m.username as addition FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastpost>".pwEscape($montime)." AND md.postnum>0 ORDER BY md.monthpost DESC ".pwLimit($num);
			} else {
				$sql = "SELECT md.uid,md.monthpost as value,m.username,m.icon,m.groupid,m.memberid FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastpost>".pwEscape($montime)." AND md.postnum>0 ORDER BY md.monthpost DESC ".pwLimit($num);
			}
		} elseif ($type == 'monoltime') {
			$montime = PwStrtoTime(get_date($GLOBALS['timestamp'],'Y-m').'-1');
			if ($this->reality == false) {
				$sql = "SELECT md.uid as id,md.monoltime as value,m.username as addition FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastvisit>".pwEscape($montime)." AND md.postnum>0 ORDER BY md.monoltime DESC ".pwLimit($num);
			} else {
				$sql = "SELECT md.uid,md.monoltime as value,m.username,m.icon,m.groupid,m.memberid FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastvisit>".pwEscape($montime)." AND md.postnum>0 ORDER BY md.monoltime DESC ".pwLimit($num);
			}
		} elseif (is_numeric($type) && $GLOBALS['_CREDITDB'][$type]) {
			if ($this->reality == false) {
				$sql = "SELECT mc.uid as id,mc.value,m.username as addition FROM pw_membercredit mc LEFT JOIN pw_members m USING(uid) WHERE mc.cid=".pwEscape($type)."ORDER BY mc.value DESC ".pwLimit($num);
			} else {
				$sql = "SELECT mc.uid,mc.value,m.username,m.icon,m.groupid,m.memberid FROM pw_membercredit mc LEFT JOIN pw_members m USING(uid) WHERE mc.cid=".pwEscape($type)."ORDER BY mc.value DESC ".pwLimit($num);
			}
		} else {
			return false;
		}
		$member = array();
		$query = $this->db->query($sql);
		while ($rt = $this->db->fetch_array($query)) {
			if ($type == 'rvrc') {
				$rt['value'] = floor($rt['value']/10);
			} elseif($type == 'onlinetime' || $type == 'monoltime') {
				$rt['value'] = floor($rt['value']/3600);
			}
			if ($this->reality == false) {
				$rt['type'] = 'usersort';
				$rt['mark'] = $type;
				$member[] = $rt;
			} else {
				$tem = array();
				$tem['url'] 	= 'u.php?action=show&uid='.$rt['uid'];
				$tem['title'] 	= $rt['username'];
				$tem['value'] 	= $rt['value'];
				if ($rt['icon']) {
					$pic = showfacedesign($rt['icon'],true);
					if (is_array($pic)) {
						$tem['image'] = $pic[0];
					} else {
						$tem['image'] = '';
					}
				} else {
					$tem['image'] = '';
				}
				$tem['addition']= $rt;
				$member[] = $tem;
			}
		}
		return $member;
	}
	/**
	 * GET newest or oldest members
	 *
	 * @param string $type
	 * @param int $num
	 * @return array
	 */
	function getMembers($type,$num){
		in_array($type,array('new','old')) || $type = 'new';
		$num = intval($num) ? intval($num) : $this->cachenum;
		$order = $type=='new'? 'DESC':'';
		$sql = "SELECT uid,username FROM pw_members ORDER BY id $order".pwLimit($num);
		$member = array();
		$query = $this->db->query($sql);
		while ($rt = $this->db->fetch_array($query)) {
			$tem = array();
			$tem['url'] 	= 'u.php?action=show&uid='.$rt['uid'];
			$tem['title'] 	= $rt['username'];
			$tem['value'] 	= '';
			$tem['image']	= '';
			$tem['addition']= $rt;
			$member[] = $tem;
		}
		return $member;
	}

	/**
	 * get new images
	 *
	 * @param int $fid
	 * @param int $num
	 * @return array
	 */
	function newPic($fid=0,$num=0){
		!$fid && $fid = getCommonFid();
		$num 	= intval($num) ? intval($num) : $this->cachenum;
		$sqladd = '';
		$fid && $sqladd .= " AND a.fid IN ($fid) ";
		$newpic = array();
		$query  = $this->db->query("SELECT a.tid,a.attachurl,t.author,t.authorid,t.subject FROM pw_attachs a FORCE INDEX (PRIMARY) LEFT JOIN pw_threads t ON a.tid=t.tid WHERE a.type='img' AND a.pid=0 $sqladd ORDER BY a.aid DESC ".pwLimit($num));
		while($rt = $this->db->fetch_array($query)){
			$pic = geturl($rt['attachurl'],'show');
			if(is_array($pic)){
				$rt['attachurl'] = $pic[0];
				$newpic[] = $rt;
			}
		}
		return $newpic;
	}

	/**
	 * get hot favor
	 *
	 * @param int $fid
	 * @param int $num
	 * @return array
	 */
	function hotfavor($fid=0,$num=0) {
		global $timestamp;
		!$fid && $fid = getCommonFid();
		$num 	= intval($num) ? intval($num) : $this->cachenum;
		$sqladd = '';
		$fid && $sqladd .= " AND fid IN ($fid) ";
		$hotfavor = array();

		$query  = $this->db->query("SELECT tid as id,fid as mark,favors as value FROM pw_threads WHERE favors>0 AND postdate >=".pwEscape($timestamp-360*24*3600)." $sqladd ORDER BY favors DESC ".pwLimit($num));
		while($rt = $this->db->fetch_array($query)){
			$hotfavor[] = $rt;
		}
		return $hotfavor;
	}

	function gethotfavor($fid=0,$num=0) {
		global $timestamp,$forum;
		!$fid && $fid = getCommonFid();
		$num 	= intval($num) ? intval($num) : $this->cachenum;
		$sqladd = '';
		$fid && $sqladd .= " AND fid IN ($fid) ";
		isset($forum) || include(D_P.'data/bbscache/forum_cache.php');
		$favors = array();
		$query = $this->db->query("SELECT tid,fid,author,authorid,subject,postdate,hits,replies,favors FROM pw_threads WHERE favors>0 AND postdate >=".pwEscape($timestamp-360*24*3600)." $sqladd ORDER BY favors DESC ".pwLimit($num));
		while ($rt = $this->db->fetch_array($query)) {
			$favor = array();
			$favor['url'] 	= 'read.php?tid='.$rt['tid'];
			$favor['title'] = $rt['subject'];
			$favor['value'] = $rt['favors'];
			$favor['posttime'] = get_date($rt['postdate']);
			$favor['forum'] = $forum[$rt['fid']]['name'];
			$favor['image']	= '';
			$favor['addition'] = $rt;
			$favors[] = $favor;
		}
		return $favors;
	}
}
?>