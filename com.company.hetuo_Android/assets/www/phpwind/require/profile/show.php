<?php
!function_exists('readover') && exit('Forbidden');

InitGP(array('uid','username'));
ObHeader('u.php?action=show&'.($username ? 'username='.$username : 'uid='.$uid));

?>