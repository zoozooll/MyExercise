<?php
/**
 * class.CsvAPI.php
 *-------------------------
 *
 * cvs file import function
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 */

class CSV {
	public $localEncoding = NULL;
	public $seperator = ",";
	public $enclosure = "\"";
	protected $topLineNum = 2;
	protected $maxLineNum = 1000000;
	protected $maxLineSize = 4096;
	protected $streamReadHandle = NULL;
	
	/**
	 * ��CSV�ļ�������ת��������
	 * @param $maxLineNum ȡCSV�ļ��е�����,-1Ϊ����.�����ܳ���$this->maxLineNum
	 * ˵��: ����ֵ��������: $result[$rowNo]['$colKey']
	 *      ��$hasHeadFlag==false �� $rowNo=0����ڵ�һ��,��$colKey��Ӧ�к�.
	 *      ��$hasHeadFlag==true �� $rowNo=0����ڵڶ���,��$colKey,��ӦΪ��һ�е�ֵ.
	 */
	public function loadToArray($pathfile, $hasHeadFlag=false, $maxLineNum=-1) {
		if($maxLineNum < 0) {
			$maxLineNum = $this->maxLineNum;
		}
		$handle = @fopen($pathfile, "r");
		if($handle == false) {
			return NULL;
			//throw new Exception("can not open [$pathfile](r).");
		}
		$result = array();
		$suffix = 0;
		for( $loopTop=0; $loopTop<$maxLineNum && !feof($handle) ; $loopTop++) {
			$arr = fgetcsv($handle, $this->maxLineSize, $this->seperator, $this->enclosure);
			//utf8 => shift-jis
			for($loop=0; $arr && $loop<count($arr); $loop++) {
				$arr[$loop] = $this->decode($arr[$loop]);
			}
			if($hasHeadFlag) {
				if($loopTop==0) { //��һ��,ȡHEAD
					$head = $arr;
				} else {
					//ת��KEYΪHEAD
					for($loop=0; $arr && $loop<count($arr); $loop++) {
						$result[$loopTop-1][$head[$loop]] = $arr[$loop];
					}
				}
			} else {
				$result[$loopTop] = $arr;
			}
		}
		fclose ($handle);
		return $result;
	}
	
	/**
	 * ������(��ά����)ת����CSV�ļ�������
	 * ˵��: ����ֵ��������: void
	 *      ��$hasHeadFlag==false �� $rowNo=0����ڵ�һ��,��$colKey��Ӧ�к�.
	 *      ��$hasHeadFlag==true �� $rowNo=0����ڵڶ���,��$colKey,��ӦΪ��һ�е�ֵ.
	 */
	public function storeFromArray($pathfile, $result, $hasHeadFlag=false) {
		$handle = @fopen($pathfile, "w");
		if($handle == false) {
			throw new Exception("can not open [$pathfile](w).");
		}
		if($result == NULL || !is_array($result)) {
			fclose($handle);
			return;
		}
		if($hasHeadFlag == false) {
			for($loop=0; $loop<count($result); $loop++) {
				$line = $this->formatLine($result[$loop]);
				fwrite($handle, $line);
			}
		} else if(count($result) > 0) {
			//��һ���Ǳ�ͷ
			$head = array();
			foreach($result[0] as $key => $val) {
				$head[] = $key;
			}
			$line = $this->formatLine($head);
			fwrite($handle, $line);
			//������
			for($loop=0; $loop<count($result); $loop++) {
				$arr = array();
				foreach($result[$loop] as $val) { //convert Array
					$arr[] = $val;
				}
				$line = $this->formatLine($arr);
				fwrite($handle, $line);
			}
		}
		fclose ($handle);
		chmod($pathfile, 0666);
	}
	
	/**
	 * ���ж�ȡCSV�ļ�
	 */
	public function &streamRead($filename = "") {
		if($this->streamReadHandle == NULL) {
			if(($this->streamReadHandle = fopen($filename, "r")) == false) {
				$this->streamReadHandle = NULL;
				return false;
			}
		}

		if(($arr = fgetcsv($this->streamReadHandle, $this->maxLineSize, $this->seperator, $this->enclosure)) == false) {
			fclose($this->streamReadHandle);
			$this->streamReadHandle = null;
			return false;
		}
		//utf8 => shift-jis
		for($loop=0; $arr && $loop<count($arr); $loop++) {
			$arr[$loop] = $this->decode($arr[$loop]);
		}
		return $arr;			
	}

