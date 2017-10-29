<?php
!defined('P_W') && exit('Forbidden');

class replyPost {

	var $db;
	var $post;
	var $forum;
	var $postdata;

	var $data;
	var $att;

	var $tid;
	var $tpcArr;

	function replyPost(&$post) {
		global $db;
		$this->db =& $db;
		$this->post =& $post;
		$this->forum =& $post->forum;
		$this->type = 'Reply';
	}

	function setTpc($arr) {
		$this->tpcArr = $arr;
		$this->tid = $this->tpcArr['tid'];
	}

	function creditSet() {
		static $creditset = null;
		if (!isset($creditset)) {
			global $db_creditset, $credit;
			require_once(R_P.'require/credit.php');
			$creditset = $credit->creditset($this->forum->creditset, $db_creditset);
			$creditset = $creditset[$this->type];
		}
		return $creditset;
	}

	function check() {
		$this->post->checkUserCredit($this->creditSet());
	}

	function setPostData(&$postdata) {
		$this->postdata =& $postdata;
		$this->att =& $postdata->att;
		$this->data = $postdata->getData();
		if (stripslashes($this->data['title']) == 'Re:' . $this->tpcArr['subject']) {
			$this->data['title'] = '';
		}
	}

	/**
	 * @author papa
	 * @param $pid
	 * @return unknown_type
	 */
	function setPostFloor($pid){
		$sql = "INSERT INTO pw_postsfloor SET pid=". pwEscape($pid) .", tid=". pwEscape($this->tid);
		$this->db->update($sql);
	}

	function execute($postdata) {
		global $timestamp,$db_ptable,$onlineip,$db_plist;
		$this->setPostData($postdata);

		if ($db_plist && count($db_plist)>1) {
			$this->db->update("INSERT INTO pw_pidtmp(pid) VALUES('')");
			$pid = $this->db->insert_id();
		} else {
			$pid = '';
		}
		$ipTable = L::loadClass('IPTable');

		$pwSQL = pwSqlSingle(array(
			'pid'		=> $pid,							'fid'		=> $this->data['fid'],
			'tid'		=> $this->tid,						'aid'		=> $this->data['aid'],
			'author'	=> $this->data['author'],			'authorid'	=> $this->data['authorid'],
			'icon'		=> $this->data['icon'],				'postdate'	=> $timestamp,
			'subject'	=> $this->data['title'],			'userip'	=> $onlineip,
			'ifsign'	=> $this->data['ifsign'],			'ipfrom'	=> $ipTable->getIpFrom($onlineip),
			'ifconvert'	=> $this->data['convert'],			'ifwordsfb'	=> $this->data['ifwordsfb'],
			'ifcheck'	=> $this->data['ifcheck'],			'content'	=> $this->data['content'],
			'anonymous'	=> $this->data['anonymous'],		'ifhide'	=> $this->data['hideatt']
		));
		$pw_posts = GetPtable($this->tpcArr['ptable']);
		$this->db->update("INSERT INTO $pw_posts SET $pwSQL");
		!$pid && $pid = $this->db->insert_id();
		$this->tpcArr['openIndex'] && $this->setPostFloor($pid);
		$this->pid = $pid;
		if (is_object($this->att) && ($aids = $this->att->getAids())) {
			$this->db->update("UPDATE pw_attachs SET " . pwSqlSingle(array('tid' => $this->tid, 'pid' => $this->pid)) . ' WHERE aid IN(' . pwImplode($aids) . ')');
		}
		if ($this->data['ifcheck'] == 1) {
			$sqladd1 = '';
			$sqladd = array('lastposter' => $this->data['lastposter']);
			$this->tpcArr['locked'] < 3 && $this->tpcArr['lastpost'] < $timestamp && $sqladd['lastpost'] = $timestamp;
			$this->data['ifupload'] && $sqladd['ifupload'] = $this->data['ifupload'];
			$ret = $this->sendMail();
			if ($ret & 2) {
				$sqladd['ifmail'] = 4;
			} elseif ($ret & 1) {
				$sqladd1 = "ifmail=ifmail-1,";
			}
			$this->db->update("UPDATE pw_threads SET {$sqladd1}replies=replies+1,hits=hits+1," . pwSqlSingle($sqladd) . " WHERE tid=" . pwEscape($this->tid));

			if (getstatus($this->tpcArr['tpcstatus'], 1)) {
				$this->db->update("UPDATE pw_argument SET lastpost=" . pwEscape($timestamp) . ' WHERE tid=' . pwEscape($this->tid));
			}
		}
		$this->post->updateUserInfo($this->type, $this->creditSet(), $this->data['content']);
		$this->afterReply();
	}

