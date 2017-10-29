<?php
	$user_id = get_sess_user_id();
	if(!$user_id){
		$iweb_shop=get_cookie('iweb_login');
		if($iweb_shop){
			$iweb_shop=explode('|',$iweb_shop);
			$cuser_name = $iweb_shop[0];
			$cuser_pw = isset($iweb_shop[1])?$iweb_shop[1]:"";
			$t_users = $tablePreStr."users";
			$t_user_rank = $tablePreStr."user_rank";
			$t_shop_info = $tablePreStr."shop_info";
			
			$dbo=new dbex();
			dbtarget('r',$dbServs);
			$sql="select a.*,b.*,c.shop_id from $t_users as a,$t_user_rank as b,$t_shop_info as c where a.user_email='$cuser_name' and a.user_id=c.user_id and a.rank_id=b.rank_id";
			$user_row = $dbo->getRow($sql);
//			$user_id = $user_row['user_id'];
			if($user_row['user_passwd']==$cuser_pw){
				set_sess_user_name($user_row['user_name']);
				set_sess_user_id($user_row['user_id']);
				set_sess_user_email($user_row['user_email']);
				set_sess_email_check($user_row['email_check']);
				set_sess_shop_id($user_row['shop_id']);
				set_sess_rank_id($user_row['rank_id']);
				set_sess_rank_name($user_row['rank_name']);
				set_sess_privilege($user_row['privilege']);
			}
		}
	}
?>