<?php
//站点离线控制代码
	if($SYSINFO['offline']=='false'){	
		   echo $SYSINFO['off_info'];
		   exit();
	}
?>