<?php
/**
 * 搜索器服务
 * @author lh 2009-12-15
 * 主搜索器接口
 * searchThreads()
 * searchForums()
 * searchUsers()
 * searchDiarys()
 * searchGroups()
 */
!function_exists('readover') && exit('Forbidden');
class PW_Searcher {
	
	var $_sphinx 		= null;
	var $_perPage 		= 20;
	var $_searchLimit 	= 1000;
	var $_db 			=  null;
	var $_time 			= null;
	var $_debug         = true;
	var $_defaultmethod = "AND";
	var $_defaultOrder  = "DESC";/*ASC DESC*/
	var $_withlen       = false;/*是否开启长度限制功能*/
	
	function PW_Searcher(){
		global $db_sphinx,$db;
		$this->_sphinx = &$db_sphinx;
		$this->_db = &$db;
		$this->_time = time();
	}
	
	/*
	 * 搜索帖子接口，定制一
	 */
	function searchThreads($keywords,$range,$userNames="",$starttime="",$endtime="",$page=1,$perpage=20){
		$keywords = $this->checkKeyWord($keywords);
		if(!$keywords){
			return array();
		}
		$index = $this->getIndex($this->_getThreadRange($range));
		/*组装用户IDs*/
		$authorIds = array();
		if($userNames && !$authorIds = $this->getUserByUserName($userNames)){
			return array();
		}
		/*时间组合*/
		list($starttime,$endtime) = $this->checkTime($starttime,$endtime);
		return $this->searchThreadsAPI($keywords,$this->_defaultmethod,$index,$authorIds,$starttime,$endtime,"",'postdate',$this->_defaultOrder,$page,$perpage);
	}
	
	/*
	 * 帖子索引名称图，默认为内容搜索
	 * $k 可取值 0=>tindex 1=>tcindex 2=>taindex
	 */
	function _getThreadRange($k){
		$ranges = array(1=>"tindex",2=>"tcindex",3=>"taindex");
		return $ranges[$k] ? $ranges[$k] : $ranges[2];
	}
	
	/*
	 * 搜索帖子API公共接口
	 * 注意，调用前请过滤参数
	 */
	function searchThreadsAPI($keywords,$method,$index,$authorids=array(),$starttime="",$endtime="",$groupby="",$sortby="postdate",$asc="DESC",$page=1,$perpage=20){
		$sphinx = $this->_getSphinx();
		list($host,$port) = $this->_getConfig();
		$sphinx->SetServer ( $host, $port );
		$sphinx->SetConnectTimeout ( 1 );
		$sphinx->SetMatchMode ( $this->_getMode($method,$keywords) );
		
		//$digest && $sphinx->SetFilter ('digest',array(1,2));
		//$fid && $sphinx->SetFilter ('fid',$fid,$exclude);
		
		$authorids && $sphinx->SetFilter ('authorid',$authorids);/*用户*/
		if($starttime && $endtime){
			$sphinx->SetFilterRange('postdate',$starttime,$endtime);/*时间*/
		}
		$groupby && $sphinx->SetGroupBy ( $groupby, $this->getGroup(), "@group desc" );/*分组*/
		$sortby && $sphinx->SetSortMode ( ($asc=='DESC' ? SPH_SORT_ATTR_DESC : SPH_SORT_ATTR_ASC), $sortby );/*排序*/
		$page = $page>1 ? $page : 1;
		$start_limit = intval(($page - 1) * $perpage);
		$sphinx->SetLimits ( $start_limit, intval($perpage), ( $perpage>$this->_searchLimit ) ? $perpage : $this->_searchLimit );
		
		$sphinx->SetRankingMode ( $this->getRanking() );
		$sphinx->SetArrayResult ( true );
		
		$this->debug($keywords);
		
		$result = $sphinx->Query ( str_replace('|',' ',$keywords), $index );
		if ( $result === false ){
			return false;
		} 
		return $this->_buildThreadResult($result,$index);
	}
	/*
	 * 组装搜索帖子结果页数据
	 */
	function _buildThreadResult($result){
		require_once(R_P.'m/chinese.php');
		$chs = new Chinese('utf8','gbk');
		foreach ( $result["words"] as $word => $info ){
			$words[]=$chs->Convert($word);
		}
		$totals=$result['total'];
		if ( is_array($result["matches"]) ){
			$tids='';
			foreach ( $result["matches"] as $docinfo ){
				$tids && $tids.=',';
				$tids .= $docinfo['id'];
			}
			$this->debug($words);
			return array($totals,$tids,$words);
		}else{
			return false;
		}
	}
	
