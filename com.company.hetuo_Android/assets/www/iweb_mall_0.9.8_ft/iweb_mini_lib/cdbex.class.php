<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

class dbex
{
	var $rowCount;
	var $affectedRows;

	public function getRs($sql) {
		$rs=array();
		$result=mysql_query($sql) or die('false getRs, Sql:'.$sql);
		$this->rowCount=mysql_num_rows($result);

		while($rsRow=mysql_fetch_array($result)) {
			$rs[]=$rsRow;
		}
		return $rs;
	}
	public function getRsassoc($sql) {
		$rs=array();
		$result=mysql_query($sql) or die('false getRsassoc, Sql:'.$sql);
		$this->rowCount=mysql_num_rows($result);

		while($rsRow=mysql_fetch_assoc($result)) {
			$rs[]=$rsRow;
		}
		return $rs;
	}

	public function getRow($sql) {
		$result=mysql_query($sql) or die('false getRow, Sql:'.$sql);
		return mysql_fetch_array($result);
	}

	public function exeUpdate($sql) {
		if(mysql_query($sql)){
			return true;
		} else {
			return false;
		}
	}

	function fetch_page($sql, $pagenum) {
		$page = 1;

		if(isset($_GET['page'])){
			$page =intval($_GET['page']);
		}
		$countnum=0;
		$query = mysql_query($sql);
		if($query){
			$countnum = mysql_num_rows($query);
		}
		$countpage = ceil($countnum/$pagenum);

		if($page<1) { $page=1; }
		if($page>$countpage) { $page=$countpage; }

		$limitstart = ($page-1)*$pagenum;

		/* 获取数据结果集 */
		$result = array();
		for($i=0; $i<($limitstart+$pagenum); $i++) {
			if($i>=$countnum) { break; }
			if($i>=$limitstart) {
				$result[] = mysql_fetch_array($query);
			} else {
				mysql_fetch_array($query);
			}
		}
		//$sql = $sql." limit $limitstart,$pagenum";
		//$result = $this->fetch_All($sql);

		/* 生成url */
		$url = $_SERVER['QUERY_STRING'];
		$url = preg_replace("/&?page=[0-9]+/i","",$url);

		$array['countnum'] = $countnum;
		$array['countpage'] = $countpage;
		$array['result'] = $result;
		$array['page'] = $page;
		if($page>1) {
			$array['preurl'] = "?".$url."&page=".($page-1);
			$array['prepage'] = $page-1;
		} else {
			$array['preurl'] = "?".$url."&page=1";
			$array['prepage'] = 1;
		}
		if($page<$countpage) {
			$array['nexturl'] = "?".$url."&page=".($page+1);
			$array['nextpage'] = $page+1;
		} else {
			$array['nexturl'] = "?".$url."&page=".$countpage;
			$array['nextpage'] = $countpage;
		}
		$array['firsturl'] = "?".$url."&page=1";
		$array['firstpage'] = 1;
		$array['lasturl'] = "?".$url."&page=".$countpage;
		$array['lastpage'] = $countpage;
		$array['nopage'] = "?".$url."&page=";

		return $array;
	}

	public function create($sql) {
		$result=mysql_query($sql);
		$create=mysql_fetch_array($result);

		return $create;
	}
	public  function createbyarr($info,$table){
		$i=0;
		foreach($info as $key=>$value){
			if ($i==0){
				$insertkey = "`".$key."`";
				$insertvalue = "'".$value."'";
			} else {
			$insertkey.= ",`".$key."`";
			$insertvalue.=",'".$value."'";
			}
			$i++;
		}
		$sql = "insert into $table($insertkey) values($insertvalue)";
		mysql_query($sql);
		return mysql_insert_id();
	}
	public  function updatebyarr($info,$table,$con){
		$i=0;
		foreach ($info as $key=>$value){
			if ($i==0){
				$change = "`$key`='$value'";
			} else{
				$change .=",`$key`='$value'";
			}

			$i++;
		}
		$sql = "update `$table` set $change where $con";
		return mysql_query($sql);
	}
	public  function query($sql){
		return mysql_query($sql);
	}
	function getCol($sql)
    {
        $res = mysql_query($sql);
        if ($res !== false)
        {
            $arr = array();
            while ($row = mysql_fetch_row($res))
            {
                $arr[] = $row[0];
            }

            return $arr;
        }
        else
        {
            return false;
        }
    }
}

?>