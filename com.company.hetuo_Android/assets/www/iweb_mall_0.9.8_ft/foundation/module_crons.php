<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

// 获取需要更新的计划任务
function get_needful_crons(&$dbo,$table) {
	global $ctime;
	$now = $ctime->time_stamp();
	$sql="select * from `$table` where nextrun < '$now' ";
//	echo $sql;
	return $dbo->getRow($sql);
}

function get_crons_list(&$dbo,$table) {
	$sql="select * from `$table`";
	return $dbo->getRs($sql);
}

function del_crons(&$dbo,$table,$id) {
	$sql = "delete from `$table` where id in($id)";
	return $dbo->exeUpdate($sql);
}

function get_crons_row(&$dbo,$table,$id) {
	$sql = "select * from `$table` where id=$id";
	return $dbo->getRow($sql);
}

function update_crons(&$dbo,$table,$update_items,$brand_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where id='$brand_id'";
	return $dbo->exeUpdate($sql);
}

function insert_crons(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
	return $dbo->exeUpdate($sql);
}

function next_run($weekday,$day,$hour,$minute){
	global $ctime;
	$now = $ctime->time_stamp();
	$y = date('Y',$now);
	$m = date('m',$now);
	$d = date('d',$now);
	$w = date('w',$now);
	$h = date('H',$now);
	$i = date('i',$now);
	
	$arr_minute = explode(",",$minute);
	$min_minute = min($arr_minute);
	$max_minute = max($arr_minute);

	$new_y = $y;
	$new_d = $d;
	$new_m = $m;
	$new_h = $h;
	$new_i = 0;
	$status = false;

	if($weekday!='-1') {
		$addday = 0;
		if($weekday > $w) {
			$addday = $weekday-$w;
		} elseif($w > $weekday) {
			$addday = 7-$w+$weekday;
		}
		$new_d = $d + $addday;
	} elseif ($weekday=='-1' && $day!='-1') {
		if($d > $day){
			$new_d = $day;
			$new_m = $m+1;
			$status = true;
		} elseif($d < $day) {
			$new_d = $day;
		}
	}
	
	if($new_d > $d || $status) {
		if($hour!='-1') {
			$new_h = $hour;
		} else {
			$new_h = 0;
		}
		return mktime($new_h,$min_minute,0,$new_m,$new_d,$new_y);
	}

	if($hour!='-1') {
		$new_h = $hour;
	}

	if($i < $max_minute) {
		foreach($arr_minute as $value) {
			if($i < $value) {
				$new_i = $value;
				break;
			}
		}
	} else {
		$new_i = $min_minute;
		if($hour!='-1') {
			$new_h = $hour;
			if($hour <= $h) {
				if($weekday!='-1') {
					if($weekday > $w) {
						$addday = $weekday-$w;
					} elseif($w > $weekday) {
						$addday = 7-$w+$weekday;
					} else {
						$addday = 7;
					}
					$new_d = $d + $addday;
				} elseif ($weekday=='-1' && $day!='-1') {
					$new_d = $d;
					if($d >= $day) {
						$new_m = $m+1;
					}
				} else {
					$new_d++;
				}
			}
		} else {
			$new_h++;
		}
	}
	return mktime($new_h,$new_i,0,$new_m,$new_d,$new_y);
}
?>