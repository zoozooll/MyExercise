<?php

!defined('P_W') && exit('Forbidden');
//api mode 1

define('API_USER_USERNAME_NOT_UNIQUE', 100);

class User {

	var $base;
	var $db;

	function User($base) {
		$this->base = $base;
		$this->db = $base->db;
	}

	function getInfo($uids, $fields = array()) {
		if (!$uids) {
			return new ApiResponse(false);
		}
		if (is_numeric($uids)) {
			$sql = ' uid=' . pwEscape($uids);
		} else {
			$sql = ' uid IN(' . pwImplode(explode(',',$uids)) . ')';
		}
		require_once(R_P.'require/showimg.php');

		$users = array();
		$query = $this->db->query("SELECT uid,username,icon,gender,location,bday FROM pw_members WHERE " . $sql);
		while ($rt = $this->db->fetch_array($query)) {
			list($rt['icon']) = showfacedesign($rt['icon'], 1, 'm');
			if ($fields) {
				$rt_a = array();
				foreach ($fields as $field) {
					if (isset($rt[$field])) {
						$rt_a[$field] = $rt[$field];
					}
				}
			} else {
				$rt_a = $rt;
			}
			$users[$rt['uid']] = $rt_a;
		}
		return new ApiResponse($users);
	}

	function alterName($uid, $newname) {
		$u = $this->db->get_one("SELECT username FROM pw_members WHERE uid=" . pwEscape($uid));
		if (!$u || $u['username'] == $newname) {
			return new ApiResponse(1);
		}
		if ($unique = $this->db->get_one("SELECT uid FROM pw_members WHERE username=" . pwEscape($newname))) {
			return new ApiResponse(API_USER_USERNAME_NOT_UNIQUE);
		}
		$this->db->update("UPDATE pw_members SET username=" . pwEscape($newname) . " WHERE uid=" . pwEscape($uid));

		$user = L::loadClass('ucuser');
		$user->alterName($uid, $u['username'], $newname);

		return new ApiResponse(1);
	}

	function deluser($uids) {
		$user = L::loadClass('ucuser');
		$user->delUserByIds($uids);

		return new ApiResponse(1);
	}

	function synlogin($user) {
		global $timestamp,$uc_key;
		list($winduid, $windid, $windpwd) = explode("\t", $this->base->strcode($user, false));
		
		header('P3P: CP="CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR"');
		$safecv = '';
		$cktime = 31536000;
		$cktime != 0 && $cktime += $timestamp;
		Cookie("winduser",StrCode($winduid."\t".PwdCode($windpwd)."\t".$safecv),$cktime);
		Cookie("ucuser",StrCode($winduid."\t".md5($uc_key.$windpwd)),$cktime);
		Cookie('lastvisit','',0);

		return '';
	}

	function synlogout() {
		header('P3P: CP="CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR"');
		Cookie('winduser',' ',0);
		Cookie('hideid','',0);
		Cookie('lastvisit','',0);
		Cookie('ck_info','',0);
		Cookie('msghide','',0,false);
		Cookie("ucuser",'',0);
		return '';
	}
    function getusergroup() {
        $usergroup = array();
        $query = $this->db->query("SELECT gid,gptype,grouptitle FROM pw_usergroups ");
        while($rt= $this->db->fetch_array($query)) {
            $usergroup[$rt['gid']] = $rt;
        }
        return new ApiResponse($usergroup);
    }
}
?>