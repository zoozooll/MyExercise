<?php
!defined('P_W') && exit('Forbidden');

class topicPost {

	var $db;
	var $post;
	var $forum;
	var $postdata;

	var $data;
	var $att;
	var $type;
	var $group = null;

	function topicPost(&$post) {
		global $db;
		$this->db =& $db;
		$this->post =& $post;
		$this->forum =& $post->forum;
		$this->type = 'Post';
	}

	function setPostData(&$postdata) {
		$this->postdata =& $postdata;
		$this->att =& $postdata->att;
		$this->data = $postdata->getData();
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
		
		/*
		$creditset = $this->forum->creditset;
		if ($this->group) {
			$creditset = $this->group->creditset($creditset);
		} else {
			$creditset = $credit->creditset($creditset, $db_creditset);
			$add = $creditset[$this->type];
		}
		*/
	}

	function check() {
		$this->post->checkUserCredit($this->creditSet());
	}

	function execute(&$postdata) {
		global $timestamp,$db_ptable,$onlineip;
		$this->setPostData($postdata);
		$pwSQL = pwSqlSingle(array(
			'fid'		=> $this->data['fid'],				'icon'		=> $this->data['icon'],
			'author'	=> $this->data['author'],			'authorid'	=> $this->data['authorid'],
			'subject'	=> $this->data['title'],			'ifcheck'	=> $this->data['ifcheck'],
			'type'		=> $this->data['w_type'],			'postdate'	=> $timestamp,
			'lastpost'	=> $timestamp,						'lastposter'=> $this->data['lastposter'],
			'hits'		=> 1,								'replies'	=> 0,
			'topped'	=> $this->data['topped'],			'digest'	=> $this->data['digest'],
			'special '	=> $this->data['special'],			'state'		=> 0,
			'ifupload'	=> $this->data['ifupload'],			'ifmail'	=> $this->data['ifmail'],
			'anonymous'	=> $this->data['anonymous'],		'ptable'	=> $db_ptable,
			'ifmagic'	=> $this->data['ifmagic'],			'ifhide'	=> $this->data['hideatt'],
			'tpcstatus'	=> $this->data['tpcstatus'],		'modelid'	=> $this->data['modelid'],
		));
		$this->db->update("INSERT INTO pw_threads SET $pwSQL");
		$this->tid = $this->db->insert_id();
		# memcache refresh
		$threadList = L::loadClass("threadlist");
		$threadList->updateThreadIdsByForumId($this->data['fid'],$this->tid);
		$pw_tmsgs = GetTtable($this->tid);

		if (is_object($postdata->tag)) {
			$postdata->tag->insert($this->tid);
			$this->data['tags'] .= "\t" . $postdata->tag->relate($this->data['title'], $this->data['content']);
		}
		if (is_object($this->att) && ($aids = $this->att->getAids())) {
			$this->att->pw_attachs->updateById($aids,array('tid' => $this->tid));
		}
		$ipTable = L::loadClass('IPTable');

		$pwSQL = pwSqlSingle(array(
			'tid'		=> $this->tid,
			'aid'		=> $this->data['aid'],
			'userip'	=> $onlineip,
			'ifsign'	=> $this->data['ifsign'],
			'buy'		=> '',
			'ipfrom'	=> $ipTable->getIpFrom($onlineip),
			'tags'		=> $this->data['tags'],
			'ifconvert'	=> $this->data['convert'],
			'ifwordsfb'	=> $this->data['ifwordsfb'],
			'content'	=> $this->data['content'],
			'magic'		=> $this->data['magic']
		));
		$this->db->update("INSERT INTO $pw_tmsgs SET $pwSQL");

		if ($this->data['digest']) {
			$this->db->update("UPDATE pw_memberdata SET digests=digests+1 WHERE uid=" . pwEscape($this->data['authorid']));
			$this->post->user['digests']++;
		}
		$this->post->updateUserInfo($this->type, $this->creditSet(), $this->data['content']);
		$this->afterpost();
	}

	function afterpost() {
		global $db_ifpwcache,$timestamp;
		if ($this->data['ifcheck'] == 1) {
			if ($this->forum->foruminfo['allowhtm'] && !$this->forum->foruminfo['cms']) {
				$StaticPage = L::loadClass('StaticPage');
				$StaticPage->update($this->tid);
			}
			$lastpost = array(
				'subject'	=> substrs($this->data['title'],26),
				'author'	=> $this->data['lastposter'],
				'lastpost'	=> $timestamp,
				'tid'		=> $this->tid,
				't_date'	=> $timestamp
			);
			$this->forum->lastinfo('topic', '+', $lastpost);

			if ($this->forum->isOpen() && !$this->data['anonymous']) {
				pwAddFeed($this->post->uid, 'post', $this->tid, array('subject' => stripslashes($this->data['title']), 'tid' => $this->tid, 'fid' => $this->forum->fid));

				//会员资讯缓存
				$usercachedata = array();
				$usercache = L::loadDB('Usercache');
				$usercachedata['subject'] = substrs(stripWindCode($this->data['title']),100,N);
				$usercachedata['content'] = substrs(stripWindCode($this->data['content']),100,N);
				$usercachedata['postdate'] = $timestamp;
				if ($this->att) {
					$usercachedata['attimages']= $this->att->getImages(4);
				}
				$usercache->update($this->data['authorid'],'topic',$this->tid,$usercachedata);
			}
			//Start elementupdate
			if ($db_ifpwcache & 128 || (($db_ifpwcache & 512) && $this->att && $this->att->elementpic)) {
				require_once(R_P.'lib/elementupdate.class.php');
				$elementupdate = new ElementUpdate($this->forum->fid);
				if ($db_ifpwcache & 128) {
					$elementupdate->newSubjectUpdate($this->tid, $this->forum->fid, $timestamp, $this->data['special']);
				}
				if (($db_ifpwcache & 512) && $this->att && $this->att->elementpic) {
					$elementupdate->newPicUpdate($this->att->elementpic['aid'], $this->forum->fid, $this->tid, $this->att->elementpic['attachurl'], $this->att->elementpic['ifthumb'], $this->data['content']);
				}
				$elementupdate->updateSQL();
			}

			updateDatanalyse($this->data['authorid'],'memberThread',1);
			//End elementupdate
		}
		if ($this->postdata->filter->filter_weight > 1) {
			$this->postdata->filter->insert($this->tid, 0, implode(',',$this->postdata->filter->filter_word),$this->postdata->filter->filter_weight);
		}
		if ($this->data['topped'] > 0) {
			require_once(R_P.'require/updateforum.php');
			setForumsTopped($this->tid,$this->data['fid'],$this->data['topped']);
			updatetop();
		}
	}

	function getNewId() {
		return $this->tid;
	}
}
?>