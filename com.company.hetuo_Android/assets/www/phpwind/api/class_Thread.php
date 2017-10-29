<?php

!defined('P_W') && exit('Forbidden');
//api mode 2

define('API_THREAD_FORUM_NOT_EXISTS', 200);
define('API_THREAD_FORUM_POST_CATEGORY', 201);
define('API_THREAD_AUTHOR_NOT_EXISTS', 202);
define('API_THREAD_TAG_LENGTH_LIMIT', 203);
define('API_THREAD_SUBJECT_LENGTH_LIMIT', 204);
define('API_THREAD_CONTENT_LENGTH_LIMIT', 205);
define('API_THREAD_SUBJECT_WORDSFB', 206);
define('API_THREAD_CONTENT_WORDSFB', 207);
define('API_THREAD_TAG_NUM_LIMIT', 208);
define('API_THREAD_ILLEGAL_TID', 209);
define('API_THREAD_MODIFY_ADMIN', 210);
define('API_THREAD_MODIFY_TIMELIMIT', 211);
define('API_THREAD_AUTHOR_ERROR', 212);

class Thread {

	var $base;
	var $db;

	function Thread($base) {
		$this->base = $base;
		$this->db = $base->db;
	}

	function getErrMsg($msg) {
		$arr = array(
			'postfunc_subject_limit' => array(API_THREAD_SUBJECT_LENGTH_LIMIT, 'Subject length error'),
			'title_wordsfb' => array(API_THREAD_SUBJECT_WORDSFB, 'Subject has illegal words'),
			'postfunc_content_limit' => array(API_THREAD_CONTENT_LENGTH_LIMIT, 'Content length error'),
			'content_wordsfb' => array(API_THREAD_CONTENT_WORDSFB, 'Content has illegal words'),
			'tag_length_limit' => array(API_THREAD_TAG_LENGTH_LIMIT, 'Tag length error'),
			'tags_num_limit' => array(API_THREAD_TAG_NUM_LIMIT, 'Tag num error')
		);
		return isset($arr[$msg]) ? $arr[$msg] : array(299, '');
	}

	function post($fid, $author, $title, $content, $tags = '', $convert = 1, $usesign = 1, $usehtml = 0, $topped = 0, $digest = 0, $p_type = '', $p_sub_type = '') {
		global $winddb,$winduid,$windid,$groupid,$_G,$SYSTEM,$db_ipban;
		require_once(R_P.'lib/forum.class.php');
		$pwforum = new PwForum($fid);
		if (!$pwforum->isForum()) {
			return new ApiResponse('API_THREAD_FORUM_NOT_EXISTS');
			//return new ErrorMsg(API_THREAD_FORUM_NOT_EXISTS, 'Forum not exists');
		}
		$winddb = $this->db->get_one("SELECT m.uid,m.username,m.password,m.safecv,m.email,m.oicq,m.groupid,m.memberid,m.groups,m.icon,m.regdate,m.honor,m.timedf,m.style,m.datefm,m.t_num,m.p_num,m.yz,m.newpm,m.userstatus,m.shortcut,md.postnum,md.rvrc,md.money,md.credit,md.currency,md.lastvisit,md.thisvisit,md.onlinetime,md.lastpost,md.todaypost,md.monthpost,md.onlineip,md.uploadtime,md.uploadnum,md.starttime,md.pwdctime,md.monoltime,md.digests,md.f_num,md.creditpop FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid WHERE m.username=" . pwEscape($author));
		if (empty($winddb)) {
			return new ApiResponse('API_THREAD_AUTHOR_NOT_EXISTS');
			//return new ErrorMsg(API_THREAD_AUTHOR_NOT_EXISTS, 'User not exists');
		}
		$winduid = $winddb['uid'];
		$groupid = $winddb['groupid'];
		$windid  = $winddb['username'];
		$groupid == '-1' && $groupid = $winddb['memberid'];

		if ($groupid == 6 || getstatus($winddb['userstatus'],1)) {//会员禁言
			return new ApiResponse('API_THREAD_THE_USER_BAN');
		}

		if ($db_ipban) {//IP禁止
			$onlineip = pwGetIp();
			$baniparray = explode(',',$db_ipban);
			foreach ($baniparray as $banip) {
				if ($banip && strpos(",$onlineip.",','.trim($banip).'.') !== false) {
					return new ApiResponse('API_THREAD_THE_IP_BAN');
				}
			}
		}

		if (file_exists(D_P."data/groupdb/group_$groupid.php")) {
			include Pcv(D_P."data/groupdb/group_$groupid.php");
		} else {
			include(D_P.'data/groupdb/group_1.php');
		}
		require_once(R_P . 'lib/post.class.php');
		require_once(R_P . 'require/bbscode.php');
		$pwpost = new PwPost($pwforum);
		$pwpost->errMode = true;

		require_once(R_P . 'lib/topicpost.class.php');
		$topicpost = new topicPost($pwpost);
		$topicpost->check();

		$postdata = new topicPostData($pwpost);

		include_once(D_P.'data/bbscache/cache_post.php');
		include_once(D_P.'data/bbscache/forum_typecache.php');
		$t_db = $topic_type_cache[$fid];
		$postdata->setWtype($p_type, $p_sub_type, 1, $t_db);
		$postdata->setTitle($title);
		$postdata->setContent($content);

		$postdata->setConvert($convert, 1);
		$postdata->setTags($tags);
		$postdata->setDigest($digest);
		$postdata->setTopped($topped);
		$postdata->setIfsign($usesign, $usehtml);

		if ($pwpost->errMsg && $msg = reset($pwpost->errMsg)) {
			return new ApiResponse($msg);
			//$errmsg = $this->getErrMsg($msg);
			//return new ErrorMsg($errmsg[0], $errmsg[1]);
		}
		$topicpost->execute($postdata);
		$tid = $topicpost->getNewId();
		return new ApiResponse($tid);
	}