	function debug($debug){
		$this->_debug && var_dump($debug);	
	}
	
	/*查找帖子*/
	function getThreads($threadIds,$keywords){
		if(!$threadIds){
			return array();
		}
		$query = $this->_db->query("SELECT * FROM pw_threads th left join pw_tmsgs t on th.tid=t.tid WHERE th.tid in(".$threadIds.") ORDER BY postdate DESC");
		$result = array();
		while($rs = $this->_db->fetch_array($query)){
			$result[] = $rs;
		}
		if(!$result){
			return array();
		}
		$data = array();
		/*组装帖子数据*/
		foreach($result as $t){
			$t['postdate'] = date("Y-m-d H:i",$t['postdate']);
			$forum = L::forum($t['fid']);
			/*加亮标题与内容*/
			foreach($keywords as $keyword){
				$keyword && $t['subject'] = $this->highlighting($keyword,$t['subject']);
				$keyword && $t['content'] = $this->highlighting($keyword,$t['content']);
			}
			$t['name'] = $forum['name'];/*fup*/
			$data[] = $t;
		}
		return $data;
	}
	
	/*
	 * 搜索用户接口，定制一
	 */
	function searchUsers($keywords,$page=1,$perpage=20){
		$keywords = $this->checkKeyWord($keywords);
		if(!$keywords){
			return array();
		}
		$index = $this->getIndex('mindex');/*一个索引*/
		return $this->searchUsersAPI($keywords,$this->_defaultmethod,$index,$page,$perpage);
	}
	
	/*搜索用户*/
	function searchUsersAPI($keywords,$method,$index,$page,$perpage){
		$sphinx = $this->_getSphinx();
		list($host,$port) = $this->_getConfig();
		$sphinx->SetServer ( $host, $port );
		$sphinx->SetConnectTimeout ( 1 );
		$sphinx->SetMatchMode ( $this->_getMode($method,$keywords) );
		
		$page = $page>1 ? $page : 1;
		$start_limit = intval(($page - 1) * $perpage);
		$sphinx->SetLimits ( $start_limit, intval($perpage), ( $perpage>$this->_searchLimit ) ? $perpage : $this->_searchLimit );
		
		$sphinx->SetRankingMode ( $this->getRanking() );
		$sphinx->SetArrayResult ( true );
		
		$result = $sphinx->Query ( str_replace('|',' ',$keywords), $index );

		if ( $result === false ){
			return false;
		} 
		return $this->_buildUserResult($result,$index);
	}
	
	/*
	 * 组装搜索用户结果页数据
	 */
	function _buildUserResult($result){
		require_once(R_P.'m/chinese.php');
		$chs = new Chinese('utf8','gbk');
		foreach ( $result["words"] as $word => $info ){
			$words[]=$chs->Convert($word);
		}
		$totals=$result['total'];
		if ( is_array($result["matches"]) ){
			foreach ( $result["matches"] as $docinfo ){
				$uids && $uids.=',';
				$uids .= $docinfo['id'];
			}
			return array($totals,$uids,$words);
		}else{
			return false;
		}
	}
	
