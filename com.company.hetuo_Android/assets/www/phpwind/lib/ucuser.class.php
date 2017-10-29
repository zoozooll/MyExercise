<?php
!defined('P_W') && exit('Forbidden');

/*
* fix by sky_hold@163.com
*
*/

class PW_Ucuser {
	
	var $db;

	function PW_Ucuser() {
		global $db;
		$this->db =& $db;
	}

	function edit($uid, $oldname, $info) {
		require_once(R_P . 'uc_client/uc_client.php');
		$errmsg  = null;
		$errcode = array(
			'-1' => 'illegal_username',			'-2' => 'username_same',
			'-3' => 'illegal_email',			'-4' => 'reg_email_have_same'
		);
		$ucstatus = uc_user_edit($uid, $oldname, $info['username'], $info['password'], $info['email']);
		if ($ucstatus < 0) {
			$errmsg = $errcode[$ucstatus];
		}
		if ($ucstatus == 2) {
			$this->alterName($uid, $oldname, $info['username']);
		}
		return array($ucstatus, $errmsg);
	}

	function delete($u_arr) {
		if (empty($u_arr))
			return false;

		require_once(R_P . 'uc_client/uc_client.php');
		uc_user_delete($u_arr);

		require_once(R_P.'require/writelog.php');
		global $admin_name,$timestamp,$onlineip;
		$udb = array();
		$query = $this->db->query("SELECT m.uid,m.username,m.groupid FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE m.uid IN(" . pwImplode($u_arr) . ")");
		while ($rt = $this->db->fetch_array($query)) {
			$log = array(
				'type'      => 'deluser',
				'username1' => $rt['username'],
				'username2' => $admin_name,
				'field1'    => 0,
				'field2'    => $rt['groupid'],
				'field3'    => '',
				'descrip'   => 'deluser_descrip',
				'timestamp' => $timestamp,
				'ip'        => $onlineip,
			);
			writelog($log);

			$udb[] = $rt['uid'];
		}
		$this->delUserByIds($udb);
	}

	function delUserByIds($uids) {
		if (!$delids = pwImplode($uids)) {
			return;
		}
		$this->db->update("DELETE FROM pw_members WHERE uid IN ($delids)");
		$this->db->update("DELETE FROM pw_memberdata WHERE uid IN ($delids)");
		$this->db->update("DELETE FROM pw_memberinfo WHERE uid IN ($delids)");
		$this->db->update("DELETE FROM pw_banuser WHERE uid IN ($delids)");

		@extract($this->db->get_one("SELECT count(*) AS count FROM pw_members"));
		@extract($this->db->get_one("SELECT username FROM pw_members ORDER BY uid DESC LIMIT 1"));
		$this->db->update("UPDATE pw_bbsinfo SET newmember=" . pwEscape($username) . ',totalmember=' . pwEscape($count) . " WHERE id='1'");
	}

	function alterName($uid, $oldname, $username) {
		global $db_plist;
		$this->db->update("UPDATE pw_threads SET author=" . pwEscape($username) . " WHERE authorid=" . pwEscape($uid));
		$ptable_a = array('pw_posts');
		if ($db_plist && count($db_plist)>1) {
			foreach ($db_plist as $key => $val) {
				if($key == 0) continue;
				$ptable_a[] = 'pw_posts'.$key;
			}
		}
		foreach ($ptable_a as $val) {
			$this->db->update("UPDATE $val SET author=" . pwEscape($username) . " WHERE authorid=" . pwEscape($uid));
		}
		$this->db->update("UPDATE pw_cmembers SET username=" . pwEscape($username) . " WHERE uid=" . pwEscape($uid));
		$this->db->update("UPDATE pw_colonys SET admin=" . pwEscape($username) . " WHERE admin=" . pwEscape($oldname));
		$this->db->update("UPDATE pw_announce SET author=" . pwEscape($username) . " WHERE author=".pwEscape($oldname));
		$this->db->update("UPDATE pw_medalslogs SET awardee=" . pwEscape($username) . " WHERE awardee=".pwEscape($oldname));
		$this->db->update("UPDATE pw_msg SET username=".pwEscape($username)."WHERE fromuid=".pwEscape($uid));

		$query = $this->db->query("SELECT fid,forumadmin,fupadmin FROM pw_forums WHERE forumadmin LIKE " . pwEscape("%,$oldname,%",false) . " OR fupadmin LIKE " . pwEscape("%,$oldname,%",false));
		while ($rt = $this->db->fetch_array($query)) {
			$rt['forumadmin']	= str_replace(",$oldname,",",$username,",$rt['forumadmin']);
			$rt['fupadmin']		= str_replace(",$oldname,",",$username,",$rt['fupadmin']);
			$this->db->update("UPDATE pw_forums SET forumadmin=" . pwEscape($rt['forumadmin'],false) . ",fupadmin=" . pwEscape($rt['fupadmin'],false) . " WHERE fid=" . pwEscape($rt['fid'],false));
		}
	}
}
?>