	function getData($tids) {//获取帖子浏览数/回复数

		if (!$tids) {
			return new ApiResponse(false);
		}
		if (is_numeric($tids)) {
			$sql = ' tid=' . pwEscape($tids);
		} else {
			$sql = ' tid IN(' . pwImplode(explode(',',$tids)) . ')';
		}

		$datadb = array();
		$query = $this->db->query("SELECT tid,toolfield,hits,replies FROM pw_threads WHERE $sql");
		while ($rt = $this->db->fetch_array($query)) {
			$datadb[$rt['tid']] = $rt;
		}

		/*$pw_tmsgsdb = array();
		foreach (explode(',',$tids) as $value) {
			$pw_tmsgs = GetTtable($value);
			$pw_tmsgsdb[$pw_tmsgs][] = $value;
		}

		foreach ($pw_tmsgsdb as $key => $value) {
			$query = $this->db->query("SELECT tid,content FROM $key WHERE tid IN(" .pwImplode($value). ")");
			while ($rt = $this->db->fetch_array($query)) {
				$datadb[$rt['tid']]['content'] = $rt['content'];
			}
		}*/

		return new ApiResponse($datadb);
	}

	function downTopped ($tid) {//取消置顶贴
		$tid = $this->db->get_value('SELECT tid FROM pw_threads WHERE tid='.pwEscape($tid));

		if (!$tid) {
			return new ApiResponse(false);
		}

		$this->db->update('UPDATE pw_threads SET topped=0 WHERE tid='.pwEscape($tid));
		require_once(R_P.'require/updateforum.php');
		updatetop();
		return new ApiResponse(true);
	}