	function getUsers($userIds){
		if(!$userIds){
			return array();
		}
		$query = $this->_db->query("SELECT * FROM pw_members m left join pw_memberdata md on m.uid=md.uid WHERE m.uid in(".$userIds.")");
		$result = array();
		while($rs = $this->_db->fetch_array($query)){
			$result[] = $rs;
		}
		if(!$result){
			return array();
		}
		$data = array();
		require_once(R_P.'require/showimg.php');
		$genders = array(0=>"保密",1=>"男",2=>"女");
		foreach($result as $t){
			list($t['face']) = showfacedesign($t['icon'],1);
			$t['gender'] = $genders[$t['gender']];	
			$data[] = $t;
		}
		return $data;
	}
	
	/*
	 * 搜索日志接口，定制一
	 */
	function searchDiarys($keywords,$range,$userNames="",$starttime="",$endtime="",$page=1,$perpage=20){
		$keywords = $this->checkKeyWord($keywords);
		if(!$keywords){
			return array();
		}
		$index = $this->getIndex($this->_getDiaryRange($range));
		/*组装用户IDs*/
		$authorIds = array();
		if($userNames && !$authorIds = $this->getUserByUserName($userNames)){
			return array();
		}
		/*时间组合*/
		list($starttime,$endtime) = $this->checkTime($starttime,$endtime);
		return $this->searchDiarysAPI($keywords,$this->_defaultmethod,$index,$authorIds,$starttime,$endtime,"",'postdate',$this->_defaultOrder,$page,$perpage);
	}
	
	/*
	 * 帖子索引名称图，默认为内容搜索
	 * $k 可取值 0=>tindex 1=>tcindex 2=>taindex
	 */
	function _getDiaryRange($k){
		$ranges = array(1=>"dindex",2=>"dcindex",3=>"daindex");
		return $ranges[$k] ? $ranges[$k] : $ranges[2];
	}
	
	/*搜索日志*/
	function searchDiarysAPI($keywords,$method,$index,$userIds=array(),$starttime="",$endtime="",$groupby="",$sortby="postdate",$asc,$page=1,$perpage=20){
		$sphinx = $this->_getSphinx();
		list($host,$port) = $this->_getConfig();
		$sphinx->SetServer ( $host, $port );
		$sphinx->SetConnectTimeout ( 1 );
		$sphinx->SetMatchMode ( $this->_getMode($method,$keywords) );

		$aids && $sphinx->SetFilter ('aid',$aids);/*按日志分类搜索*/
		$userIds && $sphinx->SetFilter ('uid',$userIds);/*用户*/
		if($starttime && $endtime){
			$sphinx->SetFilterRange('postdate',$starttime,$endtime);/*时间*/
		}
		$groupby && $sphinx->SetGroupBy ( $groupby, $this->getGroup(), "@group desc" );/*分组*/
		$sortby && $sphinx->SetSortMode ( ($asc=='DESC' ? SPH_SORT_ATTR_DESC : SPH_SORT_ATTR_ASC), $sortby );/*排序*/
		$page = $page>1 ? $page : 1;
		$start_limit = intval(($page - 1) * $perpage);
		$sphinx->SetLimits ( $start_limit, intval($perpage), ( $perpage>$this->_searchLimit ) ? $perpage : $this->_searchLimit );
		
		$sphinx->SetRankingMode ( $this->getRanking() );
		$sphinx->SetArrayResult ( true );
		
		$result = $sphinx->Query ( str_replace('|',' ',$keywords), $index );
		if ( $result === false ){
			return false;
		} 
		return $this->_buildDiaryResult($result,$index);
	}
	
	/*
	 * 组装搜索日志结果页数据
	 */
	function _buildDiaryResult($result){
		require_once(R_P.'m/chinese.php');
		$chs = new Chinese('utf8','gbk');
		foreach ( $result["words"] as $word => $info ){
			$words[]=$chs->Convert($word);
		}
		$totals=$result['total'];
		if ( is_array($result["matches"]) ){
			foreach ( $result["matches"] as $docinfo ){
				$dids && $dids.=',';
				$dids .= $docinfo['id'];
			}
			return array($totals,$dids,$words);
		}else{
			return false;
		}
	}
	