	protected function &parseLine(&$line, $sortStyle="") {
		$inEnclosure = false;
		$cnt = strlen($line);
		$start = 0;
		$arr = array();
		if(is_array($sortStyle)) {
			for($loop=0; $loop<$sortStyle['count']; $loop++) {
				$arr[$loop] = "";
			}
			$overOffset = $sortStyle['count'];
		} else {
		}
		//line seperator
		for($loop=$cnt-1; $loop>=0; $loop--) {
			if($line[$loop] == "\r" || $line[$loop] == "\n") {
				$cnt--;
			} else {
				break;
			}
		}
		for($loop=0, $suffix=0; $loop<$cnt; $loop++) {
			if($this->enclosure != "" && $line[$loop] == $this->enclosure) {
				if(($loop == 0) || ($loop > 0 && $line[$loop-1] != '\\')) {
					if($inEnclosure) {
						$inEnclosure = false;
					} else {
						$inEnclosure = true;
						$start = $loop + 1;
					}
				}
			}
			if(!$inEnclosure && $line[$loop] == $this->seperator) {
				$end = $loop - strlen($this->enclosure);
				$val = $this->decode(substr($line, $start, $end - $start));
				$col = substr("00", strlen($suffix+1)) . ($suffix+1);
				if(is_array($sortStyle)) { //sort
					//$key is current column num; $sortStyle is mapping the column actual postion
					$key = (string)($suffix + 1);
					if(isset($sortStyle[$key])) {
						$arr[$sortStyle[$key]]['val'] = $val;
						$arr[$sortStyle[$key]]['col'] = $col;
					} else {
						$arr[$overOffset]['val'] = $val; //$overOffset is the out of mapping offset
						$arr[$overOffset]['col'] = $col;
						$overOffset++;
					}
				} else { //not sort
//del 1/16/2006					$arr[$suffix] = $val;
					$arr[$suffix]['val'] = $val;
					$arr[$suffix]['col'] = $col;
				}
				$suffix++;
				$start = $loop + 1;
			}
		}
		if(!$inEnclosure) { //last attribute
			$end = $loop - strlen($this->enclosure);
			$val = $this->decode(substr($line, $start, $end - $start));
			$col = substr("00", strlen($suffix+1)) . ($suffix+1);
			if(is_array($sortStyle)) { //sort
				$key = (string)($suffix + 1);
				if(isset($sortStyle[$key])) {
					$arr[$sortStyle[$key]]['val'] = $val;
					$arr[$sortStyle[$key]]['col'] = $col;
				} else {
					$arr[$overOffset]['val'] = $val;
					$arr[$overOffset]['col'] = $col;
					$overOffset++;
				}
			} else { //not sort
//del 1/16/2006				$arr[$suffix] = $val;
				$arr[$suffix]['val'] = $val;
				$arr[$suffix]['col'] = $col;
				$suffix++;
			}
		}
		return $arr;
	}
	
	protected function formatLine($arr) {
		$line = "";
		for($loop=0; $loop<count($arr); $loop++) {
			if($loop > 0) {
				$line .= $this->seperator;
			}
			$line .= $this->enclosure . $this->encode($arr[$loop]) . $this->enclosure;
		}
		$line .= "\r\n";
		return $line;
	}
	
	protected function &keyToIndexArray($keyArr, $filter = "") {
		$arr = array();
		if(!is_array($keyArr)) {
			return $arr;
		}
		$suffix = 0;
		foreach($keyArr as $key => $value) {
			if(isset($filter[$key])) {
				continue;
			}
			$arr[$suffix++] = $key;
		}
		sort($arr);
		return $arr;
	}

	protected function &encode(&$str) {
		$str = str_replace($this->enclosure, $this->enclosure.$this->enclosure, $str);
		$str = str_replace("\\", "\\\\", $str);
		$str = str_replace("\r", "\\r", $str);
		$str = str_replace("\n", "\\n", $str);
		if($this->localEncoding == NULL) {
			return $str;
		}
		$str = iconv($this->localEncoding, "UTF-8//IGNORE", $str);

		return $str;
	}

	protected function &decode(&$str) {
		$str = str_replace($this->enclosure.$this->enclosure, $this->enclosure, $str);
		$str = str_replace("\\\\", "\\", $str);
		$str = str_replace("\\r", "\r", $str);
		$str = str_replace("\\n", "\n", $str);
		if($this->localEncoding == NULL) {
			return $str;
		}
		$str = iconv("UTF-8", $this->localEncoding."//IGNORE", $str);
		

		return $str;
	}

}
?>