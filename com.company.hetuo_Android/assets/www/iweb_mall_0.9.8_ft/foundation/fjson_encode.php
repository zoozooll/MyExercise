<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/**
 * json_encode函数
 *
 * @return  string
 */
if(!function_exists('json_encode')) {
	function json_encode($arg) {
		$returnValue = '';
		$c           = '';
		$i           = '';
		$l           = '';
		$s           = '';
		$v           = '';
		$numeric     = true;

		switch (gettype($arg)) {
			case 'array':
				foreach ($arg AS $i => $v) {
					if (!is_numeric($i)) {
						$numeric = false;
						break;
					}
				}

				if ($numeric) {
					foreach ($arg AS $i => $v) {
						if (strlen($s) > 0) {
							$s .= ',';
						}
						$s .= json_encode($arg[$i]);
					}

					$returnValue = '[' . $s . ']';
				} else {
					foreach ($arg AS $i => $v) {
						if (strlen($s) > 0) {
							$s .= ',';
						}
						$s .= json_encode($i) . ':' . json_encode($arg[$i]);
					}

					$returnValue = '{' . $s . '}';
				}
				break;

			case 'object':
				foreach (get_object_vars($arg) AS $i => $v) {
					$v = json_encode($v);

					if (strlen($s) > 0) {
						$s .= ',';
					}
					$s .= json_encode($i) . ':' . $v;
				}

				$returnValue = '{' . $s . '}';
				break;

			case 'integer':
			case 'double':
				$returnValue = is_numeric($arg) ? (string) $arg : 'null';
				break;

			case 'string':
				$returnValue = '"' . strtr($arg, array(
					"\r"   => '\\r',    "\n"   => '\\n',    "\t"   => '\\t',     "\b"   => '\\b',
					"\f"   => '\\f',    '\\'   => '\\\\',   '"'    => '\"',
					"\x00" => '\u0000', "\x01" => '\u0001', "\x02" => '\u0002', "\x03" => '\u0003',
					"\x04" => '\u0004', "\x05" => '\u0005', "\x06" => '\u0006', "\x07" => '\u0007',
					"\x08" => '\b',     "\x0b" => '\u000b', "\x0c" => '\f',     "\x0e" => '\u000e',
					"\x0f" => '\u000f', "\x10" => '\u0010', "\x11" => '\u0011', "\x12" => '\u0012',
					"\x13" => '\u0013', "\x14" => '\u0014', "\x15" => '\u0015', "\x16" => '\u0016',
					"\x17" => '\u0017', "\x18" => '\u0018', "\x19" => '\u0019', "\x1a" => '\u001a',
					"\x1b" => '\u001b', "\x1c" => '\u001c', "\x1d" => '\u001d', "\x1e" => '\u001e',
					"\x1f" => '\u001f'
				)) . '"';
				break;

			case 'boolean':
				$returnValue = $arg?'true':'false';
				break;

			default:
				$returnValue = 'null';
		}

		return $returnValue;
	}
}
?>