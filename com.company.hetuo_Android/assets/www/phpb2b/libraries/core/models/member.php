<?php
class Members extends PbModel {
 	var $name = "Member";
 	var $info = null;
 	var $id = -1;
 	var $user_name;
 	var $mask_user_name = "admin";

 	function Members()
 	{
		parent::__construct();
 	}

 	function &getInstance() {
 		static $instance = array();
 		if (!$instance) {
 			$instance[0] =& new Members();
 		}
 		return $instance[0];
 	}
	
 	function setUserName($user_name)
 	{
 		$this->user_name = $user_name;
 	}
 	
 	function getUserName()
 	{
 		return $this->user_name;
 	}
 	
 	function setInfoByUserName($user_name)
 	{
 		$result = array();
 		$sql = "SELECT m.*,mf.* FROM {$this->table_prefix}members m LEFT JOIN {$this->table_prefix}memberfields mf ON m.id=mf.member_id WHERE m.username='{$user_name}'";
 		$result = $this->dbstuff->GetRow($sql);
 		$this->info = $result;
 	}
 	
 	function setInfoBySpaceName($space_name)
 	{
 		$result = array();
 		$sql = "SELECT m.*,mf.* FROM {$this->table_prefix}members m LEFT JOIN {$this->table_prefix}memberfields mf ON m.id=mf.member_id WHERE m.space_name='{$space_name}'";
 		$result = $this->dbstuff->GetRow($sql);
 		if (empty($result) || !$result) {
 			$sql = "SELECT m.*,mf.* FROM {$this->table_prefix}members m LEFT JOIN {$this->table_prefix}memberfields mf ON m.id=mf.member_id WHERE m.username='{$space_name}'";
 			$result = $this->dbstuff->GetRow($sql);
 		}
 		$this->info = $result;
 	}
 	
 	function setId($id)
 	{
 		$this->id = $id;
 		$info = null;
 		$info = $this->getInfoById($id);
 		$this->setInfo($info); 		
 	}
 	
 	function getId()
 	{
 		return $this->id;
 	}
 	
 	function setInfo($info)
 	{
 		$this->info = $info;
 	}
 	
 	function getInfo()
 	{
 		return $this->info;
 	} 	

 	function getInfoById($member_id)
 	{
 		$tmp_img = null;
 		require(CACHE_PATH. "cache_membergroup.php");
 		require(CACHE_PATH. "cache_trusttype.php");
 		$result = array();
 		$sql = "SELECT m.*,mf.* FROM {$this->table_prefix}members m LEFT JOIN {$this->table_prefix}memberfields mf ON mf.member_id=m.id WHERE m.id='{$member_id}'";
 		$result = $this->dbstuff->GetRow($sql);
 		if (!empty($result)) {
 			if(isset($result['link_man']))
 			$result['link_people'] = $result['link_man'];
 			$result['groupname'] = $_PB_CACHE['membergroup'][$result['membergroup_id']]['name'];
 			$result['groupimage'] = "images/group/".$_PB_CACHE['membergroup'][$result['membergroup_id']]['avatar'];
			if (!empty($result['trusttype_ids'])) {
				$tmp_str = explode(",", $result['trusttype_ids']);
				foreach ($tmp_str as $key=>$val){
					$tmp_img.="<img src=\"images/icon/".$_PB_CACHE['trusttype'][$val]['avatar']."\" alt=\"".$_PB_CACHE['trusttype'][$val]['name']."\" />";
				}
				$result['trust_image'] = $tmp_img;
			} 			
 		}
 		return $result;
 	}
 	
 	function authPasswd($passwd, $type = 'md5', $salt = null)
 	{
 		switch ($type) {
 			case "md5":
 				return md5($passwd);
 				break;
 			case "crypt":
 				if (!empty($salt)) {
 					return crypt($passwd, $salt);
 				}else{
 					$salt = substr($passwd, 0, 2);
 					return crypt($passwd, $salt);
 				}
 				break;
 			case "crc32":
 				$crc = crc32($passwd);
 				return sprintf("%u", $crc);
 			default:
 				return $passwd;
 				break;
 		}
 	}
 	 	
