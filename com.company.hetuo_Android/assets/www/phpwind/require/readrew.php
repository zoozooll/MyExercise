<?php
!function_exists('readover') && exit('Forbidden');

$rewdb = $db->get_one("SELECT * FROM pw_reward WHERE tid=".pwEscape($tid));

$timeleave	= $rewdb['timelimit'] - $timestamp;
$rewardtype = $read['state'];
$rw_b_name	= is_numeric($rewdb['cbtype']) ? $_CREDITDB[$rewdb['cbtype']][0] : ${'db_'.$rewdb['cbtype'].'name'};
$rw_a_name	= is_numeric($rewdb['catype']) ? $_CREDITDB[$rewdb['catype']][0] : ${'db_'.$rewdb['catype'].'name'};

function Getrewhtml($lou,$ifreward,$pid) {
	global $rewardtype,$rw_b_name,$rw_a_name,$groupid,$admincheck,$authorid,$winduid,$tid,$rewdb,$timeleave;

	$html = "<div class=\"tips\" style=\"width:auto;\">";
	if ($lou == 0) {
		if ($rewardtype == '0') {
			$html .= '<span class="s3">'.getLangInfo('bbscode','rewarding');
			if ($timeleave > 3600) {
				$html .= ceil($timeleave/3600).getLangInfo('bbscode','hour');
			} elseif ($timeleave > 0) {
				$html .= ceil($timeleave/60).getLangInfo('bbscode','minute');
			} else {
				$html .= getLangInfo('bbscode','timeover');
			}
			$html .= ')...</span><div class="tac">'.getLangInfo('bbscode','reward_bestanswer').": $rewdb[cbval]&nbsp;$rw_b_name</div>";
			if ($rewdb['caval'] > 0) {
				$html .= "<div class=\"tac\">".getLangInfo('bbscode','reward_hlp').": $rewdb[caval] $rw_a_name</div>";
			}
			if ($groupid=='3' || $admincheck) {
				$html .= "<div class=\"tac\"><a href=\"job.php?action=endreward&tid=$tid\">".getLangInfo('bbscode','reward_cancle').'</a>&nbsp;</div>';
			} elseif ($authorid == $winduid && $timeleave < 0) {
				$html .= '<div class="tac"><a href="job.php?action=rewardmsg&tid='.$tid.'" title="'.getLangInfo('bbscode','reward_title').'" onClick="javascript:if(confirm(\''.getLangInfo('bbscode','reward_msgtoadmin').'\')){return true;}else{return false;}">'.getLangInfo('bbscode','reward_toadmin').'</a>&nbsp;</div>';
			}
		} else {
			$html .= "<span class=\"s3\">".getLangInfo('bbscode','reward_finished')."</span><div class=\"tac\">".getLangInfo('bbscode','reward_bestanswer').": $rewdb[cbval]&nbsp;$rw_b_name</div>";
			if ($rewardtype == 1) {
				$html .= "<div class=\"tac\">".getLangInfo('bbscode','reward_author').": $rewdb[author]</div>";
			} else {
				$html .= "<div class=\"tac\">".getLangInfo('bbscode','reward_endinfo_'.$rewardtype)."</div>";
			}
		}
	} else {
		if ($rewardtype=='1' && $ifreward>1) {
			$html .= "<span class=\"s3\">".getLangInfo('bbscode','reward_best_get')."</span>: (+$rewdb[cbval])&nbsp;$rw_b_name";
		} elseif ($ifreward=='1') {
			$html .= "<span class=\"s3\">".getLangInfo('bbscode','reward_help_get')."</span>: (+1)&nbsp;$rw_a_name";
		} elseif ($authorid==$winduid && $rewardtype=='0' && $ifreward==0) {
			$html .= "<span class=\"s3\">".getLangInfo('bbscode','reward_manager')."</span>: [<a href=\"job.php?action=reward&tid=$tid&pid=$pid&type=1\">".getLangInfo('bbscode','reward_bestanswer')."</a>]";
			$rewdb['caval']>0 && $html .= "[<a href=\"job.php?action=reward&tid=$tid&pid=$pid&type=2\">".getLangInfo('bbscode','reward_help')."</a>]";
		}
	}
	$html .= "</div><div class=\"c\"></div>";
	return $html;
}
?>