	function getDiarys($dids,$keywords){
		if(!$dids){
			return array();
		}
		$query = $this->_db->query("SELECT * FROM pw_diary WHERE did in(".$dids.") ORDER BY postdate DESC");
		$result = array();
		while($rs = $this->_db->fetch_array($query)){
			$result[] = $rs;
		}
		if(!$result){
			return array();
		}
		$data = array();
		foreach($result as $t){
			$t['postdate'] = date("Y-m-d H:i",$t['postdate']);
			/*加亮标题与内容*/
			foreach($keywords as $keyword){
				$keyword && $t['subject'] = $this->highlighting($keyword,$t['subject']);
				$keyword && $t['content'] = $this->highlighting($keyword,$t['content']);
			}
			$data[] = $t;
		}
		return $data;
	}
	
	/*
	 * 搜索版块接口，定制一
	 */
	function searchForums($keywords,$page=1,$perpage=20){
		$keywords = $this->checkKeyWord($keywords);
		if(!$keywords){
			return array();
		}
		return $this->searchForumsAPI($keywords,$page,$perpage);
	}
	
	/*版块搜索*/
	function searchForumsAPI($keywords,$page=1,$perpage=20){
		$page = $page>1 ? $page : 1;
		$start = intval(($page - 1) * $perpage);
		$total = $this->_db->get_value("SELECT * FROM pw_forums WHERE name like ".pwEscape("%$keywords%")." LIMIT 1");
		$result = array();
		if($total){
			$query = $this->_db->query("SELECT * FROM pw_forums WHERE name like ".pwEscape("%$keywords%")." LIMIT ".$start.",".$perpage);
			while($rs = $this->_db->fetch_array($query)){
				$result[] = $rs;
			}
		}
		return $this->_buildForumResult($total,$result,$keywords);
	}
	/*组装版块内容*/
	function _buildForumResult($total,$result,$keywords){
		return array($total,$result,$keywords);
	}
	/*获取版块信息*/
	function getForums($forums){
		if(!$forums){
			return array();
		}
		return $forums;
	}
	
	/*
	 * 搜索群组接口，定制一
	 */
	function searchGroups($keywords,$page=1,$perpage=20){
		$keywords = $this->checkKeyWord($keywords);
		if(!$keywords){
			return array();
		}
		return $this->searchGroupsAPI($keywords,$page,$perpage);
	}

	/*群组搜索*/
	function searchGroupsAPI($keywords,$page=1,$perpage=20){
		$page = $page>1 ? $page : 1;
		$start = intval(($page - 1) * $perpage);
		$total = $this->_db->get_value("SELECT COUNT(*) FROM pw_colonys WHERE cname like ".pwEscape("%$keywords%")." LIMIT 1");
		$result = array();
		if($total){
			$query = $this->_db->query("SELECT * FROM pw_colonys WHERE cname like ".pwEscape("%$keywords%")." LIMIT ".$start.",".$perpage);
			while($rs = $this->_db->fetch_array($query)){
				$result[] = $rs;
			}
		}
		return $this->_buildGroupResult($total,$result,$keywords);
	}
	
	/*组装版块内容*/
	function _buildGroupResult($total,$result,$keywords){
		return array($total,$result,$keywords);
	}
	/*获取版块信息*/
	function getSearchGroups($groups){
		if(!$groups){
			return array();
		}
		return $groups;
	}
	
	/**
	 * 工具系列：根椐用户名查找用户ID
	 */
	
	function checkKeyWord($k){
		if($this->_withlen && strlen($k)<3){/*长度限制*/
			return array();
		}
		$k = trim(($k));
		$k = str_replace(array("&#160;","&#61;","&nbsp;","&#60;","<",">","&gt;","(",")","&#41;"),array(" "),$k);/*替换*/
		$ks = explode(" ",$k);
		$keywords = array();
		foreach($ks as $v){
			$v = trim($v);
			($v)&& $keywords[] = $v;
		}
		if(!$keywords){
			return array();
		}
		$keywords = implode(" ",$keywords);
		$this->debug($keywords);
		return $keywords;
	}
	
