<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_users.php");
require_once("foundation/module_credit.php");

//引入语言包
$s_langpackage=new shoplp;

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_shop_guestbook = $tablePreStr."shop_guestbook";
$t_credit = $tablePreStr."credit";
$t_integral = $tablePreStr."integral";

/* 商铺信息处理 */
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}
$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks[0];

$header['title'] = $s_langpackage->s_shop_credit." - ".$SHOP['shop_name'];
$header['keywords'] = $SHOP['shop_management'];
$header['description'] = sub_str(strip_tags($SHOP['shop_intro']),100);

/* 本页面信息处理 */
$user_id=$SHOP['user_id'];
$sql="select * from $t_credit where buyer=$user_id or seller=$user_id";
$credit = $dbo->getRs($sql);

$seller_credit=$seller_credit_num = $seller_credit_before=$seller_credit_sex=0;
$seller_credit_num_2 = $seller_credit_before_2=$seller_credit_sex_2=0;
$seller_credit_num_1 = $seller_credit_before_1=$seller_credit_sex_1=0;
$seller_credit_num_0 = $seller_credit_before_0=$seller_credit_sex_0=0;
$seller_credit_month=$seller_credit_month_0=$seller_credit_month_1=$seller_credit_month_2=0;
$seller_credit_weed=$seller_credit_weed_0=$seller_credit_weed_1=$seller_credit_weed_2=0;

$buyer_credit=$buyer_credit_num = $buyer_credit_before=$buyer_credit_sex=0;
$buyer_credit_num_2 = $buyer_credit_before_2=$buyer_credit_sex_2=0;
$buyer_credit_num_1 = $buyer_credit_before_1=$buyer_credit_sex_1=0;
$buyer_credit_num_0 = $buyer_credit_before_0=$buyer_credit_sex_0=0;
$buyer_credit_month=$buyer_credit_month_0=$buyer_credit_month_1=$buyer_credit_month_2=0;
$buyer_credit_weed=$buyer_credit_weed_0=$buyer_credit_weed_1=$buyer_credit_weed_2=0;

$SexMonth = $ctime->time_stamp() - (180 * 24 * 60 * 60);
$SexMonth = date('Y-m-d', $SexMonth);
$LastMonth = $ctime->time_stamp() - (30 * 24 * 60 * 60);
$LastMonth = date('Y-m-d', $LastMonth);
$LastWeek = $ctime->time_stamp() - (7 * 24 * 60 * 60);
$LastWeek = date('Y-m-d', $LastWeek);
foreach ($credit as $key=>$val){
	//作为卖家
	if($val['seller']==$user_id){

		if($val['seller_evaltime']<$SexMonth){
			if($val['seller_credit']==1){
				$seller_credit_before_2++;
			}
			if($val['seller_credit']==0){
				$seller_credit_before_1++;
			}
			if($val['seller_credit']==-1){
				$seller_credit_before_0++;
			}
			$seller_credit_before=$seller_credit_before_0+$seller_credit_before_1+$seller_credit_before_2;
		}else
		if($val['seller_evaltime']>=$SexMonth){
			if($val['seller_credit']==1){
				$seller_credit_sex_2++;
			}
			if($val['seller_credit']==0){
				$seller_credit_sex_1++;
			}
			if($val['seller_credit']==-1){
				$seller_credit_sex_0++;
			}
			$seller_credit_sex=$seller_credit_sex_0+$seller_credit_sex_1+$seller_credit_sex_2;

			if($val['seller_evaltime']>=$LastMonth){
				if($val['seller_credit']==1){
					$seller_credit_month_2++;
				}
				if($val['seller_credit']==0){
					$seller_credit_month_1++;
				}
				if($val['seller_credit']==-1){
					$seller_credit_month_0++;
				}
				$seller_credit_month=$seller_credit_month_0+$seller_credit_month_1+$seller_credit_month_2;

				if($val['seller_evaltime']>=$LastWeek){
					if($val['seller_credit']==1){
						$seller_credit_weed_2++;
					}
					if($val['seller_credit']==0){
						$seller_credit_weed_1++;
					}
					if($val['seller_credit']==-1){
						$seller_credit_weed_0++;
					}
					$seller_credit_weed=$seller_credit_weed_0+$seller_credit_weed_1+$seller_credit_weed_2;
				}
			}
		}
		$seller_credit=$seller_credit+$val['seller_credit'];
	}
	//作为买家
	if($val['buyer']==$user_id){

		if($val['buyer_evaltime']<$SexMonth){
			if($val['buyer_credit']==1){
				$buyer_credit_before_2++;
			}
			if($val['buyer_credit']==0){
				$buyer_credit_before_1++;
			}
			if($val['buyer_credit']==-1){
				$buyer_credit_before_0++;
			}
			$buyer_credit_before=$buyer_credit_before_0+$buyer_credit_before_1+$buyer_credit_before_2;
		}else
		if($val['buyer_evaltime']>=$SexMonth){
			if($val['buyer_credit']==1){
				$buyer_credit_sex_2++;
			}
			if($val['buyer_credit']==0){
				$buyer_credit_sex_1++;
			}
			if($val['buyer_credit']==-1){
				$buyer_credit_sex_0++;
			}
			$buyer_credit_sex=$buyer_credit_sex_0+$buyer_credit_sex_1+$buyer_credit_sex_2;

			if($val['buyer_evaltime']>=$LastMonth){
				if($val['buyer_credit']==1){
					$buyer_credit_month_2++;
				}
				if($val['buyer_credit']==0){
					$buyer_credit_month_1++;
				}
				if($val['buyer_credit']==-1){
					$buyer_credit_month_0++;
				}
				$buyer_credit_month=$buyer_credit_month_0+$buyer_credit_month_1+$buyer_credit_month_2;

				if($val['buyer_evaltime']>=$LastWeek){
					if($val['buyer_credit']==1){
						$buyer_credit_weed_2++;
					}
					if($val['buyer_credit']==0){
						$buyer_credit_weed_1++;
					}
					if($val['buyer_credit']==-1){
						$buyer_credit_weed_0++;
					}
					$buyer_credit_weed=$buyer_credit_weed_0+$buyer_credit_weed_1+$buyer_credit_weed_2;
				}
			}
		}
		$buyer_credit=$buyer_credit+$val['buyer_credit'];
	}
}

$seller_integral=get_integral($dbo,$t_integral,$seller_credit);
$seller_credit_num = $seller_credit_before+$seller_credit_sex;
$seller_credit_num_2 = $seller_credit_before_2+$seller_credit_sex_2;
$seller_credit_num_1 = $seller_credit_before_1+$seller_credit_sex_1;
$seller_credit_num_0 = $seller_credit_before_0+$seller_credit_sex_0;
$seller_percentage=0;
if($seller_credit_num!=0){
	$seller_percentage = sub_str($seller_credit_num_2/$seller_credit_num,6)*100;
}

$buyer_integral=get_integral($dbo,$t_integral,$buyer_credit);
$buyer_credit_num = $buyer_credit_before+$buyer_credit_sex;
$buyer_credit_num_2 = $buyer_credit_before_2+$buyer_credit_sex_2;
$buyer_credit_num_1 = $buyer_credit_before_1+$buyer_credit_sex_1;
$buyer_credit_num_0 = $buyer_credit_before_0+$buyer_credit_sex_0;
$buyer_percentage=0;
if($buyer_credit_num!=0){
	$buyer_percentage = sub_str($buyer_credit_num_2/$buyer_credit_num,6)*100;
}
//echo $sql;
//echo $buyer_credit_weed_2;
?>