	function postModify($tid, $fid, $uid, $title, $content, $tags = '', $convert = 1, $usesign = 1, $usehtml = 0, $topped = 0, $digest = 0, $p_type = '', $p_sub_type = '') {
		global $winddb,$winduid,$windid,$groupid,$_G,$SYSTEM,$timestamp;
		require_once(R_P.'lib/forum.class.php');
		$pwforum = new PwForum($fid);
		if (!$pwforum->isForum()) {
			return new ErrorMsg(API_THREAD_FORUM_NOT_EXISTS, 'Forum not exists');
		}
		$winddb = $this->db->get_one("SELECT m.uid,m.username,m.password,m.safecv,m.email,m.oicq,m.groupid,m.memberid,m.groups,m.icon,m.regdate,m.honor,m.timedf,m.style,m.datefm,m.t_num,m.p_num,m.yz,m.newpm,m.userstatus,m.shortcut,md.postnum,md.rvrc,md.money,md.credit,md.currency,md.lastvisit,md.thisvisit,md.onlinetime,md.lastpost,md.todaypost,md.monthpost,md.onlineip,md.uploadtime,md.uploadnum,md.starttime,md.pwdctime,md.monoltime,md.digests,md.f_num,md.creditpop FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid WHERE m.uid=" . pwEscape($uid));
		if (empty($winddb)) {
			return new ErrorMsg(API_THREAD_AUTHOR_NOT_EXISTS, 'User not exists');
		}
		$winduid = $winddb['uid'];
		$groupid = $winddb['groupid'];
		$windid  = $winddb['username'];
		$groupid == '-1' && $groupid = $winddb['memberid'];
		if (file_exists(D_P."data/groupdb/group_$groupid.php")) {
			include Pcv(D_P."data/groupdb/group_$groupid.php");
		} else {
			include(D_P.'data/groupdb/group_1.php');
		}
		require_once(R_P . 'lib/post.class.php');
		require_once(R_P . 'require/bbscode.php');
		$pwpost = new PwPost($pwforum);
		$pwpost->errMode = true;

		require_once(R_P . 'lib/postmodify.class.php');
		$postmodify = new topicModify($tid, 0, $pwpost);
		$atcdb = $postmodify->init();
		if (empty($atcdb) || $atcdb['fid'] != $fid) {
			return new ErrorMsg(API_THREAD_ILLEGAL_TID, 'The tid is illegal');
		}

		if ($winduid != $atcdb['authorid'] && $groupid != 3 && $groupid != 4) {
			$authordb = $this->db->get_one("SELECT groupid FROM pw_members WHERE uid=" . pwEscape($atcdb['authorid']));
			if (($authordb['groupid'] == 3 || $authordb['groupid'] == 4)) {
				return new ErrorMsg(API_THREAD_MODIFY_ADMIN, 'The tid is not modify');
			}
		}

		if ($_G['edittime'] && ($timestamp - $atcdb['postdate']) > $_G['edittime'] * 60) {
			return new ErrorMsg(API_THREAD_MODIFY_TIMELIMIT, 'The modify time limit');
		}

		$postdata = new topicPostData($pwpost);

		include_once(D_P.'data/bbscache/cache_post.php');
		$t_db = $topic_type_cache[$fid];
		$postdata->setWtype($p_type, $p_sub_type, 0, $t_db);
		$postdata->initData($postmodify);
		$postdata->setTitle($title);
		$postdata->setContent($content);
		$postdata->setConvert($convert, 1);
		$postdata->setTags($tags);
		$postdata->setDigest($digest);
		$postdata->setTopped($topped);
		$postdata->setIfsign($usesign, $usehtml);

		if ($pwpost->errMsg && $msg = reset($pwpost->errMsg)) {
			$errmsg = $this->getErrMsg($msg);
			return new ErrorMsg($errmsg[0], $errmsg[1]);
		}

		$postmodify->execute($postdata);
		return new ApiResponse(true);

	}

	function postDelete($tids,$uid) {
		global $db_recycle,$db_ifpwcache;

		$tiddb = explode(',',$tids);
		$delids = array();
		foreach ($tiddb as $key => $value) {
			if (is_numeric($value)) {
				$delids[] = $value;
			}
		}

		if (!$delids) {
			return new ApiResponse(false);
		}
		foreach ($readdb as $key => $read) {
			if ($read['authorid'] != $uid) {
				return new ErrorMsg(API_THREAD_AUTHOR_ERROR, 'The author is not right');
			}
		}
		$delarticle = L::loadClass('DelArticle');
		$readdb = $delarticle->getTopicDb('tid ' . $delarticle->sqlFormatByIds($delids));
		$delarticle->delTopic($readdb, 0);

		if ($db_ifpwcache ^ 1) {
			$this->db->update("DELETE FROM pw_elements WHERE type !='usersort' AND id IN(" . pwImplode($delids) . ')');
		}
		P_unlink(D_P.'data/bbscache/c_cache.php');
		return new ApiResponse(true);
	}

    function ppost($params) {
        $author = $params['data']['author'];
        $fid = $params['data']['fid'];
        if ($params['threads'] && is_array($params['threads']) && $fid  && $author) {
            $tiddb = array();
            foreach($params['threads'] as $key => $threads) {
                @extract($threads);
                $result = $this->post($fid, $author, $subject, $content, $tags, 1 , 1, 1 , $topped , $digest);
                if (strtolower(get_class($result)) == 'apiresponse') {
                    $tid = $result->getResult();
                }
                $tiddb[$key] = $tid;
            }
            return new ApiResponse($tiddb);
        } else {
            return new ApiResponse(false);
        }
    }
}
?>