	function checkTime($starttime,$endtime){
		$starttime && $starttime = PwStrtoTime($starttime);
		$endtime   && $endtime   = PwStrtoTime($endtime);
		if($starttime && !$endtime){
			$endtime = $this->_time;/*开始到当前时间*/
		}
		if($endtime && !$starttime){
			$starttime = 0;/*1970年前到当前时间*/
		}
		return array($starttime,$endtime);
	}
	
	function getUserByUserName($userName){
		if(!$userName){
			return array();
		}
		$userNames = explode(" ",strip_tags(trim($userName)));
		if(!$userNames){
			return array();
		}
		$query = $this->_db->query("SELECT uid FROM pw_members WHERE username in(".pwImplode($userNames).")");
		$result = array();
		while($rs = $this->_db->fetch_array($query)){
			$result[] = $rs['uid'];
		}
		return $result;
	}
	
	function highlighting($pattern,$subject){
		return preg_replace('/(?<=[^\w=]|^)('.preg_quote($pattern,'/').')(?=[^\w=]|$)/si','<font color="red"><u>\\1</u></font>',$subject);
	}
	
	/*
	 * 获取索引源
	 */
	function getIndex($index){
		$map = $this->setMap();
		return ($this->_sphinx[$index]) ? $this->_sphinx[$index] : $map[$index];
	}
	
	/*
	 * 获取评分模式
	 */
	function getRanking(){
		$default = $this->getDefaults();
		return ($this->_sphinx['rank']) ? $this->_sphinx['rank'] : $default['rank'];
	}
	
	/*
	 * 获取分组模式
	 */
	function getGroup(){
		$default = $this->getDefaults();
		return ($this->_sphinx['group']) ? $this->_sphinx['group'] : $default['group'];
	}
	
	/*
	 * 设置对应图 主索引名称
	 * 注意，key不需要调整，只需要调整值
	 */
	function setMap(){
		return array( 
			'tindex'   => "threadsindex",      #帖子标题索引
			'tcindex'  => "tmsgsindex",        #帖子内容索引
			'taindex'  => "threadsallindex",   #帖子索引
			'pindex'   => "postsindex",        #回复索引
			'dindex'   => "diaryindex",        #日志标题索引
			'dcindex'  => "diarycontentindex", #日志内容索引
			'daindex'  => "diaryallindex",     #日志索引
			'mindex'   => "membersindex",      #用户索引
		);
	}
	
	
	/*
	 * 获取默认值
	 */
	function getDefaults(){
		return array ( 'isopen' => 0, 
					   'host' => 'localhost', 
					   'port' => 3312,
					   'rank'=>"SPH_RANK_PROXIMITY_BM25", 
					   'group'=>"SPH_GROUPBY_ATTR",
					   'tindex'=>"threadsindex",
					   'tcindex'=>"tmsgsindex",
					   'pindex'=>"postsindex"
		);
	}
	
	/*
	 * 获取评分模式
	 */
	function getRanks(){
		return array(1=>"SPH_RANK_PROXIMITY_BM25",2=>"SPH_RANK_BM25",3=>"SPH_RANK_NONE");;
	}
	
	/*
	 * 获取分组模式
	 */
	function getGroups(){
		return array(1=>"SPH_GROUPBY_DAY",2=>"SPH_GROUPBY_WEEK",3=>"SPH_GROUPBY_MONTH",4=>"SPH_GROUPBY_YEAR",5=>"SPH_GROUPBY_ATTR");
	}
	
	/*
	 * 获取搜索模式
	 */
	function _getMode($method,$query){
		return ($method=='AND' || strpos($query,'|') === false ) ? SPH_MATCH_ALL : SPH_MATCH_ANY;
	}
	
	/*
	 * 获取配置
	 */
	function _getConfig(){
		return array($this->_sphinx['host'],intval($this->_sphinx['port']));
	}
	
	/*
	 * 获取sphinx类
	 */
	function _getSphinx(){
		require ( "lib/sphinx.class.php" );
		return new SphinxClient ();
	}
	
	
}