	function sendMail() {
		global $db_replysendmail,$db_replysitemail;
		$ret = 0;
		if ($this->data['authorid'] == $this->tpcArr['authorid']) {
			return $ret;
		}
		if ($db_replysendmail == 1 && ($this->tpcArr['ifmail'] == 1 || $this->tpcArr['ifmail'] == 3)) {
			$receiver  = $this->tpcArr['author'];
			$old_title = $this->tpcArr['subject'];
			$detail = $this->db->get_one("SELECT email,userstatus FROM pw_members WHERE uid=" . pwEscape($this->tpcArr['authorid']));
			$send_address = $detail['email'];
			if (getstatus($detail['userstatus'], 8)) {
				require_once(R_P.'require/sendemail.php');
				sendemail($send_address, 'email_reply_subject', 'email_reply_content', 'email_additional');
			}
			$ret = 1;
		}
		if ($db_replysitemail && ($this->tpcArr['ifmail'] == 2 || $this->tpcArr['ifmail'] == 3)) {
			$rt = $this->db->get_one("SELECT mb.replyinfo,m.userstatus FROM pw_memberinfo mb LEFT JOIN pw_members m USING(uid) WHERE mb.uid=" . pwEscape($this->tpcArr['authorid']));
			if (empty($rt)) {
				$replyinfo = ",$this->tid,";
				$this->db->update("INSERT INTO pw_memberinfo SET " . pwSqlSingle(array('uid' => $this->tpcArr['authorid'], 'replyinfo' => $replyinfo)));
			} elseif (strpos($rt['replyinfo'], ",$this->tid,") === false) {
				$replyinfo = $rt['replyinfo'] ? $rt['replyinfo'].$this->tid.',' : ",$this->tid,";
				$this->db->update("UPDATE pw_memberinfo SET replyinfo=" . pwEscape($replyinfo) . " WHERE uid=" . pwEscape($this->tpcArr['authorid']));
			}
			if (!getstatus($rt['userstatus'],6)) {
				$this->db->update("UPDATE pw_members SET userstatus=userstatus|(1<<5) WHERE uid=" . pwEscape($this->tpcArr['authorid']));
			}
			$ret += 2;
		}
		return $ret;
	}

	function afterReply() {
		global $db_ifpwcache,$timestamp,$db_readperpage;
		if ($this->data['ifcheck'] == 1) {
			if ($this->forum->foruminfo['allowhtm'] && !$this->forum->foruminfo['cms'] && $this->tpcArr['replies'] < $db_readperpage) {
				$StaticPage = L::loadClass('StaticPage');
				$StaticPage->update($this->tid);
			}
			$lastpost = array(
				'subject'	=> $this->data['title'] ? substrs($this->data['title'], 26) : 'Re:'.substrs($this->tpcArr['subject'], 26),
				'author'	=> $this->data['lastposter'],
				'lastpost'	=> $timestamp,
				'tid'		=> $this->tid,
				't_date'	=> $this->tpcArr['postdate']
			);
			$this->forum->lastinfo('reply', '+', $lastpost);

			//Start Here pwcache
			if ($db_ifpwcache & 270) {
				require_once(R_P.'lib/elementupdate.class.php');
				$elementupdate = new ElementUpdate($this->forum->fid);
				$elementupdate->special = $this->tpcArr['special'];
				if ($db_ifpwcache & 14) {
					$elementupdate->replySortUpdate($this->tid, $this->forum->fid, $this->tpcArr['postdate'], $this->tpcArr['replies']+1);
				}
				if ($db_ifpwcache & 256) {
					$elementupdate->newReplyUpdate($this->tid, $this->forum->fid, $this->tpcArr['postdate']);
				}
				$elementupdate->updateSQL();
			}
			updateDatanalyse($this->data['authorid'],'memberThread',1);
			updateDatanalyse($this->tid,'threadPost',1);

            # memcache refresh
            $threadsObj = L::loadclass("threads");
            $threadsObj->clearThreadByThreadId($this->tid);

            # memcache refresh
            $threadlistObj = L::loadclass("threadlist");
            $threadlistObj->updateThreadIdsByForumId($this->forum->fid,$this->tid);
		}
		if ($this->postdata->filter->filter_weight > 1) {
			$this->postdata->filter->insert($this->tid, $this->pid, implode(',',$this->postdata->filter->filter_word), $this->postdata->filter->filter_weight);
		}
	}

	function getNewId() {
		return $this->pid;
	}
}
?>