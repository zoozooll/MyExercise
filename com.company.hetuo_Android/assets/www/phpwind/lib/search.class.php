<?php
/*
 * 搜索工具类
 * 支持全文索引搜索与普通搜索
 * @2009-10-26
 */
!defined ('P_W') && exit('Forbidden');
class PW_Search {

	var $_sphinx = null;

	function PW_Search(){
		global $db_sphinx;
		$this->_sphinx = &$db_sphinx;
	}
	/*
	 * Sphinx全文索引搜索
	 */
	function sphinxSearch($q,$method,$index,$digest,$fid,$exclude,$sortby,$asc,$authorids,$sch_timemin,$sch_timemax,$groupby,$db_perpage){
		$sphinx = $this->_getSphinx();
		list($host,$port) = $this->_getConfig();
		$sphinx->SetServer ( $host, $port );
		$sphinx->SetConnectTimeout ( 1 );
		$sphinx->SetMatchMode ( $this->_getMode($method,$q) );
		$digest && $sphinx->SetFilter ('digest',array(1,2));
		$fid && $sphinx->SetFilter ('fid',$fid,$exclude);
		$authorids && $sphinx->SetFilter ('authorid',$authorids);
		if($sch_timemin && $sch_timemax){
			$sphinx->SetFilterRange('postdate',$sch_timemin,$sch_timemax);
		}
		$groupby && $sphinx->SetGroupBy ( $groupby, $this->getGroup(), "@group desc" );
		$sortby && $sphinx->SetSortMode ( ($asc=='DESC' ? SPH_SORT_ATTR_DESC : SPH_SORT_ATTR_ASC), $sortby );
		$page = isset($_GET['page']) ? $_GET['page'] : 1;
		$page = max(1, intval($page));
		$start_limit = intval(($page - 1) * $db_perpage);
		$sphinx->SetLimits ( $start_limit, intval($db_perpage), ( $db_perpage>1000 ) ? $db_perpage : 1000 );
		$sphinx->SetRankingMode ( $this->getRanking() );
		$sphinx->SetArrayResult ( true );
		$index = $this->getIndex($index);
		$result = $sphinx->Query ( str_replace('|',' ',$q), $index );
		if ( $result === false ){
			return false;
		}
		return $this->_buildResult($result,$index);
	}

	/*
	 * 组装结果页数据
	 */
	function _buildResult($result,$index){
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
				if(strpos($index,'tmsgs')!==false || strpos($index,'threads')!==false){
					$tids .= $docinfo['id'];
				}else{
					$tids .= $docinfo['attrs']['tid'];
				}
			}
			return array($totals,$tids,$words);
		}else{
			return false;
		}
	}

	/*
	 * 获取索引源
	 */
	function getIndex($index){
		$map = array_flip($this->setMap());
		$index = $map[$index];
		$default = $this->getDefaults();
		return ($this->_sphinx[$index]) ? $this->_sphinx[$index] : $default[$index];
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
	 * 设置对应图
	 */
	function setMap(){
		return array( 'tindex'=>"threadsindex",
					  'tcindex'=>"tmsgsindex",
					  'pindex'=>"postsindex"
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