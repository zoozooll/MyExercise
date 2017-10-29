<?php
/**
 * class.WordSplitter.php
 */
class WordSplitter {
	private $mKeywords = false;
	private $mDict = "dba/dict.db";
	private $mDba = false;
	private $mSegment = array();

	function WordSplitter($kw=false) {
		if ($kw) {
			$this->init($kw);
		}
	}

	function init($kw) {
		$this->mKeywords = trim($kw);
		if (!$this->mDba) {
			if(DIRECTORY_SEPARATOR == "/") { //linux
				$this->mDba = dba_open(__ROOT_PATH . $this->mDict, "r", "db4");
			} else { //windows
				$this->mDba = dba_open(__ROOT_PATH . $this->mDict, "r", "inifile");
			}
		}
		return ($this->mDba ? true : false);
	}

	function free() {
		@dba_close($this->mDba);
		$this->mKeywords = false;
		$this->mSegment = array();
	}

	function segment() {
		$length = strlen($this->mKeywords);
		if (0 == $length) {
			return false;
		}
		$this->mSegment = array();
		if (127 < ord($this->mKeywords[0])) {
			$mode_old = __CHAR_CHINESE;
		}
		elseif (ereg("[a-zA-Z_\.\'0-9\+\-]", $this->mKeywords[0])) {
			$mode_old = __CHAR_ENGLISH;
		}
		elseif (ereg("[0-9]", $this->mKeywords[0])) {
			$mode_old = __CHAR_DIGITAL;
		}
		else {
			$mode_old = __CHAR_UNKNOWN;
		}
		$begin = 0;
		for ($i = 1; $i < $length; $i++) {
			if (127 < ord($this->mKeywords[$i])) {
				$mode_new = __CHAR_CHINESE;
			}
			elseif (ereg("[a-zA-Z_\.\'0-9\+\-]", $this->mKeywords[$i])) {
				$mode_new = __CHAR_ENGLISH;
			}
			elseif (ereg("[0-9]", $this->mKeywords[$i])) {
				$mode_new = __CHAR_DIGITAL;
			}
			else {
				$mode_new = __CHAR_UNKNOWN;
			}
			if ($mode_new != $mode_old) {
				$ended = $i;
				$this->mSegment[] = array($mode_old, $begin, $ended);
				$mode_old = $mode_new;
				$begin = $i;
			}
		}
		$ended = $i;
		$this->mSegment[] = array($mode_old, $begin, $ended);
		return true;
	}

	function splitChinese($begin, $ended) {
		$result = array();
		$sub = "";
		$start = $begin - 1; //! there is a $i++ in the for()
		$word = false;
		for ($i = $begin; $start < $ended - 1; $i++) {
			if ($i >= $ended) {
				if($word) {
					$result[] = $word;
				}
				$sub = "";
				$word = false;
				$i = $start;
				continue;
			}
			$sub .= $this->mKeywords[$i] . $this->mKeywords[$i+1];
			$i++;
			$value = dba_fetch($sub, $this->mDba);
			switch ($value) {
			case false: //! no found
				if (2 >= strlen($sub)) {
					$word = $sub[0] . $sub[1];
					$start += 2;
				}
				if($word) {
					$result[] = $word;
				}
				$sub = "";
				$word = false;
				$i = $start;
				break;
			case 1: //! 001
				if (2 >= strlen($sub)) {
					$word = $sub[0] . $sub[1];
					$start += 2;
				}
				break;
			case 2: //! 010
				$word = $sub;
				$start = $i;
				$result[] = $word;
				$sub = "";
				$word = false;
				break;
			case 3: //! 011
				$word = $sub;
				$start = $i;
				break;
			case 6: //! 110
				$start = $i;
				$sub = "";
				$word = false;
				break;
			case 7: //! 111
				$start = $i;
				$word = false;
				break;
			default:
				break;
			}
//@ echo "s[" . $sub . "] v[" . $value . "] w[" . $word . "] r[" . $result . "]<br />\n";
		}
		if($word) {
			$result[] = $word;
		}
		return $result;
	}

	function validEnglish($word) {
		if ($value = dba_fetch($word, $this->mDba)) {
			if ($value & 4) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}

	function execute($kw) {
		if ($kw) {
			$this->init($kw);
		}
		if ($this->mKeywords && $this->mDba) {
			$this->segment();
			$result = array();
			foreach ($this->mSegment as $rSegment) {
				if (__CHAR_DIGITAL == $rSegment[0]) {
					$word = substr($this->mKeywords, $rSegment[1], $rSegment[2] - $rSegment[1]);
					$result[] = $word;
				}
				elseif (__CHAR_ENGLISH == $rSegment[0]) {
					$word = substr($this->mKeywords, $rSegment[1], $rSegment[2] - $rSegment[1]);
					if ($this->validEnglish($word)) {
						$result[] = $word;
					}
				}
				elseif (__CHAR_CHINESE == $rSegment[0]) {
					$result = array_merge($result, $this->splitChinese($rSegment[1], $rSegment[2]));
				}
				else { /// __CHAR_SPECIAL
					;
				}
			}
//			$this->free();
			return $result;
		}
		else {
			$this->free();
			return "";
		}
	}
} // end of Class WordSplitter.
?>