	function checkUserLogin($uname,$upass)
	{
		$userid = trim($uname);
		if (pb_check_email($userid)) {
			$sql = "SELECT m.id,m.username,m.userpass,status,email,credits,service_end_date,office_redirect,af.member_id AS aid FROM {$this->table_prefix}members m LEFT JOIN {$this->table_prefix}adminfields af ON m.id=af.member_id WHERE m.email='$userid'";
		}else{
			$sql = "SELECT m.id,m.username,m.userpass,status,email,credits,service_end_date,office_redirect,af.member_id AS aid FROM {$this->table_prefix}members m LEFT JOIN {$this->table_prefix}adminfields af ON m.id=af.member_id WHERE m.username='$userid'";
		}
		$tmpUser = $this->dbstuff->GetRow($sql);
		if (empty($tmpUser)) {
			return -2;
		}else{
			$true_pass = $tmpUser['userpass'];
		}
		if (empty($userid) || empty($upass)){
			return -1;
		}elseif (strcmp($true_pass,$this->authPasswd($upass))!=0){
			return -3;
		}elseif ($tmpUser['status'] !=1) {
			return -4;
		}else {
			if (!empty($tmpUser['aid'])) {
				$tmpUser['is_admin'] = 1;
			}else{
				$tmpUser['is_admin'] = 0;
			}
		    $this->info = $tmpUser;
		    $tmpUser['userpass'] = $upass;
		    $this->putLoginStatus($tmpUser);
			$loginip = pb_get_client_ip();
			$this->dbstuff->Execute("UPDATE {$this->table_prefix}members SET last_login=".$this->timestamp.",last_ip='".$loginip."' WHERE id='{$tmpUser['id']}'");
		    unset($tmpUser);
			return true;
		}
	}

	function checkUserExist($uname, $set = true)
	{
		if(strlen($uname)<1 || strlen($uname)>255) {
			return false;
		}
		$sql = "SELECT m.id,m.username,m.userpass FROM {$this->table_prefix}members m WHERE m.username='{$uname}'";
		$uinfo = $this->dbstuff->GetRow($sql);
		if (!empty($uinfo) && $uinfo!='') {
			if($set) $this->info = $uinfo;
			return true;
		}else {
			return false;
		}
	}
	
	function checkUserExistsByEmail($email)
	{
		if (!pb_check_email($email)) {
			flash("email_exists");
		}
		$result = $this->field("id", "email='".$email."'");
		if (!$result || empty($result)) {
			return false;
		}else{
			return true;
		}
	}

