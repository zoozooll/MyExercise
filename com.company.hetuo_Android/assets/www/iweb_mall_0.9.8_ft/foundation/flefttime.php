<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/**
 *    计算剩余时间
 *
 *    @param     string $format
 *    @param     string $time;
 *    @return    string
 */
function time_left($time, $format = null)
{
    $time_stamp = new time_class();
	$time_stamp = $time_stamp -> time_stamp();
    $lefttime = $time - $time_stamp - date('Z');
    if ($lefttime < 0)
    {
        return '';
    }
    if ($format === null)
    {
        if ($lefttime < 3600)
        {
            $format = '%i分钟';
        }
        elseif ($lefttime < 86400)
        {
            $format = '%h小时%i分';
        }
        else
        {
            $format = '%d天%h小时';
        }
    }
    $d = intval($lefttime / 86400);
    $lefttime -= $d * 86400;
    $h = intval($lefttime / 3600);
    $lefttime -= $h * 3600;
    $m = intval($lefttime / 60);
    $lefttime -= $m * 60;
    $s = $lefttime;
    return str_replace(array('%d', '%h', '%i', '%s'),array($d, $h,$m, $s), $format);
}
?>