	function updateUserStatus($id_array, $status = 1)
	{
		if (is_array($id_array))
		{
			$tmp_ids = implode(",",$id_array);
			$sql = "UPDATE ".$this->getTable()." SET status='$status' where id in (".$tmp_ids.")";
		}
		else
		{
			$sql = "UPDATE ".$this->getTable()." SET status='$status'  WHERE id=".intval($id_array);
		}
		$result = $this->dbstuff->Execute($sql);
		if($result)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	function checkUserPasswdById($input_passwd, $member_id)
	{
		$return = false;
		$user_pass = $this->field("userpass", "id=".$member_id);
		if (pb_strcomp($this->authPasswd($input_passwd), $user_pass)) {
			return true;
		}else{
			return false;
		}
	}
	
	function updateUserPasswdById($member_id, $new_passwd)
	{
		$sql = "UPDATE {$this->table_prefix}members SET userpass='".$this->authPasswd($new_passwd)."' WHERE id={$member_id} AND status='1'";
		$result = $this->dbstuff->Execute($sql);
		return $result;
	}
	
	function passport($data, $action = 'login')
	{
		global $_PB_CACHE, $passport;
		if (!$_PB_CACHE['setting']['passport_support'] || empty($data)) {
			return false;
		}
		list($uid, $uname, $upass, $uemail) = $data;
		$passport->ucenter($uname, $upass, $uemail, $action);
	}
	
	function putLoginStatus($user_info)
	{
		global $phpb2b_auth_key;
		$_SESSION["MemberID"] = $user_info['id'];
		$_SESSION["MemberName"] = $user_info['username'];
		$auth = authcode($user_info['id']."\t".$user_info['username']."\t".$this->authPasswd($user_info['userpass'])."\t".$user_info['is_admin'], 'ENCODE', $phpb2b_auth_key);
		usetcookie('auth', $auth, $this->timestamp+3600);
		$this->passport(array($user_info['id'], $user_info['username'], $user_info['userpass']), "login");
	}
	
	function Add()
	{
		global $_PB_CACHE, $memberfield, $phpb2b_auth_key, $if_need_check;
		$error_msg = array();
		if (empty($this->params['data']['member']['username']) or 
		empty($this->params['data']['member']['userpass']) or 
		empty($this->params['data']['member']['email'])) return false;
		$space_name = $this->params['data']['member']['username'];
		$userpass = $this->params['data']['member']['userpass'];
		$this->params['data']['member']['userpass'] = $this->authPasswd($this->params['data']['member']['userpass']);
		$this->params['data']['member']['space_name'] = L10n::translateSpaceName($space_name);//Todo:
		$uip = pb_ip2long(pb_getenv('REMOTE_ADDR'));
		if(empty($uip)){
			pheader("location:".URL."redirect.php?message=".urlencode(L('sys_error')));
		}
		$this->params['data']['member']['last_login'] = $this->params['data']['member']['created'] = $this->params['data']['member']['modified'] = $this->timestamp;
		$this->params['data']['member']['last_ip'] = pb_get_client_ip('str');
		$email_exists = $this->checkUserExistsByEmail($this->params['data']['member']['email']);
		if ($email_exists) {
			flash("email_exists", null, 0);
		}
		$if_exists = $this->checkUserExist($this->params['data']['member']['username']);
		if ($if_exists) {
			flash('member_has_exists', null, 0);
		}else{
			$this->save($this->params['data']['member']);
			$key = $this->table_name."_id";
			$this->passport(array($this->$key, $this->params['data']['member']['username'], $userpass, $this->params['data']['member']['email']), "reg");
			$memberfield->primaryKey = "member_id";
			$memberfield->params['data']['memberfield']['member_id'] = $this->$key;
			$memberfield->params['data']['memberfield']['reg_ip'] = $this->params['data']['member']['last_ip'];
			$memberfield->save($memberfield->params['data']['memberfield']);
			if (!$if_need_check) {
				$user_info['id'] = $this->$key;
				$user_info['username'] = $this->params['data']['member']['username'];
				$user_info['userpass'] = $userpass;
				$user_info['lifetime'] = $this->timestamp+86400;
				$user_info['is_admin'] = 0;
				$this->putLoginStatus($user_info);
			}
		}
		return true;
	}
	
	function Delete($ids, $condition = null)
	{
		global $administrator_id;
		$id_condition = null;
		if (is_array($ids)) {
			if (in_array($administrator_id, $ids)) {
				flash("cant_del_founder");
			}
			$id_condition = "{$this->table_prefix}members.id IN (".implode(",", $ids).")";
		}else{
			if ($ids == $administrator_id) {
				flash("cant_del_founder");
			}
			$id_condition = "{$this->table_prefix}members.id = ".intval($ids);
		}
		$id_condition = "WHERE ".$id_condition;
		$sql = "DELETE FROM {$this->table_prefix}members,
		{$this->table_prefix}companies,
		{$this->table_prefix}trades,
		{$this->table_prefix}products,
		{$this->table_prefix}producttypes,
		{$this->table_prefix}personals,
		{$this->table_prefix}memberfields,
		{$this->table_prefix}tradefields 
		USING {$this->table_prefix}members 
		LEFT JOIN {$this->table_prefix}companies ON {$this->table_prefix}companies.member_id={$this->table_prefix}members.id 
		LEFT JOIN {$this->table_prefix}trades ON {$this->table_prefix}trades.member_id={$this->table_prefix}members.id 
		LEFT JOIN {$this->table_prefix}products ON {$this->table_prefix}products.member_id={$this->table_prefix}members.id 
		LEFT JOIN {$this->table_prefix}tradefields ON {$this->table_prefix}members.id={$this->table_prefix}tradefields.member_id 
		LEFT JOIN {$this->table_prefix}producttypes ON {$this->table_prefix}members.id={$this->table_prefix}producttypes.member_id
		LEFT JOIN {$this->table_prefix}memberfields ON {$this->table_prefix}members.id={$this->table_prefix}memberfields.member_id 
		LEFT JOIN {$this->table_prefix}personals ON {$this->table_prefix}members.id={$this->table_prefix}personals.member_id {$id_condition}";
		return $this->dbstuff->Execute($sql);
	}
	
	function updateSpaceName($member_info, $new_space_name)
	{
		if (empty($member_info) || !$member_info || !is_array($member_info)) {
			return false;
		}
		if (!empty($member_info['id'])) {
			$this->id = $member_info['id'];
			$data = $this->dbstuff->GetRow("SELECT id,space_name FROM {$this->table_prefix}members WHERE id='".$member_info['id']."'");
		}elseif (!empty($member_info['username'])){
			$data = $this->dbstuff->GetRow("SELECT id,space_name FROM {$this->table_prefix}members WHERE username='".$member_info['username']."'");
			$this->id = $data['id'];
		}
		if (pb_strcomp($new_space_name, $data['space_name']) || empty($data)) {
			return;
		}else{
			$if_exists = $this->dbstuff->GetOne("SELECT id FROM {$this->table_prefix}members WHERE space_name='".$new_space_name."'");
			if ($if_exists) {
				flash("space_name_exists");
			}
			$return = $this->dbstuff->Execute("UPDATE {$this->table_prefix}members m SET m.space_name='".$new_space_name."' WHERE m.id=".$this->id);
			$return = $this->dbstuff->Execute("UPDATE {$this->table_prefix}companies c SET c.cache_spacename='".$new_space_name."' WHERE c.member_id=".$this->id);
			$return = $this->dbstuff->Execute("UPDATE {$this->table_prefix}jobs j SET j.cache_spacename='".$new_space_name."' WHERE j.member_id=".$this->id);
			return true;
		}
	}
	function logOut(){
		$this->passport(array($user_info['id'], $user_info['username'], $user_info['userpass']), "logout");
	}
	function clearCache($id = null)
	{
		if (!is_null($id) && $id>0) {
			$this->dbstuff->Execute("DELETE FROM `{$this->table_prefix}membercaches` WHERE member_id='".$id."'");
		}else{
			$this->dbstuff->Execute("TRUNCATE `{$this->table_prefix}membercaches`");
		}
	}
}
?>